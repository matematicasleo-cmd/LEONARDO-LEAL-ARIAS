package com.example.data

import com.example.data.local.AstroDao
import com.example.data.local.ChallengeSessionEntity
import com.example.data.local.CustomProblemEntity
import com.example.data.local.DailyMissionEntity
import com.example.data.local.MissionProgressEntity
import com.example.data.local.StudentProfileEntity
import com.example.data.local.TeacherSettingsEntity
import com.example.model.DAILY_MISSION_ID
import com.example.model.DifficultyLevel
import com.example.model.LabProblem
import com.example.model.MathOperation
import com.example.model.MathQuestion
import com.example.model.XpProgression
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ChallengeCompletionSummary(
    val accuracy: Int,
    val starsEarned: Int,
    val xpEarned: Int,
    val completedDailyJustNow: Boolean = false,
    val dailyBonusXp: Int = 0,
    val dailyBonusStars: Int = 0,
    val unlockedWeeklyBadgeTitle: String? = null,
    val previousLevel: Int = 1,
    val newLevel: Int = 1,
    val leveledUp: Boolean = false,
    val newRankTitle: String = XpProgression.rankForLevel(1)
)

class AstroRepository(
    private val dao: AstroDao,
    private val clockMs: () -> Long = { System.currentTimeMillis() }
) {

    val profileFlow: Flow<StudentProfileEntity> = dao.observeProfile().map { it ?: StudentProfileEntity() }
    val missionProgressFlow: Flow<List<MissionProgressEntity>> = dao.observeMissionProgress()
    val sessionsFlow: Flow<List<ChallengeSessionEntity>> = dao.observeSessions()
    val customProblemsFlow: Flow<List<CustomProblemEntity>> = dao.observeCustomProblems()
    val teacherSettingsFlow: Flow<TeacherSettingsEntity> = dao.observeTeacherSettings().map { it ?: TeacherSettingsEntity() }
    val dailyMissionFlow: Flow<DailyMissionEntity> = dao.observeDailyMission().map { it ?: buildDailyEntity(cycleIndex = 0, windowStart = clockMs()) }

    fun currentTimeMs(): Long = clockMs()

    suspend fun ensureSeeded() {
        val existingProfile = dao.getProfileOnce()
        if (existingProfile == null) {
            dao.upsertProfile(StudentProfileEntity())
        } else if (!existingProfile.hasCompletedOnboarding && existingProfile.gradeLevel == "4.º Primaria") {
            dao.upsertProfile(existingProfile.copy(gradeLevel = "5.º Primaria"))
        }
        if (dao.getTeacherSettingsOnce() == null) {
            dao.upsertTeacherSettings(TeacherSettingsEntity())
        }
        val firstMission = dao.getMissionProgressOnce(AstroContentCatalog.missions.first().id)
        if (firstMission == null) {
            val initialProgress = AstroContentCatalog.missions.mapIndexed { idx, mission ->
                MissionProgressEntity(
                    missionId = mission.id,
                    operationId = mission.operation.id,
                    starsEarned = if (idx == 0) 2 else 0,
                    bestAccuracy = if (idx == 0) 80 else 0,
                    attemptsCount = if (idx == 0) 1 else 0,
                    completed = idx == 0,
                    unlocked = idx <= 2,
                    lastPlayedAt = if (idx == 0) clockMs() else 0L
                )
            }
            dao.upsertAllMissionProgress(initialProgress)
        }
        refreshDailyMissionIfExpired()
    }

    suspend fun refreshDailyMissionIfExpired(): Boolean {
        val now = clockMs()
        val existing = dao.getDailyMissionOnce()
        if (existing == null || existing.expiresAtMs <= 0L) {
            dao.upsertDailyMission(
                buildDailyEntity(
                    cycleIndex = existing?.cycleIndex ?: 0,
                    windowStart = now,
                    lastCompletedAtMs = existing?.lastCompletedAtMs ?: 0L,
                    totalDailyCompleted = existing?.totalDailyCompleted ?: 0
                )
            )
            return true
        }
        if (now >= existing.expiresAtMs) {
            val cyclesElapsed = (((now - existing.expiresAtMs) / DAILY_WINDOW_MS) + 1L).toInt().coerceAtLeast(1)
            val nextCycleIndex = existing.cycleIndex + cyclesElapsed
            val newWindowStart = existing.expiresAtMs + ((cyclesElapsed - 1L) * DAILY_WINDOW_MS)

            // If a full 24h cycle elapsed without completing the mission after a previous completion, reset active streak
            val profile = dao.getProfileOnce()
            if (profile != null && existing.lastCompletedAtMs > 0L && (newWindowStart - existing.lastCompletedAtMs) > DAILY_WINDOW_MS) {
                dao.upsertProfile(
                    profile.copy(
                        streakDays = 0,
                        updatedAt = now
                    )
                )
            }

            dao.upsertDailyMission(
                buildDailyEntity(
                    cycleIndex = nextCycleIndex,
                    windowStart = newWindowStart,
                    lastCompletedAtMs = existing.lastCompletedAtMs,
                    totalDailyCompleted = existing.totalDailyCompleted
                )
            )
            return true
        }
        return false
    }

    private fun buildDailyEntity(
        cycleIndex: Int,
        windowStart: Long,
        lastCompletedAtMs: Long = 0L,
        totalDailyCompleted: Int = 0
    ): DailyMissionEntity {
        val catalog = AstroContentCatalog.dailyMissions
        val template = catalog[cycleIndex.mod(catalog.size)]
        return DailyMissionEntity(
            id = 1,
            cycleIndex = cycleIndex,
            templateId = template.id,
            planetCode = template.planetCode,
            title = template.title,
            subtitle = template.subtitle,
            operationId = template.operation.id,
            questionCount = template.questionCount,
            targetAccuracy = template.targetAccuracy,
            bonusXp = template.bonusXp,
            bonusStars = template.bonusStars,
            windowStartMs = windowStart,
            expiresAtMs = windowStart + DAILY_WINDOW_MS,
            attemptsToday = 0,
            bestAccuracy = 0,
            completed = false,
            lastCompletedAtMs = lastCompletedAtMs,
            totalDailyCompleted = totalDailyCompleted
        )
    }

    suspend fun saveProfile(
        studentName: String,
        gradeLevel: String,
        avatarId: String,
        difficultyId: String,
        markOnboarded: Boolean = true
    ) {
        val current = dao.getProfileOnce() ?: StudentProfileEntity()
        dao.upsertProfile(
            current.copy(
                studentName = studentName.trim().ifBlank { "Cadete Estelar" },
                gradeLevel = gradeLevel.trim().ifBlank { "5.º Primaria" },
                avatarId = avatarId,
                difficultyId = difficultyId,
                hasCompletedOnboarding = if (markOnboarded) true else current.hasCompletedOnboarding,
                updatedAt = clockMs()
            )
        )
    }

    suspend fun recordChallengeResult(
        operation: MathOperation,
        missionId: String?,
        correctCount: Int,
        totalCount: Int,
        durationSeconds: Int
    ): ChallengeCompletionSummary {
        val now = clockMs()
        val safeTotal = totalCount.coerceAtLeast(1)
        val accuracy = ((correctCount.toFloat() / safeTotal.toFloat()) * 100f).toInt().coerceIn(0, 100)
        val stars = when {
            accuracy >= 90 -> 3
            accuracy >= 70 -> 2
            accuracy >= 50 -> 1
            else -> 0
        }
        val baseXp = (correctCount * 15) + (stars * 20)

        dao.insertSession(
            ChallengeSessionEntity(
                operationId = operation.id,
                missionId = missionId ?: "libre_${operation.id}",
                correctCount = correctCount,
                totalCount = safeTotal,
                xpEarned = baseXp,
                starsEarned = stars,
                durationSeconds = durationSeconds,
                timestamp = now
            )
        )

        val profile = dao.getProfileOnce() ?: StudentProfileEntity()
        var starDelta = stars
        var xpDelta = baseXp
        var newStreakDays = profile.streakDays
        var completedDailyJustNow = false
        var dailyBonusXp = 0
        var dailyBonusStars = 0

        if (missionId == DAILY_MISSION_ID) {
            refreshDailyMissionIfExpired()
            val daily = dao.getDailyMissionOnce() ?: buildDailyEntity(0, now)
            val passedDaily = accuracy >= daily.targetAccuracy
            if (passedDaily && !daily.completed) {
                completedDailyJustNow = true
                dailyBonusXp = daily.bonusXp
                dailyBonusStars = daily.bonusStars
                xpDelta += dailyBonusXp
                starDelta += dailyBonusStars
                newStreakDays = if (daily.lastCompletedAtMs == 0L || (now - daily.lastCompletedAtMs) <= (2L * DAILY_WINDOW_MS)) {
                    profile.streakDays + 1
                } else {
                    1
                }
                dao.upsertDailyMission(
                    daily.copy(
                        attemptsToday = daily.attemptsToday + 1,
                        bestAccuracy = maxOf(daily.bestAccuracy, accuracy),
                        completed = true,
                        lastCompletedAtMs = now,
                        totalDailyCompleted = daily.totalDailyCompleted + 1
                    )
                )
            } else {
                dao.upsertDailyMission(
                    daily.copy(
                        attemptsToday = daily.attemptsToday + 1,
                        bestAccuracy = maxOf(daily.bestAccuracy, accuracy)
                    )
                )
            }
        } else if (missionId != null) {
            val existingMission = dao.getMissionProgressOnce(missionId)
                ?: MissionProgressEntity(missionId = missionId, operationId = operation.id, unlocked = true)
            val prevStars = existingMission.starsEarned
            val newStars = maxOf(prevStars, stars)
            starDelta = (newStars - prevStars).coerceAtLeast(0)
            val missionDef = AstroContentCatalog.missions.firstOrNull { it.id == missionId }
            val passed = accuracy >= (missionDef?.targetAccuracy ?: 70)

            dao.upsertMissionProgress(
                existingMission.copy(
                    starsEarned = newStars,
                    bestAccuracy = maxOf(existingMission.bestAccuracy, accuracy),
                    attemptsCount = existingMission.attemptsCount + 1,
                    completed = existingMission.completed || passed,
                    unlocked = true,
                    lastPlayedAt = now
                )
            )

            // Unlock next mission if passed
            if (passed && missionDef != null) {
                val nextMission = AstroContentCatalog.missions.firstOrNull { it.order == missionDef.order + 1 }
                if (nextMission != null) {
                    val nextProgress = dao.getMissionProgressOnce(nextMission.id)
                        ?: MissionProgressEntity(missionId = nextMission.id, operationId = nextMission.operation.id)
                    if (!nextProgress.unlocked) {
                        dao.upsertMissionProgress(nextProgress.copy(unlocked = true))
                    }
                }
            }
        }

        val prevBestStreak = maxOf(profile.bestStreakDays, profile.streakDays)
        val newBestStreak = maxOf(prevBestStreak, newStreakDays)
        val newlyUnlockedWeeklyBadge = if (newBestStreak > prevBestStreak) {
            AstroContentCatalog.badges
                .filter { it.isWeeklyBadge && it.targetStreakDays in (prevBestStreak + 1)..newBestStreak }
                .maxByOrNull { it.targetStreakDays }
        } else {
            null
        }

        val prevLevelInfo = XpProgression.fromTotalXp(profile.totalXp)
        val newTotalXp = profile.totalXp + xpDelta
        val newLevelInfo = XpProgression.fromTotalXp(newTotalXp)

        dao.upsertProfile(
            profile.copy(
                totalXp = newTotalXp,
                totalStars = profile.totalStars + starDelta,
                streakDays = newStreakDays,
                bestStreakDays = newBestStreak,
                updatedAt = now
            )
        )

        return ChallengeCompletionSummary(
            accuracy = accuracy,
            starsEarned = starDelta,
            xpEarned = xpDelta,
            completedDailyJustNow = completedDailyJustNow,
            dailyBonusXp = dailyBonusXp,
            dailyBonusStars = dailyBonusStars,
            unlockedWeeklyBadgeTitle = newlyUnlockedWeeklyBadge?.title,
            previousLevel = prevLevelInfo.level,
            newLevel = newLevelInfo.level,
            leveledUp = newLevelInfo.level > prevLevelInfo.level,
            newRankTitle = newLevelInfo.rankTitle
        )
    }

    suspend fun recordLabSuccess(operation: MathOperation, xpBonus: Int = 35): ChallengeCompletionSummary {
        val now = clockMs()
        dao.insertSession(
            ChallengeSessionEntity(
                operationId = operation.id,
                missionId = "lab_${operation.id}",
                correctCount = 1,
                totalCount = 1,
                xpEarned = xpBonus,
                starsEarned = 1,
                durationSeconds = 30,
                timestamp = now
            )
        )
        val profile = dao.getProfileOnce() ?: StudentProfileEntity()
        val prevLevel = XpProgression.fromTotalXp(profile.totalXp)
        val newTotalXp = profile.totalXp + xpBonus
        val newLevel = XpProgression.fromTotalXp(newTotalXp)
        dao.upsertProfile(
            profile.copy(
                totalXp = newTotalXp,
                totalStars = profile.totalStars + 1,
                updatedAt = now
            )
        )
        return ChallengeCompletionSummary(
            accuracy = 100,
            starsEarned = 1,
            xpEarned = xpBonus,
            previousLevel = prevLevel.level,
            newLevel = newLevel.level,
            leveledUp = newLevel.level > prevLevel.level,
            newRankTitle = newLevel.rankTitle
        )
    }

    suspend fun unlockOrEquipPerk(perkId: String, starCost: Int): Boolean {
        val now = clockMs()
        val profile = dao.getProfileOnce() ?: StudentProfileEntity()
        val unlockedSet = profile.unlockedPerksCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        return if (perkId in unlockedSet) {
            dao.upsertProfile(profile.copy(equippedPerkId = perkId, updatedAt = now))
            true
        } else if (profile.totalStars >= starCost) {
            unlockedSet.add(perkId)
            dao.upsertProfile(
                profile.copy(
                    totalStars = profile.totalStars - starCost,
                    unlockedPerksCsv = unlockedSet.joinToString(","),
                    equippedPerkId = perkId,
                    updatedAt = now
                )
            )
            true
        } else {
            false
        }
    }

    suspend fun updateTeacherSettings(
        focusOperationId: String,
        challengeTimeSeconds: Int,
        questionsPerChallenge: Int,
        hintsEnabled: Boolean,
        classroomGoalAccuracy: Int
    ) {
        dao.upsertTeacherSettings(
            TeacherSettingsEntity(
                id = 1,
                focusOperationId = focusOperationId,
                challengeTimeSeconds = challengeTimeSeconds.coerceIn(30, 180),
                questionsPerChallenge = questionsPerChallenge.coerceIn(4, 12),
                hintsEnabled = hintsEnabled,
                classroomGoalAccuracy = classroomGoalAccuracy.coerceIn(60, 100)
            )
        )
    }

    suspend fun addCustomProblem(
        title: String,
        promptText: String,
        operationId: String,
        answer: Int,
        hintText: String,
        isWordProblem: Boolean
    ) {
        dao.insertCustomProblem(
            CustomProblemEntity(
                title = title.trim(),
                promptText = promptText.trim(),
                operationId = operationId,
                answer = answer,
                hintText = hintText.trim().ifBlank { "Analiza los datos paso a paso antes de operar." },
                isWordProblem = isWordProblem,
                createdAt = clockMs()
            )
        )
    }

    suspend fun deleteCustomProblem(id: Int) {
        dao.deleteCustomProblem(id)
    }

    suspend fun resetStudentProgress() {
        val now = clockMs()
        dao.clearSessions()
        val current = dao.getProfileOnce() ?: StudentProfileEntity()
        dao.upsertProfile(
            current.copy(
                totalXp = 0,
                totalStars = 0,
                streakDays = 0,
                bestStreakDays = 0,
                unlockedPerksCsv = "propulsor_base",
                equippedPerkId = "propulsor_base",
                updatedAt = now
            )
        )
        val resetMissions = AstroContentCatalog.missions.mapIndexed { idx, m ->
            MissionProgressEntity(
                missionId = m.id,
                operationId = m.operation.id,
                starsEarned = 0,
                bestAccuracy = 0,
                attemptsCount = 0,
                completed = false,
                unlocked = idx == 0,
                lastPlayedAt = 0L
            )
        }
        dao.upsertAllMissionProgress(resetMissions)
        dao.upsertDailyMission(buildDailyEntity(cycleIndex = 0, windowStart = now, lastCompletedAtMs = 0L, totalDailyCompleted = 0))
    }

    companion object {
        const val DAILY_WINDOW_MS: Long = 24L * 60L * 60L * 1000L
    }

    fun buildChallengeSet(
        operation: MathOperation,
        difficulty: DifficultyLevel,
        count: Int,
        customProblems: List<CustomProblemEntity>
    ): List<MathQuestion> {
        val teacherQuestions = customProblems
            .filter { !it.isWordProblem && it.operationId == operation.id }
            .take(2)
            .map { custom ->
                MathQuestion(
                    id = "custom_q_${custom.id}",
                    expression = custom.promptText,
                    promptLabel = "Reto del Docente: ${custom.title}",
                    operation = operation,
                    correctAnswer = custom.answer,
                    options = AstroContentCatalog.buildDistractors(custom.answer),
                    hint = custom.hintText,
                    isTeacherCustom = true
                )
            }
        val remaining = (count - teacherQuestions.size).coerceAtLeast(1)
        val generated = AstroContentCatalog.generateChallengeQuestions(operation, difficulty, remaining)
        return teacherQuestions + generated
    }

    fun buildLabCatalog(customProblems: List<CustomProblemEntity>): List<LabProblem> {
        val teacherLabs = customProblems
            .filter { it.isWordProblem }
            .map { custom ->
                val op = MathOperation.fromId(custom.operationId)
                LabProblem(
                    id = "custom_lab_${custom.id}",
                    title = custom.title,
                    missionStory = custom.promptText,
                    dataChips = listOf("Asignado por docente", op.label),
                    operation = op,
                    equationHint = "Identifica la operación (${op.symbol}) y calcula el resultado",
                    answerOptions = AstroContentCatalog.buildDistractors(custom.answer),
                    correctAnswer = custom.answer,
                    unitLabel = "unidades",
                    botHint = custom.hintText,
                    isTeacherCustom = true
                )
            }
        return teacherLabs + AstroContentCatalog.labProblems
    }
}

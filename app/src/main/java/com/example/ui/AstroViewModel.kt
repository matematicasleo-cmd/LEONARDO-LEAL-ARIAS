package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AstroContentCatalog
import com.example.data.AstroRepository
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
import com.example.model.OperationAccuracyStat
import com.example.model.XpLevelInfo
import com.example.model.XpProgression
import com.example.model.formatDailyCountdown
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ChallengeSessionUiState(
    val isActive: Boolean = false,
    val isCompleted: Boolean = false,
    val operation: MathOperation = MathOperation.SUMA,
    val missionId: String? = null,
    val missionTitle: String = "Práctica Libre",
    val questions: List<MathQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val correctCount: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val shieldsLeft: Int = 3,
    val secondsRemaining: Int = 60,
    val totalSecondsAllowed: Int = 60,
    val selectedOption: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val showHint: Boolean = false,
    val isDailyMission: Boolean = false,
    val dailyTargetAccuracy: Int = 75,
    val dailyBonusXpEarned: Int = 0,
    val dailyBonusStarsEarned: Int = 0,
    val leveledUp: Boolean = false,
    val newLevelReached: Int = 1,
    val newRankTitle: String = ""
) {
    val currentQuestion: MathQuestion?
        get() = questions.getOrNull(currentIndex)
}

data class ProblemLabUiState(
    val problems: List<LabProblem> = AstroContentCatalog.labProblems,
    val currentIndex: Int = 0,
    val step: Int = 1, // 1: Datos, 2: Operación, 3: Solución
    val selectedOperation: MathOperation? = null,
    val operationFeedbackError: Boolean = false,
    val selectedAnswer: Int? = null,
    val isSolved: Boolean = false,
    val showBotHint: Boolean = false,
    val solvedProblemIds: Set<String> = emptySet()
) {
    val currentProblem: LabProblem?
        get() = problems.getOrNull(currentIndex)
}

data class AstroUiState(
    val profile: StudentProfileEntity = StudentProfileEntity(),
    val missionProgress: Map<String, MissionProgressEntity> = emptyMap(),
    val sessions: List<ChallengeSessionEntity> = emptyList(),
    val customProblems: List<CustomProblemEntity> = emptyList(),
    val teacherSettings: TeacherSettingsEntity = TeacherSettingsEntity(),
    val dailyMission: DailyMissionEntity = DailyMissionEntity(),
    val dailyRemainingMs: Long = AstroRepository.DAILY_WINDOW_MS,
    val selectedLearningOp: MathOperation = MathOperation.SUMA,
    val quickCheckSelected: Int? = null,
    val challenge: ChallengeSessionUiState = ChallengeSessionUiState(),
    val lab: ProblemLabUiState = ProblemLabUiState(),
    val snackbarMessage: String? = null
) {
    val dailyCountdownText: String
        get() = formatDailyCountdown(dailyRemainingMs)

    val levelInfo: XpLevelInfo
        get() = XpProgression.fromTotalXp(profile.totalXp)

    val studentRank: String
        get() = "Nv.${levelInfo.level} ${levelInfo.rankTitle}"

    val unlockedPerks: Set<String>
        get() = profile.unlockedPerksCsv.split(",").filter { it.isNotBlank() }.toSet()

    val completedMissionsCount: Int
        get() = missionProgress.values.count { it.completed }

    val overallAccuracyPercent: Int
        get() {
            if (sessions.isEmpty()) return 80
            val totalCorrect = sessions.sumOf { it.correctCount }
            val totalQ = sessions.sumOf { it.totalCount }.coerceAtLeast(1)
            return ((totalCorrect.toFloat() / totalQ) * 100f).toInt().coerceIn(0, 100)
        }

    val operationStats: List<OperationAccuracyStat>
        get() = MathOperation.entries.map { op ->
            val matching = sessions.filter { it.operationId == op.id }
            if (matching.isEmpty()) {
                val fallbackProgress = missionProgress.values.filter { it.operationId == op.id && it.bestAccuracy > 0 }
                val avg = if (fallbackProgress.isEmpty()) 0 else fallbackProgress.map { it.bestAccuracy }.average().toInt()
                OperationAccuracyStat(op, avg, fallbackProgress.size)
            } else {
                val c = matching.sumOf { it.correctCount }
                val t = matching.sumOf { it.totalCount }.coerceAtLeast(1)
                OperationAccuracyStat(op, ((c.toFloat() / t) * 100f).toInt(), matching.size)
            }
        }

    val unlockedBadgeIds: Set<String>
        get() = buildSet {
            val effectiveMaxStreak = maxOf(profile.streakDays, profile.bestStreakDays)
            if (completedMissionsCount >= 1) add("badge_first_flight")
            if (profile.totalStars >= 10 || missionProgress.values.sumOf { it.starsEarned } >= 10) add("badge_star_collector")
            if (sessions.any { it.totalCount > 0 && (it.correctCount * 100 / it.totalCount) >= 90 }) add("badge_precision")
            if (profile.totalXp >= 300) add("badge_explorer_xp")
            if (dailyMission.completed || dailyMission.totalDailyCompleted >= 1) add("badge_daily_streak")
            AstroContentCatalog.badges
                .filter { it.isWeeklyBadge && effectiveMaxStreak >= it.targetStreakDays }
                .forEach { add(it.id) }
            unlockedPerks.forEach { add(it) }
        }

    val recommendedOperation: MathOperation
        get() {
            val weakest = operationStats
                .filter { it.sessionsCount > 0 }
                .minByOrNull { it.accuracyPercent }
            return weakest?.operation ?: MathOperation.fromId(teacherSettings.focusOperationId)
        }
}

class AstroViewModel(private val repository: AstroRepository) : ViewModel() {

    private val _localState = MutableStateFlow(LocalInteractionState())
    private val _nowMs = MutableStateFlow(repository.currentTimeMs())
    private var timerJob: Job? = null

    private data class LocalInteractionState(
        val selectedLearningOp: MathOperation = MathOperation.SUMA,
        val quickCheckSelected: Int? = null,
        val challenge: ChallengeSessionUiState = ChallengeSessionUiState(),
        val lab: ProblemLabUiState = ProblemLabUiState(),
        val snackbarMessage: String? = null
    )

    private val dbCombinedFlow = combine(
        repository.profileFlow,
        repository.missionProgressFlow,
        repository.sessionsFlow,
        repository.customProblemsFlow,
        repository.teacherSettingsFlow
    ) { profile, missions, sessions, customProblems, teacherSettings ->
        DbBundle(profile, missions, sessions, customProblems, teacherSettings)
    }

    private data class DbBundle(
        val profile: StudentProfileEntity,
        val missions: List<MissionProgressEntity>,
        val sessions: List<ChallengeSessionEntity>,
        val customProblems: List<CustomProblemEntity>,
        val teacherSettings: TeacherSettingsEntity
    )

    val uiState: StateFlow<AstroUiState> = combine(
        dbCombinedFlow,
        repository.dailyMissionFlow,
        _localState,
        _nowMs
    ) { db, dailyMission, local, nowMs ->
        val labList = repository.buildLabCatalog(db.customProblems)
        val safeLabIndex = local.lab.currentIndex.coerceIn(0, (labList.size - 1).coerceAtLeast(0))
        val remainingMs = if (dailyMission.expiresAtMs > 0L) {
            (dailyMission.expiresAtMs - nowMs).coerceAtLeast(0L)
        } else {
            AstroRepository.DAILY_WINDOW_MS
        }
        AstroUiState(
            profile = db.profile,
            missionProgress = db.missions.associateBy { it.missionId },
            sessions = db.sessions,
            customProblems = db.customProblems,
            teacherSettings = db.teacherSettings,
            dailyMission = dailyMission,
            dailyRemainingMs = remainingMs,
            selectedLearningOp = local.selectedLearningOp,
            quickCheckSelected = local.quickCheckSelected,
            challenge = local.challenge,
            lab = local.lab.copy(problems = labList, currentIndex = safeLabIndex),
            snackbarMessage = local.snackbarMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AstroUiState()
    )

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            _nowMs.value = repository.currentTimeMs()
        }
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val now = repository.currentTimeMs()
                _nowMs.value = now
                val expiresAt = uiState.value.dailyMission.expiresAtMs
                if (expiresAt > 0L && now >= expiresAt) {
                    val refreshed = repository.refreshDailyMissionIfExpired()
                    if (refreshed) {
                        _localState.update {
                            it.copy(snackbarMessage = "¡Nueva Misión Cósmica Diaria disponible!")
                        }
                    }
                }
            }
        }
    }

    fun clearSnackbar() {
        _localState.update { it.copy(snackbarMessage = null) }
    }

    fun saveStudentProfile(
        name: String,
        grade: String,
        avatarId: String,
        difficultyId: String
    ) {
        viewModelScope.launch {
            repository.saveProfile(name, grade, avatarId, difficultyId, markOnboarded = true)
            _localState.update { it.copy(snackbarMessage = "Perfil espacial guardado") }
        }
    }

    fun selectLearningOperation(operation: MathOperation) {
        _localState.update {
            it.copy(
                selectedLearningOp = operation,
                quickCheckSelected = null
            )
        }
    }

    fun selectQuickCheckOption(option: Int) {
        _localState.update { it.copy(quickCheckSelected = option) }
    }

    fun startDailyMissionChallenge() {
        viewModelScope.launch {
            repository.refreshDailyMissionIfExpired()
            val state = uiState.value
            val daily = state.dailyMission
            val op = MathOperation.fromId(daily.operationId)
            startChallengeInternal(
                operation = op,
                missionId = DAILY_MISSION_ID,
                missionTitle = "Misión Diaria: ${daily.title}",
                overrideQuestionCount = daily.questionCount,
                isDailyMission = true,
                dailyTargetAccuracy = daily.targetAccuracy
            )
        }
    }

    fun startChallenge(
        operation: MathOperation,
        missionId: String? = null,
        missionTitle: String = "Práctica ${operation.label}"
    ) {
        startChallengeInternal(
            operation = operation,
            missionId = missionId,
            missionTitle = missionTitle
        )
    }

    private fun startChallengeInternal(
        operation: MathOperation,
        missionId: String?,
        missionTitle: String,
        overrideQuestionCount: Int? = null,
        isDailyMission: Boolean = false,
        dailyTargetAccuracy: Int = 75
    ) {
        timerJob?.cancel()
        val state = uiState.value
        val difficulty = DifficultyLevel.fromId(state.profile.difficultyId)
        val count = overrideQuestionCount ?: state.teacherSettings.questionsPerChallenge
        val timeLimit = state.teacherSettings.challengeTimeSeconds
        val questions = repository.buildChallengeSet(
            operation = operation,
            difficulty = difficulty,
            count = count,
            customProblems = state.customProblems
        )
        _localState.update {
            it.copy(
                challenge = ChallengeSessionUiState(
                    isActive = true,
                    isCompleted = false,
                    operation = operation,
                    missionId = missionId,
                    missionTitle = missionTitle,
                    questions = questions,
                    currentIndex = 0,
                    correctCount = 0,
                    streak = 0,
                    bestStreak = 0,
                    shieldsLeft = 3,
                    secondsRemaining = timeLimit,
                    totalSecondsAllowed = timeLimit,
                    selectedOption = null,
                    isAnswerSubmitted = false,
                    showHint = false,
                    isDailyMission = isDailyMission,
                    dailyTargetAccuracy = dailyTargetAccuracy,
                    dailyBonusXpEarned = 0,
                    dailyBonusStarsEarned = 0
                )
            )
        }
        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _localState.value.challenge
                if (!current.isActive || current.isCompleted) break
                if (current.secondsRemaining <= 1) {
                    finishChallenge(timedOut = true)
                    break
                } else {
                    _localState.update {
                        it.copy(challenge = it.challenge.copy(secondsRemaining = it.challenge.secondsRemaining - 1))
                    }
                }
            }
        }
    }

    fun submitChallengeAnswer(option: Int) {
        val current = _localState.value.challenge
        val question = current.currentQuestion ?: return
        if (current.isAnswerSubmitted || current.isCompleted) return

        val isCorrect = option == question.correctAnswer
        val newCorrect = if (isCorrect) current.correctCount + 1 else current.correctCount
        val newStreak = if (isCorrect) current.streak + 1 else 0
        val newBest = maxOf(current.bestStreak, newStreak)
        val newShields = if (isCorrect) current.shieldsLeft else (current.shieldsLeft - 1).coerceAtLeast(0)

        _localState.update {
            it.copy(
                challenge = current.copy(
                    selectedOption = option,
                    isAnswerSubmitted = true,
                    correctCount = newCorrect,
                    streak = newStreak,
                    bestStreak = newBest,
                    shieldsLeft = newShields
                )
            )
        }
    }

    fun toggleChallengeHint() {
        _localState.update {
            it.copy(challenge = it.challenge.copy(showHint = !it.challenge.showHint))
        }
    }

    fun nextChallengeQuestion() {
        val current = _localState.value.challenge
        val isLast = current.currentIndex >= current.questions.lastIndex
        if (isLast || current.shieldsLeft <= 0) {
            finishChallenge(timedOut = false)
        } else {
            _localState.update {
                it.copy(
                    challenge = current.copy(
                        currentIndex = current.currentIndex + 1,
                        selectedOption = null,
                        isAnswerSubmitted = false,
                        showHint = false
                    )
                )
            }
        }
    }

    private fun finishChallenge(timedOut: Boolean) {
        timerJob?.cancel()
        val current = _localState.value.challenge
        if (current.isCompleted) return

        val elapsed = (current.totalSecondsAllowed - current.secondsRemaining).coerceAtLeast(1)
        _localState.update {
            it.copy(
                challenge = current.copy(
                    isActive = false,
                    isCompleted = true,
                    secondsRemaining = if (timedOut) 0 else current.secondsRemaining
                )
            )
        }

        viewModelScope.launch {
            val summary = repository.recordChallengeResult(
                operation = current.operation,
                missionId = current.missionId,
                correctCount = current.correctCount,
                totalCount = current.questions.size.coerceAtLeast(1),
                durationSeconds = elapsed
            )
            val snackbarParts = buildList {
                if (summary.completedDailyJustNow) {
                    val badgeMsg = summary.unlockedWeeklyBadgeTitle?.let { " • ¡Insignia semanal: $it!" } ?: ""
                    add("¡Misión Cósmica Diaria completada! +${summary.dailyBonusXp} XP y +${summary.dailyBonusStars} ★$badgeMsg")
                }
                if (summary.leveledUp) {
                    add("¡Subiste al Nivel ${summary.newLevel}: ${summary.newRankTitle}!")
                }
            }
            if (summary.completedDailyJustNow || summary.leveledUp) {
                _localState.update { state ->
                    state.copy(
                        challenge = state.challenge.copy(
                            dailyBonusXpEarned = summary.dailyBonusXp,
                            dailyBonusStarsEarned = summary.dailyBonusStars,
                            leveledUp = summary.leveledUp,
                            newLevelReached = summary.newLevel,
                            newRankTitle = summary.newRankTitle
                        ),
                        snackbarMessage = snackbarParts.joinToString(" • ").ifBlank { state.snackbarMessage }
                    )
                }
            }
        }
    }

    fun resetChallengeToMenu() {
        timerJob?.cancel()
        _localState.update { it.copy(challenge = ChallengeSessionUiState()) }
    }

    // Problem Lab actions
    fun selectLabProblem(index: Int) {
        _localState.update {
            it.copy(
                lab = it.lab.copy(
                    currentIndex = index,
                    step = 1,
                    selectedOperation = null,
                    operationFeedbackError = false,
                    selectedAnswer = null,
                    isSolved = false,
                    showBotHint = false
                )
            )
        }
    }

    fun advanceLabToStep2() {
        _localState.update { it.copy(lab = it.lab.copy(step = 2)) }
    }

    fun chooseLabOperation(op: MathOperation) {
        val problem = uiState.value.lab.currentProblem ?: return
        if (op == problem.operation) {
            _localState.update {
                it.copy(
                    lab = it.lab.copy(
                        selectedOperation = op,
                        operationFeedbackError = false,
                        step = 3
                    )
                )
            }
        } else {
            _localState.update {
                it.copy(
                    lab = it.lab.copy(
                        selectedOperation = op,
                        operationFeedbackError = true
                    )
                )
            }
        }
    }

    fun submitLabAnswer(answer: Int) {
        val problem = uiState.value.lab.currentProblem ?: return
        val wasSolved = uiState.value.lab.isSolved
        val correct = answer == problem.correctAnswer
        _localState.update {
            it.copy(
                lab = it.lab.copy(
                    selectedAnswer = answer,
                    isSolved = correct,
                    solvedProblemIds = if (correct) it.lab.solvedProblemIds + problem.id else it.lab.solvedProblemIds
                )
            )
        }
        if (correct && !wasSolved) {
            viewModelScope.launch {
                val summary = repository.recordLabSuccess(problem.operation)
                if (summary.leveledUp) {
                    _localState.update {
                        it.copy(snackbarMessage = "¡Subiste al Nivel ${summary.newLevel}: ${summary.newRankTitle}!")
                    }
                }
            }
        }
    }

    fun toggleLabHint() {
        _localState.update { it.copy(lab = it.lab.copy(showBotHint = !it.lab.showBotHint)) }
    }

    // Rewards actions
    fun unlockOrEquipPerk(perkId: String, starCost: Int, perkTitle: String) {
        viewModelScope.launch {
            val ok = repository.unlockOrEquipPerk(perkId, starCost)
            _localState.update {
                it.copy(
                    snackbarMessage = if (ok) {
                        "Equipado: $perkTitle"
                    } else {
                        "Necesitas $starCost estrellas para desbloquear esta mejora"
                    }
                )
            }
        }
    }

    // Teacher Panel actions
    fun updateTeacherSettings(
        focusOperationId: String,
        challengeTimeSeconds: Int,
        questionsPerChallenge: Int,
        hintsEnabled: Boolean,
        classroomGoalAccuracy: Int
    ) {
        viewModelScope.launch {
            repository.updateTeacherSettings(
                focusOperationId = focusOperationId,
                challengeTimeSeconds = challengeTimeSeconds,
                questionsPerChallenge = questionsPerChallenge,
                hintsEnabled = hintsEnabled,
                classroomGoalAccuracy = classroomGoalAccuracy
            )
            _localState.update { it.copy(snackbarMessage = "Configuración pedagógica actualizada") }
        }
    }

    fun addCustomTeacherProblem(
        title: String,
        promptText: String,
        operationId: String,
        answer: Int,
        hintText: String,
        isWordProblem: Boolean
    ) {
        viewModelScope.launch {
            repository.addCustomProblem(title, promptText, operationId, answer, hintText, isWordProblem)
            _localState.update { it.copy(snackbarMessage = "Actividad agregada al catálogo del estudiante") }
        }
    }

    fun deleteCustomProblem(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomProblem(id)
            _localState.update { it.copy(snackbarMessage = "Actividad personalizada eliminada") }
        }
    }

    fun resetStudentProgress() {
        viewModelScope.launch {
            repository.resetStudentProgress()
            _localState.update { it.copy(snackbarMessage = "Progreso del estudiante reiniciado") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    class Factory(private val repository: AstroRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AstroViewModel(repository) as T
        }
    }
}

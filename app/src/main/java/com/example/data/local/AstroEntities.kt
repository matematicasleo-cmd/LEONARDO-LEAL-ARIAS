package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val studentName: String = "Cadete Leo",
    val gradeLevel: String = "5.º Primaria",
    val avatarId: String = "nova",
    val difficultyId: String = "explorador",
    val totalXp: Int = 120,
    val totalStars: Int = 4,
    val streakDays: Int = 3,
    val bestStreakDays: Int = 3,
    val unlockedPerksCsv: String = "propulsor_base",
    val equippedPerkId: String = "propulsor_base",
    val hasCompletedOnboarding: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "mission_progress")
data class MissionProgressEntity(
    @PrimaryKey val missionId: String,
    val operationId: String,
    val starsEarned: Int = 0,
    val bestAccuracy: Int = 0,
    val attemptsCount: Int = 0,
    val completed: Boolean = false,
    val unlocked: Boolean = false,
    val lastPlayedAt: Long = 0L
)

@Entity(tableName = "challenge_sessions")
data class ChallengeSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val operationId: String,
    val missionId: String,
    val correctCount: Int,
    val totalCount: Int,
    val xpEarned: Int,
    val starsEarned: Int,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_problems")
data class CustomProblemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val promptText: String,
    val operationId: String,
    val answer: Int,
    val hintText: String,
    val isWordProblem: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "teacher_settings")
data class TeacherSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val focusOperationId: String = "suma",
    val challengeTimeSeconds: Int = 60,
    val questionsPerChallenge: Int = 6,
    val hintsEnabled: Boolean = true,
    val classroomGoalAccuracy: Int = 80
)

@Entity(tableName = "daily_mission")
data class DailyMissionEntity(
    @PrimaryKey val id: Int = 1,
    val cycleIndex: Int = 0,
    val templateId: String = "daily_1",
    val planetCode: String = "DIA-01",
    val title: String = "Tormenta Solar de Sumas",
    val subtitle: String = "Calcula sumas rápidas para estabilizar los paneles solares.",
    val operationId: String = "suma",
    val questionCount: Int = 5,
    val targetAccuracy: Int = 75,
    val bonusXp: Int = 90,
    val bonusStars: Int = 2,
    val windowStartMs: Long = 0L,
    val expiresAtMs: Long = 0L,
    val attemptsToday: Int = 0,
    val bestAccuracy: Int = 0,
    val completed: Boolean = false,
    val lastCompletedAtMs: Long = 0L,
    val totalDailyCompleted: Int = 0
)


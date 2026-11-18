package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AstroDao {
    @Query("SELECT * FROM student_profile WHERE id = 1")
    fun observeProfile(): Flow<StudentProfileEntity?>

    @Query("SELECT * FROM student_profile WHERE id = 1")
    suspend fun getProfileOnce(): StudentProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: StudentProfileEntity)

    @Query("SELECT * FROM mission_progress")
    fun observeMissionProgress(): Flow<List<MissionProgressEntity>>

    @Query("SELECT * FROM mission_progress WHERE missionId = :missionId")
    suspend fun getMissionProgressOnce(missionId: String): MissionProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMissionProgress(progress: MissionProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllMissionProgress(list: List<MissionProgressEntity>)

    @Query("SELECT * FROM challenge_sessions ORDER BY timestamp DESC")
    fun observeSessions(): Flow<List<ChallengeSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChallengeSessionEntity)

    @Query("DELETE FROM challenge_sessions")
    suspend fun clearSessions()

    @Query("SELECT * FROM custom_problems ORDER BY createdAt DESC")
    fun observeCustomProblems(): Flow<List<CustomProblemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomProblem(problem: CustomProblemEntity)

    @Query("DELETE FROM custom_problems WHERE id = :id")
    suspend fun deleteCustomProblem(id: Int)

    @Query("SELECT * FROM teacher_settings WHERE id = 1")
    fun observeTeacherSettings(): Flow<TeacherSettingsEntity?>

    @Query("SELECT * FROM teacher_settings WHERE id = 1")
    suspend fun getTeacherSettingsOnce(): TeacherSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTeacherSettings(settings: TeacherSettingsEntity)

    @Query("SELECT * FROM daily_mission WHERE id = 1")
    fun observeDailyMission(): Flow<DailyMissionEntity?>

    @Query("SELECT * FROM daily_mission WHERE id = 1")
    suspend fun getDailyMissionOnce(): DailyMissionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyMission(mission: DailyMissionEntity)
}


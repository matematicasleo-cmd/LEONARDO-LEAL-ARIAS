package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        StudentProfileEntity::class,
        MissionProgressEntity::class,
        ChallengeSessionEntity::class,
        CustomProblemEntity::class,
        TeacherSettingsEntity::class,
        DailyMissionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AstroDatabase : RoomDatabase() {
    abstract fun astroDao(): AstroDao

    companion object {
        @Volatile
        private var INSTANCE: AstroDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `daily_mission` (
                        `id` INTEGER NOT NULL,
                        `cycleIndex` INTEGER NOT NULL,
                        `templateId` TEXT NOT NULL,
                        `planetCode` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `subtitle` TEXT NOT NULL,
                        `operationId` TEXT NOT NULL,
                        `questionCount` INTEGER NOT NULL,
                        `targetAccuracy` INTEGER NOT NULL,
                        `bonusXp` INTEGER NOT NULL,
                        `bonusStars` INTEGER NOT NULL,
                        `windowStartMs` INTEGER NOT NULL,
                        `expiresAtMs` INTEGER NOT NULL,
                        `attemptsToday` INTEGER NOT NULL,
                        `bestAccuracy` INTEGER NOT NULL,
                        `completed` INTEGER NOT NULL,
                        `lastCompletedAtMs` INTEGER NOT NULL,
                        `totalDailyCompleted` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `student_profile` ADD COLUMN `bestStreakDays` INTEGER NOT NULL DEFAULT 3"
                )
            }
        }

        fun getInstance(context: Context): AstroDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AstroDatabase::class.java,
                    "astromate_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}


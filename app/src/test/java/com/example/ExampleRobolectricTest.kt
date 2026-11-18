package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AstroContentCatalog
import com.example.data.AstroRepository
import com.example.data.local.AstroDatabase
import com.example.model.DAILY_MISSION_ID
import com.example.model.DifficultyLevel
import com.example.model.MathOperation
import com.example.model.XpProgression
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AstroMate", appName)
    }

    @Test
    fun `pedagogical catalog and challenge generator are consistent`() {
        MathOperation.entries.forEach { op ->
            assertTrue("Missing lesson for ${op.id}", AstroContentCatalog.lessons.containsKey(op))
            val questions = AstroContentCatalog.generateChallengeQuestions(op, DifficultyLevel.PILOTO, 5)
            assertEquals(5, questions.size)
            questions.forEach { q ->
                assertEquals(4, q.options.size)
                assertTrue(q.correctAnswer in q.options)
            }
        }
        assertEquals(10, AstroContentCatalog.missions.size)
        assertEquals(7, AstroContentCatalog.dailyMissions.size)
    }

    @Test
    fun `daily cosmic mission refreshes every 24 hours and updates streak`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AstroDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            var fakeNowMs = 1_700_000_000_000L
            val repo = AstroRepository(db.astroDao(), clockMs = { fakeNowMs })
            repo.ensureSeeded()

            val day0 = repo.dailyMissionFlow.first()
            assertEquals(0, day0.cycleIndex)
            assertEquals("daily_1", day0.templateId)
            assertEquals(fakeNowMs + AstroRepository.DAILY_WINDOW_MS, day0.expiresAtMs)
            assertFalse(day0.completed)

            // Before 24 hours pass, mission should NOT refresh
            fakeNowMs += AstroRepository.DAILY_WINDOW_MS - 1_000L
            assertFalse(repo.refreshDailyMissionIfExpired())

            // Complete Day 0 Daily Cosmic Mission
            val initialProfile = repo.profileFlow.first()
            val summary = repo.recordChallengeResult(
                operation = MathOperation.fromId(day0.operationId),
                missionId = DAILY_MISSION_ID,
                correctCount = 5,
                totalCount = 5,
                durationSeconds = 25
            )
            assertTrue(summary.completedDailyJustNow)
            assertEquals(day0.bonusXp, summary.dailyBonusXp)
            assertEquals(day0.bonusStars, summary.dailyBonusStars)

            val afterCompleteDaily = repo.dailyMissionFlow.first()
            val afterCompleteProfile = repo.profileFlow.first()
            assertTrue(afterCompleteDaily.completed)
            assertEquals(1, afterCompleteDaily.totalDailyCompleted)
            assertEquals(initialProfile.streakDays + 1, afterCompleteProfile.streakDays)

            // Advance clock across the 24-hour boundary -> rotates to Day 1 mission
            fakeNowMs += 2_000L
            assertTrue(repo.refreshDailyMissionIfExpired())
            val day1 = repo.dailyMissionFlow.first()
            assertEquals(1, day1.cycleIndex)
            assertNotEquals(day0.templateId, day1.templateId)
            assertFalse(day1.completed)
            assertEquals(1, day1.totalDailyCompleted)

            // Complete 3 more consecutive daily cycles (streak reaches 7) -> unlocks 7-day weekly badge
            var unlockedWeekBadge: String? = null
            for (step in 1..3) {
                val res = repo.recordChallengeResult(
                    operation = MathOperation.fromId(repo.dailyMissionFlow.first().operationId),
                    missionId = DAILY_MISSION_ID,
                    correctCount = 5,
                    totalCount = 5,
                    durationSeconds = 20
                )
                if (res.unlockedWeeklyBadgeTitle != null) {
                    unlockedWeekBadge = res.unlockedWeeklyBadgeTitle
                }
                fakeNowMs += AstroRepository.DAILY_WINDOW_MS
                repo.refreshDailyMissionIfExpired()
            }
            val weekProfile = repo.profileFlow.first()
            assertEquals(7, weekProfile.streakDays)
            assertEquals(7, weekProfile.bestStreakDays)
            assertEquals("Semana Galáctica Perfecta", unlockedWeekBadge)

            // Skipping an entire 24h cycle without completing resets active streak but preserves bestStreakDays
            fakeNowMs += AstroRepository.DAILY_WINDOW_MS * 2L
            assertTrue(repo.refreshDailyMissionIfExpired())
            val afterMissedProfile = repo.profileFlow.first()
            assertEquals(0, afterMissedProfile.streakDays)
            assertEquals(7, afterMissedProfile.bestStreakDays)
        } finally {
            db.close()
        }
    }

    @Test
    fun `xp progression system calculates levels and awards level up after challenge`() = runTest {
        val lvl1 = XpProgression.fromTotalXp(0)
        assertEquals(1, lvl1.level)
        assertEquals(0, lvl1.xpInCurrentLevel)
        assertEquals(100, lvl1.xpRequiredForNextLevel)

        val lvl2 = XpProgression.fromTotalXp(120)
        assertEquals(2, lvl2.level)
        assertEquals("Explorador Orbital", lvl2.rankTitle)
        assertEquals(20, lvl2.xpInCurrentLevel)
        assertEquals(150, lvl2.xpRequiredForNextLevel)
        assertEquals(130, lvl2.xpRemainingToNextLevel)

        val lvl3 = XpProgression.fromTotalXp(250)
        assertEquals(3, lvl3.level)
        assertEquals("Navegante Estelar", lvl3.rankTitle)
        assertEquals(0, lvl3.xpInCurrentLevel)
        assertEquals(200, lvl3.xpRequiredForNextLevel)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AstroDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repo = AstroRepository(db.astroDao())
            repo.ensureSeeded()
            // Initial seeded XP is 120 (Level 2). Winning 5/5 gives (5*15)+(3*20) = 135 XP -> 255 XP (Level 3!)
            val summary = repo.recordChallengeResult(
                operation = MathOperation.SUMA,
                missionId = "m1_suma",
                correctCount = 5,
                totalCount = 5,
                durationSeconds = 22
            )
            assertEquals(2, summary.previousLevel)
            assertEquals(3, summary.newLevel)
            assertTrue(summary.leveledUp)
            assertEquals("Navegante Estelar", summary.newRankTitle)
        } finally {
            db.close()
        }
    }

    @Test
    fun `navigation across all eight AstroMate screens works`() {
        composeRule.waitForIdle()
        // 1. Bienvenida y Perfil, XP level progress bar & Días de Misión Consecutivos counter
        composeRule.onNodeWithText("Bienvenido a la Expedición Numérica").assertIsDisplayed()
        composeRule.onNodeWithTag("welcome_xp_progress_card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("welcome_xp_progress_bar").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("profile_consecutive_days_card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("profile_consecutive_days_counter").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Días de Misión Consecutivos").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("profile_grade_input").performScrollTo()
        composeRule.onNodeWithText("5.º Primaria").assertIsDisplayed()
        composeRule.onNodeWithTag("save_profile_launch_button").performScrollTo().performClick()
        composeRule.waitForIdle()

        // 2. Mapa de Misiones, XP level progress bar & Daily Cosmic Mission Card
        composeRule.onNodeWithText("Mapa de Misiones").assertIsDisplayed()
        composeRule.onNodeWithTag("missions_lazy_list").performScrollToNode(hasTestTag("missions_xp_progress_card"))
        composeRule.onNodeWithTag("missions_xp_progress_card").assertIsDisplayed()
        composeRule.onNodeWithTag("missions_lazy_list").performScrollToNode(hasTestTag("daily_mission_card"))
        composeRule.onNodeWithTag("daily_mission_card").assertIsDisplayed()

        // 3. Zona de Aprendizaje
        composeRule.onNodeWithTag("nav_tab_learning").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Zona de Aprendizaje").assertIsDisplayed()

        // 4. Reto Matemático (also test launching Daily Mission directly from Mapa)
        composeRule.onNodeWithTag("nav_tab_missions").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("missions_lazy_list").performScrollToNode(hasTestTag("daily_mission_card"))
        composeRule.onNodeWithTag("daily_mission_launch_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("MISIÓN CÓSMICA DIARIA").assertIsDisplayed()

        // 5. Laboratorio de Problemas
        composeRule.onNodeWithTag("nav_tab_lab").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Laboratorio de Problemas").assertIsDisplayed()

        // 6. Mi Progreso & Daily Streak Card
        composeRule.onNodeWithTag("nav_tab_progress").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Mi Progreso").assertIsDisplayed()
        composeRule.onNodeWithTag("progress_daily_streak_card").performScrollTo().assertIsDisplayed()

        // 7. Recompensas & Weekly Consistency Badges
        composeRule.onNodeWithTag("top_bar_rewards_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Recompensas y Rangos").assertIsDisplayed()
        composeRule.onNodeWithTag("rewards_weekly_badges_card").performScrollTo().assertIsDisplayed()

        // 8. Panel del Docente & Sound Toggle
        composeRule.onNodeWithTag("top_bar_sound_toggle").performClick()
        composeRule.onNodeWithTag("top_bar_teacher_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Centro de Control Educativo", ignoreCase = true).assertIsDisplayed()
        composeRule.onNodeWithText("Parámetros del Reto Matemático").performScrollTo().assertIsDisplayed()
    }
}


package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AstralBlue
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.SuccessEmerald

enum class MathOperation(
    val id: String,
    val label: String,
    val symbol: String,
    val sectorName: String,
    val pedagogicalGoal: String,
    val accentColor: Color
) {
    SUMA(
        id = "suma",
        label = "Suma",
        symbol = "+",
        sectorName = "Órbita de la Suma",
        pedagogicalGoal = "Combinar cantidades, descomponer en decenas y sumar con precisión mental.",
        accentColor = NeonCyan
    ),
    RESTA(
        id = "resta",
        label = "Resta",
        symbol = "−",
        sectorName = "Cinturón de la Resta",
        pedagogicalGoal = "Calcular diferencias, retroceder en la recta numérica y dominar el préstamo.",
        accentColor = AstralBlue
    ),
    MULTIPLICACION(
        id = "multiplicacion",
        label = "Multiplicación",
        symbol = "×",
        sectorName = "Nebulosa Multiplicadora",
        pedagogicalGoal = "Comprender grupos iguales, tablas rápidas y propiedad distributiva.",
        accentColor = CosmicPurple
    ),
    DIVISION(
        id = "division",
        label = "División",
        symbol = "÷",
        sectorName = "Estación División",
        pedagogicalGoal = "Repartir en partes exactas y relacionar la división con la multiplicación.",
        accentColor = SuccessEmerald
    ),
    COMBINADAS(
        id = "combinadas",
        label = "Combinadas",
        symbol = "★",
        sectorName = "Galaxia Combinada",
        pedagogicalGoal = "Aplicar la jerarquía de operaciones en retos espaciales mixtos.",
        accentColor = SolarYellow
    );

    companion object {
        fun fromId(id: String): MathOperation =
            entries.firstOrNull { it.id == id } ?: SUMA
    }
}

enum class SpaceAvatar(
    val id: String,
    val displayName: String,
    val title: String,
    val accentColor: Color
) {
    COMANDANTE_NOVA("nova", "Nova", "Comandante Orbital", CosmicPurple),
    PILOTO_ORION("orion", "Orión", "Piloto Estelar", NeonCyan),
    INGENIERA_VEGA("vega", "Vega", "Ingeniera Cuántica", SolarYellow),
    ASTROBOT_X7("astrobot", "AstroBot X-7", "Guía Matemático", AstralBlue);

    companion object {
        fun fromId(id: String): SpaceAvatar =
            entries.firstOrNull { it.id == id } ?: COMANDANTE_NOVA
    }
}

enum class DifficultyLevel(
    val id: String,
    val label: String,
    val gradeHint: String,
    val maxOperand: Int
) {
    EXPLORADOR("explorador", "Explorador", "3.º - 4.º Primaria", 25),
    PILOTO("piloto", "Piloto", "4.º - 5.º Primaria", 60),
    COMANDANTE("comandante", "Comandante", "5.º - 6.º Primaria", 120);

    companion object {
        fun fromId(id: String): DifficultyLevel =
            entries.firstOrNull { it.id == id } ?: EXPLORADOR
    }
}

data class MissionDefinition(
    val id: String,
    val order: Int,
    val title: String,
    val subtitle: String,
    val operation: MathOperation,
    val xpReward: Int,
    val targetAccuracy: Int,
    val planetCode: String
)

data class LearningLesson(
    val operation: MathOperation,
    val headline: String,
    val conceptSummary: String,
    val visualTitle: String,
    val leftGroupCount: Int,
    val rightGroupCount: Int,
    val visualCaption: String,
    val starTrickTitle: String,
    val starTrickBody: String,
    val stepByStepExample: List<String>,
    val quickCheckPrompt: String,
    val quickCheckOptions: List<Int>,
    val quickCheckAnswer: Int,
    val quickCheckExplanation: String
)

data class MathQuestion(
    val id: String,
    val expression: String,
    val promptLabel: String,
    val operation: MathOperation,
    val correctAnswer: Int,
    val options: List<Int>,
    val hint: String,
    val isTeacherCustom: Boolean = false
)

data class LabProblem(
    val id: String,
    val title: String,
    val missionStory: String,
    val dataChips: List<String>,
    val operation: MathOperation,
    val equationHint: String,
    val answerOptions: List<Int>,
    val correctAnswer: Int,
    val unitLabel: String,
    val botHint: String,
    val isTeacherCustom: Boolean = false
)

data class CosmicBadge(
    val id: String,
    val title: String,
    val description: String,
    val requirementLabel: String,
    val starCostToUnlockPerk: Int = 0,
    val isPerk: Boolean = false,
    val isWeeklyBadge: Boolean = false,
    val targetStreakDays: Int = 0
)

data class OperationAccuracyStat(
    val operation: MathOperation,
    val accuracyPercent: Int,
    val sessionsCount: Int
)

data class DailyMissionTemplate(
    val id: String,
    val planetCode: String,
    val title: String,
    val subtitle: String,
    val operation: MathOperation,
    val questionCount: Int,
    val targetAccuracy: Int,
    val bonusXp: Int,
    val bonusStars: Int
)

const val DAILY_MISSION_ID = "daily_cosmic_mission"

fun formatDailyCountdown(remainingMs: Long): String {
    val totalSeconds = (remainingMs / 1000L).coerceAtLeast(0L)
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

data class XpLevelInfo(
    val level: Int,
    val rankTitle: String,
    val totalXp: Int,
    val currentLevelStartXp: Int,
    val nextLevelStartXp: Int,
    val xpInCurrentLevel: Int,
    val xpRequiredForNextLevel: Int,
    val xpRemainingToNextLevel: Int,
    val progressFraction: Float
)

object XpProgression {

    fun rankForLevel(level: Int): String = when {
        level >= 9 -> "Leyenda Hiperespacial"
        level >= 7 -> "Almirante Cósmico"
        level == 6 -> "Comandante Galáctico"
        level == 5 -> "Capitán Nebular"
        level == 4 -> "Piloto Cuántico"
        level == 3 -> "Navegante Estelar"
        level == 2 -> "Explorador Orbital"
        else -> "Cadete Lunar"
    }

    fun xpStepForLevel(level: Int): Int {
        val safeLevel = level.coerceAtLeast(1)
        return 100 + ((safeLevel - 1) * 50)
    }

    fun fromTotalXp(totalXp: Int): XpLevelInfo {
        val safeXp = totalXp.coerceAtLeast(0)
        var level = 1
        var levelStartXp = 0
        var stepSize = xpStepForLevel(level)

        while (safeXp >= levelStartXp + stepSize) {
            levelStartXp += stepSize
            level += 1
            stepSize = xpStepForLevel(level)
        }

        val xpInLevel = safeXp - levelStartXp
        val nextStart = levelStartXp + stepSize
        val remaining = (nextStart - safeXp).coerceAtLeast(0)
        val fraction = (xpInLevel.toFloat() / stepSize.toFloat()).coerceIn(0f, 1f)

        return XpLevelInfo(
            level = level,
            rankTitle = rankForLevel(level),
            totalXp = safeXp,
            currentLevelStartXp = levelStartXp,
            nextLevelStartXp = nextStart,
            xpInCurrentLevel = xpInLevel,
            xpRequiredForNextLevel = stepSize,
            xpRemainingToNextLevel = remaining,
            progressFraction = fraction
        )
    }
}



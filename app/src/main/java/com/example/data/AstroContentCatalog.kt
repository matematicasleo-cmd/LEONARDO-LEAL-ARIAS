package com.example.data

import com.example.model.CosmicBadge
import com.example.model.DailyMissionTemplate
import com.example.model.DifficultyLevel
import com.example.model.LabProblem
import com.example.model.LearningLesson
import com.example.model.MathOperation
import com.example.model.MathQuestion
import com.example.model.MissionDefinition
import kotlin.math.abs
import kotlin.random.Random

object AstroContentCatalog {

    val missions: List<MissionDefinition> = listOf(
        MissionDefinition(
            id = "m_suma_1",
            order = 1,
            title = "Estación Alpha",
            subtitle = "Sumas de impulso inicial y decenas exactas",
            operation = MathOperation.SUMA,
            xpReward = 60,
            targetAccuracy = 70,
            planetCode = "ORB-01"
        ),
        MissionDefinition(
            id = "m_suma_2",
            order = 2,
            title = "Satélite Aurora",
            subtitle = "Sumas con llevadas y cálculo mental rápido",
            operation = MathOperation.SUMA,
            xpReward = 80,
            targetAccuracy = 75,
            planetCode = "ORB-02"
        ),
        MissionDefinition(
            id = "m_resta_1",
            order = 3,
            title = "Cráter Selene",
            subtitle = "Diferencias directas y conteo regresivo",
            operation = MathOperation.RESTA,
            xpReward = 70,
            targetAccuracy = 70,
            planetCode = "CIN-03"
        ),
        MissionDefinition(
            id = "m_resta_2",
            order = 4,
            title = "Anillo de Titán",
            subtitle = "Restas con descomposición y verificación",
            operation = MathOperation.RESTA,
            xpReward = 90,
            targetAccuracy = 75,
            planetCode = "CIN-04"
        ),
        MissionDefinition(
            id = "m_mult_1",
            order = 5,
            title = "Nebulosa Vega",
            subtitle = "Tablas del 2 al 5 y arreglos orbitales",
            operation = MathOperation.MULTIPLICACION,
            xpReward = 90,
            targetAccuracy = 75,
            planetCode = "NEB-05"
        ),
        MissionDefinition(
            id = "m_mult_2",
            order = 6,
            title = "Cúmulo Orión",
            subtitle = "Tablas avanzadas y multiplicación por 10",
            operation = MathOperation.MULTIPLICACION,
            xpReward = 110,
            targetAccuracy = 80,
            planetCode = "NEB-06"
        ),
        MissionDefinition(
            id = "m_div_1",
            order = 7,
            title = "Base Kepler",
            subtitle = "Reparto exacto de cápsulas en módulos",
            operation = MathOperation.DIVISION,
            xpReward = 100,
            targetAccuracy = 75,
            planetCode = "DIV-07"
        ),
        MissionDefinition(
            id = "m_div_2",
            order = 8,
            title = "Portal Andrómeda",
            subtitle = "División exacta y operación inversa",
            operation = MathOperation.DIVISION,
            xpReward = 120,
            targetAccuracy = 80,
            planetCode = "DIV-08"
        ),
        MissionDefinition(
            id = "m_comb_1",
            order = 9,
            title = "Puente Hiperespacial",
            subtitle = "Retos mixtos de suma, resta y producto",
            operation = MathOperation.COMBINADAS,
            xpReward = 140,
            targetAccuracy = 80,
            planetCode = "GAL-09"
        ),
        MissionDefinition(
            id = "m_comb_2",
            order = 10,
            title = "Núcleo Galáctico",
            subtitle = "Maestría final en jerarquía de operaciones",
            operation = MathOperation.COMBINADAS,
            xpReward = 180,
            targetAccuracy = 85,
            planetCode = "GAL-10"
        )
    )

    val lessons: Map<MathOperation, LearningLesson> = mapOf(
        MathOperation.SUMA to LearningLesson(
            operation = MathOperation.SUMA,
            headline = "Sumar es unir energía en la órbita",
            conceptSummary = "Cuando sumas, combinas dos o más grupos de unidades para formar un total mayor. Agrupar en decenas completas hace el cálculo mucho más rápido.",
            visualTitle = "Modelo visual de cristales de energía",
            leftGroupCount = 8,
            rightGroupCount = 5,
            visualCaption = "8 cristales + 5 cristales = forma una decena (8 + 2 = 10) y suma los 3 restantes -> 13.",
            starTrickTitle = "Truco Estelar: Completa a 10",
            starTrickBody = "Para sumar 28 + 15, pasa 2 del 15 al 28 para formar 30. Ahora calcula 30 + 13 = 43 en un instante.",
            stepByStepExample = listOf(
                "1. Observa las unidades: 28 + 15 -> toma 2 unidades del 15.",
                "2. Completa la decena exacta: 28 + 2 = 30.",
                "3. Suma lo que quedó: 30 + 13 = 43."
            ),
            quickCheckPrompt = "Comprobación rápida: usando el truco de completar decenas, ¿cuánto es 19 + 14?",
            quickCheckOptions = listOf(31, 33, 35, 29),
            quickCheckAnswer = 33,
            quickCheckExplanation = "19 + 1 = 20, y luego 20 + 13 = 33."
        ),
        MathOperation.RESTA to LearningLesson(
            operation = MathOperation.RESTA,
            headline = "Restar mide la distancia entre dos órbitas",
            conceptSummary = "La resta calcula cuánto queda al quitar una parte o qué distancia numérica separa dos valores. Puedes contar hacia atrás o avanzar desde el número menor al mayor.",
            visualTitle = "Modelo visual de celdas de combustible",
            leftGroupCount = 12,
            rightGroupCount = 4,
            visualCaption = "12 celdas activas menos 4 consumidas en el despegue dejan 8 celdas listas.",
            starTrickTitle = "Truco Estelar: Cuenta hacia arriba",
            starTrickBody = "Para resolver 52 − 47, pregúntate: ¿cuánto le falta a 47 para llegar a 52? De 47 a 50 van 3, y a 52 van 2 -> total 5.",
            stepByStepExample = listOf(
                "1. Identifica el número inicial (minuendo) y el que quitas (sustraendo): 64 − 28.",
                "2. Resta primero las decenas: 64 − 20 = 44.",
                "3. Ahora resta las unidades: 44 − 8 = 36."
            ),
            quickCheckPrompt = "Comprobación rápida: ¿cuánto combustible queda si tienes 45 L y usas 18 L?",
            quickCheckOptions = listOf(23, 27, 29, 37),
            quickCheckAnswer = 27,
            quickCheckExplanation = "45 − 10 = 35, y luego 35 − 8 = 27."
        ),
        MathOperation.MULTIPLICACION to LearningLesson(
            operation = MathOperation.MULTIPLICACION,
            headline = "Multiplicar acelera la suma de grupos iguales",
            conceptSummary = "Multiplicar significa sumar el mismo número varias veces de forma ordenada, como paneles solares dispuestos en filas y columnas.",
            visualTitle = "Matriz de paneles solares (3 filas × 4 columnas)",
            leftGroupCount = 3,
            rightGroupCount = 4,
            visualCaption = "3 filas con 4 paneles cada una = 4 + 4 + 4 = 12 paneles en total.",
            starTrickTitle = "Truco Estelar: Descompón en partes fáciles",
            starTrickBody = "Si olvidas 7 × 6, divídelo como (5 × 6) + (2 × 6). Es 30 + 12 = 42.",
            stepByStepExample = listOf(
                "1. Interpreta 6 × 8 como 6 grupos de 8.",
                "2. Separa 6 en (5 + 1): (5 × 8) + (1 × 8).",
                "3. Suma los resultados: 40 + 8 = 48."
            ),
            quickCheckPrompt = "Comprobación rápida: una nave tiene 4 motores con 7 turbinas cada uno. ¿Cuántas turbinas son?",
            quickCheckOptions = listOf(24, 28, 32, 21),
            quickCheckAnswer = 28,
            quickCheckExplanation = "4 × 7 = (4 × 5) + (4 × 2) = 20 + 8 = 28."
        ),
        MathOperation.DIVISION to LearningLesson(
            operation = MathOperation.DIVISION,
            headline = "Dividir distribuye recursos con equidad",
            conceptSummary = "Dividir consiste en repartir un total en partes iguales o descubrir cuántos grupos caben en una cantidad. Es la operación inversa de multiplicar.",
            visualTitle = "Reparto en cápsulas de tripulación",
            leftGroupCount = 12,
            rightGroupCount = 3,
            visualCaption = "12 raciones repartidas entre 3 módulos = 4 raciones por módulo.",
            starTrickTitle = "Truco Estelar: Usa la multiplicación inversa",
            starTrickBody = "Para resolver 56 ÷ 8, pregúntate: ¿qué número multiplicado por 8 da 56? Como 7 × 8 = 56, entonces 56 ÷ 8 = 7.",
            stepByStepExample = listOf(
                "1. Lee la división: 42 ÷ 6.",
                "2. Busca en la tabla del 6 qué factor llega a 42.",
                "3. 6 × 7 = 42, así que la respuesta exacta es 7."
            ),
            quickCheckPrompt = "Comprobación rápida: repartimos 36 muestras lunares en 4 cajas iguales. ¿Cuántas van en cada caja?",
            quickCheckOptions = listOf(8, 9, 7, 6),
            quickCheckAnswer = 9,
            quickCheckExplanation = "Porque 4 × 9 = 36, cada caja recibe 9 muestras."
        ),
        MathOperation.COMBINADAS to LearningLesson(
            operation = MathOperation.COMBINADAS,
            headline = "Orden cósmico: jerarquía de operaciones",
            conceptSummary = "Cuando una expresión mezcla operaciones, no siempre se resuelve solo de izquierda a derecha: primero multiplicaciones y divisiones, y después sumas y restas.",
            visualTitle = "Prioridad en el puente de mando",
            leftGroupCount = 4,
            rightGroupCount = 2,
            visualCaption = "En 5 + 4 × 2, primero calculamos 4 × 2 = 8 y al final sumamos 5 -> resultado 13.",
            starTrickTitle = "Truco Estelar: Subraya la prioridad",
            starTrickBody = "Antes de calcular, encierra mentalmente (× y ÷). Resuélvelas primero y reescribe la operación simplificada.",
            stepByStepExample = listOf(
                "1. Observa la expresión: 10 + 3 × 4 − 2.",
                "2. Prioridad 1 (multiplicación): 3 × 4 = 12.",
                "3. Prioridad 2 (izquierda a derecha): 10 + 12 − 2 = 20."
            ),
            quickCheckPrompt = "Comprobación rápida: aplicando la jerarquía, ¿cuál es el resultado de 6 + 4 × 3?",
            quickCheckOptions = listOf(30, 18, 13, 24),
            quickCheckAnswer = 18,
            quickCheckExplanation = "Primero 4 × 3 = 12, después 6 + 12 = 18."
        )
    )

    val labProblems: List<LabProblem> = listOf(
        LabProblem(
            id = "lab_1",
            title = "Carga de paneles solares",
            missionStory = "La estación orbital tiene 28 paneles activos en el ala norte y acaba de desplegar 17 paneles nuevos en el ala sur. ¿Cuántos paneles funcionan en total?",
            dataChips = listOf("28 paneles norte", "17 paneles sur"),
            operation = MathOperation.SUMA,
            equationHint = "28 + 17 = ?",
            answerOptions = listOf(43, 45, 47, 35),
            correctAnswer = 45,
            unitLabel = "paneles",
            botHint = "Suma 28 + 10 = 38, y luego agrega 7 más para llegar a 45."
        ),
        LabProblem(
            id = "lab_2",
            title = "Reserva de oxígeno lunar",
            missionStory = "Un módulo lunar despegó con 84 tanques de oxígeno. Durante la exploración consumió 36 tanques. ¿Cuántos tanques quedan disponibles?",
            dataChips = listOf("84 tanques iniciales", "36 tanques usados"),
            operation = MathOperation.RESTA,
            equationHint = "84 − 36 = ?",
            answerOptions = listOf(48, 52, 46, 58),
            correctAnswer = 48,
            unitLabel = "tanques",
            botHint = "Resta 84 − 30 = 54 y después quita 6 unidades: 54 − 6 = 48."
        ),
        LabProblem(
            id = "lab_3",
            title = "Flota de sondas exploradoras",
            missionStory = "Enviamos 6 naves nodrizas hacia Júpiter y cada una transporta 9 micro-sondas. ¿Cuántas micro-sondas viajarán en la expedición?",
            dataChips = listOf("6 naves nodrizas", "9 sondas por nave"),
            operation = MathOperation.MULTIPLICACION,
            equationHint = "6 × 9 = ?",
            answerOptions = listOf(48, 54, 56, 45),
            correctAnswer = 54,
            unitLabel = "sondas",
            botHint = "6 × 10 = 60. Si restas un grupo de 6 obtienes 60 − 6 = 54."
        ),
        LabProblem(
            id = "lab_4",
            title = "Distribución de cristales",
            missionStory = "Los astronautas recolectaron 72 muestras de asteroides y deben guardarlas en 8 contenedores con igual capacidad. ¿Cuántas muestras van en cada contenedor?",
            dataChips = listOf("72 muestras totales", "8 contenedores iguales"),
            operation = MathOperation.DIVISION,
            equationHint = "72 ÷ 8 = ?",
            answerOptions = listOf(8, 9, 7, 12),
            correctAnswer = 9,
            unitLabel = "muestras",
            botHint = "Piensa en la tabla del 8: ¿8 por qué número da 72?"
        ),
        LabProblem(
            id = "lab_5",
            title = "Energía del reactor principal",
            missionStory = "El reactor genera 12 unidades base más 3 baterías auxiliares de 5 unidades cada una. ¿Cuánta energía total produce?",
            dataChips = listOf("12 base", "3 baterías", "5 unidades c/u"),
            operation = MathOperation.COMBINADAS,
            equationHint = "12 + (3 × 5) = ?",
            answerOptions = listOf(27, 75, 20, 32),
            correctAnswer = 27,
            unitLabel = "unidades",
            botHint = "Primero multiplica las 3 baterías × 5 = 15. Luego suma 12 + 15."
        )
    )

    val badges: List<CosmicBadge> = listOf(
        CosmicBadge(
            id = "badge_first_flight",
            title = "Primer Despegue",
            description = "Completa tu primera misión espacial de matemáticas.",
            requirementLabel = "1 misión completada"
        ),
        CosmicBadge(
            id = "badge_star_collector",
            title = "Cazador de Estrellas",
            description = "Acumula 10 estrellas en las misiones del mapa orbital.",
            requirementLabel = "10 estrellas totales"
        ),
        CosmicBadge(
            id = "badge_precision",
            title = "Órbita Perfecta",
            description = "Alcanza 90% o más de precisión en un Reto Matemático.",
            requirementLabel = "90% de precisión"
        ),
        CosmicBadge(
            id = "badge_explorer_xp",
            title = "Piloto de Élite",
            description = "Supera los 300 puntos de experiencia cósmica (XP).",
            requirementLabel = "300 XP acumulados"
        ),
        CosmicBadge(
            id = "badge_daily_streak",
            title = "Piloto Constante",
            description = "Completa al menos una Misión Cósmica Diaria para impulsar tu racha estelar.",
            requirementLabel = "1 Misión Diaria completada"
        ),
        CosmicBadge(
            id = "badge_weekly_3",
            title = "Órbita Constante (3 Días)",
            description = "Completa 3 días de misión consecutivos manteniendo el rumbo espacial.",
            requirementLabel = "3 días consecutivos",
            isWeeklyBadge = true,
            targetStreakDays = 3
        ),
        CosmicBadge(
            id = "badge_weekly_7",
            title = "Semana Galáctica Perfecta",
            description = "Supera 7 días de misión consecutivos para conquistar la semana completa.",
            requirementLabel = "7 días consecutivos (1 semana)",
            isWeeklyBadge = true,
            targetStreakDays = 7
        ),
        CosmicBadge(
            id = "badge_weekly_14",
            title = "Comandante Inquebrantable",
            description = "Alcanza 14 días de misión consecutivos demostrando constancia semanal élite.",
            requirementLabel = "14 días consecutivos (2 semanas)",
            isWeeklyBadge = true,
            targetStreakDays = 14
        ),
        CosmicBadge(
            id = "perk_ion_thruster",
            title = "Propulsor de Iones Cian",
            description = "Mejora estética para tu perfil y placa de nave espacial.",
            requirementLabel = "Canjeable por 5 estrellas",
            starCostToUnlockPerk = 5,
            isPerk = true
        ),
        CosmicBadge(
            id = "perk_solar_shield",
            title = "Escudo Solar Dorado",
            description = "Marco estelar dorado para tu avatar de comandante.",
            requirementLabel = "Canjeable por 10 estrellas",
            starCostToUnlockPerk = 10,
            isPerk = true
        ),
        CosmicBadge(
            id = "perk_quantum_wing",
            title = "Alas Cuánticas Violeta",
            description = "Insignia de rango superior en el puente de mando.",
            requirementLabel = "Canjeable por 15 estrellas",
            starCostToUnlockPerk = 15,
            isPerk = true
        )
    )

    val dailyMissions: List<DailyMissionTemplate> = listOf(
        DailyMissionTemplate(
            id = "daily_1",
            planetCode = "DIA-01",
            title = "Tormenta Solar de Sumas",
            subtitle = "Estabiliza los paneles de la estación resolviendo 5 sumas rápidas.",
            operation = MathOperation.SUMA,
            questionCount = 5,
            targetAccuracy = 75,
            bonusXp = 90,
            bonusStars = 2
        ),
        DailyMissionTemplate(
            id = "daily_2",
            planetCode = "DIA-02",
            title = "Eclipses y Diferencias",
            subtitle = "Calcula trayectorias de resta con precisión orbital.",
            operation = MathOperation.RESTA,
            questionCount = 5,
            targetAccuracy = 75,
            bonusXp = 95,
            bonusStars = 2
        ),
        DailyMissionTemplate(
            id = "daily_3",
            planetCode = "DIA-03",
            title = "Propulsión Multiplicadora",
            subtitle = "Sincroniza 5 motores cuánticos con multiplicaciones exactas.",
            operation = MathOperation.MULTIPLICACION,
            questionCount = 5,
            targetAccuracy = 80,
            bonusXp = 110,
            bonusStars = 2
        ),
        DailyMissionTemplate(
            id = "daily_4",
            planetCode = "DIA-04",
            title = "Órbita de Reparto Exacto",
            subtitle = "Distribuye energía equitativa entre los módulos espaciales.",
            operation = MathOperation.DIVISION,
            questionCount = 5,
            targetAccuracy = 80,
            bonusXp = 115,
            bonusStars = 2
        ),
        DailyMissionTemplate(
            id = "daily_5",
            planetCode = "DIA-05",
            title = "Salto Hiperespacial Mixto",
            subtitle = "Supera la jerarquía de operaciones en 5 saltos cósmicos.",
            operation = MathOperation.COMBINADAS,
            questionCount = 5,
            targetAccuracy = 80,
            bonusXp = 130,
            bonusStars = 3
        ),
        DailyMissionTemplate(
            id = "daily_6",
            planetCode = "DIA-06",
            title = "Escudo de Cálculo Mental",
            subtitle = "Refuerza la defensa con una serie de 6 sumas estratégicas.",
            operation = MathOperation.SUMA,
            questionCount = 6,
            targetAccuracy = 80,
            bonusXp = 120,
            bonusStars = 3
        ),
        DailyMissionTemplate(
            id = "daily_7",
            planetCode = "DIA-07",
            title = "Comando Táctico Galáctico",
            subtitle = "Demuestra maestría multiplicadora con 6 desafíos intensivos.",
            operation = MathOperation.MULTIPLICACION,
            questionCount = 6,
            targetAccuracy = 85,
            bonusXp = 140,
            bonusStars = 3
        )
    )

    fun generateChallengeQuestions(
        operation: MathOperation,
        difficulty: DifficultyLevel,
        count: Int
    ): List<MathQuestion> {
        return List(count) { index ->
            createQuestion(operation, difficulty, index)
        }
    }

    private fun createQuestion(
        operation: MathOperation,
        difficulty: DifficultyLevel,
        index: Int
    ): MathQuestion {
        val maxVal = difficulty.maxOperand
        val (expr, answer, hint, actualOp) = when (operation) {
            MathOperation.SUMA -> {
                val a = Random.nextInt(5, maxVal)
                val b = Random.nextInt(4, maxVal / 2 + 6)
                val sum = a + b
                Quad("$a + $b", sum, "Descompón $b en decenas y unidades para sumarlo a $a.", MathOperation.SUMA)
            }
            MathOperation.RESTA -> {
                val b = Random.nextInt(4, maxVal / 2 + 5)
                val diff = Random.nextInt(5, maxVal / 2 + 8)
                val a = b + diff
                Quad("$a − $b", diff, "Cuenta cuánto le falta a $b para alcanzar $a.", MathOperation.RESTA)
            }
            MathOperation.MULTIPLICACION -> {
                val maxFactor = when (difficulty) {
                    DifficultyLevel.EXPLORADOR -> 8
                    DifficultyLevel.PILOTO -> 10
                    DifficultyLevel.COMANDANTE -> 12
                }
                val a = Random.nextInt(2, maxFactor + 1)
                val b = Random.nextInt(2, maxFactor + 1)
                val prod = a * b
                Quad("$a × $b", prod, "Piensa en $a grupos de $b unidades cada uno.", MathOperation.MULTIPLICACION)
            }
            MathOperation.DIVISION -> {
                val maxDivisor = when (difficulty) {
                    DifficultyLevel.EXPLORADOR -> 6
                    DifficultyLevel.PILOTO -> 9
                    DifficultyLevel.COMANDANTE -> 12
                }
                val divisor = Random.nextInt(2, maxDivisor + 1)
                val quotient = Random.nextInt(2, maxDivisor + 2)
                val dividend = divisor * quotient
                Quad("$dividend ÷ $divisor", quotient, "Usa la multiplicación inversa: ¿qué número × $divisor = $dividend?", MathOperation.DIVISION)
            }
            MathOperation.COMBINADAS -> {
                val a = Random.nextInt(3, 14)
                val b = Random.nextInt(2, 7)
                val c = Random.nextInt(2, 7)
                val usePlus = index % 2 == 0
                if (usePlus) {
                    val ans = a + (b * c)
                    Quad("$a + $b × $c", ans, "Primero multiplica $b × $c = ${b * c}, luego suma $a.", MathOperation.COMBINADAS)
                } else {
                    val prod = b * c
                    val top = prod + a
                    val ans = top - prod + b // clean positive expression: top - b * c
                    Quad("$top − $b × $c", top - prod, "Primero calcula $b × $c = $prod, luego réstalo de $top.", MathOperation.COMBINADAS)
                }
            }
        }

        val distractors = buildDistractors(answer)
        return MathQuestion(
            id = "q_${operation.id}_$index",
            expression = expr,
            promptLabel = "¿Cuál es el resultado correcto?",
            operation = actualOp,
            correctAnswer = answer,
            options = distractors,
            hint = hint
        )
    }

    fun buildDistractors(answer: Int): List<Int> {
        val set = linkedSetOf(answer)
        val offsets = listOf(-10, -4, -3, -2, -1, 1, 2, 3, 4, 5, 10)
        var guard = 0
        while (set.size < 4 && guard < 40) {
            val candidate = answer + offsets.random()
            if (candidate >= 0 && candidate != answer) {
                set.add(candidate)
            }
            guard++
        }
        var fallback = 1
        while (set.size < 4) {
            if (abs(answer + fallback) != answer) set.add(answer + fallback)
            fallback++
        }
        return set.shuffled()
    }

    private data class Quad(
        val expr: String,
        val answer: Int,
        val hint: String,
        val op: MathOperation
    )
}

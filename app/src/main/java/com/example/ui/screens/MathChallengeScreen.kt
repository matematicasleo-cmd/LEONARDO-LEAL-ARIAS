package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.model.MathOperation
import com.example.ui.AstroUiState
import com.example.ui.components.AstroBotHelperCard
import com.example.ui.components.CosmicCard
import com.example.ui.components.PrimaryCosmicButton
import com.example.ui.components.SecondaryCyanButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StarRatingRow
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.DeepCosmos
import com.example.ui.theme.ElevatedCosmic
import com.example.ui.theme.ErrorContainerDark
import com.example.ui.theme.ErrorCoral
import com.example.ui.theme.MidnightOrbit
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NebulaSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrbitBorder
import com.example.ui.theme.SolarContainer
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessEmerald

@Composable
fun MathChallengeScreen(
    uiState: AstroUiState,
    onStartOperationChallenge: (MathOperation) -> Unit,
    onLaunchDailyMission: () -> Unit,
    onSubmitAnswer: (Int) -> Unit,
    onToggleHint: () -> Unit,
    onNextQuestion: () -> Unit,
    onResetToSelector: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    val challenge = uiState.challenge
    val sound = LocalSpaceSoundManager.current

    LaunchedEffect(challenge.leveledUp, challenge.newLevelReached) {
        if (challenge.isCompleted && challenge.leveledUp) {
            sound?.play(SpaceSfx.STAR_CHIRP)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                challenge.isCompleted -> {
                    val totalQ = challenge.questions.size.coerceAtLeast(1)
                    val accuracy = ((challenge.correctCount.toFloat() / totalQ) * 100f).toInt()
                    val stars = when {
                        accuracy >= 90 -> 3
                        accuracy >= 70 -> 2
                        accuracy >= 50 -> 1
                        else -> 0
                    }
                    val xpEarned = (challenge.correctCount * 15) + (stars * 20)

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = NebulaSurface,
                        border = BorderStroke(1.5.dp, SolarYellow.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_cosmic_trophy_vault),
                                    contentDescription = "Resultado del reto espacial",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(155.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(155.dp)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    DeepCosmos.copy(alpha = 0.2f),
                                                    DeepCosmos.copy(alpha = 0.9f)
                                                )
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(18.dp)
                                ) {
                                    Text(
                                        text = if (accuracy >= 70) "¡MISIÓN SUPERADA!" else "ENTRENAMIENTO COMPLETADO",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SolarYellow
                                    )
                                    Text(
                                        text = challenge.missionTitle,
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = StarWhite
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StarRatingRow(stars = stars, starSize = 28.dp)
                                    Text(
                                        text = "+$xpEarned XP",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = NeonCyan
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    ChallengeSummaryMetric("Precisión", "$accuracy%", Modifier.weight(1f))
                                    ChallengeSummaryMetric("Aciertos", "${challenge.correctCount}/$totalQ", Modifier.weight(1f))
                                    ChallengeSummaryMetric("Mejor Racha", "x${challenge.bestStreak}", Modifier.weight(1f))
                                }

                                if (challenge.isDailyMission) {
                                    val dailySuccess = challenge.dailyBonusXpEarned > 0 || accuracy >= challenge.dailyTargetAccuracy
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (dailySuccess) SuccessContainer else ErrorContainerDark,
                                        border = BorderStroke(
                                            1.dp,
                                            if (dailySuccess) SuccessEmerald.copy(alpha = 0.7f) else SolarYellow.copy(alpha = 0.6f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("challenge_daily_result_banner")
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = if (challenge.dailyBonusXpEarned > 0) {
                                                    "¡Bonus de Misión Cósmica Diaria desbloqueado!"
                                                } else if (dailySuccess) {
                                                    "Misión Cósmica Diaria completada (racha: ${uiState.profile.streakDays} días)"
                                                } else {
                                                    "Meta diaria: ${challenge.dailyTargetAccuracy}% de precisión. ¡Inténtalo de nuevo!"
                                                },
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (dailySuccess) SuccessEmerald else SolarYellow
                                            )
                                            if (challenge.dailyBonusXpEarned > 0) {
                                                Spacer(Modifier.height(2.dp))
                                                Text(
                                                    text = "+${challenge.dailyBonusXpEarned} XP extra • +${challenge.dailyBonusStarsEarned} ★ • Racha: ${uiState.profile.streakDays} días",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = StarWhite
                                                )
                                            }
                                        }
                                    }
                                }

                                if (challenge.leveledUp) {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = SolarContainer,
                                        border = BorderStroke(1.5.dp, NeonCyan.copy(alpha = 0.85f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("challenge_level_up_banner")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.WorkspacePremium,
                                                contentDescription = "Subida de nivel",
                                                tint = SolarYellow,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Spacer(Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "¡SUBISTE AL NIVEL ${challenge.newLevelReached}!",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = SolarYellow,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Nuevo rango desbloqueado: ${challenge.newRankTitle}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = StarWhite
                                                )
                                            }
                                        }
                                    }
                                }

                                PrimaryCosmicButton(
                                    text = if (challenge.isDailyMission) {
                                        "Repetir Misión Diaria"
                                    } else {
                                        "Jugar Otro Reto de ${challenge.operation.label}"
                                    },
                                    icon = Icons.Rounded.Refresh,
                                    onClick = {
                                        if (challenge.isDailyMission) {
                                            onLaunchDailyMission()
                                        } else {
                                            onStartOperationChallenge(challenge.operation)
                                        }
                                    },
                                    modifier = Modifier.testTag("challenge_replay_button")
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    SecondaryCyanButton(
                                        text = "Mapa",
                                        onClick = {
                                            onResetToSelector()
                                            onOpenMap()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    SecondaryCyanButton(
                                        text = "Recompensas",
                                        icon = Icons.Rounded.EmojiEvents,
                                        onClick = {
                                            onResetToSelector()
                                            onOpenRewards()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                challenge.isActive && challenge.currentQuestion != null -> {
                    val question = challenge.currentQuestion!!
                    val totalQ = challenge.questions.size.coerceAtLeast(1)
                    val progress = (challenge.currentIndex + 1).toFloat() / totalQ.toFloat()

                    // HUD Card
                    CosmicCard(
                        borderColor = challenge.operation.accentColor.copy(alpha = 0.65f),
                        containerColor = NebulaSurface
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                if (challenge.isDailyMission) {
                                    Text(
                                        text = "MISIÓN CÓSMICA DIARIA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SolarYellow
                                    )
                                }
                                Text(
                                    text = challenge.missionTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = StarWhite
                                )
                                Text(
                                    text = "Pregunta ${challenge.currentIndex + 1} de $totalQ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MoonSilver
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Streak Pill
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SolarContainer,
                                    border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = SolarYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(3.dp))
                                        Text(
                                            text = "x${challenge.streak}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = SolarYellow
                                        )
                                    }
                                }

                                // Timer Pill
                                val lowTime = challenge.secondsRemaining <= 15
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (lowTime) ErrorContainerDark else MidnightOrbit,
                                    border = BorderStroke(1.dp, if (lowTime) ErrorCoral else NeonCyan)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Timer,
                                            contentDescription = "Tiempo restante",
                                            tint = if (lowTime) ErrorCoral else NeonCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = "${challenge.secondsRemaining}s",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (lowTime) ErrorCoral else NeonCyan
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = challenge.operation.accentColor,
                                trackColor = MidnightOrbit
                            )
                            Spacer(Modifier.width(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(3) { idx ->
                                    Icon(
                                        imageVector = Icons.Rounded.Shield,
                                        contentDescription = null,
                                        tint = if (idx < challenge.shieldsLeft) NeonCyan else OrbitBorder,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Question Card
                    CosmicCard(
                        borderColor = OrbitBorder,
                        containerColor = ElevatedCosmic.copy(alpha = 0.72f)
                    ) {
                        if (question.isTeacherCustom) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SolarYellow.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "ASIGNADO POR DOCENTE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SolarYellow,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        Text(
                            text = question.promptLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MoonSilver,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = question.expression,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                            color = StarWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))

                        if (uiState.teacherSettings.hintsEnabled && !challenge.isAnswerSubmitted) {
                            Surface(
                                onClick = onToggleHint,
                                shape = RoundedCornerShape(12.dp),
                                color = MidnightOrbit,
                                border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .testTag("challenge_hint_toggle")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Lightbulb,
                                        contentDescription = null,
                                        tint = SolarYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = if (challenge.showHint) "Ocultar pista" else "Pedir pista a AstroBot",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SolarYellow
                                    )
                                }
                            }
                        }
                    }

                    if (challenge.showHint) {
                        AstroBotHelperCard(
                            title = "Pista de Cálculo",
                            message = question.hint,
                            accentColor = SolarYellow
                        )
                    }

                    // 2x2 Option Grid
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        question.options.chunked(2).forEach { rowOptions ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowOptions.forEach { option ->
                                    val isSelected = challenge.selectedOption == option
                                    val isCorrect = option == question.correctAnswer
                                    val showResult = challenge.isAnswerSubmitted
                                    val bgColor = when {
                                        showResult && isCorrect -> SuccessContainer
                                        showResult && isSelected && !isCorrect -> ErrorContainerDark
                                        else -> NebulaSurface
                                    }
                                    val borderColor = when {
                                        showResult && isCorrect -> SuccessEmerald
                                        showResult && isSelected && !isCorrect -> ErrorCoral
                                        else -> OrbitBorder
                                    }

                                    Surface(
                                        onClick = {
                                            sound?.play(if (isCorrect) SpaceSfx.STAR_CHIRP else SpaceSfx.SHIELD_THUD)
                                            onSubmitAnswer(option)
                                        },
                                        enabled = !challenge.isAnswerSubmitted,
                                        modifier = Modifier
                                            .weight(1f)
                                            .heightIn(min = 68.dp)
                                            .testTag("challenge_option_$option"),
                                        shape = RoundedCornerShape(18.dp),
                                        color = bgColor,
                                        border = BorderStroke(1.6.dp, borderColor)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = option.toString(),
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = StarWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (challenge.isAnswerSubmitted) {
                        val isCorrect = challenge.selectedOption == question.correctAnswer
                        CosmicCard(
                            borderColor = if (isCorrect) SuccessEmerald else ErrorCoral,
                            containerColor = if (isCorrect) SuccessContainer else ErrorContainerDark
                        ) {
                            Text(
                                text = if (isCorrect) "¡Correcto! Propulsores al máximo." else "Respuesta correcta: ${question.correctAnswer}",
                                style = MaterialTheme.typography.titleMedium,
                                color = StarWhite
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = question.hint,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MoonSilver
                            )
                            Spacer(Modifier.height(12.dp))
                            PrimaryCosmicButton(
                                text = if (challenge.currentIndex >= challenge.questions.lastIndex || challenge.shieldsLeft <= 0) {
                                    "Ver Resultados"
                                } else {
                                    "Siguiente Desafío"
                                },
                                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                                onClick = onNextQuestion,
                                containerColor = if (isCorrect) SuccessEmerald.copy(alpha = 0.85f) else CosmicPurple,
                                modifier = Modifier.testTag("challenge_next_button")
                            )
                        }
                    }
                }

                else -> {
                    // Challenge Mode Launcher
                    SectionHeader(
                        eyebrow = "Arena de Agilidad Mental",
                        title = "Reto Matemático",
                        subtitle = "Selecciona una operación para iniciar una serie de ${uiState.teacherSettings.questionsPerChallenge} desafíos en ${uiState.teacherSettings.challengeTimeSeconds} segundos."
                    )

                    AstroBotHelperCard(
                        title = "Configuración Activa",
                        message = "Nivel ${uiState.profile.difficultyId.replaceFirstChar { it.uppercase() }} • Racha y escudos activos • Prioridad docente: ${MathOperation.fromId(uiState.teacherSettings.focusOperationId).label}."
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        MathOperation.entries.forEach { op ->
                            CosmicCard(
                                borderColor = op.accentColor.copy(alpha = 0.65f),
                                onClick = { onStartOperationChallenge(op) },
                                modifier = Modifier.testTag("challenge_launcher_${op.id}")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = op.accentColor.copy(alpha = 0.22f),
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = op.symbol,
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = op.accentColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Reto de ${op.label}",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = StarWhite
                                        )
                                        Text(
                                            text = op.sectorName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MoonSilver
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = "Iniciar reto",
                                        tint = op.accentColor,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ChallengeSummaryMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MidnightOrbit,
        border = BorderStroke(1.dp, OrbitBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = StarWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MoonSilver
            )
        }
    }
}

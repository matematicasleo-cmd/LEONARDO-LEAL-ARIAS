package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AstroContentCatalog
import com.example.model.MathOperation
import com.example.ui.AstroUiState
import com.example.ui.components.AstroBotHelperCard
import com.example.ui.components.CosmicCard
import com.example.ui.components.PrimaryCosmicButton
import com.example.ui.components.SecondaryCyanButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricTile
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.MidnightOrbit
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrbitBorder
import com.example.ui.theme.SolarContainer
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessEmerald

@Composable
fun ProgressScreen(
    uiState: AstroUiState,
    onPracticeOperation: (MathOperation) -> Unit,
    onLearnOperation: (MathOperation) -> Unit,
    onLaunchDailyMission: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenTeacherPanel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stats = uiState.operationStats
    val recommended = uiState.recommendedOperation

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
            SectionHeader(
                eyebrow = "Analítica del Estudiante",
                title = "Mi Progreso",
                subtitle = "Seguimiento detallado de precisión, misiones y dominio por operación."
            )

            // 2x2 KPI Summary Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMetricTile(
                        label = "Experiencia (XP)",
                        value = "${uiState.profile.totalXp}",
                        accent = NeonCyan,
                        icon = Icons.Rounded.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricTile(
                        label = "Precisión Global",
                        value = "${uiState.overallAccuracyPercent}%",
                        accent = SuccessEmerald,
                        icon = Icons.Rounded.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMetricTile(
                        label = "Estrellas",
                        value = "${uiState.profile.totalStars}",
                        accent = SolarYellow,
                        icon = Icons.Rounded.Star,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricTile(
                        label = "Misiones",
                        value = "${uiState.completedMissionsCount}/${AstroContentCatalog.missions.size}",
                        accent = CosmicPurple,
                        icon = Icons.Rounded.RocketLaunch,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Daily Cosmic Mission Consistency & Streak Card
            CosmicCard(
                borderColor = if (uiState.dailyMission.completed) SuccessEmerald.copy(alpha = 0.65f) else SolarYellow.copy(alpha = 0.65f),
                modifier = Modifier.testTag("progress_daily_streak_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SolarContainer,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalFireDepartment,
                                    contentDescription = "Racha diaria",
                                    tint = SolarYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Constancia Cósmica: ${uiState.profile.streakDays} ${if (uiState.profile.streakDays == 1) "día" else "días"}",
                                style = MaterialTheme.typography.titleMedium,
                                color = StarWhite
                            )
                            Text(
                                text = "${uiState.dailyMission.totalDailyCompleted} misiones diarias completadas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MoonSilver
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = uiState.dailyCountdownText,
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = if (uiState.dailyMission.completed) {
                        "Misión de hoy (${uiState.dailyMission.title}) superada. Vuelve en ${uiState.dailyCountdownText} para extender tu racha."
                    } else {
                        "Misión de hoy pendiente: ${uiState.dailyMission.title} (+${uiState.dailyMission.bonusXp} XP, +${uiState.dailyMission.bonusStars} ★)."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.dailyMission.completed) SuccessEmerald else MoonSilver
                )

                if (!uiState.dailyMission.completed) {
                    Spacer(Modifier.height(10.dp))
                    PrimaryCosmicButton(
                        text = "Completar Misión Diaria",
                        icon = Icons.Rounded.PlayArrow,
                        onClick = onLaunchDailyMission,
                        modifier = Modifier.testTag("progress_launch_daily_button")
                    )
                }
            }

            // Custom Canvas Mastery Chart + Breakdown
            CosmicCard {
                Text(
                    text = "Dominio por Operación Matemática",
                    style = MaterialTheme.typography.titleLarge,
                    color = StarWhite
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Visualización comparativa de tu precisión acumulada.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MoonSilver
                )
                Spacer(Modifier.height(14.dp))

                // Responsive Canvas bar visualization
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    val barCount = stats.size.coerceAtLeast(1)
                    val slotWidth = size.width / barCount
                    val barWidth = (slotWidth * 0.48f).coerceAtMost(46.dp.toPx())
                    val maxBarHeight = size.height - 16.dp.toPx()

                    stats.forEachIndexed { idx, stat ->
                        val ratio = (stat.accuracyPercent / 100f).coerceIn(0.08f, 1f)
                        val barHeight = maxBarHeight * ratio
                        val left = idx * slotWidth + (slotWidth - barWidth) / 2f
                        val top = size.height - barHeight

                        drawRoundRect(
                            color = OrbitBorder.copy(alpha = 0.45f),
                            topLeft = Offset(left, 8.dp.toPx()),
                            size = Size(barWidth, maxBarHeight),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                        drawRoundRect(
                            color = stat.operation.accentColor,
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    stats.forEach { stat ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${stat.operation.symbol} ${stat.operation.label}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = StarWhite
                                )
                                Text(
                                    text = "${stat.accuracyPercent}% (${stat.sessionsCount} sesiones)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = stat.operation.accentColor
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (stat.accuracyPercent / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = stat.operation.accentColor,
                                trackColor = MidnightOrbit
                            )
                        }
                    }
                }
            }

            // Personalized Recommendation Card
            AstroBotHelperCard(
                title = "Siguiente Paso Recomendado: ${recommended.label}",
                message = "Refuerza ${recommended.sectorName} en la Zona de Aprendizaje o supera un nuevo reto para elevar tu rango.",
                accentColor = recommended.accentColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryCyanButton(
                    text = "Repasar ${recommended.label}",
                    icon = Icons.Rounded.AutoStories,
                    onClick = { onLearnOperation(recommended) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_learn_recommended")
                )
                PrimaryCosmicButton(
                    text = "Practicar Reto",
                    icon = Icons.Rounded.PlayArrow,
                    onClick = { onPracticeOperation(recommended) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_practice_recommended")
                )
            }

            // Recent Flight Log
            CosmicCard {
                Text(
                    text = "Bitácora de Vuelo Reciente",
                    style = MaterialTheme.typography.titleLarge,
                    color = StarWhite
                )
                Spacer(Modifier.height(10.dp))

                if (uiState.sessions.isEmpty()) {
                    Text(
                        text = "Completa tu primer Reto Matemático o caso del Laboratorio para registrar tu historial espacial.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MoonSilver
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.sessions.take(5).forEach { session ->
                            val op = MathOperation.fromId(session.operationId)
                            val acc = if (session.totalCount > 0) (session.correctCount * 100 / session.totalCount) else 0
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MidnightOrbit,
                                border = BorderStroke(1.dp, OrbitBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = op.symbol,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = op.accentColor,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = op.label,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = StarWhite
                                            )
                                            Text(
                                                text = "${session.correctCount}/${session.totalCount} aciertos • $acc%",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MoonSilver
                                            )
                                        }
                                    }
                                    Text(
                                        text = "+${session.xpEarned} XP",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = SolarYellow
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryCyanButton(
                    text = "Ver Recompensas",
                    icon = Icons.Rounded.EmojiEvents,
                    onClick = onOpenRewards,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_open_rewards")
                )
                SecondaryCyanButton(
                    text = "Panel Docente",
                    icon = Icons.Rounded.School,
                    onClick = onOpenTeacherPanel,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_open_teacher")
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

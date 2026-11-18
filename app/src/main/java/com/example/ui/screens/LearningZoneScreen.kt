package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AstroContentCatalog
import com.example.model.MathOperation
import com.example.ui.AstroUiState
import com.example.ui.components.AstroBotHelperCard
import com.example.ui.components.CosmicCard
import com.example.ui.components.OperationBadge
import com.example.ui.components.PrimaryCosmicButton
import com.example.ui.components.SecondaryCyanButton
import com.example.ui.components.SectionHeader
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LearningZoneScreen(
    uiState: AstroUiState,
    onSelectOperation: (MathOperation) -> Unit,
    onSelectQuickCheck: (Int) -> Unit,
    onStartChallenge: (MathOperation) -> Unit,
    onOpenLab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeOp = uiState.selectedLearningOp
    val lesson = AstroContentCatalog.lessons[activeOp] ?: AstroContentCatalog.lessons.getValue(MathOperation.SUMA)
    var visualMultiplier by remember(activeOp) { mutableIntStateOf(lesson.leftGroupCount) }

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
                eyebrow = "Academia Conceptual",
                title = "Zona de Aprendizaje",
                subtitle = "Domina la estrategia visual antes de entrar en combate numérico."
            )

            // Operation Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MathOperation.entries.forEach { op ->
                    OperationBadge(
                        operation = op,
                        selected = op == activeOp,
                        onClick = { onSelectOperation(op) },
                        modifier = Modifier.testTag("learning_op_${op.id}")
                    )
                }
            }

            // Sector Illustration Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = NebulaSurface,
                border = BorderStroke(1.dp, activeOp.accentColor.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_planet_math_lab),
                        contentDescription = lesson.headline,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        DeepCosmos.copy(alpha = 0.25f),
                                        DeepCosmos.copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = activeOp.sectorName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = activeOp.accentColor
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = lesson.headline,
                            style = MaterialTheme.typography.headlineMedium,
                            color = StarWhite
                        )
                    }
                }
            }

            // Concept Overview
            CosmicCard(borderColor = activeOp.accentColor.copy(alpha = 0.5f)) {
                Text(
                    text = "Objetivo de Aprendizaje",
                    style = MaterialTheme.typography.titleMedium,
                    color = activeOp.accentColor
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = activeOp.pedagogicalGoal,
                    style = MaterialTheme.typography.bodyLarge,
                    color = StarWhite
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = lesson.conceptSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MoonSilver
                )
            }

            // Interactive Visual Model Card
            CosmicCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lesson.visualTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = StarWhite,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        onClick = {
                            visualMultiplier = if (visualMultiplier >= 10) 3 else visualMultiplier + 1
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = ElevatedCosmic,
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
                        modifier = Modifier.testTag("learning_visual_adjust")
                    ) {
                        Text(
                            text = "Explorar ($visualMultiplier)",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MidnightOrbit,
                    border = BorderStroke(1.dp, OrbitBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(visualMultiplier.coerceAtMost(12)) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(activeOp.accentColor.copy(alpha = 0.25f))
                                        .border(1.5.dp, activeOp.accentColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = StarWhite
                                    )
                                }
                            }
                            if (activeOp == MathOperation.SUMA || activeOp == MathOperation.MULTIPLICACION) {
                                Text(
                                    text = if (activeOp == MathOperation.SUMA) "+" else "×",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = SolarYellow,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                repeat(lesson.rightGroupCount.coerceAtMost(6)) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(SolarYellow.copy(alpha = 0.2f))
                                            .border(1.5.dp, SolarYellow, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "★",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SolarYellow
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = lesson.visualCaption,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MoonSilver
                        )
                    }
                }
            }

            // Star Trick Card
            AstroBotHelperCard(
                title = lesson.starTrickTitle,
                message = lesson.starTrickBody,
                accentColor = SolarYellow
            )

            // Step by step example
            CosmicCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Ejemplo Resuelto Paso a Paso",
                        style = MaterialTheme.typography.titleMedium,
                        color = StarWhite
                    )
                }
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lesson.stepByStepExample.forEach { step ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MidnightOrbit,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = StarWhite,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            // Quick Check Interactive Card
            CosmicCard(borderColor = CosmicPurple.copy(alpha = 0.8f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = SolarYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Prueba Rápida de Comprensión",
                        style = MaterialTheme.typography.titleMedium,
                        color = SolarYellow
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = lesson.quickCheckPrompt,
                    style = MaterialTheme.typography.bodyLarge,
                    color = StarWhite
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lesson.quickCheckOptions.forEach { option ->
                        val selected = uiState.quickCheckSelected == option
                        val isCorrect = option == lesson.quickCheckAnswer
                        val bg = when {
                            selected && isCorrect -> SuccessContainer
                            selected && !isCorrect -> ErrorContainerDark
                            else -> MidnightOrbit
                        }
                        val border = when {
                            selected && isCorrect -> SuccessEmerald
                            selected && !isCorrect -> ErrorCoral
                            else -> OrbitBorder
                        }
                        Surface(
                            onClick = { onSelectQuickCheck(option) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 50.dp)
                                .testTag("learning_quick_option_$option"),
                            shape = RoundedCornerShape(14.dp),
                            color = bg,
                            border = BorderStroke(1.5.dp, border)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = option.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = StarWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (uiState.quickCheckSelected != null) {
                    val correct = uiState.quickCheckSelected == lesson.quickCheckAnswer
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (correct) SuccessContainer else SolarContainer,
                        border = BorderStroke(1.dp, if (correct) SuccessEmerald else SolarYellow),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = if (correct) SuccessEmerald else SolarYellow
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = if (correct) {
                                    "¡Excelente! ${lesson.quickCheckExplanation}"
                                } else {
                                    "Casi: prueba aplicar el truco. ${lesson.quickCheckExplanation}"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = StarWhite
                            )
                        }
                    }
                }
            }

            // Action CTAs
            PrimaryCosmicButton(
                text = "Practicar ${activeOp.label} en Reto Matemático",
                icon = Icons.Rounded.PlayArrow,
                onClick = { onStartChallenge(activeOp) },
                modifier = Modifier.testTag("learning_launch_challenge")
            )

            SecondaryCyanButton(
                text = "Aplicar en Laboratorio de Problemas",
                icon = Icons.Rounded.Science,
                onClick = onOpenLab,
                modifier = Modifier.testTag("learning_open_lab")
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

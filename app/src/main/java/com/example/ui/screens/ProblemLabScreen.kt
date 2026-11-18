package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.model.MathOperation
import com.example.ui.AstroUiState
import com.example.ui.components.AstroBotHelperCard
import com.example.ui.components.CosmicCard
import com.example.ui.components.OperationBadge
import com.example.ui.components.PrimaryCosmicButton
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
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProblemLabScreen(
    uiState: AstroUiState,
    onSelectProblem: (Int) -> Unit,
    onAdvanceToStep2: () -> Unit,
    onChooseOperation: (MathOperation) -> Unit,
    onSubmitAnswer: (Int) -> Unit,
    onToggleHint: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lab = uiState.lab
    val problem = lab.currentProblem
    val sound = LocalSpaceSoundManager.current

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
                eyebrow = "Resolución Guiada en 3 Pasos",
                title = "Laboratorio de Problemas",
                subtitle = "Analiza situaciones espaciales reales: detecta datos, elige la operación y resuelve."
            )

            // Problem Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lab.problems.forEachIndexed { idx, item ->
                    val isSelected = idx == lab.currentIndex
                    val solved = item.id in lab.solvedProblemIds
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectProblem(idx) },
                        leadingIcon = if (solved) {
                            {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "Resuelto",
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        label = {
                            Text(if (item.isTeacherCustom) "★ ${item.title}" else "Caso ${idx + 1}")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CosmicPurple,
                            selectedLabelColor = StarWhite,
                            containerColor = MidnightOrbit,
                            labelColor = MoonSilver
                        ),
                        modifier = Modifier.testTag("lab_case_$idx")
                    )
                }
            }

            if (problem != null) {
                // Step Indicator Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LabStepPill("1. Datos", active = lab.step >= 1, current = lab.step == 1, Modifier.weight(1f))
                    LabStepPill("2. Operación", active = lab.step >= 2, current = lab.step == 2, Modifier.weight(1f))
                    LabStepPill("3. Resolver", active = lab.step >= 3, current = lab.step == 3, Modifier.weight(1f))
                }

                // Story Card with Space Lab Art
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = NebulaSurface,
                    border = BorderStroke(1.dp, OrbitBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_planet_math_lab),
                                contentDescription = problem.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                DeepCosmos.copy(alpha = 0.25f),
                                                DeepCosmos.copy(alpha = 0.9f)
                                            )
                                        )
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Science,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = problem.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = StarWhite
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = problem.missionStory,
                                style = MaterialTheme.typography.bodyLarge,
                                color = StarWhite
                            )
                        }
                    }
                }

                // Step 1: Identify Data
                CosmicCard(
                    borderColor = if (lab.step == 1) NeonCyan else OrbitBorder
                ) {
                    Text(
                        text = "Paso 1 · Datos del Problema",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonCyan
                    )
                    Spacer(Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        problem.dataChips.forEach { chip ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ElevatedCosmic,
                                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.45f))
                            ) {
                                Text(
                                    text = chip,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = StarWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }

                    if (lab.step == 1) {
                        Spacer(Modifier.height(14.dp))
                        PrimaryCosmicButton(
                            text = "Datos Identificados -> Elegir Operación",
                            icon = Icons.AutoMirrored.Rounded.ArrowForward,
                            onClick = onAdvanceToStep2,
                            modifier = Modifier.testTag("lab_step1_next")
                        )
                    }
                }

                // Step 2: Choose Operation
                if (lab.step >= 2) {
                    CosmicCard(
                        borderColor = if (lab.step == 2) SolarYellow else OrbitBorder
                    ) {
                        Text(
                            text = "Paso 2 · ¿Qué operación resuelve el problema?",
                            style = MaterialTheme.typography.titleMedium,
                            color = SolarYellow
                        )
                        Spacer(Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MathOperation.entries.forEach { op ->
                                OperationBadge(
                                    operation = op,
                                    selected = lab.selectedOperation == op,
                                    onClick = { onChooseOperation(op) },
                                    modifier = Modifier.testTag("lab_op_${op.id}")
                                )
                            }
                        }

                        if (lab.operationFeedbackError) {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "Esa operación no coincide con la pregunta del caso. Revisa los datos y vuelve a intentar.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ErrorCoral
                            )
                        }
                    }
                }

                // Step 3: Solve & Verify
                if (lab.step >= 3) {
                    CosmicCard(borderColor = SuccessEmerald.copy(alpha = 0.7f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Paso 3 · Calcula y Verifica",
                                style = MaterialTheme.typography.titleMedium,
                                color = SuccessEmerald
                            )
                            Surface(
                                onClick = onToggleHint,
                                shape = RoundedCornerShape(10.dp),
                                color = MidnightOrbit,
                                border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.5f)),
                                modifier = Modifier.testTag("lab_hint_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Lightbulb,
                                        contentDescription = null,
                                        tint = SolarYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = if (lab.showBotHint) "Ocultar" else "Pista",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SolarYellow
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MidnightOrbit,
                            border = BorderStroke(1.dp, OrbitBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = problem.equationHint,
                                style = MaterialTheme.typography.headlineMedium,
                                color = StarWhite,
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            problem.answerOptions.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { opt ->
                                        val isSelected = lab.selectedAnswer == opt
                                        val isCorrect = opt == problem.correctAnswer
                                        val bg = when {
                                            isSelected && isCorrect -> SuccessContainer
                                            isSelected && !isCorrect -> ErrorContainerDark
                                            else -> MidnightOrbit
                                        }
                                        val border = when {
                                            isSelected && isCorrect -> SuccessEmerald
                                            isSelected && !isCorrect -> ErrorCoral
                                            else -> OrbitBorder
                                        }
                                        Surface(
                                            onClick = {
                                                sound?.play(if (isCorrect) SpaceSfx.STAR_CHIRP else SpaceSfx.SHIELD_THUD)
                                                onSubmitAnswer(opt)
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .heightIn(min = 56.dp)
                                                .testTag("lab_answer_$opt"),
                                            shape = RoundedCornerShape(14.dp),
                                            color = bg,
                                            border = BorderStroke(1.5.dp, border)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "$opt ${problem.unitLabel}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = StarWhite,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (lab.selectedAnswer != null) {
                            Spacer(Modifier.height(12.dp))
                            if (lab.isSolved) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = SuccessContainer,
                                    border = BorderStroke(1.dp, SuccessEmerald),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "¡Problema resuelto! +35 XP y +1 Estrella",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = SuccessEmerald
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = problem.botHint,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = StarWhite
                                        )
                                        if (lab.currentIndex < lab.problems.lastIndex) {
                                            Spacer(Modifier.height(10.dp))
                                            PrimaryCosmicButton(
                                                text = "Siguiente Caso del Laboratorio",
                                                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                                                onClick = { onSelectProblem(lab.currentIndex + 1) },
                                                containerColor = SuccessEmerald.copy(alpha = 0.85f),
                                                modifier = Modifier.testTag("lab_next_case")
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "Ese resultado no coincide. Usa la pista de AstroBot para comprobar tus cálculos.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ErrorCoral
                                )
                            }
                        }
                    }

                    if (lab.showBotHint) {
                        AstroBotHelperCard(
                            title = "Pista de AstroBot",
                            message = problem.botHint,
                            accentColor = SolarYellow
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LabStepPill(
    label: String,
    active: Boolean,
    current: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = when {
            current -> CosmicPurple.copy(alpha = 0.35f)
            active -> ElevatedCosmic
            else -> MidnightOrbit
        },
        border = BorderStroke(
            1.dp,
            when {
                current -> NeonCyan
                active -> OrbitBorder
                else -> OrbitBorder.copy(alpha = 0.5f)
            }
        )
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (active) StarWhite else MoonSilver
            )
        }
    }
}

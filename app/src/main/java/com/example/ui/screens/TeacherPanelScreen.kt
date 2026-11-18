package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.MathOperation
import com.example.ui.AstroUiState
import com.example.ui.components.CosmicCard
import com.example.ui.components.OperationBadge
import com.example.ui.components.PrimaryCosmicButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricTile
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.ElevatedCosmic
import com.example.ui.theme.ErrorCoral
import com.example.ui.theme.MidnightOrbit
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NebulaSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrbitBorder
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessEmerald

@Composable
fun TeacherPanelScreen(
    uiState: AstroUiState,
    onSaveSettings: (
        focusOperationId: String,
        challengeTimeSeconds: Int,
        questionsPerChallenge: Int,
        hintsEnabled: Boolean,
        classroomGoalAccuracy: Int
    ) -> Unit,
    onAddCustomProblem: (
        title: String,
        promptText: String,
        operationId: String,
        answer: Int,
        hintText: String,
        isWordProblem: Boolean
    ) -> Unit,
    onDeleteCustomProblem: (Int) -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.teacherSettings

    var focusOpId by rememberSaveable(settings.focusOperationId) {
        mutableStateOf(settings.focusOperationId)
    }
    var timerSec by rememberSaveable(settings.challengeTimeSeconds) {
        mutableIntStateOf(settings.challengeTimeSeconds)
    }
    var questionCount by rememberSaveable(settings.questionsPerChallenge) {
        mutableIntStateOf(settings.questionsPerChallenge)
    }
    var hintsEnabled by rememberSaveable(settings.hintsEnabled) {
        mutableStateOf(settings.hintsEnabled)
    }
    var goalAccuracy by rememberSaveable(settings.classroomGoalAccuracy) {
        mutableIntStateOf(settings.classroomGoalAccuracy)
    }

    // Custom activity builder state
    var problemTitle by rememberSaveable { mutableStateOf("") }
    var problemPrompt by rememberSaveable { mutableStateOf("") }
    var problemAnswer by rememberSaveable { mutableStateOf("") }
    var problemHint by rememberSaveable { mutableStateOf("") }
    var problemOpId by rememberSaveable { mutableStateOf(MathOperation.SUMA.id) }
    var isWordProblem by rememberSaveable { mutableStateOf(true) }
    var formError by rememberSaveable { mutableStateOf<String?>(null) }

    var showResetDialog by rememberSaveable { mutableStateOf(false) }

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
                eyebrow = "Centro de Control Educativo",
                title = "Panel del Docente",
                subtitle = "Supervisa el rendimiento de ${uiState.profile.studentName}, calibra los retos y asigna problemas propios."
            )

            // Student Diagnostic Overview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricTile(
                    label = "Precisión Actual",
                    value = "${uiState.overallAccuracyPercent}%",
                    accent = if (uiState.overallAccuracyPercent >= settings.classroomGoalAccuracy) SuccessEmerald else SolarYellow,
                    icon = Icons.Rounded.Analytics,
                    modifier = Modifier.weight(1f)
                )
                StatMetricTile(
                    label = "A Reforzar",
                    value = uiState.recommendedOperation.label,
                    accent = uiState.recommendedOperation.accentColor,
                    icon = Icons.Rounded.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }

            // Pedagogical Settings Card
            CosmicCard(borderColor = NeonCyan.copy(alpha = 0.55f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = null,
                        tint = NeonCyan
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Parámetros del Reto Matemático",
                        style = MaterialTheme.typography.titleLarge,
                        color = StarWhite
                    )
                }
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Operación prioritaria de la semana",
                    style = MaterialTheme.typography.labelLarge,
                    color = MoonSilver
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MathOperation.entries.forEach { op ->
                        OperationBadge(
                            operation = op,
                            selected = op.id == focusOpId,
                            onClick = { focusOpId = op.id },
                            modifier = Modifier.testTag("teacher_focus_${op.id}")
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                StepperConfigRow(
                    label = "Tiempo del reto",
                    valueLabel = "${timerSec}s",
                    onDecrement = { timerSec = (timerSec - 15).coerceAtLeast(30) },
                    onIncrement = { timerSec = (timerSec + 15).coerceAtMost(180) }
                )

                Spacer(Modifier.height(10.dp))

                StepperConfigRow(
                    label = "Preguntas por serie",
                    valueLabel = "$questionCount",
                    onDecrement = { questionCount = (questionCount - 1).coerceAtLeast(4) },
                    onIncrement = { questionCount = (questionCount + 1).coerceAtMost(12) }
                )

                Spacer(Modifier.height(10.dp))

                StepperConfigRow(
                    label = "Meta de precisión del aula",
                    valueLabel = "${goalAccuracy}%",
                    onDecrement = { goalAccuracy = (goalAccuracy - 5).coerceAtLeast(60) },
                    onIncrement = { goalAccuracy = (goalAccuracy + 5).coerceAtMost(100) }
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Permitir pistas de AstroBot",
                            style = MaterialTheme.typography.titleSmall,
                            color = StarWhite
                        )
                        Text(
                            text = "Muestra sugerencias estratégicas durante los retos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MoonSilver
                        )
                    }
                    Switch(
                        checked = hintsEnabled,
                        onCheckedChange = { hintsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StarWhite,
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = MoonSilver,
                            uncheckedTrackColor = MidnightOrbit
                        ),
                        modifier = Modifier.testTag("teacher_hints_switch")
                    )
                }

                Spacer(Modifier.height(14.dp))

                PrimaryCosmicButton(
                    text = "Guardar Configuración Docente",
                    icon = Icons.Rounded.Save,
                    onClick = {
                        onSaveSettings(focusOpId, timerSec, questionCount, hintsEnabled, goalAccuracy)
                    },
                    modifier = Modifier.testTag("teacher_save_settings_button")
                )
            }

            // Custom Activity Creator Card
            CosmicCard(borderColor = CosmicPurple.copy(alpha = 0.7f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = null,
                        tint = SolarYellow
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Crear Actividad Personalizada",
                        style = MaterialTheme.typography.titleLarge,
                        color = StarWhite
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Lo que publiques aquí aparecerá de inmediato en el Laboratorio o Reto Matemático del estudiante.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MoonSilver
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        onClick = { isWordProblem = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isWordProblem) CosmicPurple.copy(alpha = 0.35f) else MidnightOrbit,
                        border = BorderStroke(1.dp, if (isWordProblem) NeonCyan else OrbitBorder)
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Problema (Lab)",
                                style = MaterialTheme.typography.labelLarge,
                                color = StarWhite
                            )
                        }
                    }
                    Surface(
                        onClick = { isWordProblem = false },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = if (!isWordProblem) CosmicPurple.copy(alpha = 0.35f) else MidnightOrbit,
                        border = BorderStroke(1.dp, if (!isWordProblem) NeonCyan else OrbitBorder)
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cálculo (Reto)",
                                style = MaterialTheme.typography.labelLarge,
                                color = StarWhite
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MathOperation.entries.forEach { op ->
                        OperationBadge(
                            operation = op,
                            selected = op.id == problemOpId,
                            onClick = { problemOpId = op.id }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = problemTitle,
                    onValueChange = { problemTitle = it; formError = null },
                    label = { Text("Título breve (ej. Suministros a Marte)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_problem_title"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = OrbitBorder,
                        focusedTextColor = StarWhite,
                        unfocusedTextColor = StarWhite,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = MoonSilver,
                        focusedContainerColor = MidnightOrbit,
                        unfocusedContainerColor = MidnightOrbit
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = problemPrompt,
                    onValueChange = { problemPrompt = it; formError = null },
                    label = {
                        Text(
                            if (isWordProblem) "Enunciado del problema espacial" else "Expresión matemática (ej. 24 + 18)"
                        )
                    },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_problem_prompt"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = OrbitBorder,
                        focusedTextColor = StarWhite,
                        unfocusedTextColor = StarWhite,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = MoonSilver,
                        focusedContainerColor = MidnightOrbit,
                        unfocusedContainerColor = MidnightOrbit
                    )
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = problemAnswer,
                        onValueChange = { problemAnswer = it.filter { ch -> ch.isDigit() }; formError = null },
                        label = { Text("Respuesta exacta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("teacher_problem_answer"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = OrbitBorder,
                            focusedTextColor = StarWhite,
                            unfocusedTextColor = StarWhite,
                            focusedLabelColor = NeonCyan,
                            unfocusedLabelColor = MoonSilver,
                            focusedContainerColor = MidnightOrbit,
                            unfocusedContainerColor = MidnightOrbit
                        )
                    )
                    OutlinedTextField(
                        value = problemHint,
                        onValueChange = { problemHint = it },
                        label = { Text("Pista opcional") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("teacher_problem_hint"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = OrbitBorder,
                            focusedTextColor = StarWhite,
                            unfocusedTextColor = StarWhite,
                            focusedLabelColor = NeonCyan,
                            unfocusedLabelColor = MoonSilver,
                            focusedContainerColor = MidnightOrbit,
                            unfocusedContainerColor = MidnightOrbit
                        )
                    )
                }

                if (formError != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = formError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorCoral
                    )
                }

                Spacer(Modifier.height(12.dp))

                PrimaryCosmicButton(
                    text = "Publicar Actividad",
                    icon = Icons.Rounded.AddCircle,
                    onClick = {
                        val parsedAns = problemAnswer.toIntOrNull()
                        when {
                            problemTitle.isBlank() -> formError = "Escribe un título para la actividad."
                            problemPrompt.isBlank() -> formError = "Escribe el enunciado o la operación."
                            parsedAns == null -> formError = "Ingresa una respuesta numérica válida."
                            else -> {
                                onAddCustomProblem(
                                    problemTitle,
                                    problemPrompt,
                                    problemOpId,
                                    parsedAns,
                                    problemHint,
                                    isWordProblem
                                )
                                problemTitle = ""
                                problemPrompt = ""
                                problemAnswer = ""
                                problemHint = ""
                                formError = null
                            }
                        }
                    },
                    modifier = Modifier.testTag("teacher_add_problem_button")
                )

                if (uiState.customProblems.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "Actividades Publicadas (${uiState.customProblems.size})",
                        style = MaterialTheme.typography.titleSmall,
                        color = NeonCyan
                    )
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.customProblems.forEach { custom ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MidnightOrbit,
                                border = BorderStroke(1.dp, OrbitBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${custom.title} (${if (custom.isWordProblem) "Lab" else "Reto"})",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = StarWhite
                                        )
                                        Text(
                                            text = "${custom.promptText} -> Resp: ${custom.answer}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MoonSilver
                                        )
                                    }
                                    IconButton(
                                        onClick = { onDeleteCustomProblem(custom.id) },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DeleteOutline,
                                            contentDescription = "Eliminar actividad",
                                            tint = ErrorCoral
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Reset progress button
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
                    .testTag("teacher_reset_progress_button"),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ErrorCoral.copy(alpha = 0.65f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorCoral)
            ) {
                Icon(imageVector = Icons.Rounded.RestartAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Reiniciar Progreso del Estudiante", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = NebulaSurface,
            titleContentColor = StarWhite,
            textContentColor = MoonSilver,
            title = { Text("¿Reiniciar progreso?") },
            text = { Text("Se borrarán las sesiones recientes, XP y estrellas acumuladas de ${uiState.profile.studentName} en este dispositivo.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetProgress()
                    }
                ) {
                    Text("Reiniciar", color = ErrorCoral)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar", color = NeonCyan)
                }
            }
        )
    }
}

@Composable
private fun StepperConfigRow(
    label: String,
    valueLabel: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MidnightOrbit,
        border = BorderStroke(1.dp, OrbitBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = StarWhite,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = onDecrement,
                    shape = RoundedCornerShape(10.dp),
                    color = ElevatedCosmic,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("−", style = MaterialTheme.typography.titleLarge, color = StarWhite)
                    }
                }
                Text(
                    text = valueLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarYellow
                )
                Surface(
                    onClick = onIncrement,
                    shape = RoundedCornerShape(10.dp),
                    color = ElevatedCosmic,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("+", style = MaterialTheme.typography.titleLarge, color = StarWhite)
                    }
                }
            }
        }
    }
}

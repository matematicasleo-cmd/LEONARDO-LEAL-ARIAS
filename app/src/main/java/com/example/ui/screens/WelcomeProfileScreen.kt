package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.data.AstroContentCatalog
import com.example.model.DifficultyLevel
import com.example.model.SpaceAvatar
import com.example.ui.AstroUiState
import com.example.ui.components.AstroBotHelperCard
import com.example.ui.components.AvatarEmblem
import com.example.ui.components.CosmicCard
import com.example.ui.components.PrimaryCosmicButton
import com.example.ui.components.SecondaryCyanButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.XpLevelProgressBarCard
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.DeepCosmos
import com.example.ui.theme.ElevatedCosmic
import com.example.ui.theme.MidnightOrbit
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NebulaMuted
import com.example.ui.theme.NebulaSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrbitBorder
import com.example.ui.theme.SolarContainer
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessEmerald

@Composable
fun WelcomeProfileScreen(
    uiState: AstroUiState,
    onSaveAndLaunch: (name: String, grade: String, avatarId: String, difficultyId: String) -> Unit,
    onOpenRewards: () -> Unit,
    onOpenTeacherPanel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by rememberSaveable(uiState.profile.studentName) {
        mutableStateOf(uiState.profile.studentName)
    }
    var gradeInput by rememberSaveable(uiState.profile.gradeLevel) {
        mutableStateOf(uiState.profile.gradeLevel)
    }
    var selectedAvatarId by rememberSaveable(uiState.profile.avatarId) {
        mutableStateOf(uiState.profile.avatarId)
    }
    var selectedDifficultyId by rememberSaveable(uiState.profile.difficultyId) {
        mutableStateOf(uiState.profile.difficultyId)
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
            // Hero Banner Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = NebulaSurface,
                border = BorderStroke(1.dp, OrbitBorder)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_space_hero_banner),
                        contentDescription = "Academia espacial AstroMate",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        DeepCosmos.copy(alpha = 0.25f),
                                        DeepCosmos.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CosmicPurple.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "ACADEMIA ESPACIAL ASTROMATE",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCyan,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Bienvenido a la Expedición Numérica",
                            style = MaterialTheme.typography.headlineLarge,
                            color = StarWhite
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Rango: ${uiState.studentRank} • Racha: ${uiState.profile.streakDays}d • ${uiState.profile.totalXp} XP • ${uiState.profile.totalStars} ★",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SolarYellow
                        )
                    }
                }
            }

            XpLevelProgressBarCard(
                levelInfo = uiState.levelInfo,
                cardTestTag = "welcome_xp_progress_card",
                barTestTag = "welcome_xp_progress_bar"
            )

            ConsecutiveMissionDaysCard(
                streakDays = uiState.profile.streakDays,
                bestStreakDays = maxOf(uiState.profile.streakDays, uiState.profile.bestStreakDays),
                dailyCompletedToday = uiState.dailyMission.completed,
                unlockedBadgeIds = uiState.unlockedBadgeIds,
                onOpenRewards = onOpenRewards
            )

            AstroBotHelperCard(
                title = "¡Listo para despegar, ${nameInput.ifBlank { "Cadete" }}!",
                message = "Personaliza tu comandante, elige tu nivel matemático y conquista los 5 sectores de la galaxia."
            )

            // Profile Form Card
            CosmicCard {
                SectionHeader(
                    eyebrow = "Identidad del Estudiante",
                    title = "Datos de la Tripulación",
                    subtitle = "Tu nombre y grado adaptan el tono y el progreso guardado."
                )
                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { if (it.length <= 28) nameInput = it },
                    label = { Text("Nombre del estudiante") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = OrbitBorder,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = MoonSilver,
                        focusedTextColor = StarWhite,
                        unfocusedTextColor = StarWhite,
                        focusedContainerColor = MidnightOrbit,
                        unfocusedContainerColor = MidnightOrbit
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = gradeInput,
                    onValueChange = { if (it.length <= 24) gradeInput = it },
                    label = { Text("Grado o curso (ej. 5.º Primaria)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_grade_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = OrbitBorder,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = MoonSilver,
                        focusedTextColor = StarWhite,
                        unfocusedTextColor = StarWhite,
                        focusedContainerColor = MidnightOrbit,
                        unfocusedContainerColor = MidnightOrbit
                    )
                )
            }

            // Avatar Selector Card
            CosmicCard {
                SectionHeader(
                    eyebrow = "Personaje Espacial",
                    title = "Elige tu Avatar",
                    subtitle = "Selecciona al compañero que te representará en el mapa."
                )
                Spacer(Modifier.height(14.dp))

                val avatars = SpaceAvatar.entries
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    avatars.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { avatar ->
                                val isSelected = avatar.id == selectedAvatarId
                                Surface(
                                    onClick = { selectedAvatarId = avatar.id },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("avatar_option_${avatar.id}"),
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (isSelected) ElevatedCosmic else MidnightOrbit,
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) NeonCyan else OrbitBorder
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AvatarEmblem(
                                            avatar = avatar,
                                            size = 44.dp,
                                            highlighted = isSelected
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = avatar.displayName,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = StarWhite,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = avatar.title,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSelected) NeonCyan else MoonSilver,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Rounded.CheckCircle,
                                                contentDescription = "Seleccionado",
                                                tint = NeonCyan,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Difficulty Selector Card
            CosmicCard {
                SectionHeader(
                    eyebrow = "Calibración Pedagógica",
                    title = "Nivel de Dificultad",
                    subtitle = "Ajusta el rango numérico de los retos matemáticos."
                )
                Spacer(Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DifficultyLevel.entries.forEach { level ->
                        val isSelected = level.id == selectedDifficultyId
                        Surface(
                            onClick = { selectedDifficultyId = level.id },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("difficulty_option_${level.id}"),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) CosmicPurple.copy(alpha = 0.25f) else MidnightOrbit,
                            border = BorderStroke(
                                width = if (isSelected) 1.8.dp else 1.dp,
                                color = if (isSelected) SolarYellow else OrbitBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = level.label,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) SolarYellow else StarWhite
                                    )
                                    Text(
                                        text = "Recomendado: ${level.gradeHint} (números hasta ${level.maxOperand})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MoonSilver
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Nivel activo",
                                        tint = SolarYellow
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Primary Actions
            PrimaryCosmicButton(
                text = "Guardar Perfil e Ir al Mapa de Misiones",
                icon = Icons.Rounded.RocketLaunch,
                onClick = {
                    onSaveAndLaunch(
                        nameInput,
                        gradeInput,
                        selectedAvatarId,
                        selectedDifficultyId
                    )
                },
                modifier = Modifier.testTag("save_profile_launch_button")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryCyanButton(
                    text = "Recompensas",
                    icon = Icons.Rounded.EmojiEvents,
                    onClick = onOpenRewards,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("profile_open_rewards_button")
                )
                SecondaryCyanButton(
                    text = "Panel Docente",
                    icon = Icons.Rounded.School,
                    onClick = onOpenTeacherPanel,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("profile_open_teacher_button")
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConsecutiveMissionDaysCard(
    streakDays: Int,
    bestStreakDays: Int,
    dailyCompletedToday: Boolean,
    unlockedBadgeIds: Set<String>,
    onOpenRewards: () -> Unit
) {
    val sound = LocalSpaceSoundManager.current
    val weeklyBadges = AstroContentCatalog.badges.filter { it.isWeeklyBadge }
    val currentWeekNumber = (streakDays / 7) + 1
    val weekProgressDays = when {
        streakDays <= 0 -> 0
        streakDays % 7 == 0 -> 7
        else -> streakDays % 7
    }
    val weekRatio = (weekProgressDays.toFloat() / 7f).coerceIn(0f, 1f)

    CosmicCard(
        borderColor = SolarYellow.copy(alpha = 0.72f),
        modifier = Modifier.testTag("profile_consecutive_days_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SolarContainer)
                        .border(1.5.dp, SolarYellow.copy(alpha = 0.7f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = "Contador de días consecutivos",
                        tint = SolarYellow,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "CONSTANCIA SEMANAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                    Text(
                        text = "Días de Misión Consecutivos",
                        style = MaterialTheme.typography.titleMedium,
                        color = StarWhite
                    )
                    Text(
                        text = "Mejor marca: $bestStreakDays ${if (bestStreakDays == 1) "día" else "días"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MoonSilver
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MidnightOrbit,
                border = BorderStroke(1.5.dp, SolarYellow.copy(alpha = 0.8f)),
                modifier = Modifier.testTag("profile_consecutive_days_counter")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$streakDays",
                        style = MaterialTheme.typography.headlineLarge,
                        color = SolarYellow,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (streakDays == 1) "día" else "días",
                        style = MaterialTheme.typography.labelSmall,
                        color = MoonSilver
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // 7-day weekly progress header & bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (streakDays > 0 && streakDays % 7 == 0) {
                    "¡Semana ${streakDays / 7} completada!"
                } else {
                    "Semana $currentWeekNumber • $weekProgressDays/7 días"
                },
                style = MaterialTheme.typography.labelMedium,
                color = StarWhite
            )
            Text(
                text = if (dailyCompletedToday) "Misión de hoy completada" else "Completa la misión de hoy",
                style = MaterialTheme.typography.labelSmall,
                color = if (dailyCompletedToday) SuccessEmerald else NeonCyan
            )
        }

        Spacer(Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { weekRatio },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = if (weekProgressDays == 7) SuccessEmerald else SolarYellow,
            trackColor = MidnightOrbit
        )

        Spacer(Modifier.height(10.dp))

        // 7 visual day nodes (D1 - D7)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (day in 1..7) {
                val achieved = day <= weekProgressDays
                val isWeekTarget = day == 7
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    achieved && isWeekTarget -> SuccessContainer
                                    achieved -> SolarContainer
                                    else -> MidnightOrbit
                                }
                            )
                            .border(
                                width = if (isWeekTarget || achieved) 1.5.dp else 1.dp,
                                color = when {
                                    achieved && isWeekTarget -> SuccessEmerald
                                    achieved -> SolarYellow
                                    isWeekTarget -> NeonCyan.copy(alpha = 0.6f)
                                    else -> OrbitBorder
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                isWeekTarget -> Icons.Rounded.EmojiEvents
                                achieved -> Icons.Rounded.Star
                                else -> Icons.Rounded.AutoAwesome
                            },
                            contentDescription = "Día $day",
                            tint = when {
                                achieved && isWeekTarget -> SuccessEmerald
                                achieved -> SolarYellow
                                isWeekTarget -> NeonCyan
                                else -> NebulaMuted.copy(alpha = 0.6f)
                            },
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Text(
                        text = "D$day",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (achieved) StarWhite else NebulaMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Weekly Special Badges Preview Chips
        Text(
            text = "Insignias especiales por consistencia semanal",
            style = MaterialTheme.typography.labelMedium,
            color = MoonSilver
        )
        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            weeklyBadges.forEach { badge ->
                val isUnlocked = badge.id in unlockedBadgeIds
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isUnlocked) ElevatedCosmic else MidnightOrbit,
                    border = BorderStroke(
                        1.dp,
                        if (isUnlocked) SuccessEmerald.copy(alpha = 0.75f) else OrbitBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Rounded.EmojiEvents else Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = if (isUnlocked) SolarYellow else NebulaMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${badge.targetStreakDays}d • ${badge.title.substringBefore(" (")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUnlocked) StarWhite else NebulaMuted
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                sound?.play(SpaceSfx.SOFT_BEEP)
                onOpenRewards()
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .testTag("profile_weekly_badges_button"),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.7f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Ver Insignias Semanales en Recompensas", style = MaterialTheme.typography.labelLarge)
        }
    }
}


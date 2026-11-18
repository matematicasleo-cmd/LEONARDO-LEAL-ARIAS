package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AstroContentCatalog
import com.example.model.MathOperation
import com.example.model.MissionDefinition
import com.example.ui.AstroUiState
import com.example.ui.components.CosmicCard
import com.example.ui.components.DailyCosmicMissionCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StarRatingRow
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
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.SuccessEmerald

@Composable
fun MissionsMapScreen(
    uiState: AstroUiState,
    onStartLearning: (MathOperation) -> Unit,
    onLaunchChallenge: (MissionDefinition) -> Unit,
    onLaunchDailyMission: () -> Unit,
    onOpenProblemLab: () -> Unit,
    onOpenRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilterId by rememberSaveable { mutableStateOf("all") }

    val filteredMissions = AstroContentCatalog.missions.filter {
        selectedFilterId == "all" || it.operation.id == selectedFilterId
    }
    val completedCount = uiState.completedMissionsCount
    val totalCount = AstroContentCatalog.missions.size
    val progressRatio = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxSize()
                .testTag("missions_lazy_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Cosmic Galaxy Progress Header
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = NebulaSurface,
                    border = BorderStroke(1.dp, OrbitBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_space_hero_banner),
                            contentDescription = "Ruta galáctica de misiones",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            DeepCosmos.copy(alpha = 0.3f),
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
                            SectionHeader(
                                eyebrow = "Ruta Pedagógica Espacial",
                                title = "Mapa de Misiones",
                                subtitle = "$completedCount de $totalCount planetas conquistados • Meta docente: ${uiState.teacherSettings.classroomGoalAccuracy}%"
                            )
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { progressRatio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = NeonCyan,
                                trackColor = MidnightOrbit
                            )
                        }
                    }
                }
            }

            item {
                XpLevelProgressBarCard(
                    levelInfo = uiState.levelInfo,
                    cardTestTag = "missions_xp_progress_card",
                    barTestTag = "missions_xp_progress_bar"
                )
            }

            item {
                DailyCosmicMissionCard(
                    dailyMission = uiState.dailyMission,
                    countdownText = uiState.dailyCountdownText,
                    streakDays = uiState.profile.streakDays,
                    onLaunchDailyMission = onLaunchDailyMission,
                    onLearnDailyOperation = onStartLearning
                )
            }

            item {
                // Quick shortcuts to Lab and Rewards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = onOpenProblemLab,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("map_shortcut_lab"),
                        shape = RoundedCornerShape(16.dp),
                        color = ElevatedCosmic,
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Science,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Laboratorio",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = StarWhite
                                )
                                Text(
                                    text = "Problemas paso a paso",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MoonSilver
                                )
                            }
                        }
                    }

                    Surface(
                        onClick = onOpenRewards,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("map_shortcut_rewards"),
                        shape = RoundedCornerShape(16.dp),
                        color = ElevatedCosmic,
                        border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = null,
                                tint = SolarYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Recompensas",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = StarWhite
                                )
                                Text(
                                    text = "${uiState.unlockedBadgeIds.size} insignias",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MoonSilver
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Sector filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilterId == "all",
                        onClick = { selectedFilterId = "all" },
                        label = { Text("Todos los sectores") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CosmicPurple,
                            selectedLabelColor = StarWhite,
                            containerColor = MidnightOrbit,
                            labelColor = MoonSilver
                        ),
                        modifier = Modifier.testTag("filter_all")
                    )
                    MathOperation.entries.forEach { op ->
                        FilterChip(
                            selected = selectedFilterId == op.id,
                            onClick = { selectedFilterId = op.id },
                            label = { Text("${op.symbol} ${op.label}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = op.accentColor.copy(alpha = 0.28f),
                                selectedLabelColor = StarWhite,
                                containerColor = MidnightOrbit,
                                labelColor = MoonSilver
                            ),
                            modifier = Modifier.testTag("filter_${op.id}")
                        )
                    }
                }
            }

            itemsIndexed(filteredMissions, key = { _, item -> item.id }) { _, mission ->
                val progress = uiState.missionProgress[mission.id]
                val isUnlocked = progress?.unlocked ?: (mission.order <= 3)
                val isCompleted = progress?.completed ?: false
                val stars = progress?.starsEarned ?: 0
                val bestAcc = progress?.bestAccuracy ?: 0

                MissionNodeCard(
                    mission = mission,
                    isUnlocked = isUnlocked,
                    isCompleted = isCompleted,
                    stars = stars,
                    bestAccuracy = bestAcc,
                    isTeacherFocus = uiState.teacherSettings.focusOperationId == mission.operation.id,
                    onLearnClick = { onStartLearning(mission.operation) },
                    onPlayClick = { onLaunchChallenge(mission) }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun MissionNodeCard(
    mission: MissionDefinition,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    stars: Int,
    bestAccuracy: Int,
    isTeacherFocus: Boolean,
    onLearnClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    val accent = mission.operation.accentColor
    val borderColor = when {
        isCompleted -> SuccessEmerald.copy(alpha = 0.7f)
        isUnlocked -> accent.copy(alpha = 0.75f)
        else -> OrbitBorder.copy(alpha = 0.5f)
    }

    CosmicCard(
        borderColor = borderColor,
        containerColor = if (isUnlocked) NebulaSurface else MidnightOrbit.copy(alpha = 0.75f),
        modifier = Modifier.testTag("mission_card_${mission.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Planet Emblem
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) accent.copy(alpha = 0.22f) else ElevatedCosmic.copy(alpha = 0.5f)
                    )
                    .border(
                        width = 2.dp,
                        color = if (isUnlocked) accent else NebulaMuted.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(
                        text = mission.operation.symbol,
                        style = MaterialTheme.typography.headlineMedium,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Misión bloqueada",
                        tint = NebulaMuted
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${mission.planetCode} • ${mission.operation.sectorName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUnlocked) accent else NebulaMuted
                    )
                    if (isTeacherFocus) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SolarYellow.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "PRIORIDAD DOCENTE",
                                style = MaterialTheme.typography.labelSmall,
                                color = SolarYellow,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${mission.order}. ${mission.title}",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (isUnlocked) StarWhite else NebulaMuted
                    )
                    if (isCompleted) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = "Completada",
                            tint = SuccessEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = mission.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUnlocked) MoonSilver else NebulaMuted
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StarRatingRow(stars = stars)
            Text(
                text = if (bestAccuracy > 0) "Mejor: $bestAccuracy% • +${mission.xpReward} XP" else "Meta: ${mission.targetAccuracy}% • +${mission.xpReward} XP",
                style = MaterialTheme.typography.labelMedium,
                color = if (isUnlocked) SolarYellow else NebulaMuted
            )
        }

        if (isUnlocked) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onLearnClick,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .testTag("mission_learn_${mission.id}"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.7f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Aprender", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .testTag("mission_play_${mission.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CosmicPurple,
                        contentColor = StarWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Reto", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

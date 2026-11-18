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
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.data.AstroContentCatalog
import com.example.ui.AstroUiState
import com.example.ui.components.CosmicCard
import com.example.ui.components.SectionHeader
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
fun RewardsScreen(
    uiState: AstroUiState,
    onUnlockOrEquipPerk: (perkId: String, cost: Int, title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val weeklyBadges = AstroContentCatalog.badges.filter { !it.isPerk && it.isWeeklyBadge }
    val badges = AstroContentCatalog.badges.filter { !it.isPerk && !it.isWeeklyBadge }
    val perks = AstroContentCatalog.badges.filter { it.isPerk }
    val unlocked = uiState.unlockedBadgeIds
    val sound = LocalSpaceSoundManager.current
    val bestStreak = maxOf(uiState.profile.streakDays, uiState.profile.bestStreakDays)
    val levelInfo = uiState.levelInfo
    val rankProgress = levelInfo.progressFraction

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
            // Trophy Vault Banner
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = NebulaSurface,
                border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cosmic_trophy_vault),
                        contentDescription = "Bóveda de recompensas galácticas",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        DeepCosmos.copy(alpha = 0.2f),
                                        DeepCosmos.copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                    ) {
                        SectionHeader(
                            eyebrow = "Bóveda Galáctica",
                            title = "Recompensas y Rangos",
                            subtitle = "Rango: ${uiState.studentRank} • ${uiState.profile.totalStars} estrellas disponibles",
                            eyebrowColor = SolarYellow
                        )
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { rankProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = SolarYellow,
                            trackColor = MidnightOrbit
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${levelInfo.xpInCurrentLevel} / ${levelInfo.xpRequiredForNextLevel} XP • Faltan ${levelInfo.xpRemainingToNextLevel} XP para Nivel ${levelInfo.level + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MoonSilver
                        )
                    }
                }
            }

            // Weekly Consistency Special Badges
            CosmicCard(
                borderColor = NeonCyan.copy(alpha = 0.6f),
                modifier = Modifier.testTag("rewards_weekly_badges_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = null,
                            tint = SolarYellow,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Consistencia Semanal",
                            style = MaterialTheme.typography.titleLarge,
                            color = StarWhite
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SolarContainer,
                        border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "${uiState.profile.streakDays}d racha",
                            style = MaterialTheme.typography.labelSmall,
                            color = SolarYellow,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Insignias especiales por mantener Días de Misión Consecutivos (mejor marca: $bestStreak días).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MoonSilver
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    weeklyBadges.forEach { badge ->
                        val isEarned = badge.id in unlocked
                        val progressRatio = if (badge.targetStreakDays > 0) {
                            (bestStreak.toFloat() / badge.targetStreakDays.toFloat()).coerceIn(0f, 1f)
                        } else 0f

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isEarned) ElevatedCosmic else MidnightOrbit,
                            border = BorderStroke(
                                1.2.dp,
                                if (isEarned) SuccessEmerald.copy(alpha = 0.75f) else NeonCyan.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(if (isEarned) SuccessContainer else NebulaSurface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isEarned) Icons.Rounded.EmojiEvents else Icons.Rounded.Lock,
                                            contentDescription = badge.title,
                                            tint = if (isEarned) SolarYellow else NebulaMuted,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = badge.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (isEarned) StarWhite else NebulaMuted
                                        )
                                        Text(
                                            text = badge.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MoonSilver
                                        )
                                        Spacer(Modifier.height(3.dp))
                                        Text(
                                            text = if (isEarned) {
                                                "Insignia especial desbloqueada"
                                            } else {
                                                "Progreso: $bestStreak / ${badge.targetStreakDays} días consecutivos"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isEarned) SuccessEmerald else NeonCyan
                                        )
                                    }
                                }
                                if (!isEarned) {
                                    Spacer(Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { progressRatio },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(6.dp)),
                                        color = NeonCyan,
                                        trackColor = NebulaSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Achievement Badges
            CosmicCard {
                Text(
                    text = "Insignias Galácticas",
                    style = MaterialTheme.typography.titleLarge,
                    color = StarWhite
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Desbloquéalas completando misiones, retos de precisión y experiencia.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MoonSilver
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    badges.forEach { badge ->
                        val isEarned = badge.id in unlocked
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isEarned) ElevatedCosmic else MidnightOrbit,
                            border = BorderStroke(
                                1.dp,
                                if (isEarned) SolarYellow.copy(alpha = 0.7f) else OrbitBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isEarned) SolarContainer else NebulaSurface
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isEarned) Icons.Rounded.EmojiEvents else Icons.Rounded.Lock,
                                        contentDescription = badge.title,
                                        tint = if (isEarned) SolarYellow else NebulaMuted,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = badge.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isEarned) StarWhite else NebulaMuted
                                    )
                                    Text(
                                        text = badge.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MoonSilver
                                    )
                                    Spacer(Modifier.height(3.dp))
                                    Text(
                                        text = if (isEarned) "Desbloqueada" else "Meta: ${badge.requirementLabel}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isEarned) SuccessEmerald else NeonCyan
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Star Shop / Ship Upgrades
            CosmicCard(borderColor = NeonCyan.copy(alpha = 0.5f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.RocketLaunch,
                        contentDescription = null,
                        tint = NeonCyan
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Hangar de Mejoras Estelares",
                        style = MaterialTheme.typography.titleLarge,
                        color = StarWhite
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Canjea tus estrellas por mejoras para tu nave espacial.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MoonSilver
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    perks.forEach { perk ->
                        val isOwned = perk.id in uiState.unlockedPerks
                        val isEquipped = uiState.profile.equippedPerkId == perk.id
                        val canAfford = uiState.profile.totalStars >= perk.starCostToUnlockPerk

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MidnightOrbit,
                            border = BorderStroke(
                                1.2.dp,
                                if (isEquipped) NeonCyan else OrbitBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isEquipped) Icons.Rounded.CheckCircle else Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (isEquipped) NeonCyan else SolarYellow,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = perk.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = StarWhite
                                    )
                                    Text(
                                        text = perk.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MoonSilver
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        sound?.play(SpaceSfx.STAR_CHIRP)
                                        onUnlockOrEquipPerk(perk.id, perk.starCostToUnlockPerk, perk.title)
                                    },
                                    enabled = !isEquipped && (isOwned || canAfford),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isOwned) NeonCyan else CosmicPurple,
                                        contentColor = if (isOwned) DeepCosmos else StarWhite,
                                        disabledContainerColor = ElevatedCosmic,
                                        disabledContentColor = NebulaMuted
                                    ),
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .testTag("reward_perk_${perk.id}")
                                ) {
                                    when {
                                        isEquipped -> Text("Equipada")
                                        isOwned -> Text("Equipar")
                                        else -> {
                                            Icon(
                                                imageVector = Icons.Rounded.Star,
                                                contentDescription = null,
                                                tint = SolarYellow,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text("${perk.starCostToUnlockPerk}")
                                        }
                                    }
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

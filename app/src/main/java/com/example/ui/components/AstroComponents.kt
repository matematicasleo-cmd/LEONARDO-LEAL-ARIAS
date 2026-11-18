package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.data.local.DailyMissionEntity
import com.example.model.MathOperation
import com.example.model.SpaceAvatar
import com.example.model.XpLevelInfo
import com.example.ui.theme.AstralBlue
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.DeepCosmos
import com.example.ui.theme.ElevatedCosmic
import com.example.ui.theme.MidnightOrbit
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NebulaMuted
import com.example.ui.theme.NebulaSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrbitBorder
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.SolarContainer
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite
import com.example.ui.theme.StarlightCard
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessEmerald

@Composable
fun CosmicBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepCosmos, MidnightOrbit, Color(0xFF0E0B24))
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            // Atmospheric nebula glows
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CosmicPurple.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(w * 0.85f, h * 0.12f),
                    radius = w * 0.55f
                ),
                radius = w * 0.55f,
                center = Offset(w * 0.85f, h * 0.12f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AstralBlue.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(w * 0.15f, h * 0.65f),
                    radius = w * 0.5f
                ),
                radius = w * 0.5f,
                center = Offset(w * 0.15f, h * 0.65f)
            )
            // Deterministic crisp stars proportional to viewport
            val starCoords = listOf(
                0.08f to 0.09f, 0.22f to 0.16f, 0.45f to 0.07f, 0.72f to 0.13f,
                0.91f to 0.21f, 0.14f to 0.34f, 0.62f to 0.29f, 0.83f to 0.44f,
                0.06f to 0.57f, 0.37f to 0.52f, 0.55f to 0.68f, 0.92f to 0.61f,
                0.19f to 0.78f, 0.48f to 0.84f, 0.77f to 0.79f, 0.31f to 0.93f
            )
            starCoords.forEachIndexed { idx, (rx, ry) ->
                val radius = if (idx % 3 == 0) 2.6f else 1.6f
                val color = when {
                    idx % 5 == 0 -> NeonCyan.copy(alpha = 0.45f)
                    idx % 4 == 0 -> SolarYellow.copy(alpha = 0.4f)
                    else -> StarWhite.copy(alpha = 0.32f)
                }
                drawCircle(color = color, radius = radius, center = Offset(w * rx, h * ry))
            }
        }
        content()
    }
}

@Composable
fun WarpJumpOverlay(
    routeKey: String,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(1f) }
    var isFirstComposition by remember { mutableStateOf(true) }

    LaunchedEffect(routeKey) {
        if (isFirstComposition) {
            isFirstComposition = false
            return@LaunchedEffect
        }
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
        )
    }

    val t = progress.value
    if (t >= 0.99f) return

    // Envelope rises rapidly and fades out smoothly so it never lingers
    val envelope = kotlin.math.sin(t * Math.PI).toFloat().coerceIn(0f, 1f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val centerX = w * 0.5f
        val centerY = h * 0.42f

        // 1. Subtle radial warp-jump star streaks around periphery
        val streakAngles = listOf(
            -2.65f, -2.15f, -1.65f, -0.95f, -0.45f,
            0.35f, 0.85f, 1.55f, 2.2f, 2.75f
        )
        streakAngles.forEachIndexed { idx, angle ->
            val cosA = kotlin.math.cos(angle)
            val sinA = kotlin.math.sin(angle)
            val startDist = (w * 0.24f) + (t * w * 0.34f)
            val streakLen = (w * 0.12f) + (envelope * w * 0.14f)
            val p1 = Offset(centerX + cosA * startDist, centerY + sinA * startDist)
            val p2 = Offset(
                centerX + cosA * (startDist + streakLen),
                centerY + sinA * (startDist + streakLen)
            )
            val streakColor = when {
                idx % 3 == 0 -> NeonCyan.copy(alpha = 0.28f * envelope)
                idx % 3 == 1 -> SoftLavender.copy(alpha = 0.24f * envelope)
                else -> SolarYellow.copy(alpha = 0.22f * envelope)
            }
            drawLine(
                color = streakColor,
                start = p1,
                end = p2,
                strokeWidth = if (idx % 2 == 0) 2.4f else 1.6f,
                cap = StrokeCap.Round
            )
        }

        // 2. Subtle asteroid fly-by silhouettes gliding across upper/lower corners
        val rock1X = w * (1.05f - t * 1.25f)
        val rock1Y = h * (0.18f + t * 0.16f)
        val rock1Radius = 15.dp.toPx()
        val rock1Path = Path().apply {
            moveTo(rock1X - rock1Radius, rock1Y - rock1Radius * 0.2f)
            lineTo(rock1X - rock1Radius * 0.3f, rock1Y - rock1Radius * 0.9f)
            lineTo(rock1X + rock1Radius * 0.7f, rock1Y - rock1Radius * 0.55f)
            lineTo(rock1X + rock1Radius * 0.95f, rock1Y + rock1Radius * 0.3f)
            lineTo(rock1X + rock1Radius * 0.15f, rock1Y + rock1Radius * 0.85f)
            lineTo(rock1X - rock1Radius * 0.8f, rock1Y + rock1Radius * 0.45f)
            close()
        }
        drawPath(
            path = rock1Path,
            color = ElevatedCosmic.copy(alpha = 0.42f * envelope)
        )
        drawPath(
            path = rock1Path,
            color = NeonCyan.copy(alpha = 0.32f * envelope),
            style = Stroke(width = 1.5.dp.toPx())
        )
        drawLine(
            color = NeonCyan.copy(alpha = 0.18f * envelope),
            start = Offset(rock1X + rock1Radius, rock1Y - rock1Radius * 0.2f),
            end = Offset(rock1X + rock1Radius * 3.8f, rock1Y - rock1Radius * 0.95f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        val rock2X = w * (-0.08f + t * 1.18f)
        val rock2Y = h * (0.78f - t * 0.14f)
        val rock2Radius = 11.dp.toPx()
        val rock2Path = Path().apply {
            moveTo(rock2X - rock2Radius * 0.9f, rock2Y)
            lineTo(rock2X - rock2Radius * 0.2f, rock2Y - rock2Radius * 0.85f)
            lineTo(rock2X + rock2Radius * 0.8f, rock2Y - rock2Radius * 0.3f)
            lineTo(rock2X + rock2Radius * 0.6f, rock2Y + rock2Radius * 0.75f)
            lineTo(rock2X - rock2Radius * 0.5f, rock2Y + rock2Radius * 0.65f)
            close()
        }
        drawPath(
            path = rock2Path,
            color = ElevatedCosmic.copy(alpha = 0.36f * envelope)
        )
        drawPath(
            path = rock2Path,
            color = CosmicPurple.copy(alpha = 0.35f * envelope),
            style = Stroke(width = 1.3.dp.toPx())
        )
    }
}

@Composable
fun AstroTopBar(
    studentName: String,
    rankTitle: String,
    avatar: SpaceAvatar,
    totalStars: Int,
    totalXp: Int,
    screenTitle: String,
    showBackButton: Boolean,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onRewardsClick: () -> Unit,
    onTeacherClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sound = LocalSpaceSoundManager.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        color = NebulaSurface.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, OrbitBorder.copy(alpha = 0.75f)),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = StarWhite
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            text = screenTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = StarWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$studentName • $totalXp XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(onClick = onProfileClick)
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .testTag("top_bar_profile_button")
                    ) {
                        AvatarEmblem(avatar = avatar, size = 40.dp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = studentName,
                                style = MaterialTheme.typography.titleSmall,
                                color = StarWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = rankTitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCyan,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = onToggleMute,
                    shape = RoundedCornerShape(14.dp),
                    color = MidnightOrbit,
                    border = BorderStroke(1.dp, OrbitBorder),
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("top_bar_sound_toggle")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isMuted) Icons.AutoMirrored.Rounded.VolumeOff else Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = if (isMuted) "Activar sonido espacial" else "Silenciar sonido espacial",
                            tint = if (isMuted) NebulaMuted else NeonCyan,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                Surface(
                    onClick = {
                        sound?.play(SpaceSfx.STAR_CHIRP)
                        onRewardsClick()
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = SolarContainer,
                    border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.55f)),
                    modifier = Modifier
                        .heightIn(min = 40.dp)
                        .testTag("top_bar_rewards_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = stringResource(R.string.cd_rewards_button),
                            tint = SolarYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = totalStars.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = SolarYellow
                        )
                    }
                }

                Surface(
                    onClick = {
                        sound?.play(SpaceSfx.SOFT_BEEP)
                        onTeacherClick()
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = ElevatedCosmic,
                    border = BorderStroke(1.dp, OrbitBorder),
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("top_bar_teacher_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.School,
                            contentDescription = stringResource(R.string.cd_teacher_panel),
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarEmblem(
    avatar: SpaceAvatar,
    size: Dp = 48.dp,
    highlighted: Boolean = false
) {
    val icon = when (avatar) {
        SpaceAvatar.COMANDANTE_NOVA -> Icons.Rounded.RocketLaunch
        SpaceAvatar.PILOTO_ORION -> Icons.Rounded.Flight
        SpaceAvatar.INGENIERA_VEGA -> Icons.Rounded.Engineering
        SpaceAvatar.ASTROBOT_X7 -> Icons.Rounded.SmartToy
    }
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(avatar.accentColor.copy(alpha = 0.35f), ElevatedCosmic)
                )
            )
            .border(
                width = if (highlighted) 2.5.dp else 1.5.dp,
                color = if (highlighted) SolarYellow else avatar.accentColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = avatar.displayName,
            tint = StarWhite,
            modifier = Modifier.size(size * 0.54f)
        )
    }
}

@Composable
fun CosmicCard(
    modifier: Modifier = Modifier,
    borderColor: Color = OrbitBorder,
    containerColor: Color = StarlightCard,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val sound = LocalSpaceSoundManager.current
    if (onClick != null) {
        Surface(
            onClick = {
                sound?.play(SpaceSfx.SOFT_BEEP)
                onClick()
            },
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.dp, borderColor),
            tonalElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.dp, borderColor),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    }
}

@Composable
fun SectionHeader(
    eyebrow: String,
    title: String,
    subtitle: String? = null,
    eyebrowColor: Color = NeonCyan,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = eyebrowColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = eyebrow.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = eyebrowColor
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = StarWhite
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MoonSilver
            )
        }
    }
}

@Composable
fun PrimaryCosmicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    containerColor: Color = CosmicPurple
) {
    val sound = LocalSpaceSoundManager.current
    Button(
        onClick = {
            sound?.play(SpaceSfx.SOFT_BEEP)
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = StarWhite,
            disabledContainerColor = ElevatedCosmic,
            disabledContentColor = NebulaMuted
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SecondaryCyanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val sound = LocalSpaceSoundManager.current
    OutlinedButton(
        onClick = {
            sound?.play(SpaceSfx.SOFT_BEEP)
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.2.dp, NeonCyan.copy(alpha = 0.7f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = NeonCyan.copy(alpha = 0.08f),
            contentColor = NeonCyan
        ),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun OperationBadge(
    operation: MathOperation,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) operation.accentColor.copy(alpha = 0.24f) else MidnightOrbit
    val border = if (selected) operation.accentColor else OrbitBorder
    val contentColor = if (selected) StarWhite else MoonSilver
    val sound = LocalSpaceSoundManager.current
    Surface(
        onClick = {
            sound?.play(SpaceSfx.SOFT_BEEP)
            onClick?.invoke()
        },
        enabled = onClick != null,
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = BorderStroke(1.2.dp, border),
        modifier = modifier.heightIn(min = 48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(operation.accentColor.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = operation.symbol,
                    style = MaterialTheme.typography.titleSmall,
                    color = operation.accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = operation.label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StarRatingRow(
    stars: Int,
    maxStars: Int = 3,
    starSize: Dp = 18.dp
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val filled = i <= stars
            Icon(
                imageVector = if (filled) Icons.Rounded.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = if (filled) SolarYellow else NebulaMuted.copy(alpha = 0.6f),
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
fun AstroBotHelperCard(
    title: String,
    message: String,
    accentColor: Color = NeonCyan,
    modifier: Modifier = Modifier
) {
    CosmicCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.6f),
        containerColor = NebulaSurface,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.img_mascot_astro_guide),
                contentDescription = "AstroBot guía espacial",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = accentColor
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = StarWhite
                )
            }
        }
    }
}

@Composable
fun StatMetricTile(
    label: String,
    value: String,
    accent: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MidnightOrbit,
        border = BorderStroke(1.dp, OrbitBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = StarWhite,
                maxLines = 1
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MoonSilver,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyCosmicMissionCard(
    dailyMission: DailyMissionEntity,
    countdownText: String,
    streakDays: Int,
    onLaunchDailyMission: () -> Unit,
    onLearnDailyOperation: (MathOperation) -> Unit,
    modifier: Modifier = Modifier
) {
    val sound = LocalSpaceSoundManager.current
    val op = MathOperation.fromId(dailyMission.operationId)
    val isCompleted = dailyMission.completed
    val accent = if (isCompleted) SuccessEmerald else SolarYellow

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_mission_card"),
        shape = RoundedCornerShape(22.dp),
        color = NebulaSurface,
        border = BorderStroke(1.5.dp, accent.copy(alpha = 0.72f)),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            CosmicPurple.copy(alpha = 0.16f),
                            op.accentColor.copy(alpha = 0.10f),
                            NebulaSurface
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top badge row: Daily eyebrow + 24h countdown + streak
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isCompleted) SuccessContainer else SolarContainer,
                    border = BorderStroke(1.dp, accent.copy(alpha = 0.65f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = if (isCompleted) "MISIÓN DIARIA COMPLETADA" else "MISIÓN CÓSMICA DIARIA",
                            style = MaterialTheme.typography.labelSmall,
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MidnightOrbit,
                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = "Tiempo para renovación de misión",
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Renueva en $countdownText",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MidnightOrbit,
                    border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.45f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = "Racha diaria",
                            tint = SolarYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Racha: $streakDays ${if (streakDays == 1) "día" else "días"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SolarYellow
                        )
                    }
                }
            }

            // Mission Core Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(op.accentColor.copy(alpha = 0.22f))
                        .border(2.dp, op.accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = op.symbol,
                        style = MaterialTheme.typography.headlineMedium,
                        color = op.accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${dailyMission.planetCode} • ${op.label} • ${dailyMission.questionCount} retos",
                        style = MaterialTheme.typography.labelSmall,
                        color = op.accentColor
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = dailyMission.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = StarWhite
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = dailyMission.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MoonSilver
                    )
                }
            }

            // Reward & Goal Summary Row
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ElevatedCosmic,
                    border = BorderStroke(1.dp, OrbitBorder)
                ) {
                    Text(
                        text = "Meta: ${dailyMission.targetAccuracy}% precisión",
                        style = MaterialTheme.typography.labelMedium,
                        color = StarWhite,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ElevatedCosmic,
                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = "+${dailyMission.bonusXp} XP Bonus",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SolarContainer,
                    border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = "+${dailyMission.bonusStars} ★",
                        style = MaterialTheme.typography.labelMedium,
                        color = SolarYellow,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                if (dailyMission.bestAccuracy > 0) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MidnightOrbit,
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = "Mejor hoy: ${dailyMission.bestAccuracy}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = accent,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        sound?.play(SpaceSfx.SOFT_BEEP)
                        onLearnDailyOperation(op)
                    },
                    modifier = Modifier
                        .weight(0.9f)
                        .heightIn(min = 48.dp)
                        .testTag("daily_mission_learn_button"),
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
                    Text("Repasar", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = {
                        sound?.play(SpaceSfx.SOFT_BEEP)
                        onLaunchDailyMission()
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .heightIn(min = 48.dp)
                        .testTag("daily_mission_launch_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) ElevatedCosmic else CosmicPurple,
                        contentColor = StarWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (isCompleted) "Repetir Práctica" else "Despegar Misión",
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun XpLevelProgressBarCard(
    levelInfo: XpLevelInfo,
    cardTestTag: String = "xp_level_progress_card",
    barTestTag: String = "xp_level_progress_bar",
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = levelInfo.progressFraction,
        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing),
        label = "xp_level_progress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag(cardTestTag),
        shape = RoundedCornerShape(22.dp),
        color = NebulaSurface,
        border = BorderStroke(1.5.dp, NeonCyan.copy(alpha = 0.68f)),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            CosmicPurple.copy(alpha = 0.18f),
                            AstralBlue.copy(alpha = 0.14f),
                            NebulaSurface
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                            .size(50.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(CosmicPurple.copy(alpha = 0.45f), ElevatedCosmic)
                                )
                            )
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "NV",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${levelInfo.level}",
                                style = MaterialTheme.typography.titleLarge,
                                color = StarWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "PROGRESIÓN DE NIVEL",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                        Text(
                            text = "Nivel ${levelInfo.level} • ${levelInfo.rankTitle}",
                            style = MaterialTheme.typography.titleMedium,
                            color = StarWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${levelInfo.xpInCurrentLevel} / ${levelInfo.xpRequiredForNextLevel} XP en este nivel",
                            style = MaterialTheme.typography.bodySmall,
                            color = MoonSilver
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MidnightOrbit,
                    border = BorderStroke(1.dp, SolarYellow.copy(alpha = 0.65f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WorkspacePremium,
                            contentDescription = "Experiencia total",
                            tint = SolarYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${levelInfo.totalXp} XP",
                            style = MaterialTheme.typography.labelLarge,
                            color = SolarYellow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .testTag(barTestTag),
                color = NeonCyan,
                trackColor = MidnightOrbit
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Faltan ${levelInfo.xpRemainingToNextLevel} XP para Nivel ${levelInfo.level + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SolarYellow
                )
                Text(
                    text = "${(levelInfo.progressFraction * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


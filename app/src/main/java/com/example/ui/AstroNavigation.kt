package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.R
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSfx
import com.example.model.SpaceAvatar
import com.example.ui.components.AstroTopBar
import com.example.ui.components.CosmicBackground
import com.example.ui.components.WarpJumpOverlay
import com.example.ui.screens.LearningZoneScreen
import com.example.ui.screens.MathChallengeScreen
import com.example.ui.screens.MissionsMapScreen
import com.example.ui.screens.ProblemLabScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.TeacherPanelScreen
import com.example.ui.screens.WelcomeProfileScreen
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.ElevatedCosmic
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NebulaSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.StarWhite

object AstroRoutes {
    const val ROUTE_WELCOME_PROFILE = "welcome_profile"
    const val ROUTE_MISSIONS_MAP = "missions_map"
    const val ROUTE_LEARNING_ZONE = "learning_zone"
    const val ROUTE_MATH_CHALLENGE = "math_challenge"
    const val ROUTE_PROBLEM_LAB = "problem_lab"
    const val ROUTE_REWARDS = "rewards"
    const val ROUTE_PROGRESS = "progress"
    const val ROUTE_TEACHER_PANEL = "teacher_panel"

    fun orderOf(route: String?): Int = when (route) {
        ROUTE_WELCOME_PROFILE -> 0
        ROUTE_MISSIONS_MAP -> 1
        ROUTE_LEARNING_ZONE -> 2
        ROUTE_MATH_CHALLENGE -> 3
        ROUTE_PROBLEM_LAB -> 4
        ROUTE_PROGRESS -> 5
        ROUTE_REWARDS -> 6
        ROUTE_TEACHER_PANEL -> 7
        else -> 1
    }
}

private data class NavTabItem(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val primaryTabs = listOf(
    NavTabItem(
        route = AstroRoutes.ROUTE_MISSIONS_MAP,
        labelRes = R.string.nav_missions,
        selectedIcon = Icons.Rounded.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        testTag = "nav_tab_missions"
    ),
    NavTabItem(
        route = AstroRoutes.ROUTE_LEARNING_ZONE,
        labelRes = R.string.nav_learning,
        selectedIcon = Icons.Rounded.AutoStories,
        unselectedIcon = Icons.Outlined.AutoStories,
        testTag = "nav_tab_learning"
    ),
    NavTabItem(
        route = AstroRoutes.ROUTE_MATH_CHALLENGE,
        labelRes = R.string.nav_challenge,
        selectedIcon = Icons.Rounded.Bolt,
        unselectedIcon = Icons.Outlined.Bolt,
        testTag = "nav_tab_challenge"
    ),
    NavTabItem(
        route = AstroRoutes.ROUTE_PROBLEM_LAB,
        labelRes = R.string.nav_lab,
        selectedIcon = Icons.Rounded.Science,
        unselectedIcon = Icons.Outlined.Science,
        testTag = "nav_tab_lab"
    ),
    NavTabItem(
        route = AstroRoutes.ROUTE_PROGRESS,
        labelRes = R.string.nav_progress,
        selectedIcon = Icons.Rounded.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
        testTag = "nav_tab_progress"
    )
)

@Composable
fun AstroMateApp(viewModel: AstroViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: AstroRoutes.ROUTE_WELCOME_PROFILE
    val snackbarHostState = remember { SnackbarHostState() }
    val soundManager = LocalSpaceSoundManager.current
    val isMuted by (soundManager?.isMuted ?: remember { kotlinx.coroutines.flow.MutableStateFlow(false) })
        .collectAsStateWithLifecycle()
    var isFirstRoute by remember { mutableStateOf(true) }

    LaunchedEffect(currentRoute) {
        if (isFirstRoute) {
            isFirstRoute = false
        } else {
            soundManager?.play(SpaceSfx.WARP_JUMP)
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        val msg = uiState.snackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    fun navigateToTab(route: String) {
        soundManager?.play(SpaceSfx.SOFT_BEEP)
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSecondary(route: String) {
        if (currentRoute != route) {
            soundManager?.play(SpaceSfx.SOFT_BEEP)
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    val isSecondaryScreen = currentRoute in setOf(
        AstroRoutes.ROUTE_REWARDS,
        AstroRoutes.ROUTE_TEACHER_PANEL,
        AstroRoutes.ROUTE_WELCOME_PROFILE
    )

    val screenTitle = when (currentRoute) {
        AstroRoutes.ROUTE_WELCOME_PROFILE -> stringResource(R.string.screen_welcome_profile)
        AstroRoutes.ROUTE_MISSIONS_MAP -> stringResource(R.string.screen_missions_map)
        AstroRoutes.ROUTE_LEARNING_ZONE -> stringResource(R.string.screen_learning_zone)
        AstroRoutes.ROUTE_MATH_CHALLENGE -> stringResource(R.string.screen_math_challenge)
        AstroRoutes.ROUTE_PROBLEM_LAB -> stringResource(R.string.screen_problem_lab)
        AstroRoutes.ROUTE_REWARDS -> stringResource(R.string.screen_rewards)
        AstroRoutes.ROUTE_PROGRESS -> stringResource(R.string.screen_progress)
        AstroRoutes.ROUTE_TEACHER_PANEL -> stringResource(R.string.screen_teacher_panel)
        else -> stringResource(R.string.app_name)
    }

    CosmicBackground {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavigationRail = maxWidth >= 600.dp

            Row(modifier = Modifier.fillMaxSize()) {
                if (useNavigationRail) {
                    NavigationRail(
                        containerColor = NebulaSurface.copy(alpha = 0.96f),
                        contentColor = StarWhite
                    ) {
                        primaryTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = stringResource(tab.labelRes)
                                    )
                                },
                                label = { Text(stringResource(tab.labelRes)) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = NeonCyan,
                                    selectedTextColor = NeonCyan,
                                    indicatorColor = ElevatedCosmic,
                                    unselectedIconColor = MoonSilver,
                                    unselectedTextColor = MoonSilver
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                        NavigationRailItem(
                            selected = currentRoute == AstroRoutes.ROUTE_REWARDS,
                            onClick = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) },
                            icon = { Icon(Icons.Rounded.EmojiEvents, contentDescription = stringResource(R.string.screen_rewards)) },
                            label = { Text("Premios") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = SolarYellow,
                                selectedTextColor = SolarYellow,
                                indicatorColor = ElevatedCosmic,
                                unselectedIconColor = MoonSilver,
                                unselectedTextColor = MoonSilver
                            )
                        )
                        NavigationRailItem(
                            selected = currentRoute == AstroRoutes.ROUTE_WELCOME_PROFILE,
                            onClick = { navigateToSecondary(AstroRoutes.ROUTE_WELCOME_PROFILE) },
                            icon = { Icon(Icons.Rounded.Person, contentDescription = stringResource(R.string.screen_welcome_profile)) },
                            label = { Text("Perfil") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = CosmicPurple,
                                selectedTextColor = StarWhite,
                                indicatorColor = ElevatedCosmic,
                                unselectedIconColor = MoonSilver,
                                unselectedTextColor = MoonSilver
                            )
                        )
                        NavigationRailItem(
                            selected = currentRoute == AstroRoutes.ROUTE_TEACHER_PANEL,
                            onClick = { navigateToSecondary(AstroRoutes.ROUTE_TEACHER_PANEL) },
                            icon = { Icon(Icons.Rounded.School, contentDescription = stringResource(R.string.screen_teacher_panel)) },
                            label = { Text("Docente") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                indicatorColor = ElevatedCosmic,
                                unselectedIconColor = MoonSilver,
                                unselectedTextColor = MoonSilver
                            )
                        )
                    }
                }

                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = ElevatedCosmic,
                                contentColor = StarWhite,
                                actionColor = NeonCyan
                            )
                        }
                    },
                    topBar = {
                        AstroTopBar(
                            studentName = uiState.profile.studentName,
                            rankTitle = uiState.studentRank,
                            avatar = SpaceAvatar.fromId(uiState.profile.avatarId),
                            totalStars = uiState.profile.totalStars,
                            totalXp = uiState.profile.totalXp,
                            screenTitle = screenTitle,
                            showBackButton = isSecondaryScreen && navController.previousBackStackEntry != null,
                            isMuted = isMuted,
                            onToggleMute = { soundManager?.toggleMute() },
                            onBackClick = {
                                soundManager?.play(SpaceSfx.SOFT_BEEP)
                                navController.popBackStack()
                            },
                            onProfileClick = { navigateToSecondary(AstroRoutes.ROUTE_WELCOME_PROFILE) },
                            onRewardsClick = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) },
                            onTeacherClick = { navigateToSecondary(AstroRoutes.ROUTE_TEACHER_PANEL) }
                        )
                    },
                    bottomBar = {
                        if (!useNavigationRail) {
                            NavigationBar(
                                containerColor = NebulaSurface.copy(alpha = 0.97f),
                                contentColor = StarWhite,
                                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                            ) {
                                primaryTabs.forEach { tab ->
                                    val selected = currentRoute == tab.route
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateToTab(tab.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = stringResource(tab.labelRes)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(tab.labelRes),
                                                style = MaterialTheme.typography.labelSmall,
                                                maxLines = 1
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = NeonCyan,
                                            selectedTextColor = NeonCyan,
                                            indicatorColor = ElevatedCosmic,
                                            unselectedIconColor = MoonSilver,
                                            unselectedTextColor = MoonSilver
                                        ),
                                        modifier = Modifier.testTag(tab.testTag)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AstroRoutes.ROUTE_WELCOME_PROFILE,
                            modifier = Modifier.fillMaxSize(),
                            enterTransition = {
                                val fromIdx = AstroRoutes.orderOf(initialState.destination.route)
                                val toIdx = AstroRoutes.orderOf(targetState.destination.route)
                                val forward = toIdx >= fromIdx
                                fadeIn(
                                    animationSpec = tween(260, easing = FastOutSlowInEasing)
                                ) + scaleIn(
                                    initialScale = 0.94f,
                                    animationSpec = tween(260, easing = FastOutSlowInEasing)
                                ) + slideInHorizontally(
                                    animationSpec = tween(260, easing = FastOutSlowInEasing),
                                    initialOffsetX = { w -> if (forward) w / 9 else -w / 9 }
                                )
                            },
                            exitTransition = {
                                val fromIdx = AstroRoutes.orderOf(initialState.destination.route)
                                val toIdx = AstroRoutes.orderOf(targetState.destination.route)
                                val forward = toIdx >= fromIdx
                                fadeOut(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                                ) + scaleOut(
                                    targetScale = 1.04f,
                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                                ) + slideOutHorizontally(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                                    targetOffsetX = { w -> if (forward) -w / 11 else w / 11 }
                                )
                            },
                            popEnterTransition = {
                                fadeIn(
                                    animationSpec = tween(260, easing = FastOutSlowInEasing)
                                ) + scaleIn(
                                    initialScale = 1.04f,
                                    animationSpec = tween(260, easing = FastOutSlowInEasing)
                                ) + slideInHorizontally(
                                    animationSpec = tween(260, easing = FastOutSlowInEasing),
                                    initialOffsetX = { w -> -w / 9 }
                                )
                            },
                            popExitTransition = {
                                fadeOut(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                                ) + scaleOut(
                                    targetScale = 0.94f,
                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                                ) + slideOutHorizontally(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                                    targetOffsetX = { w -> w / 11 }
                                )
                            }
                        ) {
                        composable(AstroRoutes.ROUTE_WELCOME_PROFILE) {
                            WelcomeProfileScreen(
                                uiState = uiState,
                                onSaveAndLaunch = { name, grade, avatarId, difficultyId ->
                                    viewModel.saveStudentProfile(name, grade, avatarId, difficultyId)
                                    navigateToTab(AstroRoutes.ROUTE_MISSIONS_MAP)
                                },
                                onOpenRewards = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) },
                                onOpenTeacherPanel = { navigateToSecondary(AstroRoutes.ROUTE_TEACHER_PANEL) }
                            )
                        }

                        composable(AstroRoutes.ROUTE_MISSIONS_MAP) {
                            MissionsMapScreen(
                                uiState = uiState,
                                onStartLearning = { op ->
                                    viewModel.selectLearningOperation(op)
                                    navigateToTab(AstroRoutes.ROUTE_LEARNING_ZONE)
                                },
                                onLaunchChallenge = { mission ->
                                    viewModel.startChallenge(
                                        operation = mission.operation,
                                        missionId = mission.id,
                                        missionTitle = mission.title
                                    )
                                    navigateToTab(AstroRoutes.ROUTE_MATH_CHALLENGE)
                                },
                                onLaunchDailyMission = {
                                    viewModel.startDailyMissionChallenge()
                                    navigateToTab(AstroRoutes.ROUTE_MATH_CHALLENGE)
                                },
                                onOpenProblemLab = { navigateToTab(AstroRoutes.ROUTE_PROBLEM_LAB) },
                                onOpenRewards = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) }
                            )
                        }

                        composable(AstroRoutes.ROUTE_LEARNING_ZONE) {
                            LearningZoneScreen(
                                uiState = uiState,
                                onSelectOperation = viewModel::selectLearningOperation,
                                onSelectQuickCheck = viewModel::selectQuickCheckOption,
                                onStartChallenge = { op ->
                                    viewModel.startChallenge(
                                        operation = op,
                                        missionId = null,
                                        missionTitle = "Práctica ${op.label}"
                                    )
                                    navigateToTab(AstroRoutes.ROUTE_MATH_CHALLENGE)
                                },
                                onOpenLab = { navigateToTab(AstroRoutes.ROUTE_PROBLEM_LAB) }
                            )
                        }

                        composable(AstroRoutes.ROUTE_MATH_CHALLENGE) {
                            MathChallengeScreen(
                                uiState = uiState,
                                onStartOperationChallenge = { op ->
                                    viewModel.startChallenge(
                                        operation = op,
                                        missionId = null,
                                        missionTitle = "Reto de ${op.label}"
                                    )
                                },
                                onLaunchDailyMission = {
                                    viewModel.startDailyMissionChallenge()
                                },
                                onSubmitAnswer = viewModel::submitChallengeAnswer,
                                onToggleHint = viewModel::toggleChallengeHint,
                                onNextQuestion = viewModel::nextChallengeQuestion,
                                onResetToSelector = viewModel::resetChallengeToMenu,
                                onOpenMap = { navigateToTab(AstroRoutes.ROUTE_MISSIONS_MAP) },
                                onOpenRewards = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) }
                            )
                        }

                        composable(AstroRoutes.ROUTE_PROBLEM_LAB) {
                            ProblemLabScreen(
                                uiState = uiState,
                                onSelectProblem = viewModel::selectLabProblem,
                                onAdvanceToStep2 = viewModel::advanceLabToStep2,
                                onChooseOperation = viewModel::chooseLabOperation,
                                onSubmitAnswer = viewModel::submitLabAnswer,
                                onToggleHint = viewModel::toggleLabHint
                            )
                        }

                        composable(AstroRoutes.ROUTE_REWARDS) {
                            RewardsScreen(
                                uiState = uiState,
                                onUnlockOrEquipPerk = viewModel::unlockOrEquipPerk
                            )
                        }

                        composable(AstroRoutes.ROUTE_PROGRESS) {
                            ProgressScreen(
                                uiState = uiState,
                                onPracticeOperation = { op ->
                                    viewModel.startChallenge(
                                        operation = op,
                                        missionId = null,
                                        missionTitle = "Refuerzo ${op.label}"
                                    )
                                    navigateToTab(AstroRoutes.ROUTE_MATH_CHALLENGE)
                                },
                                onLearnOperation = { op ->
                                    viewModel.selectLearningOperation(op)
                                    navigateToTab(AstroRoutes.ROUTE_LEARNING_ZONE)
                                },
                                onLaunchDailyMission = {
                                    viewModel.startDailyMissionChallenge()
                                    navigateToTab(AstroRoutes.ROUTE_MATH_CHALLENGE)
                                },
                                onOpenRewards = { navigateToSecondary(AstroRoutes.ROUTE_REWARDS) },
                                onOpenTeacherPanel = { navigateToSecondary(AstroRoutes.ROUTE_TEACHER_PANEL) }
                            )
                        }

                        composable(AstroRoutes.ROUTE_TEACHER_PANEL) {
                            TeacherPanelScreen(
                                uiState = uiState,
                                onSaveSettings = viewModel::updateTeacherSettings,
                                onAddCustomProblem = viewModel::addCustomTeacherProblem,
                                onDeleteCustomProblem = viewModel::deleteCustomProblem,
                                onResetProgress = viewModel::resetStudentProgress
                            )
                        }
                    }

                    WarpJumpOverlay(routeKey = currentRoute)
                }
                }
            }
        }
    }
}

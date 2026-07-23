package org.muslim_voice.project.features.groupDashboard.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.generated.resources.*
import org.muslim_voice.project.core.ui.components.AnimatedWaveform
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.ui.components.DayDot
import org.muslim_voice.project.core.ui.components.HadithCard
import org.muslim_voice.project.core.ui.components.PulsingRingEffect
import org.muslim_voice.project.core.ui.components.SectionHeader
import org.muslim_voice.project.core.ui.components.StatusRingAvatar
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppShapes
import org.muslim_voice.project.core.ui.theme.LatinNumericStyle
import org.muslim_voice.project.features.groupDashboard.effect.GroupDashboardEffect
import org.muslim_voice.project.features.groupDashboard.intent.GroupDashboardIntent
import org.muslim_voice.project.features.groupDashboard.state.MemberStatus
import org.muslim_voice.project.features.groupDashboard.state.ReadyStateUi
import org.muslim_voice.project.features.groupDashboard.viewModel.GroupDashboardViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDashboardScreen(
    navigator: AppNavigator,
    viewModel: GroupDashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GroupDashboardEffect.NavigateToNotifications ->
                    navigator.navigateTo(Screens.NotificationsHistory)
            }
        }
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AppColors.Primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("🕌", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = state.groupName,
                                    color = AppColors.OnPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(Res.string.group_switch),
                                    tint = AppColors.OnPrimary,
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(Res.string.cd_search),
                            tint = AppColors.OnPrimary,
                        )
                    }
                    IconButton(onClick = { viewModel.onIntent(GroupDashboardIntent.OpenNotifications) }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = stringResource(Res.string.notifications_title),
                            tint = AppColors.OnPrimary,
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = stringResource(Res.string.cd_menu),
                            tint = AppColors.OnPrimary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.PrimaryDark),
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AppColors.StatusGreen),
                )
                Text(
                    text = stringResource(Res.string.group_active_members, state.activeMembersCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Subtle,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PrayerCard(
                    prayerName = state.prayerName,
                    prayerTime = state.prayerTime,
                    hours = state.countdownHours,
                    minutes = state.countdownMinutes,
                    seconds = state.countdownSeconds,
                    readyState = state.readyState,
                    onReadyClick = { viewModel.onIntent(GroupDashboardIntent.ToggleReady) },
                )
                WeeklyCalendarRow(days = state.weekHistory)
                SectionHeader(title = stringResource(Res.string.group_members_section))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(280.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                ) {
                    items(state.members, key = { it.id }) { member ->
                        MemberCard(member)
                    }
                }
                WalkieTalkieBanner(
                    pressed = state.isWalkiePressed,
                    onPressChange = { viewModel.onIntent(GroupDashboardIntent.SetWalkiePressed(it)) },
                )
                HadithCard(quote = state.hadith.quote, source = state.hadith.source)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PrayerCard(
    prayerName: String,
    prayerTime: String,
    hours: Int,
    minutes: Int,
    seconds: Int,
    readyState: ReadyStateUi,
    onReadyClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.Accent, AppShapes.Large),
        shape = AppShapes.Large,
        colors = CardDefaults.cardColors(containerColor = AppColors.Primary),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(AppColors.Primary, AppColors.PrimaryDark)),
                )
                .padding(16.dp),
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.group_prayer_now),
                    color = AppColors.AccentLight,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "$prayerName — $prayerTime",
                    color = AppColors.OnPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CountdownBlock(value = hours, label = stringResource(Res.string.countdown_hours))
                    CountdownBlock(value = minutes, label = stringResource(Res.string.countdown_minutes))
                    CountdownBlock(value = seconds, label = stringResource(Res.string.countdown_seconds))
                }
                Spacer(modifier = Modifier.height(12.dp))
                ReadyButton(state = readyState, onClick = onReadyClick)
            }
        }
    }
}

@Composable
private fun CountdownBlock(value: Int, label: String) {
    Column(
        modifier = Modifier
            .background(AppColors.PrimaryDark.copy(alpha = 0.5f), AppShapes.Small)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            style = LatinNumericStyle.copy(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = AppColors.OnPrimary,
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = AppColors.AccentLight)
    }
}

@Composable
private fun ReadyButton(state: ReadyStateUi, onClick: () -> Unit) {
    val bg = when (state) {
        ReadyStateUi.IDLE -> AppColors.Accent
        ReadyStateUi.READY -> AppColors.Primary
        ReadyStateUi.DONE -> AppColors.StatusGreen
    }
    val textColor = when (state) {
        ReadyStateUi.IDLE -> AppColors.PrimaryDark
        else -> AppColors.OnPrimary
    }
    val label = when (state) {
        ReadyStateUi.IDLE -> stringResource(Res.string.group_ready_idle)
        ReadyStateUi.READY -> stringResource(Res.string.group_ready_done)
        ReadyStateUi.DONE -> stringResource(Res.string.group_ready_completed)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        PulsingRingEffect(
            modifier = Modifier.matchParentSize(),
            color = AppColors.Accent,
            enabled = state == ReadyStateUi.IDLE,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(AppShapes.Medium)
                .background(bg)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onClick() })
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun WeeklyCalendarRow(days: List<org.muslim_voice.project.features.groupDashboard.state.DayStatus>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        days.forEach { day ->
            DayDot(label = day.dayLabel, state = day.state)
        }
    }
}

@Composable
private fun MemberCard(member: MemberStatus) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        shape = AppShapes.Medium,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusRingAvatar(
                emoji = member.emoji,
                ringColor = member.statusRing,
                modifier = Modifier,
            )
            Text(
                text = member.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(text = member.statusText, style = MaterialTheme.typography.labelSmall, color = AppColors.Subtle)
            if (member.hasVoiceNote) {
                AnimatedWaveform(modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun WalkieTalkieBanner(
    pressed: Boolean,
    onPressChange: (Boolean) -> Unit,
) {
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, label = "mic_scale")
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        shape = AppShapes.Medium,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(if (pressed) AppColors.PrimaryDark else AppColors.Primary)
                    .border(2.dp, AppColors.Accent, CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onPressChange(true)
                                tryAwaitRelease()
                                onPressChange(false)
                            },
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                PulsingRingEffect(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColors.Accent,
                    cornerRadius = 26.dp,
                )
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = stringResource(Res.string.cd_mic_button),
                    tint = AppColors.OnPrimary,
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = stringResource(Res.string.group_walkie_title), style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(Res.string.group_walkie_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Subtle,
                )
            }
            Box(
                modifier = Modifier
                    .background(AppColors.StatusRed.copy(alpha = 0.15f), AppShapes.Small)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.group_walkie_live),
                    color = AppColors.StatusRed,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

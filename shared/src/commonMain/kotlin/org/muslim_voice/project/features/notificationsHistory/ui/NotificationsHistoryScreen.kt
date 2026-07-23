package org.muslim_voice.project.features.notificationsHistory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.generated.resources.*
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.ui.components.AppTopBar
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppShapes
import org.muslim_voice.project.features.notificationsHistory.intent.NotificationsHistoryIntent
import org.muslim_voice.project.features.notificationsHistory.state.NotificationDateGroup
import org.muslim_voice.project.features.notificationsHistory.state.NotificationItemUi
import org.muslim_voice.project.features.notificationsHistory.viewModel.NotificationsHistoryViewModel
import org.muslim_voice.project.navigation.AppNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsHistoryScreen(
    navigator: AppNavigator,
    viewModel: NotificationsHistoryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = stringResource(Res.string.notifications_title),
                onBack = { navigator.popBack() },
            )

            if (state.items.isEmpty()) {
                EmptyNotifications()
            } else {
                val grouped = state.items.groupBy { it.group }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    NotificationDateGroup.entries.forEach { group ->
                        val groupItems = grouped[group].orEmpty()
                        if (groupItems.isNotEmpty()) {
                            item(key = "header_$group") {
                                Text(
                                    text = groupTitle(group),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                            }
                            items(groupItems, key = { it.id }) { item ->
                                NotificationDismissibleCard(
                                    item = item,
                                    onDismiss = { viewModel.onIntent(NotificationsHistoryIntent.Dismiss(it)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun groupTitle(group: NotificationDateGroup): String = when (group) {
    NotificationDateGroup.TODAY -> stringResource(Res.string.notifications_group_today)
    NotificationDateGroup.YESTERDAY -> stringResource(Res.string.notifications_group_yesterday)
    NotificationDateGroup.EARLIER -> stringResource(Res.string.notifications_group_earlier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationDismissibleCard(
    item: NotificationItemUi,
    onDismiss: (String) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onDismiss(item.id)
                true
            } else {
                false
            }
        },
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(AppColors.StatusRed, AppShapes.Medium),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.notifications_delete),
                    tint = AppColors.OnPrimary,
                    modifier = Modifier.padding(end = 20.dp),
                )
            }
        },
        content = {
            NotificationCard(item = item, modifier = Modifier.padding(horizontal = 16.dp))
        },
    )
}

@Composable
private fun NotificationCard(item: NotificationItemUi, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        shape = AppShapes.Medium,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(item.type.color().copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.type.icon(),
                    contentDescription = stringResource(Res.string.cd_notification_icon),
                    tint = item.type.color(),
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = item.body, style = MaterialTheme.typography.bodyMedium, color = AppColors.Subtle)
                Text(
                    text = item.timeLabel,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (item.isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AppColors.Accent),
                )
            }
        }
    }
}

@Composable
private fun EmptyNotifications() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.NotificationsOff,
            contentDescription = null,
            tint = AppColors.Subtle,
            modifier = Modifier.size(72.dp),
        )
        Text(
            text = stringResource(Res.string.notifications_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.Subtle,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

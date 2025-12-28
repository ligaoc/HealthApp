package com.healthapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("消息通知") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (uiState) {
                is NotificationSettingsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is NotificationSettingsUiState.Error -> {
                    Text(
                        text = (uiState as NotificationSettingsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is NotificationSettingsUiState.Success -> {
                    val settings = (uiState as NotificationSettingsUiState.Success).settings

                    SettingsSection(title = "通知类型") {
                        SettingsSwitchItem(
                            title = "告警通知",
                            subtitle = "接收健康异常告警",
                            checked = settings.alarmEnabled,
                            onCheckedChange = { viewModel.updateSetting(alarmEnabled = it) }
                        )
                        SettingsSwitchItem(
                            title = "用药提醒",
                            subtitle = "接收用药时间提醒",
                            checked = settings.medicationEnabled,
                            onCheckedChange = { viewModel.updateSetting(medicationEnabled = it) }
                        )
                        SettingsSwitchItem(
                            title = "健康小贴士",
                            subtitle = "接收健康知识推送",
                            checked = settings.healthTipsEnabled,
                            onCheckedChange = { viewModel.updateSetting(healthTipsEnabled = it) }
                        )
                        SettingsSwitchItem(
                            title = "系统通知",
                            subtitle = "接收系统更新通知",
                            checked = settings.systemEnabled,
                            onCheckedChange = { viewModel.updateSetting(systemEnabled = it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingsSection(title = "免打扰") {
                        SettingsSwitchItem(
                            title = "开启免打扰",
                            subtitle = "在指定时间段内不接收通知",
                            checked = settings.quietTimeEnabled,
                            onCheckedChange = { viewModel.updateSetting(quietTimeEnabled = it) }
                        )
                        if (settings.quietTimeEnabled) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("免打扰时段", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${settings.quietTimeStart ?: "22:00"} - ${settings.quietTimeEnd ?: "08:00"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
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
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Card(shape = RoundedCornerShape(12.dp)) {
        Column(content = content)
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

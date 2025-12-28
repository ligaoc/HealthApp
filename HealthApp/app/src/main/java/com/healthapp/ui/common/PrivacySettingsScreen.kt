package com.healthapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthapp.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    onBack: () -> Unit,
    viewModel: PrivacySettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("隐私设置") },
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
                is PrivacySettingsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is PrivacySettingsUiState.Error -> {
                    Text(
                        text = (uiState as PrivacySettingsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is PrivacySettingsUiState.Success -> {
                    val settings = (uiState as PrivacySettingsUiState.Success).settings

                    PrivacySection(title = "数据共享") {
                        PrivacySwitchItem(
                            title = "与医生共享数据",
                            subtitle = "允许您的医生查看健康数据",
                            checked = settings.shareWithDoctor,
                            onCheckedChange = { viewModel.updateSetting(shareWithDoctor = it) }
                        )
                        PrivacySwitchItem(
                            title = "与家人共享数据",
                            subtitle = "允许紧急联系人查看健康数据",
                            checked = settings.shareWithFamily,
                            onCheckedChange = { viewModel.updateSetting(shareWithFamily = it) }
                        )
                        PrivacySwitchItem(
                            title = "匿名数据分析",
                            subtitle = "允许使用匿名数据改进服务",
                            checked = settings.allowAnonymousAnalysis,
                            onCheckedChange = { viewModel.updateSetting(allowAnonymousAnalysis = it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PrivacySection(title = "数据管理") {
                        PrivacyActionItem(
                            title = "导出我的数据",
                            subtitle = "下载您的所有健康数据",
                            onClick = { showExportDialog = true }
                        )
                        PrivacyActionItem(
                            title = "注销账号",
                            subtitle = "永久删除您的账号和数据",
                            onClick = { showDeleteDialog = true },
                            isDestructive = true
                        )
                    }
                }
            }
        }
    }

    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("导出数据") },
            text = { Text("确定要导出您的健康数据吗？数据将在24小时内准备完成。") },
            confirmButton = {
                Button(onClick = {
                    viewModel.exportData()
                    showExportDialog = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) { Text("取消") }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("注销账号") },
            text = { Text("确定要注销账号吗？此操作不可撤销，您的所有数据将被永久删除。") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("确定注销") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun PrivacySection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
private fun PrivacySwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun PrivacyActionItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDestructive) ErrorRed else MaterialTheme.colorScheme.onSurface
            )
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (isDestructive) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

package com.healthapp.ui.patient.device

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthapp.data.model.device.DeviceData
import com.healthapp.ui.theme.ErrorRed
import com.healthapp.ui.theme.PrimaryBlue
import com.healthapp.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceManagementScreen(
    onBack: () -> Unit,
    viewModel: DeviceManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showUnbindDialog by remember { mutableStateOf<DeviceData?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的设备") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加设备")
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is DeviceManagementUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DeviceManagementUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as DeviceManagementUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is DeviceManagementUiState.Success -> {
                val devices = (uiState as DeviceManagementUiState.Success).devices
                if (devices.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Watch,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("暂无绑定设备", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { showAddDialog = true }) {
                                Text("添加设备")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(devices) { device ->
                            DeviceItem(
                                device = device,
                                onUnbind = { showUnbindDialog = device }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddDeviceDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { deviceId ->
                viewModel.bindDevice(deviceId)
                showAddDialog = false
            }
        )
    }

    showUnbindDialog?.let { device ->
        AlertDialog(
            onDismissRequest = { showUnbindDialog = null },
            title = { Text("解绑设备") },
            text = { Text("确定要解绑 ${device.deviceName} 吗？解绑后将无法接收该设备的数据。") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.unbindDevice(device.deviceId)
                        showUnbindDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("解绑") }
            },
            dismissButton = {
                TextButton(onClick = { showUnbindDialog = null }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun DeviceItem(device: DeviceData, onUnbind: () -> Unit) {
    val statusColor = if (device.status == "online") SuccessGreen else Color.Gray
    val statusText = if (device.status == "online") "在线" else "离线"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (device.deviceType) {
                        "watch" -> Icons.Default.Watch
                        "band" -> Icons.Default.FitnessCenter
                        else -> Icons.Default.Devices
                    },
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = device.deviceName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "电量 ${device.battery}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "同步: ${device.lastSyncTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onUnbind) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "解绑",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AddDeviceDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var deviceId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加设备") },
        text = {
            Column {
                Text("请输入设备ID或扫描设备二维码", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = deviceId,
                    onValueChange = { deviceId = it },
                    label = { Text("设备ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(deviceId) },
                enabled = deviceId.isNotBlank()
            ) { Text("绑定") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}

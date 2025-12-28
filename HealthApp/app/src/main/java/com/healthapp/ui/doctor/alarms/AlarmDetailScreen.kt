package com.healthapp.ui.doctor.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailScreen(
    alarmId: String,
    onBack: () -> Unit,
    onPatientClick: (String) -> Unit,
    viewModel: AlarmDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showHandleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(alarmId) {
        viewModel.loadAlarm(alarmId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("告警详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is AlarmDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AlarmDetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text((uiState as AlarmDetailUiState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AlarmDetailUiState.Success -> {
                val alarm = (uiState as AlarmDetailUiState.Success).alarm
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // 告警信息卡片
                    AlarmInfoCard(alarm)
                    Spacer(modifier = Modifier.height(16.dp))

                    // 患者信息
                    Text("患者信息", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { onPatientClick(alarm.patientId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(CircleShape).background(PrimaryBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(alarm.patientName.first().toString(), fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(alarm.patientName, fontWeight = FontWeight.Medium)
                                Text("${alarm.patientAge}岁 · ${alarm.patientPhone}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                if (alarm.medicalHistory.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("病史: ${alarm.medicalHistory.joinToString("、")}", style = MaterialTheme.typography.bodySmall, color = WarningOrange)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 位置信息
                    if (alarm.location != null) {
                        Text("位置信息", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ErrorRed)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(alarm.location, fontWeight = FontWeight.Medium)
                                    Text("经度: ${alarm.longitude} 纬度: ${alarm.latitude}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 紧急联系人
                    if (alarm.emergencyContacts.isNotEmpty()) {
                        Text("紧急联系人", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        alarm.emergencyContacts.forEach { contact ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${contact.name}（${contact.relation}）", fontWeight = FontWeight.Medium)
                                        Text(contact.phone, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                    IconButton(onClick = { }) {
                                        Icon(Icons.Default.Phone, contentDescription = "拨打", tint = SuccessGreen)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 处理按钮
                    if (alarm.status == "pending") {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showHandleDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("处理告警")
                        }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("该告警已处理", color = SuccessGreen, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if (showHandleDialog) {
        HandleAlarmDialog(
            onDismiss = { showHandleDialog = false },
            onConfirm = { handleType, result ->
                viewModel.handleAlarm(alarmId, handleType, result)
                showHandleDialog = false
            }
        )
    }
}

@Composable
private fun AlarmInfoCard(alarm: AlarmDetailData) {
    val levelColor = when (alarm.level) {
        "critical" -> ErrorRed
        "high" -> WarningOrange
        else -> HealthYellow
    }
    val levelText = when (alarm.level) {
        "critical" -> "紧急"
        "high" -> "高"
        else -> "中"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = levelColor.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(levelColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(levelText, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(alarm.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(alarm.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(alarm.time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun HandleAlarmDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var selectedType by remember { mutableStateOf("phone") }
    var result by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("处理告警") },
        text = {
            Column {
                Text("处理方式", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("phone" to "电话联系", "visit" to "上门处理", "dispatch" to "派遣急救").forEach { (type, label) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selectedType == type, onClick = { selectedType = type })
                        Text(label)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = result,
                    onValueChange = { result = it },
                    label = { Text("处理结果") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedType, result) }) { Text("确认处理") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}

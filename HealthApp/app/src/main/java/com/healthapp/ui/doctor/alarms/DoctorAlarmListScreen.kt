package com.healthapp.ui.doctor.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.healthapp.ui.theme.*


data class AlarmItem(val id: String, val type: String, val level: String, val patientName: String, val content: String, val time: String, val status: String)

@Composable
fun DoctorAlarmListScreen() {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("全部", "待处理", "已处理")

    val alarms = remember {
        listOf(
            AlarmItem("1", "heartRate", "critical", "张三", "心率过高：125 bpm", "10分钟前", "pending"),
            AlarmItem("2", "bloodPressure", "high", "李四", "血压异常：160/100 mmHg", "30分钟前", "pending"),
            AlarmItem("3", "bloodOxygen", "medium", "王五", "血氧偏低：92%", "1小时前", "handled"),
            AlarmItem("4", "fall", "critical", "赵六", "检测到跌倒事件", "2小时前", "handled"),
            AlarmItem("5", "heartRate", "high", "钱七", "心率持续偏高", "3小时前", "pending")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "告警中心", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "共 ${alarms.count { it.status == "pending" }} 条待处理", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEachIndexed { index, filter ->
                FilterChip(
                    selected = selectedFilter == index,
                    onClick = { selectedFilter = index },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryBlue, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(alarms.filter { selectedFilter == 0 || (selectedFilter == 1 && it.status == "pending") || (selectedFilter == 2 && it.status == "handled") }) { alarm ->
                AlarmCard(alarm = alarm)
            }
        }
    }
}


@Composable
private fun AlarmCard(alarm: AlarmItem) {
    val levelColor = when (alarm.level) { "critical" -> AlarmCritical; "high" -> AlarmHigh; "medium" -> AlarmMedium; else -> AlarmLow }
    val typeIcon: ImageVector = when (alarm.type) { "heartRate" -> Icons.Default.Favorite; "bloodPressure" -> Icons.Default.LocalHospital; "bloodOxygen" -> Icons.Default.Opacity; else -> Icons.Default.Warning }
    val typeColor = when (alarm.type) { "heartRate" -> HeartRateColor; "bloodPressure" -> BloodPressureColor; "bloodOxygen" -> BloodOxygenColor; else -> WarningOrange }

    Card(modifier = Modifier.fillMaxWidth().clickable { }, shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(typeColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(imageVector = typeIcon, contentDescription = null, tint = typeColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = alarm.patientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(levelColor.copy(alpha = 0.15f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(text = when (alarm.level) { "critical" -> "紧急"; "high" -> "高"; "medium" -> "中"; else -> "低" }, style = MaterialTheme.typography.labelSmall, color = levelColor)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = alarm.content, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = alarm.time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val statusColor = if (alarm.status == "pending") WarningOrange else SuccessGreen
                    val statusText = if (alarm.status == "pending") "待处理" else "已处理"
                    Text(text = statusText, style = MaterialTheme.typography.bodySmall, color = statusColor, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

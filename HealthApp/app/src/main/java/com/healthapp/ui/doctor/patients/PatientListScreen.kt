package com.healthapp.ui.doctor.patients

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.healthapp.ui.theme.*


data class PatientItem(val id: String, val name: String, val age: Int, val gender: String, val diseases: List<String>, val riskLevel: String, val deviceStatus: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(onPatientClick: (String) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("全部", "高风险", "中风险", "低风险")

    val patients = remember {
        listOf(
            PatientItem("U10001", "张三", 72, "男", listOf("高血压", "糖尿病"), "high", "online"),
            PatientItem("U10002", "李四", 68, "女", listOf("冠心病"), "medium", "online"),
            PatientItem("3", "王五", 75, "男", listOf("高血压"), "low", "offline"),
            PatientItem("4", "赵六", 80, "女", listOf("糖尿病", "高血脂"), "high", "online"),
            PatientItem("5", "钱七", 65, "男", listOf("心律不齐"), "medium", "online")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "患者管理", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "共 ${patients.size} 位患者", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("搜索患者姓名或手机号") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            items(patients) { patient -> PatientCard(patient = patient, onClick = { onPatientClick(patient.id) }) }
        }
    }
}


@Composable
private fun PatientCard(patient: PatientItem, onClick: () -> Unit = {}) {
    val riskColor = when (patient.riskLevel) { "high" -> ErrorRed; "medium" -> WarningOrange; else -> SuccessGreen }
    val riskText = when (patient.riskLevel) { "high" -> "高风险"; "medium" -> "中风险"; else -> "低风险" }

    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(PrimaryBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Text(text = patient.name.take(1), color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = patient.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${patient.gender} ${patient.age}岁", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    patient.diseases.forEach { disease ->
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text(text = disease, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (patient.deviceStatus == "online") SuccessGreen else MaterialTheme.colorScheme.outline))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (patient.deviceStatus == "online") "设备在线" else "设备离线", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(riskColor.copy(alpha = 0.1f)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                Text(text = riskText, style = MaterialTheme.typography.labelMedium, color = riskColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}

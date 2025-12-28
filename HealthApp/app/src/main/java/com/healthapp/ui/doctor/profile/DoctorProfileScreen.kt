package com.healthapp.ui.doctor.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthapp.ui.theme.*


@Composable
fun DoctorProfileScreen(
    onLogout: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        DoctorProfileHeader(onEditClick = onNavigateToPersonalInfo)

        Spacer(modifier = Modifier.height(24.dp))

        // 工作统计
        Text(text = "工作统计", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WorkStatCard(title = "管理患者", value = "128", modifier = Modifier.weight(1f))
            WorkStatCard(title = "今日处理", value = "15", modifier = Modifier.weight(1f))
            WorkStatCard(title = "本月告警", value = "89", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileMenuSection(
            title = "账户设置",
            items = listOf(
                DoctorMenuItem(Icons.Default.Person, "个人信息", "查看和编辑个人资料", onNavigateToPersonalInfo),
                DoctorMenuItem(Icons.Default.People, "患者管理", "管理负责的患者", {}),
                DoctorMenuItem(Icons.Default.Notifications, "消息通知", "设置通知偏好", onNavigateToNotifications)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuSection(
            title = "其他",
            items = listOf(
                DoctorMenuItem(Icons.Default.Security, "隐私设置", "管理数据隐私", onNavigateToPrivacy),
                DoctorMenuItem(Icons.Default.Settings, "系统设置", "应用设置", onNavigateToSettings),
                DoctorMenuItem(Icons.Default.Info, "关于我们", "版本信息", onNavigateToAbout)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ErrorRed.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = ErrorRed)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "退出登录", color = ErrorRed)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("确认退出") },
            text = { Text("确定要退出登录吗？") },
            confirmButton = { Button(onClick = { showLogoutDialog = false; onLogout() }, colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)) { Text("退出") } },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("取消") } }
        )
    }
}


@Composable
private fun DoctorProfileHeader(onEditClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Box(modifier = Modifier.fillMaxWidth().background(brush = Brush.linearGradient(listOf(SecondaryPurple, PrimaryBlue))).padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                    Text(text = "李", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "李医生", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "心内科 | 主治医师", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        Text(text = "医生", color = Color.White, fontSize = 12.sp)
                    }
                }
                Icon(imageVector = Icons.Default.Edit, contentDescription = "编辑", tint = Color.White, modifier = Modifier.size(24.dp).clickable { onEditClick() })
            }
        }
    }
}

@Composable
private fun WorkStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

data class DoctorMenuItem(val icon: ImageVector, val title: String, val subtitle: String, val onClick: () -> Unit = {})

@Composable
private fun ProfileMenuSection(title: String, items: List<DoctorMenuItem>) {
    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(12.dp))
    Card(shape = RoundedCornerShape(16.dp)) {
        Column {
            items.forEachIndexed { index, item ->
                DoctorProfileMenuItem(item = item)
                if (index < items.size - 1) Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outlineVariant))
            }
        }
    }
}

@Composable
private fun DoctorProfileMenuItem(item: DoctorMenuItem) {
    Row(modifier = Modifier.fillMaxWidth().clickable { item.onClick() }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(PrimaryBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(imageVector = item.icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = item.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(text = item.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

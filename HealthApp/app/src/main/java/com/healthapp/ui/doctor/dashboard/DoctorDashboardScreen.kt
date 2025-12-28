package com.healthapp.ui.doctor.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthapp.ui.theme.*

@Composable
fun DoctorDashboardScreen(
    onNavigateToMessages: () -> Unit = {},
    onNavigateToAlarmDetail: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "数据大屏", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "实时监控患者健康状态", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(20.dp))

        // 统计卡片
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(icon = Icons.Default.People, title = "管理患者", value = "128", color = PrimaryBlue, modifier = Modifier.weight(1f))
            StatCard(icon = Icons.Default.Watch, title = "在线设备", value = "96", color = SuccessGreen, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(icon = Icons.Default.Notifications, title = "今日告警", value = "12", color = WarningOrange, modifier = Modifier.weight(1f))
            StatCard(icon = Icons.Default.Warning, title = "异常患者", value = "5", color = ErrorRed, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 告警趋势
        Text(text = "告警趋势（近7天）", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        TrendChart(modifier = Modifier.fillMaxWidth().height(180.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // 告警类型分布
        Text(text = "告警类型分布", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        AlarmDistributionChart()

        Spacer(modifier = Modifier.height(24.dp))

        // 待处理任务
        Text(text = "待处理任务", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        PendingTasksList()

        Spacer(modifier = Modifier.height(80.dp))
    }
}


@Composable
private fun StatCard(icon: ImageVector, title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().background(brush = Brush.linearGradient(listOf(color, color.copy(alpha = 0.7f)))).padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(text = value, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TrendChart(modifier: Modifier = Modifier) {
    val data = remember { listOf(8, 12, 6, 15, 10, 18, 12) }
    val days = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    var animProgress by remember { mutableFloatStateOf(0f) }
    val animValue by animateFloatAsState(targetValue = animProgress, animationSpec = tween(1000), label = "trend")
    LaunchedEffect(Unit) { animProgress = 1f }

    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxVal = data.maxOrNull()?.toFloat() ?: 1f
                val barWidth = size.width / data.size * 0.6f
                val spacing = size.width / data.size
                data.forEachIndexed { i, v ->
                    val barHeight = (v / maxVal) * size.height * 0.8f * animValue
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(PrimaryBlue, SecondaryTeal)),
                        topLeft = Offset(i * spacing + (spacing - barWidth) / 2, size.height - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceAround) {
                days.forEach { Text(text = it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
    }
}


@Composable
private fun AlarmDistributionChart() {
    val items = listOf(
        Triple("心率异常", 35, HeartRateColor),
        Triple("血压异常", 25, BloodPressureColor),
        Triple("血氧异常", 20, BloodOxygenColor),
        Triple("跌倒检测", 15, WarningOrange),
        Triple("其他", 5, MaterialTheme.colorScheme.outline)
    )
    var animProgress by remember { mutableFloatStateOf(0f) }
    val animValue by animateFloatAsState(targetValue = animProgress, animationSpec = tween(1200), label = "pie")
    LaunchedEffect(Unit) { animProgress = 1f }

    Card(shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(120.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    var startAngle = -90f
                    items.forEach { (_, pct, color) ->
                        val sweep = 360f * pct / 100f * animValue
                        drawArc(color = color, startAngle = startAngle, sweepAngle = sweep, useCenter = false, style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Butt), size = Size(size.width, size.height))
                        startAngle += sweep
                    }
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEach { (name, pct, color) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = name, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Text(text = "$pct%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingTasksList() {
    val tasks = listOf(
        Triple("张三", "心率持续偏高，需关注", "紧急"),
        Triple("李四", "血压异常，建议复查", "一般"),
        Triple("王五", "设备离线超过24小时", "提醒")
    )
    Card(shape = RoundedCornerShape(16.dp)) {
        Column {
            tasks.forEachIndexed { i, (name, content, level) ->
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                            Text(text = name.take(1), color = PrimaryBlue, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(text = content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }
                    val levelColor = when (level) { "紧急" -> ErrorRed; "一般" -> WarningOrange; else -> PrimaryBlue }
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(levelColor.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(text = level, style = MaterialTheme.typography.labelSmall, color = levelColor)
                    }
                }
                if (i < tasks.size - 1) Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outlineVariant))
            }
        }
    }
}

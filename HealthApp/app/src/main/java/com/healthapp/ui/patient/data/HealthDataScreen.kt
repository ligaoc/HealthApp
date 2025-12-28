package com.healthapp.ui.patient.data

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthapp.ui.theme.BloodOxygenColor
import com.healthapp.ui.theme.BloodPressureColor
import com.healthapp.ui.theme.BloodSugarColor
import com.healthapp.ui.theme.GradientEnd
import com.healthapp.ui.theme.GradientStart
import com.healthapp.ui.theme.HeartRateColor
import com.healthapp.ui.theme.PrimaryBlue
import com.healthapp.ui.theme.TemperatureColor

@Composable
fun HealthDataScreen() {
    var selectedType by remember { mutableIntStateOf(0) }
    var selectedRange by remember { mutableIntStateOf(0) }

    val dataTypes = listOf(
        DataTypeItem("心率", Icons.Default.Favorite, HeartRateColor, "78", "bpm"),
        DataTypeItem("血压", Icons.Default.LocalHospital, BloodPressureColor, "120/80", "mmHg"),
        DataTypeItem("血氧", Icons.Default.Opacity, BloodOxygenColor, "98", "%"),
        DataTypeItem("体温", Icons.Default.Thermostat, TemperatureColor, "36.5", "°C"),
        DataTypeItem("血糖", Icons.Default.WaterDrop, BloodSugarColor, "5.6", "mmol/L")
    )

    val timeRanges = listOf("今日", "本周", "本月")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "健康数据",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "查看您的健康趋势",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 数据类型选择
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dataTypes.forEachIndexed { index, item ->
                DataTypeChip(
                    item = item,
                    selected = selectedType == index,
                    onClick = { selectedType = index },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 当前数值卡片
        CurrentValueCard(dataType = dataTypes[selectedType])

        Spacer(modifier = Modifier.height(20.dp))

        // 时间范围选择
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeRanges.forEachIndexed { index, range ->
                FilterChip(
                    selected = selectedRange == index,
                    onClick = { selectedRange = index },
                    label = { Text(range) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 趋势图表
        TrendChart(
            color = dataTypes[selectedType].color,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 统计数据
        StatisticsSection(dataType = dataTypes[selectedType])

        Spacer(modifier = Modifier.height(80.dp))
    }
}

data class DataTypeItem(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val value: String,
    val unit: String
)

@Composable
private fun DataTypeChip(
    item: DataTypeItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) item.color.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                tint = if (selected) item.color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) item.color else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CurrentValueCard(dataType: DataTypeItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(dataType.color, dataType.color.copy(alpha = 0.7f))
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "当前${dataType.name}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = dataType.value,
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dataType.unit,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "状态正常",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = dataType.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendChart(
    color: Color,
    modifier: Modifier = Modifier
) {
    val mockData = remember { listOf(65f, 72f, 68f, 78f, 75f, 82f, 78f, 85f, 80f, 76f, 78f, 74f) }
    var animationProgress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(1500),
        label = "chart"
    )

    LaunchedEffect(Unit) {
        animationProgress = 1f
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxValue = mockData.maxOrNull() ?: 100f
                val minValue = mockData.minOrNull() ?: 0f
                val range = maxValue - minValue
                val stepX = size.width / (mockData.size - 1)
                val points = mockData.mapIndexed { index, value ->
                    Offset(
                        x = index * stepX,
                        y = size.height - ((value - minValue) / range * size.height * 0.8f + size.height * 0.1f)
                    )
                }

                // 绘制渐变区域
                val path = Path().apply {
                    moveTo(0f, size.height)
                    points.forEachIndexed { index, point ->
                        if (index <= (points.size - 1) * animatedProgress) {
                            lineTo(point.x, point.y)
                        }
                    }
                    val lastIndex = ((points.size - 1) * animatedProgress).toInt()
                    lineTo(points[lastIndex].x, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.3f), Color.Transparent)
                    )
                )

                // 绘制线条
                for (i in 0 until ((points.size - 1) * animatedProgress).toInt()) {
                    drawLine(
                        color = color,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                // 绘制数据点
                points.forEachIndexed { index, point ->
                    if (index <= (points.size - 1) * animatedProgress) {
                        drawCircle(
                            color = color,
                            radius = 6.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = point
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsSection(dataType: DataTypeItem) {
    Text(
        text = "统计数据",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "平均值",
            value = "76",
            unit = dataType.unit,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "最高值",
            value = "92",
            unit = dataType.unit,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "最低值",
            value = "62",
            unit = dataType.unit,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

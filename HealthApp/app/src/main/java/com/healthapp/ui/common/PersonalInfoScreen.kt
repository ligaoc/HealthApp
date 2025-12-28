package com.healthapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthapp.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onBack: () -> Unit,
    viewModel: PersonalInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is PersonalInfoUiState.Success) {
            val profile = (uiState as PersonalInfoUiState.Success).profile
            name = profile.name
            gender = profile.gender ?: ""
            age = profile.age?.toString() ?: ""
            height = profile.height?.toString() ?: ""
            weight = profile.weight?.toString() ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人信息") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "编辑")
                        }
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
            // 头像
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.firstOrNull()?.toString() ?: "用",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when (uiState) {
                is PersonalInfoUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is PersonalInfoUiState.Error -> {
                    Text(
                        text = (uiState as PersonalInfoUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is PersonalInfoUiState.Success -> {
                    ProfileField("姓名", name, isEditing) { name = it }
                    ProfileSelectField("性别", gender, isEditing, listOf("male" to "男", "female" to "女")) { gender = it }
                    ProfileField("年龄", age, isEditing, KeyboardType.Number) { age = it }
                    ProfileField("身高(cm)", height, isEditing, KeyboardType.Number) { height = it }
                    ProfileField("体重(kg)", weight, isEditing, KeyboardType.Decimal) { weight = it }
                    ProfileField("手机号", (uiState as PersonalInfoUiState.Success).profile.phone, false) {}

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.weight(1f)
                            ) { Text("取消") }
                            Button(
                                onClick = {
                                    viewModel.updateProfile(
                                        name = name,
                                        gender = gender,
                                        age = age.toIntOrNull(),
                                        height = height.toIntOrNull(),
                                        weight = weight.toFloatOrNull()
                                    )
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("保存") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        if (isEditing && label != "手机号") {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true
            )
        } else {
            Text(
                text = value.ifEmpty { "未设置" },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSelectField(
    label: String,
    value: String,
    isEditing: Boolean,
    options: List<Pair<String, String>>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = options.find { it.first == value }?.second ?: "未设置"

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        if (isEditing) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = displayValue,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { (key, display) ->
                        DropdownMenuItem(
                            text = { Text(display) },
                            onClick = { onValueChange(key); expanded = false }
                        )
                    }
                }
            }
        } else {
            Text(text = displayValue, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 12.dp))
        }
        HorizontalDivider()
    }
}

# Design Document

## Overview

本设计文档描述健康管理Android应用功能完善的技术方案。主要包括患者端和医生端个人中心功能页面的实现、首页和工作台功能入口的完善，以及Mock后端接口的补充。

## Architecture

### 新增页面结构

```
app/src/main/java/com/healthapp/ui/
├── common/                          # 通用页面（患者和医生共用）
│   ├── PersonalInfoScreen.kt       # 个人信息页面
│   ├── NotificationSettingsScreen.kt # 消息通知设置
│   ├── PrivacySettingsScreen.kt    # 隐私设置
│   ├── SystemSettingsScreen.kt     # 系统设置
│   ├── AboutScreen.kt              # 关于我们
│   └── MessageListScreen.kt        # 消息列表
├── patient/
│   └── device/
│       └── DeviceManagementScreen.kt # 设备管理页面
└── doctor/
    ├── patients/
    │   └── PatientDetailScreen.kt   # 患者详情页面
    └── alarms/
        └── AlarmDetailScreen.kt     # 告警详情页面
```

### Mock服务器新增路由

```
mock-server/routes/
├── auth.js      # 新增: PUT /profile
└── user.js      # 新增: 通知设置、隐私设置、消息列表接口
```

## Components and Interfaces

### 1. 新增API接口

```kotlin
// UserApi.kt - 新增接口
interface UserApi {
    @PUT("api/user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ApiResponse<UserProfile>
    
    @GET("api/user/notifications")
    suspend fun getNotificationSettings(): ApiResponse<NotificationSettings>
    
    @PUT("api/user/notifications")
    suspend fun updateNotificationSettings(@Body settings: NotificationSettings): ApiResponse<NotificationSettings>
    
    @GET("api/user/privacy")
    suspend fun getPrivacySettings(): ApiResponse<PrivacySettings>
    
    @PUT("api/user/privacy")
    suspend fun updatePrivacySettings(@Body settings: PrivacySettings): ApiResponse<PrivacySettings>
    
    @GET("api/user/messages")
    suspend fun getMessages(@Query("page") page: Int, @Query("size") size: Int): ApiResponse<MessageListResponse>
}

// AlarmApi.kt - 新增接口
interface AlarmApi {
    @GET("api/alarm/detail")
    suspend fun getAlarmDetail(@Query("alarmId") alarmId: String): ApiResponse<AlarmDetail>
}
```

### 2. 导航路由扩展

```kotlin
// Screen.kt - 新增路由
sealed class Screen(val route: String) {
    // ... 现有路由
    
    // 通用页面
    object PersonalInfo : Screen("common/personal-info")
    object NotificationSettings : Screen("common/notification-settings")
    object PrivacySettings : Screen("common/privacy-settings")
    object SystemSettings : Screen("common/system-settings")
    object About : Screen("common/about")
    object MessageList : Screen("common/messages")
    
    // 患者端
    object DeviceManagement : Screen("patient/devices")
    
    // 医生端
    object PatientDetail : Screen("doctor/patient/{patientId}") {
        fun createRoute(patientId: String) = "doctor/patient/$patientId"
    }
    object AlarmDetail : Screen("doctor/alarm/{alarmId}") {
        fun createRoute(alarmId: String) = "doctor/alarm/$alarmId"
    }
}
```

### 3. 页面组件设计

#### 3.1 个人信息页面

```kotlin
@Composable
fun PersonalInfoScreen(
    onBack: () -> Unit,
    viewModel: PersonalInfoViewModel = hiltViewModel()
) {
    // 显示/编辑模式切换
    // 表单字段：头像、姓名、手机号、性别、年龄、身高、体重
    // 保存按钮
}
```

#### 3.2 设备管理页面

```kotlin
@Composable
fun DeviceManagementScreen(
    onBack: () -> Unit,
    viewModel: DeviceManagementViewModel = hiltViewModel()
) {
    // 设备列表（带状态颜色）
    // 添加设备FAB
    // 解绑确认对话框
}
```

#### 3.3 患者详情页面

```kotlin
@Composable
fun PatientDetailScreen(
    patientId: String,
    onBack: () -> Unit,
    viewModel: PatientDetailViewModel = hiltViewModel()
) {
    // 基本信息卡片
    // 实时体征卡片
    // 健康档案卡片
    // 设备列表
    // 告警记录
    // 紧急联系人
}
```

#### 3.4 告警详情页面

```kotlin
@Composable
fun AlarmDetailScreen(
    alarmId: String,
    onBack: () -> Unit,
    viewModel: AlarmDetailViewModel = hiltViewModel()
) {
    // 告警信息卡片
    // 患者信息卡片
    // 位置信息卡片
    // 处理操作按钮
    // 处理结果对话框
}
```

## Data Models

### 1. 用户设置相关

```kotlin
// NotificationSettings.kt
data class NotificationSettings(
    val alarmEnabled: Boolean = true,
    val medicationEnabled: Boolean = true,
    val healthTipsEnabled: Boolean = true,
    val systemEnabled: Boolean = true,
    val quietTimeStart: String? = null,  // "22:00"
    val quietTimeEnd: String? = null     // "08:00"
)

// PrivacySettings.kt
data class PrivacySettings(
    val shareWithDoctor: Boolean = true,
    val allowDataExport: Boolean = true
)

// UpdateProfileRequest.kt
data class UpdateProfileRequest(
    val name: String?,
    val gender: String?,
    val age: Int?,
    val height: Int?,
    val weight: Float?
)
```

### 2. 消息相关

```kotlin
// Message.kt
data class Message(
    val id: String,
    val type: String,      // alarm, medication, health_tip, system
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean
)

data class MessageListResponse(
    val total: Int,
    val page: Int,
    val size: Int,
    val messages: List<Message>
)
```

### 3. 告警详情

```kotlin
// AlarmDetail.kt
data class AlarmDetail(
    val alarmId: String,
    val type: String,
    val level: String,
    val title: String,
    val content: String,
    val time: String,
    val status: String,
    val patientInfo: PatientBasicInfo,
    val deviceInfo: DeviceInfo?,
    val location: LocationInfo?,
    val emergencyContacts: List<EmergencyContact>
)

data class PatientBasicInfo(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val phone: String,
    val diseases: List<String>
)

data class LocationInfo(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    val relation: String
)
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

基于需求分析，本功能主要是UI页面实现和导航集成，大部分需求适合使用示例测试。以下是可提取的属性测试：

### Property 1: 设备状态颜色映射一致性

*For any* 设备数据，在线设备应显示绿色状态指示，离线设备应显示灰色状态指示。

**Validates: Requirements 2.3**

### Property 2: 通知设置持久化一致性

*For any* 通知设置开关状态变更，保存后重新加载应返回相同的状态值。

**Validates: Requirements 3.3**

### Property 3: API更新响应一致性

*For any* 用户信息更新请求，Mock服务器返回的数据应包含更新后的字段值。

**Validates: Requirements 11.8**

### Property 4: 菜单项导航完整性

*For any* 个人中心菜单项，点击后应导航到对应的详情页面，不应出现空操作。

**Validates: Requirements 12.1**

## Error Handling

### 网络错误处理

```kotlin
// 统一错误处理
sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val message: String) : UiState<T>()
}

// ViewModel中使用
fun loadData() {
    viewModelScope.launch {
        _uiState.value = UiState.Loading()
        try {
            val result = repository.getData()
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "加载失败")
        }
    }
}
```

### 表单验证

```kotlin
// 个人信息表单验证
fun validateProfile(name: String, age: Int?, height: Int?, weight: Float?): ValidationResult {
    return when {
        name.isBlank() -> ValidationResult.Error("姓名不能为空")
        age != null && (age < 0 || age > 150) -> ValidationResult.Error("年龄无效")
        height != null && (height < 50 || height > 250) -> ValidationResult.Error("身高无效")
        weight != null && (weight < 20 || weight > 300) -> ValidationResult.Error("体重无效")
        else -> ValidationResult.Success
    }
}
```

## Testing Strategy

### 测试类型

| 测试类型 | 工具 | 覆盖范围 |
|----------|------|----------|
| 单元测试 | JUnit5 + MockK | ViewModel、Repository、工具类 |
| UI测试 | Compose Testing | 页面渲染、导航、交互 |
| 集成测试 | MockWebServer | API调用 |

### 单元测试示例

```kotlin
// PersonalInfoViewModelTest.kt
class PersonalInfoViewModelTest {
    @Test
    fun `updateProfile with valid data should succeed`() = runTest {
        // Given
        val mockRepository = mockk<UserRepository>()
        coEvery { mockRepository.updateProfile(any()) } returns Result.success(mockProfile)
        
        val viewModel = PersonalInfoViewModel(mockRepository)
        
        // When
        viewModel.updateProfile("张三", "male", 65, 170, 68f)
        
        // Then
        assertTrue(viewModel.uiState.value is UiState.Success)
    }
}
```

### UI测试示例

```kotlin
// DeviceManagementScreenTest.kt
class DeviceManagementScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `online device should show green indicator`() {
        // Given
        val onlineDevice = Device(status = "online", ...)
        
        // When
        composeTestRule.setContent {
            DeviceItem(device = onlineDevice)
        }
        
        // Then
        composeTestRule.onNodeWithTag("status_indicator")
            .assertExists()
            // 验证颜色为绿色
    }
}
```

### 测试配置

- 使用 JUnit5 进行单元测试
- 使用 Compose Testing 进行UI测试
- 每个ViewModel和Repository需要单元测试覆盖
- 关键页面需要UI测试覆盖

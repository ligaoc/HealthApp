# Design Document

## Overview

本设计文档描述健康管理Android演示应用的技术架构和实现方案。应用采用现代Android开发技术栈（Kotlin + Jetpack Compose），遵循MVVM + Clean Architecture架构模式，支持患者和医生两种角色，提供炫酷的数据可视化界面。

## Architecture

### 整体架构

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Screens   │  │  ViewModels │  │   UI Components     │ │
│  │  (Compose)  │  │   (State)   │  │  (Charts, Cards)    │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                       Domain Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Use Cases  │  │   Models    │  │    Repositories     │ │
│  │             │  │  (Domain)   │  │    (Interfaces)     │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                        Data Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  API Service│  │ Local Store │  │  Repository Impl    │ │
│  │  (Retrofit) │  │ (DataStore) │  │                     │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Mock Server (Node.js)                    │
└─────────────────────────────────────────────────────────────┘
```

### 模块划分

```
app/
├── di/                     # 依赖注入模块
├── data/                   # 数据层
│   ├── api/               # API接口定义
│   ├── model/             # 数据模型(DTO)
│   └── repository/        # Repository实现
├── domain/                 # 领域层
│   ├── model/             # 领域模型
│   └── repository/        # Repository接口
├── ui/                     # 表现层
│   ├── theme/             # 主题配置
│   ├── components/        # 通用组件
│   ├── navigation/        # 导航配置
│   ├── auth/              # 认证模块
│   ├── patient/           # 患者端模块
│   └── doctor/            # 医生端模块
└── util/                   # 工具类
```

## Components and Interfaces

### 1. 网络层接口

```kotlin
// AuthApi.kt
interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
    
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>
    
    @GET("api/user/profile")
    suspend fun getProfile(): ApiResponse<UserProfile>
}

// HealthApi.kt
interface HealthApi {
    @GET("api/health/realtime")
    suspend fun getRealtimeData(@Query("userId") userId: String): ApiResponse<RealtimeHealthData>
    
    @GET("api/health/history")
    suspend fun getHistoryData(
        @Query("userId") userId: String,
        @Query("type") type: String,
        @Query("range") range: String
    ): ApiResponse<HistoryHealthData>
    
    @GET("api/health/score")
    suspend fun getHealthScore(@Query("userId") userId: String): ApiResponse<HealthScore>
    
    @GET("api/health/medication-reminders")
    suspend fun getMedicationReminders(@Query("userId") userId: String): ApiResponse<MedicationReminders>
}

// DeviceApi.kt
interface DeviceApi {
    @GET("api/device/bindlist")
    suspend fun getDeviceList(@Query("userId") userId: String): ApiResponse<DeviceListResponse>
}

// AlarmApi.kt
interface AlarmApi {
    @GET("api/alarm/list")
    suspend fun getAlarmList(
        @Query("userId") userId: String,
        @Query("role") role: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<AlarmListResponse>
    
    @POST("api/alarm/sos")
    suspend fun sendSOS(@Body request: SOSRequest): ApiResponse<SOSResponse>
    
    @POST("api/alarm/handle")
    suspend fun handleAlarm(@Body request: HandleAlarmRequest): ApiResponse<Unit>
}

// DoctorApi.kt
interface DoctorApi {
    @GET("api/doctor/statistics")
    suspend fun getStatistics(@Query("doctorId") doctorId: String): ApiResponse<DoctorStatistics>
    
    @GET("api/doctor/patients")
    suspend fun getPatients(
        @Query("doctorId") doctorId: String,
        @Query("keyword") keyword: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<PatientListResponse>
    
    @GET("api/doctor/patient/{patientId}")
    suspend fun getPatientDetail(@Path("patientId") patientId: String): ApiResponse<PatientDetail>
}
```

### 2. Repository接口

```kotlin
// AuthRepository.kt
interface AuthRepository {
    suspend fun login(phone: String, password: String, role: String): Result<User>
    suspend fun register(phone: String, password: String, name: String, role: String): Result<User>
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
}

// HealthRepository.kt
interface HealthRepository {
    suspend fun getRealtimeData(userId: String): Result<RealtimeHealthData>
    suspend fun getHistoryData(userId: String, type: String, range: String): Result<HistoryHealthData>
    suspend fun getHealthScore(userId: String): Result<HealthScore>
    suspend fun getMedicationReminders(userId: String): Result<List<MedicationReminder>>
}

// DeviceRepository.kt
interface DeviceRepository {
    suspend fun getDeviceList(userId: String): Result<List<Device>>
}

// AlarmRepository.kt
interface AlarmRepository {
    suspend fun getAlarmList(userId: String, role: String, page: Int, size: Int): Result<AlarmListData>
    suspend fun sendSOS(userId: String, location: Location?, description: String): Result<SOSResult>
    suspend fun handleAlarm(alarmId: String, handleType: String, handleResult: String): Result<Unit>
}

// DoctorRepository.kt
interface DoctorRepository {
    suspend fun getStatistics(doctorId: String): Result<DoctorStatistics>
    suspend fun getPatients(doctorId: String, keyword: String?, page: Int, size: Int): Result<PatientListData>
    suspend fun getPatientDetail(patientId: String): Result<PatientDetail>
}
```

### 3. 导航结构

```kotlin
// Navigation.kt
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    
    // 患者端
    object PatientHome : Screen("patient/home")
    object PatientData : Screen("patient/data")
    object PatientDataDetail : Screen("patient/data/{type}")
    object PatientSOS : Screen("patient/sos")
    object PatientAlarmList : Screen("patient/alarms")
    object PatientProfile : Screen("patient/profile")
    
    // 医生端
    object DoctorDashboard : Screen("doctor/dashboard")
    object DoctorPatients : Screen("doctor/patients")
    object DoctorPatientDetail : Screen("doctor/patient/{patientId}")
    object DoctorAlarms : Screen("doctor/alarms")
    object DoctorAlarmDetail : Screen("doctor/alarm/{alarmId}")
    object DoctorProfile : Screen("doctor/profile")
}
```

## Data Models

### 1. 用户相关

```kotlin
// User.kt
data class User(
    val userId: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val avatar: String?,
    val gender: String?,
    val age: Int?,
    val organizationId: String?,
    val organizationName: String?
)

enum class UserRole {
    PATIENT, DOCTOR
}

// LoginRequest.kt
data class LoginRequest(
    val phone: String,
    val password: String,
    val role: String
)

// LoginResponse.kt
data class LoginResponse(
    val userId: String,
    val token: String,
    val role: String,
    val name: String,
    val avatar: String?
)
```

### 2. 健康数据相关

```kotlin
// RealtimeHealthData.kt
data class RealtimeHealthData(
    val heartRate: Int,
    val bloodPressure: BloodPressure,
    val bloodOxygen: Int,
    val temperature: Float,
    val bloodSugar: Float,
    val updateTime: String
)

data class BloodPressure(
    val systolic: Int,
    val diastolic: Int
)

// HistoryHealthData.kt
data class HistoryHealthData(
    val type: String,
    val unit: String,
    val records: List<HealthRecord>,
    val average: Float,
    val max: Float,
    val min: Float
)

data class HealthRecord(
    val time: String,
    val value: Float
)

// HealthScore.kt
data class HealthScore(
    val totalScore: Int,
    val details: ScoreDetails,
    val trend: String,
    val suggestion: String
)

data class ScoreDetails(
    val heartRate: Int,
    val bloodPressure: Int,
    val bloodOxygen: Int,
    val sleep: Int,
    val activity: Int
)

// MedicationReminder.kt
data class MedicationReminder(
    val id: String,
    val medicationName: String,
    val time: String,
    val status: String  // taken, pending
)
```

### 3. 设备相关

```kotlin
// Device.kt
data class Device(
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val status: String,  // online, offline
    val battery: Int,
    val lastSyncTime: String
)
```

### 4. 告警相关

```kotlin
// Alarm.kt
data class Alarm(
    val alarmId: String,
    val type: String,
    val level: AlarmLevel,
    val title: String,
    val content: String,
    val patientId: String,
    val patientName: String,
    val deviceId: String,
    val location: String,
    val time: String,
    val status: String  // pending, handled
)

enum class AlarmLevel {
    CRITICAL, HIGH, MEDIUM, LOW
}

// SOSRequest.kt
data class SOSRequest(
    val userId: String,
    val location: LocationData?,
    val description: String
)

data class LocationData(
    val address: String,
    val latitude: Double,
    val longitude: Double
)
```

### 5. 医生端相关

```kotlin
// DoctorStatistics.kt
data class DoctorStatistics(
    val overview: Overview,
    val alarmTrend: List<TrendData>,
    val alarmTypeDistribution: List<Distribution>,
    val deviceTypeDistribution: List<Distribution>,
    val pendingTasks: List<PendingTask>
)

data class Overview(
    val totalPatients: Int,
    val onlineDevices: Int,
    val todayAlarms: Int,
    val abnormalPatients: Int
)

data class TrendData(
    val date: String,
    val count: Int
)

data class Distribution(
    val type: String,
    val name: String,
    val count: Int
)

data class PendingTask(
    val type: String,
    val patientName: String,
    val content: String,
    val time: String,
    val level: String
)

// PatientListItem.kt
data class PatientListItem(
    val patientId: String,
    val name: String,
    val gender: String,
    val age: Int,
    val phone: String,
    val diseases: List<String>,
    val riskLevel: String,
    val deviceStatus: String,
    val lastUpdateTime: String
)
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

基于需求分析，本项目主要是演示应用，大部分需求是UI展示和功能存在性验证，适合使用示例测试。以下是可提取的属性测试：

### Property 1: 登录后角色路由正确性

*For any* 有效的用户登录请求，当登录成功后，系统应根据用户角色（patient/doctor）路由到对应的首页（患者首页/医生工作台）。

**Validates: Requirements 2.6**

### Property 2: 告警列表排序正确性

*For any* 告警列表数据，列表应按紧急程度排序，顺序为：critical > high > medium > low。

**Validates: Requirements 8.2**

### Property 3: API响应数据解析一致性

*For any* 有效的API响应JSON数据，解析后再序列化应产生等价的数据结构（round-trip property）。

**Validates: Requirements 11.3, 11.4, 11.5, 11.6**

## Error Handling

### 网络错误处理

```kotlin
sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val code: Int, val message: String) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}

// 统一错误处理
fun handleApiError(code: Int, message: String): String {
    return when (code) {
        401 -> "登录已过期，请重新登录"
        403 -> "没有权限访问"
        404 -> "请求的资源不存在"
        500 -> "服务器错误，请稍后重试"
        else -> message.ifEmpty { "网络请求失败" }
    }
}
```

### UI错误状态

```kotlin
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Error, contentDescription = null)
        Text(text = message)
        Button(onClick = onRetry) {
            Text("重试")
        }
    }
}
```

## Testing Strategy

### 测试类型

| 测试类型 | 工具 | 覆盖范围 |
|----------|------|----------|
| 单元测试 | JUnit5 + MockK | Repository、ViewModel、工具类 |
| 属性测试 | Kotest Property Testing | 数据解析、排序逻辑 |
| UI测试 | Compose Testing | 关键页面和交互 |

### 单元测试示例

```kotlin
// AuthRepositoryTest.kt
class AuthRepositoryTest {
    @Test
    fun `login with valid credentials returns user`() = runTest {
        // Given
        val mockApi = mockk<AuthApi>()
        coEvery { mockApi.login(any()) } returns ApiResponse(
            code = 200,
            data = LoginResponse("U10001", "token", "patient", "张三", null)
        )
        
        val repository = AuthRepositoryImpl(mockApi, dataStore)
        
        // When
        val result = repository.login("13800138000", "123456", "patient")
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals("张三", result.getOrNull()?.name)
    }
}
```

### 属性测试配置

- 使用 Kotest Property Testing 库
- 每个属性测试运行 100 次迭代
- 测试标注格式: **Feature: health-app-android, Property N: [property_text]**


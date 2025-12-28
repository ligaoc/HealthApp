# Implementation Plan: 健康管理Android演示应用

## Overview

本实现计划将健康管理Android演示应用分解为可执行的开发任务。项目包含Android App和Mock后端服务两部分，采用增量开发方式，确保每个阶段都能构建运行。

## Tasks

- [x] 1. Android项目初始化
  - [x] 1.1 创建Android项目基础结构
    - 创建项目目录结构：HealthApp/
    - 配置settings.gradle.kts和根build.gradle.kts
    - 配置app模块build.gradle.kts（Kotlin、Compose、Hilt等依赖）
    - 配置AndroidManifest.xml（网络权限、应用信息）
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8_

  - [x] 1.2 配置主题和基础UI组件
    - 创建Color.kt定义主题颜色（科技蓝、深色主题）
    - 创建Theme.kt配置Material3主题（深色/浅色）
    - 创建Type.kt定义字体样式
    - 创建通用UI组件（LoadingIndicator、ErrorState）
    - _Requirements: 10.1, 10.2, 10.3_

  - [x] 1.3 配置网络层
    - 创建ApiResponse通用响应类
    - 配置Retrofit和OkHttp实例
    - 创建NetworkModule（Hilt依赖注入）
    - 配置BASE_URL指向Mock服务器
    - _Requirements: 1.6_

- [x] 2. Mock后端服务
  - [x] 2.1 创建Mock服务器基础结构
    - 创建mock-server目录和package.json
    - 创建server.js入口文件配置Express和CORS
    - 创建预置用户数据（患者、医生账号）
    - _Requirements: 11.1, 11.7_

  - [x] 2.2 实现认证接口
    - 实现POST /api/auth/login登录接口
    - 实现POST /api/auth/register注册接口
    - 实现GET /api/user/profile获取用户信息接口
    - _Requirements: 11.2_

  - [x] 2.3 实现健康数据接口
    - 实现GET /api/health/realtime实时体征接口
    - 实现GET /api/health/history历史数据接口
    - 实现GET /api/health/score健康评分接口
    - 实现GET /api/health/medication-reminders用药提醒接口
    - 实现随机数据生成逻辑
    - _Requirements: 11.3, 11.8_

  - [x] 2.4 实现设备和告警接口
    - 实现GET /api/device/bindlist设备列表接口
    - 实现GET /api/alarm/list告警列表接口
    - 实现POST /api/alarm/sos求助接口
    - 实现POST /api/alarm/handle处理告警接口
    - _Requirements: 11.4, 11.5_

  - [x] 2.5 实现医生端接口
    - 实现GET /api/doctor/statistics统计数据接口
    - 实现GET /api/doctor/patients患者列表接口
    - 实现GET /api/doctor/patient/:id患者详情接口
    - _Requirements: 11.6_

- [x] 3. Checkpoint - 验证Mock服务器
  - 启动Mock服务器，使用Postman或curl测试各接口
  - 确保所有接口返回正确的Mock数据

- [x] 4. 用户认证模块
  - [x] 4.1 创建认证数据层
    - 创建AuthApi接口定义
    - 创建LoginRequest、LoginResponse等DTO
    - 创建User领域模型
    - 创建AuthRepository接口和实现
    - 配置DataStore存储登录状态
    - _Requirements: 2.3, 2.4, 2.5_

  - [x] 4.2 创建认证ViewModel
    - 创建LoginViewModel处理登录逻辑
    - 实现登录状态管理（Loading、Success、Error）
    - 实现角色选择逻辑
    - _Requirements: 2.5, 2.6, 2.7_

  - [x] 4.3 创建启动页和登录页UI
    - 创建SplashScreen启动页（Logo、渐变背景）
    - 创建LoginScreen登录页（表单、角色切换Tab）
    - 实现登录表单验证
    - 实现登录成功后的导航跳转
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.6, 2.7_

  - [ ]* 4.4 编写认证模块单元测试
    - 测试AuthRepository登录逻辑
    - 测试LoginViewModel状态变化
    - _Requirements: 2.5_

- [ ] 5. 导航配置
  - [x] 5.1 配置Navigation Compose
    - 创建Screen密封类定义所有路由
    - 创建NavGraph配置导航图
    - 实现根据登录状态和角色的导航逻辑
    - 创建MainActivity作为导航宿主
    - _Requirements: 2.6, 2.9_

- [x] 6. Checkpoint - 验证登录流程
  - 确保启动页正常显示并跳转
  - 确保登录功能正常工作
  - 确保角色选择后跳转到正确页面

- [-] 7. 患者端首页
  - [x] 7.1 创建健康数据层
    - 创建HealthApi接口定义
    - 创建RealtimeHealthData、HealthScore等DTO
    - 创建HealthRepository接口和实现
    - _Requirements: 3.3, 3.4, 3.5, 3.6_

  - [ ] 7.2 创建患者首页ViewModel
    - 创建PatientHomeViewModel
    - 实现健康数据加载逻辑
    - 实现下拉刷新逻辑
    - _Requirements: 3.7_

  - [ ] 7.3 创建患者首页UI
    - 创建PatientHomeScreen主页面
    - 创建HealthScoreCard健康评分圆环图组件
    - 创建VitalSignsCard体征数据卡片组件
    - 创建DeviceStatusCard设备状态组件
    - 创建MedicationReminderCard用药提醒组件
    - 创建PatientBottomNavigation底部导航栏
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.8_

  - [ ]* 7.4 创建圆环图动画组件
    - 实现AnimatedCircularProgress组件
    - 添加数值动画效果
    - _Requirements: 10.5_

- [ ] 8. 健康数据详情页
  - [ ] 8.1 创建数据详情ViewModel
    - 创建HealthDataViewModel
    - 实现历史数据加载逻辑
    - 实现日/周/月切换逻辑
    - _Requirements: 4.2, 4.4_

  - [ ] 8.2 创建数据详情UI
    - 创建HealthDataScreen数据页面
    - 创建数据类型选择Tab（心率、血压、血氧等）
    - 创建折线图组件展示历史趋势
    - 创建统计信息卡片（平均值、最大值、最小值）
    - _Requirements: 4.1, 4.2, 4.3, 4.5, 4.6_

- [ ] 9. SOS求助功能
  - [ ] 9.1 创建告警数据层
    - 创建AlarmApi接口定义
    - 创建Alarm、SOSRequest等DTO
    - 创建AlarmRepository接口和实现
    - _Requirements: 5.4, 5.5_

  - [ ] 9.2 创建SOS页面
    - 创建SOSScreen求助页面
    - 创建脉冲动画SOS按钮组件
    - 实现求助确认对话框
    - 实现求助发送逻辑
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [ ] 9.3 创建告警记录页面
    - 创建AlarmListScreen告警列表页面
    - 创建AlarmItem告警列表项组件
    - _Requirements: 5.6, 5.7_

- [ ] 10. Checkpoint - 验证患者端功能
  - 确保患者首页正常显示所有数据
  - 确保健康数据详情页图表正常
  - 确保SOS求助功能正常工作

- [ ] 11. 医生端工作台
  - [ ] 11.1 创建医生数据层
    - 创建DoctorApi接口定义
    - 创建DoctorStatistics、PatientListItem等DTO
    - 创建DoctorRepository接口和实现
    - _Requirements: 6.3, 6.4, 6.5_

  - [ ] 11.2 创建工作台ViewModel
    - 创建DoctorDashboardViewModel
    - 实现统计数据加载逻辑
    - _Requirements: 6.3, 6.4, 6.5_

  - [ ] 11.3 创建工作台UI
    - 创建DoctorDashboardScreen工作台页面（深色主题）
    - 创建StatisticsCard统计卡片组件（患者数、告警数等）
    - 创建AlarmTrendChart告警趋势折线图组件
    - 创建PendingTaskList待办事项列表组件
    - 创建DoctorBottomNavigation底部导航栏
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

- [ ] 12. 患者管理功能
  - [ ] 12.1 创建患者列表页面
    - 创建PatientListScreen患者列表页面
    - 创建搜索栏组件
    - 创建PatientListItem患者列表项组件
    - 实现搜索和筛选功能
    - _Requirements: 7.1, 7.2, 7.3_

  - [ ] 12.2 创建患者详情页面
    - 创建PatientDetailScreen患者详情页面
    - 显示患者基本信息、实时体征、用药信息
    - _Requirements: 7.4, 7.5, 7.6_

- [ ] 13. 告警处理功能
  - [ ] 13.1 创建医生端告警列表
    - 创建DoctorAlarmListScreen告警列表页面
    - 实现按紧急程度排序显示
    - _Requirements: 8.1, 8.2, 8.3_

  - [ ]* 13.2 编写告警排序属性测试
    - **Property 2: 告警列表排序正确性**
    - **Validates: Requirements 8.2**

  - [ ] 13.3 创建告警详情和处理页面
    - 创建AlarmDetailScreen告警详情页面
    - 显示告警信息和患者信息
    - 实现告警处理功能
    - _Requirements: 8.4, 8.5, 8.6, 8.7_

- [ ] 14. 个人中心
  - [ ] 14.1 创建个人中心页面
    - 创建ProfileScreen个人中心页面
    - 显示用户头像、姓名、角色信息
    - 实现主题切换功能
    - 实现退出登录功能
    - 显示应用版本信息
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

- [ ] 15. UI优化和动画
  - [ ] 15.1 添加页面切换动画
    - 配置Navigation Compose页面切换动画
    - 添加淡入淡出和滑动效果
    - _Requirements: 10.4_

  - [ ] 15.2 优化图表动画
    - 优化折线图绘制动画
    - 优化圆环图动画效果
    - _Requirements: 10.5, 10.7_

- [ ] 16. Final Checkpoint - 完整功能验证
  - 验证患者端所有功能正常
  - 验证医生端所有功能正常
  - 验证主题切换功能
  - 验证APK构建成功
  - 在真机或模拟器上测试完整流程

## Notes

- 任务标记 `*` 为可选测试任务，可跳过以加快MVP开发
- Mock服务器需要先启动才能测试App功能
- 建议使用Android Studio Hedgehog或更新版本
- 每个Checkpoint确保阶段性功能完整可用

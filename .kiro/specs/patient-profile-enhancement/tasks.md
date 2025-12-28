# Implementation Plan: 健康管理App功能完善

## Overview

本实现计划将健康管理Android应用的功能完善分解为可执行的开发任务。包括Mock后端接口补充、患者端功能页面、医生端功能页面、以及导航集成。

## Tasks

- [x] 1. Mock后端接口补充
  - [x] 1.1 补充用户相关接口
    - 在auth.js中添加PUT /api/user/profile接口 ✓
    - 创建user.js路由文件 ✓
    - 实现GET/PUT /api/user/notifications接口 ✓
    - 实现GET/PUT /api/user/privacy接口 ✓
    - 实现GET /api/user/messages接口 ✓
    - _Requirements: 11.1, 11.2, 11.3, 11.4, 11.5, 11.7_

  - [x] 1.2 补充告警详情接口
    - GET /api/alarm/detail接口已存在 ✓
    - 返回完整的告警详情数据（患者信息、位置、紧急联系人）✓
    - _Requirements: 11.6_

- [x] 2. Checkpoint - 验证Mock接口
  - Mock服务器新增接口已完成 ✓
  - 用户设置、消息、告警详情接口已实现 ✓

- [x] 3. Android数据层扩展
  - [x] 3.1 创建新增API接口定义
    - 创建UserApi.kt定义用户设置相关接口 ✓
    - 在AlarmApi.kt中添加告警详情接口 ✓
    - _Requirements: 11.1-11.7_

  - [x] 3.2 创建数据模型
    - 创建NotificationSettings数据类 ✓
    - 创建PrivacySettings数据类 ✓
    - 创建Message和MessageListResponse数据类 ✓
    - 创建AlarmDetail数据类 ✓
    - _Requirements: 3.2, 4.2, 8.2_

  - [x] 3.3 创建Repository
    - 创建UserSettingsRepository接口和实现 ✓
    - 创建AlarmRepository接口和实现 ✓
    - _Requirements: 1.5, 3.3, 8.7_

- [x] 4. 通用功能页面实现
  - [x] 4.1 创建个人信息页面
    - 创建PersonalInfoScreen.kt ✓
    - 创建PersonalInfoViewModel.kt ✓
    - 实现查看/编辑模式切换 ✓
    - 实现表单验证和保存功能 ✓
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6_

  - [x] 4.2 创建消息通知设置页面
    - 创建NotificationSettingsScreen.kt ✓
    - 实现各类通知开关 ✓
    - 实现免打扰时间设置 ✓
    - 保存设置到服务器 ✓
    - _Requirements: 3.1, 3.2, 3.3, 3.4_

  - [x] 4.3 创建隐私设置页面
    - 创建PrivacySettingsScreen.kt ✓
    - 实现数据共享开关 ✓
    - 实现数据导出选项 ✓
    - 实现账号注销入口 ✓
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

  - [x] 4.4 创建系统设置页面
    - 创建SystemSettingsScreen.kt ✓
    - 实现主题切换功能 ✓
    - 实现语言设置选项 ✓
    - 实现清除缓存功能 ✓
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [x] 4.5 创建关于我们页面
    - 创建AboutScreen.kt ✓
    - 显示应用Logo、名称、版本号 ✓
    - 显示版权信息和协议链接 ✓
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

  - [x] 4.6 创建消息列表页面
    - 创建MessageListScreen.kt ✓
    - 创建MessageListViewModel.kt ✓
    - 实现消息列表展示 ✓
    - _Requirements: 9.1_

- [x] 5. Checkpoint - 验证通用页面
  - 所有通用页面已创建 ✓
  - 设置保存功能已实现 ✓

- [x] 6. 患者端功能完善
  - [x] 6.1 创建设备管理页面
    - 创建DeviceManagementScreen.kt ✓
    - 创建DeviceManagementViewModel.kt ✓
    - 实现设备列表展示（带状态颜色）✓
    - 实现添加设备对话框 ✓
    - 实现解绑确认对话框 ✓
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7_

  - [x] 6.2 完善患者个人中心导航
    - 修改PatientProfileScreen.kt添加菜单点击事件 ✓
    - 为每个菜单项配置导航回调 ✓
    - _Requirements: 12.1, 12.2_

  - [x] 6.3 完善患者首页功能入口
    - 修改PatientHomeScreen.kt ✓
    - 添加通知图标点击导航 ✓
    - _Requirements: 9.1, 9.2, 9.3_

- [x] 7. 医生端功能完善
  - [x] 7.1 创建患者详情页面
    - 创建PatientDetailScreen.kt ✓
    - 创建PatientDetailViewModel.kt ✓
    - 显示患者基本信息卡片 ✓
    - 显示实时体征数据卡片 ✓
    - 显示健康档案信息 ✓
    - 显示设备列表 ✓
    - 显示紧急联系人 ✓
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7_

  - [x] 7.2 创建告警详情页面
    - 创建AlarmDetailScreen.kt ✓
    - 创建AlarmDetailViewModel.kt ✓
    - 显示告警信息卡片 ✓
    - 显示患者信息卡片 ✓
    - 显示位置信息卡片 ✓
    - 实现处理操作按钮 ✓
    - 实现处理结果对话框 ✓
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7_

  - [x] 7.3 完善医生个人中心导航
    - 修改DoctorProfileScreen.kt添加菜单点击事件 ✓
    - 为每个菜单项配置导航回调 ✓
    - _Requirements: 12.1, 12.2_

  - [x] 7.4 完善患者列表点击
    - 修改PatientListScreen.kt ✓
    - 添加患者项点击导航到详情页 ✓
    - _Requirements: 7.1_

  - [x] 7.5 完善告警列表点击
    - 修改DoctorAlarmListScreen.kt ✓
    - 添加告警项点击导航到详情页 ✓
    - _Requirements: 8.1_

  - [x] 7.6 完善工作台功能入口
    - 修改DoctorDashboardScreen.kt ✓
    - 添加导航回调参数 ✓
    - _Requirements: 10.1, 10.2_

- [x] 8. 导航配置
  - [x] 8.1 扩展导航路由
    - 在AppNavigation.kt中添加新路由 ✓
    - 配置所有新页面的导航 ✓
    - _Requirements: 12.2, 12.3, 12.4_

  - [x] 8.2 更新MainScreen导航回调
    - 修改PatientMainScreen.kt传递导航回调 ✓
    - 修改DoctorMainScreen.kt传递导航回调 ✓
    - _Requirements: 12.1_

- [ ] 9. Checkpoint - 完整功能验证
  - 验证患者端所有功能页面正常
  - 验证医生端所有功能页面正常
  - 验证所有导航正常工作
  - 验证Mock数据正确显示

- [ ]* 10. 单元测试
  - [ ]* 10.1 ViewModel单元测试
    - 测试PersonalInfoViewModel
    - 测试DeviceManagementViewModel
    - 测试PatientDetailViewModel
    - 测试AlarmDetailViewModel
    - _Requirements: 1.5, 2.7, 8.7_

- [ ] 11. Final Checkpoint - 完整验证
  - 在真机或模拟器上测试完整流程
  - 验证APK构建成功
  - 确保所有点击功能正常响应

## Notes

- 任务标记 `*` 为可选测试任务，可跳过以加快开发
- Mock服务器需要先启动才能测试App功能
- 每个Checkpoint确保阶段性功能完整可用
- 优先完成Mock接口和数据层，再实现UI页面

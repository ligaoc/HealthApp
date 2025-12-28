# Requirements Document

## Introduction

本项目是一个健康管理Android演示应用，面向患者和医生两种角色，用于第三方医院客户演示。应用需要具备炫酷的界面效果、完整的登录注册流程、实时健康数据展示、告警处理等核心功能。项目需要能在Android Studio中正常构建并打包APK。

## Glossary

- **Health_App**: 健康管理Android应用程序
- **Patient_Module**: 患者端功能模块
- **Doctor_Module**: 医生端功能模块
- **Auth_System**: 用户认证系统，处理登录、注册、角色选择
- **Health_Data_Service**: 健康数据服务，获取和展示体征数据
- **Alarm_System**: 告警系统，处理健康异常告警和SOS求助
- **Device_Manager**: 设备管理器，管理智能健康设备绑定
- **Mock_Server**: Mock后端服务，提供演示数据
- **Dashboard**: 数据仪表盘，展示健康概览信息
- **Vital_Signs**: 体征数据，包括心率、血压、血氧、体温、血糖等

## Requirements

### Requirement 1: Android项目初始化

**User Story:** As a 开发者, I want 创建一个标准的Android项目结构, so that 项目能在Android Studio中正常打开、构建和打包APK。

#### Acceptance Criteria

1. THE Health_App SHALL 使用Kotlin作为开发语言
2. THE Health_App SHALL 使用Jetpack Compose作为UI框架
3. THE Health_App SHALL 配置Gradle构建脚本支持Android 8.0 (API 26)及以上版本
4. THE Health_App SHALL 配置目标SDK为Android 14 (API 34)
5. THE Health_App SHALL 集成Hilt依赖注入框架
6. THE Health_App SHALL 集成Retrofit和OkHttp网络请求库
7. THE Health_App SHALL 集成Navigation Compose导航组件
8. THE Health_App SHALL 集成Coil图片加载库
9. WHEN 在Android Studio中打开项目 THEN Health_App SHALL 能够成功同步Gradle依赖
10. WHEN 执行构建命令 THEN Health_App SHALL 能够成功生成APK文件

### Requirement 2: 用户认证功能

**User Story:** As a 用户, I want 能够注册和登录账号并选择角色, so that 我可以使用对应角色的功能。

#### Acceptance Criteria

1. WHEN 用户打开应用 THEN Auth_System SHALL 显示启动页，包含品牌Logo和应用名称
2. WHEN 启动页展示完成 THEN Auth_System SHALL 自动跳转到登录页面
3. THE Auth_System SHALL 提供手机号和密码登录功能
4. THE Auth_System SHALL 提供患者和医生两种角色选择
5. WHEN 用户输入正确的账号密码并选择角色 THEN Auth_System SHALL 调用登录接口并保存登录状态
6. WHEN 登录成功 THEN Auth_System SHALL 根据角色跳转到对应的首页（患者首页或医生工作台）
7. IF 登录失败 THEN Auth_System SHALL 显示错误提示信息
8. THE Auth_System SHALL 提供注册入口，支持新用户注册
9. WHEN 用户已登录 THEN Auth_System SHALL 在下次启动时自动跳转到对应首页
10. THE Auth_System SHALL 提供退出登录功能

### Requirement 3: 患者端首页仪表盘

**User Story:** As a 患者, I want 在首页看到我的健康概览信息, so that 我能快速了解自己的健康状况。

#### Acceptance Criteria

1. WHEN 患者登录成功 THEN Patient_Module SHALL 显示首页仪表盘
2. THE Dashboard SHALL 显示用户头像和问候语
3. THE Dashboard SHALL 显示健康评分，使用动态圆环图展示
4. THE Dashboard SHALL 显示今日体征数据卡片（心率、血压、血氧）
5. THE Dashboard SHALL 显示已绑定设备的状态和电量
6. THE Dashboard SHALL 显示今日用药提醒列表
7. WHEN 用户下拉刷新 THEN Health_Data_Service SHALL 重新获取最新数据
8. THE Dashboard SHALL 提供底部导航栏，包含首页、数据、求助、我的四个入口
9. WHEN 点击体征数据卡片 THEN Patient_Module SHALL 跳转到对应的数据详情页

### Requirement 4: 健康数据展示

**User Story:** As a 患者, I want 查看我的健康数据和历史趋势, so that 我能了解自己的健康变化。

#### Acceptance Criteria

1. THE Health_Data_Service SHALL 展示实时体征数据页面
2. THE Health_Data_Service SHALL 支持查看心率、血压、血氧、体温、血糖等数据
3. THE Health_Data_Service SHALL 使用折线图展示历史数据趋势
4. THE Health_Data_Service SHALL 支持按日、周、月切换查看历史数据
5. WHEN 数据异常 THEN Health_Data_Service SHALL 高亮显示异常数据
6. THE Health_Data_Service SHALL 显示数据的平均值、最大值、最小值统计

### Requirement 5: 告警与SOS求助

**User Story:** As a 患者, I want 能够一键发起紧急求助, so that 在紧急情况下能快速获得帮助。

#### Acceptance Criteria

1. THE Patient_Module SHALL 在底部导航提供醒目的SOS求助按钮
2. THE SOS按钮 SHALL 使用脉冲动画效果吸引注意
3. WHEN 用户点击SOS按钮 THEN Alarm_System SHALL 显示确认对话框
4. WHEN 用户确认求助 THEN Alarm_System SHALL 调用SOS接口发送求助请求
5. WHEN 求助发送成功 THEN Alarm_System SHALL 显示求助已发送的提示
6. THE Patient_Module SHALL 提供告警记录列表页面
7. THE 告警记录 SHALL 显示告警类型、时间、处理状态

### Requirement 6: 医生端工作台

**User Story:** As a 医生, I want 在工作台看到患者整体健康状况和告警信息, so that 我能及时处理异常情况。

#### Acceptance Criteria

1. WHEN 医生登录成功 THEN Doctor_Module SHALL 显示数据大屏工作台
2. THE 工作台 SHALL 使用深色主题，展示炫酷的数据可视化效果
3. THE 工作台 SHALL 显示管辖患者总数、在线设备数、今日告警数、异常患者数
4. THE 工作台 SHALL 使用折线图展示7日告警趋势
5. THE 工作台 SHALL 显示待处理事项列表，按紧急程度排序
6. THE Doctor_Module SHALL 提供底部导航栏，包含大屏、患者、告警、我的四个入口
7. WHEN 点击待处理事项 THEN Doctor_Module SHALL 跳转到对应的告警详情页

### Requirement 7: 患者管理功能

**User Story:** As a 医生, I want 查看和管理我的患者列表, so that 我能监护患者的健康状况。

#### Acceptance Criteria

1. THE Doctor_Module SHALL 提供患者列表页面
2. THE 患者列表 SHALL 支持搜索和筛选功能
3. THE 患者列表 SHALL 显示患者姓名、年龄、疾病、风险等级、设备状态
4. WHEN 点击患者 THEN Doctor_Module SHALL 跳转到患者详情页
5. THE 患者详情页 SHALL 显示患者基本信息、实时体征、健康档案
6. THE 患者详情页 SHALL 显示患者的用药信息和紧急联系人

### Requirement 8: 告警处理功能

**User Story:** As a 医生, I want 查看和处理患者告警, so that 我能及时响应异常情况。

#### Acceptance Criteria

1. THE Doctor_Module SHALL 提供告警列表页面
2. THE 告警列表 SHALL 按紧急程度排序显示（critical > high > medium > low）
3. THE 告警列表 SHALL 显示告警类型、患者信息、时间、处理状态
4. WHEN 点击告警 THEN Doctor_Module SHALL 跳转到告警详情页
5. THE 告警详情页 SHALL 显示完整的告警信息和患者信息
6. THE 告警详情页 SHALL 提供处理告警的操作按钮
7. WHEN 医生处理告警 THEN Alarm_System SHALL 调用处理接口更新告警状态

### Requirement 9: 个人中心

**User Story:** As a 用户, I want 管理我的个人信息和应用设置, so that 我能自定义使用体验。

#### Acceptance Criteria

1. THE Health_App SHALL 提供个人中心页面
2. THE 个人中心 SHALL 显示用户头像、姓名、角色信息
3. THE 个人中心 SHALL 提供主题切换功能（深色/浅色）
4. THE 个人中心 SHALL 提供退出登录功能
5. THE 个人中心 SHALL 显示应用版本信息

### Requirement 10: UI视觉效果

**User Story:** As a 演示人员, I want 应用具有炫酷的视觉效果, so that 能给客户留下深刻印象。

#### Acceptance Criteria

1. THE Health_App SHALL 支持深色和浅色两种主题
2. THE Health_App SHALL 使用科技感蓝色作为主色调
3. THE Health_App SHALL 使用圆角卡片设计
4. THE Health_App SHALL 在页面切换时使用流畅的动画效果
5. THE Dashboard圆环图 SHALL 使用动画效果展示
6. THE SOS按钮 SHALL 使用脉冲动画效果
7. THE 数据图表 SHALL 支持动画效果

### Requirement 11: Mock后端服务

**User Story:** As a 开发者, I want 有一个Mock后端服务提供演示数据, so that 应用能正常展示功能。

#### Acceptance Criteria

1. THE Mock_Server SHALL 使用Node.js + Express搭建
2. THE Mock_Server SHALL 提供用户认证相关接口（登录、注册）
3. THE Mock_Server SHALL 提供健康数据相关接口（实时数据、历史数据、健康评分）
4. THE Mock_Server SHALL 提供设备管理相关接口
5. THE Mock_Server SHALL 提供告警相关接口
6. THE Mock_Server SHALL 提供医生端相关接口（患者列表、统计数据）
7. THE Mock_Server SHALL 预置演示账号（患者：13800138000/123456，医生：13900139000/123456）
8. WHEN 请求健康数据 THEN Mock_Server SHALL 返回随机波动的模拟数据

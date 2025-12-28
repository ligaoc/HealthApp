# 健康管理App Mock后端设计文档

## 1. 概述

### 1.1 目的
为健康管理Android App提供演示用的Mock数据服务，模拟真实后端接口返回。

### 1.2 技术选型
| 项目 | 方案 | 说明 |
|------|------|------|
| 运行环境 | Node.js | 轻量、快速启动 |
| Web框架 | Express | 简单易用 |
| 数据存储 | 内存/JSON文件 | 无需数据库，演示足够 |
| 跨域处理 | CORS | 支持App访问 |

---

## 2. 接口设计

### 2.1 用户认证模块

#### 2.1.1 用户注册
```
POST /api/auth/register
```
请求体：
```json
{
  "phone": "13800138000",
  "password": "123456",
  "verifyCode": "1234",
  "role": "patient",  // patient | doctor
  "name": "张三"
}
```
响应：
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": "U10001",
    "token": "mock-token-xxxxx"
  }
}
```

#### 2.1.2 用户登录
```
POST /api/auth/login
```
请求体：
```json
{
  "phone": "13800138000",
  "password": "123456",
  "role": "patient"
}
```
响应：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": "U10001",
    "token": "mock-token-xxxxx",
    "role": "patient",
    "name": "张三",
    "avatar": "https://example.com/avatar.png"
  }
}
```

#### 2.1.3 获取用户信息
```
GET /api/user/profile
```
响应：
```json
{
  "code": 200,
  "data": {
    "userId": "U10001",
    "name": "张三",
    "phone": "13800138000",
    "role": "patient",
    "avatar": "https://example.com/avatar.png",
    "gender": "male",
    "age": 65,
    "organizationId": "ORG001",
    "organizationName": "XX医院"
  }
}
```

---

### 2.2 健康数据模块

#### 2.2.1 获取实时体征数据
```
GET /api/health/realtime?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "heartRate": 72,
    "bloodPressure": {
      "systolic": 120,
      "diastolic": 80
    },
    "bloodOxygen": 98,
    "temperature": 36.5,
    "bloodSugar": 5.6,
    "updateTime": "2025-12-28 10:30:00"
  }
}
```

#### 2.2.2 获取历史体征数据
```
GET /api/health/history?userId={userId}&type={type}&range={range}
```
参数说明：
- type: heartRate | bloodPressure | bloodOxygen | temperature | bloodSugar
- range: day | week | month

响应：
```json
{
  "code": 200,
  "data": {
    "type": "heartRate",
    "unit": "bpm",
    "records": [
      { "time": "2025-12-28 08:00", "value": 68 },
      { "time": "2025-12-28 09:00", "value": 72 },
      { "time": "2025-12-28 10:00", "value": 75 },
      { "time": "2025-12-28 11:00", "value": 70 }
    ],
    "average": 71,
    "max": 75,
    "min": 68
  }
}
```

#### 2.2.3 获取健康评分
```
GET /api/health/score?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "totalScore": 85,
    "details": {
      "heartRate": 90,
      "bloodPressure": 85,
      "bloodOxygen": 95,
      "sleep": 70,
      "activity": 80
    },
    "trend": "up",
    "suggestion": "您的健康状况良好，建议保持规律作息"
  }
}
```

#### 2.2.4 获取心电图数据
```
GET /api/health/ecg?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "ecgData": [0.1, 0.3, 0.8, 1.2, 0.5, 0.2, -0.1, 0.1, 0.3, 0.9],
    "heartRate": 72,
    "status": "normal",
    "updateTime": "2025-12-28 10:30:00"
  }
}
```

---

### 2.3 健康档案模块

#### 2.3.1 获取健康档案
```
GET /api/health/archive?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "basicInfo": {
      "name": "张三",
      "gender": "male",
      "age": 65,
      "height": 170,
      "weight": 68,
      "bmi": 23.5
    },
    "medicalHistory": [
      { "disease": "高血压", "diagnosisDate": "2020-05-10", "status": "治疗中" },
      { "disease": "糖尿病", "diagnosisDate": "2021-03-15", "status": "治疗中" }
    ],
    "surgeryHistory": [
      { "name": "阑尾切除术", "date": "2015-08-20", "hospital": "XX医院" }
    ],
    "allergyHistory": ["青霉素", "磺胺类药物"],
    "familyHistory": ["高血压", "糖尿病"],
    "riskAssessment": {
      "fallRisk": "中风险",
      "nutritionRisk": "低风险",
      "pressureUlcerRisk": "低风险"
    }
  }
}
```

#### 2.3.2 获取用药列表
```
GET /api/health/medications?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "medications": [
      {
        "id": "M001",
        "name": "降压药",
        "dosage": "1片",
        "frequency": "每日1次",
        "time": ["08:00"],
        "startDate": "2024-01-01",
        "endDate": null,
        "reminder": true
      },
      {
        "id": "M002",
        "name": "降糖药",
        "dosage": "1片",
        "frequency": "每日2次",
        "time": ["08:00", "18:00"],
        "startDate": "2024-01-01",
        "endDate": null,
        "reminder": true
      }
    ]
  }
}
```

#### 2.3.3 获取今日用药提醒
```
GET /api/health/medication-reminders?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "reminders": [
      { "id": "R001", "medicationName": "降压药", "time": "08:00", "status": "taken" },
      { "id": "R002", "medicationName": "降糖药", "time": "08:00", "status": "taken" },
      { "id": "R003", "medicationName": "降糖药", "time": "18:00", "status": "pending" }
    ]
  }
}
```

---

### 2.4 设备管理模块

#### 2.4.1 获取绑定设备列表
```
GET /api/device/bindlist?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "devices": [
      {
        "deviceId": "D10001",
        "deviceName": "智能手表",
        "deviceType": "smartwatch",
        "status": "online",
        "battery": 85,
        "lastSyncTime": "2025-12-28 10:25:00"
      },
      {
        "deviceId": "D10002",
        "deviceName": "血压仪",
        "deviceType": "bloodPressure",
        "status": "offline",
        "battery": 60,
        "lastSyncTime": "2025-12-27 18:00:00"
      }
    ]
  }
}
```

#### 2.4.2 绑定设备
```
POST /api/device/binddevice
```
请求体：
```json
{
  "userId": "U10001",
  "deviceId": "D10003",
  "deviceType": "bloodSugar"
}
```
响应：
```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "deviceId": "D10003",
    "deviceName": "血糖仪",
    "deviceType": "bloodSugar",
    "status": "online"
  }
}
```

#### 2.4.3 解绑设备
```
DELETE /api/device/unbind?deviceId={deviceId}
```
响应：
```json
{
  "code": 200,
  "message": "解绑成功"
}
```

---

### 2.5 告警模块

#### 2.5.1 获取告警列表
```
GET /api/alarm/list?userId={userId}&role={role}&page={page}&size={size}
```
响应：
```json
{
  "code": 200,
  "data": {
    "total": 25,
    "page": 1,
    "size": 10,
    "alarms": [
      {
        "alarmId": "A10001",
        "type": "heartRate",
        "level": "high",
        "title": "心率异常",
        "content": "检测到心率过快，当前心率120bpm",
        "patientId": "U10001",
        "patientName": "张三",
        "deviceId": "D10001",
        "location": "北京市朝阳区XX小区",
        "time": "2025-12-28 10:15:00",
        "status": "pending"
      },
      {
        "alarmId": "A10002",
        "type": "fall",
        "level": "critical",
        "title": "跌倒告警",
        "content": "检测到用户可能发生跌倒",
        "patientId": "U10002",
        "patientName": "李四",
        "deviceId": "D10005",
        "location": "北京市海淀区XX街道",
        "time": "2025-12-28 09:30:00",
        "status": "handled"
      }
    ]
  }
}
```

#### 2.5.2 获取告警详情
```
GET /api/alarm/detail?alarmId={alarmId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "alarmId": "A10001",
    "type": "heartRate",
    "level": "high",
    "title": "心率异常",
    "content": "检测到心率过快，当前心率120bpm，持续时间5分钟",
    "patientInfo": {
      "id": "U10001",
      "name": "张三",
      "age": 65,
      "phone": "13800138000",
      "medicalHistory": ["高血压", "糖尿病"]
    },
    "deviceInfo": {
      "deviceId": "D10001",
      "deviceName": "智能手表"
    },
    "location": {
      "address": "北京市朝阳区XX小区3号楼",
      "latitude": 39.9042,
      "longitude": 116.4074
    },
    "time": "2025-12-28 10:15:00",
    "status": "pending",
    "emergencyContacts": [
      { "name": "张三儿子", "phone": "13900139000", "relation": "子女" }
    ]
  }
}
```

#### 2.5.3 处理告警
```
POST /api/alarm/handle
```
请求体：
```json
{
  "alarmId": "A10001",
  "handleType": "phone",
  "handleResult": "已电话联系患者，确认为运动后心率升高，无异常",
  "handlerId": "D001"
}
```
响应：
```json
{
  "code": 200,
  "message": "处理成功"
}
```

#### 2.5.4 发起SOS求助
```
POST /api/alarm/sos
```
请求体：
```json
{
  "userId": "U10001",
  "location": {
    "address": "北京市朝阳区XX小区",
    "latitude": 39.9042,
    "longitude": 116.4074
  },
  "description": "感觉胸闷"
}
```
响应：
```json
{
  "code": 200,
  "message": "求助已发送",
  "data": {
    "alarmId": "A10010",
    "estimatedResponseTime": "5分钟内"
  }
}
```

---

### 2.6 医生端模块

#### 2.6.1 获取患者列表
```
GET /api/doctor/patients?doctorId={doctorId}&keyword={keyword}&page={page}&size={size}
```
响应：
```json
{
  "code": 200,
  "data": {
    "total": 128,
    "page": 1,
    "size": 20,
    "patients": [
      {
        "patientId": "U10001",
        "name": "张三",
        "gender": "male",
        "age": 65,
        "phone": "138****8000",
        "diseases": ["高血压", "糖尿病"],
        "riskLevel": "medium",
        "deviceStatus": "online",
        "lastUpdateTime": "2025-12-28 10:30:00"
      },
      {
        "patientId": "U10002",
        "name": "李四",
        "gender": "female",
        "age": 72,
        "phone": "139****9000",
        "diseases": ["冠心病"],
        "riskLevel": "high",
        "deviceStatus": "online",
        "lastUpdateTime": "2025-12-28 10:28:00"
      }
    ]
  }
}
```

#### 2.6.2 获取统计数据（大屏）
```
GET /api/doctor/statistics?doctorId={doctorId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "overview": {
      "totalPatients": 128,
      "onlineDevices": 96,
      "todayAlarms": 5,
      "abnormalPatients": 3
    },
    "alarmTrend": [
      { "date": "12-22", "count": 8 },
      { "date": "12-23", "count": 6 },
      { "date": "12-24", "count": 10 },
      { "date": "12-25", "count": 4 },
      { "date": "12-26", "count": 7 },
      { "date": "12-27", "count": 5 },
      { "date": "12-28", "count": 5 }
    ],
    "alarmTypeDistribution": [
      { "type": "heartRate", "name": "心率异常", "count": 12 },
      { "type": "bloodPressure", "name": "血压异常", "count": 8 },
      { "type": "fall", "name": "跌倒", "count": 3 },
      { "type": "sos", "name": "SOS求助", "count": 2 }
    ],
    "deviceTypeDistribution": [
      { "type": "smartwatch", "name": "智能手表", "count": 80 },
      { "type": "bloodPressure", "name": "血压仪", "count": 60 },
      { "type": "bloodSugar", "name": "血糖仪", "count": 45 },
      { "type": "bloodOxygen", "name": "血氧仪", "count": 30 }
    ],
    "pendingTasks": [
      { "type": "alarm", "patientName": "张三", "content": "心率异常", "time": "10分钟前", "level": "high" },
      { "type": "alarm", "patientName": "李四", "content": "血压偏高", "time": "30分钟前", "level": "medium" },
      { "type": "device", "patientName": "王五", "content": "设备离线", "time": "1小时前", "level": "low" }
    ]
  }
}
```

#### 2.6.3 获取患者详情
```
GET /api/doctor/patient/{patientId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "basicInfo": {
      "patientId": "U10001",
      "name": "张三",
      "gender": "male",
      "age": 65,
      "phone": "13800138000",
      "address": "北京市朝阳区XX小区"
    },
    "healthArchive": {
      "height": 170,
      "weight": 68,
      "bmi": 23.5,
      "diseases": ["高血压", "糖尿病"],
      "allergies": ["青霉素"],
      "riskLevel": "medium"
    },
    "realtimeData": {
      "heartRate": 72,
      "bloodPressure": { "systolic": 120, "diastolic": 80 },
      "bloodOxygen": 98,
      "temperature": 36.5,
      "updateTime": "2025-12-28 10:30:00"
    },
    "devices": [
      { "deviceId": "D10001", "deviceName": "智能手表", "status": "online", "battery": 85 }
    ],
    "recentAlarms": [
      { "alarmId": "A10001", "type": "heartRate", "time": "2025-12-28 10:15:00", "status": "pending" }
    ],
    "medications": [
      { "name": "降压药", "dosage": "1片", "frequency": "每日1次" },
      { "name": "降糖药", "dosage": "1片", "frequency": "每日2次" }
    ],
    "emergencyContacts": [
      { "name": "张三儿子", "phone": "13900139000", "relation": "子女" }
    ]
  }
}
```

#### 2.6.4 健康干预
```
POST /api/doctor/intervention
```
请求体：
```json
{
  "doctorId": "D001",
  "patientId": "U10001",
  "type": "suggestion",
  "content": "建议减少盐分摄入，每日食盐不超过6克",
  "priority": "normal"
}
```
响应：
```json
{
  "code": 200,
  "message": "干预建议已推送"
}
```

---

### 2.7 健康报告模块

#### 2.7.1 获取健康报告列表
```
GET /api/health/reports?userId={userId}
```
响应：
```json
{
  "code": 200,
  "data": {
    "reports": [
      {
        "reportId": "R20251228",
        "title": "12月健康报告",
        "type": "monthly",
        "generatedTime": "2025-12-28 00:00:00",
        "downloadUrl": "/api/health/report/download/R20251228"
      },
      {
        "reportId": "R20251221",
        "title": "第51周健康报告",
        "type": "weekly",
        "generatedTime": "2025-12-21 00:00:00",
        "downloadUrl": "/api/health/report/download/R20251221"
      }
    ]
  }
}
```

---

## 3. Mock数据策略

### 3.1 数据生成规则
| 数据类型 | 生成策略 |
|----------|----------|
| 心率 | 60-100 bpm 随机波动 |
| 血压 | 收缩压 110-140，舒张压 70-90 |
| 血氧 | 95-100% 随机 |
| 体温 | 36.0-37.2°C 随机 |
| 血糖 | 4.0-7.0 mmol/L 随机 |
| 心电图 | 预设波形数据循环 |

### 3.2 实时数据模拟
- 每次请求返回略有波动的数据，模拟真实场景
- 支持WebSocket推送实时数据（可选）

### 3.3 预置账号
| 角色 | 账号 | 密码 | 说明 |
|------|------|------|------|
| 患者 | 13800138000 | 123456 | 演示患者账号 |
| 患者 | 13800138001 | 123456 | 演示患者账号2 |
| 医生 | 13900139000 | 123456 | 演示医生账号 |

---

## 4. 项目结构

```
mock-server/
├── package.json
├── server.js                 # 入口文件
├── routes/
│   ├── auth.js              # 认证相关接口
│   ├── health.js            # 健康数据接口
│   ├── device.js            # 设备管理接口
│   ├── alarm.js             # 告警接口
│   └── doctor.js            # 医生端接口
├── data/
│   ├── users.json           # 用户数据
│   ├── patients.json        # 患者数据
│   ├── devices.json         # 设备数据
│   └── alarms.json          # 告警数据
└── utils/
    └── mockGenerator.js     # Mock数据生成器
```

---

## 5. 启动方式

```bash
cd mock-server
npm install
npm start
```

服务默认运行在 `http://localhost:3000`

---

## 版本信息

| 项目 | 内容 |
|------|------|
| 文档版本 | v1.0 |
| 创建日期 | 2025-12-28 |
| 文档类型 | Mock后端设计文档 |

const express = require('express');
const router = express.Router();

const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

// 获取当前时间字符串
const getCurrentTime = () => {
  const now = new Date();
  return now.toISOString().replace('T', ' ').substring(0, 19);
};

// 患者数据
const patients = [
  {
    patientId: 'U10001',
    name: '张三',
    gender: 'male',
    age: 65,
    phone: '138****8000',
    diseases: ['高血压', '糖尿病'],
    riskLevel: 'medium',
    deviceStatus: 'online',
    lastUpdateTime: getCurrentTime()
  },
  {
    patientId: 'U10002',
    name: '李四',
    gender: 'female',
    age: 72,
    phone: '139****9000',
    diseases: ['冠心病'],
    riskLevel: 'high',
    deviceStatus: 'online',
    lastUpdateTime: getCurrentTime()
  },
  {
    patientId: 'U10003',
    name: '王五',
    gender: 'male',
    age: 58,
    phone: '137****7000',
    diseases: ['糖尿病'],
    riskLevel: 'low',
    deviceStatus: 'offline',
    lastUpdateTime: '2025-12-27 18:30:00'
  },
  {
    patientId: 'U10004',
    name: '赵六',
    gender: 'female',
    age: 80,
    phone: '136****6000',
    diseases: ['高血压', '冠心病', '糖尿病'],
    riskLevel: 'high',
    deviceStatus: 'online',
    lastUpdateTime: getCurrentTime()
  },
  {
    patientId: 'U10005',
    name: '钱七',
    gender: 'male',
    age: 68,
    phone: '135****5000',
    diseases: ['高血压'],
    riskLevel: 'medium',
    deviceStatus: 'online',
    lastUpdateTime: getCurrentTime()
  }
];

// 获取统计数据（大屏）
router.get('/statistics', (req, res) => {
  const now = new Date();
  const alarmTrend = [];
  for (let i = 6; i >= 0; i--) {
    const date = new Date(now);
    date.setDate(date.getDate() - i);
    alarmTrend.push({
      date: `${date.getMonth() + 1}-${date.getDate()}`,
      count: randomInt(3, 12)
    });
  }

  res.json({
    code: 200,
    data: {
      overview: {
        totalPatients: 128,
        onlineDevices: 96,
        todayAlarms: randomInt(3, 8),
        abnormalPatients: randomInt(2, 5)
      },
      alarmTrend,
      alarmTypeDistribution: [
        { type: 'heartRate', name: '心率异常', count: randomInt(8, 15) },
        { type: 'bloodPressure', name: '血压异常', count: randomInt(5, 12) },
        { type: 'fall', name: '跌倒', count: randomInt(1, 5) },
        { type: 'sos', name: 'SOS求助', count: randomInt(1, 3) }
      ],
      deviceTypeDistribution: [
        { type: 'smartwatch', name: '智能手表', count: 80 },
        { type: 'bloodPressure', name: '血压仪', count: 60 },
        { type: 'bloodSugar', name: '血糖仪', count: 45 },
        { type: 'bloodOxygen', name: '血氧仪', count: 30 }
      ],
      pendingTasks: [
        { type: 'alarm', patientName: '张三', content: '心率异常', time: '10分钟前', level: 'high' },
        { type: 'alarm', patientName: '李四', content: '血压偏高', time: '30分钟前', level: 'medium' },
        { type: 'device', patientName: '王五', content: '设备离线', time: '1小时前', level: 'low' }
      ]
    }
  });
});

// 获取患者列表
router.get('/patients', (req, res) => {
  const { doctorId, keyword, page = 1, size = 20 } = req.query;

  let filteredPatients = [...patients];
  
  // 搜索过滤
  if (keyword) {
    filteredPatients = filteredPatients.filter(p => 
      p.name.includes(keyword) || p.patientId.includes(keyword)
    );
  }

  // 按风险等级排序
  const riskOrder = { high: 0, medium: 1, low: 2 };
  filteredPatients.sort((a, b) => riskOrder[a.riskLevel] - riskOrder[b.riskLevel]);

  const start = (page - 1) * size;
  const end = start + parseInt(size);
  const pagedPatients = filteredPatients.slice(start, end);

  res.json({
    code: 200,
    data: {
      total: filteredPatients.length,
      page: parseInt(page),
      size: parseInt(size),
      patients: pagedPatients
    }
  });
});

// 获取患者详情
router.get('/patient/:patientId', (req, res) => {
  const { patientId } = req.params;

  const patient = patients.find(p => p.patientId === patientId);
  if (!patient) {
    return res.json({
      code: 404,
      message: '患者不存在'
    });
  }

  res.json({
    code: 200,
    data: {
      basicInfo: {
        patientId: patient.patientId,
        name: patient.name,
        gender: patient.gender,
        age: patient.age,
        phone: '13800138000',
        address: '北京市朝阳区XX小区'
      },
      healthArchive: {
        height: 170,
        weight: 68,
        bmi: 23.5,
        diseases: patient.diseases,
        allergies: ['青霉素'],
        riskLevel: patient.riskLevel
      },
      realtimeData: {
        heartRate: randomInt(60, 100),
        bloodPressure: {
          systolic: randomInt(110, 140),
          diastolic: randomInt(70, 90)
        },
        bloodOxygen: randomInt(95, 100),
        temperature: parseFloat((36 + Math.random() * 1.2).toFixed(1)),
        updateTime: getCurrentTime()
      },
      devices: [
        { deviceId: 'D10001', deviceName: '智能手表', status: 'online', battery: 85 },
        { deviceId: 'D10002', deviceName: '血压仪', status: 'online', battery: 60 }
      ],
      recentAlarms: [
        { alarmId: 'A10001', type: 'heartRate', time: '2025-12-28 10:15:00', status: 'pending' },
        { alarmId: 'A10003', type: 'bloodPressure', time: '2025-12-28 08:45:00', status: 'handled' }
      ],
      medications: [
        { name: '降压药', dosage: '1片', frequency: '每日1次' },
        { name: '降糖药', dosage: '1片', frequency: '每日2次' }
      ],
      emergencyContacts: [
        { name: '张三儿子', phone: '13900139000', relation: '子女' }
      ]
    }
  });
});

// 健康干预
router.post('/intervention', (req, res) => {
  const { doctorId, patientId, type, content, priority } = req.body;

  res.json({
    code: 200,
    message: '干预建议已推送'
  });
});

module.exports = router;

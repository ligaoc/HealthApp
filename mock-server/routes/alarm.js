const express = require('express');
const router = express.Router();

const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

// 获取当前时间字符串
const getCurrentTime = () => {
  const now = new Date();
  return now.toISOString().replace('T', ' ').substring(0, 19);
};

// 告警数据
const alarms = [
  {
    alarmId: 'A10001',
    type: 'heartRate',
    level: 'high',
    title: '心率异常',
    content: '检测到心率过快，当前心率120bpm',
    patientId: 'U10001',
    patientName: '张三',
    deviceId: 'D10001',
    location: '北京市朝阳区XX小区',
    time: '2025-12-28 10:15:00',
    status: 'pending'
  },
  {
    alarmId: 'A10002',
    type: 'fall',
    level: 'critical',
    title: '跌倒告警',
    content: '检测到用户可能发生跌倒',
    patientId: 'U10002',
    patientName: '李四',
    deviceId: 'D10005',
    location: '北京市海淀区XX街道',
    time: '2025-12-28 09:30:00',
    status: 'handled'
  },
  {
    alarmId: 'A10003',
    type: 'bloodPressure',
    level: 'medium',
    title: '血压偏高',
    content: '检测到血压偏高，当前血压150/95mmHg',
    patientId: 'U10001',
    patientName: '张三',
    deviceId: 'D10002',
    location: '北京市朝阳区XX小区',
    time: '2025-12-28 08:45:00',
    status: 'pending'
  },
  {
    alarmId: 'A10004',
    type: 'bloodOxygen',
    level: 'high',
    title: '血氧过低',
    content: '检测到血氧饱和度过低，当前血氧92%',
    patientId: 'U10002',
    patientName: '李四',
    deviceId: 'D10004',
    location: '北京市海淀区XX街道',
    time: '2025-12-28 07:20:00',
    status: 'handled'
  },
  {
    alarmId: 'A10005',
    type: 'sos',
    level: 'critical',
    title: 'SOS求助',
    content: '用户发起紧急求助：感觉胸闷',
    patientId: 'U10001',
    patientName: '张三',
    deviceId: 'D10001',
    location: '北京市朝阳区XX小区3号楼',
    time: '2025-12-27 22:10:00',
    status: 'handled'
  }
];

// 获取告警列表
router.get('/list', (req, res) => {
  const { userId, role, page = 1, size = 10 } = req.query;

  // 按紧急程度排序
  const levelOrder = { critical: 0, high: 1, medium: 2, low: 3 };
  const sortedAlarms = [...alarms].sort((a, b) => {
    // 先按状态排序（pending优先）
    if (a.status !== b.status) {
      return a.status === 'pending' ? -1 : 1;
    }
    // 再按紧急程度排序
    return levelOrder[a.level] - levelOrder[b.level];
  });

  const start = (page - 1) * size;
  const end = start + parseInt(size);
  const pagedAlarms = sortedAlarms.slice(start, end);

  res.json({
    code: 200,
    data: {
      total: alarms.length,
      page: parseInt(page),
      size: parseInt(size),
      alarms: pagedAlarms
    }
  });
});

// 获取告警详情
router.get('/detail', (req, res) => {
  const { alarmId } = req.query;

  const alarm = alarms.find(a => a.alarmId === alarmId);
  if (!alarm) {
    return res.json({
      code: 404,
      message: '告警不存在'
    });
  }

  res.json({
    code: 200,
    data: {
      ...alarm,
      patientInfo: {
        id: alarm.patientId,
        name: alarm.patientName,
        age: 65,
        phone: '13800138000',
        medicalHistory: ['高血压', '糖尿病']
      },
      deviceInfo: {
        deviceId: alarm.deviceId,
        deviceName: '智能手表'
      },
      location: {
        address: alarm.location,
        latitude: 39.9042,
        longitude: 116.4074
      },
      emergencyContacts: [
        { name: '张三儿子', phone: '13900139000', relation: '子女' },
        { name: '张三女儿', phone: '13900139001', relation: '子女' }
      ]
    }
  });
});

// 处理告警
router.post('/handle', (req, res) => {
  const { alarmId, handleType, handleResult, handlerId } = req.body;

  const alarm = alarms.find(a => a.alarmId === alarmId);
  if (alarm) {
    alarm.status = 'handled';
  }

  res.json({
    code: 200,
    message: '处理成功'
  });
});

// 发起SOS求助
router.post('/sos', (req, res) => {
  const { userId, location, description } = req.body;

  const newAlarmId = `A${10000 + alarms.length + 1}`;
  const newAlarm = {
    alarmId: newAlarmId,
    type: 'sos',
    level: 'critical',
    title: 'SOS求助',
    content: `用户发起紧急求助：${description || '紧急情况'}`,
    patientId: userId,
    patientName: '张三',
    deviceId: 'D10001',
    location: location?.address || '未知位置',
    time: getCurrentTime(),
    status: 'pending'
  };

  alarms.unshift(newAlarm);

  res.json({
    code: 200,
    message: '求助已发送',
    data: {
      alarmId: newAlarmId,
      estimatedResponseTime: '5分钟内'
    }
  });
});

module.exports = router;

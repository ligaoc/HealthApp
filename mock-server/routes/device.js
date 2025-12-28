const express = require('express');
const router = express.Router();

const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

// 获取当前时间字符串
const getCurrentTime = () => {
  const now = new Date();
  return now.toISOString().replace('T', ' ').substring(0, 19);
};

// 设备数据
const devices = [
  {
    deviceId: 'D10001',
    deviceName: '智能手表',
    deviceType: 'smartwatch',
    status: 'online',
    battery: 85,
    lastSyncTime: getCurrentTime()
  },
  {
    deviceId: 'D10002',
    deviceName: '血压仪',
    deviceType: 'bloodPressure',
    status: 'online',
    battery: 60,
    lastSyncTime: getCurrentTime()
  },
  {
    deviceId: 'D10003',
    deviceName: '血糖仪',
    deviceType: 'bloodSugar',
    status: 'offline',
    battery: 45,
    lastSyncTime: '2025-12-27 18:00:00'
  },
  {
    deviceId: 'D10004',
    deviceName: '血氧仪',
    deviceType: 'bloodOxygen',
    status: 'online',
    battery: 92,
    lastSyncTime: getCurrentTime()
  }
];

// 获取绑定设备列表
router.get('/bindlist', (req, res) => {
  // 随机更新电量
  const updatedDevices = devices.map(device => ({
    ...device,
    battery: device.status === 'online' ? randomInt(device.battery - 5, device.battery) : device.battery,
    lastSyncTime: device.status === 'online' ? getCurrentTime() : device.lastSyncTime
  }));

  res.json({
    code: 200,
    data: {
      devices: updatedDevices
    }
  });
});

// 绑定设备
router.post('/binddevice', (req, res) => {
  const { userId, deviceId, deviceType } = req.body;

  const deviceNames = {
    smartwatch: '智能手表',
    bloodPressure: '血压仪',
    bloodSugar: '血糖仪',
    bloodOxygen: '血氧仪',
    ecg: '心电检测仪'
  };

  const newDevice = {
    deviceId,
    deviceName: deviceNames[deviceType] || '未知设备',
    deviceType,
    status: 'online',
    battery: 100,
    lastSyncTime: getCurrentTime()
  };

  devices.push(newDevice);

  res.json({
    code: 200,
    message: '绑定成功',
    data: newDevice
  });
});

// 解绑设备
router.delete('/unbind', (req, res) => {
  const { deviceId } = req.query;

  const index = devices.findIndex(d => d.deviceId === deviceId);
  if (index > -1) {
    devices.splice(index, 1);
  }

  res.json({
    code: 200,
    message: '解绑成功'
  });
});

// 获取设备状态
router.get('/status', (req, res) => {
  const { deviceId } = req.query;

  const device = devices.find(d => d.deviceId === deviceId);
  if (!device) {
    return res.json({
      code: 404,
      message: '设备不存在'
    });
  }

  res.json({
    code: 200,
    data: device
  });
});

module.exports = router;

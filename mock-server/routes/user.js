const express = require('express');
const router = express.Router();

// 通知设置数据
let notificationSettings = {
  alarmEnabled: true,
  medicationEnabled: true,
  healthTipsEnabled: true,
  systemEnabled: true,
  quietTimeEnabled: false,
  quietTimeStart: '22:00',
  quietTimeEnd: '08:00'
};

// 隐私设置数据
let privacySettings = {
  shareWithDoctor: true,
  shareWithFamily: true,
  allowDataExport: true,
  allowAnonymousAnalysis: false
};

// 消息数据
const messages = [
  {
    id: 'M10001',
    type: 'alarm',
    title: '心率异常提醒',
    content: '您的心率在10:15出现异常，当前心率120bpm，请注意休息。',
    time: '2025-12-28 10:15:00',
    isRead: false
  },
  {
    id: 'M10002',
    type: 'medication',
    title: '用药提醒',
    content: '该服用降压药了，请按时服药。',
    time: '2025-12-28 08:00:00',
    isRead: true
  },
  {
    id: 'M10003',
    type: 'health_tip',
    title: '健康小贴士',
    content: '冬季注意保暖，适当运动有助于提高免疫力。',
    time: '2025-12-27 09:00:00',
    isRead: true
  },
  {
    id: 'M10004',
    type: 'system',
    title: '系统通知',
    content: '您的智能手表固件已更新至最新版本。',
    time: '2025-12-26 14:30:00',
    isRead: true
  },
  {
    id: 'M10005',
    type: 'alarm',
    title: '血压偏高提醒',
    content: '您的血压在08:45检测到偏高，当前血压150/95mmHg，建议休息并监测。',
    time: '2025-12-28 08:45:00',
    isRead: false
  }
];

// 获取通知设置
router.get('/notifications', (req, res) => {
  res.json({
    code: 200,
    data: notificationSettings
  });
});

// 更新通知设置
router.put('/notifications', (req, res) => {
  const updates = req.body;
  notificationSettings = { ...notificationSettings, ...updates };
  
  res.json({
    code: 200,
    message: '设置已保存',
    data: notificationSettings
  });
});

// 获取隐私设置
router.get('/privacy', (req, res) => {
  res.json({
    code: 200,
    data: privacySettings
  });
});

// 更新隐私设置
router.put('/privacy', (req, res) => {
  const updates = req.body;
  privacySettings = { ...privacySettings, ...updates };
  
  res.json({
    code: 200,
    message: '设置已保存',
    data: privacySettings
  });
});

// 获取消息列表
router.get('/messages', (req, res) => {
  const { page = 1, size = 10, type } = req.query;
  
  let filteredMessages = messages;
  if (type && type !== 'all') {
    filteredMessages = messages.filter(m => m.type === type);
  }
  
  // 按时间倒序排序
  filteredMessages.sort((a, b) => new Date(b.time) - new Date(a.time));
  
  const start = (page - 1) * size;
  const end = start + parseInt(size);
  const pagedMessages = filteredMessages.slice(start, end);
  
  const unreadCount = messages.filter(m => !m.isRead).length;
  
  res.json({
    code: 200,
    data: {
      total: filteredMessages.length,
      page: parseInt(page),
      size: parseInt(size),
      unreadCount,
      messages: pagedMessages
    }
  });
});

// 标记消息已读
router.put('/messages/:messageId/read', (req, res) => {
  const { messageId } = req.params;
  
  const message = messages.find(m => m.id === messageId);
  if (message) {
    message.isRead = true;
  }
  
  res.json({
    code: 200,
    message: '已标记为已读'
  });
});

// 标记所有消息已读
router.put('/messages/read-all', (req, res) => {
  messages.forEach(m => m.isRead = true);
  
  res.json({
    code: 200,
    message: '已全部标记为已读'
  });
});

// 删除账号（模拟）
router.delete('/account', (req, res) => {
  res.json({
    code: 200,
    message: '账号注销申请已提交，将在7个工作日内处理'
  });
});

// 导出数据（模拟）
router.get('/export-data', (req, res) => {
  res.json({
    code: 200,
    message: '数据导出请求已提交',
    data: {
      exportId: `EXP${Date.now()}`,
      estimatedTime: '24小时内'
    }
  });
});

module.exports = router;

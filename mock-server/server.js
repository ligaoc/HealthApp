const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(express.json());

// 预置用户数据
const users = {
  '13800138000': {
    userId: 'U10001',
    name: '张三',
    phone: '13800138000',
    password: '123456',
    role: 'patient',
    avatar: null,
    gender: 'male',
    age: 65,
    organizationId: 'ORG001',
    organizationName: 'XX医院'
  },
  '13800138001': {
    userId: 'U10002',
    name: '李四',
    phone: '13800138001',
    password: '123456',
    role: 'patient',
    avatar: null,
    gender: 'female',
    age: 72,
    organizationId: 'ORG001',
    organizationName: 'XX医院'
  },
  '13900139000': {
    userId: 'D001',
    name: '李医生',
    phone: '13900139000',
    password: '123456',
    role: 'doctor',
    avatar: null,
    gender: 'male',
    age: 45,
    organizationId: 'ORG001',
    organizationName: 'XX医院'
  }
};

// 工具函数：生成随机数
const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;
const randomFloat = (min, max, decimals = 1) => parseFloat((Math.random() * (max - min) + min).toFixed(decimals));

// 导入路由
const authRoutes = require('./routes/auth');
const userRoutes = require('./routes/user');
const healthRoutes = require('./routes/health');
const deviceRoutes = require('./routes/device');
const alarmRoutes = require('./routes/alarm');
const doctorRoutes = require('./routes/doctor');

// 使用路由
app.use('/api/auth', authRoutes);
app.use('/api/user', authRoutes);  // 保留原有的profile接口
app.use('/api/user', userRoutes);  // 新增的用户设置接口
app.use('/api/health', healthRoutes);
app.use('/api/device', deviceRoutes);
app.use('/api/alarm', alarmRoutes);
app.use('/api/doctor', doctorRoutes);

// 健康检查
app.get('/', (req, res) => {
  res.json({ message: '健康管理Mock服务器运行中', version: '1.0.0' });
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`Mock服务器运行在 http://localhost:${PORT}`);
  console.log('预置账号:');
  console.log('  患者: 13800138000 / 123456');
  console.log('  患者: 13800138001 / 123456');
  console.log('  医生: 13900139000 / 123456');
});

module.exports = { users, randomInt, randomFloat };

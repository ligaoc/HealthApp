const express = require('express');
const router = express.Router();

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

// 登录
router.post('/login', (req, res) => {
  const { phone, password, role } = req.body;

  const user = users[phone];
  if (!user) {
    return res.json({
      code: 401,
      message: '用户不存在'
    });
  }

  if (user.password !== password) {
    return res.json({
      code: 401,
      message: '密码错误'
    });
  }

  if (user.role !== role) {
    return res.json({
      code: 401,
      message: '角色不匹配'
    });
  }

  res.json({
    code: 200,
    message: '登录成功',
    data: {
      userId: user.userId,
      token: `mock-token-${user.userId}-${Date.now()}`,
      role: user.role,
      name: user.name,
      avatar: user.avatar
    }
  });
});

// 注册
router.post('/register', (req, res) => {
  const { phone, password, verifyCode, role, name } = req.body;

  if (users[phone]) {
    return res.json({
      code: 400,
      message: '手机号已注册'
    });
  }

  const userId = role === 'patient' ? `U${10000 + Object.keys(users).length}` : `D${100 + Object.keys(users).length}`;
  
  users[phone] = {
    userId,
    name,
    phone,
    password,
    role,
    avatar: null,
    gender: null,
    age: null,
    organizationId: null,
    organizationName: null
  };

  res.json({
    code: 200,
    message: '注册成功',
    data: {
      userId,
      token: `mock-token-${userId}-${Date.now()}`
    }
  });
});

// 获取用户信息
router.get('/profile', (req, res) => {
  // 简化处理，返回第一个患者用户
  const user = users['13800138000'];
  
  res.json({
    code: 200,
    data: {
      userId: user.userId,
      name: user.name,
      phone: user.phone,
      role: user.role,
      avatar: user.avatar,
      gender: user.gender,
      age: user.age,
      organizationId: user.organizationId,
      organizationName: user.organizationName
    }
  });
});

module.exports = router;

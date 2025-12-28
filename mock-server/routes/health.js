const express = require('express');
const router = express.Router();

// 工具函数
const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;
const randomFloat = (min, max, decimals = 1) => parseFloat((Math.random() * (max - min) + min).toFixed(decimals));

// 获取当前时间字符串
const getCurrentTime = () => {
  const now = new Date();
  return now.toISOString().replace('T', ' ').substring(0, 19);
};

// 生成历史数据
const generateHistoryData = (type, range) => {
  const records = [];
  const now = new Date();
  let count = range === 'day' ? 24 : range === 'week' ? 7 : 30;
  
  for (let i = count - 1; i >= 0; i--) {
    const time = new Date(now);
    if (range === 'day') {
      time.setHours(now.getHours() - i);
    } else {
      time.setDate(now.getDate() - i);
    }
    
    let value;
    switch (type) {
      case 'heartRate':
        value = randomInt(60, 100);
        break;
      case 'bloodOxygen':
        value = randomInt(95, 100);
        break;
      case 'temperature':
        value = randomFloat(36.0, 37.2);
        break;
      case 'bloodSugar':
        value = randomFloat(4.0, 7.0);
        break;
      default:
        value = randomInt(60, 100);
    }
    
    records.push({
      time: time.toISOString().replace('T', ' ').substring(0, 16),
      value
    });
  }
  
  const values = records.map(r => r.value);
  return {
    records,
    average: parseFloat((values.reduce((a, b) => a + b, 0) / values.length).toFixed(1)),
    max: Math.max(...values),
    min: Math.min(...values)
  };
};

// 获取实时体征数据
router.get('/realtime', (req, res) => {
  res.json({
    code: 200,
    data: {
      heartRate: randomInt(60, 100),
      bloodPressure: {
        systolic: randomInt(110, 140),
        diastolic: randomInt(70, 90)
      },
      bloodOxygen: randomInt(95, 100),
      temperature: randomFloat(36.0, 37.2),
      bloodSugar: randomFloat(4.0, 7.0),
      updateTime: getCurrentTime()
    }
  });
});

// 获取历史体征数据
router.get('/history', (req, res) => {
  const { type = 'heartRate', range = 'day' } = req.query;
  
  const units = {
    heartRate: 'bpm',
    bloodOxygen: '%',
    temperature: '°C',
    bloodSugar: 'mmol/L',
    bloodPressure: 'mmHg'
  };
  
  const historyData = generateHistoryData(type, range);
  
  res.json({
    code: 200,
    data: {
      type,
      unit: units[type] || '',
      ...historyData
    }
  });
});

// 获取健康评分
router.get('/score', (req, res) => {
  const scores = {
    heartRate: randomInt(70, 100),
    bloodPressure: randomInt(70, 100),
    bloodOxygen: randomInt(85, 100),
    sleep: randomInt(60, 95),
    activity: randomInt(50, 90)
  };
  
  const totalScore = Math.round(
    (scores.heartRate + scores.bloodPressure + scores.bloodOxygen + scores.sleep + scores.activity) / 5
  );
  
  const suggestions = [
    '您的健康状况良好，建议保持规律作息',
    '建议适当增加运动量，每天步行30分钟',
    '血压略高，建议减少盐分摄入',
    '睡眠质量有待提高，建议晚上11点前入睡'
  ];
  
  res.json({
    code: 200,
    data: {
      totalScore,
      details: scores,
      trend: totalScore > 80 ? 'up' : 'down',
      suggestion: suggestions[randomInt(0, suggestions.length - 1)]
    }
  });
});

// 获取心电图数据
router.get('/ecg', (req, res) => {
  // 生成模拟心电图波形数据
  const ecgData = [];
  for (let i = 0; i < 100; i++) {
    const t = i / 10;
    // 模拟PQRST波形
    let value = 0;
    if (i % 20 < 2) value = 0.1; // P波
    else if (i % 20 === 5) value = -0.1; // Q波
    else if (i % 20 === 6) value = 1.2; // R波
    else if (i % 20 === 7) value = -0.2; // S波
    else if (i % 20 > 10 && i % 20 < 15) value = 0.3; // T波
    else value = randomFloat(-0.05, 0.05);
    ecgData.push(parseFloat(value.toFixed(2)));
  }
  
  res.json({
    code: 200,
    data: {
      ecgData,
      heartRate: randomInt(60, 100),
      status: 'normal',
      updateTime: getCurrentTime()
    }
  });
});

// 获取用药提醒
router.get('/medication-reminders', (req, res) => {
  const now = new Date();
  const currentHour = now.getHours();
  
  res.json({
    code: 200,
    data: {
      reminders: [
        {
          id: 'R001',
          medicationName: '降压药',
          time: '08:00',
          status: currentHour >= 8 ? 'taken' : 'pending'
        },
        {
          id: 'R002',
          medicationName: '降糖药',
          time: '08:00',
          status: currentHour >= 8 ? 'taken' : 'pending'
        },
        {
          id: 'R003',
          medicationName: '降糖药',
          time: '18:00',
          status: currentHour >= 18 ? 'taken' : 'pending'
        },
        {
          id: 'R004',
          medicationName: '维生素D',
          time: '12:00',
          status: currentHour >= 12 ? 'taken' : 'pending'
        }
      ]
    }
  });
});

// 获取健康档案
router.get('/archive', (req, res) => {
  res.json({
    code: 200,
    data: {
      basicInfo: {
        name: '张三',
        gender: 'male',
        age: 65,
        height: 170,
        weight: 68,
        bmi: 23.5
      },
      medicalHistory: [
        { disease: '高血压', diagnosisDate: '2020-05-10', status: '治疗中' },
        { disease: '糖尿病', diagnosisDate: '2021-03-15', status: '治疗中' }
      ],
      surgeryHistory: [
        { name: '阑尾切除术', date: '2015-08-20', hospital: 'XX医院' }
      ],
      allergyHistory: ['青霉素', '磺胺类药物'],
      familyHistory: ['高血压', '糖尿病'],
      riskAssessment: {
        fallRisk: '中风险',
        nutritionRisk: '低风险',
        pressureUlcerRisk: '低风险'
      }
    }
  });
});

// 获取健康报告列表
router.get('/reports', (req, res) => {
  res.json({
    code: 200,
    data: {
      reports: [
        {
          reportId: 'R20251228',
          title: '12月健康报告',
          type: 'monthly',
          generatedTime: '2025-12-28 00:00:00',
          downloadUrl: '/api/health/report/download/R20251228'
        },
        {
          reportId: 'R20251221',
          title: '第51周健康报告',
          type: 'weekly',
          generatedTime: '2025-12-21 00:00:00',
          downloadUrl: '/api/health/report/download/R20251221'
        }
      ]
    }
  });
});

module.exports = router;

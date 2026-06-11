# 🏥 医院管理系统

基于 SpringBoot + Vue3 的现代化医院管理系统，提供完整的医院信息管理、患者管理、挂号管理、药品管理和收费管理功能。

## 📋 功能模块

| 模块 | 功能描述 |
|------|----------|
| 📊 数据仪表盘 | 实时监控医院运营数据，包含统计卡片和可视化图表 |
| 🏥 医院管理 | 医院信息的增删改查、搜索、导出Excel、高德地图集成 |
| 👥 患者管理 | 患者信息管理、就诊记录、健康档案 |
| 📅 挂号管理 | 挂号记录管理、预约挂号、科室选择 |
| 💊 药品管理 | 药品库存管理、药品信息维护 |
| 💰 收费管理 | 收费记录、账单管理、支付统计 |

## 🛠️ 技术栈

### 后端
- **框架**: SpringBoot 2.7.x
- **数据库**: MySQL 8.0.31
- **ORM**: MyBatis
- **连接池**: Druid
- **安全**: MD5 密码加密

### 前端
- **框架**: Vue 3 + Vite
- **UI组件**: Element Plus
- **图表**: ECharts
- **路由**: Vue Router
- **图标**: Element Plus Icons

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- Node.js 16+
- MySQL 8.0+

### 后端启动

1. **创建数据库**
```sql
CREATE DATABASE IF NOT EXISTS de10 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **执行初始化脚本**
```bash
mysql -u root -p de10 < init_tables.sql
```

3. **修改数据库配置**
编辑 `src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/de10?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

4. **启动后端服务**
```bash
cd hospital-sys-sd
mvn spring-boot:run
```

### 前端启动

```bash
cd webui
npm install
npm run dev
```

### 访问地址
- 前端页面: http://localhost:5177
- 后端API: http://localhost:8080

## 📁 项目结构

```
hospital-sys-sd/
├── src/main/java/com/hy/
│   ├── controller/     # REST API控制层
│   ├── service/        # 业务逻辑层
│   ├── mapper/         # 数据访问层
│   ├── pojo/           # 实体类
│   ├── config/         # 配置类
│   └── HospitalApplication.java
├── src/main/resources/
│   ├── mapper/         # MyBatis映射文件
│   └── application.yml # 应用配置
├── webui/src/
│   ├── views/          # 页面组件
│   ├── components/     # 公共组件
│   ├── api/            # API接口
│   └── router/         # 路由配置
└── init_tables.sql     # 数据库初始化脚本
```

## 🗂️ 数据库表结构

| 表名 | 说明 |
|------|------|
| `sys_user` | 系统用户表 |
| `hospital` | 医院信息表 |
| `patient` | 患者信息表 |
| `register` | 挂号记录表 |
| `medicine` | 药品信息表 |
| `charge` | 收费记录表 |
| `department` | 科室信息表 |
| `sys_notification` | 通知信息表 |
| `sys_operation_log` | 操作日志表 |

## 🎨 界面预览

### 数据仪表盘
- 今日挂号量、在院人数、处方数、营收统计
- 近7日挂号量趋势图
- 各科室就诊人数对比图
- 患者年龄分布图

### 医院管理
- 医院列表展示（支持搜索、分页）
- 医院信息的增删改操作
- 数据导出Excel功能
- 高德地图定位显示

## 📝 API接口示例

### 获取医院列表
```
GET /api/hospital/list
```

### 添加医院
```
POST /api/hospital/add
Content-Type: application/json

{
  "hospitalName": "医院名称",
  "hospitalType": "综合",
  "hospitalLevel": "三甲",
  "hospitalAddress": "地址"
}
```

## 🔒 安全特性

- 用户密码 MD5 加密存储
- CORS 跨域配置
- RESTful API 设计规范

## 📄 许可证

MIT License

---

**开发团队**: 医院管理系统开发组  
**项目状态**: ✅ 开发完成
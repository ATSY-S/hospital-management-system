# 医院管理系统

基于 SpringBoot + Vue3 的医院管理系统，提供医院信息管理、患者管理、挂号管理、药品管理和收费管理等功能。

## 功能模块

- 数据仪表盘：实时监控医院运营数据，包含统计卡片和可视化图表
- 医院管理：医院信息的增删改查、搜索、导出Excel功能
- 患者管理：患者信息管理、就诊记录维护
- 挂号管理：挂号记录管理、预约挂号功能
- 药品管理：药品库存管理、药品信息维护
- 收费管理：收费记录、账单管理、支付统计

## 技术栈

### 后端
- 框架: SpringBoot 2.7.x
- 数据库: MySQL 8.0.31
- ORM: MyBatis
- 连接池: Druid

### 前端
- 框架: Vue 3 + Vite
- UI组件: Element Plus
- 图表: ECharts
- 路由: Vue Router

## 快速开始

### 环境要求
- JDK 1.8+
- Node.js 16+
- MySQL 8.0+

### 启动步骤

1. 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS de10 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本
```bash
mysql -u root -p de10 < init_tables.sql
```

3. 修改数据库配置
编辑 src/main/resources/application.yml，配置数据库连接信息

4. 启动后端服务
```bash
cd hospital-sys-sd
mvn spring-boot:run
```

5. 启动前端服务
```bash
cd webui
npm install
npm run dev
```

### 访问地址
- 前端页面: http://localhost:5177
- 后端API: http://localhost:8080

## 项目结构

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

## 数据库表结构

| 表名 | 说明 |
|------|------|
| sys_user | 系统用户表 |
| hospital | 医院信息表 |
| patient | 患者信息表 |
| register | 挂号记录表 |
| medicine | 药品信息表 |
| charge | 收费记录表 |
| department | 科室信息表 |

## API接口示例

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

## 安全特性

- 用户密码 MD5 加密存储
- CORS 跨域配置
- RESTful API 设计规范

## 许可证

MIT License
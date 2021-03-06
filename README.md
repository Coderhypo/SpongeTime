# SpongeTime

## 描述

一个集事件管理，团队看板于一身的小程序。
[http://todo.updev.cn](http://todo.updev.cn)

## 框架

目前使用了Struts2和Hibernate，暂时没有加入Spring，随着学习深入逐渐加入。
这个项目本来就是以边学习SSH和JSP边写的项目，每学到了新东西就会重构__(:з」∠)_。

## 模块

### 用户模块

#### 基础用户模块

基础用户模块实现日常用户功能（包括普通用户和管理员操作），如下：

 + 用户注册
 + 登陆模块
 + 信息修改
 + 用户管理（后台）

#### 用户权限划分

用户权限模块实现用户的权限分级，其中，所有用户分为管理员和普通用户。如下：
 
 + 用户权限
  - 超级管理员
  - 普通用户

因为涉及到团队看板，允许普通用户创建/管理团队，所以，普通用户可以具有团队内权限分级：

 + 团队权限
  - 团队创建者
  - 团队管理员
  - 团队成员

### 团队模块
 
#### 后台团队管理
 
 + 团队管理

#### 团队管理

 团队管理模块为使用用户对团队的操作，包括：

 + 创建/解散团队（团队创建者）
 + 团队管理员管理 （团队创建者）
 + 团队信息修改 （管理员及其以上）
 + 团队成员管理 （管理员及其以上）
 + 团队任务管理 （所有人）
 + 团队成员邀请 （所有人）

### 事件模块

#### 个人事件管理

  + 添加事件
  + 修改事件
  + 完成事件
  + 添加到事件组

#### 团队事件管理
 
  + 添加事件到事件组
  + 事件组事件任务分配

 团队模块的基础单位是事件组，事件不能独立存在，必须属于某个事件组，其他见团队事件组模块。
 
### 事件组

#### 个人事件组
  
  + 添加事件组
  + 添加事件到事件组
  + 从事件组移除（移除后，事件默认为无事件组状态）
  + 事件组管理

 从事件组移除：支持新建事件到事件组和添加已有事件，支持批量添加，仅支持个人事件操作。

#### 团队事件组
  
  + 添加事件组
  + 添加事件到事件组（新建事件添加到事件组）
  + 从事件组移除（移除后直接删除事件）
  + 事件组管理

### 进度管理模块

 进度管理是统计事件完成情况并给出建议todo顺序列表。

#### 权重计算模块
   
   根据事件权重（紧急重要，紧急不重要，不紧急重要，不紧急不重要），和事件组完成进度、预期完成时间，重新计算事件权重。
   
#### 建议todo模块

   根据计算出来的权重，为用户和团队推荐todo顺序列表。

## 数据库设计

## 用户模块

包括用户与团队的数据管理相关。

![users](https://raw.githubusercontent.com/Coderhypo/SpongeTime/master/users.png)

### 用户表

```
+---------------+-------------+------+-----+---------+----------------+
| Field         | Type        | Null | Key | Default | Extra          |
+---------------+-------------+------+-----+---------+----------------+
| user_id       | int(32)     | NO   | PRI | NULL    | auto_increment |
| user_login    | varchar(64) | NO   | MUL | NULL    |                |
| user_nicename | varchar(64) | NO   |     | NULL    |                |
| user_email    | varchar(64) | NO   |     | NULL    |                |
| user_pass     | varchar(64) | NO   |     | NULL    |                |
| user_url      | varchar(64) | YES  |     | NULL    |                |
| user_rule     | int(5)      | NO   |     | NULL    |                |
+---------------+-------------+------+-----+---------+----------------+
```

### 团队表

```
+------------------+-------------+------+-----+---------+----------------+
| Field            | Type        | Null | Key | Default | Extra          |
+------------------+-------------+------+-----+---------+----------------+
| user_group_id    | int(32)     | NO   | PRI | NULL    | auto_increment |
| user_group_name  | varchar(64) | NO   |     | NULL    |                |
| user_group_intro | mediumtext  | YES  |     | NULL    |                |
+------------------+-------------+------+-----+---------+----------------+
```

### 团队成员表

```
+-------------------+---------+------+-----+---------+-------+
| Field             | Type    | Null | Key | Default | Extra |
+-------------------+---------+------+-----+---------+-------+
| user_id           | int(32) | NO   | MUL | NULL    |       |
| group_id          | int(32) | NO   | MUL | NULL    |       |
| group_member_rule | int(5)  | NO   |     | NULL    |       |
+-------------------+---------+------+-----+---------+-------+
```

### 团队邀请表

```
+------------+---------+------+-----+---------+-------+
| Field      | Type    | Null | Key | Default | Extra |
+------------+---------+------+-----+---------+-------+
| group_id   | int(32) | NO   | MUL | NULL    |       |
| inviter_id | int(32) | NO   |     | NULL    |       |
| invitee_id | int(32) | NO   |     | NULL    |       |
+------------+---------+------+-----+---------+-------+
```

## 事件模块

事件与事件组相关，注意事件组在团队与个人使用中的区别。

![events](https://raw.githubusercontent.com/Coderhypo/SpongeTime/master/events.png)

### 事件表

```
+--------------------+------------+------+-----+---------+----------------+
| Field              | Type       | Null | Key | Default | Extra          |
+--------------------+------------+------+-----+---------+----------------+
| event_id           | int(64)    | NO   | PRI | NULL    | auto_increment |
| event_title        | mediumtext | YES  |     | NULL    |                |
| event_created_time | datetime   | NO   |     | NULL    |                |
| event_expect       | datetime   | NO   |     | NULL    |                |
| event_finish_time  | datetime   | NO   |     | NULL    |                |
| event_weight       | int(5)     | NO   |     | NULL    |                |
| event_owner_id     | int(32)    | NO   |     | NULL    |                |
| event_doer_id      | int(32)    | NO   |     | NULL    |                |
| event_ender_id     | int(32)    | NO   |     | NULL    |                |
| event_group_id     | int(32)    | NO   | MUL | NULL    |                |
+--------------------+------------+------+-----+---------+----------------+
```

### 事件组表

```
+--------------------------+------------+------+-----+---------+----------------+
| Field                    | Type       | Null | Key | Default | Extra          |
+--------------------------+------------+------+-----+---------+----------------+
| event_group_id           | int(32)    | NO   | PRI | NULL    | auto_increment |
| event_group_title        | mediumtext | NO   |     | NULL    |                |
| event_group_created_time | datetime   | NO   |     | NULL    |                |
| event_group_expect       | datetime   | NO   |     | NULL    |                |
| event_group_finish_time  | datetime   | NO   |     | NULL    |                |
| event_group_weight       | int(5)     | NO   |     | NULL    |                |
| event_group_owner_id     | int(32)    | NO   |     | NULL    |                |
| event_group_group_id     | int(32)    | YES  |     | NULL    |                |
+--------------------------+------------+------+-----+---------+----------------+
```

### 事件权重表

```
+--------------+-------------+------+-----+---------+-------+
| Field        | Type        | Null | Key | Default | Extra |
+--------------+-------------+------+-----+---------+-------+
| event_id     | int(64)     | NO   | MUL | NULL    |       |
| event_weight | double(5,2) | NO   |     | NULL    |       |
+--------------+-------------+------+-----+---------+-------+
```


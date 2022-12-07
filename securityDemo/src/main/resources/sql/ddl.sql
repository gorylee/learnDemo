-- learn_demo.learn_user definition

-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
      user_id           bigint(20)      not null auto_increment    comment '用户ID',
      login_name        varchar(30)     not null                   comment '登录账号',
      user_name         varchar(30)     default ''                 comment '用户昵称',
      user_type         varchar(2)      default '0'               comment '用户类型（1系统用户 0注册用户）',
      email             varchar(50)     default ''                 comment '用户邮箱',
      phone_number       varchar(11)     default ''                 comment '手机号码',
      sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
      avatar            varchar(100)    default ''                 comment '头像路径',
      password          varchar(200)     default ''                 comment '密码',
      status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
      del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
      create_by         varchar(64)     default ''                 comment '创建者',
      create_time       datetime                                   comment '创建时间',
      update_by         varchar(64)     default ''                 comment '更新者',
      update_time       datetime                                   comment '更新时间',
      remark            varchar(500)    default null               comment '备注',
      primary key (user_id)
) engine=innodb auto_increment=1 comment = '用户信息表';

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
      role_id           bigint(20)      not null auto_increment    comment '角色ID',
      role_name         varchar(30)     not null                   comment '角色名称',
      role_key          varchar(100)    not null                   comment '角色权限字符串',
      role_sort         int(4)          not null                   comment '显示顺序',
      data_scope        char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
      status            char(1)         not null                   comment '角色状态（0正常 1停用）',
      del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
      create_by         varchar(64)     default ''                 comment '创建者',
      create_time       datetime                                   comment '创建时间',
      update_by         varchar(64)     default ''                 comment '更新者',
      update_time       datetime                                   comment '更新时间',
      remark            varchar(500)    default null               comment '备注',
      primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
      menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
      menu_name         varchar(50)     not null                   comment '菜单名称',
      parent_id         bigint(20)      default 0                  comment '父菜单ID',
      order_num         int(4)          default 0                  comment '显示顺序',
      url               varchar(200)    default '#'                comment '请求地址',
      target            varchar(20)     default ''                 comment '打开方式（menuItem页签 menuBlank新窗口）',
      menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
      visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
      is_refresh        char(1)         default 1                  comment '是否刷新（0刷新 1不刷新）',
      perms             varchar(100)    default null               comment '权限标识',
      icon              varchar(100)    default '#'                comment '菜单图标',
      create_by         varchar(64)     default ''                 comment '创建者',
      create_time       datetime                                   comment '创建时间',
      update_by         varchar(64)     default ''                 comment '更新者',
      update_time       datetime                                   comment '更新时间',
      remark            varchar(500)    default ''                 comment '备注',
      primary key (menu_id)
) engine=innodb auto_increment=2 comment = '菜单权限表';

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
   id        bigint(20) not null auto_increment    comment 'ID',
   user_id   bigint(20) not null comment '用户ID',
   role_id   bigint(20) not null comment '角色ID',
   PRIMARY KEY (`id`),
   UNIQUE KEY(user_id, role_id)
) engine=innodb auto_increment=2 comment = '用户和角色关联表';

-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
   id        bigint(20) not null auto_increment    comment 'ID',
   role_id   bigint(20) not null comment '角色ID',
   menu_id   bigint(20) not null comment '菜单ID',
   PRIMARY KEY (`id`),
   UNIQUE KEY (`role_id`,`menu_id`)
) engine=innodb auto_increment=2 comment = '角色和菜单关联表';
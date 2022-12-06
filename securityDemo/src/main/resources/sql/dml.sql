-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'admin', 'lgy', '1', 'lgy@163.com', '15888888888', '1', '', '$2a$10$fl.1X4NRsNcUjaTpfzb8Ve9JZR6Rgr1tmQ1HmFA720ye1VQl6T6Qm',  '0', '0',  'admin', sysdate(), '', null, '管理员');
insert into sys_user values(2,  105, 'test',  'lee', '0', 'lgy@qq.com',  '15666666666', '1', '', '$2a$10$fl.1X4NRsNcUjaTpfzb8Ve9JZR6Rgr1tmQ1HmFA720ye1VQl6T6Qm',  '0', '0',  'admin', sysdate(), '', null, '测试员');


-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员', 'admin',  1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into sys_role values('2', '普通角色',   'common', 2, 2, '0', '0', 'admin', sysdate(), '', null, '普通角色');

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-gear',           'admin', sysdate(), '', null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', '#',                '',          'M', '0', '1', '', 'fa fa-video-camera',   'admin', sysdate(), '', null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', '#',                '',          'M', '0', '1', '', 'fa fa-bars',           'admin', sysdate(), '', null, '系统工具目录');

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1', '1');
insert into sys_user_role values ('2', '2', '2');

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu values ('1', '1', '1');
insert into sys_role_menu values ('2', '1', '2');
insert into sys_role_menu values ('3', '2', '1');
insert into sys_role_menu values ('4', '2', '2');
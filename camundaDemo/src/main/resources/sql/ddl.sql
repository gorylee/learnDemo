create table sys_user (
                          id           			bigint(0)       not null auto_increment    comment '用户ID',
                          user_name         varchar(30)     default ''                 comment '用户昵称',
                          user_role         varchar(2)      default '0'                comment '用户权限：1公司，2事业部，3集团',
                          PRIMARY KEY (`id`) USING BTREE
) engine=innodb auto_increment=10000 comment = '用户信息表';


CREATE TABLE `fi_expense`  (
                               `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `approval_status` smallint(0) NOT NULL DEFAULT 0 COMMENT '审核状态（0待提交,1审核中,2审核通过,3驳回）',
                               `amount` decimal(20, 2) NOT NULL COMMENT '申请金额',
                               `approval_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
                               `approval_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '审核人id',
                               `approval_name` varchar(50) NOT NULL DEFAULT '' COMMENT '审核人名称',
                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '提交时间',
                               `creator_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '提交人id',
                               `creator_name` varchar(50)  NOT NULL DEFAULT '' COMMENT '提交人名称',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB auto_increment=10000 comment = '费用申请表';
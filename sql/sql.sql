CREATE TABLE `image_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `image_name` varchar(128) NOT NULL DEFAULT '' COMMENT '图片名称',
  `image_label` varchar(32) DEFAULT NULL COMMENT '图片类型',
  `md5` varchar(48) DEFAULT '' COMMENT 'md5值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `img_idx1` (`md5`),
  KEY `img_idx2` (`image_label`)
) ENGINE=InnoDB AUTO_INCREMENT=24723 DEFAULT CHARSET=utf8;

CREATE TABLE `img_user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '加密用户名',
  `passwd` varchar(256) DEFAULT NULL COMMENT 'SHA256密码',
  `user_role` tinyint(1) DEFAULT NULL COMMENT '1:管理 2:查看',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ua_idx1` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14723 DEFAULT CHARSET=utf8;

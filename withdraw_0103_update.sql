ALTER TABLE c_collection_personal_business_details_extension ADD PCH VARCHAR(20) DEFAULT NULL COMMENT '批次号';
ALTER TABLE c_housing_business_details_extension ADD PCH VARCHAR(20) DEFAULT NULL COMMENT '批次号';

INSERT INTO `c_lsh` VALUES ('GRPCH', 210000000000000, '个人业务流水批次号', 0);

DROP TABLE IF EXISTS `c_collection_withdrawl_business_extension`;
CREATE TABLE `c_collection_withdrawl_business_extension` (
  `QTTQBZ` varchar(255) DEFAULT NULL COMMENT '其他提取备注',
  `SKYHZHHM` varchar(120) DEFAULT NULL COMMENT '收款银行账户号码',
  `SKYHHM` varchar(120) DEFAULT NULL COMMENT '收款银行户名',
  `SKYHMC` varchar(120) DEFAULT NULL COMMENT '收款银行名称',
  `details` varchar(32) NOT NULL COMMENT '个人业务明细',
  `id` varchar(32) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TRIGGER `create_grpch` BEFORE INSERT ON `c_collection_personal_business_details_extension` FOR EACH ROW BEGIN
IF ISNULL(NEW.PCH) THEN
CALL GET_GRPCH(@pch);
SET NEW.PCH=@pch;
END IF;
END;

-- ----------------------------
-- Procedure structure for `GET_GRPCH`
-- ----------------------------
DROP PROCEDURE IF EXISTS `GET_GRPCH`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GET_GRPCH`(OUT GRPCH varchar(15))
BEGIN
	#Routine body goes here...
	SET @old=(SELECT `no` FROM c_lsh WHERE businuess='GRPCH' FOR UPDATE);
	SET @today = DATE_FORMAT(NOW(),'%y%m%d');
	IF (@old = '99999999999999999999' OR SUBSTRING(@old,3,6) <> @today) THEN
		SET @num = "0000001";
	ELSE
		SET @num = LPAD(RIGHT(@old,7)+1, 7, 0);
	END IF;
	SET @LSH= CONCAT('21',@today,@num);
	UPDATE c_lsh SET `no`=@LSH,`count`=count+1 WHERE businuess='GRPCH';
	SELECT @LSH INTO GRPCH;
END
;;
DELIMITER ;
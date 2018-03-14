

DROP TRIGGER IF EXISTS create_dwzh;
CREATE TRIGGER create_dwzh BEFORE INSERT ON st_collection_unit_account FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),"%y%m%d");
SET @last = (SELECT MAX(DWZH) FROM st_collection_unit_account WHERE DWZH LIKE CONCAT('98',@today,'%'));
IF @last IS NULL THEN
	SET @num = "00001";
ELSE
	SET @num = LPAD(RIGHT(@last,5)+1, 5, 0);
END IF;
SET @dwzh=CONCAT("98",@today,@num);
SET NEW.DWZH=@dwzh;
END;


DROP TRIGGER IF EXISTS create_grzh;
CREATE TRIGGER create_grzh BEFORE INSERT ON st_collection_personal_account FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(GRZH) FROM st_collection_personal_account WHERE GRZH LIKE CONCAT('99',@today,'%'));
IF @last IS NULL THEN
   SET @num = "00001";
ELSE
   SET @num = LPAD(RIGHT(@last,5)+1, 5, 0);
END IF;
SET @grzh=CONCAT("99",@today,@num);
SET NEW.GRZH=@grzh;
END


DROP TRIGGER IF EXISTS create_grywlsh;
CREATE TRIGGER create_grywlsh BEFORE INSERT ON st_collection_personal_business_details FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(YWLSH) FROM st_collection_personal_business_details WHERE YWLSH LIKE CONCAT('01',@today,'%'));
IF @last IS NULL THEN
	SET @num = "0000001";
ELSE
	SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT("01",@today,@num);
SET NEW.YWLSH=@ywlsh;
END;


DROP TRIGGER IF EXISTS create_dwywlsh;
CREATE TRIGGER create_dwywlsh BEFORE INSERT ON st_collection_unit_business_details FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(YWLSH) FROM st_collection_unit_business_details WHERE YWLSH LIKE CONCAT('02',@today,'%'));
IF @last IS NULL THEN
	SET @num = "0000001";
ELSE
	SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT("02",@today,@num);
SET NEW.YWLSH=@ywlsh;
END;

-- 贷款业务流水号
DROP TRIGGER IF EXISTS create_dkywlsh;
CREATE TRIGGER create_dkywlsh BEFORE INSERT ON c_loan_housing_business_process FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(YWLSH) FROM c_loan_housing_business_process WHERE YWLSH LIKE CONCAT('05',@today,'%'));
IF @last IS NULL THEN
	SET @num = "0000001";
ELSE
	SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT("05",@today,@num);
SET NEW.YWLSH=@ywlsh;
END;

-- 贷款账号
DROP TRIGGER IF EXISTS create_dkzh;
CREATE TRIGGER create_dkzh BEFORE INSERT ON st_housing_personal_account FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(DKZH) FROM st_housing_personal_account WHERE DKZH LIKE CONCAT('80',@today,'%'));
IF @last IS NULL THEN
	SET @num = "00001";
ELSE
	SET @num = LPAD(RIGHT(@last,5)+1, 5, 0);
END IF;
SET @dkzh=CONCAT("80",@today,@num);
SET NEW.DKZH=@dkzh;
END;

-- 借款合同编号
DROP TRIGGER IF EXISTS create_dkjkhtbh;
CREATE TRIGGER create_dkjkhtbh BEFORE INSERT ON st_housing_personal_loan FOR EACH ROW
BEGIN
IF ISNULL(NEW.JKHTBH) THEN
	SET @today = DATE_FORMAT(NOW(),'%y%m%d');
	SET @last = (SELECT MAX(JKHTBH) FROM st_housing_personal_loan WHERE JKHTBH LIKE CONCAT('90',@today,'%'));
	IF @last IS NULL THEN
		SET @num = "00001";
	ELSE
		SET @num = LPAD(RIGHT(@last,5)+1, 5, 0);
	END IF;
	SET @jkhtbh=CONCAT('90',@today,@num);
	SET NEW.JKHTBH=@jkhtbh;
END IF;
END;

-- 房开商账号
DROP TRIGGER IF EXISTS create_fkgszh;
CREATE TRIGGER create_fkgszh BEFORE INSERT ON c_loan_housing_company_basic FOR EACH ROW
BEGIN
SET @last = (SELECT MAX(FKGSZH) FROM c_loan_housing_company_basic);
SET @last= IFNULL(@last,"4000000");
SET @num = (SELECT LPAD(RIGHT(@last,6)+1, 6, 0));
SET @zhanghao=(SELECT CONCAT("4",@num));
SET NEW.FKGSZH=@zhanghao;
END;

-- 员工账号
DROP TRIGGER IF EXISTS create_zhanghao;
CREATE TRIGGER create_zhanghao BEFORE INSERT ON c_account_employee FOR EACH ROW
BEGIN
SET @last = (SELECT MAX(ZhangHao) FROM c_account_employee);
SET @last= IFNULL(@last,"0");
-- SET @num = (SELECT LPAD(RIGHT(@last,6)+1, 6, 0));
-- SET @zhanghao=(SELECT CONCAT("3",@num));
SET @zhanghao=@last+1;
SET NEW.ZhangHao=@zhanghao;
END;

-- 楼盘编号
DROP TRIGGER IF EXISTS create_lpbh;
CREATE TRIGGER create_lpbh BEFORE INSERT ON c_loan_eatate_project_basic FOR EACH ROW
BEGIN
SET @last = (SELECT MAX(LPBH) FROM c_loan_eatate_project_basic);
SET @today = DATE_FORMAT(NOW(),'%y%m');
SET @last_date = (SELECT SUBSTRING(@last, 1, 4));
IF (SELECT @last_date IS NULL OR @last_date != @today) THEN
	SET @num = "0001";
ELSE
	SET @num = (SELECT LPAD(RIGHT(@last,4)+1, 4, 0));
END IF;
SET @lpbh=(SELECT CONCAT(DATE_FORMAT(NOW(),'%y%m'),@num));
SET NEW.LPBH=@lpbh;
END;

-- 财务业务日常业务处理流水号
DROP TRIGGER IF EXISTS create_cwywlsh;
CREATE TRIGGER create_cwywlsh BEFORE INSERT ON c_finance_business_process FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');	
SET @last = (SELECT MAX(YWLSH) FROM c_finance_business_process WHERE YWLSH LIKE CONCAT('08',@today,'%'));
IF @last IS NULL THEN
	SET @num = "0000001";
ELSE
	SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT('08',@today,@num);
SET NEW.YWLSH=@ywlsh;
END;

-- 财务业务活期转定期流水号
DROP TRIGGER IF EXISTS create_actived_2_fixed_ywlsh;
CREATE TRIGGER create_actived_2_fixed_ywlsh BEFORE INSERT ON c_finance_actived_2_fixed FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(YWLSH) FROM c_finance_actived_2_fixed WHERE YWLSH LIKE CONCAT('09',@today,'%'));
IF @last IS NULL THEN
SET @num = "0000001";
ELSE
SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT("09",@today,@num);
SET NEW.ywlsh=@ywlsh;
END

-- 财务业务定期支取流水号
DROP TRIGGER IF EXISTS create_fixed_draw_ywlsh;
CREATE TRIGGER create_fixed_draw_ywlsh BEFORE INSERT ON c_finance_fixed_draw FOR EACH ROW
BEGIN
SET @today = DATE_FORMAT(NOW(),'%y%m%d');
SET @last = (SELECT MAX(YWLSH) FROM c_finance_fixed_draw WHERE YWLSH LIKE CONCAT('10',@today,'%'));
IF @last IS NULL THEN
  SET @num = "0000001";
ELSE
  SET @num = LPAD(RIGHT(@last,7)+1, 7, 0);
END IF;
SET @ywlsh=CONCAT('10',@today,@num);
SET NEW.ywlsh=@ywlsh;
END

-- 财务业务凭证模板编号
DROP TRIGGER IF EXISTS create_dkmbbh;
CREATE TRIGGER create_dkmbbh BEFORE INSERT ON c_finance_business_voucher_sets FOR EACH ROW
BEGIN
SET @last = (SELECT MAX(MBBH) FROM c_finance_business_voucher_sets);
SET @last= IFNULL(@last,"000");
SET @mbbh = (SELECT LPAD(RIGHT(@last,3)+1, 3, 0));
SET NEW.MBBH=@mbbh;
END;

-- 财务业务凭证号
DROP TRIGGER IF EXISTS create_cwywpzh;
CREATE TRIGGER create_cwywpzh BEFORE INSERT ON st_finance_recording_voucher FOR EACH ROW
BEGIN
IF ISNULL(NEW.JZPZH) THEN
	SET @last = (SELECT MAX(JZPZH) FROM st_finance_recording_voucher);
	SET @today = DATE_FORMAT(NOW(),'%y%m');
	SET @last_date = (SELECT SUBSTRING(@last, 1, 4));
	IF (SELECT @last_date IS NULL OR @last_date != @today) THEN
		SET @num = "000001";
	ELSE
		SET @num = (SELECT LPAD(RIGHT(@last,4)+1, 6, 0));
	END IF;
	SET @ywpzh=(SELECT CONCAT(DATE_FORMAT(NOW(),'%y%m'),@num));
	SET NEW.JZPZH=@ywpzh;
END IF;
END;

-- 生成资金变动通知号
DROP TRIGGER IF EXISTS generate_notice_no;
create trigger generate_notice_no
             before INSERT on c_bank_acc_change_notice
             for each row
BEGIN
		DECLARE dt CHAR(6);
		DECLARE bh_id CHAR(20);
		DECLARE number INT;
		DECLARE new_bh VARCHAR(20);

		SET dt = DATE_FORMAT(CURDATE(),'%y%m%d');

		SELECT MAX(notice_no) into bh_id
		FROM c_bank_acc_change_notice
		WHERE notice_no LIKE CONCAT('52240', dt, '%');

		IF bh_id = '' OR bh_id IS NULL THEN
			SET new_bh = CONCAT('52240', dt, '000000001');
		ELSE
			SET number = RIGHT(bh_id, 9) + 1;
			SET new_bh = RIGHT(CONCAT('000000000', number), 9);
			SET new_bh = CONCAT('52240', dt, new_bh);
		END IF;

		SET NEW.notice_no = new_bh;
	END;
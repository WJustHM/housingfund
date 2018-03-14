**c_account_employee trigger:**

`BEGIN
SET @last = (SELECT MAX(ZhangHao) FROM c_account_employee);
SET @last= IFNULL(@last,"3000000");
SET @num = (SELECT LPAD(RIGHT(@last,6)+1, 6, 0));
SET @zhanghao=(SELECT CONCAT("3",@num));
SET NEW.ZhangHao=@zhanghao;
END`

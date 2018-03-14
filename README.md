# 本项目为毕节市公积金后端项目，项目的代码架构视图为
## 1.　housingfund　作为项目的根目录，作为父项目，囊括所有的子项目
## 2.　housingfund-collection　为公积金归集项目业务模块
## 3.　housingfund-common 为整个公积金项目的通用模块，包含RPC实现，数据库工具实现等
## 4.　housingfund-bank　为公积金的结算平台接口模块
## 5.　housingfund-bank-server　为公积金的结算平台到账通知模块
## 6.　housingfund-loan　为公积金项目的贷款业务模块
## 7.  housingfund-server　为公积金项目的API服务模块
## 8.　housingfund-withdrawls　为公积金项目的提取业务模块
## 9.  housingfund-account 公积金项目权限管理
## 10.  housingfund-logger 　公积金项目日志管理
## 11. housingfund-database 公积金数据库层
## 12. housingfund-others 其他模块
## 13. housingfund-ca-service 为深圳CA中心提供的webservice接口模块
## 14  housingfund-statemachine 为状态机实现
## 15  housingfund-task 定时任务模块实现


#　将项目根的setting.xml文件拷贝到  ~/.m2/    目录下　　　

git修改.gitignore文件，不生效解决步骤，依次在工程目录下执行下面命令：

```
git rm -r -f --cached .

git add .

git commit -m 'update .gitignore'
```
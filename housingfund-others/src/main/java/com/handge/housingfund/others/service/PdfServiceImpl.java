package com.handge.housingfund.others.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.enumeration.PersonCertificateType;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.model.unit.HeadUnitAcctActionRes;
import com.handge.housingfund.common.service.collection.model.unit.HeadUnitAcctBasicRes;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.ReceiptReturn;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.Record;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlRecordsReturn;
import com.handge.housingfund.common.service.finance.model.VoucherManager;
import com.handge.housingfund.common.service.finance.model.VoucherMangerList;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.loan.enums.LoanGuaranteeType;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.*;
import com.handge.housingfund.common.service.others.model.*;
import com.handge.housingfund.common.service.others.model.File;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.enums.DutiesType;
import com.handge.housingfund.others.webservice.utils.SealHelper;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/**
 * Created by tanyi on 2017/8/9.
 */
@SuppressWarnings("Convert2Lambda")
@Component
public class PdfServiceImpl implements IPdfService {

    @Autowired
    private IFileService fileService;
    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private IDistrictService districtService;
    @Autowired
    private IPdfServiceCa iPdfServiceCa;


    static Configuration config = Configure.getInstance().getConfiguration("pdf");
    String BASEPATH = config.getString("file_path");
    String TEMPLATE = BASEPATH + config.getString("pdf_template") + "/";
    String REVIEWOUTPATH = config.getString("review_out_path");// 审批表输出路径
    String TMPPATH = config.getString("tmp_path");// 临时文件路径
    // String reviewTempPathIn = config.getString("review_temp_in_path");//
    // 审批表模板路径
    // String reviewTempPathOut = config.getString("review_temp_out_path");//
    // 审批表输出路径
    // String contractTempPathIn = config.getString("contract_temp_in_path");//
    // 合同模板路径
    // String contractTempPathOut =
    // config.getString("contract_temp_out_path");// 合同输出路径

    BaseFont bfChinese;

    AcroFields form = null;
    PdfStamper stamper = null;
    RedisCache redisCache = RedisCache.getRedisCacheInstance();
    public PdfServiceImpl() {
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成审批表
     *
     * @param response
     * @return
     */
    public String getReviewTable(GetApplicantResponse response) {
        if (response == null) {
            return null;
        }

        String infile = TEMPLATE + "spb.pdf";
        String outfilename = "review_" + response.getYWLSH();

        FileOutputStream outputStream;

        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(REVIEWOUTPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);

        file = fileService.getFileMetadata(id);

        String outfile = BASEPATH + REVIEWOUTPATH + "/" + id;

        try {

            /**
             * 第1页
             */

            // 借款人信息综合信息
            GetApplicantResponseApplicantInformation jkr = response.getApplicantInformation();

            if (jkr == null) {
                return null;
            }

            // 借款人基本信息
            GetApplicantResponseApplicantInformationBorrowerInformation jkrggj = jkr.getBorrowerInformation();

            if (jkrggj == null) {
                return null;
            }
            // 借款人公积金信息
            GetApplicantResponseApplicantInformationAccountInformation jkrzh = jkr.getAccountInformation();

            // 借款人单位信息
            GetApplicantResponseApplicantInformationUnitInformation jkrdwxx = jkr.getUnitInformation();

            if (jkrdwxx == null) {
                return null;
            }

            // 资金信息
            GetApplicantResponseCapitalInformation zjxx = response.getCapitalInformation();

            if (zjxx == null) {
                return null;
            }

            // 担保信息
            GetApplicantResponseCollateralInformation dbxx = response.getCollateralInformation();

            if (dbxx == null) {
                return null;
            }

            // 共同借款人
            GetApplicantResponseCommonBorrowerInformation gtjkr = response.getCommonBorrowerInformation();

            outputStream = new FileOutputStream(outfile);
            PdfReader reader = new PdfReader(infile);
            stamper = new PdfStamper(reader, outputStream);
            form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            if (gtjkr != null){

                form.setField("GTJKSQR", gtjkr.getGTJKRXM());// 共同借款申请人
                form.setField("SFZHM1", gtjkr.getGTJKRZJHM());// 共同借款人身份证号码
                // 共同借款人单位信息
                GetApplicantResponseApplicantInformationUnitInformation gtjkrdwxx = gtjkr.getUnitInformation() == null ? new GetApplicantResponseApplicantInformationUnitInformation() : gtjkr.getUnitInformation();

                // 共同借款人公积金信息
                GetApplicantResponseApplicantInformationAccountInformation gtjkrzh = gtjkr.getAccountInformation() == null ? new GetApplicantResponseApplicantInformationAccountInformation() : gtjkr.getAccountInformation();

                form.setField("GJJZH3", gtjkr.getGTJKRGJJZH());// 公积金账号
                if (gtjkr.getJCD() != null) {
                    CommonDistrict districtInfo = districtService.getDistrictName(gtjkr.getJCD(), null);
                    form.setField("JCD3", districtInfo != null ? districtInfo.getName() : "");// 缴存地
                }
                form.setField("XingMing3", gtjkr.getGTJKRXM());// 姓名
                if (gtjkr.getGTJKRZJLX() != null) {
                    SingleDictionaryDetail GTJKRZJLX = iDictionaryService.getSingleDetail(gtjkr.getGTJKRZJLX(), "PersonalCertificate");
                    form.setField("ZJLX3", GTJKRZJLX != null ? GTJKRZJLX.getName() : "");// 证件类型
                }
                form.setField("ZJHM3", gtjkr.getGTJKRZJHM());// 证件号码
                form.setField("YSR3", gtjkr.getYSR());// 月收入
                form.setField("SJHM3", gtjkr.getSJHM());// 手机号码
                form.setField("GDDHHM3", gtjkr.getGDDHHM());// 固定电话号码
                form.setField("HKSZD3", gtjkr.getHKSZD());// 户口所在地

                if (gtjkr != null && StringUtil.notEmpty(gtjkr.getGTJKRGJJZH())) {
                    form.setField("JZNY3", gtjkrzh.getJZNY());// 缴至年月
                    form.setField("LXZCJCYS3", gtjkrzh.getLXZCJCYS());// 连续正常缴存月数
                    form.setField("YJCE3", gtjkrzh.getYJCE());// 月缴存额
                    form.setField("JRJCJS3", gtjkrzh.getGRJCJS());// 个人缴存基数
                    form.setField("GRZHYE3", gtjkrzh.getGRZHYE());// 个人账户余额
                    if (gtjkrzh.getGRZHZT() != null) {
                        SingleDictionaryDetail GRZHZT3 = iDictionaryService.getSingleDetail(gtjkrzh.getGRZHZT(), "PersonalAccountState");
                        form.setField("GRZHZT3", GRZHZT3 != null ? GRZHZT3.getName() : "");// 个人账户状态
                    }
                }
                form.setField("DWMC3", gtjkrdwxx.getDWMC());// 单位名称
                form.setField("DWZH3", gtjkrdwxx.getDWZH());// 单位账号
                form.setField("DWDH3", gtjkrdwxx.getDWDH());// 单位电话
                if (gtjkrdwxx.getDWXZ() != null) {
                    SingleDictionaryDetail DWXZ3 = iDictionaryService.getSingleDetail(gtjkrdwxx.getDWXZ(), "UnitClass");
                    form.setField("DWXZ3", DWXZ3 != null ? DWXZ3.getName() : "");// 单位性质
                }
                form.setField("DWDZ3", gtjkrdwxx.getDWDZ());// 单位地址

            }

            form.setField("BianHao", response.getYWLSH());// 编号
            form.setField("JKSQR", jkrggj.getJKRXM());// 借款人姓名
            form.setField("SFZHM", jkrggj.getJKRZJHM());// 身份证号码

            form.setField("GZDW", jkrdwxx.getDWMC());// 工作单位
            form.setField("JTZZ", jkrggj.getJTZZ());// 家庭住址
            form.setField("LXDH", jkrggj.getSJHM());// 联系电话
            form.setField("SQRQ", response.getSQSJ());// 申请日期

            form.setField("JKHTBH", null);// 合同编号
            /**
             * 第2页
             */
            if (jkr.getJCD() != null) {
                CommonDistrict districtInfo = districtService.getDistrictName(jkr.getJCD(), null);
                form.setField("JCD", districtInfo != null ? districtInfo.getName() : "");// 缴存地
            }

            form.setField("GJJZH", jkr.getJKRGJJZH());// 公积金账号
            form.setField("XingMing", jkrggj.getJKRXM());// 姓名
            if (jkrggj.getXingBie() != null) {
                SingleDictionaryDetail XingBie = iDictionaryService.getSingleDetail(jkrggj.getXingBie(), "Gender");
                form.setField("XingBie", XingBie != null ? XingBie.getName() : "");// 性别
            }
            if (jkrggj.getJKRZJLX() != null) {
                SingleDictionaryDetail GTJKRZJLX = iDictionaryService.getSingleDetail(jkrggj.getJKRZJLX(), "PersonalCertificate");
                form.setField("ZJLX", GTJKRZJLX != null ? GTJKRZJLX.getName() : "");// 证件类型
            }
            form.setField("ZJHM", jkrggj.getJKRZJHM());// 证件号码
            form.setField("CSNY", jkrggj.getCSNY());// 出生年月
            if (jkrggj.getXueLi() != null) {
                SingleDictionaryDetail XueLi = iDictionaryService.getSingleDetail(jkrggj.getXueLi(), "Educational");
                form.setField("XueLi", XueLi != null ? XueLi.getName() : "");// 学历
            }
            form.setField("Nianling", jkrggj.getNianLing());// 年龄
            if (jkrggj.getJKZK() != null) {
                SingleDictionaryDetail JKZK = iDictionaryService.getSingleDetail(jkrggj.getJKZK(), "LoanHealthStatus");
                form.setField("JKZK", JKZK != null ? JKZK.getName() : "");// 健康状况
            }
            if (jkrggj.getHYZK() != null) {
                SingleDictionaryDetail HYZK = iDictionaryService.getSingleDetail(jkrggj.getHYZK(), "MaritalStatus");
                form.setField("HYZK", HYZK != null ? HYZK.getName() : "");// 婚姻状况
            }
            if (jkrggj.getZhiWu() != null) {
                String ZhiWu = DutiesType.getName(jkrggj.getZhiWu());
                form.setField("ZhiWu", ZhiWu);// 职务
            }
            if (jkrggj.getZhiCheng() != null) {
                SingleDictionaryDetail ZhiCheng = iDictionaryService.getSingleDetail(jkrggj.getZhiCheng(), "TechnicalTitle");
                form.setField("ZhiCheng", ZhiCheng != null ? ZhiCheng.getName() : "");// 职称
            }
            if (jkrggj.getYGXZ() != null) {
                SingleDictionaryDetail YGXZ = iDictionaryService.getSingleDetail(jkrggj.getYGXZ(), "LoanEmploymentNature");
                form.setField("YGXZ", YGXZ != null ? YGXZ.getName() : "");// 用工性质
            }
            if (jkrggj.getZYJJLY() != null) {
                SingleDictionaryDetail ZYJJLY = iDictionaryService.getSingleDetail(jkrggj.getZYJJLY(), "LoanEconomicSources");
                form.setField("ZYJJLY", ZYJJLY != null ? ZYJJLY.getName() : "");// 主要经济来源
            }
            form.setField("YSR", jkrggj.getYSR());// 月收入
            form.setField("JTYSR", jkrggj.getJTYSR());// 家庭月收入
            form.setField("GDDHHM", jkrggj.getGDDHHM());// 固定电话号码
            form.setField("SJHM", jkrggj.getSJHM());// 手机号码
            form.setField("JTZZ2", jkrggj.getJTZZ());// 家庭住址
            form.setField("HKSZD", jkrggj.getHKSZD());// 户口所在地

            if (jkrzh != null) {
                form.setField("JZNY", jkrzh.getJZNY());// 缴至年月
                form.setField("LXZCJCYS", jkrzh.getLXZCJCYS());// 连续正常缴存月数
                form.setField("YJCE", jkrzh.getYJCE());// 月缴存额
                form.setField("JRJCJS", jkrzh.getGRJCJS());// 个人缴存基数
                form.setField("GRZHYE", jkrzh.getGRZHYE());// 个人账户余额
                if (jkrzh.getGRZHZT() != null) {
                    SingleDictionaryDetail GRZHZT = iDictionaryService.getSingleDetail(jkrzh.getGRZHZT(), "PersonalAccountState");
                    form.setField("GRZHZT", GRZHZT != null ? GRZHZT.getName() : "");// 个人账户状态
                }
            }

            String SFZ = jkr.getSCZL();

            List<UFile> SFZFILE = UploadImagesUtil.getFileByJson(SFZ);
            if (SFZFILE != null && SFZFILE.size() == 4) {
                addImage("SFZZM2_img", SFZFILE.get(0).getData()[0], true);// 借款人证件正
                if (SFZFILE.get(0).getData().length > 1) {
                    addImage("SFZBM2_img", SFZFILE.get(0).getData()[1], true);// 借款人证件反
                }
                if (SFZFILE.get(1).getData().length > 0) {
                    addImage("YDDKHKB_IMG_3", SFZFILE.get(1).getData()[0], true);// 借款人户口本
                }
                if (SFZFILE.get(2).getData().length > 0) {
                    addImage("YDDKJCZM_IMG_4", SFZFILE.get(2).getData()[0], true);// 异地贷款缴存证明
                }
                if (SFZFILE.get(3).getData().length > 0) {
                    addImage("JKRRMYHZX_IMG_5", SFZFILE.get(3).getData()[0], true);// 征信信息
                }
            }
            /**
             * 第3页
             */

            form.setField("DWMC", jkrdwxx.getDWMC());// 单位名称
            form.setField("DWZH", jkrdwxx.getDWZH());// 单位账号
            form.setField("DWDH", jkrdwxx.getDWDH());// 单位电话
            if (jkrdwxx.getDWXZ() != null) {
                SingleDictionaryDetail DWXZ = iDictionaryService.getSingleDetail(jkrdwxx.getDWXZ(), "UnitClass");
                form.setField("DWXZ", DWXZ != null ? DWXZ.getName() : "");// 单位性质
            }
            form.setField("DWDZ", jkrdwxx.getDWDZ());// 单位地址

            /**
             * 第4页
             */

            String GTJKRZL = gtjkr.getSCZL();

            List<UFile> GTJKRZLFILE = UploadImagesUtil.getFileByJson(GTJKRZL);

            if (GTJKRZLFILE != null && GTJKRZLFILE.size() == 5) {

                if (GTJKRZLFILE.get(0).getData().length > 0) {
                    addImage("JHZHDSSM_img", GTJKRZLFILE.get(0).getData()[0], false);// 结婚证0
                }
                if (GTJKRZLFILE.get(1).getData().length > 0) {
                    addImage("GTJKRZJZM_img", GTJKRZLFILE.get(1).getData()[0], true);// 共同借款人身份证正面1
                }

                if (GTJKRZLFILE.get(1).getData().length > 1) {
                    addImage("GTJKRZJFM_img", GTJKRZLFILE.get(1).getData()[1], true);// 共同借款人身份证反面2
                }

                /**
                 * 第5页
                 */
                if (GTJKRZLFILE.get(2).getData().length > 0) {
                    addImage("YDDKHKB5_img", GTJKRZLFILE.get(2).getData()[0], false);// 异地贷款户口本3
                }
                /**
                 * 第6页
                 */
                if (GTJKRZLFILE.get(3).getData().length > 0) {
                    addImage("YDDKJCZM6_img", GTJKRZLFILE.get(3).getData()[0], false);// 异地贷款缴存证明4
                }

                /**
                 * 第7页
                 */
                if (GTJKRZLFILE.get(4).getData().length > 0) {
                    addImage("RMYHXZ7_img", GTJKRZLFILE.get(4).getData()[0], false);// 借款人和共同借款人人民银行征信5
                }

            }
            /**
             * 第8页
             */

            // 房屋信息
            GetApplicantResponseHouseInformation fwxx = response.getHouseInformation();

            if (fwxx == null) {
                return null;
            }

            // 购买商品房信息
            GetApplicantResponseHouseInformationPurchaseFirstInformation spf = fwxx.getPurchaseFirstInformation();
            if (fwxx.getDKYT() != null && fwxx.getDKYT().equals("0") && fwxx.getSFWESF().equals("0")) {
                SingleDictionaryDetail DKYT8 = iDictionaryService.getSingleDetail(fwxx.getDKYT(), "LoanPurpose");
                form.setField("DKYT8", DKYT8 != null ? DKYT8.getName() : "");// 贷款用途
            }
            form.setField("LPMC8", spf.getLPMC());// 楼盘名称
            form.setField("DanJia8", spf.getDanJia());// 单价
            form.setField("HTJE8", spf.getHTJE());// 合同金额
            form.setField("FWZJ8", spf.getFWZJ());// 房屋总价
            form.setField("GFHTBH8", spf.getGFHTBH());// 购房合同编号
            form.setField("FWJZMJ8", spf.getFWJZMJ());// 房屋建筑面积
            form.setField("FWTNMJ8", spf.getFWTNMJ());// 房屋套内面积
            form.setField("SFK8", spf.getSFK());// 首付款
            if (spf.getFWXZ() != null) {
                SingleDictionaryDetail FWXZ8 = iDictionaryService.getSingleDetail(spf.getFWXZ(), "StateOfHouse");
                form.setField("FWXZ8", FWXZ8 != null ? FWXZ8.getName() : "");// 房屋性质
            }
            if (spf.getFWXS() != null) {
                SingleDictionaryDetail FWXS8 = iDictionaryService.getSingleDetail(spf.getFWXS(), "LoanHouseShape");
                form.setField("FWXS8", FWXS8 != null ? FWXS8.getName() : "");// 房屋形式
            }
            if (spf.getFWJG() != null) {
                SingleDictionaryDetail FWJG8 = iDictionaryService.getSingleDetail(spf.getFWJG(), "LoanHouseStructure");
                form.setField("FWJG8", FWJG8 != null ? FWJG8.getName() : "");// 房屋结构
            }
            form.setField("FWJGRQ8", spf.getFWJGRQ());// 房屋竣工日期
            form.setField("FWZL8", spf.getFWZL());// 房屋坐落
            form.setField("SFRMC8", spf.getSFRMC());// 售房人名称
            form.setField("LXFS8", spf.getLXFS());// 联系方式
            form.setField("SPFYSXKZBH8", spf.getSPFYSXKBH());// 商品房预售许可证编号
            form.setField("SFRZHHM8", spf.getSFRZHHM());// 售房人账户号码
            form.setField("SFRKHYHMC8", spf.getSFRKHYHMC());// 售房人开户银行名称
            form.setField("SFRYHKHM8", spf.getSFRYHKHM());// 售房人银行开户名

            String FWXXZL = spf.getSCZL();

            List<UFile> FWXXZLFILE = UploadImagesUtil.getFileByJson(FWXXZL);
            if (FWXXZLFILE != null && FWXXZLFILE.size() == 4) {

                if (FWXXZLFILE.get(0).getData().length > 0) {
                    addImage("SFKSJ8_img", FWXXZLFILE.get(0).getData()[0], true);// 首付款收据0
                }

                /**
                 * 第9页
                 */
                if (FWXXZLFILE.get(1).getData().length > 0) {
                    addImage("SPFYSXKZ9_img", FWXXZLFILE.get(1).getData()[0], false);// 商品房预售许可1
                }

                /**
                 * 第10页
                 */
                if (FWXXZLFILE.get(2).getData().length > 0) {
                    addImage("SFDWZHZM10_img", FWXXZLFILE.get(2).getData()[0], false);// 单位账户证明扫描2
                }

                /**
                 * 第11页
                 */
                if (FWXXZLFILE.get(3).getData().length > 0) {
                    addImage("GFHTGJY11_img", FWXXZLFILE.get(3).getData()[0], false);// 购房合同关键页3
                }

            }
            /**
             * 第12页
             */
            // 购买二手房信息
            GetApplicantResponseHouseInformationPurchaseSecondInformation esf = fwxx.getPurchaseSecondInformation();
            if ("1".equals(fwxx.getSFWESF())) {
                if (fwxx.getDKYT() != null && fwxx.getDKYT().equals("0") && fwxx.getSFWESF().equals("1")) {
                    SingleDictionaryDetail DKYT12 = iDictionaryService.getSingleDetail(fwxx.getDKYT(), "LoanPurpose");
                    form.setField("DKYT12", DKYT12 != null ? DKYT12.getName() : "");// 贷款用途
                }
                form.setField("DanJia12", esf.getDanJia());// 单价
                form.setField("HTJE12", esf.getHTJE());// 合同金额
                form.setField("FWZJ12", esf.getFWZJ());// 房屋总价
                form.setField("GFHTBH12", esf.getGFHTBH());// 购房合同编号
                form.setField("FWJZMJ12", esf.getFWJZMJ());// 房屋建筑面积
                form.setField("FWTNMJ12", esf.getFWTNMJ());// 房屋套内面积
                form.setField("YFK12", esf.getSFK());// 已付款

                if (esf.getFWXZ() != null) {
                    SingleDictionaryDetail FWXZ12 = iDictionaryService.getSingleDetail(esf.getFWXZ(), "StateOfHouse");
                    form.setField("FWXZ12", FWXZ12 != null ? FWXZ12.getName() : "");// 房屋性质
                }
                if (esf.getFWXS() != null) {
                    SingleDictionaryDetail FWXS12 = iDictionaryService.getSingleDetail(esf.getFWXS(), "LoanHouseShape");
                    form.setField("FWXS12", FWXS12 != null ? FWXS12.getName() : "");// 房屋形式
                }
                if (esf.getFWJG() != null) {
                    SingleDictionaryDetail FWJG12 = iDictionaryService.getSingleDetail(esf.getFWJG(), "LoanHouseStructure");
                    form.setField("FWJG12", FWJG12 != null ? FWJG12.getName() : "");// 房屋结构
                }
                form.setField("FWJGRQ12", esf.getFWJGRQ());// 房屋竣工日期
                form.setField("FWZL12", esf.getFWZL());// 房屋坐落
                form.setField("SFRMC12", esf.getSFRMC());// 售房人名称
                form.setField("LXFS12", esf.getLXFS());// 联系方式
                form.setField("GRSKYHZH12", esf.getGRSKYHZH());// 个人收款银行账号
                form.setField("KHYHMC12", esf.getKHYHMC());// 开户银行名称
                form.setField("YHKHM12", esf.getYHKHM());// 银行开户名

                String ESFZL = esf.getSCZL();

                List<UFile> ESFZLFILE = UploadImagesUtil.getFileByJson(ESFZL);

                if (ESFZLFILE != null && ESFZLFILE.size() == 5) {
                    if (ESFZLFILE.get(2).getData().length > 0) {
                        addImage("JKRYHZHZM12_img", ESFZLFILE.get(2).getData()[0], true);// 借款人银行账户证明
                    }

                    /**
                     * 第13页
                     */
                    if (ESFZLFILE.get(0).getData().length > 0) {
                        addImage("QKFKSWFP13_img", ESFZLFILE.get(0).getData()[0], false);// 全额付款税务发票
                    }

                    /**
                     * 第14页
                     */
                    if (ESFZLFILE.get(3).getData().length > 0) {
                        addImage("QSSJ14_img", ESFZLFILE.get(3).getData()[0], false);// 契税发票
                    }

                    /**
                     * 第15页
                     */
                    if (ESFZLFILE.get(1).getData().length > 0) {
                        addImage("GHHDBDCZ15_img", ESFZLFILE.get(1).getData()[0], false);// 过户后的不动产证
                    }

                    /**
                     * 第16页
                     */
                    if (ESFZLFILE.get(4).getData().length > 0) {
                        addImage("FWMMHTGJY16_img", ESFZLFILE.get(4).getData()[0], false);// 购房合同或协议关键页
                    }

                }

            }

            /**
             * 第17页
             */

            // 自建房屋信息
            GetApplicantResponseHouseInformationBuildInformation zjf = fwxx.getBuildInformation();
            if (fwxx.getDKYT() != null && fwxx.getDKYT().equals("1")) {
                SingleDictionaryDetail DKYT17 = iDictionaryService.getSingleDetail(fwxx.getDKYT(), "LoanPurpose");
                form.setField("DKYT17", DKYT17 != null ? DKYT17.getName() : "");// 贷款用途
            }
            form.setField("PZJGWH17", zjf.getPZJGWH());// 批准机关文号
            form.setField("JHJZFY17", zjf.getJHJZFY());// 计划建造费用
            form.setField("JZCS17", zjf.getJZCS());// 建造层数
            form.setField("JHKGRQ17", zjf.getJHKGRQ());// 计划开工日期
            form.setField("JHJGRQ17", zjf.getJHKGRQ());// 计划竣工日期
            form.setField("TDSYZH17", zjf.getTDSYZH());// 土地使用证号
            form.setField("JSGCGHXKZH17", zjf.getJZGCGHXKZH());// 建设工程规划许可证号
            form.setField("JSYDGHXKZH17", zjf.getJZYDGHXKZH());// 建设用地规划许可证号
            form.setField("JZGCSGXKZH17", zjf.getJSGCSGXKZH());// 建筑工程施工许可证号
            form.setField("GRSYZJJE17", zjf.getGRSYZJ());// 个人使用资金金额
            form.setField("FWZL17", zjf.getFWZLDZ());// 房屋坐落
            form.setField("JCHJZMJ17", zjf.getJCHJZMJ());// 建成后建筑面积
            if (spf.getFWXZ() != null && fwxx.getDKYT().equals("1")) {
                SingleDictionaryDetail FWXZ17 = iDictionaryService.getSingleDetail(spf.getFWXZ(), "StateOfHouse");
                form.setField("FWXZ17", FWXZ17 != null ? FWXZ17.getName() : "");// 房屋性质
            }
            if (spf.getFWJG() != null && fwxx.getDKYT().equals("1")) {
                SingleDictionaryDetail FWJG17 = iDictionaryService.getSingleDetail(spf.getFWJG(), "LoanHouseStructure");
                form.setField("FWJG17", FWJG17 != null ? FWJG17.getName() : "");// 房屋结构
            }
            form.setField("GRSKYHZH17", zjf.getGRSKYHZH());// 个人收款账号
            form.setField("KHYHMC17", zjf.getKHHYHMC());// 开户银行名称
            form.setField("YHKHM17", zjf.getYHKHM());// 银行开户名

            String ZJFZL = zjf.getSCZL();

            List<UFile> ZJFZLFILE = UploadImagesUtil.getFileByJson(ZJFZL);

            if (ZJFZLFILE != null && ZJFZLFILE.size() == 4) {
                if (ZJFZLFILE.get(0).getData().length > 0) {
                    addImage("JKRYHZHZM17_img", ZJFZLFILE.get(0).getData()[0], true);// 借款人银行账户证明0
                }

                /**
                 * 第18页
                 */
                if (ZJFZLFILE.get(1).getData().length > 0) {
                    addImage("TDSYZ18_img", ZJFZLFILE.get(1).getData()[0], false);// 全额付款税务发票1
                }

                /**
                 * 第19页
                 */
                if (ZJFZLFILE.get(2).getData().length > 0) {
                    addImage("ZJZ19_img", ZJFZLFILE.get(2).getData()[0], false);// 准建证2
                }

                /**
                 * 第20页
                 */
                if (ZJFZLFILE.get(3).getData().length > 0) {
                    addImage("GCYS20_img", ZJFZLFILE.get(3).getData()[0], false);// 工程预算3
                }

            }
            /**
             * 第21页
             */

            // 房屋大修信息
            GetApplicantResponseHouseInformationOverhaulInformation dxxx = fwxx.getOverhaulInformation();
            if (fwxx.getDKYT().equals("2")) {
                if (fwxx.getDKYT() != null) {
                    SingleDictionaryDetail DKYT21 = iDictionaryService.getSingleDetail(fwxx.getDKYT(), "LoanPurpose");
                    form.setField("DKYT21", DKYT21 != null ? DKYT21.getName() : "");// 贷款用途
                }
                form.setField("YBDCZH21", dxxx.getYBDCZH());// 原不动产证号
                form.setField("YJZMJ21", dxxx.getYJZMJ());// 原建筑面积
                form.setField("TDSYZH21", dxxx.getFXHCS());// 土地使用证号
                form.setField("DXGCYS21", dxxx.getDXGCYS());// 大修工程预算
                form.setField("JHKGRQ21", dxxx.getJHKGRQ());
                form.setField("JHJGRQ21", dxxx.getJHKGRQ());// 计划开工、竣工日期
                form.setField("FWZJBGJGMCJBH21", dxxx.getFWZJBGJGMCJBH());// 房屋质检报告
                form.setField("FWZLDZ21", dxxx.getFWZL());// 房屋坐落地址
                form.setField("GRSKYHZH21", dxxx.getGRSKYHZH());// 个人收款账号
                form.setField("KHYHMC21", dxxx.getKHYHMC());// 开户银行名称
                form.setField("YHKHM21", dxxx.getYHKHM());// 银行开户名

                String FWDXZL = dxxx.getSCZL();

                List<UFile> FWDXZLFILE = UploadImagesUtil.getFileByJson(FWDXZL);

                if (FWDXZLFILE != null && FWDXZLFILE.size() == 4) {
                    if (FWDXZLFILE.get(3).getData().length > 0) {
                        addImage("JKRYHZHZM21_img", FWDXZLFILE.get(3).getData()[0], true);// 借款人银行账户证明0
                    }

                    /**
                     * 第22页
                     */
                    if (FWDXZLFILE.get(0).getData().length > 0) {
                        addImage("YBDCZ22_img", FWDXZLFILE.get(0).getData()[0], false);// 原不动产证1
                    }
                    if (FWDXZLFILE.get(1).getData().length > 0) {
                        addImage("FWZJBG23_img", FWDXZLFILE.get(1).getData()[0], false);// 房屋质检报告5
                    }

                    /**
                     * 第23页
                     */
//                    if (FWDXZLFILE.get(5).getData().length > 0) {
//                        addImage("TDSYZ24_img", FWDXZLFILE.get(5).getData()[0], false);// 土地使用证2
//                    }

                    /**
                     * 第24页
                     */
//                    if (FWDXZLFILE.get(4).getData().length > 0) {
//                        addImage("ZJZ25_img", FWDXZLFILE.get(4).getData()[0], false);// 准建证3
//                    }

                    /**
                     * 第25页
                     */
                    if (FWDXZLFILE.get(2).getData().length > 0) {
                        addImage("GCYS26_img", FWDXZLFILE.get(2).getData()[0], false);//工程预算
                    }

                }
            }

            /**
             * 第26页
             */

            if (zjxx.getDKLX() != null) {
                SingleDictionaryDetail DKLX27 = iDictionaryService.getSingleDetail(zjxx.getDKLX(), "LoanType");
                form.setField("DKLX27", DKLX27 != null ? DKLX27.getName() : "");// 贷款类型
            }
            if (dbxx.getDKDBLX() != null) {
                SingleDictionaryDetail DKDBLX27 = iDictionaryService.getSingleDetail(dbxx.getDKDBLX(), "LoanGuaranteeType");
                form.setField("DKDBLX27", DKDBLX27 != null ? DKDBLX27.getName() : "");// 贷款担保类型
            }
            if (zjxx.getHKFS() != null) {
                SingleDictionaryDetail HKFS27 = iDictionaryService.getSingleDetail(zjxx.getHKFS(), "LoanPaymentMode");
                form.setField("HKFS27", HKFS27 != null ? HKFS27.getName() : "");// 还款方式
            }
            form.setField("HTDKJE26", zjxx.getHTDKJE());// 合同贷款金额
            form.setField("HTDKJEDX27", zjxx.getHTDKJEDX());// 合同贷款金额大写
            form.setField("DKQS27", zjxx.getDKQS());// 贷款期数

            if (zjxx.getFWTS() != null) {
                SingleDictionaryDetail FWTS = iDictionaryService.getSingleDetail(zjxx.getFWTS(), "LoanHouseNumber");
                form.setField("FWTS27", FWTS != null ? FWTS.getName() : "");// 房屋套数
            }
            form.setField("JKHTLL27", zjxx.getJKHTLL());// 借款合同利率
            form.setField("LLFDBL27", zjxx.getLLFSBL());// 利率浮动比例
            BigDecimal JKHTLL = new BigDecimal(zjxx.getJKHTLL());
            BigDecimal LLFSBL = new BigDecimal(zjxx.getLLFSBL());
            BigDecimal ZXLL = null;
            ZXLL = JKHTLL.multiply(LLFSBL).setScale(2);
            form.setField("ZXLL27", ZXLL.toString());// 执行利率
            form.setField("SYDKQS27", "");// 商业贷款利率
            form.setField("SYDKJE27", "");// 商业贷款金额
            form.setField("SYDKYH27", "");// 商业贷款银行
            form.setField("SYDKYHKE27", "");// 商业贷款月还款额

            // 抵押物列表
            ArrayList<GetApplicantResponseCollateralInformationMortgageInformation> dywlist = dbxx
                    .getMortgageInformation();

            if (dywlist != null && dywlist.size() > 0) {

                GetApplicantResponseCollateralInformationMortgageInformation dyw = dywlist.get(0);

                if (dbxx.getDKDBLX() != null && dbxx.getDKDBLX().equals("01")) {
                    SingleDictionaryDetail DKDBLX27 = iDictionaryService.getSingleDetail(dbxx.getDKDBLX(), "LoanGuaranteeType");
                    form.setField("DKDBLX27", DKDBLX27 != null ? DKDBLX27.getName() : "");// 贷款担保类型
                }
                form.setField("DYWSYQRXM27", dyw.getDYWSYQRXM());// 抵押物所有权人姓名
                form.setField("DYWSYQRSFZHM27", dyw.getDYWSYQRSFZHM());// 抵押物所有权人身份证号码
                form.setField("DYWSYQRLXDH27", dyw.getDYWSYQRLXDH());// 抵押物所有权人联系电话
                form.setField("DYWGYQRXM27", dyw.getDYWGYQRXM());// 抵押物共有权人姓名
                form.setField("DYWGYQRSFZHM27", dyw.getDYWGYQRSFZHM());// 抵押物共有权人身份证号码
                form.setField("DYWGYQRLXDH27", dyw.getDYWGYQRLXDH());// 抵押物共有权人联系电话
                form.setField("DYWMC27", dyw.getDYWMC());// 抵押物名称
                form.setField("QSZSBH27", dyw.getQSZSBH());// 权属证书编号
                form.setField("DYWPGJZ27", dyw.getDYWPGJZ());// 抵押物评估价值
                if (dyw.getDYFWXS() != null) {
                    SingleDictionaryDetail DYFWXS27 = iDictionaryService.getSingleDetail(dyw.getDYFWXS(), "LoanHouseShape");
                    form.setField("DYFWXS27", DYFWXS27 != null ? DYFWXS27.getName() : "");// 抵押房屋形式
                }
                form.setField("FWMJ27", dyw.getFWMJ());// 房屋面积
                if (dyw.getFWJG() != null) {
                    SingleDictionaryDetail FWJG26 = iDictionaryService.getSingleDetail(dyw.getFWJG(), "LoanHouseStructure");
                    form.setField("FWJG28", FWJG26 != null ? FWJG26.getName() : "");// 房屋结构
                }
                form.setField("DYWFWZL28", dyw.getDYWFWZL());// 抵押物房屋坐落
            }

            /**
             * 第27页
             */
            // 质押物列表
            ArrayList<GetApplicantResponseCollateralInformationPledgeInformation> zywlist = dbxx.getPledgeInformation();

            if (zywlist != null && zywlist.size() > 0) {

                GetApplicantResponseCollateralInformationPledgeInformation zyw = zywlist.get(0);
                if (dbxx.getDKDBLX() != null && dbxx.getDKDBLX().equals("02")) {
                    SingleDictionaryDetail DKDBLX28 = iDictionaryService.getSingleDetail(dbxx.getDKDBLX(), "LoanGuaranteeType");
                    form.setField("DKDBLX28", DKDBLX28 != null ? DKDBLX28.getName() : "");// 贷款担保类型
                }
                form.setField("ZYWSYQRXM28", zyw.getZYWSYQRXM());// 质押物所有权人姓名
                form.setField("ZYWSYQRSFZHM28", zyw.getZYWSYQRSFZHM());// 质押物所有权人身份证号码
                form.setField("ZYWSYQRLXDH28", zyw.getZYWSYQRLXDH());// 质押物所有权人联系电话
                form.setField("ZYWMC28", zyw.getZYWMC());// 质押物名称
                form.setField("ZYWJZ28", zyw.getZYWJZ());// 质押_质押物价值
            }

            // 担保信息
            ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation> dbxxlist = dbxx
                    .getGuaranteeInformation();

            if (dbxxlist != null && dbxxlist.size() > 0) {
                GetApplicantResponseCollateralInformationGuaranteeInformation dbx = dbxxlist.get(0);

                boolean isgr = dbx.getBZFLX().equals("0");
                if (dbxx.getDKDBLX() != null && dbxx.getDKDBLX().equals("03")) {
                    SingleDictionaryDetail DKDBLX28 = iDictionaryService.getSingleDetail(dbxx.getDKDBLX(), "LoanGuaranteeType");
                    form.setField("DKDBLX28", DKDBLX28 != null ? DKDBLX28.getName() : "");// 贷款担保类型
                }
                if (dbx.getBZFLX() != null) {
                    SingleDictionaryDetail BZFLX28 = iDictionaryService.getSingleDetail(dbx.getBZFLX(), "LoanGuaranteedType");
                    form.setField("BZFLX28", BZFLX28 != null ? BZFLX28.getName() : "");// 保证方类型
                }
                form.setField("BZRXM28", isgr ? dbx.getBZRXM() : "");// 个人_保证人姓名
                form.setField("BZRSFZHM28", isgr ? dbx.getBZRSFZHM() : "");// 保证人身份证号码
                form.setField("BZRLXDH28", isgr ? dbx.getBZRLXDH() : "");// 个人_保证人联系电话
                form.setField("BZRXJZDZ28", isgr ? dbx.getBZRXJZDZ() : "");// 保证人现居住地址
                form.setField("TXDZ28", isgr ? dbx.getTXDZ() : "");// 通讯地址
                form.setField("YZBM28", isgr ? dbx.getYZBM() : "");// 邮政编码
                form.setField("FRDBXM28", !isgr ? dbx.getBZRXM() : "");// 机构_法人代表姓名
                form.setField("FRDBSFZHM28", !isgr ? dbx.getBZRSFZHM() : "");// 机构_法人代表身份证号码
                form.setField("FRDBLXDH28", !isgr ? dbx.getBZRLXDH() : "");// 机构_法人代表联系电话
                form.setField("FRDBXJZDZ28", !isgr ? dbx.getBZRXJZDZ() : "");// 机构_法人代表现居住地址

                /**
                 * 第28页
                 */

                form.setField("TXDZ29", !isgr ? dbx.getTXDZ() : "");// 个人_通讯地址
                form.setField("YZBM29", !isgr ? dbx.getYZBM() : "");// 个人_邮政编码

            }

            String DBRZL = dbxx.getSCZL();

            List<UFile> DBRZLFILE = UploadImagesUtil.getFileByJson(DBRZL);

            if (DBRZLFILE != null && DBRZLFILE.size() == 6) {
                if (DBRZLFILE.get(4).getData().length > 0) {
                    addImage("HYZMHDSSM29_img", DBRZLFILE.get(4).getData()[0], true);// 婚姻关系证明或单身声明
                }

                if (DBRZLFILE.get(5).getData().length > 0) {
                    addImage("DYRZJZM29_img", DBRZLFILE.get(5).getData()[0], true);// 抵押人证件正面
                }

                if (DBRZLFILE.get(5).getData().length > 1) {
                    addImage("DYRZJFM29_img", DBRZLFILE.get(5).getData()[1], true);// 抵押人证件反面
                }


                /**
                 * 第29页
                 */
                if (DBRZLFILE.get(0).getData().length > 0) {
                    addImage("QFBDCDJDYYG30_img", DBRZLFILE.get(0).getData()[0], false);// 期房不动产登记抵押0
                }

                /**
                 * 第30页
                 */
                if (DBRZLFILE.get(1).getData().length > 0) {
                    addImage("DYZYWZM31_img", DBRZLFILE.get(1).getData()[0], false);// 抵押质押物证明1
                }

                /**
                 * 第31页
                 */
                if (DBRZLFILE.get(2).getData().length > 0) {
                    addImage("DYZYWJZPG32_img", DBRZLFILE.get(2).getData()[0], false);// 抵押质押物价值评估2
                }

                /**
                 * 第32页
                 */
                if (DBRZLFILE.get(3).getData().length > 0) {
                    addImage("SQRQZ33_img", DBRZLFILE.get(3).getData()[0], false);// 申请人签字3
                }

            }
            /**
             * 第33页
             */

            String QTZL = response.getSCZL();

            List<UFile> QTZLFILE = UploadImagesUtil.getFileByJson(QTZL);

            if (QTZLFILE != null && QTZLFILE.size() == 2) {
                if (QTZLFILE.get(0).getData().length > 0) {
                    addImage("QTZL34_img", QTZLFILE.get(0).getData()[0], false);// 其他资料0
                }
            }

            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();
            outputStream.close();

            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /**
     * 审批action
     *
     * @param id      原审批表文件ID
     * @param reviews 审核记录
     * @return
     */
    public String putReviewTable(String id, List<Review> reviews) {
        if (reviews == null || reviews.size() <= 0) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "审核内容不能为空");
        }
        File oldFile = fileService.getFileMetadata(id);
        String table = oldFile.getId();
        String infile = TEMPLATE + "review.pdf";
        String oldfile = BASEPATH + oldFile.getPath() + "/" + table;
        String tempfile = BASEPATH + TMPPATH + "/" + table + "temp.pdf";
        String newfilename = oldFile.getName() + "_" + reviews.get(reviews.size() - 1).getType();
        PdfReader reader;
        FileOutputStream outputStream;

        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(newfilename);
        file.setPath(REVIEWOUTPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String resid = fileService.saveFile(file);
        file = fileService.getFileMetadata(resid);

        String newfile = BASEPATH + REVIEWOUTPATH + "/" + file.getId();

        try {
            outputStream = new FileOutputStream(tempfile);
            reader = new PdfReader(infile);
            stamper = new PdfStamper(reader, outputStream);
            form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            java.io.File oldf = new java.io.File(oldfile);

            InputStream input = new FileInputStream(String.valueOf(oldf));

            PdfReader oldreader = new PdfReader(input);
            form.setField("page", String.valueOf(oldreader.getNumberOfPages() - 1));// 页码
            oldreader.close();

            for (Review review : reviews) {
                switch (review.getType()) {
                    case 0:
                        form.setField("CaoZuo", review.getAction());// 操作
                        form.setField("CZYJZW", review.getCZY());// 操作员及职务
                        form.setField("YWWD", review.getYWWD());// 业务网点
                        form.setField("CZQD", review.getCZQD());// 操作渠道
                        form.setField("SLRYJ", review.getSLRYJ());// 受理人意见
                        form.setField("SPSJ", review.getSPSJ());// 审批时间
                        // addImage("SLR", review.getSLR(), true);
                        break;
                    case 1:
//                        form.getField("CaoZuo");
//                        form.setField("CaoZuo1", review.getAction());// 操作
                        form.setField("CZYJZW1", review.getCZY());// 操作员及职务
                        form.setField("YWWD1", review.getYWWD());// 业务网点
                        form.setField("CZQD1", review.getCZQD());// 操作渠道
                        form.setField("YJSPRYJ1", review.getSLRYJ());// 一级审批人
                        form.setField("SPSJ1", review.getSPSJ());// 审批时间
                        // addImage("YJSPR1", review.getSLR(), true);
                        break;
                    case 2:
//                        form.setField("CaoZuo2", review.getAction());// 操作
                        form.setField("CZYJZW2", review.getCZY());// 操作员及职务
                        form.setField("YWWD2", review.getYWWD());// 业务网点
                        form.setField("CZQD2", review.getCZQD());// 操作渠道
                        form.setField("EJSPRYJ", review.getSLRYJ());// 二级审批人
                        form.setField("SPSJ2", review.getSPSJ());// 审批时间
                        // addImage("EJSPR", review.getSLR(), true);
                        break;
                    case 3:
//                        form.setField("CaoZuo3", review.getAction());// 操作
                        form.setField("CZYJZW3", review.getCZY());// 操作员及职务
                        form.setField("YWWD3", review.getYWWD());// 业务网点
                        form.setField("CZQD3", review.getCZQD());// 操作渠道
                        form.setField("SJSPRYJ", review.getSLRYJ());// 三级审批人
                        form.setField("SPSJ3", review.getSPSJ());// 审批时间
                        // addImage("SJSPR", review.getSLR(), true);
                        break;
                    default:
                        break;
                }
            }
            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();
            outputStream.close();
            mergePdfFiles(new String[]{oldfile, tempfile}, newfile);
            FileUtil.delete(tempfile);

            file.setSHA1(FileUtil.getCheckCode(newfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(newfile));

            fileService.updateFile(file);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            FileUtil.delete(newfile);
            throw new ErrorException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            FileUtil.delete(newfile);
            throw new ErrorException(e);
        }
        return resid;
    }

    /**
     * 添加图片
     *
     * @param field 域名称
     * @param imgid 图片文件ID
     * @param isFit 是否按域大小缩放
     */
    private void addImage(String field, String imgid, boolean isFit) {
        if (imgid == null) {
            return;
        }
        imgid = BASEPATH + config.getString("image") + "/" + imgid;
        try {
            Image jpeg = Image.getInstance(imgid);
            int pageNo = form.getFieldPositions(field).get(0).page;
            Rectangle signRect = form.getFieldPositions(field).get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            PdfContentByte under = stamper.getOverContent(pageNo);
            jpeg.scaleToFit(signRect.getWidth(), signRect.getHeight());
            jpeg.scaleAbsoluteWidth(signRect.getWidth());
            if (isFit) {
                jpeg.scaleAbsoluteHeight(signRect.getHeight());
                jpeg.setAbsolutePosition(x, y);
            } else {
                jpeg.setAbsolutePosition(x,
                        y + signRect.getHeight() - (signRect.getWidth() * jpeg.getHeight()) / jpeg.getWidth());
            }
            under.addImage(jpeg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void createCheckbox(PdfStamper stamper, float lowerLeftX, float lowerLeftY, float upperRightX, float upperRightY, String fieldName, boolean startChecked,boolean OnTrue) throws IOException, DocumentException {
        RadioCheckField bt = new RadioCheckField(stamper.getWriter(), new Rectangle(lowerLeftX, lowerLeftY, upperRightX, upperRightY), fieldName, "On");
        bt.setCheckType(RadioCheckField.TYPE_CHECK);
        if(!OnTrue){
            bt.setChecked(false);
        }else{
            bt.setChecked(startChecked);
        }
        bt.setBorderColor(BaseColor.BLACK);
        bt.setBackgroundColor(BaseColor.WHITE);
        bt.setOptions(RadioCheckField.READ_ONLY);
        PdfFormField ck = bt.getCheckField();
        PdfFormField radioForm=PdfFormField.createRadioButton(stamper.getWriter(), true);
        radioForm.addKid(ck);
        stamper.addAnnotation(radioForm,1);
    }
    /**
     * 合并pdf文件
     *
     * @param files    文件路径数组
     * @param savepath 最终文件路径
     */
    private void mergePdfFiles(String[] files, String savepath) {
        try {
            PdfReader r = new PdfReader(files[0]);
            Document document = new Document(r.getPageSize(1));
            FileOutputStream fileOutputStream = new FileOutputStream(savepath);
            PdfCopy copy = new PdfCopy(document, fileOutputStream);
            document.open();

            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    if (i == 0 && j == n) {
                        continue;
                    }
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                reader.close();
            }
            copy.close();
            document.close();
            r.close();
            fileOutputStream.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并pdf多个文件
     *
     * @param files    文件路径数组
     * @param savepath 最终文件路径
     */
    private void mergeMorePdf(String[] files, String savepath) {
        try {
            PdfReader r = new PdfReader(files[0]);
            Document document = new Document(r.getPageSize(1));
            FileOutputStream fileOutputStream = new FileOutputStream(savepath);
            PdfCopy copy = new PdfCopy(document, fileOutputStream);
            document.open();

            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                reader.close();
            }
            copy.close();
            document.close();
            r.close();
            fileOutputStream.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
    // public static void main(String args[]) {
    // PdfServiceImpl service = new PdfServiceImpl();
    // service.gettqjl();
    // }

    /*
     * 设置打印次数
     * */
    public static void setPrintManyTimes(String key){
        RedisCache redisCache = RedisCache.getRedisCacheInstance();
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            if (redis.get(key)==null) {
                redis.setex(key,24*3600,"1");
            } else {
                int m = Integer.parseInt(redis.get(key))+1;
                if(m>1000){
                    redis.setex(key, 24*3600, "0");
                }else{
                    redis.setex(key, 24*3600, String.valueOf(m));
                }
            }
            redis.close();
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
    /*
     * 获取打印次数
     */
    public static String getPrintManyTimes(String key){
        RedisCache redisCache = RedisCache.getRedisCacheInstance();
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            String t = redis.get(key);
            Integer n = Integer.parseInt(t);
            if(n>0&&n<10){
                t = "00"+t;
            }
            if(n>10&&n<100){
                t = "0"+t;
            }
            redis.close();
            return t;
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
        return null;
    }
    public String getUnitPayBackNoticePdf(HeadUnitPayBackNoticeRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "UnitPayBackNoticeTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "UnitPayBackNotice_" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            form.setField("CiShu", "1");
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("YWLSH", result.getYWLSH());
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("HBJNY", result.getBJNY());
            form.setField("BJFS", result.getBJFS());
            form.setField("FSRS", String.valueOf(result.getFSRS()));
            form.setField("FSE", String.valueOf(result.getFSE()));
            form.setField("SKZHMC", result.getSKZHMC());
            form.setField("SKZH", result.getSKZH());
            form.setField("STYHMC", result.getSTYHMC());
            form.setField("STYHDM", result.getSTYHDM());
            form.setField("RiQi", result.getJZSJ());
            form.setField("DWJBR", result.getJBRXM());
            form.setField("CZY", result.getCZY());
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 260, 600, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitPayholdReceiptPdf(HeadUnitPayHoldReceiptRes result) {
        // 模板路径
        String PayholdPathtemplatePath = TEMPLATE + "UnitPayholdReceiptTemp.pdf";
        System.out.println(PayholdPathtemplatePath);
        String outfilename = "UnitPayHoldReceipt" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(PayholdPathtemplatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JZNY", result.getJZNY());
            form.setField("YHJNY", result.getDWYHJNY());
            form.setField("HJNYQ", result.getSQHJKSNY());
            form.setField("HJNYS", result.getSQHJJSNY());
            form.setField("HJYY", result.getHJYY());
            form.setField("JBRXM", result.getJBRXM());
            form.setField("JBRZJHM", result.getJBRZJHM());
            form.setField("JBRQZ", "");
            form.setField("DWJBR", result.getJBRXM());
            form.setField("CZY", result.getCZY());
            form.setField("SHR", result.getSHR());
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 250, 650, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitRemittanceNoticePdf(UnitRemittanceNotice result) {
        // 模板路径
        String UnitRemittancetemplatePath = TEMPLATE + "RemittanceNoticeTemp.pdf";
        System.out.println(UnitRemittancetemplatePath);
        String outfilename = "UnitRemittanceNotice" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(UnitRemittancetemplatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("HBJNY", result.getHBJNY());
            form.setField("YHJNY", result.getHBJNY());
            form.setField("HJFS", result.getHJFS());// 汇缴方式
            form.setField("FSRS", result.getFSRS());// 发生人数
            form.setField("FSE", String.valueOf(result.getFSE()));// 发生额
            form.setField("ZJE", result.getZJE());// 总金额（大写）
            form.setField("SKZHMC", result.getSKZHMC());// 收款账号名称
            form.setField("SKZH", result.getSKZH());// 收款账号
            form.setField("STYHMC", result.getSTYHMC());// 委托银行名称
            form.setField("STYHDM", result.getSTYHDM());// 委托银行代码
            form.setField("SKSJ", result.getJZSJ());// 收款时间
            form.setField("DWJBR", result.getDWJBR());// 单位经办人
            form.setField("CZY", result.getCZY());// 操作员
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 250, 620, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitDepositRatioReceiptPdf(HeadUnitDepositRatioReceiptRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "UnitDepositRatioReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "UnitDepositRatioReceipt" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("TZQDWJCBL", String.valueOf(result.getTZQDWJCBL()));
            form.setField("TZQGRJCBL", String.valueOf(result.getTZQGRJCBL()));
            form.setField("TZHDWJCBL", String.valueOf(result.getTZHDWJCBL()));
            form.setField("TZHGRJCBL", String.valueOf(result.getTZHGRJCBL()));
            form.setField("SXNY", result.getSXNY());
            form.setField("JBRXXM", result.getJBRXM());
            form.setField("JBRZJHM", result.getJBRZJHM());
            form.setField("DWJBR", result.getJBRXM());// 单位经办人
            form.setField("CZY", result.getCZY());
            form.setField("JBRQZ", null);// 经办人签字
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 380, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitAcctSealReceiptPdf(HeadUnitAcctActionRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "UnitAcctSealReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "UnitAcctSealReceipt" + result.getDWGJXX().getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getDWGJXX().getYWLSH());
            form.setField("YWWD", result.getDWGJXX().getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWGJXX().getDWMC());
            form.setField("DWZH", result.getDWGJXX().getDWZH());
            form.setField("JZNY", result.getDWGJXX().getJZNY());
            form.setField("ZZJGDM", result.getDWGJXX().getZZJGDM());
            form.setField("DWFRDBXM", result.getDWGJXX().getDWFRDBXM());
            form.setField("DWFRDBZJHM", result.getDWGJXX().getDWFRDBZJHM());
            form.setField("JBRXM", result.getDWGJXX().getJBRXM());
            form.setField("JBRZJHM", result.getDWGJXX().getJBRZJHM());
            form.setField("JBRSJHM", result.getDWGJXX().getJBRSJHM());
            form.setField("FCYY", result.getDWGJXX().getFCHXHYY());
            form.setField("BeiZhu", result.getDWGJXX().getBeiZhu());
            form.setField("CZY", result.getDWGJXX().getCZY());
            form.setField("DWJBR", result.getDWGJXX().getJBRXM());// 单位经办人
            form.setField("JBRQZ", result.getDWGJXX().getJBRQM());// 经办人签字
            form.setField("SHR", result.getDWGJXX().getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 380, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitAcctUnsealReceiptPdf(HeadUnitAcctActionRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "UnitAcctsUnsealReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "UnitAcctUnsealReceipt" + result.getDWGJXX().getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getDWGJXX().getYWLSH());
            form.setField("YWWD", result.getDWGJXX().getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWGJXX().getDWMC());
            form.setField("DWZH", result.getDWGJXX().getDWZH());
            form.setField("JZNY", result.getDWGJXX().getJZNY());
            form.setField("ZZJGDM", result.getDWGJXX().getZZJGDM());
            form.setField("DWFRDBXM", result.getDWGJXX().getDWFRDBXM());
            form.setField("DWFRDBZJHM", result.getDWGJXX().getDWFRDBZJHM());
            form.setField("JBRXM", result.getDWGJXX().getJBRXM());
            form.setField("JBRZJHM", result.getDWGJXX().getJBRZJHM());
            form.setField("JBRSJHM", result.getDWGJXX().getJBRSJHM());
//			form.setField("QFYY", result.getDWGJXX().getFCHXHYY());
            form.setField("QFYY", result.getDWGJXX().getQTCZYY());
            form.setField("DWJBR", result.getDWGJXX().getJBRXM());// 单位经办人
            form.setField("BeiZhu", result.getDWGJXX().getBeiZhu());
            form.setField("CZY", result.getDWGJXX().getCZY());
            form.setField("JBRQZ", result.getDWGJXX().getJBRQM());// 经办人签字
            form.setField("SHR", result.getDWGJXX().getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 380, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitAcctDropReceiptPdf(HeadUnitAcctActionRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "UnitAcctsDropReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "UnitAcctDropReceipt" + result.getDWGJXX().getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getDWGJXX().getYWLSH());
            form.setField("YWWD", result.getDWGJXX().getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWGJXX().getDWMC());
            form.setField("DWZH", result.getDWGJXX().getDWZH());
            form.setField("JZNY", result.getDWGJXX().getJZNY());
            form.setField("DWLB", result.getDWGJXX().getDWLB());
            form.setField("ZZJGDM", result.getDWGJXX().getZZJGDM());
            form.setField("DWFRDBXM", result.getDWGJXX().getDWFRDBXM());
            form.setField("DWFRDBZJHM", result.getDWGJXX().getDWFRDBZJHM());
            form.setField("JBRXM", result.getDWGJXX().getJBRXM());
            form.setField("JBRZJHM", result.getDWGJXX().getJBRZJHM());
            form.setField("JBRSJHM", result.getDWGJXX().getJBRSJHM());
            form.setField("DWXHYY", result.getDWGJXX().getFCHXHYY());
            form.setField("BeiZhu", result.getDWGJXX().getBeiZhu());// 单位经办人
            form.setField("CZY", result.getDWGJXX().getCZY());
            form.setField("JBRQZ", result.getDWGJXX().getJBRQM());// 经办人签字
            form.setField("DWJBR", result.getDWGJXX().getJBRXM());// 单位经办人
            form.setField("SHR", result.getDWGJXX().getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 380, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getUnitAcctsSetReceiptPdf(HeadUnitAcctBasicRes result, String code) {
        // 模板路径
        String templatePath = null;
        int pdfy = 280;
        if (code.equals("03")) {
            templatePath = TEMPLATE + "UnitAcctsSetReceiptTemp.pdf";
            pdfy = 280;
        } else {
            templatePath = TEMPLATE + "UnitAcctsAlterReceiptTemp.pdf";
            pdfy = 320;
        }
        System.out.println(templatePath);
        String outfilename = "UnitAcctsSetReceipt" + result.getDWGJXX().getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getDWGJXX().getYWLSH());
            form.setField("YWWD", result.getDWGJXX().getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 单位关键信息
            form.setField("DWMC", result.getDWGJXX().getDWMC());
            form.setField("DWZH", result.getDWGJXX().getDWZH());
            form.setField("DWDZ", result.getDWGJXX().getDWDZ());
            form.setField("DWFRDBXM", result.getDWGJXX().getDWFRDBXM());
            form.setField("DWFRDBZJHM", result.getDWGJXX().getDWFRDBZJHM());
            form.setField("DWLB", result.getDWGJXX().getDWLB());
            form.setField("ZZJGDM", result.getDWGJXX().getZZJGDM());
            // 单位联系方式
            form.setField("DWLXDH", result.getDWLXFS().getDWLXDH());
            form.setField("DWDZXX", result.getDWLXFS().getDWDZXX());
            // 单位登记信息
            form.setField("DWSLRQ", result.getDWDJXX().getDWSLRQ());
            form.setField("DWSCHJNY", result.getDWDJXX().getDWSCHJNY());
            form.setField("DWJCBL",
                    result.getDWDJXX().getDWJCBL().equals("null") ? null : result.getDWDJXX().getDWJCBL());
            form.setField("GRJCBL",
                    result.getDWDJXX().getGRJCBL().equals("null") ? null : result.getDWDJXX().getGRJCBL());
            form.setField("DWKHRQ", result.getDWDJXX().getDWSLRQ());// 单位开户日期
            form.setField("BeiZhu", result.getDWDJXX().getBeiZhu());
            // 经办人信息
            form.setField("JBRXM", result.getJBRXX().getJBRXM());
            form.setField("JBRZJHM", result.getJBRXX().getJBRZJHM());
            form.setField("JBRSJHM", result.getJBRXX().getJBRSJHM());
            // 委托银行信息
            form.setField("STYHMC", result.getWTYHXX().getSTYHMC());
            form.setField("STYHDM", result.getWTYHXX().getSTYHDM());
            form.setField("JBRQZ", null);
            form.setField("CZY", result.getDWGJXX().getCZY());
            form.setField("DWJBR", result.getJBRXX().getJBRXM());// 单位经办人
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, pdfy, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getIndiAcctSetReceiptPdf(HeadIndiAcctSetRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "IndiAcctSetReceiptTemp.pdf";
        String outfilename = "IndiAcctsSetReceipt" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        System.out.println(templatePath);
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 个人信息
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("XingMing", result.getXingMing());
            form.setField("XingBie", result.getXingBie());
            form.setField("ZJHM", result.getZJHM());
            form.setField("HYZK", result.getHYZK());
            form.setField("XueLi", result.getXueLi());
            form.setField("SJHM", result.getSJHM());
            form.setField("GDDHHM", result.getGDDHHM());
            form.setField("JTZZ", result.getJTZZ());
            form.setField("YZBM", result.getYZBM());
            form.setField("YouXiang", result.getYouXiang());
            // 个人缴存信息
            form.setField("GRZHZT", result.getGRZHZT());
            form.setField("GRJCJS", String.valueOf(result.getGRJCJS()));
            form.setField("DWJCBL", String.valueOf(result.getDWJCBL()));
            form.setField("GRJCBL", String.valueOf(result.getGRJCBL()));
            form.setField("GRYJCE", String.valueOf(result.getGRYJCE()));
            form.setField("DWYJCE", String.valueOf(result.getDWYJCE()));
            form.setField("YJCE", String.valueOf(result.getYJCE()));
            form.setField("KHRQ", result.getKHRQ());
            form.setField("CKZHHM", result.getGRCKZHHM());// 存款账户号码
            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getDWJBR());// 单位经办人
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 100, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getIndiAcctAlterReceiptPdf(HeadAcctAlterRes result) {
        // 模板路径
        String templatePath = TEMPLATE + "IndiAcctAlterReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "IndiAcctAlterReceipt" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 个人信息
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("XingMing", result.getXingMing());
            form.setField("XingBie", result.getXingBie());
            form.setField("ZJHM", result.getZJHM());
            form.setField("HYZT", result.getHYZK());
            form.setField("XueLi", result.getXueLi());
            form.setField("SJHM", result.getSJHM());
            form.setField("GDDHHM", result.getGDDHHM());
            form.setField("JTDZ", result.getJTZZ());
            form.setField("YZBM", result.getYZBM());
            form.setField("YouXiang", result.getYouXiang());
            // 个人缴存信息
            form.setField("GRZHZT", result.getGRZHZT());
            form.setField("GRJCJS", String.valueOf(result.getGRJCJS()));
            form.setField("DWJCBL", String.valueOf(result.getDWJCBL()));
            form.setField("GRJCBL", String.valueOf(result.getGRJCBL()));
            form.setField("GRYJCE", String.valueOf(result.getGRYJCE()));
            form.setField("DWYJCE", String.valueOf(result.getDWYJCE()));
            form.setField("YJCE", String.valueOf(result.getYJCE()));
            form.setField("KHRQ", result.getKHRQ());
            form.setField("CKZHHM", result.getGRCKZHKHYHDM());// 存款账户号码
            form.setField("GRCKZHHM", result.getGRCKZHHM());

            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getDWJBR());// 单位经办人
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 130, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getIndiAcctActionPdf(HeadIndiAcctActionRes result, String code) {
        String templatePath = null;
        int pdf = 180;
        if (code.equals(CollectionBusinessType.冻结.getCode())) {
            // 模板路径
            templatePath = TEMPLATE + "IndiAcctFreezeReceiptTemp.pdf";
            pdf = 180;
        }
        if (code.equals(CollectionBusinessType.封存.getCode())) {
            // 模板路径
            templatePath = TEMPLATE + "IndiAcctSealReceiptTemp.pdf";
            pdf = 180;
        }
        if (code.equals(CollectionBusinessType.解冻.getCode())) {
            // 模板路径
            templatePath = TEMPLATE + "IndiAcctUnfreezeReceiptTemp.pdf";
            pdf = 160;
        }
        if (code.equals(CollectionBusinessType.启封.getCode())) {
            // 模板路径
            templatePath = TEMPLATE + "IndiAcctUnsealReceiptTemp.pdf";
            pdf = 160;
        }
        System.out.println(templatePath);
        String outfilename = "IndiAcctAction" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 个人账户信息
            form.setField("XingMing", result.getXingMing());
            form.setField("ZJHM", result.getZJHM());
            form.setField("GRZHZT", result.getGRZHZT());
            form.setField("GRJCJS", String.valueOf(result.getGRJCJS()));
            form.setField("GRJCBL", String.valueOf(result.getGRJCBL()));
            form.setField("DWJCBL", String.valueOf(result.getDWJCBL()));
            form.setField("YJCE", String.valueOf(result.getYJCE()));
            form.setField("GRZHYE", String.valueOf(result.getGRZHYE()));
            if (code.equals(CollectionBusinessType.冻结.getCode())) {
                form.setField("DJYY", result.getCZYY());
            }
            if (code.equals(CollectionBusinessType.封存.getCode())) {
                form.setField("FCYY", result.getCZYY());
            }
            if (code.equals(CollectionBusinessType.解冻.getCode())) {
                form.setField("JDYY", result.getCZYY());
            }
            if (code.equals(CollectionBusinessType.启封.getCode())) {
                form.setField("QFYY", result.getCZYY());
            }
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JZNY", result.getJZNY());
            form.setField("BeiZhu", result.getBeiZhu());
            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getDWJBR());// 单位经办人
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, pdf, SealHelper.getContractSeal(), id);
        return id;
    }

    /*
     * 提取回执单
     */
    public String getWithdrawlReceiptPdf(ReceiptReturn result) {
        // 模板路径
        String templatePath = TEMPLATE + "WithdrawlReceiptTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "WithDrawlReceipt" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 个人信息
            form.setField("GRZH", result.getReceiptIndiAcctInfo().getGrzh());
            form.setField("DWMC", result.getReceiptIndiAcctInfo().getDwmc());
            form.setField("XingMing", result.getReceiptIndiAcctInfo().getXingMing());
            form.setField("DWZH", result.getReceiptIndiAcctInfo().getDwzh());
            form.setField("TQYY", result.getReceiptWithdrawlsInfo().getTqyy());
            form.setField("SFXH", result.getReceiptWithdrawlsInfo().getSfxh());
            form.setField("TQJE", result.getReceiptWithdrawlsInfo().getTqje());
            form.setField("FSLXE", result.getReceiptWithdrawlsInfo().getFslxe());
            form.setField("ZongE", result.getReceiptWithdrawlsInfo().getZongE());
            form.setField("DQYE", result.getReceiptWithdrawlsInfo().getDqye());
            form.setField("BLSJ", result.getReceiptWithdrawlsInfo().getBlsj());
            form.setField("XCTQSJ", result.getReceiptWithdrawlsInfo().getXctqrq());
            form.setField("BLR", result.getReceiptWithdrawlsInfo().getBlr());
            form.setField("ZJHM", result.getReceiptWithdrawlsInfo().getZjhm());

            form.setField("SKYH", result.getReceiptWithdrawlsInfo().getSkyh());
            form.setField("SKYHKH", result.getReceiptWithdrawlsInfo().getSkyhkh());
            form.setField("CZY", result.getCZY());
            form.setField("SHR", result.getSHR());// 审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 10, 360, SealHelper.getContractSeal(), id);
        id = iPdfServiceCa.addSignaturePdf(1, 10, 700, SealHelper.getContractSeal(), id);
        return id;
    }

    /*
     * 单位缴存head
     */
    public String getUnitPayWrongPdf(HeadUnitPayWrongReceiptRes result) {
        ArrayList<HeadUnitPayWrongReceiptResGZXX> gzxxArrayList = result.getGZXX();
        Integer count = gzxxArrayList.size();
        String id = null;
        if (count >= 0 && count < 24) {
            id = getPayWrongReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 700, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "PayWrongTableTemp-1.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "PayWrongReceipt_head" + result.getYWLSH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/PayWrongReceipt_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getYWLSH());
                form.setField("YWWD", result.getYWWD());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWMC());
                form.setField("DWZH", result.getDWZH());
                form.setField("JCGZNY", result.getJCGZNY());
                form.setField("SKYHZH", result.getSKYHZH());
                form.setField("SKYHHM", result.getSKYHHM());
                form.setField("SKYHMC", result.getSKYHMC());
                Integer num = 1;
                Iterator<HeadUnitPayWrongReceiptResGZXX> iter = gzxxArrayList.iterator();
                while (iter.hasNext()) {
                    HeadUnitPayWrongReceiptResGZXX headUnitPayWrongReceiptResGZXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("GRZH" + num, headUnitPayWrongReceiptResGZXX.getGRZH());
                    form.setField("XingMing" + num, headUnitPayWrongReceiptResGZXX.getXingMing());
                    form.setField("DWCJJE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getDWCJJE()));
                    form.setField("GRCJJE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getGRCJJE()));
                    form.setField("FSE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getFSE()));

                    form.setField("CJYY", headUnitPayWrongReceiptResGZXX.getCJYY());
                    iter.remove();
                    if (num == 23) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = payWrongyforeachTable(gzxxArrayList, result.getYWLSH());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("CZY", result.getCZY());
            map.put("DWJBR", result.getJBRXM());
            map.put("JBRQZ", null);
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
            int fornum = ((count - 23) / 31);
            Integer fo = ((count - 23) % 31);
            if (fo.equals(0)) {
                fornum = fornum - 1;
            }
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    if (i == strArr.length - 1) {
                        strArr[i] = getPayWronyBottomPage(map, filePdfData.getGzxxArrayList(), fornum);
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 700, SealHelper.getContractSeal(), id);
            if (strArr.length > 1) {
                id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
            }
        }
        return id;
    }

    /*
     * 单位错缴仅一页模板
     */
    public String getPayWrongReceiptOnlyPage(HeadUnitPayWrongReceiptRes result) {
        ArrayList<HeadUnitPayWrongReceiptResGZXX> gzxxArrayList = result.getGZXX();
        Integer count = gzxxArrayList.size();
        String templatePath = TEMPLATE + "PayWrongTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "RemittanceRecipt_one" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);//
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JCGZNY", result.getJCGZNY());
            form.setField("SKYHZH", result.getSKYHZH());
            form.setField("SKYHHM", result.getSKYHHM());
            form.setField("SKYHMC", result.getSKYHMC());
            Integer num = 1;
            for (HeadUnitPayWrongReceiptResGZXX list : gzxxArrayList) {
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("DWCJJE" + num, String.valueOf(list.getDWCJJE()));
                form.setField("GRCJJE" + num, String.valueOf(list.getGRCJJE()));
                form.setField("FSE" + num, String.valueOf(list.getFSE()));
                form.setField("CJYY" + num, list.getCJYY());
                num++;
            }
            form.setField("page", "1"); // 页码
            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getJBRXM());
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 单位错缴body
     */
    public FilePdfData payWrongyforeachTable(ArrayList<HeadUnitPayWrongReceiptResGZXX> gzxxArrayList, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = gzxxArrayList.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "PayWrongTableTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "PayWrongReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/PayWrongReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadUnitPayWrongReceiptResGZXX> iter = gzxxArrayList.iterator();
                while (iter.hasNext()) {
                    int j = 23 + i * 31 + num;
                    HeadUnitPayWrongReceiptResGZXX headUnitPayWrongReceiptResGZXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("GRZH" + num, headUnitPayWrongReceiptResGZXX.getGRZH());
                    form.setField("XingMing" + num, headUnitPayWrongReceiptResGZXX.getXingMing());
                    form.setField("DWCJJE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getDWCJJE()));
                    form.setField("GRCJJE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getGRCJJE()));
                    form.setField("FSE" + num, String.valueOf(headUnitPayWrongReceiptResGZXX.getFSE()));
                    form.setField("CJYY" + num, headUnitPayWrongReceiptResGZXX.getCJYY());
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setGzxxArrayList(gzxxArrayList);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 单位错缴footer
     */
    public String getPayWronyBottomPage(HashMap<String, String> map, ArrayList<HeadUnitPayWrongReceiptResGZXX> gzxxArrayList, int fornum) {
        String templatePath = TEMPLATE + "PayWrongTableTemp-3.pdf";
        System.out.println(templatePath);
        String outfilename = "PayWrongReceipt_footer";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/PayWrongReceipt_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (HeadUnitPayWrongReceiptResGZXX list : gzxxArrayList) {
                int j = 23 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("DWCJJE" + num, String.valueOf(list.getDWCJJE()));
                form.setField("GRCJJE" + num, String.valueOf(list.getGRCJJE()));
                form.setField("FSE" + num, String.valueOf(list.getFSE()));
                form.setField("CJYY" + num, list.getCJYY());
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));// 页码
            form.setField("JBRQZ", map.get("JBRQZ"));
            form.setField("CZY", map.get("CZY"));
            form.setField("DWJBR", map.get("DWJBR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 单位缴存head
     */
    public String getUnitRemittanceReceiptPdf(HeadUnitRemittanceReceiptRes result) {
        ArrayList<HeadUnitRemittanceReceiptResDWHJQC> dqhjxqArrayList = result.getDWHJQC();
        Integer count = dqhjxqArrayList.size();
        String id = null;
        if (count >= 0 && count < 19) {
            id = RemittanceReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 700, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "RemittanceTableTemp-1.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "RemittanceReceipt_head" + result.getYWLSH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/RemittanceReceipt_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getYWLSH());
                form.setField("YWWD", result.getYWWD());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWMC());
                form.setField("DWZH", result.getDWZH());
                form.setField("JZNY", result.getHJNY());// 缴至年月
                form.setField("ZSYE", String.valueOf(result.getZSYE()));
                form.setField("DWZHYE", String.valueOf(result.getDWZHYE()));
                form.setField("QCQRDH", result.getJCXX().getQCQRDH());
                form.setField("HBJNY", result.getJCXX().getHBJNY());
                form.setField("FSRS", String.valueOf(result.getJCXX().getFSRS()));
                form.setField("FSE", String.valueOf(result.getJCXX().getFSE()));
                form.setField("HJFS", result.getJCXX().getHJFS());
                form.setField("STYHMC", result.getJCXX().getSTYHMC());
                form.setField("STYHZH", result.getJCXX().getSTYHZH());
                Integer num = 1;
                Iterator<HeadUnitRemittanceReceiptResDWHJQC> iter = dqhjxqArrayList.iterator();
                while (iter.hasNext()) {
                    HeadUnitRemittanceReceiptResDWHJQC headUnitRemittanceReceiptResDWHJQC = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("GRZH" + num, headUnitRemittanceReceiptResDWHJQC.getGRZH());
                    form.setField("XingMing" + num, headUnitRemittanceReceiptResDWHJQC.getXingMing());
                    form.setField("GRJCJS" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRJCJS()));
                    form.setField("DWYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getDWYJCE()));
                    form.setField("GRYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRYJCE()));
                    form.setField("HeJi" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getHeJi()));
                    iter.remove();
                    if (num == 18) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = RemittanceforeachTable(dqhjxqArrayList, result.getYWLSH());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("DWYJCE32", String.valueOf(result.getYJEZHJ().getDWYJCEZHJ()));
            map.put("GRYJCE32", String.valueOf(result.getYJEZHJ().getGRYJCEZHJ()));
            map.put("HeJi32", String.valueOf(result.getYJEZHJ().getZHJ()));
            map.put("CZY", result.getCZY());
            map.put("DWJBR", result.getJBRXM());
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
            int fornum = ((count - 18) / 31);
            Integer fo = ((count - 18) % 31);
            if (fo.equals(0)) {
                fornum = fornum - 1;
            }
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    if (i == strArr.length - 1) {
                        strArr[i] = getRemittanceBottomPage(map, filePdfData.getDqhjxqArrayList(), fornum);
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 700, SealHelper.getContractSeal(), id);
            if (strArr.length > 1) {
                id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
            }
        }
        return id;
    }

    /*
     * 单位缴存仅一页模板
     */
    public String RemittanceReceiptOnlyPage(HeadUnitRemittanceReceiptRes result) {
        ArrayList<HeadUnitRemittanceReceiptResDWHJQC> dqhjxqArrayList = result.getDWHJQC();
        Integer count = dqhjxqArrayList.size();
        String templatePath = TEMPLATE + "RemittanceTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "RemittanceRecipt_one" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JZNY", result.getHJNY());// 缴至年月
            form.setField("ZSYE", String.valueOf(result.getZSYE()));
            form.setField("DWZHYE", String.valueOf(result.getDWZHYE()));
            form.setField("QCQRDH", result.getJCXX().getQCQRDH());
            form.setField("HBJNY", result.getJCXX().getHBJNY());
            form.setField("FSRS", String.valueOf(result.getJCXX().getFSRS()));
            form.setField("FSE", String.valueOf(result.getJCXX().getFSE()));
            form.setField("HJFS", result.getJCXX().getHJFS());
            form.setField("STYHMC", result.getJCXX().getSTYHMC());
            form.setField("STYHZH", result.getJCXX().getSTYHZH());
            Integer num = 1;
            Iterator<HeadUnitRemittanceReceiptResDWHJQC> iter = dqhjxqArrayList.iterator();
            while (iter.hasNext()) {
                HeadUnitRemittanceReceiptResDWHJQC headUnitRemittanceReceiptResDWHJQC = iter.next();
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("GRZH" + num, headUnitRemittanceReceiptResDWHJQC.getGRZH());
                form.setField("XingMing" + num, headUnitRemittanceReceiptResDWHJQC.getXingMing());
                form.setField("GRJCJS" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRJCJS()));
                form.setField("DWYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getDWYJCE()));
                form.setField("GRYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRYJCE()));
                form.setField("HeJi" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getHeJi()));
                num++;
            }
            form.setField("page", "1");// 页码
            form.setField("HJDWYJCE", String.valueOf(result.getYJEZHJ().getDWYJCEZHJ()));
            form.setField("HJGRYJCE", String.valueOf(result.getYJEZHJ().getGRYJCEZHJ()));
            form.setField("HeJi19", String.valueOf(result.getYJEZHJ().getZHJ()));
            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getJBRXM());
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 单位缴存body
     */
    public FilePdfData RemittanceforeachTable(ArrayList<HeadUnitRemittanceReceiptResDWHJQC> dqhjxqArrayList, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = dqhjxqArrayList.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "RemittanceTableTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "RemittanceReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/RemittanceReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadUnitRemittanceReceiptResDWHJQC> iter = dqhjxqArrayList.iterator();
                while (iter.hasNext()) {
                    int j = 18 + i * 31 + num;
                    HeadUnitRemittanceReceiptResDWHJQC headUnitRemittanceReceiptResDWHJQC = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("GRZH" + num, headUnitRemittanceReceiptResDWHJQC.getGRZH());
                    form.setField("XingMing" + num, headUnitRemittanceReceiptResDWHJQC.getXingMing());
                    form.setField("GRJCJS" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRJCJS()));
                    form.setField("DWYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getDWYJCE()));
                    form.setField("GRYJCE" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getGRYJCE()));
                    form.setField("HeJi" + num, String.valueOf(headUnitRemittanceReceiptResDWHJQC.getHeJi()));
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setDqhjxqArrayList(dqhjxqArrayList);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 单位缴存footer
     */
    public String getRemittanceBottomPage(HashMap<String, String> map, ArrayList<HeadUnitRemittanceReceiptResDWHJQC> hjArrayList, int fornum) {
        String templatePath = TEMPLATE + "RemittanceTableTemp-3.pdf";
        System.out.println(templatePath);
        String outfilename = "RemittanceReceipt_footer";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/RemittanceReceipt_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (HeadUnitRemittanceReceiptResDWHJQC list : hjArrayList) {
                int j = 18 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("GRJCJS" + num, String.valueOf(list.getGRJCJS()));
                form.setField("DWYJCE" + num, String.valueOf(list.getDWYJCE()));
                form.setField("GRYJCE" + num, String.valueOf(list.getGRYJCE()));
                form.setField("HeJi" + num, String.valueOf(list.getHeJi()));
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));
            form.setField("DWYJCE32", map.get("DWYJCE32"));
            form.setField("GRYJCE32", map.get("GRYJCE32"));
            form.setField("HeJi32", map.get("HeJi32"));
            form.setField("CZY", map.get("CZY"));
            form.setField("DWJBR", map.get("DWJBR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 个人基数调整head
     */
    public String getPersonRadixPdf(HeadPersonRadixRes result) {
        ArrayList<HeadPersonRadixResJCJSTZXX> jcjstzxxArrayList = result.getJCJSTZXX();
        Integer count = jcjstzxxArrayList.size();
        String id = null;
        if (count >= 0 && count < 25) {
            id = getPersonRadixReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 100, 10, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "PersonRadixTableTemp-1.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "PersonRadixReceipt_head" + result.getDWGJXX().getYWLSH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/PersonRadixReceipt_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getDWGJXX().getYWLSH());
                form.setField("YWWD", result.getDWGJXX().getYWWD());// 无业务网点
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWGJXX().getDWMC());
                form.setField("DWZH", result.getDWGJXX().getDWZH());
                form.setField("FSRS", result.getDWGJXX().getFSRS());
                form.setField("JBRXM", result.getDWGJXX().getDWJBR());
                form.setField("JBRZJHM", result.getDWGJXX().getJBRZJHM());
                Integer num = 1;
                Iterator<HeadPersonRadixResJCJSTZXX> iter = jcjstzxxArrayList.iterator();
                while (iter.hasNext()) {
                    HeadPersonRadixResJCJSTZXX headPersonRadixResJCJSTZXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("GRZH" + num, headPersonRadixResJCJSTZXX.getGRZH());
                    form.setField("XingMing" + num, headPersonRadixResJCJSTZXX.getXingMing());
                    form.setField("TZQGRJCJS" + num, String.valueOf(headPersonRadixResJCJSTZXX.getTZQJCJS()));
                    form.setField("TZHGRJCJS" + num, String.valueOf(headPersonRadixResJCJSTZXX.getTZHJCJS()));
                    iter.remove();
                    if (num == 24) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码

                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = PersonRadixforeachTable(jcjstzxxArrayList, result.getDWGJXX().getYWLSH());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("CZY", result.getDWGJXX().getCZY());
            map.put("DWJBR", result.getDWGJXX().getDWJBR());
            map.put("SHR", result.getSHR());
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
            int fornum = ((count - 24) / 31);
            Integer fo = ((count - 24) % 31);
            if (fo.equals(0)) {
                fornum = fornum - 1;
            }
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    if (i == strArr.length - 1) {
                        strArr[i] = getPersonRadixBottomPage(map, filePdfData.getJcjstzxxArrayList(), fornum);
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 100, 10, SealHelper.getContractSeal(), id);
            if (strArr.length > 1) {
                id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
            }
        }
        return id;
    }

    /*
     * 个人基数调整仅一页模板
     */
    public String getPersonRadixReceiptOnlyPage(HeadPersonRadixRes result) {
        ArrayList<HeadPersonRadixResJCJSTZXX> jcjstzxxArrayList = result.getJCJSTZXX();
        Integer count = jcjstzxxArrayList.size();
        String templatePath = TEMPLATE + "PersonRadixTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "PersonRadixReceipt_one" + result.getDWGJXX().getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getDWGJXX().getYWLSH());
            form.setField("YWWD", result.getDWGJXX().getYWWD());// 无业务网点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWGJXX().getDWMC());
            form.setField("DWZH", result.getDWGJXX().getDWZH());
            form.setField("FSRS", result.getDWGJXX().getFSRS());
            form.setField("JBRXM", result.getDWGJXX().getDWJBR());
            form.setField("JBRZJHM", result.getDWGJXX().getJBRZJHM());
            Integer num = 1;
            for (HeadPersonRadixResJCJSTZXX list : jcjstzxxArrayList) {
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("TZQGRJCJS" + num, String.valueOf(list.getTZQJCJS()));
                form.setField("TZHGRJCJS" + num, String.valueOf(list.getTZHJCJS()));
                num++;
            }
            form.setField("page", "1");// 页码
            form.setField("CZY", result.getDWGJXX().getCZY());
            form.setField("DWJBR", result.getDWGJXX().getDWJBR());
            form.setField("SHR", result.getSHR());
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 个人基数调整body
     */
    public FilePdfData PersonRadixforeachTable(ArrayList<HeadPersonRadixResJCJSTZXX> jcjstzxxArrayList, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = jcjstzxxArrayList.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "PersonRadixTableTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "PersonRadixReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/PersonRadixReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadPersonRadixResJCJSTZXX> iter = jcjstzxxArrayList.iterator();
                while (iter.hasNext()) {
                    int j = 24 + i * 31 + num;
                    HeadPersonRadixResJCJSTZXX headPersonRadixResJCJSTZXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("GRZH" + num, headPersonRadixResJCJSTZXX.getGRZH());
                    form.setField("XingMing" + num, headPersonRadixResJCJSTZXX.getXingMing());
                    form.setField("TZQGRJCJS" + num, String.valueOf(headPersonRadixResJCJSTZXX.getTZQJCJS()));
                    form.setField("TZHGRJCJS" + num, String.valueOf(headPersonRadixResJCJSTZXX.getTZHJCJS()));
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setJcjstzxxArrayList(jcjstzxxArrayList);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 个人基数调整footer
     */
    public String getPersonRadixBottomPage(HashMap<String, String> map, ArrayList<HeadPersonRadixResJCJSTZXX> jcjstzxxArrayList, int fornum) {
        String templatePath = TEMPLATE + "PersonRadixTableTemp-3.pdf";
        System.out.println(templatePath);
        String outfilename = "PersonRadixReceipt_footer";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/PersonRadixReceipt_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (HeadPersonRadixResJCJSTZXX list : jcjstzxxArrayList) {
                int j = 24 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("TZQGRJCJS" + num, String.valueOf(list.getTZQJCJS()));
                form.setField("TZHGRJCJS" + num, String.valueOf(list.getTZHJCJS()));
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));
            form.setField("CZY", map.get("CZY"));
            form.setField("DWJBR", map.get("DWJBR"));
            form.setField("SHR", map.get("SHR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 单位清册确认head
     */
    public String getRemittanceInventoryReceiptPdf(HeadRemittanceInventoryRes result) {
        ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList = result.getDWHJQC();
        Integer count = dwhjqcArrayList.size();
        String id = null;
        String wybm = result.getDWZH() + result.getQCNY() + System.currentTimeMillis();
        if (count >= 0 && count < 23) {
            id = InventoryReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 680, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "InventoryTableTemp-1.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "InventoryReceipt_head";
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/InventoryReceipt_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();

                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getYWLSH());
                form.setField("YWWD", result.getYWWD());// 业务网点
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWMC());
                form.setField("DWZH", result.getDWZH());
                form.setField("QCNY", result.getQCNY());
                form.setField("FSRS", String.valueOf(result.getFSRS()));
                form.setField("FSE", String.valueOf(result.getFSE()));
                Integer num = 1;
                Iterator<HeadRemittanceInventoryResDWHJQC> iter = dwhjqcArrayList.iterator();
                while (iter.hasNext()) {
                    HeadRemittanceInventoryResDWHJQC headRemittanceInventoryResDWHJQC = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("ZJHM" + num, headRemittanceInventoryResDWHJQC.getZJHM());
                    form.setField("XingMing" + num, headRemittanceInventoryResDWHJQC.getXingMing());
                    form.setField("GRJCJS" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRJCJS()));
                    form.setField("DWYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getDWYJCE()));
                    form.setField("GRYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRYJCE()));
                    form.setField("HeJi" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getHeJi()));
                    iter.remove();
                    if (num == 22) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = InventoryforeachTable(dwhjqcArrayList, result.getYWLSH());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("DWYJCE32", String.valueOf(result.getYJEZHJ().getDWYJCEZHJ()));
            map.put("GRYJCE32", String.valueOf(result.getYJEZHJ().getGRYJCEZHJ()));
            map.put("HeJi32", String.valueOf(result.getYJEZHJ().getZHJ()));
            map.put("CZY", result.getCZY());
            map.put("DWJBR", result.getJBRXM());
//		    String[] strArr = (String[]) filePdfData.getFilePathArray().toArray();
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
            int fornum = ((count - 22) / 31);
            Integer fo = ((count - 22) % 31);
            if (fo.equals(0)) {
                fornum = fornum - 1;
            }
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    if (i == strArr.length - 1) {
                        strArr[i] = getInventoryBottomPage(map, filePdfData.getDwhjqcArrayList(), fornum);
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 300, 680, SealHelper.getContractSeal(), id);
//            if (strArr.length > 1) {
//                id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
//            }
        }
        return id;
    }

    /*
     * 单位清册确认仅一页模板
     */
    public String InventoryReceiptOnlyPage(HeadRemittanceInventoryRes result) {
        ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList = result.getDWHJQC();
        Integer count = dwhjqcArrayList.size();
        String templatePath = TEMPLATE + "InventoryTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "InventoryReceipt_one";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());// 业务网点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("QCNY", result.getQCNY());
            form.setField("FSRS", String.valueOf(result.getFSRS()));
            form.setField("FSE", String.valueOf(result.getFSE()));
            Integer num = 1;
            Iterator<HeadRemittanceInventoryResDWHJQC> iter = dwhjqcArrayList.iterator();
            while (iter.hasNext()) {
                HeadRemittanceInventoryResDWHJQC headRemittanceInventoryResDWHJQC = iter.next();
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("ZJHM" + num, headRemittanceInventoryResDWHJQC.getZJHM());
                form.setField("XingMing" + num, headRemittanceInventoryResDWHJQC.getXingMing());
                form.setField("GRJCJS" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRJCJS()));
                form.setField("DWYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getDWYJCE()));
                form.setField("GRYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRYJCE()));
                form.setField("HeJi" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getHeJi()));
                num++;
            }
            form.setField("page", "1");// 页码
            form.setField("DWYJCEHJ", String.valueOf(result.getYJEZHJ().getDWYJCEZHJ()));
            form.setField("GRYJCEHJ", String.valueOf(result.getYJEZHJ().getGRYJCEZHJ()));
            form.setField("HeJi23", String.valueOf(result.getYJEZHJ().getZHJ()));
            form.setField("CZY", result.getCZY());
            form.setField("DWJBR", result.getJBRXM());
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    public FilePdfData estateforeachTable(ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateDetails, String YWLSH, String code) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = estateIdGetLDXXEstateDetails.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = "";
            String outfilename = "";
            if (code.equals("01")) {
                templatePath = TEMPLATE + "EstateProjectReceipt-2.pdf";
                outfilename = "EstateProjectReceipt_body" + i + YWLSH;
            } else if (code.equals("02")) {
                templatePath = TEMPLATE + "EstateProjectAlterReceipt-2.pdf";
                outfilename = "EstateProjectReceipt_body" + i + YWLSH;
            }
            System.out.println(templatePath);

            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/InventoryReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<EstateIdGetLDXXEstateDetail> iter = estateIdGetLDXXEstateDetails.iterator();
                while (iter.hasNext()) {
                    int j = 18 + i * 31 + num;
                    EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));//楼栋名/号
                    form.setField("LDMH" + num, estateIdGetLDXXEstateDetail.getLDMH());//楼栋名/号
                    form.setField("DKBL" + num, estateIdGetLDXXEstateDetail.getDKBL());//贷款比例
                    form.setField("JGQZ" + num, estateIdGetLDXXEstateDetail.getJGRQ());//竣工日期
                    form.setField("XYRQ" + num, estateIdGetLDXXEstateDetail.getXYRQ());//协议日期
                    form.setField("KSDYH" + num, estateIdGetLDXXEstateDetail.getKSDYH());//开始单元号
                    form.setField("DYS" + num, estateIdGetLDXXEstateDetail.getDYS());//单元数
                    form.setField("BeiZhu" + num, estateIdGetLDXXEstateDetail.getBeiZhu());//备注
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setEstateIdGetLDXXEstateList(estateIdGetLDXXEstateDetails);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 单位清册确认body
     */
    public FilePdfData InventoryforeachTable(ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = dwhjqcArrayList.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "InventoryTableTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "InventoryReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/InventoryReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadRemittanceInventoryResDWHJQC> iter = dwhjqcArrayList.iterator();
                while (iter.hasNext()) {
                    int j = 22 + i * 31 + num;
                    HeadRemittanceInventoryResDWHJQC headRemittanceInventoryResDWHJQC = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("ZJHM" + num, headRemittanceInventoryResDWHJQC.getZJHM());
                    form.setField("XingMing" + num, headRemittanceInventoryResDWHJQC.getXingMing());
                    form.setField("GRJCJS" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRJCJS()));
                    form.setField("DWYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getDWYJCE()));
                    form.setField("GRYJCE" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getGRYJCE()));
                    form.setField("HeJi" + num, String.valueOf(headRemittanceInventoryResDWHJQC.getHeJi()));
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setDwhjqcArrayList(dwhjqcArrayList);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    private String getEstateBottomPage(HashMap<String, String> map, ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateDetails, int fornum, String code) {
        String templatePath = "";
        String outfilename = "";
        if (code.equals("01")) {
            templatePath = TEMPLATE + "EstateProjectReceipt-3.pdf";
            outfilename = "EstateProjectAlterReceipt_footer";
        } else if (code.equals("02")) {
            templatePath = TEMPLATE + "EstateProjectAlterReceipt-3.pdf";
            outfilename = "EstateProjectAlterReceipt_footer";
        }

        System.out.println(templatePath);

        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/EstateProject_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail : estateIdGetLDXXEstateDetails) {
                int j = 18 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("LDMH" + num, estateIdGetLDXXEstateDetail.getLDMH());//楼栋名/号
                form.setField("DKBL" + num, estateIdGetLDXXEstateDetail.getDKBL());//贷款比例
                form.setField("JGQZ" + num, estateIdGetLDXXEstateDetail.getJGRQ());//竣工日期
                form.setField("XYRQ" + num, estateIdGetLDXXEstateDetail.getXYRQ());//协议日期
                form.setField("KSDYH" + num, estateIdGetLDXXEstateDetail.getKSDYH());//开始单元号
                form.setField("DYS" + num, estateIdGetLDXXEstateDetail.getDYS());//单元数
                form.setField("BeiZhu" + num, estateIdGetLDXXEstateDetail.getBeiZhu());//备注
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));
            form.setField("BLR", map.get("BLR"));
            form.setField("CZY", map.get("CZY"));
            form.setField("SHR", map.get("SHR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 单位清册确认footer
     */
    public String getInventoryBottomPage(HashMap<String, String> map, ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList, int fornum) {
        String templatePath = TEMPLATE + "InventoryTableTemp-3.pdf";
        System.out.println(templatePath);
        String outfilename = "InventoryReceipt_footer";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/InventoryReceipt_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (HeadRemittanceInventoryResDWHJQC list : dwhjqcArrayList) {
                int j = 22 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("ZJHM" + num, list.getZJHM());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("GRJCJS" + num, String.valueOf(list.getGRJCJS()));
                form.setField("DWYJCE" + num, String.valueOf(list.getDWYJCE()));
                form.setField("GRYJCE" + num, String.valueOf(list.getGRYJCE()));
                form.setField("HeJi" + num, String.valueOf(list.getHeJi()));
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));
            form.setField("DWYJCE32", map.get("DWYJCE32"));
            form.setField("GRYJCE32", map.get("GRYJCE32"));
            form.setField("HeJi32", map.get("HeJi32"));
            form.setField("CZY", map.get("CZY"));
            form.setField("DWJBR", map.get("DWJBR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 提取记录head
     */
    public String getWithdrawlsRecords(WithdrawlRecordsReturn result) {
        ArrayList<Record> records = result.getRecordsList().getResults();
        Integer count = records.size();
        String id = null;
        if (count >= 0 && count < 25) {
            id = getWithdrawlReceiptOnlyPage(result);
        } else {
            String templatePath = TEMPLATE + "WithdrawlRecordTableTemp-0.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "WithdrawlRecord_head" + result.getIndiAcctInfo().getYwlsh();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/WithdrawlRecord_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getIndiAcctInfo().getYwlsh());
                form.setField("YWWD", result.getYWWD());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("DYRQ", sdf.format(now));
                form.setField("DWMC", result.getIndiAcctInfo().getDwmc());
                form.setField("GRZH", result.getIndiAcctInfo().getGrzh());
                form.setField("XingMing", result.getIndiAcctInfo().getXingMing());
                Integer num = 1;
                Iterator<Record> iter = records.iterator();
                while (iter.hasNext()) {
                    Record record = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("TQRQ" + num, record.getTQRQ());
                    form.setField("BCTQJE" + num, String.valueOf(record.getBCTQJE()));
                    form.setField("LJTQJE" + num, String.valueOf(record.getLJTQJE()));
                    form.setField("BLR" + num, record.getBLR());
                    form.setField("CZY" + num, record.getCZY());
                    form.setField("YWWD" + num, record.getYWWD());
                    iter.remove();
                    if (num == 24) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = getWithdrawlRecordforeachTable(records, result.getIndiAcctInfo().getYwlsh());
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 1)];
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                }
            }
            mergeMorePdf(strArr, outfile);
        }
        return id;
    }

    /*
     * 提取记录仅一页模板
     */
    public String getWithdrawlReceiptOnlyPage(WithdrawlRecordsReturn result) {
        ArrayList<Record> records = result.getRecordsList().getResults();
        String templatePath = TEMPLATE + "WithdrawlRecordTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "WithdrawlRecordT_one" + result.getIndiAcctInfo().getYwlsh();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getIndiAcctInfo().getYwlsh());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("DYRQ", sdf.format(now));
            form.setField("DWMC", result.getIndiAcctInfo().getDwmc());
            form.setField("GRZH", result.getIndiAcctInfo().getGrzh());
            form.setField("XingMing", result.getIndiAcctInfo().getXingMing());
            Integer num = 1;
            for (Record record : records) {
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("TQRQ" + num, record.getTQRQ());
                form.setField("BCTQJE" + num, String.valueOf(record.getBCTQJE()));
                form.setField("LJTQJE" + num, String.valueOf(record.getLJTQJE()));
                form.setField("BLR" + num, record.getBLR());
                form.setField("CZY" + num, record.getCZY());
                form.setField("YWWD" + num, record.getYWWD());
                num++;
            }
            form.setField("page", "1");// 页码
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 提取记录body
     */
    public FilePdfData getWithdrawlRecordforeachTable(ArrayList<Record> records, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = records.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo != 0) {
            fornum = fornum + 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "WithdrawlRecordTableTemp-1.pdf";
            System.out.println(templatePath);
            String outfilename = "WithdrawlRecord_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/WithdrawlRecord_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<Record> iter = records.iterator();
                while (iter.hasNext()) {
                    int j = 24 + i * 31 + num;
                    Record record = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("TQRQ" + num, record.getTQRQ());
                    form.setField("BCTQJE" + num, String.valueOf(record.getBCTQJE()));
                    form.setField("LJTQJE" + num, String.valueOf(record.getLJTQJE()));
                    form.setField("BLR" + num, record.getBLR());
                    form.setField("CZY" + num, record.getCZY());
                    form.setField("YWWD" + num, record.getYWWD());
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 单位催缴记录head
     */
    public String getUnitPayCallPdf(HeadUnitPayCallReceiptRes result) {
        ArrayList<HeadUnitPayCallReceiptResCJJL> CJJLArrayList = result.getCJJL();
        Integer count = CJJLArrayList.size();
        String id = null;
        if (count >= 0 && count < 24) {
            id = PayCallReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 250, 680, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "UnitPayCallReceiptTemp-0.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "IUnitPayCall_head" + result.getYWLSH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/IUnitPayCall_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getYWLSH());
                form.setField("YWWD", result.getYWWD());// 业务网点
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWMC());
                form.setField("DWZH", result.getDWZH());
                form.setField("JBRXM", result.getJBRXM());
                form.setField("JBRSJHM", result.getJBRSJHM());
                form.setField("CZY", result.getCZY());
                Integer num = 1;
                Iterator<HeadUnitPayCallReceiptResCJJL> iter = CJJLArrayList.iterator();
                while (iter.hasNext()) {
                    HeadUnitPayCallReceiptResCJJL headUnitPayCallReceiptResCJJL = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("YHJNY" + num, headUnitPayCallReceiptResCJJL.getYHJNY());
                    form.setField("FSRS" + num, String.valueOf(headUnitPayCallReceiptResCJJL.getFSRS()));
                    form.setField("FSE" + num, headUnitPayCallReceiptResCJJL.getFSE());
                    form.setField("CJSJ" + num, headUnitPayCallReceiptResCJJL.getCJSJ());
                    form.setField("CZY" + num, headUnitPayCallReceiptResCJJL.getCZY());
                    form.setField("CJFS" + num, headUnitPayCallReceiptResCJJL.getCJFS());
                    iter.remove();
                    if (num == 23) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = getUnitPayCallforeachTable(CJJLArrayList, result.getYWLSH());
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 1)];
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 250, 680, SealHelper.getContractSeal(), id);
        }
        return id;
    }

    /*
     * 单位催缴记录仅一页模板
     */
    public String PayCallReceiptOnlyPage(HeadUnitPayCallReceiptRes result) {
        ArrayList<HeadUnitPayCallReceiptResCJJL> CJJLArrayList = result.getCJJL();
        String templatePath = TEMPLATE + "UnitPayCallReceiptTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "UnitPayCall_one" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());// 业务网点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JBRXM", result.getJBRXM());
            form.setField("JBRSJHM", result.getJBRSJHM());
            form.setField("CZY", result.getCZY());
            Integer num = 1;
            Iterator<HeadUnitPayCallReceiptResCJJL> iter = CJJLArrayList.iterator();
            while (iter.hasNext()) {
                HeadUnitPayCallReceiptResCJJL headUnitPayCallReceiptResCJJL = iter.next();
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("YHJNY" + num, headUnitPayCallReceiptResCJJL.getYHJNY());
                form.setField("FSRS" + num, String.valueOf(headUnitPayCallReceiptResCJJL.getFSRS()));
                form.setField("FSE" + num, headUnitPayCallReceiptResCJJL.getFSE());
                form.setField("CJSJ" + num, headUnitPayCallReceiptResCJJL.getCJSJ());
                form.setField("CZY" + num, headUnitPayCallReceiptResCJJL.getCZY());
                form.setField("CJFS" + num, headUnitPayCallReceiptResCJJL.getCJFS());
                num++;
            }
            form.setField("page", "1");// 页码
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 单位催缴记录body
     */
    public FilePdfData getUnitPayCallforeachTable(ArrayList<HeadUnitPayCallReceiptResCJJL> CJJLArrayList, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = CJJLArrayList.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo != 0) {
            fornum = fornum + 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "UnitPayCallReceiptTemp-1.pdf";
            System.out.println(templatePath);
            String outfilename = "UnitPayCallReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/InventoryReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadUnitPayCallReceiptResCJJL> iter = CJJLArrayList.iterator();
                while (iter.hasNext()) {
                    int j = 23 + i * 31 + num;
                    HeadUnitPayCallReceiptResCJJL headUnitPayCallReceiptResCJJL = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("YHJNY" + num, headUnitPayCallReceiptResCJJL.getYHJNY());
                    form.setField("FSRS" + num, String.valueOf(headUnitPayCallReceiptResCJJL.getFSRS()));
                    form.setField("FSE" + num, headUnitPayCallReceiptResCJJL.getFSE());
                    form.setField("CJSJ" + num, headUnitPayCallReceiptResCJJL.getCJSJ());
                    form.setField("CZY" + num, headUnitPayCallReceiptResCJJL.getCZY());
                    form.setField("CJFS" + num, headUnitPayCallReceiptResCJJL.getCJFS());
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 生成凭证
     */
    public String getVoucherGetDetailPdf(VoucherManager result) {
        List<VoucherMangerList> voucherMangerList = result.getVoucherMangerLists();
        Integer count = voucherMangerList.size();
        String templatePath = null;// 模板路径
        String id = null;
        Integer fornum = (count / 5);
        if (count >= 0 && count <= 5) {
            id = getVoucherOnlyOne(result);
            id = iPdfServiceCa.addSignaturePdf(1, 100, 20, SealHelper.getFinancialSeal(), id);
        } else {
            FilePdfData filePdfData = VoucherForeachTable(result);
            templatePath = TEMPLATE + "VoucherTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "VoucherGetDetail_footer" + result.getJZPZH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/VoucherGetDetail_footer" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("Year", String.valueOf(DateUtil.getYear(result.getJZRQ())));
                form.setField("Mon", String.valueOf(DateUtil.getMonth(result.getJZRQ())));
                form.setField("Day", String.valueOf(DateUtil.getDay(result.getJZRQ())));
                form.setField("HSDW", result.getHSDW());
                form.setField("PZH", result.getJZPZH());
                form.setField("No", String.valueOf(fornum + 1));
                form.setField("NO", String.valueOf(fornum + 1));
                form.setField("FJSL", result.getFJSL());
                form.setField("CWZG", result.getCWZG());
                form.setField("JiZhang", result.getJiZhang());
                form.setField("FuHe", result.getFuHe());
                form.setField("ChuNa", result.getChuNa());
                form.setField("ZhiDan", result.getZhiDan());
                Integer num = 1;
                Iterator<VoucherMangerList> iter = voucherMangerList.iterator();
                while (iter.hasNext()) {
                    VoucherMangerList voucherMangerList1 = iter.next();
                    form.setField("ZhaiYao" + num, voucherMangerList1.getZhaiYao());
                    form.setField("KJKM" + num, voucherMangerList1.getKJKMKMMC());
                    form.setField("JFJE" + num, voucherMangerList1.getJFJE());
                    form.setField("DFJE" + num, voucherMangerList1.getDFJE());
                    iter.remove();
                    if (num == 5) {
                        break;
                    }
                    num++;
                }
                form.setField("HJJFJE", result.getHJJFJE());
                form.setField("HJDFJE", result.getHJDFJE());
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 1)];
            int[] pages = new int[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                if (i == (strArr.length - 1)) {
                    strArr[i] = outfile_head;
                } else {
                    strArr[i] = filePdfData.getFilePathArray().get(i);
                }
                pages[i] = i + 1;
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(pages, 100, 20, SealHelper.getFinancialSeal(), id);
        }
        return id;
    }

    /*
     * 生成凭证head\body
     */
    public FilePdfData VoucherForeachTable(VoucherManager result) {
        ArrayList<String> filePathArray = new ArrayList<>();
        List<VoucherMangerList> voucherMangerList = result.getVoucherMangerLists();
        Integer count = voucherMangerList.size();
        Integer fornum = (count / 5);
        Integer fo = (count % 5);
        if (fo == 0) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "VoucherTemp-1.pdf";
            System.out.println(templatePath);
            String outfilename = "VoucherTemp_body" + i + result.getJZPZH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/VoucherTemp_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<VoucherMangerList> iter = voucherMangerList.iterator();
                while (iter.hasNext()) {
                    VoucherMangerList voucherMangerList1 = iter.next();
                    form.setField("ZhaiYao" + num, voucherMangerList1.getZhaiYao());
                    form.setField("KJKM" + num, voucherMangerList1.getKJKMKMMC());
                    form.setField("JFJE" + num, voucherMangerList1.getJFJE());
                    form.setField("DFJE" + num, voucherMangerList1.getDFJE());
                    iter.remove();
                    if (num == 5) {
                        break;
                    }
                    num++;
                }
                form.setField("Year", String.valueOf(DateUtil.getYear(result.getJZRQ())));
                form.setField("Mon", String.valueOf(DateUtil.getMonth(result.getJZRQ())));
                form.setField("Day", String.valueOf(DateUtil.getDay(result.getJZRQ())));
                form.setField("HSDW", result.getHSDW());
                form.setField("PZH", result.getJZPZH());
                form.setField("No", String.valueOf(i + 1));
                form.setField("NO", String.valueOf(fornum + 1));
                form.setField("FJSL", result.getFJSL());
                form.setField("CWZG", result.getCWZG());
                form.setField("JiZhang", result.getJiZhang());
                form.setField("FuHe", result.getFuHe());
                form.setField("ChuNa", result.getChuNa());
                form.setField("ZhiDan", result.getZhiDan());
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     *生产凭证，仅一张模板
     */
    public String getVoucherOnlyOne(VoucherManager result) {
        List<VoucherMangerList> voucherMangerList = result.getVoucherMangerLists();
        Integer count = voucherMangerList.size();
        String templatePath = TEMPLATE + "VoucherTemp-2.pdf";
        System.out.println(templatePath);
        String outfilename = "VoucherGetDetail_one" + result.getJZPZH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("Year", String.valueOf(DateUtil.getYear(result.getJZRQ())));
            form.setField("Mon", String.valueOf(DateUtil.getMonth(result.getJZRQ())));
            form.setField("Day", String.valueOf(DateUtil.getDay(result.getJZRQ())));
            form.setField("HSDW", result.getHSDW());
            form.setField("PZH", result.getJZPZH());
            form.setField("No", "1");
            form.setField("NO", "1");
            form.setField("FJSL", result.getFJSL());
            form.setField("CWZG", result.getCWZG());
            form.setField("JiZhang", result.getJiZhang());
            form.setField("FuHe", result.getFuHe());
            form.setField("ChuNa", result.getChuNa());
            form.setField("ZhiDan", result.getZhiDan());
            Integer num = 1;
            for (VoucherMangerList list : voucherMangerList) {
                form.setField("ZhaiYao" + num, list.getZhaiYao());
                form.setField("KJKM" + num, list.getKJKMKMMC());
                form.setField("JFJE" + num, list.getJFJE());
                form.setField("DFJE" + num, list.getDFJE());
                num++;
            }
            form.setField("HJJFJE", String.valueOf(result.getHJJFJE()));
            form.setField("HJDFJE", String.valueOf(result.getHJDFJE()));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            java.io.File out = new java.io.File(outfile);
//            if (!out.exists()){
//                out.mkdir();
//            }
            OutputStream fos = new FileOutputStream(out);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }


    public String getApplyHousingCompanyReceipt(ApplyHousingCompanyReceipt result, String code) {
        // 模板路径
        String templatePath = null;
        String outfilename = null;
        int pdfy = 180;
        if (code.equals("72")) {
            templatePath = TEMPLATE + "ApplyHousingCompanyReceipt.pdf";
            outfilename = "ApplyHousingCompanyReceipt" + result.getYWLSH();
            pdfy = 180;
        }
        if (code.equals("76")) {
            templatePath = TEMPLATE + "HousingCompanyAlterReceipt.pdf";
            outfilename = "HousingCompanyAlterReceipt" + result.getYWLSH();
            pdfy = 180;
        }
        System.out.println(templatePath);
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getUnitInfo().getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("FKZH", result.getUnitInfo().getFKZH());
            form.setField("FKGS", result.getUnitInfo().getFKGS());
            form.setField("ZZDJ", result.getUnitInfo().getZZDJ());

            form.setField("SJFLX", result.getUnitInfo().getSJFLB());
            form.setField("ZZDJ", result.getUnitInfo().getZZDJ());
            form.setField("ZZJGDM", result.getUnitInfo().getZZJGDM());
            form.setField("ZCZJ", result.getUnitInfo().getZCZJ());
            form.setField("DWDZ", result.getUnitInfo().getDWDZ());
            form.setField("ZCDZ", result.getUnitInfo().getZCDZ());
            form.setField("LXR", result.getUnitInfo().getLXR());
            form.setField("LXDH", result.getUnitInfo().getLXDH());
            form.setField("SFRKHYHMC", result.getUnitInfo().getHousingCompanyInfoSales().get(0).getSFRKHYHMC());
            form.setField("SFRKHYHKHM", result.getUnitInfo().getHousingCompanyInfoSales().get(0).getSFRKHYHKHM());// 经办人签字
            form.setField("SFRZHHM", result.getUnitInfo().getHousingCompanyInfoSales().get(0).getSFRZHHM());
            form.setField("FRDB", result.getUnitInfo().getFRDB());
            form.setField("FRDBZJLX", result.getUnitInfo().getFRDBZJLX());
            form.setField("FRDBZJHM", result.getUnitInfo().getFRDBZJHM());
            form.setField("BZJZH", result.getUnitInfo().getBZJZH());
            form.setField("BZJZHKHH", result.getUnitInfo().getBZJZHKHH());
            form.setField("BZJKHM", result.getUnitInfo().getBZJKHM());// 经办人签字
            form.setField("BeiZhu", result.getUnitInfo().getBeiZhu());// 审核人\
            form.setField("JBRQZ", null);
            form.setField("CZY", result.getCZY());
            form.setField("SHR", result.getSHR());
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 200, pdfy, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getPrintRepaymentReceipt(RepaymentApplyReceipt result, String hklx) {
        // 模板路径
        String templatePath = null;
        String outfilename = null;
        int pdfy = 300;
        if (hklx.equals("04")) {//逾期还款
            templatePath = TEMPLATE + "RepaymentReceiptLater-1.pdf";
            outfilename = "PrintRepaymentverdueReceip" + result.getYWLSH();
            pdfy = 300;
        }
        if (hklx.equals("03")) {//提前部分还款
            templatePath = TEMPLATE + "RepaymentReceiptRe.pdf";
            outfilename = "PrintRepaymentPartReceip" + result.getYWLSH();
            pdfy = 260;
        }
        if (hklx.equals("06")) {//提前结清
            templatePath = TEMPLATE + "RepaymentReceiptReAll.pdf";
            outfilename = "PrintRepaymentSettleReceip" + result.getYWLSH();
            pdfy = 280;
        }
        System.out.println(templatePath);
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            if (hklx.equals("03")) {//提前部分还款
                form.setField("JKRZJHM", result.getTQBFHKXX().getJKRZJHM());
                form.setField("JKRXM", result.getTQBFHKXX().getJKRXM());
                form.setField("HKFS", result.getTQBFHKXX().getHKFS());
                form.setField("SYBJ", result.getTQBFHKXX().getSYBJ());
                form.setField("SYLX", result.getTQBFHKXX().getSYLX());
                form.setField("SYQS", result.getTQBFHKXX().getSYQS());
                form.setField("HKJE", result.getTQBFHKXX().getBCHKJE());
                form.setField("YDKKRQ", result.getTQBFHKXX().getYDKKRQ());
                form.setField("YSYHKE", result.getTQBFHKXX().getYSYHKE());
                form.setField("MYDJ", result.getTQBFHKXX().getMYDJ());
                form.setField("YZHHKQX", result.getTQBFHKXX().getYZHHKQX());
                form.setField("GYYCHKE", result.getTQBFHKXX().getGYYCHKE());
                form.setField("XYHKE", result.getTQBFHKXX().getXYHKE());// 经办人签字
                form.setField("XMYDJ", result.getTQBFHKXX().getXMYDJ());
                form.setField("JYLX", result.getTQBFHKXX().getJYLX());
            } else if (hklx.equals("04")) {//逾期还款
                form.setField("JKRZJHM", result.getYQHKXX().getJKRZJHM());
                form.setField("JKRXM", result.getYQHKXX().getJKRXM());
                form.setField("YQKZJ", result.getYQHKXX().getYQKZJ());
                form.setField("HKJE", null);//还款金额
                form.setField("YDKKRQ", result.getYQHKXX().getYDKKRQ());
                Integer num = 1;
                Double YQBJ = 0.0;
                Double YQLX = 0.0;
                Double YQFX = 0.0;
                ArrayList<RepaymentApplyPrepaymentPostYQHKXXYQXX> yqxxlist = result.getYQHKXX().getYQXX();
                if (yqxxlist != null) {
                    for (RepaymentApplyPrepaymentPostYQHKXXYQXX list : yqxxlist) {
                        YQBJ += Double.parseDouble(list.getYQBJ());
                        YQLX += Double.parseDouble(list.getYQLX());
                        YQFX += Double.parseDouble(list.getYQFX());
                        num++;
                    }
                }
                form.setField("YQQC", String.valueOf(num));
                form.setField("YQBJ", String.valueOf(YQBJ));
                form.setField("YQLX", String.valueOf(YQLX));
                form.setField("YQFX", String.valueOf(YQFX));

            } else if (hklx.equals("06")) {//提前结清
                form.setField("JKRZJHM", result.getTQJQHKXX().getJKRZJHM());
                form.setField("JKRXM", result.getTQJQHKXX().getJKRXM());
                form.setField("HKFS", result.getTQJQHKXX().getHKFS());
                form.setField("SYBJ", result.getTQJQHKXX().getSYBJ());
                form.setField("SYLX", result.getTQJQHKXX().getSYLX());
                form.setField("SYQS", result.getTQJQHKXX().getSYQS());
                form.setField("HKJE", result.getTQJQHKXX().getHKFS());
                form.setField("YDKKRQ", result.getTQJQHKXX().getYDKKRQ());

            }
            form.setField("BLRQZ", null);
            form.setField("CZY", result.getCZY());
            form.setField("SHR", result.getSHR());

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, pdfy, SealHelper.getContractSeal(), id);
        return id;
    }

    private String EstateProjectAlterReceiptOnlyPage(EstateIdGet result, String code) {
        String templatePath = null;
        String outfilename = null;
        int pdfy = 10;
        if (code.equals("01")) {
            templatePath = TEMPLATE + "EstateProjectReceipt_one.pdf";
            outfilename = "EstateProjectReceipt_one" + result.getYWLSH();
            pdfy = 10;
        } else if (code.equals("02")) {
            templatePath = TEMPLATE + "EstateProjectAlterReceipt_one.pdf";
            outfilename = "EstateProjectAlterReceipt_one" + result.getYWLSH();
            pdfy = 10;
        }
        System.out.println(templatePath);
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());//业务流水号
            form.setField("YWWD", result.getYWWD());//业务网点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));//填制日期
            form.setField("FKGS", result.getFKGS());//房开公司
            form.setField("LPMC", result.getLPMC());//楼盘名称
            form.setField("LPDZ", result.getLPDZ());//楼盘地址
            form.setField("YSXKZH", result.getYSXKZH());//预售许可证号
            form.setField("BZJBL", result.getBZJBL());//保证金比例
            form.setField("JZZMJ", result.getJZZMJ());//建筑总面积
            form.setField("JZZJE", result.getJZZJE());//建筑总金额
            form.setField("HQTDDJ", result.getHQTDDJ());//获取土地单价
            form.setField("HQTDZJ", result.getHQTDZJ());//获取土地总价
            form.setField("LXR", result.getLXR());//联系人
            form.setField("LXDH", result.getLXDH());//联系电话
            form.setField("AJXYRQ", result.getAJXYRQ());//按揭协议日期
            form.setField("BeiZhu", result.getBeiZhu());// 备注
            // 楼栋名/号 贷款比例 竣工日期 协议日期 开始单元号 单元数 备注
            int num = 1;
            for (EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail : result.getLDXX().getestateDetail()) {
                form.setField("XuHao" + num, num + "");//楼栋名/号
                form.setField("LDMH" + num, estateIdGetLDXXEstateDetail.getLDMH());//楼栋名/号
                form.setField("DKBL" + num, estateIdGetLDXXEstateDetail.getDKBL());//贷款比例
                form.setField("JGRQ" + num, estateIdGetLDXXEstateDetail.getJGRQ());//竣工日期
                form.setField("XYRQ" + num, estateIdGetLDXXEstateDetail.getXYRQ());//协议日期
                form.setField("KSDYH" + num, estateIdGetLDXXEstateDetail.getKSDYH());//开始单元号
                form.setField("DYS" + num, estateIdGetLDXXEstateDetail.getDYS());//单元数
                form.setField("BeiZhu" + num, estateIdGetLDXXEstateDetail.getBeiZhu());//备注
                num++;
            }
            form.setField("page", 1 + "");//页码
            form.setField("BLR", result.getBLR());//办理人
            form.setField("CZY", result.getCZY());//操作员
            form.setField("SHR", result.getSHR());//审核人

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, pdfy, SealHelper.getContractSeal(), id);
        return id;
    }

    private String getEstateProjectAlterReceiptNoOne(EstateIdGet result, String code) {
        ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateDetails = result.getLDXX().getestateDetail();
        int count = estateIdGetLDXXEstateDetails.size();
        String id = "";
        String templatePath = "";
        String outfilename = "";
        int pdfy = 20;
        if (code.equals("01")) {
            templatePath = TEMPLATE + "EstateProjectReceipt-1.pdf";// 模板路径
            outfilename = "EstateProjectReceipt_head" + result.getYWLSH();
            pdfy = 20;
        } else if (code.equals("02")) {
            templatePath = TEMPLATE + "EstateProjectAlterReceipt-1.pdf";// 模板路径
            outfilename = "EstateProjectAlterReceipt_head" + result.getYWLSH();
            pdfy = 20;
        }
        System.out.println(templatePath);
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile_head = BASEPATH + TMPPATH + "/EstateReceipt_head" + id;
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());//业务流水号
            form.setField("YWWD", result.getYWWD());//业务网点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));//填制日期
            form.setField("FKGS", result.getFKGS());//房开公司
            form.setField("LPMC", result.getLPMC());//楼盘名称
            form.setField("LPDZ", result.getLPDZ());//楼盘地址
            form.setField("YSXKZH", result.getYSXKZH());//预售许可证号
            form.setField("BZJBL", result.getBZJBL());//保证金比例
            form.setField("JZZMJ", result.getJZZMJ());//建筑总面积
            form.setField("JZZJE", result.getJZZJE());//建筑总金额
            form.setField("HQTDDJ", result.getHQTDDJ());//获取土地单价
            form.setField("HQTDZJ", result.getHQTDZJ());//获取土地总价
            form.setField("LXR", result.getLXR());//联系人
            form.setField("LXDH", result.getLXDH());//联系电话
            form.setField("AJXYRQ", result.getAJXYRQ());//按揭协议日期
            form.setField("BeiZhu", result.getBeiZhu());// 备注
            // 楼栋名/号 贷款比例 竣工日期 协议日期 开始单元号 单元数 备注
            int num = 1;
            Iterator<EstateIdGetLDXXEstateDetail> iter = estateIdGetLDXXEstateDetails.iterator();
            while (iter.hasNext()) {

                EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail = iter.next();
                form.setField("XuHao" + num, num + "");
                form.setField("LDMH" + num, estateIdGetLDXXEstateDetail.getLDMH());//楼栋名/号
                form.setField("DKBL" + num, estateIdGetLDXXEstateDetail.getDKBL());//贷款比例
                form.setField("JGQZ" + num, estateIdGetLDXXEstateDetail.getJGRQ());//竣工日期
                form.setField("XYRQ" + num, estateIdGetLDXXEstateDetail.getXYRQ());//协议日期
                form.setField("KSDYH" + num, estateIdGetLDXXEstateDetail.getKSDYH());//开始单元号
                form.setField("DYS" + num, estateIdGetLDXXEstateDetail.getDYS());//单元数
                form.setField("BeiZhu" + num, estateIdGetLDXXEstateDetail.getBeiZhu());//备注
                iter.remove();
                if (num == 18) {
                    break;
                }
                num++;
            }
            form.setField("BLR", result.getBLR());//办理人
            form.setField("CZY", result.getCZY());//操作员
            form.setField("SHR", result.getSHR());//审核人
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile_head);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile_head));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        FilePdfData filePdfData = estateforeachTable(estateIdGetLDXXEstateDetails, result.getYWLSH(), code);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("BLR", result.getBLR());
        map.put("CZY", result.getCZY());
        map.put("SHR", result.getSHR());
        String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
        int fornum = ((count - 18) / 31);
        Integer fo = ((count - 18) % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0) {
                strArr[i] = outfile_head;
            } else {
                if (i == strArr.length - 1) {
                    strArr[i] = getEstateBottomPage(map, filePdfData.getEstateIdGetLDXXEstateList(), fornum, code);
                } else {
                    strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                }
            }
        }
        mergeMorePdf(strArr, outfile);
        id = iPdfServiceCa.addSignaturePdf(strArr.length, 300, pdfy, SealHelper.getContractSeal(), id);
        if (strArr.length > 1) {
            id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
        }
        return id;
    }


    @Override
    public String getEstateProjectAlterReceipt(EstateIdGet result, String code) {
        String id = "";
        // for(int i =0;i<100;i++){
        //     EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail =new EstateIdGetLDXXEstateDetail();
        //     estateIdGetLDXXEstateDetail.setBeiZhu("111");
        //     estateIdGetLDXXEstateDetail.setDYS("11");
        //     result.getLDXX().getestateDetail().add(estateIdGetLDXXEstateDetail);
        // }
        int size = result.getLDXX().getestateDetail().size();
        if (size >= 0 && size < 19)
            id = EstateProjectAlterReceiptOnlyPage(result, code);
        else {
            id = getEstateProjectAlterReceiptNoOne(result, code);
        }
        return id;
    }

    public String getContractRecepit(LoanContractPDFResponse loanContractPDFResponse) {

        String templatePath = TEMPLATE + "LoanContract.pdf";

        String outfilename = "LoanContract.pdf";

        System.out.println(templatePath);
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(REVIEWOUTPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + REVIEWOUTPATH + "/" + id;
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);

            //region//担保信息 明细
            HashSet<String> BZR = new HashSet<>();/*保证人*/
            HashSet<String> DYRMC = new HashSet<>();
            ;/*抵押人名称*/
            HashSet<String> DYRSFZJMCJHM = new HashSet<>();/*抵押人身份证件名称及号码*/
            HashSet<String> DYWGYQRMC = new HashSet<>();
            ;/*抵押物共有权人名称*/
            HashSet<String> DYWGYQRSFZJMCJHM = new HashSet<>();
            ;/*抵押物共有权人身份证件名称及号码*/
            HashSet<String> DYWPGJ = new HashSet<>();/*抵押物评估价*/

            if (LoanGuaranteeType.抵押.getCode().equals(loanContractPDFResponse.getOrignal().getGuaranteeContractInformation().getDKDBLX())) {

                for (int i = 0; (i < loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getMortgageInformations().size() || i == 4); i++) {

                    GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation mortgageInformation = loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getMortgageInformations().get(i);

                    if (i <= 4) {
                        form.setField("ChuSuo" + (i + 1), mortgageInformation.getDYWFWZL()/*处所 抵押房屋坐落*/);

                        form.setField("DYCCDJZ" + (i + 1), mortgageInformation.getDYWPGJZ()/*抵押财产的价值*/);

                        form.setField("QSZSJQTYGZSBH" + (i + 1), mortgageInformation.getQSZSBH()/*权属证书及其他有关证书编号*/);

                        form.setField("DYCCMC" + (i + 1), mortgageInformation.getDYWMC()/*抵押财产名称*/);

                        form.setField("MJHSL" + (i + 1), mortgageInformation.getFWMJ()/*面积或数量_抵押物 套内面 积*/);
                    }
                    DYRMC.add(mortgageInformation.getDYWSYQRXM());
                    DYRSFZJMCJHM.add("".equals(mortgageInformation.getDYWSYQRSFZHM()) ? "" : ("身份证" + mortgageInformation.getDYWSYQRSFZHM()));
                    DYWGYQRMC.add(mortgageInformation.getDYWGYQRXM());
                    DYWGYQRSFZJMCJHM.add("".equals(mortgageInformation.getDYWGYQRSFZHM()) ? "" : ("身份证" + mortgageInformation.getDYWGYQRSFZHM()));
                    DYWPGJ.add(mortgageInformation.getDYWPGJZ());
                }
            }

            if (LoanGuaranteeType.质押.getCode().equals(loanContractPDFResponse.getOrignal().getGuaranteeContractInformation().getDKDBLX())) {

                for (int i = 0; (i < loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getPledgeInformations().size() || i == 4); i++) {

                    GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation pledgeInformation = loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getPledgeInformations().get(i);


                }

            }

            if (LoanGuaranteeType.保证.getCode().equals(loanContractPDFResponse.getOrignal().getGuaranteeContractInformation().getDKDBLX())) {

                for (int i = 0; (i < loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getGuaranteeInformations().size() || i == 4); i++) {

                    GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation guaranteeInformation = loanContractPDFResponse.getApplicationDetails().getCollateralInformation().getGuaranteeInformations().get(i);

                }
            }

            //endregion

            //region //银行信息
            form.setField("DKR", loanContractPDFResponse.getOrignal().getContractInformation().getSWTYHMC()/*贷款人*/);
            form.setField("DKRTXDZ", loanContractPDFResponse.getDKRTXDZ()/*贷款人通讯地址*/);
            form.setField("DKYHMC", loanContractPDFResponse.getOrignal().getContractInformation().getSWTYHMC()/*贷款银行名称?*/);
            form.setField("DKYHQC", loanContractPDFResponse.getDKYHQC()/*贷款银行全称?*/);
            form.setField("LXFS", loanContractPDFResponse.getLXFS()/*贷款人联系方式*/);
            //endregion

            //region//担保 总和

//            form.setField("BZR", CollectionUtils.reduce(BZR, "", new CollectionUtils.Reducer<String, String>() {
//                @Override
//                public String reducer(String sum, String obj) {
//                    return sum + obj;
//                }
//
//            })/*保证人*/);
            form.setField("BZR", new HashMap<String, String>() {{
                this.put("0", "1".equals(loanContractPDFResponse.getApplicationDetails().getHouseInformation().getSFWESF()) ? loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM() : loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRMC());
                this.put("1", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM());
                this.put("2", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM());
            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()));

            form.setField("DYRMC", CollectionUtils.reduce(DYRMC, "", new CollectionUtils.Reducer<String, String>() {
                @Override
                public String reducer(String sum, String obj) {
                    return sum + obj;
                }

            })/*抵押人名称*/);
            form.setField("DYRSFZJMCJHM", CollectionUtils.reduce(DYRSFZJMCJHM, "", new CollectionUtils.Reducer<String, String>() {
                @Override
                public String reducer(String sum, String obj) {
                    return sum + obj;
                }

            })/*抵押人身份证件名称及号码*/);
            form.setField("DYWGYQRMC", CollectionUtils.reduce(DYWGYQRMC, "", new CollectionUtils.Reducer<String, String>() {
                @Override
                public String reducer(String sum, String obj) {
                    return sum + obj;
                }

            })/*抵押物共有权人名称*/);
            form.setField("DYWGYQRSFZJMCJHM", CollectionUtils.reduce(DYWGYQRSFZJMCJHM, "", new CollectionUtils.Reducer<String, String>() {
                @Override
                public String reducer(String sum, String obj) {
                    return sum + obj;
                }

            })/*抵押物共有权人身份证件名称及号码*/);
            form.setField("DYWPGJ", CollectionUtils.reduce(DYWPGJ, "0", new CollectionUtils.Reducer<String, String>() {
                @Override
                public String reducer(String sum, String obj) {

                    return new BigDecimal(sum).add(StringUtil.isDigits(obj, false) ? new BigDecimal(obj) : BigDecimal.ZERO) + "";
                }

            })/*抵押物评估价*/);
            //endregion

            //region //房屋信息
            form.setField("FWJZMJ", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getFWJZMJ());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getFWJZMJ());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getFWJZMJ());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*房屋建筑面积*/);

            CommonDictionary fwxzDictionary = CollectionUtils.find(iDictionaryService.getDictionaryByType("StateOfHouse"), new CollectionUtils.Predicate<CommonDictionary>() {
                @Override
                public boolean evaluate(CommonDictionary var1) {
                    return var1.getName().equals(new HashMap<String, String>() {{

                        this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getFWXZ());
                        this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getFWXZ());
                        this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getFWXZ());

                    }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*房屋性质*/);
                }
            });
            //按照最新需求，修改房屋性质。默认：住房
            form.setField("FWXZ", "住房");
//          // form.setField("FWXZ", fwxzDictionary == null ? null : fwxzDictionary.getName());
            form.setField("FWZL", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getFWZL());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getFWZL());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getFWZL());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()) /*房屋坐落*/);
            form.setField("GFHTMCJBH", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getGFHTBH());
                this.put("1", "");//loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().get);
                this.put("2", "");//loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getFWZL());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*购房合同名称及编号*/);

            form.setField("GMFWCJJ", new HashMap<String, String>() {{
                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getFWZJ());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getJHJZZJ());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getJHFXFY());
            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*购买房屋成交价(自建、大修 即 工程造价)*/);

            form.setField("SFR", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRMC());
                this.put("1", "");
                this.put("2", "");

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*售房人*/);
            form.setField("TNJZMJ", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getFWTNMJ());
                this.put("1", "");
                this.put("2", "");
            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*套内建筑面积*/);
            //endregion

            //region //借款人信息
            form.setField("JKR", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM()/*借款人*/);
            form.setField("JKRLXDHJCZ", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getSJHM()/*借款人联系电话及传真*/);
            form.setField("JKRMC", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM()/*借款人名称*/);
            form.setField("JKRSFZJMCJHM", new HashMap<String, String>() {{

                for (PersonCertificateType certificateType : PersonCertificateType.values()) {

                    this.put(certificateType.getCode(), certificateType.getName());
                }
            }}.get(loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRZJLX()) + loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRZJHM()/*借款人身份证件名称及号码*/);
            form.setField("JKRTXDZ", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJTZZ()/*借款人通讯地址*/);
            form.setField("JKRZDZHHM", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRZHHM());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getGRSKYHZH());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getGRSKYHZH());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*借款人指定账户号码*/);
            form.setField("JKRZDZHKHYH", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRKHYHMC());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getKHYHMC());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getKHYHMC());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()/*借款人指定账户开户银行*/));
            form.setField("JKRZDZHMC", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRKHHM());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getKHHM());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getKHHM());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()/*借款人指定账户名称*/));
            form.setField("KHYH", new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getSFRKHYHMC());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getKHYHMC());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getKHYHMC());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()/*借款人开户银行*/));
            form.setField("ZhangHao", loanContractPDFResponse.getOrignal().getContractInformation().getHKZH()/*账号 借款人*/);
            form.setField("ZHMC", loanContractPDFResponse.getApplicationDetails().getApplicantInformation().getBorrowerInformation().getJKRXM()/*借款人*//*账号名称 借款人*/);
            //endregion

            //region //共同借款人信息
            form.setField("GTJKRMC", loanContractPDFResponse.getApplicationDetails().getCommonBorrowerInformation().getGTJKRXM()/*共同借款人名称*/);
            form.setField("GTJKRSFZJMCJHM", new HashMap<String, String>() {{
                for (PersonCertificateType certificateType : PersonCertificateType.values()) {

                    this.put(certificateType.getCode(), certificateType.getName());
                }
            }}.get(loanContractPDFResponse.getApplicationDetails().getCommonBorrowerInformation().getGTJKRZJLX()) + loanContractPDFResponse.getApplicationDetails().getCommonBorrowerInformation().getGTJKRZJHM()/*共同借款人身份证件名称及号码*/);
            //endregion

            //region//资金信息
            form.setField("HKFF", new HashMap<String, String>() {{

                this.put("01", "壹");
                this.put("02", "贰");

            }}.get(new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getDKHKFS());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getDKHKFS());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getDKHKFS());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()))/*还款方法*/);

            form.setField("JKBJJE", StringUtil.digitUppercase(new HashMap<String, BigDecimal>() {{

                this.put("0", StringUtil.safeBigDecimal(loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getHTDKJE()));
                this.put("1", StringUtil.safeBigDecimal(loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getHTDKJE()));
                this.put("2", StringUtil.safeBigDecimal(loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getHTDKJE()));
            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT()).doubleValue())/*借款本金金额大写*/);

            form.setField("YHKE", "02".equals(new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getDKHKFS());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getDKHKFS());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getDKHKFS());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())) ? "" : loanContractPDFResponse.getGHBJJE()/*(每月)归还本金金额*/);

            form.setField("GHBJJE", "01".equals(new HashMap<String, String>() {{

                this.put("0", loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getDKHKFS());
                this.put("1", loanContractPDFResponse.getOrignal().getContractInformation().getBuildInformation().getDKHKFS());
                this.put("2", loanContractPDFResponse.getOrignal().getContractInformation().getOverhaulInformation().getDKHKFS());

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())) ? "" : loanContractPDFResponse.getYHKE()/*月还款额 本息*/);

            form.setField("JKQX", StringUtil.digitUppercase(loanContractPDFResponse.getApplicationDetails().getLoanAccountInformation().getDKQS()).replaceAll("[元|角|分|整]", "")/*借款期限(月)*/);
            form.setField("JKQXJS", ""/*借款期限结束(yyyy-mm)?*/);
            form.setField("JKQXKS", ""/*借款期限开始(yyyy-mm-)?*/);
            //endregion

            //region //合同信息
            form.setField("HTBH", loanContractPDFResponse.getApplicationDetails().getLoanAccountInformation().getJKHTBH()/*本合同编号*/);
            form.setField("SPBBH", loanContractPDFResponse.getSpbbh()/*审批表编号*/);

            //endregion

            //region//其他信息
            form.setField("QTXX0", ""/*其他信息 借款人及共同借款人*/);
            form.setField("QTXX1", ""/*其他信息 贷款人*/);
            form.setField("QTXX2", ""/*其他信息 抵押人*/);
            //endregion

            //region//待定
            form.setField("Num", "壹"/*1，2 中文大写*/);
            form.setField("SFK", new HashMap<String, String>() {{

                this.put("0", StringUtil.digitUppercase(StringUtil.safeBigDecimal(loanContractPDFResponse.getOrignal().getContractInformation().getPurchaseInformation().getGFSFK()).doubleValue()));
                this.put("1", "");
                this.put("2", "");

            }}.get(loanContractPDFResponse.getOrignal().getContractInformation().getDKYT())/*首付款 金额大写*/);
            //endregion

            //region//受委托银行信息
            form.setField("WTYHMC", loanContractPDFResponse.getOrignal().getContractInformation().getSWTYHMC()/*委托银行名称*/);
            form.setField("WTYHQC", loanContractPDFResponse.getOrignal().getContractInformation().getSWTYHMC()/*委托银行全称*/);
            //endregion

            form.getFields().forEach(new BiConsumer<String, AcroFields.Item>() {
                @Override
                public void accept(String string, AcroFields.Item item) {

                    if (form.getField(string) == null || form.getField(string).equals("null")) {

                        try {

                            form.setField(string, "");

                        } catch (IOException | DocumentException e) {

                            e.printStackTrace();
                        }
                    }
                }
            });
            //form.setField("logo_img",null);

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);

        } catch (IOException | DocumentException | NoSuchAlgorithmException e) {

            e.printStackTrace();

            throw new ErrorException(e);

        } catch (Exception e) {

            throw new ErrorException(e);
        }
//        id = iPdfServiceCa.addSignaturePdf(32, 280, 400, SealHelper.getContractSeal(), id);
//        id = iPdfServiceCa.addSignatureToIndexPdf(1, 32, SealHelper.getContractSeal(), id);
        return id;
    }

    /**
     * 单位补缴回执单head
     */
    public String getUnitPaybackReceipt(HeadUnitPayBackReceiptRes result) {
        ArrayList<HeadUnitPayBackReceiptResBJXX> personDetail = result.getBJXX();
        Integer count = personDetail.size();
        String id = null;
        if (count >= 0 && count < 20) {
            id = getPayBackReceiptOnlyPage(result);
            id = iPdfServiceCa.addSignaturePdf(1, 250, 620, SealHelper.getContractSeal(), id);
        } else {
            String templatePath = TEMPLATE + "PayBackTableTemp-1.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "PayBackReceipt_head" + result.getYWLSH();
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/PayBackReceipt_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("YZM", id);
                // form.setField("CiShu", "1");
                form.setField("YWLSH", result.getYWLSH());
                form.setField("YWWD", result.getYWWD());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("TZRQ", sdf.format(now));
                form.setField("DWMC", result.getDWMC());
                form.setField("DWZH", result.getDWZH());
                form.setField("JZNY", result.getJZNY());
                form.setField("YHJNY", result.getDWYHJNY());
                form.setField("ZSYE", String.valueOf(result.getZSYE()));
                form.setField("LSYLWFT", null);
                form.setField("DWZHYE", String.valueOf(result.getDWZHYE()));
                form.setField("HBJNY", result.getBJNY());//汇补缴年月
                form.setField("BJFS", result.getBJFS());
                form.setField("JZRQ", result.getJZRQ());
                form.setField("JBRXM", result.getJBRXM());
                form.setField("JBRZJHM", result.getJBRZJHM());
                Integer num = 1;
                Iterator<HeadUnitPayBackReceiptResBJXX> iter = personDetail.iterator();
                while (iter.hasNext()) {
                    HeadUnitPayBackReceiptResBJXX headUnitPayBackReceiptResBJXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(num));
                    form.setField("GRZH" + num, headUnitPayBackReceiptResBJXX.getGRZH());
                    form.setField("XingMing" + num, headUnitPayBackReceiptResBJXX.getXingMing());
                    form.setField("DWBJE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getDWBJE()));
                    form.setField("GRBJE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getGRBJE()));
                    form.setField("FSE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getFSE()));
                    form.setField("BJYY" + num, headUnitPayBackReceiptResBJXX.getBJYY());
                    iter.remove();
                    if (num == 19) {
                        break;
                    }
                    num++;
                }

                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            FilePdfData filePdfData = payBackReceiptforeachTable(personDetail, result.getYWLSH());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JBRQZ", null);//无签字
            map.put("DWJBR", result.getJBRXM());
            map.put("CZY", result.getCZY());//无操作人
            String[] strArr = new String[(filePdfData.getFilePathArray().size() + 2)];
            int fornum = ((count - 19) / 31);
            for (int i = 0; i < strArr.length; i++) {
                if (i == 0) {
                    strArr[i] = outfile_head;
                } else {
                    if (i == strArr.length - 1) {
                        strArr[i] = getPayBackBottomPage(map, filePdfData.getPersonDetail(), fornum);
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
            }
            mergeMorePdf(strArr, outfile);
            id = iPdfServiceCa.addSignaturePdf(1, 250, 620, SealHelper.getContractSeal(), id);
            if (strArr.length > 1) {
                id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
            }
        }
        return id;
    }

    /*
     * 单位补缴回执单--仅一张模板
     */
    public String getPayBackReceiptOnlyPage(HeadUnitPayBackReceiptRes result) {
        ArrayList<HeadUnitPayBackReceiptResBJXX> personDetail = result.getBJXX();
        Integer count = personDetail.size();
        String templatePath = TEMPLATE + "PayBackTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "PayBackTableRecipt_one" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);//
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            form.setField("DWMC", result.getDWMC());
            form.setField("DWZH", result.getDWZH());
            form.setField("JZNY", result.getJZNY());
            form.setField("YHJNY", result.getDWYHJNY());
            form.setField("ZSYE", String.valueOf(result.getZSYE()));
            form.setField("LSYLWFT", null);
            form.setField("DWZHYE", String.valueOf(result.getDWZHYE()));
            form.setField("HBJNY", result.getBJNY());//汇补缴年月
            form.setField("BJFS", result.getBJFS());
            form.setField("JZRQ", result.getJZRQ());
            form.setField("JBRXM", result.getJBRXM());
            form.setField("JBRZJHM", result.getJBRZJHM());
            Integer num = 1;
            for (HeadUnitPayBackReceiptResBJXX list : personDetail) {
                form.setField("XuHao" + num, String.valueOf(num));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("DWBJE" + num, String.valueOf(list.getDWBJE()));
                form.setField("GRBJE" + num, String.valueOf(list.getGRBJE()));
                form.setField("FSE" + num, String.valueOf(list.getFSE()));
                form.setField("BJYY" + num, list.getBJYY());
                num++;
            }
            form.setField("JBRQZ", null);//无签字
            form.setField("DWJBR", result.getJBRXM());
            form.setField("CZY", result.getCZY());//无操作人
            form.setField("page", "1");// 页码
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }

    /*
     * 单位补缴body
     */
    public FilePdfData payBackReceiptforeachTable(ArrayList<HeadUnitPayBackReceiptResBJXX> personDetail, String YWLSH) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = personDetail.size();
        Integer fornum = (count / 31);
        Integer fo = (count % 31);
        if (fo.equals(0)) {
            fornum = fornum - 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "PayBackTableTemp-2.pdf";
            System.out.println(templatePath);
            String outfilename = "PayBackReceipt_body" + i + YWLSH;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/PayBackReceipt_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<HeadUnitPayBackReceiptResBJXX> iter = personDetail.iterator();
                while (iter.hasNext()) {
                    int j = 19 + i * 31 + num;
                    HeadUnitPayBackReceiptResBJXX headUnitPayBackReceiptResBJXX = iter.next();
                    form.setField("XuHao" + num, String.valueOf(j));
                    form.setField("GRZH" + num, headUnitPayBackReceiptResBJXX.getGRZH());
                    form.setField("XingMing" + num, headUnitPayBackReceiptResBJXX.getXingMing());
                    form.setField("DWBJE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getDWBJE()));
                    form.setField("GRBJE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getGRBJE()));
                    form.setField("FSE" + num, String.valueOf(headUnitPayBackReceiptResBJXX.getFSE()));
                    form.setField("BJYY" + num, headUnitPayBackReceiptResBJXX.getBJYY());
                    iter.remove();
                    if (num == 31) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setPersonDetail(personDetail);
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 单位补缴footer
     */
    public String getPayBackBottomPage(HashMap<String, String> map, ArrayList<HeadUnitPayBackReceiptResBJXX> personDetail, int fornum) {
        String templatePath = TEMPLATE + "PayBackTableTemp-3.pdf";
        System.out.println(templatePath);
        String outfilename = "PayBackReceipt_footer";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/PayBackReceipt_footer" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            Integer num = 1;
            for (HeadUnitPayBackReceiptResBJXX list : personDetail) {
                int j = 19 + fornum * 31 + num;
                form.setField("XuHao" + num, String.valueOf(j));
                form.setField("GRZH" + num, list.getGRZH());
                form.setField("XingMing" + num, list.getXingMing());
                form.setField("DWBJE" + num, String.valueOf(list.getDWBJE()));
                form.setField("GRBJE" + num, String.valueOf(list.getGRBJE()));
                form.setField("FSE" + num, String.valueOf(list.getFSE()));
                form.setField("BJYY" + num, list.getBJYY());
                num++;
            }
            form.setField("page", String.valueOf(fornum + 2));// 页码
            form.setField("JBRQZ", map.get("JBRQZ"));
            form.setField("CZY", map.get("CZY"));
            form.setField("DWJBR", map.get("DWJBR"));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return outfile;
    }

    /*
     * 合并转移回执单
     */
    public String getMergeTransferReceiptPdf(TransferListGet transferListGet) {
        // 模板路径
        String templatePath = TEMPLATE + "MergeTransferReceipt.pdf";
        System.out.println(templatePath);
        String outfilename = "MergeTransferReceipt" + transferListGet.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", transferListGet.getYWLSH());
            form.setField("YWWD", transferListGet.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 单位关键信息
            form.setField("XingMing", transferListGet.getXingMing()); //姓名
            form.setField("GRZH", transferListGet.getGRZH()); //个人账号
            form.setField("GRYHCKZHHM", transferListGet.getGRYHCKZHHM()); //个人银行存款账户号码
            form.setField("ZCDWMC", transferListGet.getZCDWM()); //转出单位名称
            form.setField("ZCDWZH", transferListGet.getZCDWZH()); //转出单位账号
            form.setField("ZRDWMC", transferListGet.getZRDWM()); //转入单位名称
            form.setField("ZRDWZH", transferListGet.getZRDWZH()); //转入单位账号
            // 单位联系方式
            form.setField("ZYJE", transferListGet.getZYJE()); //转移金额
            form.setField("JZNY", transferListGet.getJZNY()); //缴至年月
            // 单位登记信息
            form.setField("GRZHYE", transferListGet.getZHYE()); //个人账户余额
            form.setField("QianZi", null); //签字
            form.setField("CZY", transferListGet.getCZY()); //操作员
            form.setField("SHR", transferListGet.getSHR()); //审核人
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 300, SealHelper.getContractSeal(), id);
        return id;
    }

    /*
     * 合同变更回执单
     */
    public String getContractAlterPdf(HeadLoanContract result) {
        // 模板路径
        String templatePath = TEMPLATE + "ContractAlterTemp.pdf";
        System.out.println(templatePath);
        String outfilename = "ContractAlterTemp" + result.getYWLSH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("YZM", id);
            // form.setField("CiShu", "1");
            form.setField("YWLSH", result.getYWLSH());
            form.setField("YWWD", result.getYWWD());
            form.setField("JKHTBH", result.getJKHTBH());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("TZRQ", sdf.format(now));
            // 借款人关键信息
            form.setField("JKRXM_1", result.getLoanContractChangeBefore().getBorrowerInfomation().getXingMing()); //变更前姓名
            form.setField("JKRZJHM_1", result.getLoanContractChangeBefore().getBorrowerInfomation().getZJHM()); //变更前证件号码
            form.setField("JKRSJHM_1", result.getLoanContractChangeBefore().getBorrowerInfomation().getSJHM()); //变更前手机号码
            form.setField("JKRJTZZ_1", result.getLoanContractChangeBefore().getBorrowerInfomation().getJTZZ()); //变更前家庭地址

            form.setField("JKRXM_2", result.getLoanContractChangeAfter().getBorrowerInfomation().getXingMing()); //变更后姓名
            form.setField("JKRZJHM_2", result.getLoanContractChangeAfter().getBorrowerInfomation().getZJHM()); //变更后证件号码
            form.setField("JKRSJHM_2", result.getLoanContractChangeAfter().getBorrowerInfomation().getSJHM()); //变更后手机号码
            form.setField("JKRJTZZ_2", result.getLoanContractChangeAfter().getBorrowerInfomation().getJTZZ()); //变更后家庭地址
            // 共同借款人-变更前
            if (result.getLoanContractChangeBefore().getCommonBorrowerInfomation() != null) {
                form.setField("GTJKRXM_1", result.getLoanContractChangeBefore().getCommonBorrowerInfomation().getXingMing()); //姓名
                form.setField("GTJKRZJHM_1", result.getLoanContractChangeBefore().getCommonBorrowerInfomation().getZJHM()); //证件号码
                form.setField("GTJKRSJHM_1", result.getLoanContractChangeBefore().getCommonBorrowerInfomation().getSJHM()); //手机号码
                form.setField("GTJKRJTZZ_1", result.getLoanContractChangeBefore().getCommonBorrowerInfomation().getJTZZ()); //家庭地址
            }
            // 共同借款人-变更后
            if (result.getLoanContractChangeAfter().getCommonBorrowerInfomation() != null) {
                form.setField("GTJKRXM_2", result.getLoanContractChangeAfter().getCommonBorrowerInfomation().getXingMing()); //姓名
                form.setField("GTJKRZJHM_2", result.getLoanContractChangeAfter().getCommonBorrowerInfomation().getZJHM()); //证件号码
                form.setField("GTJKRSJHM_2", result.getLoanContractChangeAfter().getCommonBorrowerInfomation().getSJHM()); //手机号码
                form.setField("GTJKRJTZZ_2", result.getLoanContractChangeAfter().getCommonBorrowerInfomation().getJTZZ()); //家庭地址
            }
            if (result.getLoanContractChangeAfter().getLoanContractChangeQTXX() != null && result.getLoanContractChangeBefore().getLoanContractChangeQTXX() != null){
                form.setField("HKZH_1", result.getLoanContractChangeBefore().getLoanContractChangeQTXX().getHKZH_1());
                form.setField("KHYH_1", result.getLoanContractChangeBefore().getLoanContractChangeQTXX().getKHYH_1());
                form.setField("ZHHM_1", result.getLoanContractChangeBefore().getLoanContractChangeQTXX().getZHHM_1());
                form.setField("YJCE_1", result.getLoanContractChangeBefore().getLoanContractChangeQTXX().getYJCE_1());

                form.setField("HKZH_2", result.getLoanContractChangeAfter().getLoanContractChangeQTXX().getHKZH_1());
                form.setField("KHYH_2", result.getLoanContractChangeAfter().getLoanContractChangeQTXX().getKHYH_1());
                form.setField("ZHHM_2", result.getLoanContractChangeAfter().getLoanContractChangeQTXX().getZHHM_1());
                form.setField("YJCE_2", result.getLoanContractChangeAfter().getLoanContractChangeQTXX().getYJCE_1());
            }
            form.setField("CZY", result.getCZY()); //操作员
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 30, SealHelper.getContractSeal(), id);
        return id;
    }


    /*
     * 个人缴存明细head
     */
    public String getPersonDepositPdf(IndiAcctDepositDetails depositDetails) {
        ArrayList<GetIndiAcctDepositDetailsDep> lists = depositDetails.getList();
        Integer count = lists.size();
        String id = null;
        if (count >= 0 && count < 21) {
            id = getPersonDepositReceiptOnlyPage(depositDetails);
        } else {
            String templatePath = TEMPLATE + "PersonDepositTableTemp-0.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "PersonDepositTableTemp_head";
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/PersonDepositTableTemp_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                // form.setField("CiShu", "1");
                form.setField("XingMing", depositDetails.getGetIndiAcctDepositDetailsPerson().getXingMing());
                form.setField("GRZH", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRZH());
                form.setField("SFDJ", depositDetails.getGetIndiAcctDepositDetailsPerson().getSFDJ());
                form.setField("ZJLX", depositDetails.getGetIndiAcctDepositDetailsPerson().getZJLX());
                form.setField("ZJHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getZJHM());
                form.setField("YouXiang", depositDetails.getGetIndiAcctDepositDetailsPerson().getYouXiang());
                form.setField("SJHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getSJHM());
                form.setField("GRZHYE", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRZHYE());
                form.setField("GRCKZHHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRCKZHHM());
                form.setField("GRZHKHYHMC", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRCKZHKHYHMC());
                Integer num = 1;
                Iterator<GetIndiAcctDepositDetailsDep> iter = lists.iterator();
                while (iter.hasNext()) {
                    GetIndiAcctDepositDetailsDep list = iter.next();
                    form.setField("No" + num, num.toString());
                    form.setField("JZRQ" + num, list.getJZRQ());
                    form.setField("YWLX" + num, list.getYWLX());
                    form.setField("DWFSE" + num, list.getDWFSE());
                    form.setField("GRFSE" + num, list.getGRFSE());
                    form.setField("FSE" + num, list.getFSE());
                    form.setField("HJNY" + num, list.getHJNY());
                    form.setField("DWMC" + num, list.getDWMC());
                    form.setField("DWZH" + num, list.getDWZH());
                    form.setField("GRZHYE" + num, list.getGRZHYE());
                    iter.remove();
                    if (num == 21) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            if (count > 21) {
                FilePdfData filePdfData = getPersonDepositforeachTable(lists);
                String[] strArr = new String[(filePdfData.getFilePathArray().size() + 1)];
                for (int i = 0; i < strArr.length; i++) {
                    if (i == 0) {
                        strArr[i] = outfile_head;
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
                mergeMorePdf(strArr, outfile);
                id = iPdfServiceCa.addSignaturePdf(1, 400, 650, SealHelper.getContractSeal(), id);
                if (strArr.length > 1) {
                    id = iPdfServiceCa.addSignatureToIndexPdf(1, strArr.length, SealHelper.getContractSeal(), id);
                }
            }
        }
        return id;
    }

    /*
     * 个人缴存明细body
     */
    public FilePdfData getPersonDepositforeachTable(ArrayList<GetIndiAcctDepositDetailsDep> lists) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = lists.size();
        Integer fornum = (count / 28);
        Integer fo = (count % 28);
        if (fo != 0) {
            fornum = fornum + 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "PersonDepositTableTemp-1.pdf";
            System.out.println(templatePath);
            String outfilename = "PersonDepositTableTemp_body" + i;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/PersonDepositTableTemp_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Iterator<GetIndiAcctDepositDetailsDep> iter = lists.iterator();
                while (iter.hasNext()) {
                    int j = 21 + i * 28 + num;
                    GetIndiAcctDepositDetailsDep list = iter.next();
                    form.setField("No" + num, String.valueOf(j));
                    form.setField("JZRQ" + num, list.getJZRQ());
                    form.setField("YWLX" + num, list.getYWLX());
                    form.setField("DWFSE" + num, list.getDWFSE());
                    form.setField("GRFSE" + num, list.getGRFSE());
                    form.setField("FSE" + num, list.getFSE());
                    form.setField("HJNY" + num, list.getHJNY());
                    form.setField("DWMC" + num, list.getDWMC());
                    form.setField("DWZH" + num, list.getDWZH());
                    form.setField("GRZHYE" + num, list.getGRZHYE());
                    iter.remove();
                    if (num == 28) {
                        break;
                    }
                    num++;
                }
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }

    /*
     * 个人缴存明细仅一页模板
     */
    public String getPersonDepositReceiptOnlyPage(IndiAcctDepositDetails depositDetails) {
        ArrayList<GetIndiAcctDepositDetailsDep> lists = depositDetails.getList();
        String id = null;
        String templatePath = TEMPLATE + "PersonDepositTableTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "PersonDepositTableTemp_onlypage";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            // form.setField("CiShu", "1");
            form.setField("XingMing", depositDetails.getGetIndiAcctDepositDetailsPerson().getXingMing());
            form.setField("GRZH", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRZH());
            form.setField("SFDJ", depositDetails.getGetIndiAcctDepositDetailsPerson().getSFDJ());
            form.setField("ZJLX", depositDetails.getGetIndiAcctDepositDetailsPerson().getZJLX());
            form.setField("ZJHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getZJHM());
            form.setField("YouXiang", depositDetails.getGetIndiAcctDepositDetailsPerson().getYouXiang());
            form.setField("SJHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getSJHM());
            form.setField("GRZHYE", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRZHYE());
            form.setField("GRCKZHHM", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRCKZHHM());
            form.setField("GRZHKHYHMC", depositDetails.getGetIndiAcctDepositDetailsPerson().getGRCKZHKHYHMC());
            Integer num = 1;
            for (GetIndiAcctDepositDetailsDep list : lists) {
                form.setField("No" + num, num.toString());
                form.setField("JZRQ" + num, list.getJZRQ());
                form.setField("YWLX" + num, list.getYWLX());
                form.setField("DWFSE" + num, list.getDWFSE());
                form.setField("GRFSE" + num, list.getGRFSE());
                form.setField("FSE" + num, list.getFSE());
                form.setField("HJNY" + num, list.getHJNY());
                form.setField("DWMC" + num, list.getDWMC());
                form.setField("DWZH" + num, list.getDWZH());
                form.setField("GRZHYE" + num, list.getGRZHYE());
                num++;
            }
            form.setField("page", "1");// 页码
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 400, 650, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getSquareReceiptPdf(AccountSquarepdfInformation accountSquarepdfInformation) {
        String id = null;
        String templatePath = TEMPLATE + "SquareReceiptPdf.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "SquareReceiptPdf";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取贷款账户结清pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            int year = DateUtil.getYear(DateUtil.date2Str(new Date()));
            ;//年
            int month = DateUtil.getMonth(DateUtil.date2Str(new Date()));//月
            int day = DateUtil.getDay(DateUtil.date2Str(new Date()));
            form.setField("XinGMing", accountSquarepdfInformation.getXingMing());
            if (accountSquarepdfInformation.getJQR() != null) {
                form.setField("JQ_Year", String.valueOf(DateUtil.getYear(DateUtil.date2Str(accountSquarepdfInformation.getJQR()))));
                form.setField("JQ_Month", String.valueOf(DateUtil.getMonth(DateUtil.date2Str(accountSquarepdfInformation.getJQR()))));
                form.setField("JQ_Day", String.valueOf(DateUtil.getDay(DateUtil.date2Str(accountSquarepdfInformation.getJQR()))));
            }
            form.setField("ZJHM", accountSquarepdfInformation.getZJHM());
            form.setField("DKZH", accountSquarepdfInformation.getDKZH());
            form.setField("JKJE_DX", accountSquarepdfInformation.getJKJE_DX());
            form.setField("JKJE", accountSquarepdfInformation.getJKJE().toString());
            form.setField("JKQX", accountSquarepdfInformation.getJKQS().toString());
            if (accountSquarepdfInformation.getFKR() != null) {
                form.setField("Year", String.valueOf(DateUtil.getYear(DateUtil.date2Str(accountSquarepdfInformation.getFKR()))));
                form.setField("Month", String.valueOf(DateUtil.getMonth(DateUtil.date2Str(accountSquarepdfInformation.getFKR()))));
                form.setField("Day", String.valueOf(DateUtil.getDay(DateUtil.date2Str(accountSquarepdfInformation.getFKR()))));
            }
            form.setField("DY_Year", String.valueOf(year));
            form.setField("DY_Month", String.valueOf(month));
            form.setField("DY_Day", String.valueOf(day));
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 300, 300, SealHelper.getContractSeal(), id);
        return id;
    }

    public String getMergerVoucherMorePdf(ArrayList<VoucherManager> VoucherInfoArray) {
        String outfilename = "VoucherMorePdf";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        Integer PZHS = VoucherInfoArray.size();
        String outfile = BASEPATH + TMPPATH + "/" + id;
        Boolean allListSmaller = true;
        String code = null;
        for (int i = 0; i < PZHS; i++) {
            Integer n = VoucherInfoArray.get(i).getVoucherMangerLists().size();
            if (n > 5) {
                allListSmaller = false;
            }
        }
        if (allListSmaller) {
            Boolean JO = false;
            if (PZHS % 2 == 1) {//奇数
                JO = true;
            }
            if (JO) {
                if (PZHS == 1) {
                    id = VoucherOnlyA4(VoucherInfoArray.get(PZHS - 1), "1");
                } else {
                    String lastlyid = VoucherOnlyA4(VoucherInfoArray.get(PZHS - 1), "1");
                    for (int i = 0; i < PZHS; i++) {
                        if ((PZHS - 1) == i) {
                            VoucherInfoArray.remove(i);
                        }
                    }
                    Integer l = VoucherInfoArray.size() / 2;
                    String[] strArrA = new String[l + 1];
                    String[] strArr = VoucherOddPageA4(VoucherInfoArray);
                    for (int j = 0; j < strArr.length + 1; j++) {
                        if (j == strArr.length) {
                            strArrA[j] = BASEPATH + TMPPATH + "/" + lastlyid;
                        } else {
                            strArrA[j] = strArr[j];
                        }
                    }
                    mergeMorePdf(strArrA, outfile);
                }
            } else {
                String[] strArr = VoucherOddPageA4(VoucherInfoArray);
                mergeMorePdf(strArr, outfile);
            }
        } else {
            HashMap<String, VoucherManager> map = new HashMap<>();
            VoucherTransmissionData voucherTransmissionData = new VoucherTransmissionData();
            ArrayList allFilepath = new ArrayList();
            String lastFilepath = null;
            for (int i = 0; i < PZHS; i++) {
                Integer n = VoucherInfoArray.get(i).getVoucherMangerLists().size();
                if (i == 0) {
                    voucherTransmissionData = commVoucherfun(n, i, map, VoucherInfoArray);
                    map.put(Integer.toString(i), voucherTransmissionData.getDataMap().get(String.valueOf(i)));
                } else {
                    if (map.get(Integer.toString(i - 1)) != null) {
                        voucherTransmissionData = commVoucherfun(n - 5, i, map, VoucherInfoArray);
                    } else {
                        voucherTransmissionData = commVoucherfun(n, i, map, VoucherInfoArray);
                    }
                }

                if (voucherTransmissionData.getSavePath() != null) {
                    for (int f = 0; f < voucherTransmissionData.getSavePath().size(); f++) {
                        allFilepath.add(voucherTransmissionData.getSavePath().get(f));
                    }
                }
                if (i == PZHS - 1 && map.get(Integer.toString(i)) != null) {
                    //取最后一张模板
                    VoucherManager voucherManager = voucherTransmissionData.getDataMap().get(String.valueOf(i));
                    lastFilepath = BASEPATH + TMPPATH + "/" + VoucherOnlyA4(voucherManager, "1");
                    allFilepath.add(lastFilepath);
                }
            }
            Integer l = allFilepath.size();
            String[] strArr = new String[l];

            if (l > 0) {
                for (int k = 0; k < l; k++) {
                    String p = String.valueOf(allFilepath.get(k)).replace("[", "").replace("]", "");
                    strArr[k] = p;
                }
                mergeMorePdf(strArr, outfile);
            }
        }
        return id;
    }

    public String VoucherOnlyA4(VoucherManager voucherManager, String code) {
        List<VoucherMangerList> voucherMangerList = voucherManager.getVoucherMangerLists();
        Integer count = voucherMangerList.size();
        String templatePath = null;
        if (code == "1") {
            templatePath = TEMPLATE + "VoucherMorePdfOnly.pdf";
        } else {
            templatePath = TEMPLATE + "VoucherMorePdfDouble.pdf";
        }
        System.out.println(templatePath);
        String outfilename = "VoucherMorePdfOnly" + voucherManager.getJZPZH();
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("JZ_Year_1", String.valueOf(DateUtil.getYear(voucherManager.getJZRQ())));
            form.setField("JZ_Month_1", String.valueOf(DateUtil.getMonth(voucherManager.getJZRQ())));
            form.setField("JZ_Day_1", String.valueOf(DateUtil.getDay(voucherManager.getJZRQ())));
            form.setField("HSDW", voucherManager.getHSDW());
            form.setField("JZPZH_1", voucherManager.getJZPZH());
            form.setField("No_1", "1");
            form.setField("Num_1", "1");
            form.setField("SL_1", voucherManager.getFJSL());
            form.setField("CWZG_1", voucherManager.getCWZG());
            form.setField("JZ_1", voucherManager.getJiZhang());
            form.setField("FH_1", voucherManager.getFuHe());
            form.setField("CN_1", voucherManager.getChuNa());
            form.setField("ZD_1", voucherManager.getZhiDan());
            Integer num = 1;
            for (VoucherMangerList list : voucherMangerList) {
                form.setField("ZhaiYao_1_" + num, list.getZhaiYao());
                form.setField("KJKM_1_" + num, list.getKJKMKMMC());
                form.setField("JFJE_1_" + num, list.getJFJE());
                form.setField("DFJE_1_" + num, list.getDFJE());
                num++;
            }
            form.setField("JFZJ_1", String.valueOf(voucherManager.getHJJFJE()));
            form.setField("DFZJ_1", String.valueOf(voucherManager.getHJDFJE()));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            java.io.File out = new java.io.File(outfile);
//            if (!out.exists()){
//                out.mkdir();
//            }
            OutputStream fos = new FileOutputStream(out);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
//        id = iPdfServiceCa.addSignaturePdf(1, 150, 450, SealHelper.getFinancialSeal(), id);
        return id;
    }

    public String[] VoucherOddPageA4(ArrayList<VoucherManager> VoucherInfoArray) {//基数页
        Integer f = VoucherInfoArray.size() / 2;
        String id = null;
        String[] strArr = new String[f];
        for (int i = 0; i < f; i++) {
            String templatePath = TEMPLATE + "VoucherMorePdfDouble.pdf";
            System.out.println(templatePath);
            String outfilename = "VoucherDouble";
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("JZ_Year_1", String.valueOf(DateUtil.getYear(VoucherInfoArray.get(2 * i).getJZRQ())));
                form.setField("JZ_Month_1", String.valueOf(DateUtil.getMonth(VoucherInfoArray.get(2 * i).getJZRQ())));
                form.setField("JZ_Day_1", String.valueOf(DateUtil.getDay(VoucherInfoArray.get(2 * i).getJZRQ())));
                form.setField("HSDW_1", VoucherInfoArray.get(2 * i).getHSDW());
                form.setField("JZPZH_1", VoucherInfoArray.get(2 * i).getJZPZH());
                form.setField("No_1", "1");
                form.setField("Num_1", "1");
                form.setField("SL_1", VoucherInfoArray.get(2 * i).getFJSL());
                form.setField("CWZG_1", VoucherInfoArray.get(2 * i).getCWZG());
                form.setField("JZ_1", VoucherInfoArray.get(2 * i).getJiZhang());
                form.setField("FH_1", VoucherInfoArray.get(2 * i).getFuHe());
                form.setField("CN_1", VoucherInfoArray.get(2 * i).getChuNa());
                form.setField("ZD_1", VoucherInfoArray.get(2 * i).getZhiDan());
                form.setField("JZ_Year_2", String.valueOf(DateUtil.getYear(VoucherInfoArray.get(2 * i + 1).getJZRQ())));
                form.setField("JZ_Month_2", String.valueOf(DateUtil.getMonth(VoucherInfoArray.get(2 * i + 1).getJZRQ())));
                form.setField("JZ_Day_2", String.valueOf(DateUtil.getDay(VoucherInfoArray.get(2 * i + 1).getJZRQ())));
                form.setField("HSDW_2", VoucherInfoArray.get(2 * i + 1).getHSDW());
                form.setField("JZPZH_2", VoucherInfoArray.get(2 * i + 1).getJZPZH());
                form.setField("No_2", "1");
                form.setField("Num_2", "1");
                form.setField("SL_2", VoucherInfoArray.get(2 * i + 1).getFJSL());
                form.setField("CWZG_2", VoucherInfoArray.get(2 * i + 1).getCWZG());
                form.setField("JZ_2", VoucherInfoArray.get(2 * i + 1).getJiZhang());
                form.setField("FH_2", VoucherInfoArray.get(2 * i + 1).getFuHe());
                form.setField("CN_2", VoucherInfoArray.get(2 * i + 1).getChuNa());
                form.setField("ZD_2", VoucherInfoArray.get(2 * i + 1).getZhiDan());
                Integer num = 1;
                Integer num2 = 1;
                for (VoucherMangerList list : VoucherInfoArray.get(2 * i).getVoucherMangerLists()) {
                    form.setField("ZhaiYao_1_" + num, list.getZhaiYao());
                    form.setField("KJKM_1_" + num, list.getKJKMKMMC());
                    form.setField("JFJE_1_" + num, list.getJFJE());
                    form.setField("DFJE_1_" + num, list.getDFJE());
                    num++;
                }
                for (VoucherMangerList list : VoucherInfoArray.get(2 * i + 1).getVoucherMangerLists()) {
                    form.setField("ZhaiYao_2_" + num2, list.getZhaiYao());
                    form.setField("KJKM_2_" + num2, list.getKJKMKMMC());
                    form.setField("JFJE_2_" + num2, list.getJFJE());
                    form.setField("DFJE_2_" + num2, list.getDFJE());
                    num2++;
                }
                form.setField("JFZJ_1", String.valueOf(VoucherInfoArray.get(2 * i).getHJJFJE()));
                form.setField("DFZJ_1", String.valueOf(VoucherInfoArray.get(2 * i).getHJDFJE()));
                form.setField("JFZJ_2", String.valueOf(VoucherInfoArray.get(2 * i + 1).getHJJFJE()));
                form.setField("DFZJ_2", String.valueOf(VoucherInfoArray.get(2 * i + 1).getHJDFJE()));
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                java.io.File out = new java.io.File(outfile);
//            if (!out.exists()){
//                out.mkdir();
//            }
                OutputStream fos = new FileOutputStream(out);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 450, SealHelper.getFinancialSeal(), id);
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 10, SealHelper.getFinancialSeal(), id);
            strArr[i] = outfile;
        }
        return strArr;
    }

    public ArrayList VoucherSamePZHA4(VoucherManager voucherManager) {
        Integer f = voucherManager.getVoucherMangerLists().size() / 10;
        if (f < 1) {
            f = 1;
        }
        String id = null;
        HashMap<String, Object> hashMap = new HashMap<>();
        ArrayList pathArr = new ArrayList();
        for (int i = 0; i < f; i++) {
            String templatePath = TEMPLATE + "VoucherMorePdfDouble.pdf";
            System.out.println(templatePath);
            String outfilename = "VoucherDouble";
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("JZ_Year_1", String.valueOf(DateUtil.getYear(voucherManager.getJZRQ())));
                form.setField("JZ_Month_1", String.valueOf(DateUtil.getMonth(voucherManager.getJZRQ())));
                form.setField("JZ_Day_1", String.valueOf(DateUtil.getDay(voucherManager.getJZRQ())));
                form.setField("HSDW_1", voucherManager.getHSDW());
                form.setField("JZPZH_1", voucherManager.getJZPZH());
                form.setField("No_1", "1");
                form.setField("Num_1", "1");
                form.setField("SL_1", voucherManager.getFJSL());
                form.setField("CWZG_1", voucherManager.getCWZG());
                form.setField("JZ_1", voucherManager.getJiZhang());
                form.setField("FH_1", voucherManager.getFuHe());
                form.setField("CN_1", voucherManager.getChuNa());
                form.setField("ZD_1", voucherManager.getZhiDan());
                form.setField("JZ_Year_2", String.valueOf(DateUtil.getYear(voucherManager.getJZRQ())));
                form.setField("JZ_Month_2", String.valueOf(DateUtil.getMonth(voucherManager.getJZRQ())));
                form.setField("JZ_Day_2", String.valueOf(DateUtil.getDay(voucherManager.getJZRQ())));
                form.setField("HSDW_2", voucherManager.getHSDW());
                form.setField("JZPZH_2", voucherManager.getJZPZH());
                form.setField("No_2", "1");
                form.setField("Num_2", "1");
                form.setField("SL_2", voucherManager.getFJSL());
                form.setField("CWZG_2", voucherManager.getCWZG());
                form.setField("JZ_2", voucherManager.getJiZhang());
                form.setField("FH_2", voucherManager.getFuHe());
                form.setField("CN_2", voucherManager.getChuNa());
                form.setField("ZD_2", voucherManager.getZhiDan());
                Integer num = 1;
                Integer num2 = 1;
                Iterator<VoucherMangerList> iter = voucherManager.getVoucherMangerLists().iterator();
                while (iter.hasNext()) {
                    VoucherMangerList list = iter.next();
                    form.setField("ZhaiYao_1_" + num, list.getZhaiYao());
                    form.setField("KJKM_1_" + num, list.getKJKMKMMC());
                    form.setField("JFJE_1_" + num, list.getJFJE());
                    form.setField("DFJE_1_" + num, list.getDFJE());
                    if (num > 5) {
                        form.setField("ZhaiYao_2_" + num2, list.getZhaiYao());
                        form.setField("KJKM_2_" + num2, list.getKJKMKMMC());
                        form.setField("JFJE_2_" + num2, list.getJFJE());
                        form.setField("DFJE_2_" + num2, list.getDFJE());
                        num2++;
                    }
                    num++;
                    iter.remove();
                }
//                  form.setField("JFZJ_1", String.valueOf(voucherManager.getHJJFJE()));
//                  form.setField("DFZJ_1", String.valueOf(voucherManager.getHJDFJE()));
                if (f - 1 == i) {
                    form.setField("JFZJ_2", String.valueOf(voucherManager.getHJJFJE()));
                    form.setField("DFZJ_2", String.valueOf(voucherManager.getHJDFJE()));
                }
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                java.io.File out = new java.io.File(outfile);

                OutputStream fos = new FileOutputStream(out);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 450, SealHelper.getFinancialSeal(), id);
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 10, SealHelper.getFinancialSeal(), id);
            pathArr.add(outfile);
        }
        return pathArr;
    }

    public VoucherTransmissionData commVoucherfun(int n, int i, HashMap map, ArrayList<VoucherManager> VoucherInfoArray) {
        VoucherTransmissionData voucherTransmissionData = new VoucherTransmissionData();
        ArrayList savePath = new ArrayList();
        if (n <= 5) {
            if (map.get(Integer.toString(i - 1)) != null) {
                String id = VoucherPageA4Only(VoucherInfoArray, i);
                map.put(Integer.toString(i - 1), null);
                if (n > 0) {
                    map.put(Integer.toString(i), VoucherInfoArray.get(i));
                } else {
                    map.put(Integer.toString(i), null);
                }
                savePath.add(id);
            } else {
                map.put(Integer.toString(i), VoucherInfoArray.get(i));
            }
        } else if (n > 5 && n <= 10) {
            savePath.add(VoucherSamePZHA4(VoucherInfoArray.get(i)));
        } else if (n > 10) {
            int f = n / 10;
            savePath = VoucherSamePZHA4(VoucherInfoArray.get(i));
            if (f % 10 != 0) {
                map.put(Integer.toString(i), VoucherInfoArray.get(i));
            }
        }
        voucherTransmissionData.setSavePath(savePath);
        voucherTransmissionData.setDataMap(map);

        return voucherTransmissionData;
    }

    public String VoucherPageA4Only(ArrayList<VoucherManager> VoucherInfoArray, int i) {
        String id = null;
        String templatePath = TEMPLATE + "VoucherMorePdfDouble.pdf";
        System.out.println(templatePath);
        String outfilename = "VoucherDouble";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("JZ_Year_1", String.valueOf(DateUtil.getYear(VoucherInfoArray.get(i - 1).getJZRQ())));
            form.setField("JZ_Month_1", String.valueOf(DateUtil.getMonth(VoucherInfoArray.get(i - 1).getJZRQ())));
            form.setField("JZ_Day_1", String.valueOf(DateUtil.getDay(VoucherInfoArray.get(i - 1).getJZRQ())));
            form.setField("HSDW_1", VoucherInfoArray.get(i - 1).getHSDW());
            form.setField("JZPZH_1", VoucherInfoArray.get(i - 1).getJZPZH());
            form.setField("No_1", "1");
            form.setField("Num_1", "1");
            form.setField("SL_1", VoucherInfoArray.get(i - 1).getFJSL());
            form.setField("CWZG_1", VoucherInfoArray.get(i - 1).getCWZG());
            form.setField("JZ_1", VoucherInfoArray.get(i - 1).getJiZhang());
            form.setField("FH_1", VoucherInfoArray.get(i - 1).getFuHe());
            form.setField("CN_1", VoucherInfoArray.get(i - 1).getChuNa());
            form.setField("ZD_1", VoucherInfoArray.get(i - 1).getZhiDan());
            form.setField("JZ_Year_2", String.valueOf(DateUtil.getYear(VoucherInfoArray.get(i).getJZRQ())));
            form.setField("JZ_Month_2", String.valueOf(DateUtil.getMonth(VoucherInfoArray.get(i).getJZRQ())));
            form.setField("JZ_Day_2", String.valueOf(DateUtil.getDay(VoucherInfoArray.get(i).getJZRQ())));
            form.setField("HSDW_2", VoucherInfoArray.get(i).getHSDW());
            form.setField("JZPZH_2", VoucherInfoArray.get(i).getJZPZH());
            form.setField("No_2", "1");
            form.setField("Num_2", "1");
            form.setField("SL_2", VoucherInfoArray.get(i).getFJSL());
            form.setField("CWZG_2", VoucherInfoArray.get(i).getCWZG());
            form.setField("JZ_2", VoucherInfoArray.get(i).getJiZhang());
            form.setField("FH_2", VoucherInfoArray.get(i).getFuHe());
            form.setField("CN_2", VoucherInfoArray.get(i).getChuNa());
            form.setField("ZD_2", VoucherInfoArray.get(i).getZhiDan());
            Integer num = 1;
            Integer num2 = 1;
            Iterator<VoucherMangerList> iter1 = VoucherInfoArray.get(i - 1).getVoucherMangerLists().iterator();
            Iterator<VoucherMangerList> iter2 = VoucherInfoArray.get(i).getVoucherMangerLists().iterator();
            while (iter1.hasNext()) {
                VoucherMangerList list1 = iter1.next();
                form.setField("ZhaiYao_1_" + num, list1.getZhaiYao());
                form.setField("KJKM_1_" + num, list1.getKJKMKMMC());
                form.setField("JFJE_1_" + num, list1.getJFJE());
                form.setField("DFJE_1_" + num, list1.getDFJE());
                iter1.remove();
                if (num == 5) {
                    break;
                }
                num++;
            }
            while (iter2.hasNext()) {
                VoucherMangerList list2 = iter2.next();
                form.setField("ZhaiYao_2_" + num2, list2.getZhaiYao());
                form.setField("KJKM_2_" + num2, list2.getKJKMKMMC());
                form.setField("JFJE_2_" + num2, list2.getJFJE());
                form.setField("DFJE_2_" + num2, list2.getDFJE());
                iter2.remove();
                if (num2 == 5) {
                    break;
                }
                num2++;
            }
            form.setField("JFZJ_1", String.valueOf(VoucherInfoArray.get(i - 1).getHJJFJE()));
            form.setField("DFZJ_1", String.valueOf(VoucherInfoArray.get(i - 1).getHJDFJE()));
            form.setField("JFZJ_2", String.valueOf(VoucherInfoArray.get(i).getHJJFJE()));
            form.setField("DFZJ_2", String.valueOf(VoucherInfoArray.get(i).getHJDFJE()));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            java.io.File out = new java.io.File(outfile);
//            if (!out.exists()){
//                out.mkdir();
//            }
            OutputStream fos = new FileOutputStream(out);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 450, SealHelper.getFinancialSeal(), id);
//           id = iPdfServiceCa.addSignaturePdf(1, 150, 10, SealHelper.getFinancialSeal(), id);

        return outfile;
    }
    /*
     * 还款计划head
     */
    public String  getHousingfundPlanPdf(HousingfundAccountPlanGet planGet){
        LinkedList<HousingfundAccountPlanGetInformation> information = planGet.getinformation();
        Integer count = information.size();
        String id = null;
        if (count >= 0 && count < 26) {
            id = getPlanPdfOnlyPage(planGet);
        } else {
            String templatePath = TEMPLATE + "RepaymentPlanTemp-0.pdf";// 模板路径
            System.out.println(templatePath);
            String outfilename = "RepaymentPlanTemp_head";
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile_head = BASEPATH + TMPPATH + "/RepaymentPlanTemp_head" + id;
            String outfile = BASEPATH + TMPPATH + "/" + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                form.setField("JKRXM",planGet.getJKRXM());
                form.setField("JKRZJLX", "身份证");
                form.setField("JKRZJHM", planGet.getJKRZJHM());
                form.setField("DKZH", planGet.getDKZH());
                form.setField("YZM", id);
                form.setField("YWWD", planGet.getYWWD());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                form.setField("DYRQ", sdf.format(now));
                Integer num = 1;
                for(int i=0;i<count;i++){
                    form.setField("QC_" + num, information.get(i).getHKQC());
                    form.setField("HKRQ_" + num, information.get(i).getHKRQ());
                    form.setField("FSE_" + num, information.get(i).getFSE());
                    form.setField("HKBJJE_" + num, information.get(i).getHKBJJE());
                    form.setField("HKLXJE_" + num, information.get(i).getHKLXJE());
                    form.setField("DKYE_" + num, information.get(i).getDKYE());
                    if (num == 25) {
                        break;
                    }
                    num++;
                }
                form.setField("page", "1");// 页码
                // form.setField("SHR",null);
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile_head);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile_head, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile_head));
                fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            if (count > 25) {
                for(int l=0;l<25;l++){
                    information.remove(information.get(24-l));
                }
                FilePdfData filePdfData = getPlanforeachTable(information);
                String[] strArr = new String[(filePdfData.getFilePathArray().size() + 1)];
                for (int i = 0; i < strArr.length; i++) {
                    if (i == 0) {
                        strArr[i] = outfile_head;
                    } else {
                        strArr[i] = filePdfData.getFilePathArray().get(i - 1);
                    }
                }
                mergeMorePdf(strArr, outfile);
            }
        }
        id = iPdfServiceCa.addSignaturePdf(1, 220, 680, SealHelper.getContractSeal(), id);
        return id;
    }
    public String getPlanPdfOnlyPage(HousingfundAccountPlanGet planGet){
        LinkedList<HousingfundAccountPlanGetInformation> information = planGet.getinformation();
        String id = null;
        Integer count = information.size();
        String templatePath = TEMPLATE + "RepaymentPlanTemp-0.pdf";// 模板路径
        System.out.println(templatePath);
        String outfilename = "RepaymentPlanTemp_head";
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            form.setField("JKRXM",planGet.getJKRXM());
            form.setField("JKRZJLX", "身份证");
            form.setField("JKRZJHM", planGet.getJKRZJHM());
            form.setField("DKZH", planGet.getDKZH());
            form.setField("YZM", id);
            form.setField("YWWD", planGet.getYWWD());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            form.setField("DYRQ", sdf.format(now));
            Integer num = 1;
            for(int i=0;i<count;i++){
                form.setField("QC_" + num, information.get(i).getHKQC());
                form.setField("HKRQ_" + num, information.get(i).getHKRQ());
                form.setField("FSE_" + num, information.get(i).getFSE());
                form.setField("HKBJJE_" + num, information.get(i).getHKBJJE());
                form.setField("HKLXJE_" + num, information.get(i).getHKLXJE());
                form.setField("DKYE_" + num, information.get(i).getDKYE());
                num++;
            }
            form.setField("page", "1");// 页码
            // form.setField("SHR",null);
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        return id;
    }
    /*
     * 还款计划body
     */
    public FilePdfData getPlanforeachTable(LinkedList<HousingfundAccountPlanGetInformation> information) {
        ArrayList<String> filePathArray = new ArrayList<>();
        Integer count = information.size();
        Integer fornum = (count / 33);
        Integer fo = (count % 33);
        if (fo != 0) {
            fornum = fornum + 1;
        }
        for (int i = 0; i < fornum; i++) {
            String templatePath = TEMPLATE + "RepaymentPlanTemp-1.pdf";
            System.out.println(templatePath);
            String outfilename = "RepaymentPlanTemp_body" + i;
            File file = new File();
            file.setCount(new BigDecimal(0));
            file.setName(outfilename);
            file.setPath(TMPPATH);
            file.setType(FileType.PDF);
            file.setSize(new BigDecimal(0));
            String id = fileService.saveFile(file);
            file = fileService.getFileMetadata(id);
            String outfile = BASEPATH + TMPPATH + "/RepaymentPlanTemp_body" + i + id;
            // 生成的新文件路径
            PdfReader reader;
            ByteArrayOutputStream bos;
            PdfStamper stamper;
            try {
                reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
                bos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, bos);
                AcroFields form = stamper.getAcroFields();
                form.addSubstitutionFont(bfChinese);
                Integer num = 1;
                Boolean h = false;
                for(int k=0;k<count;k++){
                    int j = 25 + i * 33 + num;
                    form.setField("QC_" + num,Integer.toString(j));
                    form.setField("HKRQ_" + num, information.get(k).getHKRQ());
                    form.setField("FSE_" + num, information.get(k).getFSE());
                    form.setField("HKBJJE_" + num, information.get(k).getHKBJJE());
                    form.setField("HKLXJE_" + num, information.get(k).getHKLXJE());
                    form.setField("DKYE_" + num, information.get(k).getDKYE());
                    if (num == 33) {
                        h = true;
                        break;
                    }
                    num++;
                }
                int f = 0;
                int t = 0;
                if(num==33 && h){
                     f = 33;
                     t = 32;
                }else{
                     f = num-1;
                     t = f-1;
                }
                for(int l=0;l<f;l++){
                    information.remove(information.get(t-l));
                }
                count = information.size();
                form.setField("page", String.valueOf(i + 2));// 页码
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();
                OutputStream fos = new FileOutputStream(outfile);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
                bos.close();
                file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
                file.setSize(FileUtil.getFileSize(outfile));
                //fileService.updateFile(file);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ErrorException() {
                    {
                        this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                        this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                    }
                };
            }
            filePathArray.add(outfile);
        }
        FilePdfData filePdfData = new FilePdfData();
        filePdfData.setFilePathArray(filePathArray);
        return filePdfData;
    }
    public String getDiffTerritoryLoadProvePdf(ForeignLoanProof foreignLoanProof) {
        // 模板路径
        String templatePath = TEMPLATE + "DiffTerritoryLoadProve.pdf";
        System.out.println(templatePath);
        String outfilename = "DiffTerritoryLoadProve" ;
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfilename);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            setPrintManyTimes("yddkjczm");
            SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
            form.setField("BianHao", ymd.format(now)+getPrintManyTimes("yddkjczm"));
            // form.setField("CiShu", "1");

            form.setField("TZRQ", sdf.format(now));
            // 个人信息
            form.setField("GJJZX", "毕节市住房公积金中心");
            form.setField("XingMing", foreignLoanProof.getZGXM());
            form.setField("SFZH", foreignLoanProof.getSFZH());
            form.setField("GJJZH", foreignLoanProof.getGJJZH());
            if(foreignLoanProof.getKHSJ()!=null && !foreignLoanProof.getKHSJ().equals("")){
                form.setField("KH_Year", String.valueOf(DateUtil.getYear(foreignLoanProof.getKHSJ())));
                form.setField("KH_Month", String.valueOf(DateUtil.getMonth(foreignLoanProof.getKHSJ())));
            }
            Boolean OnTrue_1 = false;
            Boolean OnTrue_2 = false;
            Boolean OnTrue_3 = false;
            Boolean OnTrue_4 = false;
            Boolean OnTrue_5 = false;

            if(PersonAccountStatus.正常.getCode().equals(foreignLoanProof.getZHZT())){
                OnTrue_1 = true;
            }
            createCheckbox(stamper, 355, 630, 365, 640, "ZHZT_1", true,OnTrue_1);
            if(PersonAccountStatus.封存.getCode().equals(foreignLoanProof.getZHZT())){
                OnTrue_2 = true;
            }
            createCheckbox(stamper, 395, 630, 405, 640, "ZHZT_2", true,OnTrue_2);
            if(PersonAccountStatus.其他.getCode().equals(foreignLoanProof.getZHZT())){
                OnTrue_3 = true;
            }
            createCheckbox(stamper, 435, 630, 445, 640, "ZHZT_3", true,OnTrue_3);
            createCheckbox(stamper, 475, 630, 485, 640, "ZHZT_4", true,false);
            form.setField("GRJCJS", foreignLoanProof.getJCJS());
            form.setField("DWJCBL", foreignLoanProof.getDWJCBL());
            form.setField("GRJCBL", foreignLoanProof.getGRJCBL());
            form.setField("YJCE", foreignLoanProof.getYJCE());
            form.setField("JCYE", foreignLoanProof.getJCYE());
            if(foreignLoanProof.getLXJCKS()!=null && !foreignLoanProof.getLXJCKS().equals("")){
                form.setField("LXJC_Year_1", String.valueOf(DateUtil.getYear(foreignLoanProof.getLXJCKS())));
                form.setField("LXJC_Month_1", String.valueOf(DateUtil.getMonth(foreignLoanProof.getLXJCKS())));
            }
            if(foreignLoanProof.getLXJCJS()!=null && !foreignLoanProof.getLXJCJS().equals("")){
                form.setField("LXJC_Year_2", String.valueOf(DateUtil.getYear(foreignLoanProof.getLXJCJS())));
                form.setField("LXJC_Month_2", String.valueOf(DateUtil.getMonth(foreignLoanProof.getLXJCJS())));
            }
            if(foreignLoanProof.getSFDK().equals("0")){
                OnTrue_5 = true;
            }
            if(foreignLoanProof.getSFDK().equals("1")){
                OnTrue_4 = true;
            }
            createCheckbox(stamper, 365, 535, 375, 545, "SFYDK_yes", true,OnTrue_4);
            createCheckbox(stamper, 425, 535, 435, 545, "SFYDK_no", true,OnTrue_5);

            form.setField("DKCS", foreignLoanProof.getDKCS());
            form.setField("LJDKJE", foreignLoanProof.getLJJE());
            form.setField("WQCDKYE", foreignLoanProof.getWQCDKYE());
            form.setField("CZY", foreignLoanProof.getDWJBR());
            form.setField("LXDH", foreignLoanProof.getLXDH());
            form.setField("DY_Year", String.valueOf(DateUtil.getYear(sdf.format(now))));
            form.setField("DY_Month", String.valueOf(DateUtil.getMonth(sdf.format(now))));
            form.setField("DY_Day", String.valueOf(DateUtil.getDay(sdf.format(now))));
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }
        id = iPdfServiceCa.addSignaturePdf(1, 350, 365, SealHelper.getContractSeal(), id);
        id = iPdfServiceCa.addSignaturePdf(1, 200, 300, SealHelper.getContractSeal(), id);

        return id;
    }

    @Override
    public String getEntrustDeductPdf(EntrustDeductInfos entrustDeductInfos) {
        // 模板路径
        String templatePath = TEMPLATE + "LoanEntrustDeduct.pdf";
        System.out.println(templatePath);
        String outfileName = "LoanEntrustDeduct" ;
        File file = new File();
        file.setCount(new BigDecimal(0));
        file.setName(outfileName);
        file.setPath(TMPPATH);
        file.setType(FileType.PDF);
        file.setSize(new BigDecimal(0));
        String id = fileService.saveFile(file);
        file = fileService.getFileMetadata(id);
        String outfile = BASEPATH + TMPPATH + "/" + id;
        // 生成的新文件路径
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取个人账户变更pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bfChinese);

            form.setField("JKRXM", entrustDeductInfos.getWTRXM());
            form.setField("GRZH", entrustDeductInfos.getGRZFGJJZH());
            form.setField("SFZH", entrustDeductInfos.getSFZH());
            form.setField("JKHTBH", entrustDeductInfos.getJKHTBH());
            form.setField("DKZH", entrustDeductInfos.getDKZH());
            form.setField("GTJKRXM_1", entrustDeductInfos.getGTJKRXM_1());
            form.setField("GTJKR_GRZH_1", entrustDeductInfos.getGTJKRGJJZH_1());
            form.setField("GTJKR_SFZH_1", entrustDeductInfos.getGTJKRSFZH_1());
            form.setField("GTJKRXM_2", entrustDeductInfos.getGTJKRXM_2());
            form.setField("GTJKR_GRZH_2", entrustDeductInfos.getGTJKRGJJZH_2());
            form.setField("GTJKR_SFZH_2", entrustDeductInfos.getGTJKRSFZH_2());
            form.setField("YWWD", entrustDeductInfos.getSWTYWWD());

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            OutputStream fos = new FileOutputStream(outfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
            file.setSHA1(FileUtil.getCheckCode(outfile, "SHA-1"));
            file.setSize(FileUtil.getFileSize(outfile));
            fileService.updateFile(file);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorException() {
                {
                    this.setCode(ReturnEnumeration.Program_UNKNOW_ERROR.getCode());
                    this.setMsg(ReturnEnumeration.Program_UNKNOW_ERROR.getMessage() + e.getMessage());
                }
            };
        }

        id = iPdfServiceCa.addSignaturePdf(2, 370, 200, SealHelper.getContractSeal(), id);

        return id;
    }
}

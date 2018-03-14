package com.handge.housingfund.others.service;

import com.edao.dss.EdaoSDK;
import com.edao.ss.std.EdaoStdConfig;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.ca.CaService;
import com.handge.housingfund.common.service.ca.model.UnitInfo;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.SearchOption;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by tanyi on 2017/6/23.
 */

public class CaServiceImpl implements CaService {
    @Autowired
    IStCommonUnitDAO commonUnitDAO;


    /**
     * 对服务器访问的一些设置
     */
    public static EdaoStdConfig edaoConfig = new EdaoStdConfig() {
        @Override
        public String getBaseServerUrl() {
            Configuration configuration = Configure.getInstance().getConfiguration("caservice");
            String url = configuration.getString("CAService");
            System.out.println("caservice:" + url);
            return url;//CA认证服务器地址
        }
    };
    EdaoSDK edaoSDK = null;
    String certName = "szca";
    // RSA, SM2
    String asymmAlg = "SM2";
    String digestAlg = "SM3";
    boolean setAsymmAlg = false;

    public CaServiceImpl() {
        init();
        // System.out.print("init");
    }

    public void init() {
        // 初始化SDK
        edaoSDK = new EdaoSDK(edaoConfig);
        edaoSDK.getPropConfig().setAsymmAlg(asymmAlg);
        edaoSDK.getPropConfig().setDigestAlg(digestAlg);
    }

    public boolean VerifySignP1(String CertBase64, String indata, String SignDate) {
        try {
            // 验证证书有效性
            boolean cert = this.edaoSDK.SOF_ValidateCert(CertBase64);
            if (!cert) {
                System.out.println("证书验证不通过!");
                return false;
            }
            // 验证签名
            boolean result1 = this.edaoSDK.SOF_VerifySignedData(CertBase64, indata, SignDate);
            if (!result1) {
                System.out.println("签名验证不通过!");
                return false;
            }
            return true;

        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public boolean VerifySignP7(String CertBase64, String indata, String SignDate) {
        try {
            boolean cert = this.edaoSDK.SOF_ValidateCert(CertBase64);
            if (!cert) {
                System.out.println("证书验证不通过!");
                return false;
            }
            // 验证签名
            boolean result1 = this.edaoSDK.SOF_VerifyDetachSignedDataByP7(indata, SignDate);
            if (!result1) {
                System.out.println("签名验证不通过!");
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    // 随机数产生
    public static String Random(int RandomSize) {
        String serverRandom = "";
        Random rdm = new Random(System.currentTimeMillis());

        for (int i = 0; i < RandomSize / 2; i++) {
            String hex = Integer.toHexString(Math.abs(rdm.nextInt()) & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            serverRandom += hex.toUpperCase();
        }
        return serverRandom;
    }

    // 对称加密文件
    // certAilas：加密标识 inFile：输入文件路径
    public boolean EncryptFile(String certAlias, String inFile, String outFile) {

        boolean encryptdate = false;
        try {

            // 设置对称加密算法
            // edaoSDK.SOF_SetEncryptMethod(asymmAlg);

            // certAilas：加密标识 inFile：输入文件路径
            encryptdate = this.edaoSDK.SOF_EncryptFile(certAlias, inFile, outFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new ErrorException(e);
        }
        return encryptdate;
    }

    public UnitInfo GetGJJCompanyInfo(String dwgjjzh) {
        try {
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("dwzh", dwgjjzh);
            List<StCommonUnit> data = commonUnitDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED,
                    SearchOption.REFINED);
            if (data.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位不存在");
            }
            StCommonUnit com = data.get(0);
            UnitInfo info = new UnitInfo();
            info.setDwdjrq(DateUtil.date2Str(com.getDwkhrq(), "yyyy年MM月dd日"));// DWKHRQ
            info.setDwdz(com.getDwdz());// DWDZ
            info.setDwgjjzh(com.getDwzh());// DWZH
            info.setDwlxrdh(com.getJbrsjhm());// JBRSJHM
            info.setDwmc(com.getDwmc());// DWMC
            info.setDwlxrxm(com.getJbrxm());// JBRXM
            info.setDwzzjgdm(com.getZzjgdm());// ZZJGDM
            info.setDwlxrzjhm(com.getJbrzjhm());// JBRZJHM
            return info;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }
}

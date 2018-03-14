package com.handge.housingfund.others.webservice;

import com.google.gson.Gson;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.others.IFileService;
import com.handge.housingfund.common.service.others.IPdfServiceCa;
import com.handge.housingfund.common.service.others.model.File;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.FileUtil;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.others.webservice.pdfca.ISignatureService;
import com.handge.housingfund.others.webservice.pdfca.ISignatureServiceService;
import com.handge.housingfund.others.webservice.pdfca.Stamp;
import com.handge.housingfund.others.webservice.pdfca.StampPattern;
import com.handge.housingfund.others.webservice.utils.ClientHandler;
import com.handge.housingfund.others.webservice.utils.StreamConvertUtil;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tanyi on 2017/12/1.
 */
@Component
public class PdfServiceCa implements IPdfServiceCa {

    private ISignatureServiceService server = new ISignatureServiceService();

    private ISignatureService signatureService;

    @Autowired
    private IFileService fileService;

    public static Gson gson = new Gson();

    private static Configuration config = Configure.getInstance().getConfiguration("pdf");
    private String BASEPATH = config.getString("file_path");

    public PdfServiceCa() {
        //向SOAP添加表头
        server.setHandlerResolver(new HandlerResolver() {
            public List<Handler> getHandlerChain(javax.xml.ws.handler.PortInfo portInfo) {
                List<Handler> handlerList = new ArrayList<Handler>();
                handlerList.add(new ClientHandler());
                return handlerList;
            }
        });
        signatureService = server.getISignatureServicePort();
    }

    @Override
    public String addSignaturePdf(int pageNum, float x, float y, String signerId, String id) {
        int[] pageNums = {pageNum};
        float[] xs = {x};
        float[] ys = {y};
        String[] signerIds = {signerId};
        List<Stamp> list = getStamp(pageNums, xs, ys, signerIds);
        return addSignaturePdfBase(list, id);
    }

    @Override
    public String addSignaturePdf(int[] pageNums, float x, float y, String signerId, String id) {
        float[] xs = {x};
        float[] ys = {y};
        String[] signerIds = {signerId};
        List<Stamp> list = getStamp(pageNums, xs, ys, signerIds);
        return addSignaturePdfBase(list, id);
    }

    @Override
    public String addSignaturePdf(int[] pageNums, float[] xs, float[] ys, String signerId, String id) {
        String[] signerIds = {signerId};
        List<Stamp> list = getStamp(pageNums, xs, ys, signerIds);
        return addSignaturePdfBase(list, id);
    }

    @Override
    public String addSignaturePdf(int[] pageNums, float x, float y, String[] signerIds, String id) {
        float[] xs = {x};
        float[] ys = {y};
        List<Stamp> list = getStamp(pageNums, xs, ys, signerIds);
        return addSignaturePdfBase(list, id);
    }

    @Override
    public String addSignaturePdf(int[] pageNums, float[] xs, float[] ys, String[] signerIds, String id) {
        List<Stamp> list = getStamp(pageNums, xs, ys, signerIds);
        return addSignaturePdfBase(list, id);
    }

    @Override
    public String addSignatureToIndexPdf(int startPage, int endPage, String signerId, String id) {
        String[] signerIds = {signerId};
        List<StampPattern> list = getStampPattern(startPage, endPage, signerIds);
        return addSignaturePdfToIndexBase(list, id);
    }

    @Override
    public String addSignatureToIndexPdf(int startPage, int endPage, String[] signerIds, String id) {
        List<StampPattern> list = getStampPattern(startPage, endPage, signerIds);
        return addSignaturePdfToIndexBase(list, id);
    }


    /**
     * 签章
     *
     * @param list
     * @param id
     * @return
     */
    private String addSignaturePdfBase(List<Stamp> list, String id) {
//        return id;
        if (!StringUtil.notEmpty(id)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "签章文件id不能为空");
        }
        try {
            File oldFile = fileService.getFileMetadata(id);
            String table = oldFile.getId();
            String oldfile = BASEPATH + oldFile.getPath() + "/" + table;
            String newfilename = oldFile.getName() + "_ca";

            String base64file = StreamConvertUtil.base64Encode(StreamConvertUtil.file2bytes(oldfile));

            String result = signatureService.pdfSignature(base64file, newfilename, list);

            Map content = gson.fromJson(result, Map.class);

            if (content.containsKey("rtnCode") && (Double) content.get("rtnCode") == 1) {
                FileUtil.delete(oldfile);
                StreamConvertUtil.bytes2file(StreamConvertUtil.base64Decode((String) content.get("signed")), oldfile);
                oldFile.setName(newfilename);
                oldFile.setSHA1(FileUtil.getCheckCode(oldfile, "SHA-1"));
                oldFile.setSize(FileUtil.getFileSize(oldfile));
                fileService.updateFile(oldFile);
                System.out.println("签章成功");
                return id;
            } else {
                System.out.println("签章失败");
                return id;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return id;
        }
    }

    /**
     * 签骑缝章
     *
     * @param list
     * @param id
     * @return
     */
    private String addSignaturePdfToIndexBase(List<StampPattern> list, String id) {
        return id;
//        if (!StringUtil.notEmpty(id)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "签章文件id不能为空");
//        }
//        try {
//            File oldFile = fileService.getFileMetadata(id);
//            String table = oldFile.getId();
//            String oldfile = BASEPATH + oldFile.getPath() + "/" + table;
//            String newfilename = oldFile.getName() + "_ca";
//
//            PdfReader reader = new PdfReader(oldfile);
//            int count = reader.getNumberOfPages();
//            reader.close();
//
//            if (count < 100) {
//
//                String base64file = StreamConvertUtil.base64Encode(StreamConvertUtil.file2bytes(oldfile));
//
//                String result = signatureService.pdfSignatureToIndex(base64file, newfilename, "1", list);
//
//                Map content = gson.fromJson(result, Map.class);
//
//                if (content.containsKey("rtnCode") && (Double) content.get("rtnCode") == 1) {
//                    FileUtil.delete(oldfile);
//                    StreamConvertUtil.bytes2file(StreamConvertUtil.base64Decode((String) content.get("signed")), oldfile);
//                    oldFile.setName(newfilename);
//                    oldFile.setSHA1(FileUtil.getCheckCode(oldfile, "SHA-1"));
//                    oldFile.setSize(FileUtil.getFileSize(oldfile));
//                    fileService.updateFile(oldFile);
//                    System.out.println("签章成功");
//                    return id;
//                } else {
//                    System.out.println("签章失败");
//                    return id;
//                }
//            } else {
//                System.out.println("PDF文件不能大于100页");
//                return id;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return id;
//        }
    }

    /**
     * 获取签章配置
     *
     * @param pageNums
     * @param xs
     * @param ys
     * @param signerIds
     * @return
     */
    private List<Stamp> getStamp(int[] pageNums, float[] xs, float[] ys, String[] signerIds) {
        List<Stamp> list = new ArrayList<>();
        for (int i = 0; i < pageNums.length; i++) {
            Stamp stamp = new Stamp();
            stamp.setLeft(i < xs.length - 1 ? xs[i] : xs[0]);
            stamp.setBottom(i < ys.length - 1 ? ys[i] : ys[0]);
            stamp.setPageNum(pageNums[i]);
            stamp.setAddTag(true);
            stamp.setSignerId(i < signerIds.length - 1 ? signerIds[i] : signerIds[0]);
            list.add(stamp);
        }
        return list;
    }

    /**
     * 获取骑缝章配置
     *
     * @param startPage
     * @param endPage
     * @param signerIds
     * @return
     */
    private List<StampPattern> getStampPattern(int startPage, int endPage, String[] signerIds) {
        List<StampPattern> list = new ArrayList<>();
        for (String signerId : signerIds) {
            StampPattern stamp = new StampPattern();
            stamp.setIndexName("毕节市住房公积金管理中心公章");
            stamp.setRightSeal(true);
            stamp.setAddTag(true);
            if (startPage >= 0) stamp.setStartPage(startPage);
            if (endPage >= 0) stamp.setEndPage(endPage);
            stamp.setSignerId(signerId);
            list.add(stamp);
        }
        return list;
    }

}

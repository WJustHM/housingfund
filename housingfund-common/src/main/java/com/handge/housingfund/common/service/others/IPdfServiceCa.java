package com.handge.housingfund.common.service.others;

/**
 * Created by tanyi on 2017/12/1.
 */
public interface IPdfServiceCa {

    /**
     * pdf 单页签章
     *
     * @param pageNum  页码
     * @param x        原点为文档左下角，x轴坐标
     * @param y        原点为文档左下角，y轴坐标
     * @param signerId 印章id（1，2，3）
     * @param id       需要签章文件的id
     * @return
     */
    String addSignaturePdf(int pageNum, float x, float y, String signerId, String id);

    /**
     * pdf多页签章
     *
     * @param pageNums 页码数组
     * @param x        原点为文档左下角，x轴坐标
     * @param y        原点为文档左下角，y轴坐标
     * @param signerId 印章id（1，2，3）
     * @param id       需要签章文件的id
     * @return
     */
    String addSignaturePdf(int[] pageNums, float x, float y, String signerId, String id);

    /**
     * pdf多页签章，位置不同
     *
     * @param pageNums 页码数组
     * @param xs       原点为文档左下角，x轴坐标
     * @param ys       原点为文档左下角，y轴坐标
     * @param signerId 印章id（1，2，3）
     * @param id       需要签章文件的id
     * @return
     */
    String addSignaturePdf(int[] pageNums, float[] xs, float[] ys, String signerId, String id);

    /**
     * pdf多页签章，章不同
     *
     * @param pageNums  页码数组
     * @param x         原点为文档左下角，x轴坐标
     * @param y         原点为文档左下角，y轴坐标
     * @param signerIds 印章id（1，2，3）
     * @param id        需要签章文件的id
     * @return
     */
    String addSignaturePdf(int[] pageNums, float x, float y, String[] signerIds, String id);

    /**
     * pdf多页签章，位置不同,章不同
     *
     * @param pageNums  页码数组
     * @param xs        原点为文档左下角，x轴坐标
     * @param ys        原点为文档左下角，y轴坐标
     * @param signerIds 印章id（1，2，3）
     * @param id        需要签章文件的id
     * @return
     */
    String addSignaturePdf(int[] pageNums, float[] xs, float[] ys, String[] signerIds, String id);

    /**
     * pdf 签骑缝章
     *
     * @param startPage 开始页面
     * @param endPage   结算页面
     * @param signerId  印章id（1，2，3）
     * @param id        需要签章文件的id
     * @return
     */
    String addSignatureToIndexPdf(int startPage, int endPage, String signerId, String id);


    /**
     * pdf 签骑缝章,章不同
     *
     * @param startPage 开始页面
     * @param endPage   结算页面
     * @param signerIds 印章id（1，2，3）
     * @param id        需要签章文件的id
     * @return
     */
    String addSignatureToIndexPdf(int startPage, int endPage, String[] signerIds, String id);


}

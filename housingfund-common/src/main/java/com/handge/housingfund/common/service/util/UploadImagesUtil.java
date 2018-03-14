package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.others.model.UFile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyi on 2017/8/19.
 */
public class UploadImagesUtil {

    /**
     * 获取资料列表
     *
     * @param json
     * @return
     */
    public static List<UFile> getFileByJson(String json) {
        if (!StringUtil.notEmpty(json)) {
            return null;
        }
        List<UFile> res = new ArrayList<>();
        try {
            JSONArray files = new JSONArray(json);
            for (int i = 0; i < files.length(); i++) {
                UFile uFile = new UFile();
                JSONObject file = files.optJSONObject(i);
                uFile.setBusiness(file.getString("business"));
                JSONArray datas = file.getJSONArray("data");
                if (datas.length() > 0) {
                    String[] datasting = new String[datas.length()];
                    for (int j = 0; j < datas.length(); j++) {
                        datasting[j] = datas.getString(j);
                    }
                    uFile.setData(datasting);
                } else {
                    uFile.setData(new String[0]);
                }
                uFile.setModle(file.getString("modle"));
                uFile.setName(file.getString("name"));
                uFile.setOperation(file.getString("operation"));
                uFile.setRequired(file.getBoolean("required"));
                res.add(uFile);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res = null;
        }
        return res;
    }

    /**
     * @param srcImgPath 源图片路径
     * @param tarImgPath 保存的图片路径
     * @param waterMarkContent 水印内容
     * @param markContentColor 水印颜色     *
     */
    public static void addWaterMark(String srcImgPath, String tarImgPath, String waterMarkContent,Color markContentColor) {

        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); //根据图片的背景设置水印颜色
            if(srcImgHeight>1000){
                g.setFont(new Font("微软雅黑", Font.PLAIN, 32));
            }else g.setFont(new Font("微软雅黑", Font.PLAIN, 19));              //设置字体
            //设置水印的坐标
            g.rotate(Math.toRadians(-45),srcImgWidth/2,srcImgWidth/2);//逆向旋转45度
            int x = (srcImgWidth - getWatermarkLength(waterMarkContent, g)) / 2;
            int y = srcImgHeight / 2;
            g.drawString(waterMarkContent, x-100, y);  //画出水印
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            System.out.println("添加水印完成");
            outImgStream.flush();
            outImgStream.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    public static void main(String[] args) {
        UploadImagesUtil.addWaterMark("C:\\Users\\xiangchao\\Pictures\\Saved Pictures\\u=4287850242,3208290927&fm=200&gp=0.jpg","C:\\Users\\xiangchao\\Pictures\\Saved Pictures\\timg (1)2.jpg","仅限毕节市住房公积金管理中心使用 上传者 向超",new Color(255,255,255,200));
    }

}

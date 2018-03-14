package com.handge.housingfund.common.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件操作工具类 Created by gxy on 17-8-5.
 */
public class FileUtil {
    public static void createFile(String path) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        file.createNewFile();
    }

    public static void mkdirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    /**
     * 写文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @throws IOException io异常
     */
    public static void write(String path, byte[] content) throws Exception {
        File file = new File(path);

        try {
            // 如果文件不存在,则创建
            if (!file.exists()) {
                createFile(path);
                file = new File(path);
            }
            FileOutputStream fop = new FileOutputStream(file);
            fop.write(content);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            throw new Exception("写文件异常. " + LogUtil.getTrace(e));
        }

    }

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return byte[] 文件内容
     * @throws IOException io异常
     */
    public static byte[] read(String path) throws Exception {
        File file = new File(path);

        try {
            if (!file.exists()) {
                throw new Exception("文件不存在. ");
            }
            byte[] content = new byte[(int) file.length()];
            FileInputStream fip = new FileInputStream(file);
            fip.read(content);
            fip.close();

            return content;

        } catch (IOException e) {
            throw new Exception("读文件异常. " + LogUtil.getTrace(e));
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文件 hash值
     *
     * @param filePath
     * @param algorithm SHA-1
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getCheckCode(String filePath, String algorithm) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        FileInputStream in = new FileInputStream(file);
        MessageDigest messagedigest;
        messagedigest = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[1024 * 1024 * 10];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, messagedigest.digest());
        return bigInt.toString(16);

    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static BigDecimal getFileSize(String filePath) {
        File file = new File(filePath);
        return BigDecimal.valueOf(file.length());
    }

    /**
     * 获取文件最后修改日期
     *
     * @param filePath
     * @return
     */
    public static long getLastModified(String filePath) throws Exception {
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                throw new Exception("文件不存在. ");
            } else {
                return file.lastModified();
            }
        } catch (IOException e) {
            throw new Exception("读文件异常. " + LogUtil.getTrace(e));
        }
    }
}

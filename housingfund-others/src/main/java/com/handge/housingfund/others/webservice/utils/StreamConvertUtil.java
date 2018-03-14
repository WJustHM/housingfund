package com.handge.housingfund.others.webservice.utils;

import java.io.*;


/**
 * 字符与流的转换
 * @author fupf
 *
 */
public class StreamConvertUtil
{

    private static String hexString = "0123456789ABCDEF";

    private static final int BUFFER_SIZE = 2048;

    private static char[] base64EncodeChars = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '+', '/'};

    private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
        60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1,
        -1};

    /**
     * byte[]转HEX
     * @param str
     * @return
     */
    public static String hexEncode(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++ )
        {
            sb.append(hexString.charAt( (bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt( (bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * HEX转byte[]
     * @param bytes
     * @return
     */
    public static byte[] hexDecode(String bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write( (hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return baos.toByteArray();
    }

    /**
     * byte[]转base64
     * @param data
     * @return
     */
    public static String base64Encode(byte[] data)
    {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len)
        {
            b1 = data[i++ ] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[ (b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++ ] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[ ( (b1 & 0x03) << 4) | ( (b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[ (b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++ ] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[ ( (b1 & 0x03) << 4) | ( (b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[ ( (b2 & 0x0f) << 2) | ( (b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * base64转byte[]
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] base64Decode(String str)
        throws UnsupportedEncodingException
    {
        StringBuffer sb = new StringBuffer();
        byte[] data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len)
        {
            /* b1 */
            do
            {
                b1 = base64DecodeChars[data[i++ ]];
            }
            while (i < len && b1 == -1);
            if (b1 == -1) break;
            /* b2 */
            do
            {
                b2 = base64DecodeChars[data[i++ ]];
            }
            while (i < len && b2 == -1);
            if (b2 == -1) break;
            sb.append((char) ( (b1 << 2) | ( (b2 & 0x30) >>> 4)));
            /* b3 */
            do
            {
                b3 = data[i++ ];
                if (b3 == 61) return sb.toString().getBytes("ISO-8859-1");
                b3 = base64DecodeChars[b3];
            }
            while (i < len && b3 == -1);
            if (b3 == -1) break;
            sb.append((char) ( ( (b2 & 0x0f) << 4) | ( (b3 & 0x3c) >>> 2)));
            /* b4 */
            do
            {
                b4 = data[i++ ];
                if (b4 == 61) return sb.toString().getBytes("ISO-8859-1");
                b4 = base64DecodeChars[b4];
            }
            while (i < len && b4 == -1);
            if (b4 == -1) break;
            sb.append((char) ( ( (b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("ISO-8859-1");
    }

    /**
     * 字符转utf-8编码
     * @param s
     * @return
     */
    public static String utf8Encode(String s)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++ )
        {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255)
            {
                sb.append(c);
            }
            else
            {
                byte[] b;
                try
                {
                    b = Character.toString(c).getBytes("utf-8");
                }
                catch (Exception ex)
                {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++ )
                {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 把文件转为byte数组
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] file2bytes(String path)
        throws IOException
    {
        FileInputStream inputFile = null;
        try
        {
            File file = new File(path);
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            return buffer;
        }
        finally
        {
            if (inputFile != null) try
            {
                inputFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
            inputFile = null;
        }
    }

    /**
     * byte[]存文件
     * @param buffer
     * @param targetPath
     * @throws IOException
     */
    public static void bytes2file(byte[] buffer, String targetPath)
        throws IOException
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(targetPath);
            out.write(buffer);
        }
        finally
        {
            if (out != null) try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
            out = null;
        }
    }

    /** 
     * 将InputStream转换成byte数组 
     * @param in InputStream 
     * @return byte[] 
     * @throws IOException 
     */
    public static byte[] inputStream2bytes(InputStream in)
        throws IOException
    {
        ByteArrayOutputStream outStream = null;
        try
        {
            outStream = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count = -1;
            while ( (count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
            return outStream.toByteArray();
        }
        finally
        {
            if (outStream != null) try
            {
                outStream.close();
            }
            catch (IOException e)
            {
            }
            outStream = null;
            if (in != null) try
            {
                in.close();
            }
            catch (IOException e)
            {
            }
            in = null;
        }
    }

    /**
     * 流转字符
     * @param in
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String inputStream2string(InputStream in, String charset)
        throws UnsupportedEncodingException, IOException
    {
        return new String(inputStream2bytes(in), charset);
    }

    /**
     * 将文件转成base64 字符串
     * @param path 文件路径
     * @return  * 
     * @throws IOException 
     */
    public static String encodeBase64File(String path)
        throws IOException
    {
        FileInputStream inputFile = null;
        try
        {
            File file = new File(path);;
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            return base64Encode(buffer);
        }
        finally
        {
            if (inputFile != null) try
            {
                inputFile.close();
            }
            catch (IOException e)
            {
            }
            inputFile = null;
        }

    }

    /**
      * 将base64字符解码保存文件
      * @param base64Code
      * @param targetPath
      * @throws Exception
      */
    public static void decoderBase64ToFile(String base64Code, String targetPath)
        throws Exception
    {
        byte[] buffer = base64Decode(base64Code);
        bytes2file(buffer, targetPath);
    }

    /**
      * 将base64字符保存文本文件
      * @param base64Code
      * @param targetPath
      * @throws Exception
      */
    public static void base64ToFile(String base64Code, String targetPath)
        throws Exception
    {
        byte[] buffer = base64Code.getBytes();
        bytes2file(buffer, targetPath);
    }
}

package com.handge.housingfund.bank.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;

/**
 * Created by gxy on 17-6-28.
 */
public class TcpSocketIO {
    private InputStream is;

    private static Logger logger = LogManager.getLogger(TcpSocketIO.class);

    public TcpSocketIO(SocketChannel sc) throws IOException {
        this.is = sc.socket().getInputStream();
    }

    protected byte[] read(int offset, int length) throws IOException {
        try {
            if(length == 0) {
                return null;
            } else {
                byte[] buf = new byte[length];

                int n;
                for(int pos = 0; pos < length; pos += n) {
                    n = this.is.read(buf, pos + offset, length - pos);
                    if(n < 0) {
                        throw new IOException("读 ByteArray 错: 读到流末尾");
                    }
                }

                return buf;
            }
        } catch (SocketTimeoutException var6) {
            throw new IOException("读ByteArray超时." + var6.getMessage(), var6);
        } catch (IOException var7) {
//            throw new IOException("读 ByteArray 错", var7);
            logger.warn("可能收到住建部接口探测报文");
            return null;
        }
    }


    protected void close() throws IOException{
        try{
            this.is.close();
        }catch (IOException e){
            throw new IOException("关闭流失败："+e.getMessage(), e);
        }
    }
}

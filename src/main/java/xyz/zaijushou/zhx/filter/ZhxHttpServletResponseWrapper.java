package xyz.zaijushou.zhx.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ZhxHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer;//输出到byte array
    private ServletOutputStream out;
    private PrintWriter writer;

    ZhxHttpServletResponseWrapper(HttpServletResponse resp) {
        super(resp);
        buffer = new ByteArrayOutputStream();// 真正存储数据的流
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8));
    }

    /**
     * 重载父类获取outputstream的方法
     */
    @Override
    public ServletOutputStream getOutputStream() {
        return out;
    }

    /**
     * 重载父类获取writer的方法
     */
    @Override
    public PrintWriter getWriter(){
        return writer;
    }

    /**
     * 重载父类获取flushBuffer的方法
     */
    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    /**
     * 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据
     */
    byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    /**
     * 内部类，对ServletOutputStream进行包装
     */
    private class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos;

        WapperedOutputStream(ByteArrayOutputStream stream) {
            bos = stream;
        }

        @Override
        public void write(int b) {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) {
            bos.write(b, 0, b.length);
        }

        public boolean isReady() {
            return false;
        }

        public void setWriteListener(WriteListener writeListener) {
        }
    }
}
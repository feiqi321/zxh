package xyz.zaijushou.zhx.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class ZhxHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body; //用于保存读取body中数据

    ZhxHttpServletRequestWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        ServletInputStream servletInputStream = request.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = servletInputStream.read(buffer)) > -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byteArrayOutputStream.flush();
        body = byteArrayOutputStream.toByteArray();
        servletInputStream.close();
        byteArrayOutputStream.close();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }
}

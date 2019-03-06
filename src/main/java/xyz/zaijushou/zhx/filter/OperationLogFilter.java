package xyz.zaijushou.zhx.filter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OperationLogFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(OperationLogFilter.class);

    @Resource
    private SysOperationLogService sysOperationLogService;

    private static final String[] REQUEST_EXCEPT_URL = {
            "/import",   //上传文件
    };

    private static final String[] RESPONSE_EXCEPT_URL = {
            "/operationLog/pageLogs",   //操作日志查询

    };

    private static final String[] IMPORT_FILE_URL = {
            "/dataCase/tel/import", //案件电话导入
            "/dataCase/updateCase/import", //更新案件导入
            "/synergistic/finishedSynergisticImport",
            "/synergistic/synergisticRecordImport",
            "/reduce/import",//减免导入
    };

    private static final String[] DOWNLOAD_FILE_URL = {
            "/fileManage/download", //下载附件
            "/reduce/download", //下载减免附件
            "/fileManage/downloadArchive", //下载附件
            "/dataBatch/pageDataBatchExport",
            "/dataBatch/totalDataBatchExport",
            "/dataBatch/selectDataBatchExport",
            "/dataBatch/selectDataCollectExportByBatch",
            "/dataCollect/pageDataCollectExport", //催记导出
            "/dataCollect/totalDataCollectExport",
            "/dataCollect/selectDataCollectExport", //查询导出所选批次的催收记录
            "/fileManage/downloadCase",
            "/dataCase/selectDataCollectExportByBatch",
            "/dataCase/selectDataCaseTel",
            "/dataCase/totalDataBatchExport",
            "/synergistic/queryDataExport",
            "/dataCase/selectDataCaseExport",
            "/reduce/dataExport",//减免导出
            "/statistics/collection/day/export",//电催员电催单日统计导出
            "/statistics/collection/month/export",//电催员电催月度统计导出
            "/statistics/collection/day/action/export",//电催员每日动作统计导出
            "/dataCase/selectDataCaseExportByBatch",
            "/dataCase/pageCaseListExport"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//        if(true) {
//            return;
//        }
        String method = httpServletRequest.getMethod();
        //仅对post请求进行过滤
        if (!"POST".equals(method)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        ZhxHttpServletRequestWrapper requestWrapper = new ZhxHttpServletRequestWrapper(httpServletRequest);
        ZhxHttpServletResponseWrapper responseWrapper = new ZhxHttpServletResponseWrapper(httpServletResponse);

        SysOperationLogEntity operationLog = new SysOperationLogEntity();

        operationLog.setUrl(requestWrapper.getRequestURI().replaceAll("/+", "/"));

        StringBuilder body = new StringBuilder();
        if(arrayContainsContent(REQUEST_EXCEPT_URL, operationLog.getUrl())) {
            BufferedReader reader = requestWrapper.getReader();
            String line;
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            logger.info("请求参数:{}", body.toString());

            operationLog.setRequestBody(body.toString());
        }

        String token = requestWrapper.getHeader("Authorization");
        operationLog.setUserIp(getIPAddress(requestWrapper));
        operationLog.setUserBrowser(requestWrapper.getHeader("User-Agent"));
        operationLog.setRequestToken(token);
        if(!StringUtils.isEmpty(token)) {
            try{
                JSONObject tokenData = JwtTokenUtil.tokenData();
                operationLog.setUserId(tokenData.getInteger("userId"));
                operationLog.setUserLoginName(tokenData.getString("loginName"));
            } catch (Exception e) {
                logger.error("token解析失败，{}", e);
            }
        }

        sysOperationLogService.insertRequest(operationLog);
        String result;
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            if(arrayContainsContent(DOWNLOAD_FILE_URL, operationLog.getUrl())) {
                httpServletResponse.setHeader("Access-Control-Expose-Headers","Content-Disposition");
                httpServletResponse.setHeader("Access-Control-Expose-Headers","response-msg-info");
                httpServletResponse.setHeader("response-msg-info",JSONObject.toJSONString(WebResponse.success(responseWrapper.getHeader("Content-Disposition").replace("attachment;filename=", ""))));
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                servletOutputStream.write(responseWrapper.getResponseData());
                servletOutputStream.flush();
                servletOutputStream.close();
                sysOperationLogService.updateResponse(operationLog);
                return;
            }
            result = new String(responseWrapper.getResponseData(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("后台错误：{}", e);
            result = JSONObject.toJSONString(WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "后台错误error：" + e.getMessage()));
            if(arrayContainsContent(DOWNLOAD_FILE_URL, operationLog.getUrl())) {
                httpServletResponse.setHeader("Access-Control-Expose-Headers","response-msg-info");
                httpServletResponse.setHeader("response-msg-info", URLEncoder.encode(result, "UTF-8"));
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                servletOutputStream.write(result.getBytes(StandardCharsets.UTF_8));
                servletOutputStream.flush();
                servletOutputStream.close();
                return;
            }
        }

        httpServletResponse.setContentLength(-1);
        httpServletResponse.setCharacterEncoding("UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(result);
        out.flush();
        out.close();
        logger.info("返回结果:{}", result);
        if(!arrayContainsContent(RESPONSE_EXCEPT_URL, operationLog.getUrl())) {
            operationLog.setResponseBody(result);
        }
        if("/login".equals(operationLog.getUrl())) {
            JSONObject jsonData = JSONObject.parseObject(body.toString());
            if(jsonData != null) {
                operationLog.setUserLoginName(jsonData.getString("loginName"));
            }
            jsonData = JSONObject.parseObject(result);
            if("100".equals(jsonData.getString("code"))) {
                String responseToken = jsonData.getJSONObject("data").getString("token");
                JSONObject tokenData = JwtTokenUtil.tokenData(responseToken);
                operationLog.setUserId(tokenData.getInteger("userId"));
            }
        }
        sysOperationLogService.updateResponse(operationLog);
    }

    @Override
    public void destroy() {
    }

    private static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private boolean arrayContainsContent(String[] array, String value){
        for(String val : array) {
            if(val.equals(value)) {
                return true;
            }
        }
        return false;
    }
}

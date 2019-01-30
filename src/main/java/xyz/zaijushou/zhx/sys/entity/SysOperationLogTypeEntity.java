package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * 操作日志类型实体类
 */
public class SysOperationLogTypeEntity extends CommonEntity {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 日志内容模板
     */
    private String logTemplate;

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogTemplate() {
        return logTemplate;
    }

    public void setLogTemplate(String logTemplate) {
        this.logTemplate = logTemplate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

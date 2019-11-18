package xyz.zaijushou.zhx.sys.entity;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/28,11:17]
 */
public class ImportResultDTO {
    /**部门*/
    private String department;
    /**用户名*/
    private String loginName;
    /**姓名*/
    private String username;

    private String role;

    private String result;
    /**显示颜色标记：0红色，1黑色*/
    private Integer ColorStatus;

    public Integer getColorStatus() {
        return ColorStatus;
    }

    public void setColorStatus(Integer colorStatus) {
        ColorStatus = colorStatus;
    }

    public ImportResultDTO() {
    }

    public ImportResultDTO(String department, String loginName, String username, String role, String result) {
        this.department = department;
        this.loginName = loginName;
        this.username = username;
        this.role = role;
        this.result = result;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;

/**
 * 用户实体类
 */
public class SysOperationUserEntity extends CommonEntity {

  private String username;
  private String number;
  private String department;
  private String position;
  private String sex;
  private String highEdu;
  private String userType;
  private String category;
  private String posiTitle;
  private String comeResource;
  private String password;
private String departId;
  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date joinTime;
  private int internship;

  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date planTime;

  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date actualTime;

  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date creditTime;

  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date leaveTime;
  private double yearlySalary;
  private String securityNumber;
  private String fundNumber;
  private String benefit;
  private String depositBank;
  private String bankNumber;
  private String idnumber;

  @JSONField(format="yyyy-MM-dd HH:mm:ss")
  private Date birthTime;
  private int marryStatus;
  private String political;
  private String nation;
  private String birthOrigin;
  private String householdAddress;
  private String householdNature;
  private String graduation;
  private String eduBackground;
  private String workExperience;
  private String mobilization;
  private String mobile;
  private String officePhone;
  private String homePhone;
  private String qq;
  private String msn;
  private String email;
  private String address;
  private String loginName;
  private int enable;

  private int status;

  public String getDepartId() {
    return departId;
  }

  public void setDepartId(String departId) {
    this.departId = departId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }


  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public String getHighEdu() {
    return highEdu;
  }

  public void setHighEdu(String highEdu) {
    this.highEdu = highEdu;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


  public String getPosiTitle() {
    return posiTitle;
  }

  public void setPosiTitle(String posiTitle) {
    this.posiTitle = posiTitle;
  }


  public String getComeResource() {
    return comeResource;
  }

  public void setComeResource(String comeResource) {
    this.comeResource = comeResource;
  }


  public Date getJoinTime() {
    return joinTime;
  }

  public void setJoinTime(Date joinTime) {
    this.joinTime = joinTime;
  }


  public int getInternship() {
    return internship;
  }

  public void setInternship(int internship) {
    this.internship = internship;
  }


  public Date getPlanTime() {
    return planTime;
  }

  public void setPlanTime(Date planTime) {
    this.planTime = planTime;
  }


  public Date getActualTime() {
    return actualTime;
  }

  public void setActualTime(Date actualTime) {
    this.actualTime = actualTime;
  }


  public Date getCreditTime() {
    return creditTime;
  }

  public void setCreditTime(Date creditTime) {
    this.creditTime = creditTime;
  }


  public Date getLeaveTime() {
    return leaveTime;
  }

  public void setLeaveTime(Date leaveTime) {
    this.leaveTime = leaveTime;
  }


  public double getYearlySalary() {
    return yearlySalary;
  }

  public void setYearlySalary(double yearlySalary) {
    this.yearlySalary = yearlySalary;
  }


  public String getSecurityNumber() {
    return securityNumber;
  }

  public void setSecurityNumber(String securityNumber) {
    this.securityNumber = securityNumber;
  }


  public String getFundNumber() {
    return fundNumber;
  }

  public void setFundNumber(String fundNumber) {
    this.fundNumber = fundNumber;
  }


  public String getBenefit() {
    return benefit;
  }

  public void setBenefit(String benefit) {
    this.benefit = benefit;
  }


  public String getDepositBank() {
    return depositBank;
  }

  public void setDepositBank(String depositBank) {
    this.depositBank = depositBank;
  }


  public String getBankNumber() {
    return bankNumber;
  }

  public void setBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
  }


  public String getIdnumber() {
    return idnumber;
  }

  public void setIdnumber(String idnumber) {
    this.idnumber = idnumber;
  }


  public Date getBirthTime() {
    return birthTime;
  }

  public void setBirthTime(Date birthTime) {
    this.birthTime = birthTime;
  }


  public int getMarryStatus() {
    return marryStatus;
  }

  public void setMarryStatus(int marryStatus) {
    this.marryStatus = marryStatus;
  }


  public String getPolitical() {
    return political;
  }

  public void setPolitical(String political) {
    this.political = political;
  }


  public String getNation() {
    return nation;
  }

  public void setNation(String nation) {
    this.nation = nation;
  }


  public String getBirthOrigin() {
    return birthOrigin;
  }

  public void setBirthOrigin(String birthOrigin) {
    this.birthOrigin = birthOrigin;
  }


  public String getHouseholdAddress() {
    return householdAddress;
  }

  public void setHouseholdAddress(String householdAddress) {
    this.householdAddress = householdAddress;
  }


  public String getHouseholdNature() {
    return householdNature;
  }

  public void setHouseholdNature(String householdNature) {
    this.householdNature = householdNature;
  }


  public String getGraduation() {
    return graduation;
  }

  public void setGraduation(String graduation) {
    this.graduation = graduation;
  }


  public String getEduBackground() {
    return eduBackground;
  }

  public void setEduBackground(String eduBackground) {
    this.eduBackground = eduBackground;
  }


  public String getWorkExperience() {
    return workExperience;
  }

  public void setWorkExperience(String workExperience) {
    this.workExperience = workExperience;
  }


  public String getMobilization() {
    return mobilization;
  }

  public void setMobilization(String mobilization) {
    this.mobilization = mobilization;
  }


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  public String getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
  }


  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }


  public String getQq() {
    return qq;
  }

  public void setQq(String qq) {
    this.qq = qq;
  }


  public String getMsn() {
    return msn;
  }

  public void setMsn(String msn) {
    this.msn = msn;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }



  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }
  
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}

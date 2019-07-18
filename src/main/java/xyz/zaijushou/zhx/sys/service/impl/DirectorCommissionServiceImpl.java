package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DirectorCommissionService;
import xyz.zaijushou.zhx.sys.service.SysPercentService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lsl
 * @version [1.0.0, 2019/5/9,14:29]
 */
@Service
public class DirectorCommissionServiceImpl implements DirectorCommissionService {
    @Resource
    private SysOrganizationMapper sysOrganizationMapper;

    @Resource
    private SysPercentService sysPercentService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public WebResponse findDirectorCommission() {
        Integer id = JwtTokenUtil.tokenData().getInteger("userId");
        // 通过用户id 查到department
        SysNewUserEntity sysNewUserEntity = sysUserMapper.findDepartment(id);
        String department = sysNewUserEntity.getDepartment();
        // 查到id下所有的组织下所有在职催收员信息
        List<SysNewUserDataForm> list = sysOrganizationMapper.findSysNewUserInformationById(department);
        List<Staffs> listStaffs = new ArrayList<>();
        Staffs staffs;
        Date actualTime;
        for (SysNewUserDataForm sysUserEntity : list) {
            String userName = sysUserEntity.getUserName();
            staffs = new Staffs();
            staffs.setUserName(userName);
            DataCaseEntity tempCase = new DataCaseEntity();
            // 当前催收员
            tempCase.setOdv(sysUserEntity.getId() + "");
            // 转正时间
            actualTime = sysUserEntity.getActualTime();
            BigDecimal commission = sysUserEntity.getPercentage();
            BigDecimal repay_amt = sysUserEntity.getRepay_amt();
            staffs.setCommissionMsg(commission == null ? 0 + "" : FmtMicrometer.fmtMicrometer(commission.stripTrailingZeros() + ""));
            staffs.setPaidMoneyMsg(repay_amt == null ? 0 + "" : FmtMicrometer.fmtMicrometer(repay_amt.stripTrailingZeros() + ""));
            // 提成金额
            staffs.setCommission(commission == null ? new BigDecimal(Integer.toString(0)) : commission);
            // 还款金额
            staffs.setPaidMoney(repay_amt == null ? new BigDecimal(Integer.toString(0)) : repay_amt);
            staffs.setActualTime(actualTime);
            listStaffs.add(staffs);
        }
        HashMap<String, Object> map = new HashMap<>(2);
        SysStandard sysStandard = sysPercentService.listStandard();
        Level level = new Level();
        ArrayList<Staffs> list0 = new ArrayList<>();
        ArrayList<Staffs> list1 = new ArrayList<>();
        ArrayList<Staffs> list2 = new ArrayList<>();
        ArrayList<Staffs> list3 = new ArrayList<>();
        for (Staffs listStaff : listStaffs) {
            // 判断是否为当月下组
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
            if (listStaff.getActualTime() != null) {
                String actualMonth = date.format(listStaff.getActualTime()).substring(4, 6);
                String currentMonth = date.format(new Date()).substring(4, 6);
                // 当月下组
                if (actualMonth.equals(currentMonth)) {
                    list0.add(listStaff);
                    level.setLevel0(list0);
                } else {
                    // 非当月下组,纳入计算范围
                    setLevelMethod(sysStandard, listStaff, level, list1, list2, list3);
                }
            } else {
                // 下组日期为空时
                // 判断提成记入相应的等级
                setLevelMethod(sysStandard, listStaff, level, list1, list2, list3);
            }
        }
        map.put("commissionSettings", sysStandard);
        map.put("staffs", level);
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setTotalNum(listStaffs.size());
        webResponse.setData(map);
        return webResponse;
    }

    private void setLevelMethod(SysStandard sysStandard, Staffs listStaff, Level level, ArrayList<Staffs> list1, ArrayList<Staffs> list2, ArrayList<Staffs> list3) {
        if (listStaff.getCommission().compareTo(sysStandard.getStandard2()) >= 0) {
            list3.add(listStaff);
            level.setLevel3(list3);
        } else if (listStaff.getCommission().compareTo(sysStandard.getStandard2()) < 0 && listStaff.getCommission().compareTo(sysStandard.getStandard1()) >= 0) {
            list2.add(listStaff);
            level.setLevel2(list2);
        } else {
            list1.add(listStaff);
            level.setLevel1(list1);
        }
    }
}

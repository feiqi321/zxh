package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseBaseConstant;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DirectorCommissionService;
import xyz.zaijushou.zhx.sys.service.SysPercentService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

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
    SysOrganizationMapper sysOrganizationMapper;

    @Resource
    SysPercentService sysPercentService;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysPercentMapper sysPercentMapper;
    /**
     * 案件数据层
     */
    @Resource
    private DataCaseMapper caseMapper;

    @Resource
    DataCollectionMapper dataCollectionMapper;

    @Override
    public WebResponse findDirectorCommission() {
        Integer id = JwtTokenUtil.tokenData().getInteger("userId");
        // 通过用户id 查到department
        SysNewUserEntity sysNewUserEntity = sysUserMapper.findDepartment(id);
       String department = sysNewUserEntity.getDepartment();
        // 查到id下所有的组织下所有催收员
        List<SysNewUserEntity> list = sysOrganizationMapper.findById(department);
        List<Staffs> listStaffs = new ArrayList<>();
        Staffs staffs;
        Date actualTime;
        Calendar timeStart;
        Calendar timeEnd;
        for (SysNewUserEntity sysUserEntity : list) {
            String userName = sysUserEntity.getUserName();
            staffs = new Staffs();
            staffs.setUserName(userName);

            DataCaseEntity tempCase = new DataCaseEntity();
            // 当前催收员
            tempCase.setOdv(sysUserEntity.getId() + "");
            // 转正时间
            actualTime = sysUserEntity.getActualTime();


            CollectionStatistic beanInfoData = new CollectionStatistic();
            timeStart = Calendar.getInstance();
            timeEnd = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            timeStart.add(Calendar.MONTH, 0);    //得到前一个月
            timeStart.set(Calendar.DAY_OF_MONTH, 1);//设置为1号

            String first = sdf1.format(timeStart.getTime());
            beanInfoData.setMonthStart(first);//月第一天

            timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
            String last = sdf1.format(timeEnd.getTime());
            // 月最后一天
            beanInfoData.setMonthEnd(last);
            //获取当前月25号
            Calendar ca25 = Calendar.getInstance();
            ca25.set(Calendar.DAY_OF_MONTH, 25);
            String day25 = sdf1.format(ca25.getTime());

            tempCase.setRepayDateStart(first);

            BigDecimal result = new BigDecimal("0.00");
            // 催收员
            if ("1".equals(tempCase.getCaseType())) {
                if (actualTime == null || actualTime.compareTo(timeStart.getTime()) < 0 || actualTime.compareTo(timeEnd.getTime()) > 0) {
                    //阶梯累加
                    tempCase.setRepayDateEnd(last);
                    result = royaltyType(tempCase, 1);
                } else if (actualTime.compareTo(timeStart.getTime()) >= 0 || actualTime.compareTo(ca25.getTime()) < 0) {
                    tempCase.setRepayDateEnd(day25);
                    result = royaltyType(tempCase, 2);
                    //25到月底
                    tempCase.setRepayDateStart(day25);
                    tempCase.setRepayDateEnd(last);
                    result = result.add(royaltyType(tempCase, 2));
                } else if (actualTime.compareTo(ca25.getTime()) >= 0) {
                    tempCase.setRepayDateEnd(last);
                    result = royaltyType(tempCase, 3);
                }
            }
            CollectionStatistic collectionStatic = dataCollectionMapper.statisticsCollectionPayM(beanInfoData);
            // 已还款提成金额
            BigDecimal commission = collectionStatic.getRepaidAmt();
            staffs.setCommissionMsg(collectionStatic.getRepaidAmt() == null ? "" : "￥" + FmtMicrometer.fmtMicrometer(collectionStatic.getRepaidAmt().stripTrailingZeros() + ""));
            staffs.setPaidMoneyMsg(result == null ? "" : "￥" + FmtMicrometer.fmtMicrometer(result.stripTrailingZeros() + ""));
            // 提成金额
            staffs.setCommission(commission);
            // 还款金额
            staffs.setPaidMoney(result);
            staffs.setActualTime(actualTime);
            listStaffs.add(staffs);
        }

        HashMap<String, Object> map = new HashMap<>(16);
        SysStandard sysStandard = sysPercentService.listStandard();
        Level level = new Level();

        timeStart = Calendar.getInstance();
        // 设置为1号
        timeStart.set(Calendar.DAY_OF_MONTH, 1);
        timeEnd = Calendar.getInstance();
        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));

        ArrayList<Staffs> list0 = new ArrayList<>();
        ArrayList<Staffs> list1 = new ArrayList<>();
        ArrayList<Staffs> list2 = new ArrayList<>();
        ArrayList<Staffs> list3 = new ArrayList<>();
        for (Staffs listStaff : listStaffs) {
            if (listStaff.getCommission().compareTo(sysStandard.getStandard2()) > -1) {
                list3.add(listStaff);
                level.setLevel3(list3);
            } else if (listStaff.getCommission().compareTo(sysStandard.getStandard2()) < 0 && listStaff.getCommission().compareTo(sysStandard.getStandard1()) > -1) {
                list2.add(listStaff);
                level.setLevel3(list2);
            } else if (listStaff.getCommission().compareTo(sysStandard.getStandard1()) < 0) {
                // 判断是否为当月下组
                if (listStaff.getActualTime() != null) {
                    if (listStaff.getActualTime().compareTo(timeStart.getTime()) > 0 && listStaff.getActualTime().compareTo(timeEnd.getTime()) < 0) {
                        list0.add(listStaff);
                        level.setLevel0(list0);
                    }
                }
                // 如果非当月下组
                else {
                    list1.add(listStaff);
                    level.setLevel1(list1);
                }
            }
        }
        map.put("commissionSettings", sysStandard);
        map.put("staffs", level);
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setTotalNum(listStaffs.size());
        webResponse.setData(map);
        return webResponse;
    }


    /**
     * 催收员提成
     *
     * @param tempCase
     * @param type
     * @return
     */
    private BigDecimal royaltyType(DataCaseEntity tempCase, int type) {
        BigDecimal result = new BigDecimal("0.00");
        //获取案件条线类型
        SysPercent percentData = new SysPercent();
        List<DataCaseEntity> caseList1 = caseMapper.selectCommonCase(tempCase);
        for (int i = 0; i < caseList1.size(); i++) {
            if (caseList1.get(i) == null) {
                continue;
            }
            percentData.setClient(caseList1.get(i).getBusinessType());
            SysPercent percent = sysPercentMapper.findByClient(percentData);
            caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt() == null ? new BigDecimal(0) : caseList1.get(i).getEnRepayAmt());
            if (StringUtils.notEmpty(percent)) {
                if (type == 1 ||
                        ((type == 2 || type == 3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW) >= 0)) {
                    result = result.add(
                            royaltyCalculate(caseList1.get(i).getEnRepayAmt(), null, percent));
                }
            }
        }
        //特殊1
        List<DataCaseEntity> caseList2 = caseMapper.selectTsCase1(tempCase);
        for (int i = 0; i < caseList2.size(); i++) {
            if (caseList2.get(i) == null) {
                continue;
            }
            percentData.setClient(caseList2.get(i).getBusinessType());
            SysPercent percent = sysPercentMapper.findByClient(percentData);
            String settleDate = caseList2.get(i).getSettleDate();
            // 1 已结清 0 未结清
            String settleFlag = caseList2.get(i).getSettleFlag();
            caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt() == null ? new BigDecimal(0) : caseList1.get(i).getEnRepayAmt());
            if ("已结清".equals(settleFlag)) {
                if (type == 1 ||
                        ((type == 2 || type == 3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW) >= 0)) {
                    result = result.add(
                            royaltyCalculate(caseList2.get(i).getEnRepayAmt(), caseList2.get(i).getMoney(), percent));
                }
            }
        }
        //特殊2
        List<DataCaseEntity> caseList3 = caseMapper.selectTsCase2(tempCase);
        if (StringUtils.notEmpty(caseList3)) {
            BigDecimal numHoursPay = new BigDecimal(0);
            BigDecimal numHoursMoney = new BigDecimal(0);
            for (int i = 0; i < caseList3.size(); i++) {
                caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt() == null ? new BigDecimal(0) : caseList1.get(i).getEnRepayAmt());
                if (type == 1 ||
                        ((type == 2 || type == 3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW) >= 0)) {
                    numHoursPay = numHoursPay.add(calHoursValue(caseList3.get(i).getEnRepayAmt()));
                    numHoursMoney = numHoursMoney.add(calHoursValue(caseList3.get(i).getMoney()));
                }
            }
            percentData.setClient(caseList3.get(0).getBusinessType());
            SysPercent percent = sysPercentMapper.findByClient(percentData);
            result = royaltyCalculate(numHoursPay, numHoursMoney, percent);
        }
        return result;
    }

    /**
     * 提成计算
     *
     * @param enRepayAmt
     * @param money
     * @param sysPercent
     */
    private BigDecimal royaltyCalculate(BigDecimal enRepayAmt, BigDecimal money, SysPercent sysPercent) {
        BigDecimal result = new BigDecimal("0.00");
        if (enRepayAmt == null) {
            enRepayAmt = new BigDecimal(0);
        }
        if (money == null) {
            money = new BigDecimal(0);
        }
        switch (sysPercent.getEnable()) {
            case "特殊1"://特殊一
                if (StringUtils.notEmpty(money) && StringUtils.notEmpty(enRepayAmt) && enRepayAmt.compareTo(money) >= 0) {
                    result = calMoneyOne(enRepayAmt, money, sysPercent);
                }
                break;
            case "特殊2"://特殊二
                result = calMoneyTwo(enRepayAmt, money, sysPercent);
                break;
            default://阶梯累加
                if (StringUtils.notEmpty(enRepayAmt)) {
                    result = calPaidMoney(enRepayAmt, sysPercent);
                }
                break;
        }
        return result;
    }

    private BigDecimal calPaidMoney(BigDecimal enRepayAmt, SysPercent sysPercent) {
        BigDecimal result = new BigDecimal("0.00");
        if (enRepayAmt == null) {
            enRepayAmt = new BigDecimal(0);
        }
        if (enRepayAmt.compareTo(sysPercent.getOdvBasic()) > 0) {
            if (enRepayAmt.compareTo(sysPercent.getOdvHighBasic()) < 0) {
                result = sysPercent.getOdvBasic().multiply(sysPercent.getOdvLow())
                        .add((enRepayAmt.subtract(sysPercent.getOdvBasic())).multiply(sysPercent.getOdvReward()));
            } else {
                result = sysPercent.getOdvBasic().multiply(sysPercent.getOdvLow())
                        .add(((sysPercent.getOdvHighBasic().subtract(sysPercent.getOdvHighBasic())).multiply(sysPercent.getOdvReward())))
                        .add(enRepayAmt.subtract(sysPercent.getOdvHighBasic()).multiply(sysPercent.getOdvHighReward()));
            }
        } else {
            result = enRepayAmt.multiply(sysPercent.getOdvLow());
        }
        return result;
    }

    private BigDecimal calMoneyOne(BigDecimal enRepayAmt, BigDecimal money, SysPercent sysPercent) {
        BigDecimal result;
        if (enRepayAmt == null) {
            enRepayAmt = new BigDecimal(0);
        }
        if (enRepayAmt.compareTo(sysPercent.getOdvBasic()) < 0) {
            result = money.multiply(sysPercent.getOdvReward())
                    .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvLow()));
        } else {
            if (enRepayAmt.compareTo(sysPercent.getOdvHighBasic()) < 0) {
                result = money.multiply(sysPercent.getOdvReward2())
                        .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvReward()));
            } else {
                result = money.multiply(sysPercent.getOdvReward3())
                        .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvReward2()));
            }
        }
        return result;
    }

    private BigDecimal calMoneyTwo(BigDecimal enRepayAmt, BigDecimal money, SysPercent sysPercent) {
        if (enRepayAmt == null) {
            enRepayAmt = new BigDecimal(0);
        }
        BigDecimal result = new BigDecimal("0.00");
        // 综合户达率
        BigDecimal hourseholdRate = new BigDecimal("0.00");
        hourseholdRate = enRepayAmt.divide(money).setScale(2, BigDecimal.ROUND_UP).multiply(CaseBaseConstant.ROUND);
        if (enRepayAmt.compareTo(sysPercent.getOdvBasic()) < 0) {
            result = enRepayAmt.multiply(sysPercent.getOdvLow());
        } else {
            if (hourseholdRate.compareTo(sysPercent.getOdvRewardRange1()) >= 0 &&
                    hourseholdRate.compareTo(sysPercent.getManageRewardRange2()) < 0) {
                result = enRepayAmt.multiply(sysPercent.getOdvReward());
            } else if (hourseholdRate.compareTo(sysPercent.getManageRewardRange2()) >= 0 &&
                    hourseholdRate.compareTo(sysPercent.getManageRewardRange5()) < 0) {
                result = enRepayAmt.multiply(sysPercent.getOdvReward2());
            } else if (hourseholdRate.compareTo(sysPercent.getManageRewardRange5()) >= 0) {
                result = enRepayAmt.multiply(sysPercent.getOdvReward3());
            }
        }
        return result;
    }

    /**
     * 根据回款金额，算户数
     *
     * @param value
     * @return
     */
    private BigDecimal calHoursValue(BigDecimal value) {
        BigDecimal result = new BigDecimal(0);
        if (value == null) {
            value = new BigDecimal(0);
        }
        if (value.compareTo(CaseBaseConstant.MLOW) < 0) {
            result.add(CaseBaseConstant.H1);
        } else if (value.compareTo(CaseBaseConstant.MMIDDLE) < 0) {
            result.add(CaseBaseConstant.H2);
        } else if (value.compareTo(CaseBaseConstant.MHIGH) < 0) {
            result.add(CaseBaseConstant.H3);
        } else {
            result.add(CaseBaseConstant.H4);
        }
        return result;
    }

}

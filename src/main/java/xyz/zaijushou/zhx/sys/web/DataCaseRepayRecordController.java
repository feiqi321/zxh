package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelReduceExportConstant;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRemarkMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/repayRecord")
public class DataCaseRepayRecordController {

    @Resource
    private DataCaseRepayRecordService dataCaseRepayRecordService;

    @Resource
    private DataCaseService dataCaseService;

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseRepayRecordEntity entity) {
        PageInfo<DataCaseRepayRecordEntity> pageInfo = dataCaseRepayRecordService.pageRepayRecordList(entity);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/showRepay")
    public Object showRepay(@RequestBody OdvPercentage entity) {
        List<DataCaseRepayRecordEntity> pageInfo = dataCaseRepayRecordService.showRepay(entity);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/querySum")
    public Object querySum(@RequestBody DataCaseRepayRecordEntity entity) {
        entity = dataCaseRepayRecordService.querySum(entity);
        return WebResponse.success(entity);
    }

    @PostMapping("/revoke")
    public Object revoke(@RequestBody DataCaseRepayRecordEntity entity) {
        if(entity == null || entity.getIds() == null || entity.getIds().length == 0) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入ids");
        }
        dataCaseRepayRecordService.revoke(entity);
        return WebResponse.success();
    }

    @PostMapping("/save")
    public Object save(@RequestBody DataCaseRepayRecordEntity entity) {
        if(entity.getRepayMoney()==null){
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入还款金额");
        }

        dataCaseRepayRecordService.save(entity);
        return WebResponse.success();
    }

    @PostMapping("/queryDataExport")
    public Object queryDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {

        String fileName = "导出还款记录查询结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List exportKeyList = new ArrayList();

        Iterator iter = repayRecordEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelRepayRecordConstant.RepayRecordExportConf repayRecordExportConf = ExcelRepayRecordConstant.RepayRecordExportConf.getEnumByKey(entry.getKey().toString());
                if (repayRecordExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(repayRecordExportConf.getAttr())) {
                    exportKeyList.add(repayRecordExportConf.getAttr());
                }
                colMap.put(repayRecordExportConf.getCol(), repayRecordExportConf.getCol());
            }
        }

        ExcelRepayRecordConstant.RepayRecordExport repayList[]= ExcelRepayRecordConstant.RepayRecordExport.values();
        List<ExcelRepayRecordConstant.RepayRecordExport> repayList2 = new ArrayList<ExcelRepayRecordConstant.RepayRecordExport>();

        for (int i=0;i<repayList.length;i++){
            ExcelRepayRecordConstant.RepayRecordExport repayListTemp = repayList[i];
            if (colMap.get(repayListTemp.getCol())!=null){
                repayList2.add(repayListTemp);
            }

        }

        repayRecordEntity.setExportKeyList(exportKeyList);
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.listRepayRecordExport(repayRecordEntity);

        ExcelUtils.exportExcel(
                list,
                repayList2.toArray(new ExcelRepayRecordConstant.RepayRecordExport[repayList2.size()]),
                fileName+ ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/pageDataExport")
    public Object pageDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {

        String fileName = "导出还款记录当前页结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List exportKeyList = new ArrayList();

        Iterator iter = repayRecordEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelRepayRecordConstant.RepayRecordExportConf repayRecordExportConf = ExcelRepayRecordConstant.RepayRecordExportConf.getEnumByKey(entry.getKey().toString());
                if (repayRecordExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(repayRecordExportConf.getAttr())) {
                    exportKeyList.add(repayRecordExportConf.getAttr());
                }
                colMap.put(repayRecordExportConf.getCol(), repayRecordExportConf.getCol());
            }
        }

        ExcelRepayRecordConstant.RepayRecordExport repayList[]= ExcelRepayRecordConstant.RepayRecordExport.values();
        List<ExcelRepayRecordConstant.RepayRecordExport> repayList2 = new ArrayList<ExcelRepayRecordConstant.RepayRecordExport>();

        for (int i=0;i<repayList.length;i++){
            ExcelRepayRecordConstant.RepayRecordExport repayListTemp = repayList[i];
            if (colMap.get(repayListTemp.getCol())!=null){
                repayList2.add(repayListTemp);
            }

        }

        repayRecordEntity.setExportKeyList(exportKeyList);
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.pageRepayRecordList(repayRecordEntity).getList();

        ExcelUtils.exportExcel(
                list,
                repayList2.toArray(new ExcelRepayRecordConstant.RepayRecordExport[repayList2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/selectDataExport")
    public Object selectDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {
        String fileName = "导出还款记录选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List exportKeyList = new ArrayList();
        Iterator iter = repayRecordEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelRepayRecordConstant.RepayRecordExportConf repayRecordExportConf = ExcelRepayRecordConstant.RepayRecordExportConf.getEnumByKey(entry.getKey().toString());
                if (repayRecordExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(repayRecordExportConf.getAttr())) {
                    exportKeyList.add(repayRecordExportConf.getAttr());
                }
                colMap.put(repayRecordExportConf.getCol(), repayRecordExportConf.getCol());
            }
        }

        ExcelRepayRecordConstant.RepayRecordExport repayList[]= ExcelRepayRecordConstant.RepayRecordExport.values();
        List<ExcelRepayRecordConstant.RepayRecordExport> repayList2 = new ArrayList<ExcelRepayRecordConstant.RepayRecordExport>();

        for (int i=0;i<repayList.length;i++){
            ExcelRepayRecordConstant.RepayRecordExport repayListTemp = repayList[i];
            if (colMap.get(repayListTemp.getCol())!=null){
                repayList2.add(repayListTemp);
            }

        }
        repayRecordEntity.setExportKeyList(exportKeyList);

        if(repayRecordEntity == null || repayRecordEntity.getIds() == null || repayRecordEntity.getIds().length == 0) {
            ExcelUtils.exportExcel(
                    new ArrayList<>(),
                    repayList2.toArray(new ExcelRepayRecordConstant.RepayRecordExport[repayList2.size()]),
                    fileName + ".xlsx",
                    response
            );
            return null;
        }

        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.listRepayRecordSelectExport(repayRecordEntity);

        ExcelUtils.exportExcel(
                list,
                repayList2.toArray(new ExcelRepayRecordConstant.RepayRecordExport[repayList2.size()]),
                "导出还款记录选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "导入还款记录", notes = "导入还款记录")
    @PostMapping("/import")
    public Object dataCaseNewCaseImport(MultipartFile file) throws IOException {
        List<DataCaseRepayRecordEntity> dataEntities = ExcelUtils.importExcel(file, ExcelRepayRecordConstant.RepayRecordImport.values(), DataCaseRepayRecordEntity.class);;
        if(dataEntities.size() == 0) {
            return WebResponse.success("添加0条数据");
        }

        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        Set<String> seqNoSet = new HashSet<>();
        for(int i = 0; i < dataEntities.size(); i ++) {
            if(dataEntities.get(i).getDataCase() == null || StringUtils.isEmpty(dataEntities.get(i).getDataCase().getSeqNo())) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            seqNoSet.add(dataEntities.get(i).getDataCase().getSeqNo());
        }

        /*DataCaseEntity queryEntity = new DataCaseEntity();
        queryEntity.setSeqNoSet(seqNoSet);
        List<DataCaseEntity> existCaseList = dataCaseService.listBySeqNoSet(queryEntity);
        Map<String, DataCaseEntity> existCaseMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existCaseMap.put(entity.getSeqNo(), entity);
        }*/
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        SysNewUserEntity confirmUser = new SysNewUserEntity();
        confirmUser.setId(userId);

        for (DataCaseRepayRecordEntity entity : dataEntities) {
            DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+entity.getDataCase().getSeqNo(),DataCaseEntity.class);
            if (dataCaseEntity ==null) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "个案序列号:" + entity.getDataCase().getSeqNo() + "不存在，请确认后重新上传");
            }
            entity.setDataCase(dataCaseEntity);
            if(entity.getCollectUser() == null || entity.getCollectUser().getId() == null) {

                if(StringUtils.isNotEmpty(entity.getDataCase().getOdv())){
                    SysNewUserEntity newUser = new SysNewUserEntity();
                    newUser.setId(Integer.parseInt(entity.getDataCase().getOdv()));
                    entity.setCollectUser(newUser);
                }else{
                    SysNewUserEntity newUser = new SysNewUserEntity();
                    entity.setCollectUser(newUser);
                }

            }
            entity.setRepayMoney(entity.getRepayMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
            if(StringUtils.isNotEmpty(entity.getSettleFlag()) && (
                    "是".equals(entity.getSettleFlag().trim()) ||
                            "Y".equals(entity.getSettleFlag().trim()) ||
                            "y".equals(entity.getSettleFlag().trim()) ||
                            "1".equals(entity.getSettleFlag().trim()) ||
                            "已结清".equals(entity.getSettleFlag().trim()) ||
                            "结清".equals(entity.getSettleFlag().trim()) ||
                            "已经结清".equals(entity.getSettleFlag().trim())
                    )) {
                entity.setSettleFlag("已结清");
            } else if(StringUtils.isNotEmpty(entity.getSettleFlag())) {
                entity.setSettleFlag("");
            } else {
                entity.setSettleFlag("未结清");
            }
            if (entity.getRepayType()!=null && StringUtils.isNotEmpty(entity.getRepayType().getName())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRepayType().getName(),SysDictionaryEntity.class);
                if(sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "还款方式:" + entity.getRepayType().getName() + "不在枚举中，请确认后重新上传");
                }
                entity.setRepayType(sysDictionaryEntity);
            }else{
                SysDictionaryEntity sysDictionaryEntity = new SysDictionaryEntity();
                entity.setRepayType(sysDictionaryEntity);
            }
           /* if (StringUtils.isNotEmpty(entity.getRemark())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRemark(),SysDictionaryEntity.class);
                if(sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "还款备注:" + entity.getRemark() + "不在枚举中，请确认后重新上传");
                }
                entity.setRemark(sysDictionaryEntity==null?"":sysDictionaryEntity.getId()+"");
            }*/

            entity.setConfirmUser(confirmUser);
            entity.setCreateUser(user);
            entity.setUpdateUser(user);
        }
        dataCaseRepayRecordService.addList(dataEntities);
        return WebResponse.success();
    }

}

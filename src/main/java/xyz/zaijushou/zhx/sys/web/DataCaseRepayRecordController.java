package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/repayRecord")
public class DataCaseRepayRecordController {

    @Resource
    private DataCaseRepayRecordService dataCaseRepayRecordService;

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseRepayRecordEntity entity) {
        PageInfo<DataCaseRepayRecordEntity> pageInfo = dataCaseRepayRecordService.pageRepayRecordList(entity);
        return WebResponse.success(pageInfo);
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
        dataCaseRepayRecordService.save(entity);
        return WebResponse.success();
    }

    @PostMapping("/queryDataExport")
    public Object queryDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.listRepayRecord(repayRecordEntity);
        Set<String> userIdsSet = new HashSet<>();
        for(DataCaseRepayRecordEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseRepayRecordEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        ExcelUtils.exportExcel(
                list,
                ExcelRepayRecordConstant.RepayRecordExport.values(),
                "导出还款记录查询结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/pageDataExport")
    public Object pageDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.pageRepayRecordList(repayRecordEntity).getList();
        Set<String> userIdsSet = new HashSet<>();
        for(DataCaseRepayRecordEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseRepayRecordEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        ExcelUtils.exportExcel(
                list,
                ExcelRepayRecordConstant.RepayRecordExport.values(),
                "导出还款记录当前页结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/selectDataExport")
    public Object selectDataExport(@RequestBody DataCaseRepayRecordEntity repayRecordEntity, HttpServletResponse response) throws IOException {
        if(repayRecordEntity == null || repayRecordEntity.getIds() == null || repayRecordEntity.getIds().length == 0) {
            ExcelUtils.exportExcel(
                    new ArrayList<>(),
                    ExcelRepayRecordConstant.RepayRecordExport.values(),
                    "导出还款记录选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                    response
            );
            return null;
        }
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordService.listRepayRecord(repayRecordEntity);
        Set<String> userIdsSet = new HashSet<>();
        for(DataCaseRepayRecordEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseRepayRecordEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        ExcelUtils.exportExcel(
                list,
                ExcelRepayRecordConstant.RepayRecordExport.values(),
                "导出还款记录选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


}

package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
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

    @Resource
    private DataCaseService dataCaseService;

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

    @ApiOperation(value = "导入还款记录", notes = "导入还款记录")
    @PostMapping("/import")
    public Object dataCaseNewCaseImport(MultipartFile file) throws IOException {
        List<DataCaseRepayRecordEntity> dataEntities = ExcelUtils.importExcel(file, ExcelRepayRecordConstant.RepayRecordImport.values(), DataCaseRepayRecordEntity.class);;
        if(dataEntities.size() == 0) {
            return WebResponse.success("添加0条数据");
        }
        Map<String, Integer> countMap = new HashMap<>();
        Set<String> seqNoSet = new HashSet<>();
        for(int i = 0; i < dataEntities.size(); i ++) {
            if(dataEntities.get(i).getDataCase() == null || StringUtils.isEmpty(dataEntities.get(i).getDataCase().getSeqNo())) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            seqNoSet.add(dataEntities.get(i).getDataCase().getSeqNo());
            countMap.put(dataEntities.get(i).getDataCase().getSeqNo(), countMap.get(dataEntities.get(i).getDataCase().getSeqNo()) == null ? 1 : countMap.get(dataEntities.get(i).getDataCase().getSeqNo()) + 1);
        }
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if(entry.getValue() > 1) {
                builder.append(entry.getKey()).append(" ");
            }
        }
        if(StringUtils.isNotEmpty(builder.toString())) {
            return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "存在重复的个案序列号：" + builder.toString());
        }
        DataCaseEntity queryEntity = new DataCaseEntity();
        queryEntity.setSeqNoSet(seqNoSet);
        List<DataCaseEntity> existCaseList = dataCaseService.listBySeqNoSet(queryEntity);
        Map<String, DataCaseEntity> existCaseMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existCaseMap.put(entity.getSeqNo(), entity);
        }
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        for (DataCaseRepayRecordEntity entity : dataEntities) {
            if (existCaseMap.containsKey(entity.getDataCase().getSeqNo())) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "个案序列号:" + entity.getDataCase().getSeqNo() + "已存在，请确认后重新上传");
            }
            entity.setDataCase(existCaseMap.get(entity.getDataCase().getSeqNo()));
            entity.setCreateUser(user);
            entity.setUpdateUser(user);
        }
        dataCaseRepayRecordService.addList(dataEntities);
        return WebResponse.success();
    }


}

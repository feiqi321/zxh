package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/催收管理")
@RestController
public class DataCollectionController {

    @Autowired
    private DataCollectionService dataCollectionService;
    @Autowired
    private SysOrganizationService sysOrganizationService;
    @Resource
    private SysUserMapper sysUserMapper;//用户业务控制层

    @ApiOperation(value = "新增催收", notes = "新增催收")
    @PostMapping("/dataCollection/save")
    public Object save(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "详情新增辅助催收", notes = "详情新增辅助催收")
    @PostMapping("/dataCollection/detailSave")
    public Object detailSave(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.detailSave(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "详情刪除辅助催收", notes = "详情刪除辅助催收")
    @PostMapping("/dataCollection/detailDel")
    public Object detailDel(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.detailDel(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改催收", notes = "修改催收")
    @PostMapping("/dataCollection/update")
    public Object update(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.update(bean);

        return WebResponse.success();

    }


    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCollection/pageDataCollection")
    public Object pageDataCollection(@RequestBody DataCollectionEntity bean) {

        List<DataCollectionEntity> list = dataCollectionService.pageDataCollectionList(bean);
        return WebResponse.success(list);

    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return user;
    }
    @ApiOperation(value = "催收管理-我的案件分頁查询", notes = "催收管理-我的案件分頁查询")
    @PostMapping("/dataCollection/pageMyCase")
    public Object pageMyCase(@RequestBody DataCollectionEntity bean) throws Exception{

        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return WebResponse.success();
        }
        //查询个人  and 查询部门
        if (user.getId()==1){
            if (bean.getsType() == 0) {

            } else if (bean.getsType() == 1  && (bean.getOdvs()==null ||  bean.getOdvs().length==0) && StringUtils.isEmpty(bean.getDept())){

            }else if (bean.getsType() == 1  && (bean.getOdvs()==null ||  bean.getOdvs().length==0)) {
                SysOrganizationEntity organizationEntity = new SysOrganizationEntity();
                if (StringUtils.isEmpty(bean.getDept())){

                }else{
                    organizationEntity.setId(Integer.parseInt(bean.getDept()));
                }

                //查询标识
                organizationEntity.setTypeFlag(1);
                List<SysOrganizationEntity> orgList = sysOrganizationService.listChildOrganization2(organizationEntity);
                Integer[] deptList = new Integer[orgList.size()];
                for (int i=0;i<orgList.size();i++){
                    SysOrganizationEntity orgEntity = orgList.get(i);
                    deptList[i] = orgEntity.getId();
                }

                bean.setDeptFlag(1);
                bean.setDepts(deptList);

            }else if (bean.getsType() == 1){
               /* SysOrganizationEntity organizationEntity = new SysOrganizationEntity();
                if (StringUtils.isEmpty(bean.getDept())){

                }else{
                    organizationEntity.setId(Integer.parseInt(bean.getDept()));
                }

                //查询标识
                organizationEntity.setTypeFlag(1);
                List<SysOrganizationEntity> orgList = sysOrganizationService.listChildOrganization2(organizationEntity);
                Integer[] deptList = new Integer[orgList.size()];
                for (int i=0;i<orgList.size();i++){
                    SysOrganizationEntity orgEntity = orgList.get(i);
                    deptList[i] = orgEntity.getId();
                }

                bean.setDeptFlag(1);
                bean.setDepts(deptList);*/

            }
        }else {
            if (bean.getsType() == 0) {
                bean.setOdv(user.getId() + "");
            } else if (bean.getsType() == 1  && (bean.getOdvs()==null ||  bean.getOdvs().length==0)) {
                SysOrganizationEntity organizationEntity = new SysOrganizationEntity();
                if (StringUtils.isEmpty(bean.getDept())){

                }else{
                    organizationEntity.setId(Integer.parseInt(bean.getDept()));
                }

                //查询标识
                organizationEntity.setTypeFlag(1);
                List<SysOrganizationEntity> orgList = sysOrganizationService.listChildOrganization2(organizationEntity);

                Integer[] deptList = new Integer[orgList.size()];
                for (int i=0;i<orgList.size();i++){
                    SysOrganizationEntity orgEntity = orgList.get(i);
                    deptList[i] = orgEntity.getId();
                }

                bean.setDeptFlag(1);
                bean.setDepts(deptList);
            }else if (bean.getsType() == 1){
                SysOrganizationEntity organizationEntity = new SysOrganizationEntity();
                if (StringUtils.isEmpty(bean.getDept())){

                }else{
                    organizationEntity.setId(Integer.parseInt(bean.getDept()));
                }

                //查询标识
                organizationEntity.setTypeFlag(1);
                List<SysOrganizationEntity> orgList = sysOrganizationService.listChildOrganization2(organizationEntity);
                Integer[] deptList = new Integer[orgList.size()];
                for (int i=0;i<orgList.size();i++){
                    SysOrganizationEntity orgEntity = orgList.get(i);
                    deptList[i] = orgEntity.getId();
                }

                bean.setDeptFlag(1);
                bean.setDepts(deptList);

            }
        }
        bean.setDept(null);
        if (StringUtils.notEmpty(bean.getName())){
            bean.setNames(bean.getName().split(","));
        }
        if (StringUtils.notEmpty(bean.getIdentNo())){
            bean.setIdentNos(bean.getIdentNo().split(","));
        }
        if (StringUtils.notEmpty(bean.getArchiveNo())){
            bean.setArchiveNos(bean.getArchiveNo().split(","));
        }
        if (StringUtils.notEmpty(bean.getCardNo())){
            bean.setCardNos(bean.getCardNo().split(","));
        }
        if (StringUtils.notEmpty(bean.getSeqno())){
            bean.setSeqnos(bean.getSeqno().split(","));
        }
        WebResponse webResponse = dataCollectionService.pageMyCase(bean);

        return webResponse;

    }

    @ApiOperation(value = "催收管理-当日电催量", notes = "催收管理-当日电催量")
    @PostMapping("/dataCollection/statistics/day")
    public Object statisticsCollectionDay(@RequestBody CollectionStatistic bean) {
        List<CollectionStatistic> list = dataCollectionService.statisticsCollectionDay(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-催收状况", notes = "催收管理-催收状况")
    @PostMapping("/dataCollection/statistics/state")
    public Object statisticsCollectionState(@RequestBody CollectionStatistic bean) {

        WebResponse webResponse = dataCollectionService.statisticsCollectionState(bean);

        return webResponse;

    }

    @ApiOperation(value = "催收管理-批次分类", notes = "催收管理-批次分类")
    @PostMapping("/dataCollection/statistics/batch")
    public Object statisticsCollectionBatch(@RequestBody CollectionStatistic bean) {

        WebResponse webResponse = dataCollectionService.statisticsCollectionBatch(bean);

        return webResponse;

    }

    @ApiOperation(value = "催收管理-我的还款统计", notes = "催收管理-我的还款统计")
    @PostMapping("/dataCollection/statistics/pay")
    public Object statisticsCollectionPay(@RequestBody CollectionStatistic bean)  throws Exception{
        WebResponse webResponse = dataCollectionService.statisticsCollectionPay(bean);
        return webResponse;
    }

    @ApiOperation(value = "案件详情-查询同批次公债催记", notes = "减免详情-查询同批次公债催记")
    @PostMapping("/case/batch/idno")
    public Object listCaseBatchIdNo(@RequestBody DataCollectionEntity bean) {

        List<DataCollectionEntity> list = dataCollectionService.listCaseBatchIdNo(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-催收员提成", notes = "催收管理-催收员提成")
    @PostMapping("/dataCollection/loadDataOdv")
    public Object loadDataOdv() {
        WebResponse webResponse = dataCollectionService.loadDataOdv();
        return webResponse;
    }

    @ApiOperation(value = "催收管理-经理提成", notes = "催收管理-经理提成")
    @PostMapping("/dataCollection/loadDataManage")
    public Object loadDataManage() {
        WebResponse webResponse = dataCollectionService.loadDataManage();
        return webResponse;
    }

    @ApiOperation(value = "催收管理-催收员提成", notes = "催收管理-催收员提成")
    @PostMapping("/dataCollection/showOdv")
    public Object showOdv(@RequestBody OdvPercentage bean) {
        WebResponse webResponse = dataCollectionService.showOdv(bean);
        return webResponse;
    }
}

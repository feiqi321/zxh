package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseSortEnum;
import xyz.zaijushou.zhx.constant.LetterSortEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.LetterService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/2/16.
 */
@Service
public class LetterServiceImpl implements LetterService {

    @Resource
    private LetterMapper letterMapper;
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private SysModuleMapper sysModuleMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;

    public WebResponse pageDataLetterInfo(Letter letter){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("id");
            letter.setSort("desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<Letter> list = new ArrayList<Letter>();

        list = letterMapper.pageDataLetterInfo(letter);

        for (int i=0;i<list.size();i++){
            Letter temp = list.get(i);
            temp.setRelationer(temp.getRelationer()==null?"":(temp.getRelationer().equals("NULL")?"":temp.getRelationer()));
            if (temp.getCollectStatus()==0){
                temp.setCollectStatusMsg("");
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if(StringUtils.isEmpty(temp.getModule())){
                temp.setModule("");
            }else{
                SysModule sysModule = new SysModule();
                sysModule.setId(Integer.parseInt(temp.getModule()));
                SysModule moduleTemp = sysModuleMapper.selectModuleById(sysModule);
                temp.setModule(moduleTemp==null?"":moduleTemp.getTitle());
            }
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getApplyer(), SysUserEntity.class);
            temp.setApplyer(user==null?"":user.getUserName());

            temp.setCaseAmtMsg(temp.getCaseAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCaseAmt().stripTrailingZeros()+""));
            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getRepayAmt().stripTrailingZeros()+""));
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));
        return webResponse;
    }

    public WebResponse pageDataLetter(Letter letter){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("id");
            letter.setSort("desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<Letter> list = new ArrayList<Letter>();
        if (letter.getStatus().equals("3")){//查询未撤销的
            list = letterMapper.pageDataLetter2(letter);
        }else {
            list = letterMapper.pageDataLetter(letter);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i=0;i<list.size();i++){
            Letter temp = list.get(i);
            if (temp.getSynergyDate()!=null) {
                try {
                    temp.setSynergyDate(sdf.format(sdf.parse(temp.getSynergyDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            temp.setRelationer(temp.getRelationer()==null?"":(temp.getRelationer().equals("NULL")?"":temp.getRelationer()));
            if (temp.getCollectStatus()==0){
                temp.setCollectStatusMsg("");
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if(StringUtils.isEmpty(temp.getModule())){
                temp.setModule("");
                temp.setModuleId("");
            }else{
                SysModule sysModule = new SysModule();
                sysModule.setId(Integer.parseInt(temp.getModule()));
                SysModule moduleTemp = sysModuleMapper.selectModuleById(sysModule);
                temp.setModuleId(temp.getModule());
                temp.setModule(moduleTemp==null?"":moduleTemp.getTitle());
            }

            temp.setCaseAmtMsg(temp.getCaseAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCaseAmt().stripTrailingZeros()+""));
            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getRepayAmt().stripTrailingZeros()+""));
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));
        return webResponse;
    }

    public void confirmSynergy(Letter letter){
        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setSynergy(1);
        dataCaseEntity.setId(letter.getCaseId());
        dataCaseMapper.updateSynergy(dataCaseEntity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        letter.setSynergyDate(sdf.format(new Date()));
        letterMapper.confirmSynergy(letter);

    }


    public void cancelLetter(Letter letter){
        letterMapper.cancelLetter(letter);
    }

    public void confirmLetter(Letter letter){
        letterMapper.confirmLetter(letter);
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
    public void addLetter(Letter letter){
        SysUserEntity mine = getUserInfo();
        DataCaseEntity request = new DataCaseEntity();
        request.setId(letter.getCaseId());
        DataCaseEntity dataCaseEntity = dataCaseMapper.findById(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        letter.setCardNo(dataCaseEntity.getCardNo());
        letter.setIdentNo(dataCaseEntity.getIdentNo());
        letter.setCaseDate(dataCaseEntity.getCaseDate());
        letter.setName(dataCaseEntity.getName());
        letter.setDeleteFlag(0);
        letter.setApplyer(mine.getId()+"");
        letter.setStatus("0");
        letter.setApplyDate(sdf.format(new Date()));
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setId(letter.getAddressId());
        dataCaseAddressMapper.updateLetterCount(dataCaseAddressEntity);

        letterMapper.insert(letter);
    }

    public WebResponse findByCaseId(Letter letter){
        List<Letter> list = letterMapper.findByCaseId(letter);
        for(int i=0;i<list.size();i++){
            Letter temp = list.get(i);
            temp.setRelationer(temp.getRelationer()==null?"":(temp.getRelationer().equals("NULL")?"":temp.getRelationer()));
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getApplyer(), SysUserEntity.class);
            temp.setApplyer(user==null?"":user.getUserName());
            if (temp.getStatus()==null || temp.getStatus().equals("0")){
                temp.setStatus("申请中");
            }else if (temp.getStatus().equals("1")){
                temp.setStatus("已审核");
            }else if (temp.getStatus().equals("2")){
                temp.setStatus("撤销");
            }
            list.set(i,temp);
        }
        return WebResponse.success(list);
    }

    public WebResponse updateModule(Letter letter){
        letterMapper.updateModule(letter);
        return WebResponse.success();
    }

    public List<LetterExportEntity> pageExportList(Letter letter){
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("t.id");
            letter.setSort("t.desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<LetterExportEntity> list = letterMapper.pageExportList(letter);
        for (int i=0;i<list.size();i++){
            LetterExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        if (list.size()>0){
            list = PageInfo.of(list).getList();
        }
        return list;
    }

    public List<LetterExportEntity> totalExportList(Letter letter){
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("id");
            letter.setSort("desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<LetterExportEntity> list = letterMapper.totalExportList(letter);
        for (int i=0;i<list.size();i++){
            LetterExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        return list;
    }

    public List<LetterExportEntity> pageInfoExportList(Letter letter){
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("t.id");
            letter.setSort("t.desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<LetterExportEntity> list = letterMapper.pageInfoExportList(letter);
        for (int i=0;i<list.size();i++){
            LetterExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        if (list.size()>0){
            list = PageInfo.of(list).getList();
        }
        return list;
    }

    public List<LetterExportEntity> totalInfoExportList(Letter letter){
        String[] clients = letter.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            letter.setClientFlag(null);
        }else{
            letter.setClientFlag("1");
        }
        String[] batchNos = letter.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            letter.setBatchNoFlag(null);
        }else{
            letter.setBatchNoFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(letter.getOrderBy())){
            letter.setOrderBy("id");
            letter.setSort("desc");
        }else {
            letter.setOrderBy(LetterSortEnum.getEnumByKey(letter.getOrderBy()).getValue());
        }
        List<LetterExportEntity> list = letterMapper.totalInfoExportList(letter);
        for (int i=0;i<list.size();i++){
            LetterExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        return list;
    }

}

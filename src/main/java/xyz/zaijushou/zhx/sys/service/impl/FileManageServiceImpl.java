package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by looyer on 2019/2/12.
 */
@Service
public class FileManageServiceImpl implements FileManageService {
    @Resource
    private DataCaseTelMapper dataCaseTelMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;
    @Resource
    private DataCaseInterestMapper dataCaseInterestMapper;
    @Resource
    private DataCaseMapper dateCaseMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DataArchiveMapper dataArchiveMapper;
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCaseCommentMapper dataCaseCommentMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Resource
    private LetterMapper letterMapper;
    @Autowired
    private DataLogService dataLogService;
    @Resource
    private SysModuleMapper sysModuleMapper;
    @Resource
    private DataCaseService dataCaseService;
    @Resource
    private DataArchiveAddressMapper dataArchiveAddressMapper;
    @Resource
    private DataArchiveTelMapper dataArchiveTelMapper;
    @Resource
    private DataArchiveRemarkMapper dataArchiveRemarkMapper;


    public WebResponse batchCaseTel(List<DataCaseTelEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();

        dataCaseTelMapper.insertBatchTel(list);
        webResponse.setMsg( "导入成功");
        webResponse.setCode("100");

        return webResponse;
    }

    @Transactional
    public WebResponse batchCaseAddress(List<DataCaseAddressEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseAddressEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getCardNo()+"@"+dataCaseAddressEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressEntity.setCaseId(temp.getId());
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressEntity.setCaseId(temp.getId());
                }else {
                    DataCaseEntity caseEntityTemp = new DataCaseEntity();
                    caseEntityTemp.setSeqNo(dataCaseAddressEntity.getSeqNo());
                    List<DataCaseEntity> caseEntityTempList = dateCaseMapper.findBySeqNo(caseEntityTemp);
                    if (caseEntityTempList.size() > 0) {
                        dataCaseAddressEntity.setCaseId(caseEntityTempList.get(0).getId());
                        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + dataCaseAddressEntity.getSeqNo(), JSONObject.toJSONString(caseEntityTempList.get(0)));
                    } else {
                        return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                    }
                }
            }
            list.set(i,dataCaseAddressEntity);
        }
        //dataCaseAddressMapper
        webResponse.setMsg("导入成功");
        webResponse.setCode("100");
        return webResponse;
    }
    @Transactional
    public WebResponse batchCaseInterest(List<DataCaseInterestEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i=0;i<list.size();i++){
            DataCaseInterestEntity dataCaseInterestEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseInterestEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getCardNo()+"@"+dataCaseInterestEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseInterestEntity.setCaseId(temp.getId());
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                    temp.setInterestDate(sdf.format(new Date()));
                    temp.setLatestOverdueMoney(dataCaseInterestEntity.getLastestDebt()+"");
                    dateCaseMapper.updateInterest(temp);
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getSeqNo(),DataCaseEntity.class);
                if (temp ==null){
                    DataCaseEntity caseEntityTemp = new DataCaseEntity();
                    caseEntityTemp.setSeqNo(dataCaseInterestEntity.getSeqNo());
                    List<DataCaseEntity> caseEntityTempList = dateCaseMapper.findBySeqNo(caseEntityTemp);
                    if (caseEntityTempList.size() > 0) {
                        temp = caseEntityTempList.get(0);
                        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + dataCaseInterestEntity.getSeqNo(), JSONObject.toJSONString(caseEntityTempList.get(0)));
                    }
                }
                if (temp!=null){
                    dataCaseInterestEntity.setCaseId(temp.getId());
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                    temp.setInterestDate(sdf.format(new Date()));
                    temp.setLatestOverdueMoney(dataCaseInterestEntity.getLastestDebt()+"");
                    dateCaseMapper.updateInterest(temp);
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }

        }

        webResponse.setMsg("导入成功");
        webResponse.setCode("100");
        return webResponse;
    }
    @Transactional
    public WebResponse batchLetter(List<Letter> list){
        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            Letter temp = list.get(i);
            DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
            if (dataCaseEntity!=null){
                temp.setCaseId(dataCaseEntity.getId());
            }else{
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写卡号和委案日期，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            list.set(i,temp);
        }
        letterMapper.batchInsert(list);
        webResponse.setMsg( "导入成功");
        webResponse.setCode("100");
        return webResponse;
    }
    @Transactional
    public WebResponse batchCaseComment(List<DataCaseEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);

            if (StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntity.getCardNo()+"@"+dataCaseEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    if (dataCaseEntity.getColor().equals("0")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("黑").getValue());
                    }else if (dataCaseEntity.getColor().equals("1")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("红").getValue());
                    }else if (dataCaseEntity.getColor().equals("2")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("蓝").getValue());
                    }
                    dateCaseMapper.updateComment(dataCaseEntity);
                    DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
                    dataCaseCommentEntity.setCaseId(temp.getId());
                    dataCaseCommentEntity.setComment(dataCaseEntity.getComment());
                    SysUserEntity user = this.getUserInfo();
                    dataCaseCommentEntity.setCreatUser(user.getId());
                    dataCaseCommentMapper.saveComment(dataCaseCommentEntity);
                    DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
                    dataCollectionEntity.setColor(dataCaseEntity.getColor());
                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    dataCollectionMapper.addColor(dataCollectionEntity);

                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntity.getSeqNo(),DataCaseEntity.class);

                if (temp ==null){
                    DataCaseEntity caseEntityTemp = new DataCaseEntity();
                    caseEntityTemp.setSeqNo(dataCaseEntity.getSeqNo());
                    List<DataCaseEntity> caseEntityTempList = dateCaseMapper.findBySeqNo(caseEntityTemp);
                    if (caseEntityTempList.size() > 0) {
                        temp = caseEntityTempList.get(0);
                        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + dataCaseEntity.getSeqNo(), JSONObject.toJSONString(caseEntityTempList.get(0)));
                    }
                }

                if (temp!=null){
                    if (dataCaseEntity.getColor().equals("0")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("黑").getValue());
                    }else if (dataCaseEntity.getColor().equals("1")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("红").getValue());
                    }else if (dataCaseEntity.getColor().equals("2")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("蓝").getValue());
                    }
                    dateCaseMapper.updateComment(dataCaseEntity);
                    DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
                    dataCaseCommentEntity.setCaseId(temp.getId());
                    dataCaseCommentEntity.setComment(dataCaseEntity.getComment());
                    SysUserEntity user = this.getUserInfo();
                    dataCaseCommentEntity.setCreatUser(user.getId());
                    dataCaseCommentMapper.saveComment(dataCaseCommentEntity);
                    DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
                    dataCollectionEntity.setColor(dataCaseEntity.getColor());
                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    dataCollectionMapper.addColor(dataCollectionEntity);

                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }
        }

        webResponse.setMsg("导入成功");
        webResponse.setCode("100");
        return webResponse;
    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
    @Transactional
    public WebResponse batchArchive(List<DataArchiveEntity> list){

        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataArchiveEntity dataArchiveEntity = list.get(i);
            DataArchiveEntity temp = dataArchiveMapper.selectByIdentNo(dataArchiveEntity);
            if (temp==null) {
                dataArchiveMapper.saveArchive(dataArchiveEntity);
                if (StringUtils.isNotEmpty(dataArchiveEntity.getAddress())) {
                    DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
                    dataArchiveAddressEntity.setArchiveId(dataArchiveEntity.getId());
                    dataArchiveAddressEntity.setAddress(dataArchiveEntity.getAddress());
                    dataArchiveAddressMapper.saveAddress(dataArchiveAddressEntity);
                }
                if (StringUtils.isNotEmpty(dataArchiveEntity.getMobile())) {
                    DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
                    dataArchiveTelEntity.setArchiveId(dataArchiveEntity.getId());
                    dataArchiveTelEntity.setTel(dataArchiveEntity.getMobile());
                    dataArchiveTelMapper.saveTel(dataArchiveTelEntity);
                }
                if (StringUtils.isNotEmpty(dataArchiveEntity.getRemark())) {
                    DataArchiveRemarkEntity dataArchiveRemarkEntity = new DataArchiveRemarkEntity();
                    dataArchiveRemarkEntity.setArchiveId(dataArchiveEntity.getId());
                    dataArchiveRemarkEntity.setRemark(dataArchiveEntity.getRemark());
                    dataArchiveRemarkMapper.saveRemark(dataArchiveRemarkEntity);
                }
            }else{
                dataArchiveEntity.setId(temp.getId());
                dataArchiveMapper.updateArchive(dataArchiveEntity);
                if (StringUtils.isNotEmpty(dataArchiveEntity.getAddress())) {
                    DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
                    dataArchiveAddressEntity.setArchiveId(temp.getId());
                    dataArchiveAddressEntity.setAddress(dataArchiveEntity.getAddress());
                    dataArchiveAddressMapper.saveAddress(dataArchiveAddressEntity);
                }
                if (StringUtils.isNotEmpty(dataArchiveEntity.getMobile())) {
                    DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
                    dataArchiveTelEntity.setArchiveId(temp.getId());
                    dataArchiveTelEntity.setTel(dataArchiveEntity.getMobile());
                    dataArchiveTelMapper.saveTel(dataArchiveTelEntity);
                }
                if (StringUtils.isNotEmpty(dataArchiveEntity.getRemark())) {
                    DataArchiveRemarkEntity dataArchiveRemarkEntity = new DataArchiveRemarkEntity();
                    dataArchiveRemarkEntity.setArchiveId(temp.getId());
                    dataArchiveRemarkEntity.setRemark(dataArchiveEntity.getRemark());
                    dataArchiveRemarkMapper.saveRemark(dataArchiveRemarkEntity);
                }
            }

        }

        webResponse.setCode("100");
        return webResponse;
    }

    @Transactional
    public WebResponse batchCollect(List<DataCollectionEntity> list){

        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> saveList = new ArrayList<DataCollectionEntity>();
        List<DataCollectionEntity> caseList = new ArrayList<DataCollectionEntity>();
        List<DataOpLog> logList = new ArrayList<DataOpLog>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        for (int i=0;i<list.size();i++){
            DataCollectionEntity dataCollectionEntity = list.get(i);
            SysUserEntity sysUserEntity = getUserInfo();
            dataCollectionEntity.setOdv(sysUserEntity.getId()+"");
            if (StringUtils.isEmpty(dataCollectionEntity.getContractDate())){
                dataCollectionEntity.setContractDate(sdf2.format(new Date()));
            }
            if (StringUtils.isEmpty(dataCollectionEntity.getSeqno())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCollectionEntity.getCardNo()+"@"+dataCollectionEntity.getCaseDate(),DataCaseEntity.class);
                if (temp ==null){
                    DataCaseEntity caseEntityTemp = new DataCaseEntity();
                    caseEntityTemp.setSeqNo(dataCollectionEntity.getSeqno());
                    List<DataCaseEntity> caseEntityTempList = dateCaseMapper.findBySeqNo(caseEntityTemp);
                    if (caseEntityTempList.size() > 0) {
                        temp = caseEntityTempList.get(0);
                        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + dataCollectionEntity.getSeqno(), JSONObject.toJSONString(caseEntityTempList.get(0)));
                    }
                }
                if (temp!=null){

                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    temp.setCollectDate(dataCollectionEntity.getContractDate());


                    dateCaseMapper.addCollectTimes(temp);
                    DataOpLog log = new DataOpLog();
                    log.setType("电话催收");
                    log.setContext("联系人："+dataCollectionEntity.getTargetName()+"，电话号码："+dataCollectionEntity.getMobile()+"[手机]，通话内容："+(dataCollectionEntity.getCollectInfo()==null?"":dataCollectionEntity.getCollectInfo())+"，催收状态： 可联本人");
                    log.setOper(getUserInfo().getId());
                    log.setOperName(getUserInfo().getUserName());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    log.setOpTime(sdf.format(new Date()));
                    log.setCaseId(dataCollectionEntity.getCaseId()+"");
                    logList.add(log);

                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCollectionEntity.getSeqno(),DataCaseEntity.class);
                if (temp!=null){
                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    temp.setCollectDate(dataCollectionEntity.getContractDate());


                    dateCaseMapper.addCollectTimes(temp);

                    DataOpLog log = new DataOpLog();
                    log.setType("电话催收");
                    log.setContext("联系人："+dataCollectionEntity.getTargetName()+"，电话号码："+dataCollectionEntity.getMobile()+"[手机]，通话内容："+(dataCollectionEntity.getCollectInfo()==null?"":dataCollectionEntity.getCollectInfo())+"，催收状态： 可联本人");
                    log.setOper(getUserInfo().getId());
                    log.setOperName(getUserInfo().getUserName());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    log.setOpTime(sdf.format(new Date()));
                    log.setCaseId(dataCollectionEntity.getCaseId()+"");
                    logList.add(log);

                }
            }
            saveList.add(dataCollectionEntity);
            if (i>0 && i%20==0){
                caseList.addAll(saveList);
                dataCollectionMapper.saveBatchCollection(saveList);
                dataLogService.saveBatchDataLog(logList);
                saveList.clear();
                logList.clear();
            }
            //list.set(i,dataCollectionEntity);

        }
        if (saveList.size()>0) {
            dataCollectionMapper.saveBatchCollection(saveList);
            caseList.addAll(saveList);
        }
        if (logList.size()>0) {
            dataLogService.saveBatchDataLog(logList);
        }

        //更新催收时间
        this.updateCollectDate(caseList);


        webResponse.setMsg("导入成功");
        webResponse.setCode("100");
        return webResponse;

    }
    @Transactional
    private void updateCollectDate(List<DataCollectionEntity> list){
        for (DataCollectionEntity dataCollectionEntity: list) {
            if (StringUtils.isNotBlank(dataCollectionEntity.getContractDate())){
                DataCaseEntity dataCaseEntity = new DataCaseEntity();
                dataCaseEntity.setId( Integer.parseInt(dataCollectionEntity.getCaseId()) );
                dataCaseEntity.setCollectDate(dataCollectionEntity.getContractDate());
                dateCaseMapper.updateCollectDate(dataCaseEntity);
            }

        }
    }


    public  void docFile() throws Exception {
        //拼一个标准的HTML格式文档
        String content = "<html><head><title>这是个测试demo</title></head><body><font color='red'>这是个测试demo的内容</font></body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
        OutputStream os = new FileOutputStream("D:\\2.doc");
        this.inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     * @param is
     * @param os
     * @throws IOException
     */
    private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {

        POIFSFileSystem fs = new POIFSFileSystem();
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

    /**
     * 把输入流里面的内容以UTF-8编码当文本取出。
     * 不考虑异常，直接抛出
     * @param ises
     * @return
     * @throws IOException
     */
    private String getContent(InputStream... ises) throws IOException {
        if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line=br.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        }
        return null;
    }


    public static void main(String args[]){

        FileManageServiceImpl editor=new FileManageServiceImpl();
        try {
            editor.docFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public String findDocString(Letter letter){
        SysModule sysModule = new SysModule();
        sysModule.setId(Integer.parseInt(letter.getModule()));
        SysModule temp = sysModuleMapper.selectModuleById(sysModule);
        String context = temp.getContext();
        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setId(letter.getCaseId());
        DataCaseDetail dataCaseDetail = dataCaseService.detail(dataCaseEntity);
        if (dataCaseDetail == null){
            return context;
        }

        letter = letterMapper.selectByPrimaryKey(letter.getId());
        Map map = new HashMap();
        map.put("${caseID}",dataCaseDetail.getId());
        map.put("${caseNo}",dataCaseDetail.getSeqNo());
        map.put("${cardNo}",dataCaseDetail.getCardNo());
        if (StringUtils.isNotEmpty(dataCaseDetail.getCardNo())) {
            map.put("${cardNoHide}", dataCaseDetail.getCardNo().substring(0, dataCaseDetail.getCardNo().length() - 4) + "****");
            map.put("${hideMidCNo}", dataCaseDetail.getCardNo().substring(0, 6) + "****"+dataCaseDetail.getCardNo().substring(11));
            map.put("${hideMidCNoSix}", dataCaseDetail.getCardNo().substring(0, 5) + "******"+dataCaseDetail.getCardNo().substring(12));
            map.put("${cardTailNo}", dataCaseDetail.getCardNo().substring(dataCaseDetail.getCardNo().length() - 4));
        }
        map.put("${name}",dataCaseDetail.getName());
        map.put("${IDNo}",dataCaseDetail.getIdentNo());
        map.put("${IDType}",dataCaseDetail.getIdentType());
        if (StringUtils.isNotEmpty(dataCaseDetail.getIdentNo())) {
            map.put("${IDNoHide}", dataCaseDetail.getCardNo().substring(0, dataCaseDetail.getCardNo().length() - 4) + "****");
            map.put("${hideMidIDNo}", dataCaseDetail.getCardNo().substring(0, 6) + "****"+dataCaseDetail.getCardNo().substring(11));
            map.put("${hideMidIDNoSix}", dataCaseDetail.getCardNo().substring(0, 5) + "******"+dataCaseDetail.getCardNo().substring(12));
        }
        map.put("${addr}",letter.getAddress());
        map.put("${apply}",letter.getApplyContext());
        map.put("${content}",letter.getSynergyResult());
        map.put("${company}",dataCaseDetail.getUnitName());
        map.put("${homeAddr}",dataCaseDetail.getHomeAddress());
        map.put("${homeAddr}",dataCaseDetail.getHomeZipCode());
        map.put("${workAddr}",dataCaseDetail.getUnitAddress());
        map.put("${wpostCode}",dataCaseDetail.getUnitZipCode());
        map.put("${mailAddr}",dataCaseDetail.getStatementAddress());
        map.put("${mpostCode}",dataCaseDetail.getStatementZipCode());
        map.put("${regAddr}",dataCaseDetail.getCensusRegisterAddress());
        map.put("${regpostCode}",dataCaseDetail.getCensusRegisterZipCode());
        map.put("${contact}",dataCaseDetail.getContactName1());
        map.put("${debt}",dataCaseDetail.getOverdueBalance());
        map.put("${lastDebt}",dataCaseDetail.getLatestOverdueMoney());
        map.put("${lastDebtEndDate}","");
        map.put("${lastDebtEndYear}","");
        map.put("${lastDebtEndMonth}","");
        map.put("${lastDebtEndDay}","");
        map.put("${aged}",dataCaseDetail.getAccountAge());
        map.put("${caseAmt}",dataCaseDetail.getMoney());
        map.put("${casePaid}",dataCaseDetail.getEnRepayAmt());
        map.put("${mCat}",dataCaseDetail.getCurrencyType());
        map.put("${rmb}",dataCaseDetail.getRmb());
        map.put("${usd}",dataCaseDetail.getForeignCurrency());
        map.put("${hkd}",dataCaseDetail.getHkd());
        if (StringUtils.isNotEmpty(dataCaseDetail.getCaseDate())) {
            map.put("${cdYear}", dataCaseDetail.getCaseDate().substring(0, 4));
            map.put("${cdMonth}", dataCaseDetail.getCaseDate().substring(5, 7));
            map.put("${cdDay}", dataCaseDetail.getCaseDate().substring(8));
        }
        map.put("${year}",new Date().getYear());
        map.put("${month}",new Date().getMonth());
        map.put("${day}",new Date().getDay());
        map.put("${hkd}",dataCaseDetail.getHkd());

        map.put("${payDL}",dataCaseDetail.getRepayDeadline());
        map.put("${credLim}",dataCaseDetail.getCreditLine());
        map.put("${account}",dataCaseDetail.getAccount());
        map.put("${product}",dataCaseDetail.getGoods());
        map.put("${fileNo}",dataCaseDetail.getArchiveNo());
        map.put("${loanDate}",dataCaseDetail.getLoanDate());

        String odv = dataCaseDetail.getOdvId();
        if (StringUtils.isNotEmpty(odv)) {
            SysNewUserEntity userEntity = new SysNewUserEntity();
            userEntity.setId(Integer.parseInt(odv));
            SysNewUserEntity user = sysUserService.getDataById(userEntity);
            map.put("${empPhone}",user == null ? "" : user.getOfficePhone());
            map.put("${empMob}",user == null ? "" : user.getMobile());
            map.put("${empName}",user == null ? "" : user.getUserName());
            map.put("${empFstName}",user == null ? "" : (user.getUserName()));
        }else{
            map.put("${empPhone}","");
            map.put("${empMob}","");
            map.put("${empName}","");
            map.put("${empFstName}","");
        }

        SysDictionaryEntity collectStatusDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ dataCaseDetail.getClient(), SysDictionaryEntity.class);
        map.put("${bank}",collectStatusDic==null?"":collectStatusDic.getName());
        map.put("${overdueNum}",dataCaseDetail.getOverduePeriods());
        map.put("${insured}","");
        map.put("${overdueDays}",dataCaseDetail.getOverdueDays());
        map.put("${paidDate}",dataCaseDetail.getLastRepayDate());
        map.put("${pcp}",dataCaseDetail.getPrinciple());
        map.put("${leftPri}",dataCaseDetail.getResidualPrinciple());
        map.put("${monthPaid}",dataCaseDetail.getMonthlyRepayments());
        map.put("${cardCat}",dataCaseDetail.getCardType());
        map.put("${creDate}",dataCaseDetail.getActiveCardDate());
        map.put("${loanEndDate}",dataCaseDetail.getLoanDeadline());
        map.put("${issuer}",dataCaseDetail.getBank());
        map.put("${minPaid}",dataCaseDetail.getMinimumPayment());
        map.put("${appNo}",dataCaseDetail.getApplyOrderNo());
        map.put("${ovdDate}",dataCaseDetail.getOverdueDate());
        map.put("${paidCount}",dataCaseDetail.getRepaidPeriods());
        map.put("${remark1}",dataCaseDetail.getRemark1());
        map.put("${remark2}",dataCaseDetail.getRemark2());
        map.put("${remark3}",dataCaseDetail.getRemark3());
        map.put("${remark4}",dataCaseDetail.getRemark4());
        map.put("${remark5}",dataCaseDetail.getRemark5());
        map.put("${remark6}",dataCaseDetail.getRemark6());

        map.put("${mob}",dataCaseDetail.getTel());
        map.put("${homePho}",dataCaseDetail.getHomeTelNumber());
        map.put("${comPho}",dataCaseDetail.getUnitTelNumber());
        map.put("${bir}",dataCaseDetail.getBirthday());
        map.put("${sex}",dataCaseDetail.getGender());


        Set<Map.Entry<String, String>> entryseSet=map.entrySet();
        for (Map.Entry<String, String> entry:entryseSet) {
            context = context.replace(entry.getKey(),String.valueOf(entry.getValue()));
        }


        return context;

    }


}

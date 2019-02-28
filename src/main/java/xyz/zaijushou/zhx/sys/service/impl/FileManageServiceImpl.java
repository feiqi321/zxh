package xyz.zaijushou.zhx.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;

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
    private DataArchiveMapper dataArchiveMapper;
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCaseCommentMapper dataCaseCommentMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层


    public WebResponse batchCaseTel(List<DataCaseTelEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        int code = 200;
        int lineCount=0;
        int sucessCount =0;
        StringBuffer errorStr = new StringBuffer("导入失败，错误总行数为:");
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:");
        for (int i=0;i<list.size();i++){
            DataCaseTelEntity dataCaseTelEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseTelEntity.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getCardNo()+"@"+dataCaseTelEntity.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                   // errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }

        }
        if (code==500){
            webResponse.setCode("500");
            errorStr.append(lineCount);
            webResponse.setMsg(errorStr.toString());
            return webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCaseTelEntity dataCaseTelEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseTelEntity.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getCardNo()+"@"+dataCaseTelEntity.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                    sucessCount =sucessCount+1;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                    sucessCount =sucessCount+1;

                }
            }

        }
        sucessStr.append(sucessCount);
        webResponse.setMsg(sucessStr.toString());
        webResponse.setCode("100");

        return webResponse;
    }


    public WebResponse batchCaseAddress(List<DataCaseAddressEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        int code = 200;
        int lineCount=0;
        StringBuffer errorStr = new StringBuffer("导入失败，错误总行数为:");
        int sucessCount =0;
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:");
        for (int i=0;i<list.size();i++){
            DataCaseAddressEntity temp = list.get(i);
            if (StringUtils.isEmpty(temp.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }

        }
        if (code==500){
            webResponse.setCode("500");
            errorStr.append(lineCount);
            webResponse.setMsg(errorStr.toString());
            return webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseAddressEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getCardNo()+"@"+dataCaseAddressEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressEntity.setCaseId(temp.getId());
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                    sucessCount =sucessCount+1;
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressEntity.setCaseId(temp.getId());
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                    sucessCount =sucessCount+1;
                }
            }

        }
        sucessStr.append(sucessCount);
        webResponse.setMsg(sucessStr.toString());
        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchCaseInterest(List<DataCaseInterestEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        int code = 200;
        int lineCount=0;
        StringBuffer errorStr = new StringBuffer("导入失败，错误总行数为:");
        int sucessCount =0;
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:");
        for (int i=0;i<list.size();i++){
            DataCaseInterestEntity temp = list.get(i);
            if (StringUtils.isEmpty(temp.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }

        }
        if (code==500){
            webResponse.setCode("500");
            errorStr.append(lineCount);
            webResponse.setMsg(errorStr.toString());
            return webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCaseInterestEntity dataCaseInterestEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseInterestEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getCardNo()+"@"+dataCaseInterestEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseInterestEntity.setCaseId(temp.getId());
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                    sucessCount =sucessCount+1;
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseInterestEntity.setCaseId(temp.getId());
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                    sucessCount =sucessCount+1;
                }
            }

        }
        sucessStr.append(sucessCount);
        webResponse.setMsg(sucessStr.toString());
        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchCaseComment(List<DataCaseEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        int code = 200;
        int lineCount=0;
        StringBuffer errorStr = new StringBuffer("导入失败，错误总行数为:");
        int sucessCount =0;
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:");
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            if (StringUtils.isEmpty(temp.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                  /*  errorStr.append(i+2);*/
                    lineCount = lineCount+1;
                    code = 500;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }

        }
        if (code==500){
            webResponse.setCode("500");
            errorStr.append(lineCount);
            webResponse.setMsg(errorStr.toString());
            return webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            if (StringUtils.isNotEmpty(dataCaseEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntity.getSeqNo(),DataCaseEntity.class);
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
                    sucessCount =sucessCount+1;
                }
            }else{

            }

        }

        sucessStr.append(sucessCount);
        webResponse.setMsg(sucessStr.toString());
        webResponse.setCode("100");
        return webResponse;
    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutStatusById(user);
    }
    public WebResponse batchArchive(List<DataArchiveEntity> list){

        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataArchiveEntity dataArchiveEntity = list.get(i);
            dataArchiveMapper.saveArchive(dataArchiveEntity);

        }


        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchCollect(List<DataCollectionEntity> list){

        WebResponse webResponse = WebResponse.buildResponse();
        int code = 200;
        int lineCount=0;
        StringBuffer errorStr = new StringBuffer("导入失败，错误总行数为:");
        int sucessCount =0;
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:");
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            if (StringUtils.isEmpty(temp.getSeqno())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                  /*  errorStr.append(i+2);*/
                    lineCount = lineCount+1;
                    code = 500;
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqno(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    //errorStr.append(i+2);
                    lineCount = lineCount+1;
                    code = 500;
                }
            }

        }
        if (code==500){
            webResponse.setCode("500");
            errorStr.append(lineCount);
            webResponse.setMsg(errorStr.toString());
            return webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCollectionEntity dataCollectionEntity = list.get(i);

            if (StringUtils.isEmpty(dataCollectionEntity.getSeqno())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCollectionEntity.getCardNo()+"@"+dataCollectionEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCollectionEntity.setName(temp.getName());
                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    dataCollectionEntity.setExpectTime(temp.getExpectTime());
                    dataCollectionEntity.setCollectTime(temp.getCollectDate());
                    dataCollectionEntity.setCollectInfo(temp.getCollectInfo());
                    dataCollectionEntity.setCaseType(temp.getCaseType());
                    dataCollectionEntity.setNewCase(temp.getNewCase());
                    dataCollectionEntity.setCaseAllotTime(temp.getSynergyDate());
                    dataCollectionEntity.setBailDate(temp.getCaseDate());
                    dataCollectionEntity.setCaseStatus(temp.getStatus());
                    dataCollectionEntity.setAccountAge(temp.getAccountAge());
                    dataCollectionEntity.setEnRepayAmt(temp.getEnRepayAmt());
                    dataCollectionEntity.setPayName("");
                    dataCollectionEntity.setPayMethod("");
                    dataCollectionEntity.setConfimName("");
                    dataCollectionEntity.setConfimTime("");
                    dataCollectionEntity.setMoney(temp.getMoney());
                    dataCollectionEntity.setBalance(temp.getBalance());
                    dataCollectionEntity.setmVal(new BigDecimal(temp.getMVal()));
                    dataCollectionEntity.setLastFollDate(temp.getCollectDate());
                    dataCollectionEntity.setNextFollDate(temp.getNextFollowDate()==null?"":temp.getNextFollowDate().toString());
                    dataCollectionEntity.setCountFollow(temp.getCollectTimes()+"");
                    dataCollectionEntity.setLastPhoneTime(temp.getLastCall());
                    dataCollectionEntity.setLeaveDays("");
                    dataCollectionEntity.setReduceAmt(new BigDecimal(0));
                    dataCollectionEntity.setReduceStatus(temp.getReduceStatus());
                    dataCollectionEntity.setReduceUpdateTime("");
                    dataCollectionEntity.setReduceReferTime("");
                    dataCollectionMapper.saveCollection(dataCollectionEntity);
                    sucessCount =sucessCount+1;
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCollectionEntity.getSeqno(),DataCaseEntity.class);
                if (temp!=null){
                    dataCollectionEntity.setCaseId(temp.getId()+"");
                    dataCollectionEntity.setName(temp.getName());
                    dataCollectionEntity.setExpectTime(temp.getExpectTime());
                    dataCollectionEntity.setCollectTime(temp.getCollectDate());
                    dataCollectionEntity.setCollectInfo(temp.getCollectInfo());
                    dataCollectionEntity.setCaseType(temp.getCaseType());
                    dataCollectionEntity.setNewCase(temp.getNewCase());
                    dataCollectionEntity.setCaseAllotTime(temp.getSynergyDate());
                    dataCollectionEntity.setBailDate(temp.getCaseDate());
                    dataCollectionEntity.setCaseStatus(temp.getStatus());
                    dataCollectionEntity.setAccountAge(temp.getAccountAge());
                    dataCollectionEntity.setEnRepayAmt(temp.getEnRepayAmt());
                    dataCollectionEntity.setPayName("");
                    dataCollectionEntity.setPayMethod("");
                    dataCollectionEntity.setConfimName("");
                    dataCollectionEntity.setConfimTime("");
                    dataCollectionEntity.setMoney(temp.getMoney());
                    dataCollectionEntity.setBalance(temp.getBalance());
                    dataCollectionEntity.setmVal(new BigDecimal(temp.getMVal()));
                    dataCollectionEntity.setLastFollDate(temp.getCollectDate());
                    dataCollectionEntity.setNextFollDate(temp.getNextFollowDate()==null?"":temp.getNextFollowDate().toString());
                    dataCollectionEntity.setCountFollow(temp.getCollectTimes()+"");
                    dataCollectionEntity.setLastPhoneTime(temp.getLastCall());
                    dataCollectionEntity.setLeaveDays("");
                    dataCollectionEntity.setReduceAmt(new BigDecimal(0));
                    dataCollectionEntity.setReduceStatus(temp.getReduceStatus());
                    dataCollectionEntity.setReduceUpdateTime("");
                    dataCollectionEntity.setReduceReferTime("");
                    dataCollectionMapper.saveCollection(dataCollectionEntity);
                    sucessCount =sucessCount+1;
                }
            }


        }

        sucessStr.append(sucessCount);
        webResponse.setMsg(sucessStr.toString());
        webResponse.setCode("100");
        return webResponse;

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


}

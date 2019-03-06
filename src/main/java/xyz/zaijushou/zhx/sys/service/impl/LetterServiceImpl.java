package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.LetterService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;

import javax.annotation.Resource;
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
    private SysDictionaryMapper dictionaryMapper;
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private SysModuleMapper sysModuleMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;

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
        List<Letter> list = new ArrayList<Letter>();
        if (letter.getStatus().equals("3")){//查询未撤销的
            list = letterMapper.pageDataLetter2(letter);
        }else {
            list = letterMapper.pageDataLetter(letter);
        }
        for (int i=0;i<list.size();i++){
            Letter temp = list.get(i);
            if (temp.getCollectStatus()==0){
                temp.setCollectStatusMsg("");
            }else{
                List<SysDictionaryEntity> dictList = dictionaryMapper.getDataById(temp.getCollectStatus());
                if (dictList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = dictList.get(0);
                    temp.setCollectStatusMsg(sysDictionaryEntity.getName());
                }
            }
            if(StringUtils.isEmpty(temp.getModule())){
                temp.setModule("");
            }else{
                SysModule sysModule = new SysModule();
                sysModule.setId(Integer.parseInt(temp.getModule()));
                SysModule moduleTemp = sysModuleMapper.selectModuleById(sysModule);
                temp.setModule(moduleTemp==null?"":moduleTemp.getTitle());
            }
            temp.setCaseAmtMsg(temp.getCaseAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCaseAmt()+""));
            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"": "￥"+FmtMicrometer.fmtMicrometer(temp.getRepayAmt()+""));
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


    public void addLetter(Letter letter){
        DataCaseEntity request = new DataCaseEntity();
        request.setId(letter.getCaseId());
        DataCaseEntity dataCaseEntity = dataCaseMapper.findById(request);
        letter.setCardNo(dataCaseEntity.getCardNo());
        letter.setIdentNo(dataCaseEntity.getIdentNo());
        letter.setCaseDate(dataCaseEntity.getCaseDate());
        letter.setName(dataCaseEntity.getName());
        letter.setDeleteFlag(0);
        letter.setStatus("0");
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setId(letter.getAddressId());
        dataCaseAddressMapper.updateLetterCount(dataCaseAddressEntity);

        letterMapper.insert(letter);
    }

    public WebResponse findByCaseId(Letter letter){
        List<Letter> list = letterMapper.findByCaseId(letter);
        return WebResponse.success(list);
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
        List<LetterExportEntity> list = letterMapper.pageExportList(letter);
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
        List<LetterExportEntity> list = letterMapper.totalExportList(letter);

        return list;
    }

}

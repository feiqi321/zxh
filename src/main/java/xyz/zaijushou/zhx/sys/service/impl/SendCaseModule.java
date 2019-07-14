package xyz.zaijushou.zhx.sys.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by looyer on 2019/7/13.
 */
public class SendCaseModule {
    private static Logger log = LoggerFactory.getLogger(SendCaseModule.class);
    //案件按照金额排序
    private void sortCases(List<DataCaseEntity> caseHeads) {
        Collections.sort(caseHeads, new Comparator<DataCaseEntity>() {
            @Override
            public int compare(DataCaseEntity o1, DataCaseEntity o2) {
                return (int) (o2.getMoney().doubleValue() - o1.getMoney().doubleValue()) * 1000;
            }
        });
    }

    //案件按照用户金额排序
    private void sortUserMoney(List<DataCaseEntity> Users) {
        Collections.sort(Users, new Comparator<DataCaseEntity>() {
            @Override
            public int compare(DataCaseEntity o1, DataCaseEntity o2) {
                return (int) (o2.getMoney().doubleValue() - o1.getMoney().doubleValue()) * 1000;
            }
        });
    }

    //案件按照用户百分比排序
    private void sortUserRate(List<DataCaseEntity> Users) {
        Collections.sort(Users, new Comparator<DataCaseEntity>() {
            @Override
            public int compare(DataCaseEntity o1, DataCaseEntity o2) {
                return (int) (Double.parseDouble(o2.getPercent()) - Double.parseDouble(o1.getPercent())) * 1000;
            }
        });
    }

    //案件按照用户百分比排序 公用
    public static void publicSortUserRate(List<DataCaseEntity> Users) {
        Collections.sort(Users, new Comparator<DataCaseEntity>() {
            @Override
            public int compare(DataCaseEntity o1, DataCaseEntity o2) {
                return (int) (Double.parseDouble(o2.getPercent()) - Double.parseDouble(o1.getPercent())) * 1000;
            }
        });
    }

    //案件按照用户百分比排序 公用
    public static void publicSortUserMoney(List<DataCaseEntity> Users) {
        Collections.sort(Users, new Comparator<DataCaseEntity>() {
            @Override
            public int compare(DataCaseEntity o1, DataCaseEntity o2) {
                BigDecimal thisMoney = (o1.getMoney()==null?new BigDecimal(0):o1.getMoney()).divide(new BigDecimal(o1.getPercent()==null?"1":o1.getPercent()),BigDecimal.ROUND_HALF_UP);
                BigDecimal dataMoney = (o2.getMoney()==null?new BigDecimal(0):o2.getMoney()).divide(new BigDecimal(o2.getPercent()==null?"1":o2.getPercent()),BigDecimal.ROUND_HALF_UP);
                if (o1.getMoney()!=null && o2.getMoney()!=null && o1.getMoney().compareTo(new BigDecimal(0))==0 && o2.getMoney().compareTo(new BigDecimal(0))==0){
                    return o1.getPercent().compareTo(o2.getPercent());
                }else {
                    return thisMoney.compareTo(dataMoney);  // 根据年龄升序排列，降序修改相减顺序即可
                }
            }
        });
    }


    //*********************************************新版催收公司分案相关2019-07-13************************************************//
    public Map<String, List<DataCaseEntity>> authSend(List<DataCaseEntity> list, String[] odvs, Map percentMap, Integer sendType, Integer authType,double totalPercent){
        Map<String, List<DataCaseEntity>> map = new HashMap<String, List<DataCaseEntity>>();
        double percentTotal= 0;


        List<DataCaseEntity> users = new ArrayList<DataCaseEntity>();
        for (int i=0;i<odvs.length;i++){
            DataCaseEntity user = new DataCaseEntity();

            user.setOdv(odvs[i]);
            user.setPercent(percentMap.get(odvs[i])==null?"1":percentMap.get(odvs[i]).toString());
            user.setMoney(new BigDecimal(0));

            users.add(user);
        }

        if (authType==1){
            users = this.updateAutoCaseSplit1(list,users,sendType);
        }else if (authType==2){
            users = this.updateAutoCaseSplit2(list,users,sendType,totalPercent);
        }
        for (DataCaseEntity user : users){
            map.put(user.getOdv(),user.getDataList());
        }

        return map;
    }
    //按照數量分配
    public List<DataCaseEntity> updateAutoCaseSplit1(List<DataCaseEntity> cases,List<DataCaseEntity> users,Integer sendType) {

        Integer casecount=cases.size();//案件个数
        Integer leftCount = cases.size();

            for (int i=0;i<users.size();i++){
                DataCaseEntity userCase  = users.get(i);
                double userCaseCount=casecount*Double.parseDouble(userCase.getPercent())/casecount;
                Integer count = 0;
                if (i==(users.size()-1)){
                    count = leftCount;
                }else{
                    count=(int)Math.floor(userCaseCount);
                    leftCount = leftCount - count;
                }

                int num = 0;
                while (true){

                    if (count > 0 && userCase.getSplitCount() < count && cases.size() > 0 && num<cases.size()) {//此用户被分到的案子大于一个并且不大于应分配的数时
                        DataCaseEntity addcase = cases.get(num);//取第一个案件
                        if (sendType==3){
                            if (!userCase.getOdv().equals(addcase.getOdv())){
                                userCase.addCaseHead(addcase);//中
                                log.debug("分配案件金额"+addcase.getMoney());
                                cases.remove(num);//清除分过的元素
                            }else if(num<cases.size()){
                                num = num +1 ;
                            }else{
                                if (i==0 && i==(users.size()-1)){

                                }else if(i==0){
                                    users.get(i+1).addCaseHead(addcase);//中
                                    log.debug("分配案件金额"+addcase.getMoney());
                                    cases.remove(num);//清除分过的元素
                                    break;
                                }else{
                                    users.get(i-1).addCaseHead(addcase);//中
                                    log.debug("分配案件金额"+addcase.getMoney());
                                    cases.remove(num);//清除分过的元素
                                    break;
                                }

                            }
                        }else{
                            userCase.addCaseHead(addcase);//中
                            log.debug("分配案件金额"+addcase.getMoney());
                            cases.remove(num);//清除分过的元素
                            break;
                        }

                    }else{
                        break;
                    }
                }

            }
        while(true){
            if (cases.size()>0){
                this.ontinue1(cases,users,sendType);
            }else{
                break;
            }
        }

        return users;
    }
    //数量分配第一轮没有分配的重新分配一轮
    public List<DataCaseEntity> ontinue1(List<DataCaseEntity> cases,List<DataCaseEntity> users,Integer sendType){
        Integer casecount=cases.size();//案件个数
        Integer leftCount = cases.size();
        while (true){
            if (cases.size()==0){
                break;
            }
            for (int i=0;i<users.size();i++){
                DataCaseEntity userCase  = users.get(i);
                double userCaseCount=casecount*Double.parseDouble(userCase.getPercent())/casecount;
                Integer count = 0;
                if (i==(users.size()-1)){
                    count = leftCount;
                }else{
                    count=(int)Math.floor(userCaseCount);
                    leftCount = leftCount - count;
                }

                int num = 0;
                while (true){

                    if (count > 0 && cases.size() > 0 && num<cases.size()) {//此用户被分到的案子大于一个并且不大于应分配的数时
                        DataCaseEntity addcase = cases.get(num);//取第一个案件
                        if (sendType==3){
                            if (!userCase.getOdv().equals(addcase.getOdv())){
                                userCase.addCaseHead(addcase);//中
                                log.debug("分配案件金额"+addcase.getMoney());
                                cases.remove(num);//清除分过的元素
                            }else if(num<cases.size()){
                                num = num +1 ;
                            }else{
                                if (i==0 && i==(users.size()-1)){

                                }else if(i==0){
                                    users.get(i+1).addCaseHead(addcase);//中
                                    log.debug("分配案件金额"+addcase.getMoney());
                                    cases.remove(num);//清除分过的元素
                                    break;
                                }else{
                                    users.get(i-1).addCaseHead(addcase);//中
                                    log.debug("分配案件金额"+addcase.getMoney());
                                    cases.remove(num);//清除分过的元素
                                    break;
                                }

                            }
                        }else{
                            userCase.addCaseHead(addcase);//中
                            log.debug("分配案件金额"+addcase.getMoney());
                            cases.remove(num);//清除分过的元素
                            break;
                        }

                    }else{
                        break;
                    }
                }

            }
        }
        return users;
    }
    //按照金额分配
    public List<DataCaseEntity> updateAutoCaseSplit2(List<DataCaseEntity> heads,List<DataCaseEntity> users,Integer sendType,double totalPercent) {

        // 进行分案处理 1.案件金额排序
        sortCases(heads);
        Integer casecount=heads.size();//案件个数
        //将用户排序打乱
        Collections.shuffle(users);

        List<DataCaseEntity> caselist=new ArrayList<DataCaseEntity>();//剩余案件
        caselist=heads;
        Integer count=1;//判断是第几次取值
        Integer lastcount=casecount;

        while(true){
            int size = users.size();//有待分案件
            if(caselist.size()>0 && caselist.size()>lastcount){//有待收案件用户
                //获取每个人得多少值，计算之后剩余的案件随机分配
                if(count%2 != 0){//当为奇数时从头取。为偶数时从尾取
                    //按案件金额排序用户 小的先取
                    sortUserMoney(users);
                    //log.debug("催收员排序："+users.toString());
                    for (int i = users.size()-1; i >= 0 ; i--) {
                        gettopcase(caselist,users.get(i),casecount,sendType,totalPercent);//从头取案件
                        log.debug("催收员："+users.get(i).getOdv()+"分后总金额"+users.get(i).getMoney());
                    }
                    count=count+1;
                }else{//尾 按案件金额排序用户 大的先取
                    sortUserMoney(users);
                    //log.debug("催收员排序："+users.toString());
                    for (DataCaseEntity  userCase : users) {
                        getendcase(caselist, userCase,casecount,sendType,totalPercent);//从尾取案件
                        log.debug("催收员："+userCase.getOdv()+"分后总金额"+userCase.getMoney());
                    }
                    count=count+1;
                }
            }else if (caselist.size() >0 && caselist.size() <= lastcount ){//剩余几个分不出去了按百分比从大到小依次分一波
                sortUserRate(users);
                //log.debug("催收员排序："+users.toString());
                for (DataCaseEntity  userCase : users) {
                    getcase(caselist, userCase,sendType);
                    log.debug("催收员："+userCase.getOdv()+"分后总金额"+userCase.getMoney());
                }
            }else{
                break;
            }
        }
        return  users;
    }

    private static boolean chackUserTotalNum(List<DataCaseEntity> users) {
        boolean flag = true;
        double samenum = users.get(0).getMoney().doubleValue();
        for (DataCaseEntity user : users) {
            if(user.getMoney().doubleValue() != samenum){
                flag = false;
            }
        }
        return flag;
    }

    private  void gettopcase(List<DataCaseEntity> cases, DataCaseEntity userCase,Integer casecount,Integer sendType,double totalPercent) {
        double userCaseCount=casecount*Double.parseDouble(userCase.getPercent())/totalPercent;
        Integer count=(int)Math.floor(userCaseCount);
        int num = 0;
        if (count > 0 && userCase.getSplitCount() != count && cases.size() > 0) {//此用户被分到的案子大于一个并且不大于应分配的数时
            while (true){
                if (num>=cases.size()){
                    break;
                }
                DataCaseEntity addcase = cases.get(num);//取第一个案件
                if (sendType==3){
                    if (!userCase.getOdv().equals(addcase.getOdv())){
                        userCase.addCaseHead(addcase);//中
                        log.debug("分配案件金额"+addcase.getMoney());
                        cases.remove(num);//清除分过的元素
                        break;
                    }else if(num<cases.size()){
                        num = num +1 ;
                    }else{
                        break;
                    }
                }else{
                    userCase.addCaseHead(addcase);//中
                    log.debug("分配案件金额"+addcase.getMoney());
                    cases.remove(num);//清除分过的元素
                    break;
                }

            }

        }
    }

    private  void getendcase(List<DataCaseEntity> cases, DataCaseEntity userCase,Integer casecount,Integer sendType,double totalPercent) {
        double userCaseCount=casecount*Double.parseDouble(userCase.getPercent())/totalPercent;
        Integer count=(int)Math.floor(userCaseCount);
        if (count > 0 && userCase.getSplitCount() != count && cases.size() > 0 ) {//此用户被分到的案子大于一个并且不大于应分配的数时
            int num = cases.size() - 1;
            while (true){
                if (num>=cases.size()){
                    break;
                }
                DataCaseEntity addcase = cases.get(num);//取第一个案件
                if (sendType==3){
                    if (!userCase.getOdv().equals(addcase.getOdv())){
                        userCase.addCaseHead(addcase);//中
                        log.debug("分配案件金额"+addcase.getMoney());
                        cases.remove(num);//清除分过的元素
                        break;
                    }else if(num >0 ){
                        num = num -1 ;
                    }else{
                        break;
                    }
                }else{
                    userCase.addCaseHead(addcase);//中
                    log.debug("分配案件金额"+addcase.getMoney());
                    cases.remove(num);//清除分过的元素
                    break;
                }

            }



        }
    }

    private  void getcase(List<DataCaseEntity> cases, DataCaseEntity userCase,Integer sendType) {
        if (cases.size() > 0) {//此用户被分到的案子大于一个并且不大于应分配的数时
            int num = 0;
            while (true){
                if (num>=cases.size()){
                    break;
                }
                DataCaseEntity addcase = cases.get(num);//取第一个案件

                if (sendType==3) {
                    if (!userCase.getOdv().equals(addcase.getOdv())) {
                        userCase.addCaseHead(addcase);//中
                        log.debug("分配案件金额" + addcase.getMoney());
                        cases.remove(num);//清除分过的元素
                        break;
                    } else if (num < cases.size()) {
                        num = num + 1;
                    } else {
                        break;
                    }
                }else{
                    userCase.addCaseHead(addcase);//中
                    log.debug("分配案件金额" + addcase.getMoney());
                    cases.remove(num);//清除分过的元素
                    break;
                }
            }
        }
    }



}

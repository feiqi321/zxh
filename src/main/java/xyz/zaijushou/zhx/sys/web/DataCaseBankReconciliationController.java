package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RepayTypeEnum;

@RestController
@RequestMapping("/bankReconciliation")
public class DataCaseBankReconciliationController {


    @PostMapping("/listRepayType")
    public Object listRepayType() {
        JSONArray typeList = new JSONArray();
        JSONObject typeAll = new JSONObject();
        typeAll.put("code", "");
        typeAll.put("typeName", "全部");
        typeList.add(typeAll);
        for(RepayTypeEnum value : RepayTypeEnum.values()) {
            JSONObject type = new JSONObject();
            type.put("code", value.getCode());
            type.put("typeName", value.getTypeName());
            typeList.add(type);
        }
        return WebResponse.success(typeList);
    }

}

package xyz.zaijushou.zhx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuTest {

    private List<JSONObject> singleMenus = new ArrayList<>();

    private static Logger logger = LoggerFactory.getLogger(MenuTest.class);

    @Test
    public void listMenus() {
        String menus = "[\n" +
                "    {\n" +
                "        \"menuUrl\": \"/home-page\",\n" +
                "        \"leafNode\": 1,\n" +
                "        \"menuLabel\": \"首页\",\n" +
                "        \"level\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"数据管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"案件导入\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"未导入批次\",\n" +
                "                        \"menuUrl\": \"/data-case-no-import\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"已导入批次\",\n" +
                "                        \"menuUrl\": \"/data-case-imported\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"批次管理\",\n" +
                "                \"menuUrl\": \"/data-batch-manage\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"案件管理\",\n" +
                "                \"menuUrl\": \"/data-case-manage\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"催记管理\",\n" +
                "                \"menuUrl\": \"/data-memorize-manage\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"案人档案管理\",\n" +
                "                \"menuUrl\": \"/data-file-manage\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"催收管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"我的案件\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"我的案件\",\n" +
                "                        \"menuUrl\": \"/collect-my-case\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"催收状况统计\",\n" +
                "                        \"menuUrl\": \"/collect-status-statistics\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"我的还款统计\",\n" +
                "                        \"menuUrl\": \"/collect-repayment-statistics\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"部门案件\",\n" +
                "                \"menuUrl\": \"/collect-departmental-case\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"来电查询\",\n" +
                "                \"menuUrl\": \"/collect-call-inquiry\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"主管协催\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"协催申请\",\n" +
                "                        \"menuUrl\": \"/collect-co-application\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"待协催\",\n" +
                "                        \"menuUrl\": \"/collect-to-be-reminded\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"已完成\",\n" +
                "                        \"menuUrl\": \"/collect-finished\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"协催管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"待处理信函\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"信函申请\",\n" +
                "                        \"menuUrl\": \"/synergistic-letter-application\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"待发信函\",\n" +
                "                        \"menuUrl\": \"/synergistic-to-be-sent\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"信函记录\",\n" +
                "                \"menuUrl\": \"/synergistic-letter-record\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"协催记录\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"协催申请\",\n" +
                "                        \"menuUrl\": \"/synergistic-application\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"待办协催\",\n" +
                "                        \"menuUrl\": \"/synergistic-to-be-precessed\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"协催记录\",\n" +
                "                \"menuUrl\": \"/synergistic-record\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"待银行对账\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"待银行对账\",\n" +
                "                        \"menuUrl\": \"/synergistic-bank-reconciliation\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"已作废\",\n" +
                "                        \"menuUrl\": \"/synergistic-canceld\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"还款记录\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"还款记录\",\n" +
                "                        \"menuUrl\": \"/synergistic-repayment-record\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"已撤销\",\n" +
                "                        \"menuUrl\": \"/synergistic-revoked\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"诉讼管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"诉讼申请\",\n" +
                "                \"menuUrl\": \"/litigation-apply\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"诉讼案件\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"我的诉讼案件\",\n" +
                "                        \"menuUrl\": \"/litigation-my\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuLabel\": \"全部诉讼案件\",\n" +
                "                        \"menuUrl\": \"/litigation-all\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"减免管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"减免结果管理\",\n" +
                "                \"menuUrl\": \"/relief-management\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"统计报表\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 0,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"电催统计\",\n" +
                "                \"children\": [\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuUrl\": \"/statistics-day\",\n" +
                "                        \"menuLabel\": \"单日统计\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuUrl\": \"/statistics-month\",\n" +
                "                        \"menuLabel\": \"月度统计\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"leafNode\": 1,\n" +
                "                        \"level\": 3,\n" +
                "                        \"menuUrl\": \"/statistics-day-action\",\n" +
                "                        \"menuLabel\": \"每日动作统计\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"员工管理\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"在职员工管理\",\n" +
                "                \"menuUrl\": \"/member-in\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"离职员工管理\",\n" +
                "                \"menuUrl\": \"/member-off\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"leafNode\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"menuLabel\": \"系统设置\",\n" +
                "        \"children\": [\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"枚举设置\",\n" +
                "                \"menuUrl\": \"/setting-enum\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"模板设置\",\n" +
                "                \"menuUrl\": \"/setting-template\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"操作日志查询\",\n" +
                "                \"menuUrl\": \"/setting-log\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"部门设置\",\n" +
                "                \"menuUrl\": \"/setting-department\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"leafNode\": 1,\n" +
                "                \"level\": 2,\n" +
                "                \"menuLabel\": \"角色设置\",\n" +
                "                \"menuUrl\": \"/setting-role\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        JSONArray menuList = JSONArray.parseArray(menus);
        ergodicList(menuList, "0");
        for(int i = 0; i < singleMenus.size(); i ++) {
            JSONObject menu = singleMenus.get(i);
            logger.info(menu.getString("id") + "," + menu.getString("menuLabel") + "," + menu.getString("level") + "," + menu.getString("leafNode") + "," + menu.getString("menuUrl"));
        }

    }

    public void ergodicList(JSONArray menuList, String parentId) {
        for (int i = 0; i < menuList.size(); i++) {
            JSONObject menu = menuList.getJSONObject(i);
            menu.put("id", parentId + "" + i);
            if (menu.get("children") != null) {
                ergodicList(menu.getJSONArray("children"), parentId + "" + i);
                menu.remove("children");
            }
            singleMenus.add(menu);

        }

    }

}

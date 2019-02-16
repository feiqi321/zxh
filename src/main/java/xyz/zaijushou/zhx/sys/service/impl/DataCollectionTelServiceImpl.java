package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionTelServiceImpl implements DataCollectionTelService {
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Resource
    private SysUserService sysUserService;//用户业务控制层

    @Override
    public PageInfo<CollectionStatistic> pageCollectionDay(CollectionStatistic bean){
        PageInfo<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionDay(bean);
        return  list;
    }

    @Override
    public PageInfo<CollectionStatistic> pageCollectionMonth(CollectionStatistic bean){
        PageInfo<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionMonth(bean);
        return  list;
    }

    @Override
    public PageInfo<CollectionStatistic> pageCollectionDayAction(CollectionStatistic bean){
        PageInfo<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionDayAction(bean);
        return  list;
    }
}

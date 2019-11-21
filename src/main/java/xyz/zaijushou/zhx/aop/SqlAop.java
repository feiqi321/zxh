package xyz.zaijushou.zhx.aop;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

/**
 * 对serviceImpl中的page*方法加分页操作
 */

@Aspect
@Component
public class SqlAop {

    private static Logger logger = LoggerFactory.getLogger(SqlAop.class);

    @Value("${xyz.zaijushou.zhx.page.start-page}")
    private Integer startPage;

    @Value("${xyz.zaijushou.zhx.page.default-page-size}")
    private Integer defaultPageSize;

    @Before("execution(* xyz.zaijushou.zhx..*ServiceImpl.page*(..))")
    public void doBefore(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            CommonEntity commonEntity = (CommonEntity) args[0];
            if (commonEntity == null) {
                commonEntity = new CommonEntity();
            }
            if (commonEntity.getPageNum() == null || commonEntity.getPageNum() <= 0) {
                commonEntity.setPageNum(startPage);
            }
            if (commonEntity.getPageSize() == null || commonEntity.getPageSize() <= 0) {
                commonEntity.setPageSize(defaultPageSize);
            }
            PageHelper.startPage(commonEntity.getPageNum(), commonEntity.getPageSize());
        } catch (Exception e) {
            logger.error("pageHelper aop 异常：{}", e);
        }
    }

    @Before("execution(* xyz.zaijushou.zhx.sys.web.*.*(..))")
    public void controller(JoinPoint point) {
        PageHelper.clearPage();

    }


    @Before("(" +
            "   (execution(* xyz.zaijushou.zhx..*Mapper.save*(..))) || " +
            "   (execution(* xyz.zaijushou.zhx..*Mapper.update*(..))) || " +
            "   (execution(* xyz.zaijushou.zhx..*Mapper.delete*(..)))" +
            ") && " +
            "!(" +
            "   (execution(* xyz.zaijushou.zhx.sys.dao.SysOperationLogMapper.*(..))) ||" +
            "   (execution(* xyz.zaijushou.zhx.sys.dao.SysConfigMapper.*(..))) ||" +
            "   (execution(* xyz.zaijushou.zhx.sys.dao.ForbiddenWordsMapper.*(..)))" +
            ")"
    )
    public void doInsertUpdateDeleteBefore(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            CommonEntity commonEntity = (CommonEntity) args[0];
            Integer userId = new Integer(1);
            try {
                userId = JwtTokenUtil.tokenData().getInteger("userId");
            }catch(Exception e){
                userId = 1;
            }
            SysUserEntity user = new SysUserEntity();
            user.setId(userId);
            commonEntity.setUpdateUser(user);
            String methodName = joinPoint.getSignature().getName();
            if(methodName.startsWith("save")) {
                commonEntity.setCreateUser(user);
            }
        } catch (Exception e) {
            logger.error("pageHelper aop 异常：{}", e);
        }
    }

}

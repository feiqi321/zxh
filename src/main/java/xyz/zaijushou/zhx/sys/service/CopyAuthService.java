package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.CopyAuth;

import java.util.List;

/**
 * Created by looyer on 2019/8/3.
 */
public interface CopyAuthService {

    public List<CopyAuth> list();

    public void update(CopyAuth copyAuth);

}

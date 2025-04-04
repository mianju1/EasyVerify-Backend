package cn.mianju.strategy.function.impl;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.api.LoginCodeVO;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.FunctionStrategy;
import com.alibaba.fastjson2.JSON;

public class LoginCodeStrategy implements FunctionStrategy {

    @Override
    public RestBean<?> execute(TUserService tUserService, String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception {
        LoginCodeVO loginCodeVO = JSON.parseObject(body, LoginCodeVO.class);
        return tUserService.loginCode(loginCodeVO, interfaceInfo);
    }
}

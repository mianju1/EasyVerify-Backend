package cn.mianju.strategy.function.impl;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.api.RegisterUserVO;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.FunctionStrategy;
import com.alibaba.fastjson2.JSON;

import java.security.NoSuchAlgorithmException;

public class RegisterUserStrategy implements FunctionStrategy {


    @Override
    public RestBean<?> execute(TUserService tUserService,String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws NoSuchAlgorithmException {
        RegisterUserVO registerUserVO = JSON.parseObject(body, RegisterUserVO.class);
        String message = tUserService.registerUser(registerUserVO, interfaceInfo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}

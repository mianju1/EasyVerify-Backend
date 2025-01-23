package cn.mianju.strategy.function.impl;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.response.ResponseVO;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.FunctionStrategy;
import cn.mianju.utils.EncryptUtils;

import java.util.Objects;

public class GetVersionStrategy implements FunctionStrategy {

    @Override
    public RestBean<?> execute(TUserService tUserService,String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception {
        ResponseVO response = tUserService.getVersion(interfaceInfo);
        Object data = Objects.isNull(encryptInfo) ? response.getData()
                : EncryptUtils.encryptRsa((String) response.getData(), encryptInfo.getPublicKey());
        String message = response.getMessage();
        return response.getSuccess() ? RestBean.success(data, message) : RestBean.failure(400, message);
    }
}

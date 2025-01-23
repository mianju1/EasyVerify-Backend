package cn.mianju.strategy.function.impl;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.api.SoftwareKeyVO;
import cn.mianju.entity.vo.response.ResponseVO;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.FunctionStrategy;
import cn.mianju.utils.EncryptUtils;
import com.alibaba.fastjson2.JSON;

import java.util.Objects;

public class GetExpiredTimeStrategy implements FunctionStrategy {

    @Override
    public RestBean<?> execute(TUserService tUserService,String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception {
        SoftwareKeyVO softwareKeyVO = JSON.parseObject(body, SoftwareKeyVO.class);
        ResponseVO response = tUserService.getExpireTime(softwareKeyVO, interfaceInfo);
        Object data = Objects.isNull(encryptInfo) ? response.getData()
                : EncryptUtils.encryptRsa((String) response.getData(), encryptInfo.getPublicKey());
        String message = response.getMessage();
        return response.getSuccess() ? RestBean.success(data, message) : RestBean.failure(400, message);
    }
}

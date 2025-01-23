package cn.mianju.strategy.function.impl;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.api.UpdateUserPasswordVO;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.FunctionStrategy;
import com.alibaba.fastjson2.JSON;

public class UpdateUserPasswordStrategy implements FunctionStrategy {

    @Override
    public RestBean<?> execute(TUserService tUserService,String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception {
        UpdateUserPasswordVO updateUserPasswordVO = JSON.parseObject(body, UpdateUserPasswordVO.class);
        String message = tUserService.updateUserPassword(updateUserPasswordVO, interfaceInfo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}

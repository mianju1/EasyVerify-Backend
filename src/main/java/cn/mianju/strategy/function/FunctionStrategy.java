package cn.mianju.strategy.function;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.service.user.TUserService;

public interface FunctionStrategy {
    RestBean<?> execute(TUserService tUserService, String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception;
}
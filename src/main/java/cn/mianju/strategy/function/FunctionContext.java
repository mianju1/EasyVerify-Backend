package cn.mianju.strategy.function;

import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.service.user.TUserService;
import cn.mianju.strategy.function.impl.*;

import java.util.HashMap;
import java.util.Map;

public class FunctionContext {
    private final Map<Integer, FunctionStrategy> strategyMap = new HashMap<>();

    public FunctionContext() {
        // 初始化策略映射
        strategyMap.put(0, new RegisterUserStrategy());
        strategyMap.put(1, new LoginUserStrategy());
        strategyMap.put(2, new LoginCodeStrategy());
        strategyMap.put(3, new GetVersionStrategy());
        strategyMap.put(4, new GetNoticeStrategy());
        strategyMap.put(5, new GetExpiredTimeStrategy());
        strategyMap.put(6, new GetIsNewVersionStrategy());
        strategyMap.put(7, new UpdateUserPasswordStrategy());
    }

    public RestBean<?> executeStrategy(int iFunction, TUserService tUserService, String body, VInterfaceinfo interfaceInfo, EncryptInfo encryptInfo) throws Exception {
        FunctionStrategy strategy = strategyMap.get(iFunction);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid function: " + iFunction);
        }
        return strategy.execute(tUserService,body, interfaceInfo, encryptInfo);
    }
}

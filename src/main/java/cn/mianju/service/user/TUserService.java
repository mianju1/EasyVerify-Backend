package cn.mianju.service.user;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TUser;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.api.*;
import cn.mianju.entity.vo.response.ResponseVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;


public interface TUserService extends IService<TUser> {

    // 路由
    RestBean<?> routeFunction(String jsonBody, String url) throws Exception;

    // 0-注册用户
    String registerUser(@Validated RegisterUserVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException;

    // 1-登录用户
    RestBean<String> loginUser(@Validated LoginUserVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException;

    // 2-激活码登录
    RestBean<String> loginCode(@Validated LoginCodeVO vo, VInterfaceinfo interfaceInfo);

    // 3-获取最新版本
    ResponseVO getVersion(VInterfaceinfo interfaceInfo);

    // 4-获取公告
    ResponseVO getNotice(VInterfaceinfo interfaceInfo);

    // 5-获取指定用户到期时间
    ResponseVO getExpireTime(SoftwareKeyVO vo, VInterfaceinfo interfaceInfo);

    // 6-是否为当前最新版本
    ResponseVO getIsNewVersion(SoftwareKeyVO vo, VInterfaceinfo interfaceInfo);

    // 7-修改用户密码
    String updateUserPassword(@Validated UpdateUserPasswordVO vo, VInterfaceinfo interfaceInfo) throws NoSuchAlgorithmException;
}

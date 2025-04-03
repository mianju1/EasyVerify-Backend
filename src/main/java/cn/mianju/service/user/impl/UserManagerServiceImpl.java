package cn.mianju.service.user.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TUser;
import cn.mianju.entity.view.VUserinfo;
import cn.mianju.entity.vo.request.PageVO;
import cn.mianju.entity.vo.request.user.BanUserVO;
import cn.mianju.entity.vo.request.user.DeleteUserVO;
import cn.mianju.entity.vo.request.user.UpdateOneUserVO;
import cn.mianju.entity.vo.request.user.UserSeachVO;
import cn.mianju.entity.vo.response.user.UserListVO;
import cn.mianju.mapper.TUserMapper;
import cn.mianju.service.user.UserManagerService;
import cn.mianju.service.user.VUserinfoService;
import cn.mianju.utils.EncryptUtils;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
public class UserManagerServiceImpl extends ServiceImpl<TUserMapper, TUser>
        implements UserManagerService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    HttpServletRequest request;

    @Resource
    VUserinfoService vUserinfoService;

    @FlowLimit
    @Override
    public RestBean<List<UserListVO>> getUserList(UserSeachVO vo) {
        String dId = getUserId();
        String sid = vo.getSid();
        String keyword = vo.getKeyword();
        PageVO page = vo.getPage();


        IPage<VUserinfo> pageInfo = new Page<>(page.getCurrentPage(), page.getPageSize());
        QueryWrapper<VUserinfo> wrapper = new QueryWrapper<>();
        wrapper.select("s_name", "u_name", "c_expired","u_status")
                .eq("d_id", dId)
                .eq("s_id", sid)
                .like(!keyword.isEmpty(), "u_name", keyword);

        IPage<VUserinfo> responsePage = vUserinfoService.page(pageInfo, wrapper);
        List<VUserinfo> records = responsePage.getRecords();
        Long total = responsePage.getTotal();


        List<UserListVO> list = records.stream().map(vUserinfo -> {
            UserListVO userListVO = new UserListVO();
            userListVO.setSname(vUserinfo.getSName());
            userListVO.setUsername(vUserinfo.getUName());
            userListVO.setExpiredTime(vUserinfo.getCExpired());
            userListVO.setLoginTime(null);
            userListVO.setStatus(vUserinfo.getUStatus());

            return userListVO;
        }).toList();

        return RestBean.success(list, total);
    }

    @FlowLimit
    @Override
    public String updateManagerUserOne(UpdateOneUserVO vo) throws NoSuchAlgorithmException {
        String userId = getUserId();
        String sid = vo.getSid();
        String username = vo.getUsername();
        String password = EncryptUtils.sha256Encode(vo.getPassword());

        // 确定当前请求下的开发者请求的用户是合法
        VUserinfo one = vUserinfoService.query()
                .select("u_id", "u_name")
                .eq("d_id", userId)
                .eq("s_id", sid)
                .eq("u_name", username)
                .one();
        if (Objects.isNull(one)) return "用户不存在";

        // 修改用户信息
        boolean update = this.update()
                .eq("s_id", sid)
                .eq("u_name", one.getUName())
                .set(!password.isEmpty(), "u_password", password)
                .update();

        return update ? null : "更新失败";
    }

    @FlowLimit
    @Override
    public String deleteManagerUserOne(DeleteUserVO vo) {
        String userId = getUserId();
        String sid = vo.getSid();
        List<String> username = vo.getUsername();

        // 确定当前请求下的开发者请求的用户是合法
        List<VUserinfo> list = vUserinfoService.query()
                .select("u_id", "u_name")
                .eq("d_id", userId)
                .eq("s_id", sid)
                .in("u_name", username)
                .list();
        if (list.isEmpty()) return "用户不存在";

        List<String> uidList = list.stream().map(VUserinfo::getUId).toList();
        boolean remove = this.removeByIds(uidList);

        return remove ? null : "删除失败";
    }

    @FlowLimit
    @Override
    public String banMangerUserOne(BanUserVO vo) {
        String userId = getUserId();
        String sid = vo.getSid();
        List<String> username = vo.getUsername();

        // 确定当前请求下的开发者请求的用户是合法
        List<VUserinfo> list = vUserinfoService.query()
                .select("u_id", "u_name")
                .eq("d_id", userId)
                .eq("s_id", sid)
                .in("u_name", username)
                .list();
        if (list.isEmpty()) return "用户不存在";

        List<String> uidList = list.stream().map(VUserinfo::getUId).toList();


        return "";
    }

    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }


}

package cn.mianju.service.encrypt.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VEncryptinfo;
import cn.mianju.entity.vo.response.encrypt.ShowEncrtyptVO;
import cn.mianju.mapper.VEncryptinfoMapper;
import cn.mianju.service.encrypt.VEncryptinfoService;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Admin
 * @description 针对表【v_encryptinfo】的数据库操作Service实现
 * @createDate 2025-01-06 22:11:35
 */
@Service
public class VEncryptinfoServiceImpl extends ServiceImpl<VEncryptinfoMapper, VEncryptinfo>
        implements VEncryptinfoService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    HttpServletRequest request;

    @FlowLimit
    @Override
    public RestBean<List<ShowEncrtyptVO>> getEncryptByUserAndSoft(String sid) {
        // 获取用户ID
        String userId = getUserId();
        if (sid.isEmpty()) return RestBean.failure(400, "软件ID不能为空");

        // 查询当前登录用户id下指定的软件id
        List<VEncryptinfo> list = this.query()
                .select("e_publickey", "e_privatekey", "e_updatetime", "encryption", "s_name")
                .eq("d_id", userId)
                .eq("s_id", sid).list();

        if (list.isEmpty()) return RestBean.failure(400, "参数有误");

        // 封装为VO进行输出
        List<ShowEncrtyptVO> data = list.stream().map(vEncryptinfo -> {
            ShowEncrtyptVO showEncrtyptVO = new ShowEncrtyptVO();

            showEncrtyptVO.setEncryption(vEncryptinfo.getEncryption());
            showEncrtyptVO.setSname(vEncryptinfo.getSName());
            showEncrtyptVO.setUpdateTime(vEncryptinfo.getEUpdatetime());
            showEncrtyptVO.setPublicKey(vEncryptinfo.getEPublickey());
            showEncrtyptVO.setPrivateKey(vEncryptinfo.getEPrivatekey());
            return showEncrtyptVO;
        }).toList();

        return RestBean.success(data);

    }
    @FlowLimit
    @Override
    public RestBean<List<Integer>> getEncryptByUser(String sid) {
        String userId = getUserId();

        List<VEncryptinfo> list = this.query()
                .select("e_id", "encryption")
                .eq("d_id", userId)
                .eq("s_id", sid)
                .list();

        if (list.isEmpty()) return RestBean.failure(404, "软件不存在");
        if (Objects.isNull(list.get(0)))
            return RestBean.failure(404, "加密算法不存在，请添加指定加密算法");

        List<Integer> data = list.stream().map(VEncryptinfo::getEncryption).toList();

        return RestBean.success(data);
    }


    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }

}





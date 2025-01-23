package cn.mianju.service.encrypt.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.EncryptInfo;
import cn.mianju.entity.dto.TEncrypt;
import cn.mianju.entity.view.VEncryptinfo;
import cn.mianju.entity.vo.request.encrypt.AddEncryptVO;
import cn.mianju.entity.vo.request.encrypt.DelEncryptVO;
import cn.mianju.mapper.TEncryptMapper;
import cn.mianju.service.encrypt.TEncryptService;
import cn.mianju.service.encrypt.VEncryptinfoService;
import cn.mianju.utils.EncryptUtils;
import cn.mianju.utils.JwtUtils;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Admin
 * @description 针对表【t_encrypt】的数据库操作Service实现
 * @createDate 2025-01-06 15:36:48
 */
@Service
public class TEncryptServiceImpl extends ServiceImpl<TEncryptMapper, TEncrypt>
        implements TEncryptService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    VEncryptinfoService vEncryptinfoService;

    @Resource
    HttpServletRequest request;

    @Resource
    SnowflakeIdGenerator snowflakeIdGenerator;

    @FlowLimit
    @Override
    public String addEncrypt(AddEncryptVO vo) throws NoSuchAlgorithmException {
        String userId = getUserId();

        List<VEncryptinfo> user_software_list = getUserEntryptList(userId, vo.getSid());

        // 如果提交的软件id不属于当前用户则不允许修改
        if (user_software_list.isEmpty()) return "参数有误";

        // 如果当前sid存在指定加密方式则返回重复提交
        if (!Objects.isNull(user_software_list.get(0).getEncryption())
                && user_software_list.stream().anyMatch(v -> v.getEncryption().equals(vo.getEncryption())))
            return "加密方式不可重复提交";

        TEncrypt tEncrypt = new TEncrypt();

        String e_id = snowflakeIdGenerator.nextId() + "";
        Integer encryption = vo.getEncryption();

        tEncrypt.setEId(e_id);
        tEncrypt.setSId(vo.getSid());
        tEncrypt.setEncryption(encryption);
        tEncrypt.setEUpdatetime(new Date());

        boolean save;
        // 生成公钥和私钥存入加密方式中
        if (!encryption.equals(0)) {
            EncryptInfo encryptInfo = EncryptUtils.generateRsaKey();
            tEncrypt.setEPublickey(encryptInfo.getPublicKey());
            tEncrypt.setEPrivatekey(encryptInfo.getPrivateKey());
        }
        save = this.save(tEncrypt);


        return save ? null : "添加失败";
    }

    @FlowLimit
    @Override
    public String deleteEncrypt(DelEncryptVO vo) {
        String userId = getUserId();
        String sid = vo.getSid();
        List<Integer> encryption = vo.getEncryption();

        List<VEncryptinfo> userEntryptList = getUserEntryptList(userId, sid);

        // 没有查到任何软件和加密算法
        if (userEntryptList.isEmpty()) return "参数有误";
        if (Objects.isNull(userEntryptList.get(0).getEncryption())) return "接口不存在";

        QueryWrapper<TEncrypt> wrapper = new QueryWrapper<>();
        wrapper.select("e_id")
                .eq("s_id", sid)
                .in("encryption", encryption);

        boolean remove = this.remove(wrapper);

        return remove ? null : "删除失败";
    }


    private List<VEncryptinfo> getUserEntryptList(String userId, String sid) {
        return vEncryptinfoService.query()
                .select("s_id", "encryption")
                .eq("d_id", userId)
                .eq("s_id", sid).list();
    }


    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }

}





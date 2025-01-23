package cn.mianju.service.encrypt;

import cn.mianju.entity.dto.TEncrypt;
import cn.mianju.entity.vo.request.encrypt.AddEncryptVO;
import cn.mianju.entity.vo.request.encrypt.DelEncryptVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.security.NoSuchAlgorithmException;

/**
 * @author Admin
 * @description 针对表【t_encrypt】的数据库操作Service
 * @createDate 2025-01-06 15:36:48
 */
public interface TEncryptService extends IService<TEncrypt> {

    // 新增加密信息
    String addEncrypt(AddEncryptVO vo) throws NoSuchAlgorithmException;

    // 删除加密算法
    String deleteEncrypt(DelEncryptVO vo);

}

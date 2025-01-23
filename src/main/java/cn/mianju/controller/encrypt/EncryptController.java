package cn.mianju.controller.encrypt;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.encrypt.AddEncryptVO;
import cn.mianju.entity.vo.request.encrypt.DelEncryptVO;
import cn.mianju.entity.vo.response.encrypt.ShowEncrtyptVO;
import cn.mianju.service.encrypt.TEncryptService;
import cn.mianju.service.encrypt.VEncryptinfoService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/encrypt")
public class EncryptController {

    @Resource
    VEncryptinfoService vEncryptinfoService;

    @Resource
    TEncryptService tEncryptService;

    @GetMapping("/get-encryption")
    public RestBean<List<ShowEncrtyptVO>> getEncryption(@RequestParam String sid) {
        return vEncryptinfoService.getEncryptByUserAndSoft(sid);
    }

    @PostMapping("/add-encryption")
    public RestBean<Void> addEncryption(@RequestBody @Validated AddEncryptVO vo) throws NoSuchAlgorithmException {
        String message = tEncryptService.addEncrypt(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/del-encryption")
    public RestBean<Void> delEncryption(@RequestBody @Validated DelEncryptVO vo) {
        String message = tEncryptService.deleteEncrypt(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @GetMapping("/get-has-encryption")
    public RestBean<List<Integer>> getHasEncryption(@RequestParam @Validated @NotNull String sid) {
        return vEncryptinfoService.getEncryptByUser(sid);
    }

}

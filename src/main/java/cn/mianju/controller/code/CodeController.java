package cn.mianju.controller.code;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.code.ActiveCodeMachineVO;
import cn.mianju.entity.vo.request.code.AddCodeVO;
import cn.mianju.entity.vo.request.code.CodeSeachVO;
import cn.mianju.entity.vo.request.code.DeleteCodeVO;
import cn.mianju.entity.vo.response.code.CodeShowVO;
import cn.mianju.service.code.TCodeService;
import cn.mianju.service.code.VCodeinfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MianJu 2024/12/18
 * EasyVerify cn.mianju.controller.code
 */
@RequestMapping("/api/code")
@RestController
public class CodeController {

    @Resource
    VCodeinfoService codeinfoService;

    @Resource
    TCodeService codeService;

    @PostMapping("/seach-code")
    @Operation(summary = "获取所有注册码信息")
    public RestBean<List<CodeShowVO>> seachRegisterCodeList(@RequestBody @Valid CodeSeachVO vo) {
        return codeinfoService.seachCode(vo);
    }

    @PostMapping("/delete-code")
    @Operation(summary = "删除指定注册码")
    public RestBean<Void> deleteRegisterCode(@RequestBody DeleteCodeVO vo) {
        String message = codeService.deleteCode(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/add-code")
    @Operation(summary = "新增注册码")
    public RestBean<List<String>> addRegisterCode(@RequestBody @Valid AddCodeVO vo) {
        return codeService.addCode(vo);
    }

    @PostMapping("/update-machine")
    @Operation(summary = "修改激活码的机器码")
    public RestBean<Void> updateMachine(@RequestBody @Valid ActiveCodeMachineVO vo) {
        String message = codeService.updateCodeMachine(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }


//    用户请求的时候内部调用该service方法
//    @PostMapping("/update-code")
//    public RestBean<Void> updateRegisterCode(@RequestBody @Valid UpdateCodeVO vo) {
//        String message = codeService.updateCode(vo);
//        return message == null ? RestBean.success() : RestBean.failure(400, message);
//    }

}

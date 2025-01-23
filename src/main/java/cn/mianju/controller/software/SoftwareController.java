package cn.mianju.controller.software;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.software.*;
import cn.mianju.entity.vo.response.software.SoftwareShowVO;
import cn.mianju.entity.vo.response.software.SoftwareTypeVO;
import cn.mianju.service.software.TSoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MianJu 2024/12/11
 * EasyVerify cn.mianju.controller.software
 */
@RestController
@RequestMapping("/api/soft")
public class SoftwareController {

    @Resource
    TSoftwareService tSoftwareService;

    @PostMapping("/add-soft")
    @Operation(summary = "添加软件")
    public RestBean<Void> addSoft(@RequestBody @Valid SoftwareAddVO vo) {
        String message = tSoftwareService.addSoftware(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/update-soft")
    @Operation(summary = "修改软件信息")
    public RestBean<Void> updateSoft(@RequestBody @Valid SoftwareUpdateVO vo) {
        String message = tSoftwareService.updateSoftware(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/update-soft-by-ids")
    @Operation(summary = "批量修改软件信息")
    public RestBean<Void> updateSoftByIds(@RequestBody @Valid UpdateByIdsVO vo) {
        String message = tSoftwareService.updateSoftwareByIds(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/delete-soft")
    @Operation(summary = "删除软件")
    public RestBean<Void> deleteSoft(@RequestBody DeleteSoftwareVo vo) {
        String message = tSoftwareService.deleteSoftware(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/seach-softs")
    @Operation(summary = "根据软件ID获取软件信息")
    public RestBean<List<SoftwareShowVO>> getSoftById(@RequestBody @Valid SoftwareSeachVO vo) {
        return tSoftwareService.searchSoft(vo);
    }

    @GetMapping("/list-soft")
    @Operation(summary = "列出激活码或者注册码的软件")
    public RestBean<List<SoftwareTypeVO>> listSoft(@RequestParam Integer codetype) {
        return tSoftwareService.listSoft(codetype);
    }
}

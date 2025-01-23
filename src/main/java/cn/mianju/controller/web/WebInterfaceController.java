package cn.mianju.controller.web;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.request.web.AddInterfaceVO;
import cn.mianju.entity.vo.request.web.DelInterfaceVO;
import cn.mianju.entity.vo.request.web.GetInterfaceVO;
import cn.mianju.entity.vo.request.web.UpdateInterfaceVO;
import cn.mianju.entity.vo.response.web.ShowInterfaceVO;
import cn.mianju.service.web.TInterfaceService;
import cn.mianju.service.web.VInterfaceinfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MianJu 2024/12/20
 * EasyVerify cn.mianju.controller.web
 */
@RestController
@RequestMapping("/api/web")
public class WebInterfaceController {

    @Resource
    VInterfaceinfoService vInterfaceinfoService;

    @Resource
    TInterfaceService tInterfaceService;

    @PostMapping("/get-interface")
    @Operation(summary = "获取指定软件的所有接口")
    public RestBean<List<ShowInterfaceVO>> getInterface(@RequestBody @Valid GetInterfaceVO vo) {
        return vInterfaceinfoService.getInterfaceBySid(vo);
    }

    @PostMapping("/add-interface")
    @Operation(summary = "添加接口至指定软件")
    public RestBean<Void> addInterface(@RequestBody @Valid AddInterfaceVO vo) {
        String message = tInterfaceService.addInterface(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/update-interface")
    @Operation(summary = "更新软件接口")
    public RestBean<Void> updateInterface(@RequestBody @Valid UpdateInterfaceVO vo) {
        String message = tInterfaceService.updateInterface(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    @PostMapping("/delete-interface")
    @Operation(summary = "删除软件接口")
    public RestBean<Void> deleteInterface(@RequestBody @Valid DelInterfaceVO vo) {
        String message = tInterfaceService.deleteInterface(vo);
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

}

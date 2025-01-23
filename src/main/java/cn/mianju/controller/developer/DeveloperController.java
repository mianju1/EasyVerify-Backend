package cn.mianju.controller.developer;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.vo.response.developer.DeveloperInfoVO;
import cn.mianju.service.developer.TDeveloperInfoService;
import cn.mianju.service.request.VRequestlogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MianJu 2024/12/14
 * EasyVerify cn.mianju.controller.developer
 */
@RestController
@RequestMapping("/api/dev")
public class DeveloperController {
    @Resource
    TDeveloperInfoService developerService;

    @Resource
    VRequestlogService requestlogService;

    @GetMapping("/info")
    @Operation(summary = "获取开发者个人中心信息")
    public RestBean<DeveloperInfoVO> getInfo(@RequestParam String d_name) {
        return developerService.getDeveloperInfo(d_name);
    }

    @GetMapping("/last7days")
    @Operation(summary = "获取开发者最近7天登录人数")
    public RestBean<HashMap<String, Map<String, Integer>>> getLastSevenDays() {
        return requestlogService.last7DayLoginCount();
    }

}

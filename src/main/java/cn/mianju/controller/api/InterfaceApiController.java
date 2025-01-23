package cn.mianju.controller.api;

import cn.mianju.entity.RestBean;
import cn.mianju.service.user.TUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InterfaceApiController {

    @Resource
    TUserService tUserService;


    @PostMapping("/{url}")
    public RestBean<?> route(
            @RequestBody(required = false) String body,
            @PathVariable(value = "url") String url
    ) throws Exception {
        return tUserService.routeFunction(body, url);
    }
}

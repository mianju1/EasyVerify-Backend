package cn.mianju.service.web.Impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.web.GetInterfaceVO;
import cn.mianju.entity.vo.response.web.ShowInterfaceVO;
import cn.mianju.mapper.VInterfaceinfoMapper;
import cn.mianju.service.web.VInterfaceinfoService;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 10408
 * @description 针对表【v_interfaceinfo】的数据库操作Service实现
 * @createDate 2024-12-20 13:54:09
 */
@Service
public class VInterfaceinfoServiceImpl extends ServiceImpl<VInterfaceinfoMapper, VInterfaceinfo>
        implements VInterfaceinfoService {
    @Resource
    HttpServletRequest request;

    @Resource
    JwtUtils jwtUtils;


    @FlowLimit
    @Override
    public RestBean<List<ShowInterfaceVO>> getInterfaceBySid(GetInterfaceVO vo) {
        String id = getUserId();
        if (vo.getSid().isEmpty()) return RestBean.failure(400, "参数为空");

        // 分页查询
        IPage<VInterfaceinfo> page = new Page<>(vo.getPage().getCurrentPage(), vo.getPage().getPageSize());

        // 查询该用户下的指定软件的接口
        QueryWrapper<VInterfaceinfo> wrapper = new QueryWrapper<VInterfaceinfo>()
                .select("i_id", "i_function", "i_url", "i_desc", "s_name", "encryption")
                .eq("d_id", id)
                .eq("s_id", vo.getSid());

        IPage<VInterfaceinfo> vInterfaceinfoIPage = this.page(page, wrapper);
        List<VInterfaceinfo> records = vInterfaceinfoIPage.getRecords();
        Long total = vInterfaceinfoIPage.getTotal();


        List<ShowInterfaceVO> result = records.stream().map(vInterfaceinfo -> {
                    ShowInterfaceVO showInterfaceVO = new ShowInterfaceVO();
                    showInterfaceVO.setName(vInterfaceinfo.getSName());
                    showInterfaceVO.setDesc(vInterfaceinfo.getIDesc());
                    showInterfaceVO.setEncryption(vInterfaceinfo.getEncryption());
                    showInterfaceVO.setUrl(vInterfaceinfo.getIUrl());
                    showInterfaceVO.setFunction(vInterfaceinfo.getIFunction());
                    return showInterfaceVO;
                }
        ).toList();

        return RestBean.success(result,total);
    }

    @Override
    // 判断在请求的ID下是否存在软件，且查询是否存在相同function的接口
    public List<VInterfaceinfo> getInterfaceByDidAndSid(String did, String sid){
        return this.baseMapper.selectFunctionByDIdAndSid(did, sid);
    }

    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }

}





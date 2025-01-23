package cn.mianju.service.web.Impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.dto.TInterface;
import cn.mianju.entity.view.VInterfaceinfo;
import cn.mianju.entity.vo.request.web.AddInterfaceVO;
import cn.mianju.entity.vo.request.web.DelInterfaceVO;
import cn.mianju.entity.vo.request.web.UpdateInterfaceVO;
import cn.mianju.mapper.TInterfaceMapper;
import cn.mianju.service.web.TInterfaceService;
import cn.mianju.service.web.VInterfaceinfoService;
import cn.mianju.utils.Const;
import cn.mianju.utils.JwtUtils;
import cn.mianju.utils.SnowflakeIdGenerator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author 10408
 * @description 针对表【t_interface】的数据库操作Service实现
 * @createDate 2024-12-10 14:01:26
 */
@Service
public class TInterfaceServiceImpl extends ServiceImpl<TInterfaceMapper, TInterface>
        implements TInterfaceService {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    HttpServletRequest request;

    @Resource
    VInterfaceinfoService vInterfaceinfoService;

    @Resource
    SnowflakeIdGenerator snowflakeIdGenerator;


    @FlowLimit
    @Override
    public String addInterface(AddInterfaceVO vo) {
        String id = getUserId();
        Integer function = vo.getFunction();
        Integer encryption = vo.getEncryption();
        String sid = vo.getSid();


        if (sid.isEmpty()
                || Objects.isNull(function)
                || Objects.isNull(encryption)) return "参数不能为空";


        // 判断在请求的ID下是否存在软件，且查询是否存在相同function的接口
        List<VInterfaceinfo> list = vInterfaceinfoService.getInterfaceByDidAndSid(id, sid);

        if (list.isEmpty()) return "软件不存在";

        // 获取所有的该软件的接口功能
        List<Integer> functions = list.stream()
                .map(VInterfaceinfo::getIFunction)
                .toList();

        // 如果functions包含function，则说明已有该接口
        if (functions.contains(function)) return "已存在当前功能接口";


        // 生成16位 URL
        String url = UUID.randomUUID().toString().substring(0, 18).toUpperCase().replace("-", "");

        TInterface tInterface = new TInterface();
        tInterface.setSId(sid);
        tInterface.setEncryption(encryption);
        tInterface.setIId(snowflakeIdGenerator.nextId() + "");
        tInterface.setIUrl(url);
        tInterface.setIFunction(function);
        tInterface.setIDesc(Const.CODE_TYPE_DESC.get(function));

        // 保存接口至数据库
        boolean save = this.save(tInterface);

        return save ? null : "保存失败";
    }


    @FlowLimit
    @Override
    public String updateInterface(UpdateInterfaceVO vo) {
        String id = getUserId();

        String sids = vo.getSid();
        List<String> url = vo.getUrl();
        Integer encryption = vo.getEncryption();

        if (sids.isEmpty()
                || url.isEmpty()
                || Objects.isNull(encryption))
            return "参数不能为空";

        List<VInterfaceinfo> list = vInterfaceinfoService.getInterfaceByDidAndSid(id, sids);

        if (list.isEmpty()) return "接口不存在";

        // 要更新的数据为指定的软件id和功能
        QueryWrapper<TInterface> wrapper = new QueryWrapper<>();
        wrapper.eq("s_id", sids)
                .in("i_url", url);

        // 更新为新设置的功能和加密方式
        TInterface tInterface = new TInterface();
        tInterface.setEncryption(encryption);

        boolean update = this.update(tInterface, wrapper);

        return update ? null : "更新失败";
    }

    @FlowLimit
    @Override
    public String deleteInterface(DelInterfaceVO vo) {
        String id = getUserId();
        String sid = vo.getSid();
        List<String> url = vo.getUrl();

        // 查询请求开发者下指定软件的要删除的url的id
        List<String> list = vInterfaceinfoService.query()
                .select("i_id")
                .eq("s_id", sid)
                .eq("d_id", id)
                .in("i_url", url)
                .list()
                // 从Dto对象中将i_id提取出来
                .stream().map(VInterfaceinfo::getIId).toList();
        if (list.isEmpty()) return "接口不存在";

        // 在接口表中查询符合条件的id并删除
        QueryWrapper<TInterface> wrapper = new QueryWrapper<>();
        wrapper.in("i_id", list);

        boolean remove = this.remove(wrapper);

        return remove ? null : "删除失败";
    }

    // 获取用户ID
    private String getUserId() {
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        return jwtUtils.toId(authorization);
    }
}





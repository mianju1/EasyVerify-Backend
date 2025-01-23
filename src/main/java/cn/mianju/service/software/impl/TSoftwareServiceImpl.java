package cn.mianju.service.software.impl;

import cn.mianju.annotation.FlowLimit;
import cn.mianju.entity.RestBean;
import cn.mianju.entity.dto.TSoftware;
import cn.mianju.entity.vo.request.PageVO;
import cn.mianju.entity.vo.request.software.*;
import cn.mianju.entity.vo.response.software.SoftwareShowVO;
import cn.mianju.entity.vo.response.software.SoftwareTypeVO;
import cn.mianju.exception.RequestFrequencyException;
import cn.mianju.mapper.TSoftwareMapper;
import cn.mianju.service.software.TSoftwareService;
import cn.mianju.utils.IpUtil;
import cn.mianju.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 10408
 * @description 针对表【t_software】的数据s库操作Service实现
 * @createDate 2024-12-10 14:01:27
 */
@Service
public class TSoftwareServiceImpl extends ServiceImpl<TSoftwareMapper, TSoftware>
        implements TSoftwareService {

    @Resource
    HttpServletRequest request;

    @Resource
    JwtUtils jwtUtils;

    @FlowLimit
    @Override
    public String updateSoftware(SoftwareUpdateVO vo) {

        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);
        String name = jwtUtils.toColumn(authorization, "name");

        if (Objects.isNull(id) || Objects.isNull(name)) return "开发者名称不能为空";

        TSoftware software = new TSoftware();
        software.setSVersion(vo.getVersion());
        software.setSDesc(vo.getDesc());
        software.setSNotice(vo.getNotice());

        boolean update = this.update().eq("s_id", vo.getId()).update(software);

        return update ? null : "更新失败";
    }

    @FlowLimit
    @Override
    public String updateSoftwareByIds(UpdateByIdsVO vo) {
        if (Objects.isNull(vo.getDesc()) && Objects.isNull(vo.getVersion())) return "不是哥们，啥都不改你点集贸编辑呢";

        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);
        String name = jwtUtils.toColumn(authorization, "name");

        if (Objects.isNull(id) || Objects.isNull(name)) return "开发者名称不能为空";
        if (Objects.isNull(vo.getId())) return "参数有误";

        TSoftware tSoftware = new TSoftware();
        tSoftware.setSVersion(vo.getVersion());
        tSoftware.setSDesc(vo.getDesc());

        // 查询在该开发者下是否存在该软件id
        QueryWrapper<TSoftware> wrapper = new QueryWrapper<>();
        wrapper.eq("d_id", id).in("s_id", vo.getId());

        boolean update = this.update(tSoftware, wrapper);

        return update ? null : "更新失败";
    }

    /**
     * 添加软件信息
     *
     * @param vo 软件添加对象
     * @return 如果参数有误则返回"参数有误"，如果注册失败则返回"内部错误，注册失败"，否则返回null
     * @throws RequestFrequencyException 请求频率过快异常
     */
    @FlowLimit
    @Override
    public String addSoftware(SoftwareAddVO vo) {
        String ip = IpUtil.getRealIP(request);
        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);

        if (Objects.isNull(id) || Objects.isNull(vo.getName()) || Objects.isNull(vo.getVersion()) || Objects.isNull(vo.getCodetype()))
            return "参数有误";

        if (exists(id, vo.getName())) return "软件名称已存在,请勿重复创建";


        TSoftware software = new TSoftware();
        // 生成16位字符串UUID作为软件密钥key
        String key = UUID.randomUUID().toString().substring(0, 18).toUpperCase().replace("-", "");

        software.setSName(vo.getName());
        software.setSVersion(vo.getVersion());
        software.setSDesc(vo.getDesc());
        software.setSCodetype(vo.getCodetype());
        software.setDId(id);
        software.setSKey(key);
        software.setCreateAddress(ip);
        software.setCreateTime(new Date());

        if (!this.save(software)) {
            return "内部错误，注册失败";
        } else {
            return null;
        }
    }

    @FlowLimit
    @Override
    public String deleteSoftware(DeleteSoftwareVo vo) {
        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);

        if (Objects.isNull(id) || Objects.isNull(vo.getId().get(0))) return "参数有误";

        // 查询在该开发者下是否存在该软件id
        QueryWrapper<TSoftware> tSoftwareQueryWrapper = new QueryWrapper<>();
        tSoftwareQueryWrapper.eq("d_id", id)
                .in("s_id", vo.getId());

        boolean remove = this.remove(tSoftwareQueryWrapper);

        return remove ? null : "删除失败";
    }

    @FlowLimit
    @Override
    public RestBean<List<SoftwareShowVO>> searchSoft(SoftwareSeachVO vo) {
        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);

        PageVO pageVO = vo.getPage();
        if (Objects.isNull(pageVO) || Objects.isNull(vo.getKeyword()))
            return RestBean.failure(400, "参数有误");

        // 分页查询
        IPage<TSoftware> page = new Page<>(pageVO.getCurrentPage(), pageVO.getPageSize());

        // 模糊查询
        QueryWrapper<TSoftware> wrapper = new QueryWrapper<TSoftware>()
                .select("s_id", "s_name", "s_key", "s_desc", "s_version", "s_notice", "s_codetype")
                .eq("d_id", id)
                // 如果关键字为空，则不进行模糊查询，而查询全部信息
                .and(!vo.getKeyword().isEmpty(), i -> i.like("s_name", vo.getKeyword())
                        .or().like("s_desc", vo.getKeyword())
                        .or().like("s_version", vo.getKeyword())
                        .or().like("s_id", vo.getKeyword()));

        IPage<TSoftware> softwareIPage = this.page(page, wrapper);
        List<TSoftware> records = softwareIPage.getRecords();
        Long total = softwareIPage.getTotal();

        List<SoftwareShowVO> collect = records.stream()
                .map(software -> {
                    SoftwareShowVO softwareShowVO = new SoftwareShowVO();
                    softwareShowVO.setSId(software.getSId());
                    softwareShowVO.setSName(software.getSName());
                    softwareShowVO.setSKey(software.getSKey());
                    softwareShowVO.setSNotice(software.getSNotice());
                    softwareShowVO.setSDesc(software.getSDesc());
                    softwareShowVO.setSVersion(software.getSVersion());
                    softwareShowVO.setSCodetype(software.getSCodetype());
                    return softwareShowVO;
                }).collect(Collectors.toList());

        return RestBean.success(collect, total);
    }

    @FlowLimit
    @Override
    public RestBean<List<SoftwareTypeVO>> listSoft(Integer codetype) {
        // jwt 获得开发者id和名称
        DecodedJWT authorization = jwtUtils.resolveJwt(request.getHeader("Authorization"));
        String id = jwtUtils.toId(authorization);
        if (Objects.isNull(id)) return RestBean.failure(400, "参数有误");

        List<TSoftware> queryList = this.query()
                .select("s_id", "s_name")
                .eq("s_codetype", codetype)
                .eq("d_id", id)
                .list();

        List<SoftwareTypeVO> resultList = queryList.stream().map(tSoftware -> {
            SoftwareTypeVO softwareTypeVO = new SoftwareTypeVO();
            softwareTypeVO.setSid(tSoftware.getSId());
            softwareTypeVO.setName(tSoftware.getSName());
            return softwareTypeVO;
        }).toList();

        return RestBean.success(resultList);
    }

    @Override
    public List<String> getSoftIdByDid(String did) {
        QueryWrapper<TSoftware> wrapper = new QueryWrapper<>();
        wrapper.select("s_id")
                .eq("d_id", did);
        return this.list(wrapper).stream().map(TSoftware::getSId).collect(Collectors.toList());
    }

    private boolean exists(String dId, String sName) {
        return this.query().eq("d_id", dId)
                .eq("s_name", sName)
                .exists();
    }
}





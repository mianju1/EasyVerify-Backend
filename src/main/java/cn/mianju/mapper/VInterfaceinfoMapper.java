package cn.mianju.mapper;

import cn.mianju.entity.view.VInterfaceinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 10408
* @description 针对表【v_interfaceinfo】的数据库操作Mapper
* @createDate 2024-12-20 13:54:09
* @Entity cn.mianju.entity.view.VInterfaceinfo
*/
@Mapper
public interface VInterfaceinfoMapper extends BaseMapper<VInterfaceinfo> {

    @Select("""
            select t_software.d_id,t_interface.i_function,t_interface.encryption
            from t_software left join t_interface 
                on t_software.s_id = t_interface.s_id 
            where t_software.d_id = #{dId} and t_software.s_id = #{sId}
            """)
    List<VInterfaceinfo> selectFunctionByDIdAndSid(String dId,String sId);

}





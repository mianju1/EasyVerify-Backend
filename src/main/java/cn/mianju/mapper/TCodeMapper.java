package cn.mianju.mapper;

import cn.mianju.entity.dto.TCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 10408
 * @description 针对表【t_code】的数据库操作Mapper
 * @createDate 2024-12-18 10:23:54
 * @Entity cn.mianju.entity.dto.TCode
 */
@Mapper
public interface TCodeMapper extends BaseMapper<TCode> {

    // 通过did查询code
    @Select("select t_code.c_code " +
            "from t_code inner join t_software " +
            "on t_software.s_id = t_code.s_id " +
            "where " +
            "t_software.d_id = #{did}")
    List<TCode> selectCodeFormDid(String did);

}





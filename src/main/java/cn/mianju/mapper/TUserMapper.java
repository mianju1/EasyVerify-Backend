package cn.mianju.mapper;

import cn.mianju.entity.dto.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 10408
 * @description 针对表【t_user】的数据库操作Mapper
 * @createDate 2024-12-10 14:01:27
 * @Entity cn.mianju.entity.dto.TUser
 */
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {

}





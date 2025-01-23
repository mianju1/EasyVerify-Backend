package cn.mianju.mapper;

import cn.mianju.entity.view.VRequestlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【v_requestlog】的数据库操作Mapper
 * @createDate 2025-01-16 19:27:02
 * @Entity cn.mianju.entity.view.VRequestlog
 */
@Mapper
public interface VRequestlogMapper extends BaseMapper<VRequestlog> {

    @Select("""	
            SELECT s_id,s_name,d_id,DATE(r_date) as 'date',COUNT(DISTINCT(r_body)) AS 'num' from v_requestlog
            WHERE r_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
            AND s_id  in #{sIdList}
            AND d_id = #{dId}
            AND i_function in (1,2)
            AND INSTR(r_response,'200')
            GROUP BY s_id,DATE(r_date);
                       """)
    List<VRequestlog> selectLast7DaysLogin(String dId,List<String> sIdList);
}





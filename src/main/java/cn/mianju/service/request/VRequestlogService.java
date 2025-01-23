package cn.mianju.service.request;

import cn.mianju.entity.RestBean;
import cn.mianju.entity.view.VRequestlog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.Map;

/**
* @author Admin
* @description 针对表【v_requestlog】的数据库操作Service
* @createDate 2025-01-16 19:27:02
*/
public interface VRequestlogService extends IService<VRequestlog> {

    RestBean<HashMap<String, Map<String,Integer>>> last7DayLoginCount();

}

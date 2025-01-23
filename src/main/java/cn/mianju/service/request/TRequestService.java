package cn.mianju.service.request;

import cn.mianju.entity.dto.TRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Admin
 * @description 针对表【t_request】的数据库操作Service
 * @createDate 2025-01-14 00:04:30
 */
public interface TRequestService extends IService<TRequest> {
    // 添加日志记录到消息队列中
    void addRequestToQueue(TRequest request);

}

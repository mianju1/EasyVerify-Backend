package cn.mianju.service.request.impl;

import cn.mianju.entity.dto.TRequest;
import cn.mianju.mapper.TRequestMapper;
import cn.mianju.service.request.TRequestService;
import cn.mianju.utils.Const;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author Admin
* @description 针对表【t_request】的数据库操作Service实现
* @createDate 2025-01-14 00:04:30
*/
@Service
public class TRequestServiceImpl extends ServiceImpl<TRequestMapper, TRequest>
    implements TRequestService{

    @Resource
    AmqpTemplate rabbitTemplate;

    @Override
    public void addRequestToQueue(TRequest request) {
        // 添加日志记录到消息队列中
        synchronized (request.getRId().intern()) {
            // 添加到队列中
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", request.getRId());
            data.put("method", request.getRMethod());
            data.put("uri", request.getRUri());
            data.put("params", request.getRParams());
            data.put("body", request.getRBody());
            data.put("username", request.getRUsername());
            data.put("role", request.getRRole());
            data.put("date", request.getRDate());
            data.put("time", request.getRTime());
            data.put("ip", request.getRIpaddress());
            data.put("response", request.getRResponse());
            // 发送到消息队列中
            rabbitTemplate.convertAndSend(Const.MQ_LOG,data);
        }

    }

}





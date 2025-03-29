package cn.mianju.listener;

import cn.mianju.entity.dto.TRequest;
import cn.mianju.mapper.TRequestMapper;
import cn.mianju.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
@RabbitListener(queues = Const.MQ_LOG)
public class LogQueueListener {
    @Resource
    TRequestMapper requestMapper;

    /**
     * 处理请求记录存储
     *
     * @param data 请求记录信息
     */
    @RabbitHandler
    public void saveRequestRecord(Map<String, Object> data) {
        Object id = data.get("id");
        Object method = data.get("method");
        Object uri = data.get("uri");
        Object params = data.get("params");
        Object body = data.get("body");
        Object username = data.get("username");
        Object role = data.get("role");
        Object time = data.get("time");
        Object ip = data.get("ip");
        Object response = data.get("response");

        // 将 Map 数据转换为 TRequest 实体类
        TRequest request = new TRequest();
        request.setRId(Objects.isNull(id) ? null : id.toString());
        request.setRMethod(Objects.isNull(method) ? null : method.toString());
        request.setRUri(Objects.isNull(uri) ? null : uri.toString());
        request.setRParams(Objects.isNull(params) ? null : params.toString());
        request.setRBody(Objects.isNull(body) ? null : body.toString());
        request.setRUsername(Objects.isNull(username) ? null : username.toString());
        request.setRRole(Objects.isNull(role) ? null : role.toString());
        // 将 Long 类型的时间戳转换为 Date
        Long timestamp = (Long) data.get("date");
        request.setRDate(timestamp != null ? new Date(timestamp) : null);
        request.setRTime(Long.parseLong(time.toString()));
        request.setRIpaddress(Objects.isNull(ip) ? null : ip.toString());
        request.setRResponse(Objects.isNull(response) ? null : response.toString());

        // 将请求记录存入数据库
        requestMapper.insertOrUpdate(request);
    }
}

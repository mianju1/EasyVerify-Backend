package cn.mianju.entity.vo.response;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

@Data
public class ResponseVO {
    Object data;
    String message = "请求成功";
    Boolean success = true;

    public void setData(Object data) {
        if (data == null) return;
        if (data.getClass() == String.class) {
            this.data = data;
        } else {
            this.data = JSON.toJSONString(data);
        }
        this.data = this.data.toString().replace("\"", "");
    }
}

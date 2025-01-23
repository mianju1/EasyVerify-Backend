package cn.mianju.entity.vo.request.code;

import lombok.Data;

import java.util.List;

/**
 * @author MianJu 2024/12/19
 * EasyVerify cn.mianju.entity.vo.request.code
 */
@Data
public class DeleteCodeVO {
    List<String> code;
}

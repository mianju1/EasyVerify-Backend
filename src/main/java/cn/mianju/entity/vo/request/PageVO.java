package cn.mianju.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author MianJu 2024/12/16
 * EasyVerify cn.mianju.entity.vo.request.software
 */
@Data
public class PageVO {
    @Min(value = 1)
    Integer currentPage;
    @Range(min = 20, max = 30)
    Integer pageSize;
}

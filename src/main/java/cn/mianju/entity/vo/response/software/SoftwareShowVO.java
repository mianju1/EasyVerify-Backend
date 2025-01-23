package cn.mianju.entity.vo.response.software;

import lombok.Data;

/**
 * @author MianJu 2024/12/15
 * EasyVerify cn.mianju.entity.vo.response.software
 */
@Data
public class SoftwareShowVO {
    String sName;
    String sId;
    String sDesc;
    String sNotice;
    String sVersion;
    String sKey;
    Integer sCodetype;
}

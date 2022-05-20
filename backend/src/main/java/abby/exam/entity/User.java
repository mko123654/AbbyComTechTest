/***********************************************************
 * @Description : User
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:50
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicUpdate
public class User {
    @Id
    private String userId;
    private String userUsername;
    private String userNickname;
    private String userPassword;
    private Integer userRoleId;
    private String userAvatar;
    private String userDescription;
    private String userEmail;
    private String userPhone;

    /**
     * DB已設定預設加入CURRENT_TIMESTAMP
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * DB已設定預設加入CURRENT_TIMESTAMP
     * 註解@DynamicUpdate 當DB資料有變化時自動更新
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

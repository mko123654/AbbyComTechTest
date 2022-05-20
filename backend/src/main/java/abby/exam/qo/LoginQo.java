/***********************************************************
 * @Description : 登入的查詢參數
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-10 17:51
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginQo {
    /**
     * 1：用戶名稱 2：E-mail
     */
    private Integer loginType;
    private String userInfo;
    private String password;
}

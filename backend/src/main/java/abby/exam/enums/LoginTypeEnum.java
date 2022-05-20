/***********************************************************
 * @Description : 問題類型Enum
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-16 20:07
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.enums;

import lombok.Getter;

@Getter
public enum LoginTypeEnum {

    USERNAME(1, "用戶名稱"),
    EMAIL(2, "E-mail");

    LoginTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;
}

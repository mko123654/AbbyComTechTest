/***********************************************************
 * @Description : 角色Enum
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-18 19:21
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    /**
     * USER的角色，與DB中的role對應
     */
    ADMIN(1, "系統管理員"),
    TEACHER(2, "考官"),
    STUDENT(3, "考生");


    RoleEnum(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    private Integer id;
    private String role;
}

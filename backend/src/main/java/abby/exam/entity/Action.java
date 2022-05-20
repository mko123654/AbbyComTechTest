/***********************************************************
 * @Description : 前端操作的物件
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:51
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Action {
    @Id
    @GeneratedValue
    private Integer actionId;

    private String actionName;

    private String actionDescription;

    private Boolean defaultCheck;
}

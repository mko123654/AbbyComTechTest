/***********************************************************
 * @Description : 前端畫面物件
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 14:11
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Page {
    @Id
    @GeneratedValue
    private Integer pageId;

    private String pageName;

    private String pageDescription;

    private String actionIds;
}

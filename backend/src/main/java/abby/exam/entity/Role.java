/***********************************************************
 * @Description : Role
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:48
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private String roleDetail;
    /**
     * 角色具權限能進入之畫面的PK集合(以-連接為字串)
     */
    private String rolePageIds;
}

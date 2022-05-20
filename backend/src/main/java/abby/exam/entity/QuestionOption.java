/***********************************************************
 * @Description : 問題選項
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:51
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class QuestionOption {
    @Id
    private String questionOptionId;
    private String questionOptionContent;
    private String questionOptionDescription;
}

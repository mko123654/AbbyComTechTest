/***********************************************************
 * @Description : 考試得分等級
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:50
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ExamRecordLevel {
    @Id
    @GeneratedValue
    private Integer examRecordLevelId;
    private String examRecordLevelName;
    private String examRecordLevelDescription;
}

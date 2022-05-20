/***********************************************************
 * @Description : 題目難度
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:54
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class QuestionLevel {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionLevelId;

    @JsonProperty("name")
    private String questionLevelName;

    @JsonProperty("description")
    private String questionLevelDescription;
}

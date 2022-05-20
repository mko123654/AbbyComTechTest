/***********************************************************
 * @Description : 題目類型，以單選題、多選題、是非題劃分
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:51
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class QuestionType {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionTypeId;

    @JsonProperty("name")
    private String questionTypeName;

    @JsonProperty("description")
    private String questionTypeDescription;
}

/***********************************************************
 * @Description : 題目類別，以內容(JAVA、C#、Python等)劃分
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
public class QuestionCategory {

    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionCategoryId;

    @JsonProperty("name")
    private String questionCategoryName;

    @JsonProperty("description")
    private String questionCategoryDescription;
}

/***********************************************************
 * @Description : 問題類型Enum
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-18 18:57
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.enums;

import lombok.Getter;

@Getter
public enum QuestionEnum {

    RADIO(1, "單選題"),
    CHECK(2, "多選題"),
    JUDGE(3, "是非題");


    QuestionEnum(Integer id, String questionType) {
        this.id = id;
        this.questionType = questionType;
    }

    private Integer id;
    private String questionType;
}

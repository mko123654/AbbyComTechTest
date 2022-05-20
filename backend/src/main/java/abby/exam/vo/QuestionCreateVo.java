/***********************************************************
 * @Description : 问题创建的实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-02 13:26
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateVo {

    @JsonProperty("name")
    private String questionName;

    @JsonProperty("desc")
    private String questionDescription;

    /**
     * 題目分數，預設為5
     */
    @JsonProperty("score")
    private Integer questionScore = 5;

    @JsonProperty("creator")
    private String questionCreatorId;

    @JsonProperty("level")
    private Integer questionLevelId;

    @JsonProperty("type")
    private Integer questionTypeId;

    @JsonProperty("category")
    private Integer questionCategoryId;

    @JsonProperty("options")
    private List<QuestionOptionCreateVo> questionOptionCreateVoList;
}

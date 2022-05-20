package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateSimplifyVo {

    @JsonProperty("name")
    private String questionName;

    @JsonProperty("desc")
    private String questionDescription;

    @JsonProperty("level")
    private Integer questionLevelId;

    @JsonProperty("type")
    private Integer questionTypeId;

    @JsonProperty("category")
    private Integer questionCategoryId;

    @JsonProperty("option")
    private String option;

    @JsonProperty("options")
    private List<QuestionOptionCreateVo> questionOptionCreateVoList;
}

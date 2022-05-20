package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionVo {
    @JsonProperty("id")
    private String questionId;

    @JsonProperty("name")
    private String questionName;

    @JsonProperty("score")
    private Integer questionScore;

    @JsonProperty("creator")
    private String questionCreator;

    @JsonProperty("level")
    private String questionLevel;

    @JsonProperty("levelId")
    private int questionLevelId;

    @JsonProperty("type")
    private String questionType;

    @JsonProperty("typeId")
    private int questionTypeId;

    @JsonProperty("category")
    private String questionCategory;

    @JsonProperty("categoryId")
    private int questionCategoryId;

    @JsonProperty("description")
    private String questionDescription;

    @JsonProperty("options")
    private List<QuestionOptionVo> questionOptionVoList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

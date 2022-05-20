package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import abby.exam.entity.QuestionCategory;
import abby.exam.entity.QuestionLevel;
import abby.exam.entity.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSelectionVo {
    @JsonProperty("types")
    private List<QuestionType> questionTypeList;

    @JsonProperty("categories")
    private List<QuestionCategory> questionCategoryList;

    @JsonProperty("levels")
    private List<QuestionLevel> questionLevelList;
}

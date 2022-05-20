package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamCreateVo {

    @JsonProperty("name")
    private String examName;

    @JsonProperty("avatar")
    private String examAvatar;

    @JsonProperty("desc")
    private String examDescription;

    @JsonProperty("elapse")
    private Integer examTimeLimit;

    private List<ExamQuestionSelectVo> radios;

    private List<ExamQuestionSelectVo> checks;

    private List<ExamQuestionSelectVo> judges;

    @JsonProperty("radioScore")
    private Integer examScoreRadio;

    @JsonProperty("checkScore")
    private Integer examScoreCheck;

    @JsonProperty("judgeScore")
    private Integer examScoreJudge;
}

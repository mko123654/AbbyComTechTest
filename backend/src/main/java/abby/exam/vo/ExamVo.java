package abby.exam.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamVo {
    @JsonProperty("id")
    private String examId;
    @JsonProperty("name")
    private String examName;
    @JsonProperty("avatar")
    private String examAvatar;
    @JsonProperty("desc")
    private String examDescription;

    @JsonProperty("radios")
    private List<ExamQuestionSelectVo> examQuestionSelectVoRadioList;

    @JsonProperty("checks")
    private List<ExamQuestionSelectVo> examQuestionSelectVoCheckList;

    @JsonProperty("judges")
    private List<ExamQuestionSelectVo> examQuestionSelectVoJudgeList;

    @JsonProperty("score")
    private Integer examScore;

    @JsonProperty("radioScore")
    private Integer examScoreRadio;

    @JsonProperty("checkScore")
    private Integer examScoreCheck;

    @JsonProperty("judgeScore")
    private Integer examScoreJudge;

    @JsonProperty("creator")
    private String examCreator;

    /**
     * 單位：分鐘
     */
    @JsonProperty("elapse")
    private Integer examTimeLimit;

    @JsonProperty("startDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartDate;

    @JsonProperty("endDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examEndDate;

    @JsonProperty("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonProperty("updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

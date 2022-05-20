package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamPageVo {

    private Integer pageSize;

    private Integer pageNo;

    private Long totalCount;

    private Integer totalPage;

    @JsonProperty("data")
    private List<ExamVo> examVoList;
}

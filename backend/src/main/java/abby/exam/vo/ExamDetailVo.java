package abby.exam.vo;

import abby.exam.entity.Exam;
import lombok.Data;

@Data
public class ExamDetailVo {

    private Exam exam;

    private String[] radioIds;

    private String[] checkIds;

    private String[] judgeIds;

}

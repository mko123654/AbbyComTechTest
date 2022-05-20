package abby.exam.vo;

import abby.exam.entity.Exam;
import abby.exam.entity.ExamRecord;
import abby.exam.entity.User;
import lombok.Data;

@Data
public class ExamRecordVo {

    private Exam exam;

    private ExamRecord examRecord;

    private User user;
}

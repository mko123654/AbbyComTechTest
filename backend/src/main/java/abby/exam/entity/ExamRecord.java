/***********************************************************
 * @Description : 考試紀錄
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 13:50
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ExamRecord {

    @Id
    private String examRecordId;
    private String examId;
    /**
     * 考生作答的每个题目的選項(题目和题目之間用_分隔，题目有多選項時以-分隔)，用在查看考試詳情
     */
    private String answerOptionIds;
    private String examJoinerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examJoinDate;

    private Integer examTimeCost;
    private Integer examJoinScore;
    private Integer examResultLevel;
}

package abby.exam.vo;

import abby.exam.entity.ExamRecord;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class RecordDetailVo {

    private ExamRecord examRecord;

    private HashMap<String, List<String>> answersMap;

    private HashMap<String, String> resultsMap;

    private HashMap<String, List<String>> answersRightMap;
}

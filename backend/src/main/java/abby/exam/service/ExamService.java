package abby.exam.service;

import abby.exam.entity.ExamRecord;
import abby.exam.vo.*;
import abby.exam.entity.Exam;

import java.util.HashMap;
import java.util.List;

public interface ExamService {

    List<QuestionVo> getQuestionAll();

    /**
     * 根據前端傳過來的QuestionVo更新問題、選項
     */
    QuestionVo updateQuestion(QuestionVo questionVo);

    /**
     * 根據前端傳過來的QuestionVo新增問題、選項
     */
    void questionCreate(QuestionCreateVo questionCreateVo);

    /**
     * 取得問題的選項、分類、難易度的下拉選單選項
     */
    QuestionSelectionVo getSelections();

    /**
     * 取得問題詳情
     */
    QuestionDetailVo getQuestionDetail(String id);

    /**
     * 取得全部考試列表
     */
    List<ExamVo> getExamAll();

    /**
     * 取得所有問題的下拉選單，方便前端新增考試時篩選
     */
    ExamQuestionTypeVo getExamQuestionType();

    /**
     * 新增考試
     */
    Exam create(ExamCreateVo examCreateVo, String userId);

    /**
     * 取得考試卡片列表
     */
    List<ExamCardVo> getExamCardList();

    /**
     * 依考試id取得考試詳情
     */
    ExamDetailVo getExamDetail(String id);

    /**
     * 依用户交券進行評分
     */
    ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap);

    /**
     * 依用戶ID取得該用戶所有考試紀錄
     */
    List<ExamRecordVo> getExamRecordList(String userId);

    /**
     * 取得該用戶考試紀錄詳情
     */
    RecordDetailVo getRecordDetail(String recordId);

    /**
     * 更新考試
     */
    Exam update(ExamVo examVo, String userId);
}

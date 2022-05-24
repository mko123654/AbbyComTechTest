/***********************************************************
 * @Description : 考試RESTful API
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-15 23:11
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.controller;

import abby.exam.entity.Exam;
import abby.exam.entity.ExamRecord;
import abby.exam.enums.ResultEnum;
import abby.exam.service.ExamService;
import abby.exam.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "Exam APIs")
@RequestMapping("/api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/all")
    @Operation(summary = "取得所有考試的列表", description = "取得所有考試的列表")
    ResultVO<List<ExamVo>> getExamAll() {
        ResultVO<List<ExamVo>> resultVO;
        try {
            List<ExamVo> examVos = examService.getExamAll();
            resultVO = new ResultVO<>(ResultEnum.GET_ALL_EXAM_SUCCESS.getCode(), ResultEnum.GET_ALL_EXAM_SUCCESS.getMessage(), examVos);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_ALL_EXAM_FAILED.getCode(), ResultEnum.GET_ALL_EXAM_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @PostMapping("/create")
    @Operation(summary = "新增考試", description = "新增考試")
    ResultVO<Exam> createExam(@RequestBody ExamCreateVo examCreateVo, HttpServletRequest request) {
        ResultVO<Exam> resultVO;
        String userId = (String) request.getAttribute("user_id");
        try {
            Exam exam = examService.create(examCreateVo, userId);
            resultVO = new ResultVO<>(ResultEnum.CREATE_EXAM_SUCCESS.getCode(), ResultEnum.CREATE_EXAM_SUCCESS.getMessage(), exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.CREATE_EXAM_FAILED.getCode(), ResultEnum.CREATE_EXAM_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @PostMapping("/update")
    @Operation(summary = "更新考試", description = "更新考試")
    ResultVO<Exam> updateExam(@RequestBody ExamVo examVo, HttpServletRequest request) {
        ResultVO<Exam> resultVO;
        String userId = (String) request.getAttribute("user_id");
        try {
            Exam exam = examService.update(examVo, userId);
            resultVO = new ResultVO<>(ResultEnum.UPDATE_EXAM_SUCCESS.getCode(), ResultEnum.UPDATE_EXAM_SUCCESS.getMessage(), exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.UPDATE_EXAM_FAILED.getCode(), ResultEnum.UPDATE_EXAM_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/card/list")
    @Operation(summary = "取得考試列表，並配合前端卡片顯示", description = "取得考試列表，並配合前端卡片顯示")
    ResultVO<List<ExamCardVo>> getExamCardList() {
        ResultVO<List<ExamCardVo>> resultVO;
        try {
            List<ExamCardVo> examCardVoList = examService.getExamCardList();
            resultVO = new ResultVO<>(ResultEnum.GET_CARD_LIST_SUCCESS.getCode(), ResultEnum.GET_CARD_LIST_SUCCESS.getMessage(), examCardVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_CARD_LIST_FAILED.getCode(), ResultEnum.GET_CARD_LIST_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "依考試的id，獲得考試詳情", description = "依考試的id，獲得考試詳情")
    ResultVO<ExamDetailVo> getExamDetail(@PathVariable String id) {
        ResultVO<ExamDetailVo> resultVO;
        try {
            ExamDetailVo examDetail = examService.getExamDetail(id);
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_DETAILS_SUCCESS.getCode(), ResultEnum.GET_EXAM_DETAILS_SUCCESS.getMessage(), examDetail);
        } catch (Exception e) {
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_DETAILS_FAILED.getCode(), ResultEnum.GET_EXAM_DETAILS_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/question/all")
    @Operation(summary = "取得所有考試問題", description = "取得所有考試問題")
    ResultVO<List<QuestionVo>> getQuestionAll() {
        ResultVO<List<QuestionVo>> resultVO;
        try {
            List<QuestionVo> questionAll = examService.getQuestionAll();
            resultVO = new ResultVO<>(ResultEnum.GET_ALL_QUESTION_SUCCESS.getCode(), ResultEnum.GET_ALL_QUESTION_SUCCESS.getMessage(), questionAll);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_ALL_QUESTION_FAILED.getCode(), ResultEnum.GET_ALL_QUESTION_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @PostMapping("/question/update")
    @Operation(summary = "更新考試問題", description = "更新考試問題")
    ResultVO<QuestionVo> questionUpdate(@RequestBody QuestionVo questionVo) {
        try {
            QuestionVo questionVoResult = examService.updateQuestion(questionVo);
            return new ResultVO<>(ResultEnum.UPDATE_QUESTION_SUCCESS.getCode(), ResultEnum.UPDATE_QUESTION_SUCCESS.getMessage(), questionVoResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(ResultEnum.UPDATE_QUESTION_FAILED.getCode(), ResultEnum.UPDATE_QUESTION_FAILED.getMessage(), null);
        }
    }

    @PostMapping("/question/create")
    @Operation(summary = "新增考試問題", description = "新增考試問題")
    ResultVO<String> questionCreate(@RequestBody QuestionCreateSimplifyVo questionCreateSimplifyVo, HttpServletRequest request) {
        QuestionCreateVo questionCreateVo = new QuestionCreateVo();
        // 把能複製的屬性複製到新的物件
        BeanUtils.copyProperties(questionCreateSimplifyVo, questionCreateVo);
        String userId = (String) request.getAttribute("user_id");
        questionCreateVo.setQuestionCreatorId(userId);
        try {
            examService.questionCreate(questionCreateVo);
            return new ResultVO<>(ResultEnum.CREATE_QUESTION_SUCCESS.getCode(), ResultEnum.CREATE_QUESTION_SUCCESS.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(ResultEnum.CREATE_QUESTION_FAILED.getCode(), ResultEnum.CREATE_QUESTION_FAILED.getMessage(), null);
        }
    }

    @GetMapping("/question/selection")
    @Operation(summary = "取得問題分類的相關選項", description = "取得問題分類的相關選項")
    ResultVO<QuestionSelectionVo> getSelections() {
        QuestionSelectionVo questionSelectionVo = examService.getSelections();
        if (questionSelectionVo != null) {
            return new ResultVO<>(ResultEnum.GET_SELECTIONS_SUCCESS.getCode(), ResultEnum.GET_SELECTIONS_SUCCESS.getMessage(), questionSelectionVo);
        } else {
            return new ResultVO<>(ResultEnum.GET_SELECTIONS_FAILED.getCode(), ResultEnum.GET_SELECTIONS_FAILED.getMessage(), null);
        }
    }

    @GetMapping("/question/detail/{id}")
    @Operation(summary = "根據考試問題的id查看詳細資訊", description = "根據考試問題的id查看詳細資訊")
    ResultVO<QuestionDetailVo> getQuestionDetail(@PathVariable String id) {
        ResultVO<QuestionDetailVo> resultVO;
        try {
            QuestionDetailVo questionDetailVo = examService.getQuestionDetail(id);
            resultVO = new ResultVO<>(ResultEnum.GET_QUESTION_DETAILS_SUCCESS.getCode(), ResultEnum.GET_QUESTION_DETAILS_SUCCESS.getMessage(), questionDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_QUESTION_DETAILS_FAILED.getCode(), ResultEnum.GET_QUESTION_DETAILS_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/question/type/list")
    @Operation(summary = "取得考試問題列表，依單選、多選和是非題型", description = "取得考試問題列表，依單選、多選和是非題型")
    ResultVO<ExamQuestionTypeVo> getExamQuestionTypeList() {
        ResultVO<ExamQuestionTypeVo> resultVO;
        try {
            ExamQuestionTypeVo examQuestionTypeVo = examService.getExamQuestionType();
            resultVO = new ResultVO<>(ResultEnum.GET_QUESTION_BY_TYPE_SUCCESS.getCode(), ResultEnum.GET_QUESTION_BY_TYPE_SUCCESS.getMessage(), examQuestionTypeVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_QUESTION_BY_TYPE_FAILED.getCode(), ResultEnum.GET_QUESTION_BY_TYPE_FAILED.getMessage(), null);
        }
        return resultVO;
    }


    @PostMapping("/finish/{examId}")
    @Operation(summary = "考生送出考試答案，並計算分數回傳", description = "考生送出考試答案，並計算分數回傳")
    ResultVO<ExamRecord> finishExam(@PathVariable String examId, @RequestBody HashMap<String, List<String>> answersMap, HttpServletRequest request) {
        ResultVO<ExamRecord> resultVO;
        try {
            String userId = (String) request.getAttribute("user_id");
            ExamRecord examRecord = examService.judge(userId, examId, answersMap);
            resultVO = new ResultVO<>(ResultEnum.SUBMIT_EXAM_SUCCESS.getCode(), ResultEnum.SUBMIT_EXAM_SUCCESS.getMessage(), examRecord);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.SUBMIT_EXAM_FAILED.getCode(), ResultEnum.SUBMIT_EXAM_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/record/list")
    @Operation(summary = "取得該登入用戶的考試紀錄", description = "取得該登入用戶的考試紀錄")
    ResultVO<List<ExamRecordVo>> getExamRecordList(HttpServletRequest request) {
        ResultVO<List<ExamRecordVo>> resultVO;
        try {
            String userId = (String) request.getAttribute("user_id");
            List<ExamRecordVo> examRecordVoList = examService.getExamRecordList(userId);
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_BY_USERID_SUCCESS.getCode(), ResultEnum.GET_EXAM_RECORD_BY_USERID_SUCCESS.getMessage(), examRecordVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_BY_USERID_FAILED.getCode(), ResultEnum.GET_EXAM_RECORD_BY_USERID_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/record/list/all")
    @Operation(summary = "取得所有用戶的考試紀錄", description = "取得所有用戶的考試紀錄")
    ResultVO<List<ExamRecordVo>> getAllExamRecordList(HttpServletRequest request) {
        ResultVO<List<ExamRecordVo>> resultVO;
        try {
            String userId = (String) request.getAttribute("user_id");
            List<ExamRecordVo> examRecordVoList = examService.getAllExamRecordList();
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_BY_USERID_SUCCESS.getCode(), ResultEnum.GET_EXAM_RECORD_BY_USERID_SUCCESS.getMessage(), examRecordVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_BY_USERID_FAILED.getCode(), ResultEnum.GET_EXAM_RECORD_BY_USERID_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/record/detail/{recordId}")
    @Operation(summary = "依照考試紀錄id，取得考試紀錄詳情", description = "依照考試紀錄id，取得考試紀錄詳情")
    ResultVO<RecordDetailVo> getExamRecordDetail(@PathVariable String recordId) {
        ResultVO<RecordDetailVo> resultVO;
        try {
            RecordDetailVo recordDetailVo = examService.getRecordDetail(recordId);
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_DETAILS_SUCCESS.getCode(), ResultEnum.GET_EXAM_RECORD_DETAILS_SUCCESS.getMessage(), recordDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(ResultEnum.GET_EXAM_RECORD_DETAILS_FAILED.getCode(), ResultEnum.GET_ALL_EXAM_FAILED.getMessage(), null);
        }
        return resultVO;
    }
}

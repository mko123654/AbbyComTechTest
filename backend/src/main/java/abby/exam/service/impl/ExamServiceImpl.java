/***********************************************************
 * @Description : ExamService實作
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-16 01:31
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.service.impl;

import abby.exam.entity.*;
import abby.exam.repository.*;
import abby.exam.vo.*;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import abby.exam.enums.QuestionEnum;
import abby.exam.service.ExamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final ExamRecordRepository examRecordRepository;

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final QuestionLevelRepository questionLevelRepository;

    private final QuestionTypeRepository questionTypeRepository;

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionOptionRepository questionOptionRepository;

    public ExamServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, QuestionLevelRepository questionLevelRepository, QuestionTypeRepository questionTypeRepository, QuestionCategoryRepository questionCategoryRepository, QuestionOptionRepository questionOptionRepository, ExamRepository examRepository, ExamRecordRepository examRecordRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionLevelRepository = questionLevelRepository;
        this.questionTypeRepository = questionTypeRepository;
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.examRepository = examRepository;
        this.examRecordRepository = examRecordRepository;
    }

    @Override
    public List<QuestionVo> getQuestionAll() {
        List<Question> questionList = questionRepository.findAll();
        return getQuestionVos(questionList);
    }

    private List<QuestionVo> getQuestionVos(List<Question> questionList) {
        List<QuestionVo> questionVoList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVo questionVo = getQuestionVo(question);
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }

    private QuestionVo getQuestionVo(Question question) {
        QuestionVo questionVo = new QuestionVo();
        BeanUtils.copyProperties(question, questionVo);

        // 建立人員
        questionVo.setQuestionCreator(
                Objects.requireNonNull(
                        userRepository.findById(
                                question.getQuestionCreatorId()
                        ).orElse(null)
                ).getUserUsername());
        // 難度
        questionVo.setQuestionLevel(
                Objects.requireNonNull(
                        questionLevelRepository.findById(
                                question.getQuestionLevelId()
                        ).orElse(null)
                ).getQuestionLevelDescription());

        // 題目類別(單選、多選、是非)
        questionVo.setQuestionType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription());

        // 題目分類(JAVA、C#、Python)
        questionVo.setQuestionCategory(
                Objects.requireNonNull(
                        questionCategoryRepository.findById(
                                question.getQuestionCategoryId()
                        ).orElse(null)
                ).getQuestionCategoryName()
        );

        // 取得所有選項列表
        List<QuestionOptionVo> optionVoList = new ArrayList<>();
        List<QuestionOption> optionList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionOptionIds().split("-"))
        );

        // 取得所有的答案列表optionList中每個option的isAnswer選項
        List<QuestionOption> answerList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionAnswerOptionIds().split("-"))
        );

        // 依照選項和答案的id相同設定optionVo的isAnswer屬性
        for (QuestionOption option : optionList) {
            QuestionOptionVo optionVo = new QuestionOptionVo();
            BeanUtils.copyProperties(option, optionVo);
            for (QuestionOption answer : answerList) {
                if (option.getQuestionOptionId().equals(answer.getQuestionOptionId())) {
                    optionVo.setAnswer(true);
                }
            }
            optionVoList.add(optionVo);
        }

        // 設定題目的所有選項
        questionVo.setQuestionOptionVoList(optionVoList);
        return questionVo;
    }

    @Override
    public QuestionVo updateQuestion(QuestionVo questionVo) {
        // 1.把需要的屬性都設定好
        StringBuilder questionAnswerOptionIds = new StringBuilder();
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionVo> questionOptionVoList = questionVo.getQuestionOptionVoList();
        int size = questionOptionVoList.size();
        for (int i = 0; i < questionOptionVoList.size(); i++) {
            QuestionOptionVo questionOptionVo = questionOptionVoList.get(i);
            QuestionOption questionOption = new QuestionOption();
            BeanUtils.copyProperties(questionOptionVo, questionOption);
            questionOptionList.add(questionOption);
            if (questionOptionVo.getAnswer()) {
                if (i != size - 1) {
                    // 把更新後的答案id加上去(要用-組起來)
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId()).append("-");
                } else {
                    // 最後一個不用加-
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId());
                }
            }
        }

        // 1.更新問題
        Question question = questionRepository.findById(questionVo.getQuestionId()).orElse(null);
        assert question != null;
        BeanUtils.copyProperties(questionVo, question);
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds.toString());
        questionRepository.save(question);

        // 2.更新所有的option
        questionOptionRepository.saveAll(questionOptionList);

        return getQuestionVo(question);
    }

    @Override
    public void questionCreate(QuestionCreateVo questionCreateVo) {
        Question question = new Question();
        BeanUtils.copyProperties(questionCreateVo, question);

        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionCreateVo> questionOptionCreateVoList = questionCreateVo.getQuestionOptionCreateVoList();
        for (QuestionOptionCreateVo questionOptionCreateVo : questionOptionCreateVoList) {
            QuestionOption questionOption = new QuestionOption();
            // 設定選項內容
            questionOption.setQuestionOptionContent(questionOptionCreateVo.getQuestionOptionContent());
            // 設定選項ID
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOptionList.add(questionOption);
        }
        // 把選項都存起來，才能用於下面設定Question的questionOptionIds和questionAnswerOptionIds
        questionOptionRepository.saveAll(questionOptionList);
        String questionOptionIds = "";
        String questionAnswerOptionIds = "";

        for (int i = 0; i < questionOptionCreateVoList.size(); i++) {
            QuestionOptionCreateVo questionOptionCreateVo = questionOptionCreateVoList.get(i);
            QuestionOption questionOption = questionOptionList.get(i);
            questionOptionIds += questionOption.getQuestionOptionId() + "-";
            if (questionOptionCreateVo.getAnswer()) {
                // 如果是答案的話
                questionAnswerOptionIds += questionOption.getQuestionOptionId() + "-";
            }
        }
        // 把字串最後面的-刪掉
        questionAnswerOptionIds = replaceLastSeparator(questionAnswerOptionIds);
        questionOptionIds = replaceLastSeparator(questionOptionIds);
        // 設定選項ID组成的字串
        question.setQuestionOptionIds(questionOptionIds);
        // 設定答案選項ID组成的字串
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds);
        question.setQuestionId(IdUtil.simpleUUID());
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        questionRepository.save(question);
    }

    @Override
    public QuestionSelectionVo getSelections() {
        QuestionSelectionVo questionSelectionVo = new QuestionSelectionVo();
        questionSelectionVo.setQuestionCategoryList(questionCategoryRepository.findAll());
        questionSelectionVo.setQuestionLevelList(questionLevelRepository.findAll());
        questionSelectionVo.setQuestionTypeList(questionTypeRepository.findAll());

        return questionSelectionVo;
    }

    public static String trimMiddleLine(String str) {
        if (str.charAt(str.length() - 1) == '-') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public QuestionDetailVo getQuestionDetail(String id) {
        Question question = questionRepository.findById(id).orElse(null);
        QuestionDetailVo questionDetailVo = new QuestionDetailVo();
        questionDetailVo.setId(id);
        questionDetailVo.setName(question.getQuestionName());
        questionDetailVo.setDescription(question.getQuestionDescription());
        // 問題類型，單選、多選、是非
        questionDetailVo.setType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription()
        );
        // 取得問題選項
        String optionIdsStr = trimMiddleLine(question.getQuestionOptionIds());
        String[] optionIds = optionIdsStr.split("-");
        // 取得選項列表
        List<QuestionOption> optionList = questionOptionRepository.findAllById(Arrays.asList(optionIds));
        questionDetailVo.setOptions(optionList);
        return questionDetailVo;
    }

    @Override
    public List<ExamVo> getExamAll() {
        List<Exam> examList = examRepository.findAll();
        return getExamVos(examList);
    }

    private List<ExamVo> getExamVos(List<Exam> examList) {

        List<ExamVo> examVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            BeanUtils.copyProperties(exam, examVo);
            examVo.setExamCreator(
                    Objects.requireNonNull(
                            userRepository.findById(
                                    exam.getExamCreatorId()
                            ).orElse(null)
                    ).getUserUsername()
            );

            // 取得所有單選題
            List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
            List<Question> radioQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsRadio().split("-"))
            );
            for (Question question : radioQuestionList) {
                ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, radioQuestionVo);
                radioQuestionVo.setChecked(true); // 考試中的問題肯定被選中的
                radioQuestionVoList.add(radioQuestionVo);
            }
            examVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

            // 取得所有多選題
            List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
            List<Question> checkQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsCheck().split("-"))
            );
            for (Question question : checkQuestionList) {
                ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, checkQuestionVo);
                checkQuestionVo.setChecked(true); // 考試中的問題肯定被選中的
                checkQuestionVoList.add(checkQuestionVo);
            }
            examVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

            // 取得所有是非題
            List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
            List<Question> judgeQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsJudge().split("-"))
            );
            for (Question question : judgeQuestionList) {
                ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, judgeQuestionVo);
                judgeQuestionVo.setChecked(true); // 考試中的問題肯定被選中的
                judgeQuestionVoList.add(judgeQuestionVo);
            }
            examVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);
            examVoList.add(examVo);
        }
        return examVoList;
    }



    @Override
    public ExamQuestionTypeVo getExamQuestionType() {
        ExamQuestionTypeVo examQuestionTypeVo = new ExamQuestionTypeVo();
        List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
        List<Question> radioQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.RADIO.getId());
        for (Question question : radioQuestionList) {
            ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, radioQuestionVo);
            radioQuestionVoList.add(radioQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

        List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
        List<Question> checkQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.CHECK.getId());
        for (Question question : checkQuestionList) {
            ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, checkQuestionVo);
            checkQuestionVoList.add(checkQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

        List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
        List<Question> judgeQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.JUDGE.getId());
        for (Question question : judgeQuestionList) {
            ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, judgeQuestionVo);
            judgeQuestionVoList.add(judgeQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);
        return examQuestionTypeVo;
    }

    @Override
    public Exam create(ExamCreateVo examCreateVo, String userId) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateVo, exam);
        exam.setExamId(IdUtil.simpleUUID());
        exam.setExamCreatorId(userId);
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());
        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examCreateVo.getRadios();
        List<ExamQuestionSelectVo> checks = examCreateVo.getChecks();
        List<ExamQuestionSelectVo> judges = examCreateVo.getJudges();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);
        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        examRepository.save(exam);
        return exam;
    }

    @Override
    public Exam update(ExamVo examVo, String userId) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examVo, exam);
        exam.setExamCreatorId(userId);
        exam.setUpdateTime(new Date());

        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examVo.getExamQuestionSelectVoRadioList();
        List<ExamQuestionSelectVo> checks = examVo.getExamQuestionSelectVoCheckList();
        List<ExamQuestionSelectVo> judges = examVo.getExamQuestionSelectVoJudgeList();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);

        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        examRepository.save(exam);
        return exam;
    }

    @Override
    public List<ExamCardVo> getExamCardList() {
        List<Exam> examList = examRepository.findAll();
        List<ExamCardVo> examCardVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamCardVo examCardVo = new ExamCardVo();
            BeanUtils.copyProperties(exam, examCardVo);
            examCardVoList.add(examCardVo);
        }
        return examCardVoList;
    }

    @Override
    public ExamDetailVo getExamDetail(String id) {
        Exam exam = examRepository.findById(id).orElse(null);
        ExamDetailVo examDetailVo = new ExamDetailVo();
        examDetailVo.setExam(exam);
        assert exam != null;
        examDetailVo.setRadioIds(exam.getExamQuestionIdsRadio().split("-"));
        examDetailVo.setCheckIds(exam.getExamQuestionIdsCheck().split("-"));
        examDetailVo.setJudgeIds(exam.getExamQuestionIdsJudge().split("-"));
        return examDetailVo;
    }

    @Override
    public ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap) {
        // 評分
        ExamDetailVo examDetailVo = getExamDetail(examId);

        Exam exam = examDetailVo.getExam();
        List<String> questionIds = new ArrayList<>();

        List<String> radioIdList = Arrays.asList(examDetailVo.getRadioIds());
        List<String> checkIdList = Arrays.asList(examDetailVo.getCheckIds());
        List<String> judgeIdList = Arrays.asList(examDetailVo.getJudgeIds());
        questionIds.addAll(radioIdList);
        questionIds.addAll(checkIdList);
        questionIds.addAll(judgeIdList);

        int radioScore = exam.getExamScoreRadio();
        int checkScore = exam.getExamScoreCheck();
        int judgeScore = exam.getExamScoreJudge();

        List<Question> questionList = questionRepository.findAllById(questionIds);
        Map<String, Question> questionMap = new HashMap<>();
        for (Question question : questionList) {
            questionMap.put(question.getQuestionId(), question);
        }

        Set<String> questionIdsAnswer = answersMap.keySet();

        Map<String, Integer> judgeMap = new HashMap<>();

        StringBuilder answerOptionIdsSb = new StringBuilder();

        int totalScore = 0;
        for (String questionId : questionIdsAnswer) {

            Question question = questionMap.get(questionId);

            String questionAnswerOptionIds = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            List<String> questionAnswerOptionIdList = Arrays.asList(questionAnswerOptionIds.split("-"));
            Collections.sort(questionAnswerOptionIdList);
            String answerStr = listConcat(questionAnswerOptionIdList);

            List<String> questionUserOptionIdList = answersMap.get(questionId);
            Collections.sort(questionUserOptionIdList);
            String userStr = listConcat(questionUserOptionIdList);

            if (answerStr.equals(userStr)) {
                int score = 0;
                if (radioIdList.contains(questionId)) {
                    score = radioScore;
                }
                if (checkIdList.contains(questionId)) {
                    score = checkScore;
                }
                if (judgeIdList.contains(questionId)) {
                    score = judgeScore;
                }
                totalScore += score;
                answerOptionIdsSb.append(questionId + "@True_" + userStr + "$");
                judgeMap.put(questionId, score);
            } else {
                answerOptionIdsSb.append(questionId + "@False_" + userStr + "$");
                judgeMap.put(questionId, 0);
            }
        }
        ExamRecord examRecord = new ExamRecord();
        examRecord.setExamRecordId(IdUtil.simpleUUID());
        examRecord.setExamId(examId);
        examRecord.setAnswerOptionIds(replaceLastSeparator(answerOptionIdsSb.toString()));
        examRecord.setExamJoinerId(userId);
        examRecord.setExamJoinDate(new Date());
        examRecord.setExamJoinScore(totalScore);
        examRecordRepository.save(examRecord);
        return examRecord;
    }

    @Override
    public List<ExamRecordVo> getExamRecordList(String userId) {
        List<ExamRecord> examRecordList = examRecordRepository.findByExamJoinerIdOrderByExamJoinDateDesc(userId);
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examRepository.findById(examRecord.getExamId()).orElse(null);
            examRecordVo.setExam(exam);
            User user = userRepository.findById(userId).orElse(null);
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public List<ExamRecordVo> getAllExamRecordList() {
        List<ExamRecord> examRecordList = examRecordRepository.findAll();
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examRepository.findById(examRecord.getExamId()).orElse(null);
            examRecordVo.setExam(exam);
            User user = userRepository.findById(examRecord.getExamJoinerId()).orElse(null);
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public RecordDetailVo getRecordDetail(String recordId) {
        ExamRecord record = examRecordRepository.findById(recordId).orElse(null);
        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setExamRecord(record);

        HashMap<String, List<String>> answersMap = new HashMap<>();
        HashMap<String, String> resultsMap = new HashMap<>();
        assert record != null;
        String answersStr = record.getAnswerOptionIds();

        String[] questionArr = answersStr.split("[$]");
        for (String questionStr : questionArr) {
            System.out.println(questionStr);

            String[] questionTitleResultAndOption = questionStr.split("_");
            String[] questionTitleAndResult = questionTitleResultAndOption[0].split("@");
            String[] questionOptions = questionTitleResultAndOption[1].split("-");

            answersMap.put(questionTitleAndResult[0], Arrays.asList(questionOptions));

            resultsMap.put(questionTitleAndResult[0], questionTitleAndResult[1]);
        }
        recordDetailVo.setAnswersMap(answersMap);
        recordDetailVo.setResultsMap(resultsMap);

        ExamDetailVo examDetailVo = getExamDetail(record.getExamId());
        List<String> questionIdList = new ArrayList<>();
        questionIdList.addAll(Arrays.asList(examDetailVo.getRadioIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getCheckIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getJudgeIds()));

        List<Question> questionList = questionRepository.findAllById(questionIdList);
        HashMap<String, List<String>> answersRightMap = new HashMap<>();
        for (Question question : questionList) {
            String questionAnswerOptionIdsStr = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            String[] questionAnswerOptionIds = questionAnswerOptionIdsStr.split("-");
            answersRightMap.put(question.getQuestionId(), Arrays.asList(questionAnswerOptionIds));
        }
        recordDetailVo.setAnswersRightMap(answersRightMap);
        return recordDetailVo;
    }

    private String replaceLastSeparator(String str) {
        String lastChar = str.substring(str.length() - 1);
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    private String listConcat(List<String> strList) {
        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            sb.append(str);
            sb.append("-");
        }
        return replaceLastSeparator(sb.toString());
    }
}

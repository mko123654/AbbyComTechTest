/***********************************************************
 * @Description : 結果Enum，配合controller使用
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-18 18:57
 * @email       : mko123654@gmail.com
 ***********************************************************/

package abby.exam.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    // User
    REGISTER_SUCCESS(0, "註冊成功"),
    REGISTER_FAILED(-2, "註冊失敗"),
    LOGIN_SUCCESS(0, "登入成功"),
    LOGIN_FAILED(-1, "帳號或密碼錯誤"),
    GET_INFO_SUCCESS(0, "取得使用者訊息成功"),

    //Exam
    GET_ALL_EXAM_SUCCESS(0, "取得所有考試的列表成功"),
    GET_ALL_EXAM_FAILED(-1, "取得所有考試的列表失敗"),
    GET_EXAM_DETAILS_SUCCESS(0, "取得考試詳細資訊成功"),
    GET_EXAM_DETAILS_FAILED(-1, "取得考試詳細資訊失敗"),
    GET_CARD_LIST_SUCCESS(0, "取得考試列表卡片成功"),
    GET_CARD_LIST_FAILED(-1, "取得考試列表卡片失敗"),
    CREATE_EXAM_SUCCESS(0, "新增考試成功"),
    CREATE_EXAM_FAILED(-1, "新增考試失敗"),
    UPDATE_EXAM_SUCCESS(0, "更新考試成功"),
    UPDATE_EXAM_FAILED(-1, "更新考試失敗"),

    GET_ALL_QUESTION_SUCCESS(0, "取得所有考試題目成功"),
    GET_ALL_QUESTION_FAILED(-1, "取得所有考試題目失敗"),
    GET_SELECTIONS_SUCCESS(0, "取得問題分類的相關選項成功"),
    GET_SELECTIONS_FAILED(-1, "取得問題分類的相關選項失敗"),
    GET_QUESTION_BY_TYPE_SUCCESS(0, "依題型取得考試問題列表成功"),
    GET_QUESTION_BY_TYPE_FAILED(-1, "依題型取得考試問題列表失敗"),
    GET_QUESTION_DETAILS_SUCCESS(0, "根據考試問題的id查看詳細資訊成功"),
    GET_QUESTION_DETAILS_FAILED(-1, "根據考試問題的id查看詳細資訊失敗"),
    CREATE_QUESTION_SUCCESS(0, "新增考試題目成功"),
    CREATE_QUESTION_FAILED(-1, "新增考試題目失敗"),
    UPDATE_QUESTION_SUCCESS(0, "更新考試題目成功"),
    UPDATE_QUESTION_FAILED(-1, "更新考試題目失敗"),

    GET_EXAM_RECORD_BY_USERID_SUCCESS(0, "取得個人考試紀錄成功"),
    GET_EXAM_RECORD_BY_USERID_FAILED(-1, "取得個人考試紀錄失敗"),
    GET_EXAM_RECORD_DETAILS_SUCCESS(0, "取得考試紀錄詳情成功"),
    GET_EXAM_RECORD_DETAILS_FAILED(-1, "取得考試紀錄詳情失敗"),
    SUBMIT_EXAM_SUCCESS(0, "考券送出成功"),
    SUBMIT_EXAM_FAILED(-1, "考券送出失敗"),
    ;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;
}

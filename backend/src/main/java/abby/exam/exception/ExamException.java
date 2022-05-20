/***********************************************************
 * @Description : 自訂的錯誤訊息
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-14 14:51
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.exception;

import abby.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ExamException extends RuntimeException {
    private Integer code;

    public ExamException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ExamException( Integer code, String message) {
        super(message);
        this.code = code;
    }
}

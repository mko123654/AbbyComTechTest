package abby.exam.service;

import abby.exam.dto.RegisterDTO;
import abby.exam.qo.LoginQo;
import abby.exam.vo.UserInfoVo;
import abby.exam.vo.UserVo;
import abby.exam.entity.User;

public interface UserService {

    User register(RegisterDTO registerDTO);

    String login(LoginQo loginQo);

    UserVo getUserInfo(String userId);

    /**
     * 取得用戶詳情(主要是判定權限)
     */
    UserInfoVo getInfo(String userId);
}

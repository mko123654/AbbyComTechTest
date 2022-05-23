/***********************************************************
 * @Description : 考試RESTful API
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-16 01:12
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.controller;

import abby.exam.dto.RegisterDTO;
import abby.exam.entity.User;
import abby.exam.enums.ResultEnum;
import abby.exam.qo.LoginQo;
import abby.exam.service.UserService;
import abby.exam.vo.ResultVO;
import abby.exam.vo.UserInfoVo;
import abby.exam.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "User APIs")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "", description = "使用者註冊")
    ResultVO<User> register(@RequestBody RegisterDTO registerDTO) {
        ResultVO<User> resultVO;
        User user = userService.register(registerDTO);
        if (user != null) {
            // 註冊成功
            resultVO = new ResultVO<>(ResultEnum.REGISTER_SUCCESS.getCode(), ResultEnum.REGISTER_SUCCESS.getMessage(), user);
        } else {
            resultVO = new ResultVO<>(ResultEnum.REGISTER_FAILED.getCode(), ResultEnum.REGISTER_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @PostMapping("/login")
    @Operation(summary = "", description = "根據username或email登入，登入成功回傳token")
    ResultVO<String> login(@RequestBody LoginQo loginQo) {
        ResultVO<String> resultVO;
        String token = userService.login(loginQo);
        if (token != null) {
            resultVO = new ResultVO<>(ResultEnum.LOGIN_SUCCESS.getCode(), ResultEnum.LOGIN_SUCCESS.getMessage(), token);
        } else {
            resultVO = new ResultVO<>(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/user-info")
    @Operation(summary = "", description = "取得UserInfo")
    ResultVO<UserVo> getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        UserVo userVo = userService.getUserInfo(userId);
        return new ResultVO<>(ResultEnum.GET_INFO_SUCCESS.getCode(), ResultEnum.GET_INFO_SUCCESS.getMessage(), userVo);
    }

    @GetMapping("/info")
    @Operation(summary = "", description = "取得UserInfo，包含個人資料和操作權限")
    ResultVO<UserInfoVo> getInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        UserInfoVo userInfoVo = userService.getInfo(userId);
        return new ResultVO<>(ResultEnum.GET_INFO_SUCCESS.getCode(), ResultEnum.GET_INFO_SUCCESS.getMessage(), userInfoVo);
    }

    @GetMapping("/test")
    @Operation(summary = "", description = "test API")
    String test(HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        String username = (String) request.getAttribute("username");
        System.out.println("userId：" + userId);
        System.out.println("userName：" + username);
        return "userId：" + userId + "userName：" + username;
    }
}

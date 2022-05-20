/***********************************************************
 * @Description : UserService實作
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-15 08:12
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.service.impl;

import abby.exam.dto.RegisterDTO;
import abby.exam.entity.Action;
import abby.exam.entity.Page;
import abby.exam.qo.LoginQo;
import abby.exam.vo.*;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import abby.exam.entity.Role;
import abby.exam.entity.User;
import abby.exam.enums.LoginTypeEnum;
import abby.exam.enums.RoleEnum;
import abby.exam.repository.ActionRepository;
import abby.exam.repository.PageRepository;
import abby.exam.repository.RoleRepository;
import abby.exam.repository.UserRepository;
import abby.exam.service.UserService;
import abby.exam.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    ActionRepository actionRepository;


    @Override
    public User register(RegisterDTO registerDTO) {
        try {
            User user = new User();
            user.setUserId(IdUtil.simpleUUID());
            // 用"user_{手機號碼}"作為用戶名稱
            String defaultUsername = "user";
            user.setUserUsername(defaultUsername + "_" + registerDTO.getMobile());
            user.setUserNickname(user.getUserUsername());
            user.setUserPassword(Base64.encode(registerDTO.getPassword()));
            // 預測角色為考生，其他角色權限需管理員修改
            user.setUserRoleId(RoleEnum.STUDENT.getId());
            // 預設頭像
            String defaultAvatar = "https://i.imgur.com/9LDfN2H.png";
            user.setUserAvatar(defaultAvatar);
            user.setUserDescription("welcome to online exam system");
            user.setUserEmail(registerDTO.getEmail());
            user.setUserPhone(registerDTO.getMobile());
            user.setCreateTime(Date.from(new Date().toInstant()));
            user.setUpdateTime(Date.from(new Date().toInstant()));
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String login(LoginQo loginQo) {
        User user;

        if (LoginTypeEnum.USERNAME.getType().equals(loginQo.getLoginType())) {
            user = userRepository.findByUserUsername(loginQo.getUserInfo());
        } else {
            user = userRepository.findByUserEmail(loginQo.getUserInfo());
        }
        if (user != null) {
            String passwordDb = Base64.decodeStr(user.getUserPassword());
            String passwordQo = loginQo.getPassword();
            if (passwordQo.equals(passwordDb)) {
                // 驗證成功回傳Token，有效期為一天
                return JwtUtils.genJsonWebToken(user);
            }
        }
        return null;
    }

    @Override
    public UserVo getUserInfo(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        UserVo userVo = new UserVo();
        assert user != null;
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public UserInfoVo getInfo(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        Integer roleId = user.getUserRoleId();
        Role role = roleRepository.findById(roleId).orElse(null);
        assert role != null;
        String roleName = role.getRoleName();
        userInfoVo.setRoleName(roleName);

        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(role, roleVo);

        // 設定角色可進入畫面
        String rolePageIds = role.getRolePageIds();
        String[] pageIdArr = rolePageIds.split("-");
        List<PageVo> pageVoList = new ArrayList<>();
        for (String pageIdStr : pageIdArr) {
            Integer pageId = Integer.parseInt(pageIdStr);

            Page page = pageRepository.findById(pageId).orElse(null);
            PageVo pageVo = new PageVo();
            BeanUtils.copyProperties(page, pageVo);

            List<ActionVo> actionVoList = new ArrayList<>();
            String actionIdsStr = page.getActionIds();
            String[] actionIdArr = actionIdsStr.split("-");
            for (String actionIdStr : actionIdArr) {
                Integer actionId = Integer.parseInt(actionIdStr);
                Action action = actionRepository.findById(actionId).orElse(null);
                ActionVo actionVo = new ActionVo();
                assert action != null;
                BeanUtils.copyProperties(action, actionVo);
                actionVoList.add(actionVo);
            }
            pageVo.setActionVoList(actionVoList);
            pageVoList.add(pageVo);
        }

        roleVo.setPageVoList(pageVoList);
        userInfoVo.setRoleVo(roleVo);
        return userInfoVo;
    }
}

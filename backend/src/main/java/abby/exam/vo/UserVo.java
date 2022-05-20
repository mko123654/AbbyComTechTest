package abby.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserVo {
    @JsonProperty("id")
    private String userId;

    @JsonProperty("username")
    private String userUsername;

    @JsonProperty("nickname")
    private String userNickname;

    @JsonProperty("role")
    private Integer userRoleId;

    @JsonProperty("avatar")
    private String userAvatar;

    @JsonProperty("description")
    private String userDescription;

    @JsonProperty("email")
    private String userEmail;

    @JsonProperty("phone")
    private String userPhone;
}

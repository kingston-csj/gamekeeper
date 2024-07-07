package jforgame.admin.io.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqModifyPass {

    private Long userId;

    private String oldPass;

    private String newPass;

    private String checkNewPass;
}

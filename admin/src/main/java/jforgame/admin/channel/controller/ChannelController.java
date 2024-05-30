package jforgame.admin.channel.controller;

import jforgame.admin.channel.service.ChannelService;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.system.model.RoleKinds;
import jforgame.admin.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/children", method = RequestMethod.GET)
    public List<String> getChildrenChannels() {
        String code = userService.getCurrentUser();
        if (SecurityUtils.hasAuth(RoleKinds.ADMIN)) {
            return channelService.queryAllChannels();
        }
        return channelService.queryChildChannel(code);
    }

}

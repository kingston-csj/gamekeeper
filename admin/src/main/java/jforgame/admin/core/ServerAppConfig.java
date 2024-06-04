package jforgame.admin.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Component
@Data
public class ServerAppConfig {

    /**
     * 免登录白名单ip（支持子网掩码）
     */
    private String anonymousClientIp;
}

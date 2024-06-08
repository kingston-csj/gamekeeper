package jforgame.admin.logger;

import org.slf4j.Logger;

public enum LoggerFunction {

    // 服务器节点配置
    SERVER_NODE,
    //gs节点命令
    ADMIN_CMD,
    //文件操作
    FILE,

    ;


    public Logger getLogger() {
        return LoggerBuilder.getLogger(this.name());
    }


}

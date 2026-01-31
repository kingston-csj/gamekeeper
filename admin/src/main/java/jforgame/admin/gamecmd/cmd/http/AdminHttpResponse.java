package jforgame.admin.gamecmd.cmd.http;


import lombok.Getter;
import lombok.Setter;

/**
 * 后台管理http请求返回值
 * 当code为0时，data为返回数据(可能为空)
 * 当code为1时，message为错误消息(可能为空)
 */
@Getter
@Setter
public class AdminHttpResponse {
    /**
     * 执行成功
     */
    public static final byte SUCC = 0;
    /**
     * 执行失败， 非0
     */
    public static final byte FAILED = 1;
    /**
     * 执行结果状态码
     */
    private byte code;
    /**
     * 错误消息
     */
    private String message;

    /**
     * 返回数据
     */
    private String data;

    public static AdminHttpResponse ok() {
        AdminHttpResponse response = new AdminHttpResponse();
        response.code = SUCC;
        response.data = "执行成功";
        return response;
    }

    public static AdminHttpResponse ok(String data) {
        AdminHttpResponse response = new AdminHttpResponse();
        response.code = SUCC;
        response.data = data;
        return response;
    }


    public static AdminHttpResponse failed(String message) {
        AdminHttpResponse response = new AdminHttpResponse();
        response.code = FAILED;
        response.message = message;
        return response;
    }


    @Override
    public String toString() {
        return "HttpCommandResponse [code=" + code + ", message="
                + message + "]";
    }

}
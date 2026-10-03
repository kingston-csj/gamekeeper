package jforgame.admin.s3;

public class OssException extends RuntimeException {

    public OssException(String msg) {
        super(msg);
    }

    public OssException(Exception e) {
        super(e);
    }

}
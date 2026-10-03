package jforgame.admin.s3;

public class FileUtil {

    /**
     * 返回一个文件的类型后缀(统一返回小写)
     * @param fileName
     * @return
     */
    public static String suffixType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}

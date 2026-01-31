package jforgame.admin.file.controller;

import jforgame.admin.file.io.UploadFileVo;
import jforgame.admin.file.service.FileService;
import jforgame.admin.http.HttpResult;
import jforgame.admin.logger.LoggerFunction;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.oss.OssService;
import jforgame.admin.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    OssService ossService;

    @Autowired
    FileService fileService;

    @PostMapping("/uploadPic")
    public HttpResult uploadPic(@RequestParam("file") MultipartFile file) {
        try (InputStream ignored = file.getInputStream()) {
            UploadFileVo fileVo = UploadFileVo.builder().fileName(file.getOriginalFilename())
                    .inputStream(file.getInputStream()).contentType(file.getContentType()).size(file.getSize())
                    .fileData(file.getBytes())
                    .build();
            String suffix = fileVo.getFileName().substring(fileVo.getFileName().lastIndexOf(".") + 1);
            if (!ossService.allowFileType(suffix)) {
                return HttpResult.error("图片格式不允许");
            }
//            T_Picture oss = fileService.uploadPicture(fileVo);
//            LoggerUtil.info(LoggerFunction.FILE, "operator", SecurityUtils.getUsername(), "type", "uploadPic", "fileName", fileVo.getFileName(), "url", oss.getUrl());
            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

}

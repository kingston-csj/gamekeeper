package jforgame.admin.file.controller;

import jforgame.admin.core.SpringContext;
import jforgame.admin.external.ExternalController;
import jforgame.admin.file.service.FileService;
import jforgame.admin.file.io.UploadFileVo;
import jforgame.admin.http.HttpResult;
import jforgame.admin.oss.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
                    .build();
            String suffix = fileVo.getFileName().substring(fileVo.getFileName().lastIndexOf(".") + 1);
            if (!ossService.allowFileType(suffix)) {
                return HttpResult.error("图片格式不允许");
            }
            fileService.upload(fileVo);

            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @GetMapping("/allPicture")
    public HttpResult fileList() {
        return HttpResult.ok(fileService.queryAllPicture());
    }

    @PostMapping("/uploadFont")
    public HttpResult uploadFont(@RequestParam("file") MultipartFile file) {
        try (InputStream ignored = file.getInputStream()) {
            UploadFileVo fileVo = UploadFileVo.builder().fileName(file.getOriginalFilename())
                    .inputStream(file.getInputStream()).contentType(file.getContentType()).size(file.getSize())
                    .build();
            String suffix = fileVo.getFileName().substring(fileVo.getFileName().lastIndexOf(".") + 1);
            if (!suffix.equals("ttf")) {
                return HttpResult.error("图片格式不允许");
            }
            fileService.uploadFont(fileVo);

            SpringContext.getBean(ExternalController.class).clearFontJsonCache();

            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @GetMapping("/allFont")
    public HttpResult fontList() {
        return HttpResult.ok(fileService.queryAllFont());
    }

}

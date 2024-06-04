package jforgame.admin.external;

import jforgame.admin.file.io.FontVo;
import jforgame.admin.file.service.FileService;
import jforgame.admin.http.HttpResult;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对外部应用程序提供服务
 */
@RestController
@RequestMapping("/admin/api")
public class ExternalController {

    @Autowired
    FileService fileService;

    private String fontJson;

    @GetMapping("/allFont")
    public HttpResult queryAllFonts() {
        if (!StringUtils.isEmpty(fontJson)) {
            return HttpResult.ok(fontJson);
        }
        synchronized (this) {
            List<FontVo> voList = fileService.queryAllFont();
            this.fontJson = JsonUtil.object2String(voList);
        }

        return HttpResult.ok(this.fontJson);
    }

    public void clearFontJsonCache() {
        this.fontJson = null;
    }



}

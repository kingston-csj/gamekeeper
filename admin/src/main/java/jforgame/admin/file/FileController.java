package jforgame.admin.file;

import jforgame.admin.http.HttpResult;
import jforgame.admin.oss.MinioUtil;
import jforgame.admin.oss.OssService;
import jforgame.commons.Pair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    OssService ossService;

    @Autowired
    OssRecordDao ossRecordDao;

    @Autowired
    MinioUtil minioUtil;

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

            // 图片对象
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            // 宽度
            int width = bufferedImage.getWidth();
            // 高度
            int height = bufferedImage.getHeight();

            // 根据文件类型选择目录
            String catalog = ossService.getPathOf(suffix);
            fileVo.setCatalog(catalog);
            Pair<String, String> urls = minioUtil.uploadFile(fileVo);
            OssRecord oss = new OssRecord();
            oss.setId(ObjectId.get().toString());
            oss.setUrl(urls.getFirst());
            oss.setName(fileVo.getFileName());
            oss.setCreateTime(System.currentTimeMillis());
            oss.setSize(fileVo.getSize());
            oss.setWidth(width);
            oss.setHeight(height);

            ossRecordDao.save(oss);

            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @GetMapping("/allPicture")
    public HttpResult fileList() {
        List<ImageVo> pics = new ArrayList<>();
        ossRecordDao.findAll().forEach(e -> {
                    ImageVo vo = new ImageVo();
                    vo.setUrl(ossService.fullImagePath(e.getUrl()));
                    vo.setName(e.getName());
                    vo.setId(e.getId());
                    pics.add(vo);

                }
        );

        return HttpResult.ok(pics);
    }

}

package jforgame.admin.file.service;

import jforgame.admin.file.dao.FontDao;
import jforgame.admin.file.io.FontVo;
import jforgame.admin.file.io.ImageVo;
import jforgame.admin.file.dao.PictureDao;
import jforgame.admin.file.domain.T_Font;
import jforgame.admin.file.domain.T_Picture;
import jforgame.admin.file.io.UploadFileVo;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.mapstruct.FontMapper;
import jforgame.admin.mapstruct.PictureMapper;
import jforgame.admin.oss.MinioUtil;
import jforgame.admin.oss.OssService;
import jforgame.commons.Pair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    OssService ossService;

    @Autowired
    PictureDao pictureDao;

    @Autowired
    FontDao fontDao;

    @Autowired
    MinioUtil minioUtil;

    public T_Picture uploadPicture(UploadFileVo file) throws IOException {
        try {
            String suffix = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
            // 根据文件类型选择目录
            String catalog = ossService.getPathOf(suffix);
            file.setCatalog(catalog);
            Pair<String, String> urls = minioUtil.uploadFile(file);
            T_Picture oss = new T_Picture();
            oss.setId(ObjectId.get().toString());
            oss.setUrl(urls.getFirst());
            oss.setName(file.getFileName());
            oss.setCreateTime(System.currentTimeMillis());
            oss.setSize(file.getSize());

            // 图片对象
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getFileData()));
            // 宽度
            int width = bufferedImage.getWidth();
            // 高度
            int height = bufferedImage.getHeight();
            oss.setWidth(width);
            oss.setHeight(height);

            pictureDao.save(oss);
            return oss;
        } catch (Exception e) {
            LoggerUtil.error("", e);
            throw new IOException(e);
        }
    }

    public List<ImageVo> queryAllPicture() {
        List<ImageVo> pics = new ArrayList<>();
        PictureMapper mapper = PictureMapper.INSTANCE;
        pictureDao.findAll().forEach(e -> {
            ImageVo vo = mapper.ossRecord2ImageVo(e);
            vo.setUrl(ossService.fullImagePath(vo.getUrl()));
            pics.add(vo);
                }
        );
        return pics;
    }

    public void uploadFont(UploadFileVo file) throws IOException {
        try {
            String suffix = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
            String catalog = ossService.getPathOf(suffix);
            file.setCatalog(catalog);
            Pair<String, String> urls = minioUtil.uploadFile(file);
            T_Font font = new T_Font();
            font.setDescription(file.getFileName());
            font.setUrl(urls.getFirst());
            font.setName(file.getFileName());
            font.setId(ObjectId.get().toString());
            fontDao.save(font);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public List<FontVo> queryAllFont() {
        List<FontVo> vos = new ArrayList<>();
        FontMapper fontMapper = FontMapper.INSTANCE;
        fontDao.findAll().forEach(e -> {
            FontVo vo = fontMapper.entity2Vo(e);
                    vo.setUrl(ossService.fullImagePath(e.getUrl()));
                    vo.setName(e.getName());
                    vos.add(vo);
                }
        );
        return vos;
    }
}

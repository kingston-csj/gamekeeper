package jforgame.admin.file.service;

import org.springframework.stereotype.Service;

@Service
public class FileService {


//    public T_Picture uploadPicture(UploadFileVo file) throws IOException {
//        try {
//            String suffix = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
//            // 根据文件类型选择目录
//            String catalog = ossService.getPathOf(suffix);
//            file.setCatalog(catalog);
//            Pair<String, String> urls = minioUtil.uploadFile(file);
//            T_Picture oss = new T_Picture();
//            oss.setId(IdFactory.nextUUId());
//            oss.setUrl(urls.getFirst());
//            oss.setName(file.getFileName());
//            oss.setCreateTime(System.currentTimeMillis());
//            oss.setSize(file.getSize());
//
//            // 图片对象
//            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getFileData()));
//            // 宽度
//            int width = bufferedImage.getWidth();
//            // 高度
//            int height = bufferedImage.getHeight();
//            oss.setWidth(width);
//            oss.setHeight(height);
//
//            pictureDao.save(oss);
//            return oss;
//        } catch (Exception e) {
//            LoggerUtil.error("", e);
//            throw new IOException(e);
//        }
//    }

//    public List<ImageVo> queryAllPicture() {
//        List<ImageVo> pics = new ArrayList<>();
//        PictureMapper mapper = PictureMapper.INSTANCE;
//        pictureDao.findAll().forEach(e -> {
//                    ImageVo vo = mapper.ossRecord2ImageVo(e);
//                    vo.setUrl(ossService.fullImagePath(vo.getUrl()));
//                    pics.add(vo);
//                }
//        );
//        return pics;
//    }

}

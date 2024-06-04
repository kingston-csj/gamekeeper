package jforgame.admin.mapstruct;

import jforgame.admin.file.io.ImageVo;
import jforgame.admin.file.domain.T_Picture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PictureMapper {

    PictureMapper INSTANCE = Mappers.getMapper(PictureMapper.class);

    @Mapping(target = "time", ignore = true)
    ImageVo ossRecord2ImageVo(T_Picture var);

}

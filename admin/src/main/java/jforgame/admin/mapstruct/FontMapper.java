package jforgame.admin.mapstruct;

import jforgame.admin.file.io.FontVo;
import jforgame.admin.file.domain.T_Font;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FontMapper {

    FontMapper INSTANCE = Mappers.getMapper(FontMapper.class);

    @Mapping(target = "desc", source = "description")
    FontVo entity2Vo(T_Font var);

}

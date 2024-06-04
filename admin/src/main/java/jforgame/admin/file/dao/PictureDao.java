package jforgame.admin.file.dao;

import jforgame.admin.file.domain.T_Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureDao extends JpaRepository<T_Picture, Integer> {
}

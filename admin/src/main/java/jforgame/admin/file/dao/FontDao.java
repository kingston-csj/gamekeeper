package jforgame.admin.file.dao;

import jforgame.admin.file.domain.T_Font;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FontDao extends JpaRepository<T_Font, Integer> {
}

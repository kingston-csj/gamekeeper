package jforgame.admin.file;

import jforgame.admin.domain.ServerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OssRecordDao extends JpaRepository<OssRecord, Integer> {
}

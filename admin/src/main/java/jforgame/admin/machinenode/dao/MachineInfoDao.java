package jforgame.admin.machinenode.dao;

import jforgame.admin.domain.MachineInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineInfoDao extends JpaRepository<MachineInfo, Integer> {

    MachineInfo findMachineInfoByIp(String ip);

}

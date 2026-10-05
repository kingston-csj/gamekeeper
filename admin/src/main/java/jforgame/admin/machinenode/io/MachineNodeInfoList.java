package jforgame.admin.machinenode.io;

import lombok.Data;

import java.util.List;

@Data
public class MachineNodeInfoList {
	
	private List<MachineNodeInfo> servers;
	
	private int totalCount;

}

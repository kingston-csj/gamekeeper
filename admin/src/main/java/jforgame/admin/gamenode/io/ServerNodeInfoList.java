package jforgame.admin.gamenode.io;

import lombok.Data;

import java.util.List;

@Data
public class ServerNodeInfoList {
	
	private List<ServerNodeInfo> servers;
	
	private int totalCount;

}

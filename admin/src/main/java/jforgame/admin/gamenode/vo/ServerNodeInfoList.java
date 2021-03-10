package jforgame.admin.gamenode.vo;

import java.util.List;

public class ServerNodeInfoList {
	
	private List<ServerNodeInfo> servers;
	
	private int totalCount;

	public List<ServerNodeInfo> getServers() {
		return servers;
	}

	public void setServers(List<ServerNodeInfo> servers) {
		this.servers = servers;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}

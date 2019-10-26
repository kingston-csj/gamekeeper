package com.kingston.jforgame.admin.gamenode.vo;

import java.util.List;

import com.kingston.jforgame.admin.domain.ServerInfo;

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

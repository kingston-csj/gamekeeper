package com.kingston.jforgame.admin.gamenode.vo;

import java.util.List;

import com.kingston.jforgame.admin.domain.ServerInfo;

public class ServerList {
	
	private List<ServerInfo> servers;
	
	private int totalCount;

	public List<ServerInfo> getServers() {
		return servers;
	}

	public void setServers(List<ServerInfo> servers) {
		this.servers = servers;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}

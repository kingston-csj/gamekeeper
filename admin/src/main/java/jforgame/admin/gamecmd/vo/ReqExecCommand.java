package jforgame.admin.gamecmd.vo;

public class ReqExecCommand {

    private String selectedServers;

    private int type;

    private String params;

    public String getSelectedServers() {
        return selectedServers;
    }

    public void setSelectedServers(String selectedServers) {
        this.selectedServers = selectedServers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}

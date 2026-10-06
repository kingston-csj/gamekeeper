package jforgame.admin.gamelog.io;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AgentResponse {

    private boolean ok;
    private List<Map<String, Object>> data;
}

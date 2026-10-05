package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="t_machine")
@Data
@NoArgsConstructor
public class MachineInfo {

    @Id
    private Integer id;
    @Column(name="name")
    private String name;
    @Column
    private String ip;
    @Column
    private String agentUrl;
    @Column
    private String logRoot;

}

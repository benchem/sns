package team.benchem.webapi.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "T_MICROSERVICE")
public class MicroServiceInfo {
    @Id
    @Column(name="FROWID")
    private String id;
    @Column(name="FSERVICENAME")
    private String serviceName;
    @Column(name="FRASPUBKEY")
    private String ras_pubKey;
    @Column(name="FRASPRIKEY")
    private String ras_priKey;
    @Column(name="FDESC")
    private String desc;

    public MicroServiceInfo() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRas_pubKey() {
        return ras_pubKey;
    }

    public void setRas_pubKey(String ras_pubKey) {
        this.ras_pubKey = ras_pubKey;
    }

    public String getRas_priKey() {
        return ras_priKey;
    }

    public void setRas_priKey(String ras_priKey) {
        this.ras_priKey = ras_priKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

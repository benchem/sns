package team.benchem.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "T_MICROSERVICE_INSTACE")
public class MicroServiceInstaceInfo {
    @Id
    @Column(name = "FROWID")
    private String id;
    @Column(name = "FDESC", length = 2048)
    private String desc;
    @Column(name = "FSERVICEKEY", length = 2014)
    private String serviceKey;
    @Column(name = "FURL", length = 2048)
    private String url;
    @Column(name = "FTIMEOUT")
    private int timeout;
    @Column(name = "FWEIGHT")
    private int weight;

    public MicroServiceInstaceInfo() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

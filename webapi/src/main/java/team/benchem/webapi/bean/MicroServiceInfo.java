package team.benchem.webapi.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity(name = "T_MICROSERVICE")
public class MicroServiceInfo {
    @Id
    @Column(name = "FROWID")
    private String id;
    @Column(name = "FSERVICENAME")
    private String serviceName;
    @Column(name = "FRSAPUBKEY", length = 2048)
    private String rsa_pubKey;
    @Column(name = "FRSAPRIKEY", length = 2048)
    private String rsa_priKey;
    @Column(name = "FDESC")
    private String desc;

    public MicroServiceInfo() {
        id = UUID.randomUUID().toString();
    }

    public MicroServiceInfo(String serviceName,String rsaPrivateKey,String rsaPublicKey,String desc){
        this.id = UUID.randomUUID().toString();
        this.serviceName= serviceName;
        this.rsa_priKey = rsaPrivateKey;
        this.rsa_priKey = rsaPublicKey;
        this.desc=desc;
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

    public String getRsa_pubKey() {
        return rsa_pubKey;
    }

    public void setRsa_pubKey(String rsa_pubKey) {
        this.rsa_pubKey = rsa_pubKey;
    }

    public String getRsa_priKey() {
        return rsa_priKey;
    }

    public void setRsa_priKey(String rsa_priKey) {
        this.rsa_priKey = rsa_priKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

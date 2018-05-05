package team.benchem.webapi.entity;

import team.benchem.webapi.utils.RSAUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
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
    @Column(name = "FACCESSTYPE")
    private int accessType = 0;

    public MicroServiceInfo() {

        id = UUID.randomUUID().toString();

        try {
            Map<String, String> rasKeys = RSAUtils.createKeys();
            setRsa_priKey(rasKeys.get(RSAUtils.PRIVATE_NAME));
            setRsa_pubKey(rasKeys.get(RSAUtils.PUBLICKEY_NAME));
        } catch (NoSuchAlgorithmException e) {
            //ignore
        }

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

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }
}

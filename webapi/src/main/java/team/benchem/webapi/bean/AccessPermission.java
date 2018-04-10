package team.benchem.webapi.bean;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "T_MICROSERVICE_ACCESSPERMISSION")
public class AccessPermission {

    @Id
    @Column(name = "FROWID")
    private String id;

    @Column(name = "FSERVICEKEY")
    private String servceKey;

    @Column(name = "FACCESSTYPE")
    private int accessType = 0;

    @Column(name = "FCALLERKEY")
    private String callerKey;


    public AccessPermission() {
        id = UUID.randomUUID().toString();
    }

    public AccessPermission(String servceKey, int accessType, String callerKey) {
        this.id = UUID.randomUUID().toString();
        this.servceKey = servceKey;
        this.accessType = accessType;
        this.callerKey = callerKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServceKey() {
        return servceKey;
    }

    public void setServceKey(String servceKey) {
        this.servceKey = servceKey;
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public String getCallerKey() {
        return callerKey;
    }

    public void setCallerKey(String callerKey) {
        this.callerKey = callerKey;
    }
}

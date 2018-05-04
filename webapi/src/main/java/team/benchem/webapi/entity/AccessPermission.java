package team.benchem.webapi.entity;

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


    @Column(name = "FCALLERKEY")
    private String callerKey;


    public AccessPermission() {
        id = UUID.randomUUID().toString();
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

    public String getCallerKey() {
        return callerKey;
    }

    public void setCallerKey(String callerKey) {
        this.callerKey = callerKey;
    }
}

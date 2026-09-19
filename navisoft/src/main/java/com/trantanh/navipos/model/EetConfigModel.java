package com.trantanh.navipos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Tran Tuan Anh
 */

@Entity
@Table(name = "eet")
public class EetConfigModel implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dic;
    private String password;
    private String path;
    private String provoz;
    private String pokl;
    private String validity;
    @Column(name = "created")
    private java.sql.Timestamp created;
    private String space;
    @Column(name = "id_provoz")
    private String idprovoz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProvoz() {
        return provoz;
    }

    public void setProvoz(String provoz) {
        this.provoz = provoz;
    }

    public String getPokl() {
        return pokl;
    }

    public void setPokl(String pokl) {
        this.pokl = pokl;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getIdprovoz() {
        return idprovoz;
    }

    public void setIdprovoz(String idprovoz) {
        this.idprovoz = idprovoz;
    }
}

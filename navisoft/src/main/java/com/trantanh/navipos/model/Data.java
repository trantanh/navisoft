package com.trantanh.navipos.model;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
@Entity
@Table(name = "data")
public class Data {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String pathFile;
    private String pathMySQl;

    public Data(String pathFile, String  pathMySQl) {
        this.pathFile = pathFile;
        this.pathMySQl = pathMySQl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPathFile() {
        return pathFile;
    }

    public String getPathMySQl() {
        return pathMySQl;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public void setPathMySQl(String pathMySQl) {
        this.pathMySQl = pathMySQl;
    }
}

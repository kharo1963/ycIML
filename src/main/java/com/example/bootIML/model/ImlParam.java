package com.example.bootIML.model;

import jakarta.persistence.*;

@Entity
@Table(name = "iml_param")
public class ImlParam {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prog_name")
    private String progName;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_val")
    private Integer paramVal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getParamVal() {
        return paramVal;
    }

    public void setParamVal(Integer paramVal) {
        this.paramVal = paramVal;
    }
}

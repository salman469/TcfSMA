package com.tcf.sma.Survey.model;

public class District {

    public final String id;
    public final String name;
    public final String status;

    public District(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;

        if (id == null)
            id = "";
        if (name == null)
            name = "";
        if (status == null)
            status = "";
    }

}

package com.tcf.sma.Survey.model;

import java.io.Serializable;

public class OptionField implements Serializable {

    public String OptionId = "";
    public String OptionValue = "";

    public OptionField(String id, String value) {
        this.OptionId = id;
        this.OptionValue = value;
    }

}

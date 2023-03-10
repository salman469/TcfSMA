package com.tcf.sma.Models.RetrofitModels;

public class AppModulesModel {
    private int Module_ID;
    private String Module_Name;
    private String minVersion;
    private String Created_On;

    public int getModule_ID() {
        return Module_ID;
    }

    public void setModule_ID(int module_ID) {
        Module_ID = module_ID;
    }

    public String getModule_Name() {
        return Module_Name;
    }

    public void setModule_Name(String module_Name) {
        Module_Name = module_Name;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getCreated_On() {
        return Created_On;
    }

    public void setCreated_On(String created_On) {
        Created_On = created_On;
    }
}

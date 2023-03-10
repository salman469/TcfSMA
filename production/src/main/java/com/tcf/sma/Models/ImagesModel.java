package com.tcf.sma.Models;

/**
 * Created by Zubair Soomro on 12/19/2016.
 */

public class ImagesModel {
    String ImageName;
    String isSynced;
    String Id;


    public String getImageName() {
        return ImageName;

    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getIsSynced() {
        return isSynced;
    }


    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}

package com.ewo.skripsi.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarouselModel {

    @SerializedName("nama_file")
    @Expose
    private String namaFile;
    @SerializedName("id")
    @Expose
    private String id;

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
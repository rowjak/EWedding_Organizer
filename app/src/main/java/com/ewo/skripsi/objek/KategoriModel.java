package com.ewo.skripsi.objek;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KategoriModel {

    @SerializedName("kategori")
    @Expose
    private String kategori;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

}

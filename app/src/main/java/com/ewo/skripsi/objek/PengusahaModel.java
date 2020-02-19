package com.ewo.skripsi.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PengusahaModel {

    @SerializedName("id_usaha")
    @Expose
    public String idUsaha;
    @SerializedName("nama_usaha")
    @Expose
    private String namaUsaha;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("no_wa")
    @Expose
    private String noWa;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("jarak")
    @Expose
    private String jarak;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("post_kategori")
    @Expose
    private String post_kategori;

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getPost_kategori() {
        return post_kategori;
    }

    public void setPost_kategori(String post_kategori) {
        this.post_kategori = post_kategori;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(String idUsaha) {
        this.idUsaha = idUsaha;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoWa() {
        return noWa;
    }

    public void setNoWa(String noWa) {
        this.noWa = noWa;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public PengusahaModel(String idUsaha){
        this.idUsaha =idUsaha;
    }


}

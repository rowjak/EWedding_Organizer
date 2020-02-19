package com.ewo.skripsi.api;


import com.ewo.skripsi.objek.PengusahaModel;
import com.google.gson.JsonArray;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

//    @FormUrlEncoded
//    @POST("api.php")
//    Call<List<PengusahaModel>> getUsaha(
//            @Field("index") int index,
//            @Field("kategori") String kategori,
//            @Field("lat") String lat,
//            @Field("lng") String lng);

    @FormUrlEncoded
    @POST("pengusaha/api_post")
    Call<List<PengusahaModel>> getUsaha(
            @Field("kategori") String kategori,
            @Field("lat") String lat,
            @Field("lng") String lng);

    @FormUrlEncoded
    @POST("pengusaha/api")
    Call<List<PengusahaModel>> getPengusaha(
            @Field("kategori") String kategori,
            @Field("lat") String lat,
            @Field("lng") String lng);

    @FormUrlEncoded
    @POST("pengusaha/detail_usaha")
    Call<List<PengusahaModel>> getPost(
            @Field("id_usaha") String id_usaha);

    @FormUrlEncoded
    @POST("pengusaha/foto")
    Call<JsonArray> getFoto(
            @Field("id_usaha") String id_usaha);

    @FormUrlEncoded
    @POST("pengusaha/cari")
    Call<List<PengusahaModel>> search_usaha(
            @Field("key") String key,
            @Field("lat") Double lat,
            @Field("lng") Double lng);

    @GET("pengusaha/kategori")
    Call<JsonArray> loadKategori();

    @FormUrlEncoded
    @POST("pengusaha/android_usulan")
    Call<ResponseBody> simpan_usulan(
            @Field("id_usaha") String id_usaha,
            @Field("pengusul") String pengusul,
            @Field("nama_usaha") String nama_usaha,
            @Field("alamat") String alamat,
            @Field("no_wa") String no_wa,
            @Field("deskripsi") String deskripsi,
            @Field("kategori") String kategori,
            @Field("latlng") String latlng,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("pengusaha/simpan_post")
    Call<ResponseBody> simpan_post(
            @Field("id_usaha") String id_usaha,
//            @Field("kategori") String kategori,
            @Field("keterangan") String keterangan,
            @Field("foto") String foto
    );

    @Multipart
    @POST("pengusaha/upload")
    Call<UploadResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name,
                                    @Query("nama_file") String nama_file,
                                    @Query("id_usaha") String id_usaha
    );

    @FormUrlEncoded
    @POST("pengusaha/login")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("pengusaha/detail_pengusaha")
    Call<ResponseBody> detail_pengusaha(
            @Field("id_usaha") String id_usaha,
            @Field("lat") Double lat,
            @Field("lng") Double lng
    );

}

package com.ewo.skripsi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.dd.processbutton.FlatButton;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.ewo.skripsi.R;
import com.ewo.skripsi.api.Client;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.api.UploadConfig;
import com.ewo.skripsi.api.UploadResponse;
import com.ewo.skripsi.objek.KategoriModel;
import com.ewo.skripsi.utilities.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class PostActivity extends AppCompatActivity {

    MaterialDialog materialDialog; //material dialog
    int PLACE_PICKER_REQUEST = 1; //place pick result request
//    private List<com.ragshion.ciamik.objek.Type> list_type = new ArrayList<>(); //list lokasi kecamatan
    ArrayList<String> array_type=new ArrayList<>(); //array tempat naruh data kecamatan

    ImageView iv_foto;
    int status_image = 0;
    String string_foto;
    List<Image> images;

    FlatButton btn_simpan;
    AREditText tv_deskripsi;
    private IARE_Toolbar mToolbar;

//    private List<KategoriModel> list_kategori = new ArrayList<>();
//    ArrayList<String> array_kategori=new ArrayList<>();
//    SpinnerDialog spinnerKategori;

    SharedPrefManager sharedPrefManager;

//    MaterialEditText tv_kategori;
    String kategori;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Post");

        sharedPrefManager = new SharedPrefManager(this);

        iv_foto = findViewById(R.id.iv_foto);
        iv_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickerDiri();
            }
        });
//        load_kategori();
//        tv_kategori = findViewById(R.id.tv_kategori);
//        tv_kategori.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                spinnerKategori.showSpinerDialog();
//            }
//        });

        tv_deskripsi = findViewById(R.id.arEditText);
        initToolbar();

        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (
                        tv_deskripsi.getText().toString().equals("") |
                        status_image == 0
                ){
                    materialDialog = new MaterialDialog.Builder(PostActivity.this)
                            .title("Peringatan!")
                            .content("Harap isi semua field terlebih dahulu, Terima Kasih.")
                            .positiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    materialDialog.dismiss();
                                }
                            })
                            .show();
                }else{
                    materialDialog = new MaterialDialog.Builder(view.getContext())
                            .content("Sedang Menyimpan...")
                            .progress(true, 0)
                            .cancelable(false)
                            .progressIndeterminateStyle(true)
                            .show();

                    if (status_image == 0){
                        string_foto = "0";
                    }else{
                        iv_foto.buildDrawingCache();
                        Bitmap bitmap = iv_foto.getDrawingCache();

                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        byte[] image=stream.toByteArray();
                        System.out.println("byte array:"+image);
                        string_foto = Base64.encodeToString(image, 0);
                    }

                    simpan_post();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Bungee.swipeRight(this);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.swipeRight(this);
    }

    void initToolbar(){
        mToolbar = findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem fontColor = new ARE_ToolItem_FontColor();
        IARE_ToolItem backgroundColor = new ARE_ToolItem_BackgroundColor();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(fontColor);
        mToolbar.addToolbarItem(backgroundColor);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);

        tv_deskripsi.setToolbar(mToolbar);
    }

    void imagePickerDiri(){
        ImagePicker.create(this)
                .language("in") // Set image picker language
                .single()
                .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .toolbarArrowColor(Color.RED) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap untuk Memilih") // image selection title
                .toolbarDoneButtonText("SELESAI") // done button text
                .start();
    }

    void simpan_post(){
        Service service = Client.getClient();
        Call<ResponseBody> simpan_usulan = service.simpan_post(
                sharedPrefManager.getSPID(),
//                tv_kategori.getText().toString(),
                Html.toHtml(tv_deskripsi.getText()),
                string_foto);
        simpan_usulan.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                materialDialog.dismiss();
                try{
                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
                    Toasty.success(PostActivity.this, jsonRESULTS.getString("respon")).show();
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }catch (Exception e){
                    Toasty.error(PostActivity.this, e.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toasty.error(PostActivity.this, t.getMessage()).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            status_image = 1;
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

//    public void load_kategori(){
//        Service serviceAPI = Client.getClient();
//        Call<JsonArray> loadLokasi = serviceAPI.loadKategori();
//
//        loadLokasi.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                String kecamatanString = response.body().toString();
//
//                Type listType = new TypeToken<List<KategoriModel>>() {}.getType();
//                list_kategori = getTeamListFromJson(kecamatanString, listType);
//
//                for (int i=0; i<list_kategori.size(); i++){
//                    array_kategori.add(list_kategori.get(i).getKategori());
//                }
//
//                spinnerKategori = new SpinnerDialog(PostActivity.this,array_kategori,"Kategori",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
//
//                spinnerKategori.setCancellable(true);
//                spinnerKategori.setShowKeyboard(false);
//
//                spinnerKategori.bindOnSpinerListener(new OnSpinerItemClick() {
//                    @Override
//                    public void onClick(String item, int position) {
//                        tv_kategori.setText(item);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Toast.makeText(PostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath());
        }

        File imgFile = new File(stringBuffer.toString());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_foto.setImageBitmap(myBitmap);
        }else{
            Toast.makeText(this, imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }
}

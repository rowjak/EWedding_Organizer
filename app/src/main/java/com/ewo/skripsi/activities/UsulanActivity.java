package com.ewo.skripsi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
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
import com.ewo.skripsi.utilities.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

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

public class UsulanActivity extends AppCompatActivity {

    MaterialDialog materialDialog; //material dialog
    int PLACE_PICKER_REQUEST = 1; //place pick result request
//    private List<com.ragshion.ciamik.objek.Type> list_type = new ArrayList<>(); //list lokasi kecamatan
    ArrayList<String> array_type=new ArrayList<>(); //array tempat naruh data kecamatan

    ImageView iv_foto;
    TextView clear_foto;
    MaterialEditText tv_nama_usaha, tv_alamat, tv_no_wa, tv_lokasi, tv_username, tv_password;
    int status_image = 0;
    String username;
    List<Image> images;
    LinearLayout layout_type;

    FlatButton btn_simpan;
    AREditText tv_deskripsi;
    private IARE_Toolbar mToolbar;
    String uniqid;

    LinearLayout linearLayout;
    String tipe_usaha = "";
    CheckBox tp_wo, tp_rp, tp_ct, tp_gp,tp_dk,tp_hb,tp_dekor;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usulan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pendaftaran Data Pengusaha");

        sharedPrefManager = new SharedPrefManager(this);

        layout_type = findViewById(R.id.layout_checkbox);

        iv_foto = findViewById(R.id.iv_foto);
        iv_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImagePicker();
            }
        });

        linearLayout = findViewById(R.id.tempat_foto);
        linearLayout.setVisibility(View.GONE);
        clear_foto = findViewById(R.id.clear_foto);
        clear_foto.setVisibility(View.GONE);
        clear_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_foto.setVisibility(View.VISIBLE);
                clear_foto.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                status_image = 0;
                linearLayout.removeAllViews();
            }
        });

        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nama_usaha = findViewById(R.id.tv_nama_usaha);
        tv_no_wa = findViewById(R.id.tv_no_wa);
        tv_lokasi = findViewById(R.id.tv_lokasi);
        tv_username = findViewById(R.id.tv_username);
        tv_password = findViewById(R.id.tv_password);
        tv_lokasi.setFocusable(false);
        tv_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlacePicker();
            }
        });

        tp_wo = findViewById(R.id.tp_wo);
        tp_rp = findViewById(R.id.tp_rp);
        tp_ct = findViewById(R.id.tp_ct);
        tp_gp = findViewById(R.id.tp_gp);
        tp_dk = findViewById(R.id.tp_dk);
        tp_hb = findViewById(R.id.tp_hb);
        tp_dekor = findViewById(R.id.tp_dekor);
        tv_deskripsi = findViewById(R.id.arEditText);
        initToolbar();

        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tp_wo.isChecked())
                    tipe_usaha = tipe_usaha+tp_wo.getText()+";";
                if (tp_rp.isChecked())
                    tipe_usaha = tipe_usaha+tp_rp.getText()+";";
                if (tp_ct.isChecked())
                    tipe_usaha = tipe_usaha+tp_ct.getText()+";";
                if (tp_gp.isChecked())
                    tipe_usaha = tipe_usaha+tp_gp.getText()+";";
                if (tp_dk.isChecked())
                    tipe_usaha = tipe_usaha+tp_dk.getText()+";";
                if (tp_hb.isChecked())
                    tipe_usaha = tipe_usaha+tp_hb.getText()+";";
                if (tp_dekor.isChecked())
                    tipe_usaha = tipe_usaha+tp_dekor.getText()+";";

                if (
                        tv_nama_usaha.getText().toString().equals("") |
                        tv_alamat.getText().toString().equals("") |
                        tv_no_wa.getText().toString().equals("") |
                        tv_lokasi.getText().toString().equals("") |
                        tipe_usaha.equals("") |
                        tv_deskripsi.getText().toString().equals("") |
                        status_image == 0
                ){
                    materialDialog = new MaterialDialog.Builder(UsulanActivity.this)
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
                    uploadFile(images);
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

    void loadImagePicker(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .includeVideo(true) // Show video on image picker
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") .enableLog(false)
                .start(); // start image picker activity with request code
    }

    void loadPlacePicker(){
        startActivityForResult(new Intent(this, LocationPickerActivity.class), PLACE_PICKER_REQUEST);
    }

    void simpan_perumahan(){

        Service service = Client.getClient();
        Call<ResponseBody> simpan_usulan = service.simpan_usulan(
                uniqid,
                sharedPrefManager.getSpUser(),
                tv_nama_usaha.getText().toString(),
                tv_alamat.getText().toString(),
                tv_no_wa.getText().toString(),
                Html.toHtml(tv_deskripsi.getText()),
                tipe_usaha,
                tv_lokasi.getText().toString(),
                tv_username.getText().toString(),
                tv_password.getText().toString());
        simpan_usulan.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
                    Toasty.success(UsulanActivity.this, jsonRESULTS.getString("respon")).show();
                    finish();
                    Bungee.fade(UsulanActivity.this);
                }catch (Exception e){
//                    Toasty.error(SavePerumahanActivity.this, e.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(UsulanActivity.this, t.getMessage()).show();
            }
        });
    }

    private void uploadFile(List<Image> foto) {
        materialDialog = new MaterialDialog.Builder(this)
                .content("Sedang Menyimpan...")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();

        uniqid = UUID.randomUUID().toString();

        for (int i = 0; i < foto.size(); i++) {
            File file = new File(foto.get(i).getPath());

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            Service getResponse = UploadConfig.getRetrofit().create(Service.class);
            Call<UploadResponse> call = getResponse.uploadFile(fileToUpload, filename, uniqid+"-"+(i+1), uniqid);
            call.enqueue(new Callback<UploadResponse>() {

                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    UploadResponse serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {
                            simpan_perumahan();
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        assert serverResponse != null;
                        Log.v("Response", serverResponse.toString());
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    materialDialog.dismiss();
                    Toast.makeText(UsulanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            // Get a list of picked images
            images = ImagePicker.getImages(data);

//            StringBuilder stringBuffer = new StringBuilder();
//            for (int i = 0; i < images.size(); i++) {
//                stringBuffer.append(images.get(i).getPath()+"\n");
//            }

//            lokasi_file.setText("Lokasi File : \n"+stringBuffer.toString());
            iv_foto.setVisibility(View.GONE);
            clear_foto.setVisibility(View.VISIBLE);
            status_image = 1;

            create_view();

            // or get a single image only
            // Image image = ImagePicker.getFirstImageOrNull(data);

//            uploadFile(images);

        }

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            tv_lokasi.setText(data.getStringExtra("latitude")+","+data.getStringExtra("longitude"));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void create_view(){
        linearLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < images.size(); i++) {
            CardView card = new CardView(this);

            LayoutParams params = new LayoutParams(
                    400,
                    300
//                    LayoutParams.WRAP_CONTENT
            );

            card.setLayoutParams(params);
            card.setRadius(10);
            card.setMaxCardElevation(15);
            card.setCardElevation(9);


            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
            cardViewMarginParams.setMargins(30, 30, 30, 30);
            card.requestLayout();  //Dont forget this line

            File file = new File(images.get(i).getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bitmap);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            card.addView(iv);
            linearLayout.addView(card);
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

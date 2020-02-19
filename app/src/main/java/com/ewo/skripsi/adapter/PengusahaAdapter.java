package com.ewo.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ewo.skripsi.R;
import com.ewo.skripsi.activities.DetailActivity;
import com.ewo.skripsi.objek.PengusahaModel;

import java.util.List;


public class PengusahaAdapter extends RecyclerView.Adapter<PengusahaAdapter.MyViewHolder> {

    private List<PengusahaModel> contacts;
    private Context context;

    public PengusahaAdapter(List<PengusahaModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_pengusaha, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.id_usaha = contacts.get(position).getIdUsaha();
        holder.tv_nama_usaha.setText(contacts.get(position).getNamaUsaha());
        holder.tv_jarak.setText(contacts.get(position).getJarak());
//        holder.tv_keterangan.setText(Html.fromHtml(contacts.get(position).getKeterangan()));
//        holder.tv_kategori.setText(contacts.get(position).getPost_kategori());

//        byte[] decodedString = Base64.decode(contacts.get(position).getFoto(), Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        holder.iv_foto.setImageBitmap(decodedByte);

        String url = holder.itemView.getResources().getString(R.string.pathfoto)+contacts.get(position).getFoto();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.loading_elips).centerCrop())
                .into(holder.iv_foto);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_usaha, tv_jarak, tv_keterangan,tv_kategori;
        ImageView iv_foto;
        String id_usaha;

        public MyViewHolder(View view) {
            super(view);

            tv_nama_usaha = view.findViewById(R.id.tv_nama_usaha);
//            tv_keterangan = view.findViewById(R.id.tv_keterangan);
            tv_jarak= view.findViewById(R.id.tv_jarak);
//            tv_kategori= view.findViewById(R.id.tv_kategori);
            iv_foto = view.findViewById(R.id.iv_foto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("id_usaha",id_usaha);
                    intent.putExtra("detail","1");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

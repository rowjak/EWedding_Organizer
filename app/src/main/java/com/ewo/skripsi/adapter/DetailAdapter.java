package com.ewo.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
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


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    private List<PengusahaModel> contacts;
    private Context context;

    public DetailAdapter(List<PengusahaModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_keterangan.setText(Html.fromHtml(contacts.get(position).getKeterangan()));
//        holder.tv_kategori.setText(contacts.get(position).getPost_kategori());

        byte[] decodedString = Base64.decode(contacts.get(position).getFoto(), Base64.DEFAULT);

//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        holder.iv_foto.setImageBitmap(decodedByte);

//        String url = holder.itemView.getResources().getString(R.string.pathfoto)+contacts.get(position).getFoto();
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(decodedString)
                .apply(new RequestOptions().placeholder(R.drawable.loading_elips).fitCenter())
                .into(holder.iv_foto);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView tv_keterangan,tv_kategori;
        TextView tv_keterangan;
        ImageView iv_foto;

        public MyViewHolder(View view) {
            super(view);

            tv_keterangan = view.findViewById(R.id.tv_keterangan);
//            tv_kategori= view.findViewById(R.id.tv_kategori);
            iv_foto = view.findViewById(R.id.iv_foto);
        }
    }
}

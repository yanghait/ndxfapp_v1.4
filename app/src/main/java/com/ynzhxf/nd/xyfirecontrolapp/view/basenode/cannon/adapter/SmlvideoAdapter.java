package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;

public class SmlvideoAdapter extends RecyclerView.Adapter {

    private String[] videoPath = new String[]{
            "http://hls18.homca.com:9999/3GB0024441MCR5B_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810",
            "http://39.130.146.210:9999/3GB0024441QMQVR_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810",
            "http://39.130.146.210:9999/3GB0024441XXS2P_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810",
            "http://39.130.146.211:9999/3GB002444187S2H_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810",
            "http://39.130.146.210:9999/3GB0024441Y98GK_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810",
            "http://39.130.146.210:9999/3GB0024441KTS6M_0.m3u8?key=0edb937d1aeac50dd9f4162f2727d810"};

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sml_item_video_list, parent, false);
        SmlVideoViewHolder smlVideoViewHolder = new SmlVideoViewHolder(view);
        return smlVideoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SmlVideoViewHolder viewHolder = (SmlVideoViewHolder) holder;
        viewHolder.txtName.setText("视频监控" + (position + 1));
        viewHolder.contentView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(videoPath[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoPath.length;
    }

    public class SmlVideoViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView txtName;

        public SmlVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.container);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String videoPath);
    }
}

package com.ynzhxf.nd.xyfirecontrolapp.adapter.share;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileUpdateProgressBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FileUpdateProgressAdapter extends RecyclerView.Adapter<FileUpdateProgressAdapter.ViewHolder> {


    private List<FileUpdateProgressAdapter.ViewHolder> listHolder = new ArrayList<>();

    private FileShareMyFileAdapter.OnItemClickListener mOnItemClickListener;

    private OnViewHolderClickListener onHolderListener;

    private List<FileUpdateProgressBean> beanList;

    private Context context;

    private boolean isPaused = true;

    private LinkedHashMap<Integer, Boolean> pausedMap = new LinkedHashMap<>();

    public FileUpdateProgressAdapter(Context context, List<FileUpdateProgressBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public FileUpdateProgressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.item_file_update_progress, parent, false);
        FileUpdateProgressAdapter.ViewHolder holder = new FileUpdateProgressAdapter.ViewHolder(view);
        listHolder.add(holder);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(FileUpdateProgressAdapter.ViewHolder holder, final int position) {

        int okWidth = ScreenUtil.dp2px(context, 200f);

        boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), beanList.get(position).getFileName());

        String successName = SPUtils.getInstance().getString(beanList.get(position).getUrl());
        if (!StringUtils.isEmpty(successName)) {
            String fileName = successName.substring(successName.lastIndexOf("/") + 1);
//            if (!StringUtils.isEmpty(fileName) && fileName.length() >= 8) {
//                fileName = fileName.substring(0, 8) + "...";
//            }
            holder.fileName.setText(fileName);

            CharSequence s1 = TextUtils.ellipsize(holder.fileName.getText(), holder.fileName.getPaint(), okWidth, TextUtils.TruncateAt.END);

            holder.fileName.setText(s1);
        } else {

            holder.fileName.setText(beanList.get(position).getFileName());

            CharSequence s1 = TextUtils.ellipsize(holder.fileName.getText(), holder.fileName.getPaint(), okWidth, TextUtils.TruncateAt.END);

            holder.fileName.setText(s1);
//            if (beanList.get(position).getFileName().length() >= 8) {
//                holder.fileName.setText(String.valueOf(beanList.get(position).getFileName().substring(0, 8) + "..."));
//            } else {
//                holder.fileName.setText(beanList.get(position).getFileName());
//            }
        }

        // 处理文件名


        // 测量TextView 的实际显示宽度 超过指定宽度后打点
        CharSequence s1 = TextUtils.ellipsize(holder.fileName.getText(), holder.fileName.getPaint(), okWidth, TextUtils.TruncateAt.END);
        //CharSequence s2 = TextUtils.ellipsize(holder.fileType.getText(), holder.fileType.getPaint(), okWidth, TextUtils.TruncateAt.END);



        holder.fileLength.setText(String.valueOf(beanList.get(position).getFileLength() + "KB"));

        if (exists) {
            holder.down_ok.setVisibility(View.VISIBLE);
            holder.selectState.setVisibility(View.GONE);
            holder.progressBar.setProgress(100);
            LogUtils.showLoge("文件是否存在1212---", "121212");
        } else {
            holder.selectState.setImageDrawable(context.getResources().getDrawable(R.mipmap.file_update_pause));
        }
        if (beanList.get(position).getStatus() == -2) {
            pausedMap.put(position, true);
            holder.selectState.setImageDrawable(context.getResources().getDrawable(R.mipmap.file_update_pause));
            holder.progressBar.setProgress(beanList.get(position).getProgress());
        } else if (beanList.get(position).getStatus() == 3) {
            pausedMap.put(position, false);
            holder.selectState.setImageDrawable(context.getResources().getDrawable(R.mipmap.file_downing));
            holder.progressBar.setProgress(beanList.get(position).getProgress());
        } else if (beanList.get(position).getStatus() == -3) {
            pausedMap.put(position, false);
            holder.progressBar.setProgress(100);
            holder.progressBar.setMax(100);
            holder.selectState.setVisibility(View.GONE);
            holder.down_ok.setVisibility(View.VISIBLE);
        } else if (beanList.get(position).getStatus() == -4 || beanList.get(position).getStatus() == -1) {
            pausedMap.put(position, true);
            holder.selectState.setImageDrawable(context.getResources().getDrawable(R.mipmap.file_update_pause));
        } else {
            pausedMap.put(position, true);
        }

        holder.selectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onHolderListener != null) {
                    onHolderListener.OnClickPaused(position);
                }
            }
        });

        if (onHolderListener != null) {
            onHolderListener.OnInitData(position);
        }
    }

    public LinkedHashMap<Integer, Boolean> getPausedMap() {
        return pausedMap;
    }

    public boolean getPausedState() {
        return isPaused;
    }

    public void setPausedState(boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton down_ok;

        public NumberProgressBar progressBar;

        public TextView fileName;

        public TextView fileLength;

        public ImageButton selectState;


        public ViewHolder(View itemView) {

            super(itemView);

            fileName = itemView.findViewById(R.id.item_file_share_file_name);

            progressBar = itemView.findViewById(R.id.item_file_share_upload);

            selectState = itemView.findViewById(R.id.item_file_down_btn);

            fileLength = itemView.findViewById(R.id.item_file_length);

            down_ok = itemView.findViewById(R.id.item_file_down_ok);

        }
    }

    public void setHolderClickListener(OnViewHolderClickListener onHolderListener) {
        this.onHolderListener = onHolderListener;
    }

    public interface OnViewHolderClickListener {
        void OnInitData(int position);

        void OnClickPaused(int position);
    }

    public List<FileUpdateProgressAdapter.ViewHolder> getListHolder() {
        return listHolder;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(FileShareMyFileAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected void setListener(final ViewGroup parent, final FileUpdateProgressAdapter.ViewHolder viewHolder, int viewType) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }
}

package com.ynzhxf.nd.xyfirecontrolapp.adapter.share;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FileShareMyFileAdapter extends RecyclerView.Adapter<FileShareMyFileAdapter.ViewHolder> {

    public List<FileShareMyFileBean> myFileBeanList;

    public List<ViewHolder> listHolder = new ArrayList<>();
    // 记录选中状态
    public static LinkedHashMap<Integer, Boolean> radioState = new LinkedHashMap<>();

    public Context context;

    public OnCheckBoxChangeListener callback;

    public static int radioCount = 0;

    private OnItemClickListener mOnItemClickListener;

    public FileShareMyFileAdapter(List<FileShareMyFileBean> myFileBeanList, Context context, OnCheckBoxChangeListener callback) {
        this.myFileBeanList = myFileBeanList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_share_my_file, parent, false);
        FileShareMyFileAdapter.ViewHolder holder = new FileShareMyFileAdapter.ViewHolder(view);
        listHolder.add(holder);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FileShareMyFileBean myFileBean = myFileBeanList.get(position);

        holder.loading.setViewColor(context.getResources().getColor(R.color.gray));
        holder.loading.setBarColor(context.getResources().getColor(R.color.primary_text_color));


        holder.fileName.setText(myFileBean.getF_Title());
        holder.fileType.setText(myFileBean.getTypeNmae());

        int okWidth = ScreenUtil.dp2px(context, 230f);

        // 测量TextView 的实际显示宽度 超过指定宽度后打点
        CharSequence s1 = TextUtils.ellipsize(holder.fileName.getText(), holder.fileName.getPaint(), okWidth, TextUtils.TruncateAt.END);
        CharSequence s2 = TextUtils.ellipsize(holder.fileType.getText(), holder.fileType.getPaint(), okWidth, TextUtils.TruncateAt.END);

        holder.fileName.setText(s1);
        holder.fileType.setText(s2);

        holder.uploadPerson.setText(myFileBean.getF_UploadDate());
        holder.time.setText(String.valueOf("上传人:" + myFileBean.getUserName()));//

        if (radioState.size() > 0 && radioState.containsKey(position)) {
            holder.checkBox.setChecked(radioState.get(position));
        }

        boolean exists = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());

        if (exists) {
            holder.checkBox.setVisibility(View.GONE);
            holder.down_ok.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.down_ok.setVisibility(View.GONE);
            holder.loading.setVisibility(View.GONE);
        }
        final int count = position;

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (callback != null) {
                    callback.onCheckedChanged(compoundButton, b, count);
                }
            }
        });
        if (callback != null) {
            callback.initAdapter(count);
        }

        String downloadPath = myFileBeanList.get(position).getF_FileUrl();
        String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(position).getF_FileName();
        SPUtils.getInstance().put(URLConstant.URL_BASE1 + downloadPath, newPath);
    }

    public static boolean whetherExistFile(String path, String fileName1) {
        if (StringUtils.isEmpty(fileName1) || StringUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            String filename = f.getName();
            if (filename.endsWith(fileName1)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return myFileBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fileName, fileType, uploadPerson, time;

        public LVCircularRing loading;

        public CheckBox checkBox;

        public ImageButton down_ok;

        public View itemView;

        public ImageButton item_down_btn;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            fileName = view.findViewById(R.id.item_file_share_file_name);
            fileType = view.findViewById(R.id.item_file_share_file_type);
            uploadPerson = view.findViewById(R.id.item_file_share_upload);
            time = view.findViewById(R.id.item_file_share_time);

            loading = view.findViewById(R.id.item_file_LVCircularRing);
            checkBox = view.findViewById(R.id.item_file_share_radio);

            down_ok = view.findViewById(R.id.item_file_down_ok);

            item_down_btn = view.findViewById(R.id.item_file_down_btn);

        }
    }

    protected void setListener(final ViewGroup parent, final FileShareMyFileAdapter.ViewHolder viewHolder, int viewType) {
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

    public List<ViewHolder> getViewHolderList() {
        return listHolder;
    }

    public interface OnCheckBoxChangeListener {
        void initAdapter(int position);

        void onCheckedChanged(CompoundButton compoundButton, boolean checked, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

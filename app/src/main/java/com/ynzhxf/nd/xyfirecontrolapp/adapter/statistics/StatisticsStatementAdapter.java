package com.ynzhxf.nd.xyfirecontrolapp.adapter.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.Statement.StatementItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

import java.util.LinkedHashMap;
import java.util.List;

import static com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter.whetherExistFile;


/**
 * author hbzhou
 * date 2019/3/20 15:17
 */
public class StatisticsStatementAdapter extends RecyclerView.Adapter<StatisticsStatementAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private List<StatementItemListBean> beanList;

    private Context mContext;

    public StatisticsStatementAdapter(Context context, List<StatementItemListBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistics_statement, parent, false);
        ViewHolder holder = new ViewHolder(view);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final StatementItemListBean bean = beanList.get(position);

        holder.fileName.setText(bean.getName());

        holder.loading.setViewColor(mContext.getResources().getColor(R.color.gray));
        holder.loading.setBarColor(mContext.getResources().getColor(R.color.primary_text_color));

        FileDownloader.setup(mContext);

        String fileName;

        if (StringUtils.isEmpty(bean.getFileUrl())) {
            fileName = "";
        } else {
            fileName = bean.getFileUrl().substring(bean.getFileUrl().lastIndexOf("/") + 1);
        }

        boolean exists = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), fileName);
        if (exists) {
            holder.down_ok.setVisibility(View.VISIBLE);
            holder.item_down_btn.setVisibility(View.GONE);
        }

        holder.item_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理下载文件逻辑

                LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
                hashMap.put(holder.getAdapterPosition(), bean.getFileUrl());

                FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                    @Override
                    public void onStart(BaseDownloadTask task) {
                        holder.loading.setVisibility(View.VISIBLE);
                        holder.item_down_btn.setVisibility(View.GONE);
                        holder.down_ok.setVisibility(View.GONE);

                        holder.loading.startAnim(500);
                    }

                    @Override
                    public void onCompleted(BaseDownloadTask task) {
                        holder.down_ok.setVisibility(View.VISIBLE);
                        holder.loading.setVisibility(View.GONE);
                        holder.loading.stopAnim();
                        holder.item_down_btn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(BaseDownloadTask task) {

                        holder.item_down_btn.setVisibility(View.VISIBLE);
                        holder.loading.setVisibility(View.GONE);
                        holder.down_ok.setVisibility(View.GONE);

                        holder.loading.stopAnim();

                        LogUtils.eTag("error12121212----", task.getErrorCause());

                        ToastUtils.showLong("未发现此文件!");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fileName;

        public LVCircularRing loading;

        public ImageButton down_ok;

        public View itemView;

        public ImageButton item_down_btn;

        public ViewHolder(View view) {
            super(view);
            itemView = view;

            fileName = view.findViewById(R.id.item_statistics_state_title);

            loading = view.findViewById(R.id.item_file_LVCircularRing);

            down_ok = view.findViewById(R.id.item_file_down_ok);

            item_down_btn = view.findViewById(R.id.item_file_down_btn);
        }
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
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

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int position);

        boolean onItemLongClick(View view, ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

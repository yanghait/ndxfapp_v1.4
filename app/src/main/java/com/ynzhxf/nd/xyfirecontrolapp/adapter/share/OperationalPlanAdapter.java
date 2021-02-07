package com.ynzhxf.nd.xyfirecontrolapp.adapter.share;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.OperationalPlanFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import static com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareMyFileActivity.renameFile;

/**
 * 作战预案附件列表适配器
 */
public class OperationalPlanAdapter extends FileShareMyFileAdapter {

    private List<OperationalPlanFileBean> beanList;

    public OperationalPlanAdapter(List<FileShareMyFileBean> myFileBeanList, Context context, OnCheckBoxChangeListener callback, List<OperationalPlanFileBean> beanList) {
        super(myFileBeanList, context, callback);
        this.beanList = beanList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final OperationalPlanFileBean bean = beanList.get(position);

        LinearLayout linear = holder.itemView.findViewById(R.id.linear);
        linear.setVisibility(View.INVISIBLE);
        holder.fileType.setVisibility(View.INVISIBLE);

        ImageView img = holder.itemView.findViewById(R.id.item_file_share_img);
        img.setBackground(context.getResources().getDrawable(R.mipmap.attachment_img));
        TextView time = holder.itemView.findViewById(R.id.item_file_share_time);
        time.setVisibility(View.INVISIBLE);

        holder.checkBox.setVisibility(View.GONE);

        holder.loading.setViewColor(context.getResources().getColor(R.color.gray));
        holder.loading.setBarColor(context.getResources().getColor(R.color.primary_text_color));

        holder.fileName.setText(bean.getF_FileName());
        holder.uploadPerson.setText(String.valueOf(bean.getF_FileSize() / 1024 + "KB"));

        FileDownloader.setup(context);
        boolean exists = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), bean.getF_FileName());
        holder.item_down_btn.setVisibility(View.VISIBLE);
        if (exists) {
            holder.checkBox.setVisibility(View.GONE);
            holder.down_ok.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.GONE);
            holder.item_down_btn.setVisibility(View.GONE);
        }

        final int count = position;

        holder.item_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.initAdapter(count);
                }
                // 处理下载文件逻辑
                LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
                hashMap.put(count, bean.getF_Url());
                FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                    @Override
                    public void onStart(BaseDownloadTask task) {
                        holder.loading.setVisibility(View.VISIBLE);
                        holder.item_down_btn.setVisibility(View.GONE);
                        holder.checkBox.setVisibility(View.GONE);
                        holder.down_ok.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCompleted(BaseDownloadTask task) {
                        holder.down_ok.setVisibility(View.VISIBLE);

                        holder.loading.setVisibility(View.GONE);
                        holder.item_down_btn.setVisibility(View.GONE);
                        holder.checkBox.setVisibility(View.GONE);

                        String downPath = bean.getF_Url();
                        String oldPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + downPath.substring(downPath.lastIndexOf("/") + 1);
                        String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + bean.getF_FileName();
                        renameFile(oldPath, newPath);
                    }

                    @Override
                    public void onError(BaseDownloadTask task) {
                        holder.item_down_btn.setVisibility(View.VISIBLE);

                        holder.loading.setVisibility(View.GONE);
                        holder.checkBox.setVisibility(View.GONE);
                        holder.down_ok.setVisibility(View.GONE);
                        HelperView.Toast(context, "未发现此文件!");
                    }
                });
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (callback != null) {
                    callback.onCheckedChanged(compoundButton, b, count);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
}

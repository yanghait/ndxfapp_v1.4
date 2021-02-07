package com.ynzhxf.nd.xyfirecontrolapp.adapter.maintenance;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.UploadPhotoNormalBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.VideoPhotoSelectActivity;

import java.util.List;

public class OwnerGridUploadImageAdapter extends BaseAdapter {

    private List<UploadPhotoNormalBean> pathBeanList;
    private Activity activity;

    public OwnerGridUploadImageAdapter(List<UploadPhotoNormalBean> pathBeanList, Activity activity) {
        this.pathBeanList = pathBeanList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pathBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return pathBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null || view.getTag() != null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.activity_create_order_grid_item, viewGroup, false);
            viewHolder.mImgBack = view.findViewById(R.id.grid_view_img);
            viewHolder.mBtnClose = view.findViewById(R.id.grid_view_img_btn);
            viewHolder.tv_update = view.findViewById(R.id.tv_update);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if ("1".equals(pathBeanList.get(i).getLocalPath())) {
            viewHolder.mBtnClose.setVisibility(View.INVISIBLE);
            viewHolder.tv_update.setVisibility(View.VISIBLE);

            // ViewGroup.LayoutParams params = viewHolder.mImgBack.getLayoutParams();

            viewHolder.mImgBack.getLayoutParams().width = ScreenUtil.dp2px(activity, 40);
            viewHolder.mImgBack.getLayoutParams().height = ScreenUtil.dp2px(activity, 40);
            viewHolder.mImgBack.setImageDrawable(activity.getResources().getDrawable(R.mipmap.selector_img));
            viewHolder.mImgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pathBeanList.size() > 12) {
                        HelperView.Toast(activity, "最多上传12张图片!");
                        return;
                    }

//                    PictureSelector.create(activity)
//                            .openGallery(PictureMimeType.ofImage())
//                            .theme(R.style.picture_default_style)
//                            // 最大选择图片数
//                            .maxSelectNum(1)
//                            // 最小选择图片数
//                            .minSelectNum(1)
//                            .selectionMode(PictureConfig.MULTIPLE)
//                            .previewImage(true)
//                            .previewEggs(true)
//                            .compress(true)
//                            .isCamera(true)
//                            .synOrAsy(true)
//                            .forResult(10);

                    AndPermission.with(activity)
                            .runtime()
                            .permission(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE, Permission.RECORD_AUDIO)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Intent intent = new Intent(activity, VideoPhotoSelectActivity.class);

                                    activity.startActivityForResult(intent, 10);
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    ToastUtils.showLong("权限被拒绝,拍照上传功能无法使用!");
                                }
                            })
                            .start();


//                    PictureSelector.create(activity)
//                            .openCamera(PictureMimeType.ofImage())
//                            .forResult(10);
                }
            });
        } else {
            viewHolder.tv_update.setVisibility(View.GONE);
            viewHolder.mBtnClose.setVisibility(View.VISIBLE);
            Picasso.get().load("file://".concat(pathBeanList.get(i).getLocalPath())).into(viewHolder.mImgBack);
            viewHolder.mBtnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathBeanList.remove(pathBeanList.get(i));
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    static class ViewHolder {
        ImageButton mBtnClose;
        ImageView mImgBack;
        TextView tv_update;
    }

    public void addItemData(UploadPhotoNormalBean bean) {
        pathBeanList.add(0, bean);
        notifyDataSetChanged();
    }

    public List<UploadPhotoNormalBean> getImageList() {
        return pathBeanList;
    }
}

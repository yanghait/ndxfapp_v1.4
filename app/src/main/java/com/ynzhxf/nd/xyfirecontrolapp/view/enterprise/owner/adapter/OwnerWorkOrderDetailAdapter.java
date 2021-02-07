package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OwnerWorkOrderDetailAdapter extends BaseAdapter {

    private List<String> imagePath;

    private Activity context;

    public OwnerWorkOrderDetailAdapter(Activity context, List<String> imagePath) {
        this.context = context;
        this.imagePath = imagePath;
    }

    @Override
    public int getCount() {
        return imagePath.size();
    }

    @Override
    public Object getItem(int i) {
        return imagePath.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        ImageView mImageView;
        ImageView mVideoPlay;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.gridview_item_see, null);
            viewHolder.mImageView = view.findViewById(R.id.image_load);
            viewHolder.mVideoPlay = view.findViewById(R.id.video_play);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mImageView.getLayoutParams().height = 230;
        viewHolder.mImageView.getLayoutParams().width = 230;

        final String fileUlr = imagePath.get(position);

        //LogUtils.eTag("输出文件路径001234---" + fileUlr);

        //com.ynzhxf.nd.firecontrolapp.util.LogUtils.showLoge("输出文件路径00123456---", fileUlr);

        if (!StringUtils.isEmpty(fileUlr) && (fileUlr.toLowerCase().endsWith(".jpg") || fileUlr.toLowerCase().endsWith(".jpeg") ||
                fileUlr.toLowerCase().endsWith(".png"))) {
            viewHolder.mVideoPlay.setVisibility(View.GONE);

            Picasso.get().load(URLConstant.URL_BASE1.concat(imagePath.get(position)))
                    .resize(230, 230).centerCrop().error(R.drawable.img_load).into(viewHolder.mImageView);

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Picasso.get().load(URLConstant.URL_BASE1.concat(fileUlr)).error(R.drawable.img_load).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                File file = bitmapToFile(bitmap, String.valueOf(System.currentTimeMillis()).concat(".png"));

                                final LocalMedia media = new LocalMedia();
                                media.setPath(file.getAbsolutePath());
                                List<LocalMedia> selectList = new ArrayList<>();
                                selectList.add(media);
                                PictureSelector.create(context).themeStyle(R.style.picture_default_style).compress(true).openExternalPreview(0, selectList);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                ToastUtils.showLong("加载失败,图片可能不存在!");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        HelperView.Toast(context, "预览失败,请稍后再试!");
                    }
                }
            });
        } else if (!StringUtils.isEmpty(fileUlr)) {

            viewHolder.mVideoPlay.setVisibility(View.VISIBLE);

            RequestBuilder<Drawable> requestBuilder = Glide.with(context).asDrawable();
            RequestBuilder<Drawable> requestErrorBuilder = Glide.with(context).asDrawable().load(R.drawable.img_load);
            RequestOptions options = new RequestOptions();

            Glide.with(context).load(URLConstant.URL_BASE1.concat(fileUlr))
                    .thumbnail(requestBuilder.apply(options.circleCrop().centerCrop().override(230, 230)
                            .error(R.drawable.img_load))).error(requestErrorBuilder).into(viewHolder.mImageView);

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final ProgressDialog dialog = showProgress(context, "加载中...", false);

                    LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();

                    hashMap.put(0, fileUlr);

                    FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                        @Override
                        public void onStart(BaseDownloadTask task) {

                        }

                        @Override
                        public void onCompleted(BaseDownloadTask task) {
                            dialog.dismiss();

                            final String fileName = fileUlr.substring(fileUlr.lastIndexOf("/") + 1);

                            final String filePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;

                            PictureSelector.create(context).externalPictureVideo(filePath);
                        }

                        @Override
                        public void onError(BaseDownloadTask task) {
                            ToastUtils.showLong("文件不存在!");
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            viewHolder.mVideoPlay.setVisibility(View.GONE);
            Picasso.get().load(URLConstant.URL_BASE1.concat(imagePath.get(position)))
                    .resize(230, 230).centerCrop().error(R.drawable.img_load).into(viewHolder.mImageView);
        }

        return view;
    }

    protected ProgressDialog showProgress(Context context, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {

        String defaultPath = FileDownloadUtils.getDefaultSaveRootPath() + "/ViewFile";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String defaultImgPath = defaultPath + "/" + fileName;
        file = new File(defaultImgPath);
        try {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);

            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}

package com.ynzhxf.nd.xyfirecontrolapp.util;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.Call;

import static com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.OperationalPlansActivity.getMIMEType;

/**
 * author hbzhou
 * date 2019/3/22 15:52
 */
public class FileOutUtil {

    private static ProgressDialog showProgress(Context context, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    public static void initDataForMonth(boolean isInspect, final Context context, String orderId, String url) {

        if (StringUtils.isEmpty(orderId)) {
            ToastUtils.showLong("未发现参数信息,无法导出报表!");
            return;
        }
        final ProgressDialog dialog = showProgress(context, "导出中...", false);

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        if (!isInspect) {
            params.put("WorkOrderId", orderId);
        } else {
            params.put("TaskId", orderId);
        }
        // LogUtils.showLoge("params0000---", HelperTool.getToken() + "~~~" + projectId);

        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("获取业主巡检记录008---", e.getMessage());
                        dialog.dismiss();
                        ToastUtils.showLong("暂未发现可导出报表!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取业主巡检记录008---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                if (StringUtils.isEmpty(jsonObject.getString("data"))) {
                                    ToastUtils.showLong("暂无可导出记录报表!");
                                    return;
                                }

                                final String fileName = jsonObject.getString("data").substring(jsonObject.getString("data").lastIndexOf("/") + 1);

                                //FileDownloader.getImpl().clearAllTaskData();

                                // 处理下载文件逻辑
                                LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
                                hashMap.put(0, jsonObject.getString("data"));

                                FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                                    @Override
                                    public void onStart(BaseDownloadTask task) {

                                    }

                                    @Override
                                    public void onCompleted(BaseDownloadTask task) {
                                        dialog.dismiss();

                                        if (StringUtils.isEmpty(fileName)) {
                                            HelperView.Toast(context, "报表未下载或已删除!");
                                            return;
                                        }

                                        final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;

                                        openFilePath(context, fileName, newPath);
                                    }

                                    @Override
                                    public void onError(BaseDownloadTask task) {
                                        dialog.dismiss();

                                        ToastUtils.showLong("暂无可导出记录报表!");
                                    }
                                });

                            } else {
                                dialog.dismiss();
                                ToastUtils.showLong("暂无可导出记录报表!");
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void openFileReader(Context context, String pathName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try {
            Object.put("pkgName", context.getApplicationContext().getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("menuData", Object.toString());
        QbSdk.getMiniQBVersion(context);
        QbSdk.openFileReader(context, pathName, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                LogUtils.showLogI("打开文件结果", s);
            }
        });
    }

    private static void openFilePath(Context context, String fileName, String newPath) {
        boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), fileName);
        if (exists) {
            FileOutUtil.openFileReader(context, newPath);
        } else {
            HelperView.Toast(context, "文件未下载或已删除!");
        }
    }


    public static void openFileForSdk(Context context, String newPath) {
        HashMap<String, String> params = new HashMap<>();

        params.put("local", "false");

        QbSdk.openFileReader(context, newPath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                LogUtils.showLogI("OnOpenFile---", s);
            }
        });
    }

    public static void openFileForThirdApp(Context context, String newPath, String name) {
        Uri uri;
        // 兼容7.0及以上版本获取URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.ynzhxf.nd.firecontrolapp.fileprovider", new File(newPath));
        } else {
            uri = Uri.fromFile(new File(newPath));
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMIMEType(name));//getMIMEType(myFileBeanList.get(position).getF_FileName())
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }


    public static String saveBitmap(String dir, Bitmap b) {
        long dataTake = System.currentTimeMillis();
        String jpegName = dir + File.separator + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
                return bitmap;
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
//        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
        return bitmap;
    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND); //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }
        return bitmap;
    }
}

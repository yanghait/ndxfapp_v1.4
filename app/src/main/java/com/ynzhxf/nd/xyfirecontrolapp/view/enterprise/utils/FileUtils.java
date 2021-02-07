package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareMyFileActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileShareProgressUpdateActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class FileUtils {

    private static FileDownloadClick callBack;

    private static OnProgressUpdateListener listener;

    private static Context context;

    public static FileUtils getInstant() {
        return new FileUtils();
    }

    public void setUpdateProgressListener(OnProgressUpdateListener updateListener) {
        listener = updateListener;
    }

    public static void fileDelete(final Activity activity, List<String> listId, final IFileDeleteCallBack callBack) {
        String ids = "";
        for (String id : listId) {
            ids = ids.concat(id).concat(",");
        }

        String idList = ids.substring(0, ids.lastIndexOf(","));
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("IDs", idList);

        //LogUtils.showLoge("输出要删除文件数1351~~~~", idList + "--------" + listId.size());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_DELETE_ACTION))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //LogUtils.showLoge("文件分享文件删除结果1111---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("文件分享文件删除结果0909---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOk = json.getBoolean("success");
                            callBack.onResult(isOk);
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(activity, e.getMessage());
                        }
                    }
                });
    }

    private static void initTestData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", id);
        params.put("typeId", "");
        params.put("pageIndex", "1");
        params.put("pageSize", "20");
        params.put("keyword", "");

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_SHARE_ALL_FILE_LIST))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("文件分享共享文件列表1111---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("文件分享共享文件列表0909---", response);
                    }
                });
    }

    private static FileDownloadListener queueTarget = new FileDownloadListener() {

        @Override
        protected void started(BaseDownloadTask task) {
            LogUtils.showLoge("下载完成文件位置1212_started", task.getPath() + "~~~~" + task.getFilename());

            updateTasks(task);

            //initTaskData(task,task.getSoFarBytes(),task.getTotalBytes());

            if (listener != null) {
                listener.OnStart(task);
            }
            if (callBack != null) {
                callBack.onStart(task);
            }
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            if (listener != null) {
                listener.OnProgress(task, soFarBytes, totalBytes);
            }
            LogUtils.showLoge("下载完成文件位置1212_progress", (soFarBytes / 1024) + "~~~~" + (totalBytes / 1024));

        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
        }

        @Override
        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {

        }

        @Override
        protected void completed(BaseDownloadTask task) {

            removeCompletedTasks(task);

            initTaskData(task, task.getSoFarBytes(), task.getTotalBytes());

            LogUtils.showLoge("下载完成文件url1111111_completed", task.getUrl());

            if (listener != null) {
                listener.Completed(task);
            }
            if (callBack != null) {
                callBack.onCompleted(task);
            }

            // 下载完成重命名
            String oldPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + task.getUrl().substring(task.getUrl().lastIndexOf("/") + 1);

            String path = SPUtils.getInstance().getString(task.getUrl());

            FileShareMyFileActivity.renameFile(oldPath, path);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            updateTasks(task);

            if (listener != null) {
                listener.OnPaused(task);
            }
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            LogUtils.showLoge("下载完成文件位置1212_error", task.getPath() + "~~~~" + task.getFilename() + e.getMessage());

            if (listener != null) {
                listener.OnError(task);
            }
            if (callBack != null) {
                callBack.onError(task);
            }
        }

        @Override
        protected void warn(BaseDownloadTask task) {

            LogUtils.showLoge("下载完成文件位置1212_warn", task.getPath() + "~~~~" + task.getFilename());

            if (listener != null) {
                listener.OnError(task);
            }
            if (callBack != null) {
                callBack.onError(task);
            }
        }
    };

    private static void updateTasks(BaseDownloadTask task) {
        List<BaseDownloadTask> tasks = FileShareProgressUpdateActivity.tasks;
        if (tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {
                if (task.getUrl().equals(tasks.get(i).getUrl())) {
                    tasks.remove(i);
                    tasks.add(i, task);
                    break;
                }
                if (i == tasks.size() - 1) {
                    tasks.add(0, task);
                    break;
                }
            }
        } else {
            tasks.add(0, task);
        }
    }

    private static void removeCompletedTasks(BaseDownloadTask task) {
        List<BaseDownloadTask> tasks = FileShareProgressUpdateActivity.tasks;
        if (tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {
                if (task.getUrl().equals(tasks.get(i).getUrl())) {
                    tasks.remove(i);
                    break;
                }
            }
        }
    }

    private static void initTaskData(BaseDownloadTask task, int so, int total) {
        double pro = 1.00 * so / total * 100.00;
        if (StringUtils.isEmpty(CacheDiskUtils.getInstance().getString("task", ""))) {
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            try {
                json.put("url", task.getUrl());
                json.put("status", task.getStatus());
                json.put("fileLength", total / 1024);
                json.put("fileName", task.getFilename());
                json.put("progress", (int) Math.round(pro));
                json.put("task", task);
                jsonArray.put(json);

            } catch (Exception e) {
                //HelperView.Toast();
            }
            CacheDiskUtils.getInstance().put("task", jsonArray.toString());
        } else {
            try {
                JSONArray jsonArray = new JSONArray(CacheDiskUtils.getInstance().getString("task", ""));

                JSONObject json = new JSONObject();
                json.put("url", task.getUrl());
                json.put("status", task.getStatus());
                json.put("fileLength", total / 1024);
                json.put("fileName", task.getFilename());
                json.put("progress", (int) Math.round(pro));
                json.put("task", task);

                jsonArray.put(json);

                CacheDiskUtils.getInstance().put("task", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initQueueDownload(final LinkedHashMap<Integer, String> mSelectMap,
                                         final FileDownloadClick callBack1) {
        // 第二种方式:

        callBack = callBack1;

        String[] urlStr = new String[mSelectMap.size()];

        List<Integer> listPosition = new ArrayList<>();

        int n = 0;
        Iterator<Integer> it = mSelectMap.keySet().iterator();
        while (it.hasNext()) {
            int key = it.next();
            listPosition.add(key);
            urlStr[n] = mSelectMap.get(key);
            n++;
        }

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);

        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < urlStr.length; i++) {
            String path = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + urlStr[i].substring(urlStr[i].lastIndexOf("/") + 1);

            // 对中文链接编码
            String url = URLConstant.URL_BASE1 + urlStr[i];
            try {
                url = URIEncoderUtil.encode(URLConstant.URL_BASE1 + urlStr[i], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            BaseDownloadTask task = FileDownloader.getImpl().create(url);
            task.setCallbackProgressTimes(300);
            task.setMinIntervalUpdateSpeed(400);
            task.setPath(path);
            task.setTag(listPosition.get(i));
            tasks.add(task);
        }

        // 所有任务在下载失败的时候都自动重试一次
        queueSet.setAutoRetryTimes(1);

        // 并行执行该任务队列
        queueSet.downloadTogether(tasks);

        // 最后你需要主动调用start方法来启动该Queue
        queueSet.start();
    }

    public interface FileDownloadClick {
        void onStart(BaseDownloadTask task);

        void onCompleted(BaseDownloadTask task);

        void onError(BaseDownloadTask task);
    }

    public interface IFileDeleteCallBack {
        void onResult(boolean result);
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void beginCameraDialog(final AlertDialog DIALOG, final OnClickSelecedListener callBack) {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onCancel();
                }
            });
            window.findViewById(R.id.photodialog_btn_take).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onTake();
                }
            });
            window.findViewById(R.id.photodialog_btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onEditor();
                }
            });
            window.findViewById(R.id.photodialog_btn_native).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onNative();
                }
            });
        }
    }

    public static void beginAlertDialog(final AlertDialog DIALOG, final OnClickInspectListener callBack) {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_inspect_records);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            Button viewContent1 = window.findViewById(R.id.photodialog_btn_take);
            Button viewContent2 = window.findViewById(R.id.photodialog_btn_edit);
            Button viewContent3 = window.findViewById(R.id.photodialog_btn_native);
            Button viewContent4 = window.findViewById(R.id.photodialog_btn_out);

            viewContent1.setText("只看异常");
            viewContent2.setText("只看正常");
            viewContent3.setText("查看全部");

            window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonFive();
                }
            });
            viewContent1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonOne();
                }
            });
            viewContent2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonTwo();
                }
            });
            viewContent3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonThree();
                }
            });
            viewContent4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonFour();
                }
            });
        }
    }

    public static void beginDialogForDownload(final AlertDialog DIALOG, final OnClickSelecedListener callBack) {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onCancel();
                }
            });
            window.findViewById(R.id.photodialog_btn_take).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onTake();
                }
            });
//            window.findViewById(R.id.photodialog_btn_edit).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callBack.onEditor();
//                }
//            });
            window.findViewById(R.id.photodialog_btn_edit).setVisibility(View.GONE);

            window.findViewById(R.id.photodialog_btn_native).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onNative();
                }
            });
            View view = window.findViewById(R.id.file_download_line);
            view.setVisibility(View.GONE);
        }
    }

    public interface OnProgressUpdateListener {
        void OnStart(BaseDownloadTask task);

        void OnProgress(BaseDownloadTask task, int cur, int total);

        void Completed(BaseDownloadTask task);

        void OnPaused(BaseDownloadTask task);

        void OnError(BaseDownloadTask task);
    }

    public interface OnClickSelecedListener {
        void onCancel();

        void onTake();

        void onNative();

        void onEditor();
    }

    public interface OnClickInspectListener {
        void onButtonOne();

        void onButtonTwo();

        void onButtonThree();

        void onButtonFour();

        void onButtonFive();
    }

    /**
     * -- MIME 列表 --
     */
    public static final String[][] MIME_MapTable = {
            // --{后缀名， MIME类型}   --
            {".3gp", "video/3gpp"},
            {".3gpp", "video/3gpp"},
            {".aac", "audio/x-mpeg"},
            {".amr", "audio/x-mpeg"},
            {".apk", "application/vnd.android.package-archive"},
            {".avi", "video/x-msvideo"},
            {".aab", "application/x-authoware-bin"},
            {".aam", "application/x-authoware-map"},
            {".aas", "application/x-authoware-seg"},
            {".ai", "application/postscript"},
            {".aif", "audio/x-aiff"},
            {".aifc", "audio/x-aiff"},
            {".aiff", "audio/x-aiff"},
            {".als", "audio/x-alpha5"},
            {".amc", "application/x-mpeg"},
            {".ani", "application/octet-stream"},
            {".asc", "text/plain"},
            {".asd", "application/astound"},
            {".asf", "video/x-ms-asf"},
            {".asn", "application/astound"},
            {".asp", "application/x-asap"},
            {".asx", " video/x-ms-asf"},
            {".au", "audio/basic"},
            {".avb", "application/octet-stream"},
            {".awb", "audio/amr-wb"},
            {".bcpio", "application/x-bcpio"},
            {".bld", "application/bld"},
            {".bld2", "application/bld2"},
            {".bpk", "application/octet-stream"},
            {".bz2", "application/x-bzip2"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".cal", "image/x-cals"},
            {".ccn", "application/x-cnc"},
            {".cco", "application/x-cocoa"},
            {".cdf", "application/x-netcdf"},
            {".cgi", "magnus-internal/cgi"},
            {".chat", "application/x-chat"},
            {".clp", "application/x-msclip"},
            {".cmx", "application/x-cmx"},
            {".co", "application/x-cult3d-object"},
            {".cod", "image/cis-cod"},
            {".cpio", "application/x-cpio"},
            {".cpt", "application/mac-compactpro"},
            {".crd", "application/x-mscardfile"},
            {".csh", "application/x-csh"},
            {".csm", "chemical/x-csml"},
            {".csml", "chemical/x-csml"},
            {".css", "text/css"},
            {".cur", "application/octet-stream"},
            {".doc", "application/msword"},
            {".dcm", "x-lml/x-evm"},
            {".dcr", "application/x-director"},
            {".dcx", "image/x-dcx"},
            {".dhtml", "text/html"},
            {".dir", "application/x-director"},
            {".dll", "application/octet-stream"},
            {".dmg", "application/octet-stream"},
            {".dms", "application/octet-stream"},
            {".dot", "application/x-dot"},
            {".dvi", "application/x-dvi"},
            {".dwf", "drawing/x-dwf"},
            {".dwg", "application/x-autocad"},
            {".dxf", "application/x-autocad"},
            {".dxr", "application/x-director"},
            {".ebk", "application/x-expandedbook"},
            {".emb", "chemical/x-embl-dl-nucleotide"},
            {".embl", "chemical/x-embl-dl-nucleotide"},
            {".eps", "application/postscript"},
            {".epub", "application/epub+zip"},
            {".eri", "image/x-eri"},
            {".es", "audio/echospeech"},
            {".esl", "audio/echospeech"},
            {".etc", "application/x-earthtime"},
            {".etx", "text/x-setext"},
            {".evm", "x-lml/x-evm"},
            {".evy", "application/x-envoy"},
            {".exe", "application/octet-stream"},
            {".fh4", "image/x-freehand"},
            {".fh5", "image/x-freehand"},
            {".fhc", "image/x-freehand"},
            {".fif", "image/fif"},
            {".fm", "application/x-maker"},
            {".fpx", "image/x-fpx"},
            {".fvi", "video/isivideo"},
            {".flv", "video/x-msvideo"},
            {".gau", "chemical/x-gaussian-input"},
            {".gca", "application/x-gca-compressed"},
            {".gdb", "x-lml/x-gdb"},
            {".gif", "image/gif"},
            {".gps", "application/x-gps"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".hdf", "application/x-hdf"},
            {".hdm", "text/x-hdml"},
            {".hdml", "text/x-hdml"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".hlp", "application/winhlp"},
            {".hqx", "application/mac-binhex40"},
            {".hts", "text/html"},
            {".ice", "x-conference/x-cooltalk"},
            {".ico", "application/octet-stream"},
            {".ief", "image/ief"},
            {".ifm", "image/gif"},
            {".ifs", "image/ifs"},
            {".imy", "audio/melody"},
            {".ins", "application/x-net-install"},
            {".ips", "application/x-ipscript"},
            {".ipx", "application/x-ipix"},
            {".it", "audio/x-mod"},
            {".itz", "audio/x-mod"},
            {".ivr", "i-world/i-vrml"},
            {".j2k", "image/j2k"},
            {".jad", "text/vnd.sun.j2me.app-descriptor"},
            {".jam", "application/x-jam"},
            {".jnlp", "application/x-java-jnlp-file"},
            {".jpe", "image/jpeg"},
            {".jpz", "image/jpeg"},
            {".jwc", "application/jwc"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".kjx", "application/x-kjx"},
            {".lak", "x-lml/x-lak"},
            {".latex", "application/x-latex"},
            {".lcc", "application/fastman"},
            {".lcl", "application/x-digitalloca"},
            {".lcr", "application/x-digitalloca"},
            {".lgh", "application/lgh"},
            {".lha", "application/octet-stream"},
            {".lml", "x-lml/x-lml"},
            {".lmlpack", "x-lml/x-lmlpack"},
            {".log", "text/plain"},
            {".lsf", "video/x-ms-asf"},
            {".lsx", "video/x-ms-asf"},
            {".lzh", "application/x-lzh "},
            {".m13", "application/x-msmediaview"},
            {".m14", "application/x-msmediaview"},
            {".m15", "audio/x-mod"},
            {".m3u", "audio/x-mpegurl"},
            {".m3url", "audio/x-mpegurl"},
            {".ma1", "audio/ma1"},
            {".ma2", "audio/ma2"},
            {".ma3", "audio/ma3"},
            {".ma5", "audio/ma5"},
            {".man", "application/x-troff-man"},
            {".map", "magnus-internal/imagemap"},
            {".mbd", "application/mbedlet"},
            {".mct", "application/x-mascot"},
            {".mdb", "application/x-msaccess"},
            {".mdz", "audio/x-mod"},
            {".me", "application/x-troff-me"},
            {".mel", "text/x-vmel"},
            {".mi", "application/x-mif"},
            {".mid", "audio/midi"},
            {".midi", "audio/midi"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".mif", "application/x-mif"},
            {".mil", "image/x-cals"},
            {".mio", "audio/x-mio"},
            {".mmf", "application/x-skt-lbs"},
            {".mng", "video/x-mng"},
            {".mny", "application/x-msmoney"},
            {".moc", "application/x-mocha"},
            {".mocha", "application/x-mocha"},
            {".mod", "audio/x-mod"},
            {".mof", "application/x-yumekara"},
            {".mol", "chemical/x-mdl-molfile"},
            {".mop", "chemical/x-mopac-input"},
            {".movie", "video/x-sgi-movie"},
            {".mpn", "application/vnd.mophun.application"},
            {".mpp", "application/vnd.ms-project"},
            {".mps", "application/x-mapserver"},
            {".mrl", "text/x-mrml"},
            {".mrm", "application/x-mrm"},
            {".ms", "application/x-troff-ms"},
            {".mts", "application/metastream"},
            {".mtx", "application/metastream"},
            {".mtz", "application/metastream"},
            {".mzv", "application/metastream"},
            {".nar", "application/zip"},
            {".nbmp", "image/nbmp"},
            {".nc", "application/x-netcdf"},
            {".ndb", "x-lml/x-ndb"},
            {".ndwn", "application/ndwn"},
            {".nif", "application/x-nif"},
            {".nmz", "application/x-scream"},
            {".nokia-op-logo", "image/vnd.nok-oplogo-color"},
            {".npx", "application/x-netfpx"},
            {".nsnd", "audio/nsnd"},
            {".nva", "application/x-neva1"},
            {".oda", "application/oda"},
            {".oom", "application/x-atlasMate-plugin"},
            {".ogg", "audio/ogg"},
            {".pac", "audio/x-pac"},
            {".pae", "audio/x-epac"},
            {".pan", "application/x-pan"},
            {".pbm", "image/x-portable-bitmap"},
            {".pcx", "image/x-pcx"},
            {".pda", "image/x-pda"},
            {".pdb", "chemical/x-pdb"},
            {".pdf", "application/pdf"},
            {".pfr", "application/font-tdpfr"},
            {".pgm", "image/x-portable-graymap"},
            {".pict", "image/x-pict"},
            {".pm", "application/x-perl"},
            {".pmd", "application/x-pmd"},
            {".png", "image/png"},
            {".pnm", "image/x-portable-anymap"},
            {".pnz", "image/png"},
            {".pot", "application/vnd.ms-powerpoint"},
            {".ppm", "image/x-portable-pixmap"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pqf", "application/x-cprplayer"},
            {".pqi", "application/cprplayer"},
            {".prc", "application/x-prc"},
            {".proxy", "application/x-ns-proxy-autoconfig"},
            {".prop", "text/plain"},
            {".ps", "application/postscript"},
            {".ptlk", "application/listenup"},
            {".pub", "application/x-mspublisher"},
            {".pvx", "video/x-pv-pvx"},
            {".qcp", "audio/vnd.qcelp"},
            {".qt", "video/quicktime"},
            {".qti", "image/x-quicktime"},
            {".qtif", "image/x-quicktime"},
            {".r3t", "text/vnd.rn-realtext3d"},
            {".ra", "audio/x-pn-realaudio"},
            {".ram", "audio/x-pn-realaudio"},
            {".ras", "image/x-cmu-raster"},
            {".rdf", "application/rdf+xml"},
            {".rf", "image/vnd.rn-realflash"},
            {".rgb", "image/x-rgb"},
            {".rlf", "application/x-richlink"},
            {".rm", "audio/x-pn-realaudio"},
            {".rmf", "audio/x-rmf"},
            {".rmm", "audio/x-pn-realaudio"},
            {".rnx", "application/vnd.rn-realplayer"},
            {".roff", "application/x-troff"},
            {".rp", "image/vnd.rn-realpix"},
            {".rpm", "audio/x-pn-realaudio-plugin"},
            {".rt", "text/vnd.rn-realtext"},
            {".rte", "x-lml/x-gps"},
            {".rtf", "application/rtf"},
            {".rtg", "application/metastream"},
            {".rtx", "text/richtext"},
            {".rv", "video/vnd.rn-realvideo"},
            {".rwc", "application/x-rogerwilco"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".s3m", "audio/x-mod"},
            {".s3z", "audio/x-mod"},
            {".sca", "application/x-supercard"},
            {".scd", "application/x-msschedule"},
            {".sdf", "application/e-score"},
            {".sea", "application/x-stuffit"},
            {".sgm", "text/x-sgml"},
            {".sgml", "text/x-sgml"},
            {".shar", "application/x-shar"},
            {".shtml", "magnus-internal/parsed-html"},
            {".shw", "application/presentations"},
            {".si6", "image/si6"},
            {".si7", "image/vnd.stiwap.sis"},
            {".si9", "image/vnd.lgtwap.sis"},
            {".sis", "application/vnd.symbian.install"},
            {".sit", "application/x-stuffit"},
            {".skd", "application/x-koan"},
            {".skm", "application/x-koan"},
            {".skp", "application/x-koan"},
            {".skt", "application/x-koan"},
            {".slc", "application/x-salsa"},
            {".smd", "audio/x-smd"},
            {".smi", "application/smil"},
            {".smil", "application/smil"},
            {".smp", "application/studiom"},
            {".smz", "audio/x-smd"},
            {".sh", "application/x-sh"},
            {".snd", "audio/basic"},
            {".spc", "text/x-speech"},
            {".spl", "application/futuresplash"},
            {".spr", "application/x-sprite"},
            {".sprite", "application/x-sprite"},
            {".sdp", "application/sdp"},
            {".spt", "application/x-spt"},
            {".src", "application/x-wais-source"},
            {".stk", "application/hyperstudio"},
            {".stm", "audio/x-mod"},
            {".sv4cpio", "application/x-sv4cpio"},
            {".sv4crc", "application/x-sv4crc"},
            {".svf", "image/vnd"},
            {".svg", "image/svg-xml"},
            {".svh", "image/svh"},
            {".svr", "x-world/x-svr"},
            {".swf", "application/x-shockwave-flash"},
            {".swfl", "application/x-shockwave-flash"},
            {".t", "application/x-troff"},
            {".tad", "application/octet-stream"},
            {".talk", "text/x-speech"},
            {".tar", "application/x-tar"},
            {".taz", "application/x-tar"},
            {".tbp", "application/x-timbuktu"},
            {".tbt", "application/x-timbuktu"},
            {".tcl", "application/x-tcl"},
            {".tex", "application/x-tex"},
            {".texi", "application/x-texinfo"},
            {".texinfo", "application/x-texinfo"},
            {".tgz", "application/x-tar"},
            {".thm", "application/vnd.eri.thm"},
            {".tif", "image/tiff"},
            {".tiff", "image/tiff"},
            {".tki", "application/x-tkined"},
            {".tkined", "application/x-tkined"},
            {".toc", "application/toc"},
            {".toy", "image/toy"},
            {".tr", "application/x-troff"},
            {".trk", "x-lml/x-gps"},
            {".trm", "application/x-msterminal"},
            {".tsi", "audio/tsplayer"},
            {".tsp", "application/dsptype"},
            {".tsv", "text/tab-separated-values"},
            {".ttf", "application/octet-stream"},
            {".ttz", "application/t-time"},
            {".txt", "text/plain"},
            {".ult", "audio/x-mod"},
            {".ustar", "application/x-ustar"},
            {".uu", "application/x-uuencode"},
            {".uue", "application/x-uuencode"},
            {".vcd", "application/x-cdlink"},
            {".vcf", "text/x-vcard"},
            {".vdo", "video/vdo"},
            {".vib", "audio/vib"},
            {".viv", "video/vivo"},
            {".vivo", "video/vivo"},
            {".vmd", "application/vocaltec-media-desc"},
            {".vmf", "application/vocaltec-media-file"},
            {".vmi", "application/x-dreamcast-vms-info"},
            {".vms", "application/x-dreamcast-vms"},
            {".vox", "audio/voxware"},
            {".vqe", "audio/x-twinvq-plugin"},
            {".vqf", "audio/x-twinvq"},
            {".vql", "audio/x-twinvq"},
            {".vre", "x-world/x-vream"},
            {".vrml", "x-world/x-vrml"},
            {".vrt", "x-world/x-vrt"},
            {".vrw", "x-world/x-vream"},
            {".vts", "workbook/formulaone"},
            {".wax", "audio/x-ms-wax"},
            {".wbmp", "image/vnd.wap.wbmp"},
            {".web", "application/vnd.xara"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wi", "image/wavelet"},
            {".wis", "application/x-InstallShield"},
            {".wm", "video/x-ms-wm"},
            {".wmd", "application/x-ms-wmd"},
            {".wmf", "application/x-msmetafile"},
            {".wml", "text/vnd.wap.wml"},
            {".wmlc", "application/vnd.wap.wmlc"},
            {".wmls", "text/vnd.wap.wmlscript"},
            {".wmlsc", "application/vnd.wap.wmlscriptc"},
            {".wmlscript", "text/vnd.wap.wmlscript"},
            {".wmv", "video/x-ms-wmv"},
            {".wmx", "video/x-ms-wmx"},
            {".wmz", "application/x-ms-wmz"},
            {".wpng", "image/x-up-wpng"},
            {".wps", "application/vnd.ms-works"},
            {".wpt", "x-lml/x-gps"},
            {".wri", "application/x-mswrite"},
            {".wrl", "x-world/x-vrml"},
            {".wrz", "x-world/x-vrml"},
            {".ws", "text/vnd.wap.wmlscript"},
            {".wsc", "application/vnd.wap.wmlscriptc"},
            {".wv", "video/wavelet"},
            {".wvx", "video/x-ms-wvx"},
            {".wxl", "application/x-wxl"},
            {".x-gzip", "application/x-gzip"},
            {".xar", "application/vnd.xara"},
            {".xbm", "image/x-xbitmap"},
            {".xdm", "application/x-xdma"},
            {".xdma", "application/x-xdma"},
            {".xdw", "application/vnd.fujixerox.docuworks"},
            {".xht", "application/xhtml+xml"},
            {".xhtm", "application/xhtml+xml"},
            {".xhtml", "application/xhtml+xml"},
            {".xla", "application/vnd.ms-excel"},
            {".xlc", "application/vnd.ms-excel"},
            {".xll", "application/x-excel"},
            {".xlm", "application/vnd.ms-excel"},
            {".xls", "application/vnd.ms-excel"},
            {".xlt", "application/vnd.ms-excel"},
            {".xlw", "application/vnd.ms-excel"},
            {".xm", "audio/x-mod"},
            {".xml", "text/xml"},
            {".xmz", "audio/x-mod"},
            {".xpi", "application/x-xpinstall"},
            {".xpm", "image/x-xpixmap"},
            {".xsit", "text/xml"},
            {".xsl", "text/xml"},
            {".xul", "text/xul"},
            {".xwd", "image/x-xwindowdump"},
            {".xyz", "chemical/x-pdb"},
            {".yz1", "application/x-yz1"},
            {".z", "application/x-compress"},
            {".zac", "application/x-zaurus-zac"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}

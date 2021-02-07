package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareFileTypePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareMyFileListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.impl.FileSharePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.FlowQuickPopupWindow;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.InputMethodUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileShareCreateFileActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.FileShareEditSaveActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.youngfeng.snake.annotations.EnableDragToClose;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.BasePopupWindow;

/**
 * 文件分享我的文件
 */
@EnableDragToClose
public class FileShareMyFileActivity extends AppCompatActivity implements IBaseView, IFileShareFileTypePresenter.IFileShareFileTypeView, IFileShareMyFileListPresenter.IFileShareMyFileListView {

    public IFileShareFileTypePresenter presenter;

    public IFileShareMyFileListPresenter myFileListPresenter;

    public List<String> contents = new ArrayList<>();

    public FileTypeAdapter adapter;

    public TextView tv_file_share_type;

    public boolean isShow = false;

    public PopupWindow window;

    public PopupWindow windowBottom;

    public RecyclerView mRecyclerView;

    public RefreshLayout refreshLayout;

    public ImageButton search;
    public ImageButton add;

    public int currentPosition = 1;

    final public List<FileShareMyFileBean> myFileBeanList = new ArrayList<>();

    public FileShareMyFileInputBean myFileBean;

    public View showView;

    public String projectId;

    public FileShareMyFileAdapter myFileAdapter;

    public LinkedHashMap<Integer, String> mSelectMap = new LinkedHashMap<>();

    public String fileTypeId;

    public boolean isReSetData = true;

    public boolean isShowSearch = false;

    private boolean isSearchText = true;

    protected LinearLayout mShowNoDataView;

    protected ProgressDialog dialog;

    private List<FileShareFileTypeBean> beansType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_share_my);
        //setBarTitle("我的文件");

        search = findViewById(R.id.file_share_search_img);
        add = findViewById(R.id.file_share_add_img);

        mShowNoDataView = findViewById(R.id.all_no_data);

        mRecyclerView = findViewById(R.id.file_share_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_for_file));
//        mRecyclerView.addItemDecoration(div);

        refreshLayout = findViewById(R.id.refreshLayout);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyLoadHeader myLoadHeader = new MyLoadHeader(this);
        refreshLayout.setRefreshHeader(myLoadHeader);

        dialog = showProgress(this, "加载中...", false);


        FileShareMyFileAdapter.radioCount = 0;

        FlowQuickPopupWindow.checkItemPosition = 0;

        initSelectView();

        doRequestForMyFile();

        initRefreshListener();

        FileDownloader.setup(this);

        initToolBarOnClick();

        setBarLintColor();

    }

    protected void setBarLintColor() {
        // 默认状态栏使用ToolBar的颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tool_bar), 0);
    }

    protected ProgressDialog showProgress(Context context, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    public void initToolBarOnClick() {
        ImageButton back = findViewById(R.id.tool_back);

        TextView title = findViewById(R.id.toolbar_title);

        ImageButton addFile = findViewById(R.id.file_share_add_img);

        ImageButton searchButton = findViewById(R.id.file_share_search_img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 添加搜索框

        final LinearLayout file_type_search = findViewById(R.id.file_type_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowSearch) {
                    isShowSearch = false;
                    file_type_search.setVisibility(View.GONE);
                } else {
                    isShowSearch = true;
                    file_type_search.setVisibility(View.VISIBLE);
                }
            }
        });

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initSearchAction(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (StringUtils.isEmpty(newText)) {
                    if (isSearchText) {
                        isSearchText = false;
                    } else {
                        // 当搜索框为空的时候加载默认文件列表
                        myFileBean.setKeyword("");
                        myFileBean.setPageIndex("1");
                        myFileBeanList.clear();
                        myFileListPresenter.doFileShareMyFileList(myFileBean);
                    }
                }
                return true;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (windowBottom != null && windowBottom.isShowing()) {
                    Set<Integer> entitySet = FileShareMyFileAdapter.radioState.keySet();
                    for (Integer key : entitySet) {
                        if (FileShareMyFileAdapter.radioState.get(key)) {

                            myFileAdapter.getViewHolderList().get(key).checkBox.setChecked(false);

                            FileShareMyFileAdapter.radioState.put(key, false);
                        }
                    }
                    windowBottom.dismiss();
                }
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }

            }
        });
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(androidx.appcompat.R.id.submit_area).setBackground(null);
        searchView.onActionViewExpanded();


        title.setText("我的文件");

        title.setFocusable(true);

        title.setFocusableInTouchMode(true);

        title.requestFocus();

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReSetData = true;
                Intent intent = new Intent(FileShareMyFileActivity.this, FileShareCreateFileActivity.class);
                intent.putExtra("id", projectId);
                startActivity(intent);
            }
        });

    }

    public void initSearchAction(String query) {
        if (!StringUtils.isEmpty(query)) {
            myFileBean.setKeyword(query);
        }
        myFileBean.setPageIndex("1");
        myFileBeanList.clear();
        myFileListPresenter.doFileShareMyFileList(myFileBean);
    }

    @Override
    public void callBackFileShareMyFileList(ResultBean<List<FileShareMyFileBean>, String> resultBean) {
        dialog.dismiss();
        if (resultBean == null || resultBean.getData().size() == 0) {
            //HelperView.Toast(this, "暂无更多数据!");
            if (currentPosition == 1 && myFileBeanList.size() == 0) {
                mShowNoDataView.setVisibility(View.VISIBLE);
            }
            return;
        } else {
            mShowNoDataView.setVisibility(View.GONE);
        }

        myFileBeanList.addAll(resultBean.getData());

        myFileAdapter = new FileShareMyFileAdapter(myFileBeanList, this, new FileShareMyFileAdapter.OnCheckBoxChangeListener() {
            @Override
            public void initAdapter(final int position) {
                // LogUtils.showLoge("输出每个文件ID1212---", myFileBeanList.get(position).getID());
                initBottomView(new FileBottomClickCallBack() {
                    @Override
                    public void onDownload() {
                        mSelectMap.clear();
                        Set<Integer> entitySet = FileShareMyFileAdapter.radioState.keySet();
                        for (Integer key : entitySet) {
                            if (FileShareMyFileAdapter.radioState.get(key)) {

                                mSelectMap.put(key, myFileBeanList.get(key).getF_FileUrl());

                                myFileAdapter.getViewHolderList().get(key).checkBox.setChecked(false);

                                FileShareMyFileAdapter.radioState.put(key, false);

                                //myFileAdapter.notifyDataSetChanged();

                                //LogUtils.showLoge("取消checkbox选中位置1200---", String.valueOf(key) + String.valueOf(myFileAdapter.getViewHolderList().get(key).checkBox.isChecked()));
                            }
                        }
                        windowBottom.dismiss();
                        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                        if (EasyPermissions.hasPermissions(FileShareMyFileActivity.this, permissions)) {
                            startQueueDownload();
                        } else {
                            requestPermissionsCallBack(FileShareMyFileActivity.this, "请求存储权限", 12, permissions, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (Success && requestCode == 12) {
                                        startQueueDownload();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onDelete() {

                        windowBottom.dismiss();

                        Set<Integer> entitySet = FileShareMyFileAdapter.radioState.keySet();

                        List<String> idList = new ArrayList<>();

                        for (Integer key : entitySet) {
                            if (FileShareMyFileAdapter.radioState.get(key)) {

                                myFileAdapter.getViewHolderList().get(key).checkBox.setChecked(false);

                                FileShareMyFileAdapter.radioState.put(key, false);

                                idList.add(myFileBeanList.get(key).getID());

                            }
                        }

                        FileShareMyFileAdapter.radioCount = 0;

                        FileUtils.fileDelete(FileShareMyFileActivity.this, idList, new FileUtils.IFileDeleteCallBack() {
                            @Override
                            public void onResult(boolean result) {
                                if (result) {
                                    FileShareMyFileAdapter.radioState.clear();
                                    dialog.show();
                                    currentPosition = 1;
                                    myFileBean.setPageIndex("1");
                                    myFileBean.setPageSize(String.valueOf(myFileBeanList.size()));
                                    myFileBeanList.clear();
                                    myFileListPresenter.doFileShareMyFileList(myFileBean);

                                    myFileAdapter.notifyDataSetChanged();

                                } else {
                                    HelperView.Toast(FileShareMyFileActivity.this, "删除失败,请稍后再试!");
                                }
                            }
                        });
                    }

                    @Override
                    public void onEditor() {

                        windowBottom.dismiss();
                        isReSetData = true;
                        Set<Integer> entitySet = FileShareMyFileAdapter.radioState.keySet();
                        for (Integer key : entitySet) {
                            if (FileShareMyFileAdapter.radioState.get(key)) {

                                myFileAdapter.getViewHolderList().get(key).checkBox.setChecked(false);

                                FileShareMyFileAdapter.radioState.put(key, false);

                                Intent intent = new Intent(FileShareMyFileActivity.this, FileShareEditSaveActivity.class);
                                intent.putExtra("id", projectId);
                                intent.putExtra("fileId", myFileBeanList.get(key).getID());
                                intent.putExtra("typeId", myFileBeanList.get(key).getF_FileTypeID());
                                intent.putExtra("title", myFileBeanList.get(key).getF_Title());
                                startActivity(intent);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked, int position) {

                FileShareMyFileAdapter.radioState.put(position, checked);

                if (checked) {
                    FileShareMyFileAdapter.radioCount++;

                    if (FileShareMyFileAdapter.radioCount > 1) {

                        showEditButton(showView, false);

                    } else if (FileShareMyFileAdapter.radioCount == 1) {

                        showEditButton(showView, true);

                        showPopupForBottom(showView);
                    }

                } else {
                    FileShareMyFileAdapter.radioCount--;
                    if (FileShareMyFileAdapter.radioCount == 0) {
                        windowBottom.dismiss();
                    } else if (FileShareMyFileAdapter.radioCount == 1) {
                        showEditButton(showView, true);
                    }
                }
            }
        });

        myFileAdapter.setOnItemClickListener(new FileShareMyFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (myFileBeanList.size() == 0) {
                    ToastUtils.showLong("网络异常,请稍后再试!");
                    return;
                }

                boolean isExist = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());

                String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(position).getF_FileName();

                if (isExist) {
                    isReSetData = false;
//                    if (Build.VERSION.SDK_INT >= 28) {
//                        openFileForThirdApp(newPath, myFileBeanList.get(position).getF_FileName());
//                    } else {
                        //openFileForSdk(newPath);

                    FileOutUtil.openFileReader(FileShareMyFileActivity.this,newPath);
//                    }
                } else {
                    HelperView.Toast(FileShareMyFileActivity.this, "请先下载后再查看!");
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {

                boolean isExist = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());

                final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(position).getF_FileName();

                if (isExist) {
                    final AlertDialog dialog = new AlertDialog.Builder(FileShareMyFileActivity.this).create();
                    FileUtils.beginCameraDialog(dialog, new FileUtils.OnClickSelecedListener() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onTake() {
                            //1
                            dialog.dismiss();
                            isReSetData = false;
//                            if (Build.VERSION.SDK_INT >= 28) {
//                                openFileForThirdApp(newPath, myFileBeanList.get(position).getF_FileName());
//                            } else {
//                                openFileForSdk(newPath);
//                            }
                            FileOutUtil.openFileReader(FileShareMyFileActivity.this,newPath);
                        }

                        @Override
                        public void onNative() {
                            //3
                            dialog.dismiss();
                            List<String> idList = new ArrayList<>();
                            idList.add(myFileBeanList.get(position).getID());

                            FileUtils.fileDelete(FileShareMyFileActivity.this, idList, new FileUtils.IFileDeleteCallBack() {
                                @Override
                                public void onResult(boolean result) {
                                    if (result) {
                                        currentPosition = 1;
                                        myFileBeanList.clear();
                                        myFileAdapter.notifyDataSetChanged();
                                        myFileBean.setPageIndex("1");
                                        myFileListPresenter.doFileShareMyFileList(myFileBean);

                                    } else {
                                        HelperView.Toast(FileShareMyFileActivity.this, "删除失败,请稍后再试!");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onEditor() {
                            //2
                            dialog.dismiss();
                            isReSetData = true;
                            Intent intent = new Intent(FileShareMyFileActivity.this, FileShareEditSaveActivity.class);
                            intent.putExtra("id", projectId);
                            intent.putExtra("fileId", myFileBeanList.get(position).getID());
                            intent.putExtra("typeId", myFileBeanList.get(position).getF_FileTypeID());
                            intent.putExtra("title", myFileBeanList.get(position).getF_Title());
                            startActivity(intent);
                        }
                    });
                }

                return true;
            }
        });

        myFileAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myFileAdapter);
    }

    public void openFileForSdk(String newPath) {
        HashMap<String, String> params = new HashMap<>();

        params.put("local", "false");
        QbSdk.openFileReader(FileShareMyFileActivity.this, newPath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                LogUtils.showLoge("输出预览文件输出结果---", s);
            }
        });
    }

    public void openFileForThirdApp(String newPath, String name) {
        Uri uri;
        // 兼容7.0及以上版本获取URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(FileShareMyFileActivity.this, "com.ynzhxf.nd.firecontrolapp.fileprovider", new File(newPath));
        } else {
            uri = Uri.fromFile(new File(newPath));
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMIMEType(name));//getMIMEType(myFileBeanList.get(position).getF_FileName())
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(FileShareMyFileActivity.this, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * --获取文件类型 --
     */
    public static String getMIMEType(String filePath) {
        String type = "*/*";
        String fName = filePath;

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }

        for (int i = 0; i < FileUtils.MIME_MapTable.length; i++) {
            if (end.equals(FileUtils.MIME_MapTable[i][0])) {
                type = FileUtils.MIME_MapTable[i][1];
            }
        }
        return type;
    }

    public static boolean renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        return oleFile.renameTo(newFile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFirstData();
    }

    public void initFirstData() {
        if (isReSetData) {
            // 请求我的文件列表

            myFileListPresenter = FileSharePresenterFactory.getFileShareMyFilePresenterImpl(this);

            myFileBean = new FileShareMyFileInputBean();
            myFileBean.setToken(HelperTool.getToken());
            myFileBean.setProjectId(getIntent().getStringExtra("id"));
            myFileBean.setTypeId(fileTypeId);
            currentPosition = 1;
            myFileBean.setPageIndex(String.valueOf(currentPosition));
            myFileBean.setPageSize("20");
            myFileBeanList.clear();

            myFileListPresenter.doFileShareMyFileList(myFileBean);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (windowBottom != null) {
            Set<Integer> entitySet = FileShareMyFileAdapter.radioState.keySet();
            for (Integer key : entitySet) {
                if (FileShareMyFileAdapter.radioState.get(key)) {

                    myFileAdapter.getViewHolderList().get(key).checkBox.setChecked(false);

                    FileShareMyFileAdapter.radioState.put(key, false);
                }
            }
            windowBottom.dismiss();
        }
    }

    public void startQueueDownload() {

        FileUtils.initQueueDownload(mSelectMap, new FileUtils.FileDownloadClick() {
            @Override
            public void onStart(BaseDownloadTask task) {
                int flag = (int) task.getTag();
                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.VISIBLE);
                    myFileAdapter.getViewHolderList().get(flag).loading.startAnim(500);
                }
                FileShareMyFileAdapter.radioCount = 0;
                HelperView.Toast(FileShareMyFileActivity.this, "正在下载...");
                myFileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCompleted(BaseDownloadTask task) {
                int flag = (int) task.getTag();
                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).loading.stopAnim();
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).down_ok.setVisibility(View.VISIBLE);
                }
                FileShareMyFileAdapter.radioCount = 0;
                myFileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(BaseDownloadTask task) {
                int flag = (int) task.getTag();

                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.VISIBLE);
                    myFileAdapter.getViewHolderList().get(flag).loading.stopAnim();
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).down_ok.setVisibility(View.GONE);
                }
                FileShareMyFileAdapter.radioCount = 0;
                myFileAdapter.notifyDataSetChanged();
                HelperView.Toast(FileShareMyFileActivity.this, "下载出错，请稍后再试!");
            }
        });
    }

    private void initBottomView(final FileBottomClickCallBack callBack) {

        if (showView == null) {
            showView = getLayoutInflater().inflate(R.layout.popupwindow_file_bottom, null);
        }

        LinearLayout down_view = showView.findViewById(R.id.popup_down_view);
        LinearLayout dele_view = showView.findViewById(R.id.popup_dele_view);
        LinearLayout edit_view = showView.findViewById(R.id.popup_edit_view);

        down_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onDownload();
            }
        });

        dele_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onDelete();
            }
        });

        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onEditor();
            }
        });
    }

    private void showEditButton(View view, boolean isShow) {
        ImageView edit = view.findViewById(R.id.popup_bottom_img3);
        TextView edit_tv = view.findViewById(R.id.popup_bottom_tx3);
        LinearLayout edit_view = view.findViewById(R.id.popup_edit_view);
        if (isShow) {
            edit_view.setClickable(true);
            edit.setImageDrawable(getResources().getDrawable(R.mipmap.popup_edit_on));
            edit_tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        } else {
            edit_view.setClickable(false);
            edit.setImageDrawable(getResources().getDrawable(R.mipmap.popup_edit_off));
            edit_tv.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    private void showPopupForBottom(View popupBottomView) {

        windowBottom = new PopupWindow(popupBottomView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        windowBottom.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        windowBottom.setFocusable(false);

        windowBottom.setOutsideTouchable(false);

        windowBottom.setTouchable(true);

        windowBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //Log.v("ta","setOnDismissListener");
                isShow = false;
                backgroundAlpha(1f);
            }
        });
        //backgroundAlpha(0.7f);
        if (InputMethodUtils.isShowing(this)) {
            InputMethodUtils.showOrHide(this, this);
        }
        windowBottom.showAtLocation(findViewById(R.id.file_share_main), Gravity.BOTTOM, 0, 0);
    }

    public void initRefreshListener() {

        refreshLayout.setEnableRefresh(false);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPosition++;
                myFileBean.setPageIndex(String.valueOf(currentPosition));
                myFileListPresenter.doFileShareMyFileList(myFileBean);
                refreshLayout.finishLoadMore();
            }
        });
    }

    public void initSelectView() {

        tv_file_share_type = findViewById(R.id.tv_file_share_type);

        tv_file_share_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShow = true;
                if (contents.size() == 0) {
                    ToastUtils.showLong("加载文件分类中...请稍后!");
                    return;
                }

                final FlowQuickPopupWindow mFlowPop = new FlowQuickPopupWindow(FileShareMyFileActivity.this, contents, new FileTypeAdapter.OnClickSelectListener() {
                    @Override
                    public void onClick(View v, int position) {

                        //处理选中文件分类点击事件

                        fileTypeId = beansType.get(position).getID();

                        tv_file_share_type.setText(String.valueOf(beansType.get(position).getF_Name() + " "));

                        // 改为点击按钮类型选择框消失并刷新列表

                        myFileBean.setTypeId(fileTypeId);
                        currentPosition = 1;
                        myFileBean.setPageIndex("1");
                        myFileBeanList.clear();
                        myFileListPresenter.doFileShareMyFileList(myFileBean);

                    }
                });
                mFlowPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        isShow = false;
                    }
                });

                mFlowPop.setAlignBackground(true);

                mFlowPop.showPopupWindow(tv_file_share_type);
            }
        });
    }

    public void doRequestForMyFile() {
        projectId = getIntent().getStringExtra("id");
        // 请求文件分类列表
        presenter = FileSharePresenterFactory.getFileShareFileTypePresenterImpl(this);
        FileShareFileTypeInputBean inputBean = new FileShareFileTypeInputBean();
        inputBean.setToken(HelperTool.getToken());
        inputBean.setProjectId(getIntent().getStringExtra("id"));
        presenter.doFileShareFileType(inputBean);

    }

    public void backgroundAlpha(float bgAlpha)  //阴影改变
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void callBackFileShareFileType(final ResultBean<List<FileShareFileTypeBean>, String> resultBean) {
        if (resultBean != null && resultBean.getData().size() > 0) {

            FileShareFileTypeBean beanAll = new FileShareFileTypeBean();
            beanAll.setID("");
            beanAll.setF_Name("全部");

            beansType.clear();

            beansType.add(beanAll);

            beansType.addAll(resultBean.getData());

            for (FileShareFileTypeBean bean : beansType) {

                contents.add(bean.getF_Name());

            }
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        dialog.dismiss();
    }

    public static PermissionsUtil.IGrantCallBack callBack;

    public static void requestPermissionsCallBack(Activity activity, String title, int requestCode, String[] perm, PermissionsUtil.IGrantCallBack callBack1) {
        callBack = callBack1;
        EasyPermissions.requestPermissions(activity, title, requestCode, perm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (callBack != null) {
            boolean isOK = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    isOK = false;
                    break;
                }
            }
            callBack.result(isOK, requestCode);
        }
    }
}

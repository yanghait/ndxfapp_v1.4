package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.Manifest;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.impl.FileSharePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.FlowQuickPopupWindow;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareMyFileActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter.whetherExistFile;

public class FileShareWeFileActivity extends FileShareMyFileActivity {

    @Override
    public void initToolBarOnClick() {
        super.initToolBarOnClick();
        TextView title = findViewById(R.id.toolbar_title);
        View view = findViewById(R.id.file_we_file_line);
        view.setVisibility(View.INVISIBLE);
        title.setText("共享文件");
        add.setVisibility(View.INVISIBLE);
        ImageButton back = findViewById(R.id.tool_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FlowQuickPopupWindow.checkItemPosition = 0;
    }

    @Override
    public void initSearchAction(String query) {
        //

        myFileBean.setShareList(true);

        myFileBean.setKeyword(query);

        myFileBean.setPageIndex("1");

        myFileBeanList.clear();

        myFileListPresenter.doFileShareMyFileList(myFileBean);
    }

    @Override
    public void callBackFileShareMyFileList(ResultBean<List<FileShareMyFileBean>, String> resultBean) {
        //
        dialog.dismiss();
        if (resultBean == null || resultBean.getData().size() == 0) {
            //HelperView.Toast(this, "暂无更多数据!");
            if (myFileBeanList.size() == 0) {
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

                FileShareMyFileAdapter.ViewHolder holder = myFileAdapter.getViewHolderList().get(position);
                holder.item_down_btn.setVisibility(View.VISIBLE);

                holder.checkBox.setVisibility(View.GONE);
                holder.down_ok.setVisibility(View.GONE);

                boolean exists = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());

                if (exists) {
                    holder.item_down_btn.setVisibility(View.GONE);
                    holder.down_ok.setVisibility(View.VISIBLE);
                }

                holder.item_down_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectMap.clear();
                        mSelectMap.put(position, myFileBeanList.get(position).getF_FileUrl());
                        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                        if (EasyPermissions.hasPermissions(FileShareWeFileActivity.this, permissions)) {
                            startQueueDownload();
                        } else {
                            requestPermissionsCallBack(FileShareWeFileActivity.this, "请求存储权限", 12, permissions, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (Success && requestCode == 12) {
                                        startQueueDownload();
                                    }
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked, int position) {
                //
            }
        });

        myFileAdapter.setOnItemClickListener(new FileShareMyFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (myFileBeanList.size() == 0) {
                    ToastUtils.showLong("网络异常,请稍后再试!");
                    return;
                }

                boolean isExist = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());

                String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(position).getF_FileName();

                if (isExist) {
                    isReSetData = false;
//                    if (Build.VERSION.SDK_INT >= 28) {
//                        openFileForThirdApp(newPath, myFileBeanList.get(position).getF_FileName());
//                    } else {
                        //openFileForSdk(newPath);

                    FileOutUtil.openFileReader(FileShareWeFileActivity.this,newPath);
//                    }
                } else {
                    HelperView.Toast(FileShareWeFileActivity.this, "请先下载后再查看!");
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {

//                boolean isExist = whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), myFileBeanList.get(position).getF_FileName());
//
//                final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(position).getF_FileName();
//
//                if (isExist) {
//                    final AlertDialog dialog = new AlertDialog.Builder(FileShareWeFileActivity.this).create();
//                    FileUtils.beginCameraDialog(dialog, new FileUtils.OnClickSelecedListener() {
//                        @Override
//                        public void onCancel() {
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onTake() {
//                            //1
//                            dialog.dismiss();
//                            isReSetData = false;
//                            if (Build.VERSION.SDK_INT >= 28) {
//                                openFileForThirdApp(newPath, myFileBeanList.get(position).getF_FileName());
//                            } else {
//                                openFileForSdk(newPath);
//                            }
//                        }
//
//                        @Override
//                        public void onNative() {
//                            //3
//                            dialog.dismiss();
//                            List<String> idList = new ArrayList<>();
//                            idList.add(myFileBeanList.get(position).getID());
//
//                            FileUtils.fileDelete(FileShareWeFileActivity.this, idList, new FileUtils.IFileDeleteCallBack() {
//                                @Override
//                                public void onResult(boolean result) {
//                                    if (result) {
//                                        //FileShareMyFileAdapter.radioState.clear()
//                                        currentPosition = 1;
//                                        myFileBeanList.clear();
//                                        myFileBean.setPageIndex("1");
//                                        myFileListPresenter.doFileShareMyFileList(myFileBean);
//
//                                    } else {
//                                        HelperView.Toast(FileShareWeFileActivity.this, "删除失败,请稍后再试!");
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onEditor() {
//                            //2
//                            dialog.dismiss();
//                            isReSetData = true;
//                            Intent intent = new Intent(FileShareWeFileActivity.this, FileShareEditSaveActivity.class);
//                            intent.putExtra("id", projectId);
//                            intent.putExtra("fileId", myFileBeanList.get(position).getID());
//                            intent.putExtra("typeId", myFileBeanList.get(position).getF_FileTypeID());
//                            intent.putExtra("title", myFileBeanList.get(position).getF_Title());
//                            startActivity(intent);
//                        }
//                    });
//                }

                return true;
            }
        });

        myFileAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myFileAdapter);
    }

    @Override
    public void startQueueDownload() {
        //
        FileUtils.initQueueDownload(mSelectMap, new FileUtils.FileDownloadClick() {
            @Override
            public void onStart(BaseDownloadTask task) {
                int flag = (int) task.getTag();
                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).item_down_btn.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.VISIBLE);
                    myFileAdapter.getViewHolderList().get(flag).loading.startAnim(500);
                }
            }

            @Override
            public void onCompleted(BaseDownloadTask task) {
                int flag = (int) task.getTag();
                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).loading.stopAnim();
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).item_down_btn.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).down_ok.setVisibility(View.VISIBLE);
                }

                String downloadPath = myFileBeanList.get(flag).getF_FileUrl();
                String oldPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + downloadPath.substring(downloadPath.lastIndexOf("/") + 1);
                String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + myFileBeanList.get(flag).getF_FileName();
                renameFile(oldPath, newPath);
            }

            @Override
            public void onError(BaseDownloadTask task) {
                int flag = (int) task.getTag();

                if (flag <= myFileAdapter.getViewHolderList().size() - 1) {
                    myFileAdapter.getViewHolderList().get(flag).item_down_btn.setVisibility(View.VISIBLE);
                    myFileAdapter.getViewHolderList().get(flag).checkBox.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).loading.stopAnim();
                    myFileAdapter.getViewHolderList().get(flag).loading.setVisibility(View.GONE);
                    myFileAdapter.getViewHolderList().get(flag).down_ok.setVisibility(View.GONE);
                }
                HelperView.Toast(FileShareWeFileActivity.this, "下载出错，请稍后再试!");
            }
        });
    }

    @Override
    public void initFirstData() {

        if (isReSetData) {
            // 请求我的文件列表
            myFileListPresenter = FileSharePresenterFactory.getFileShareMyFilePresenterImpl(this);

            myFileBean = new FileShareMyFileInputBean();
            myFileBean.setToken(HelperTool.getToken());
            myFileBean.setProjectId(getIntent().getStringExtra("id"));
            myFileBean.setTypeId(fileTypeId);
            currentPosition = 1;
            myFileBean.setShareList(true);
            myFileBean.setPageIndex(String.valueOf(currentPosition));
            myFileBean.setPageSize("20");
            myFileBeanList.clear();

            myFileListPresenter.doFileShareMyFileList(myFileBean);
        }
    }
}

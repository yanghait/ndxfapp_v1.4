package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareFileTypePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.impl.FileSharePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileOptionUtil;
import com.youngfeng.snake.annotations.EnableDragToClose;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@EnableDragToClose
public class FileTypeListActivity extends AppCompatActivity implements IBaseView, IFileShareFileTypePresenter.IFileShareFileTypeView {

    private String projectId;

    public IFileShareFileTypePresenter presenter;

    private RecyclerView mRecyclerView;

    private CommonAdapter adapter;

    private LinkedHashMap<String, Boolean> boxChecked = new LinkedHashMap<>();

    private PopupWindow windowBottom;

    private View showView;

    List<FileShareFileTypeBean> typeBeanList = new ArrayList<>();

    private int checkedNum = 0;

    List<ViewHolder> holderList = new ArrayList<>();

    private int newPosition = 0;

    private FileShareFileTypeInputBean inputBean;

    private boolean isShowSearch = false;

    private boolean isSearchText = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_type_list);

        projectId = getIntent().getStringExtra("id");

        initRecyclerView();

        initToolBarOnClick();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setBarLintColor();

    }

    protected void setBarLintColor() {
        // 默认状态栏使用ToolBar的颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tool_bar), 0);
    }

    private void initRecyclerView() {

        mRecyclerView = findViewById(R.id.file_type_list_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_for_file));
//        mRecyclerView.addItemDecoration(div);
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {

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
                //isShow = false;
                //backgroundAlpha(1f);
            }
        });
        //backgroundAlpha(0.7f);
        windowBottom.showAtLocation(findViewById(R.id.file_type_main), Gravity.BOTTOM, 0, 0);
    }

    public void initToolBarOnClick() {
        ImageButton back = findViewById(R.id.tool_back);

        TextView title = findViewById(R.id.toolbar_title);

        ImageButton addFile = findViewById(R.id.file_share_add_img);

        ImageButton searchButton = findViewById(R.id.file_share_search_img);

        final LinearLayout file_type_search = findViewById(R.id.file_type_search);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        file_type_search.clearFocus();

        SearchView searchView = findViewById(R.id.search_view);

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inputBean = new FileShareFileTypeInputBean();
                inputBean.setToken(HelperTool.getToken());
                inputBean.setProjectId(projectId);
                if (!StringUtils.isEmpty(query)) {
                    inputBean.setKeyword(query);
                }
                presenter.doFileShareFileType(inputBean);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (StringUtils.isEmpty(newText)) {
                    if (isSearchText) {
                        isSearchText = false;
                    } else {
                        inputBean.setToken(HelperTool.getToken());
                        inputBean.setProjectId(projectId);
                        inputBean.setKeyword("");
                        presenter.doFileShareFileType(inputBean);
                    }
                }
                return true;
            }
        });
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(androidx.appcompat.R.id.submit_area).setBackground(null);
        searchView.onActionViewExpanded();

        title.setText("文件分类管理");

        title.requestFocus();

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FileTypeListActivity.this, FileTypeEditorActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("optionId", 1);
                startActivity(intent);
            }
        });
    }

    public void doRequestForMyFile() {
        // 请求文件分类列表
        presenter = FileSharePresenterFactory.getFileShareFileTypePresenterImpl(this);
        inputBean = new FileShareFileTypeInputBean();
        inputBean.setToken(HelperTool.getToken());
        inputBean.setProjectId(projectId);
        presenter.doFileShareFileType(inputBean);

    }

    @Override
    public void callBackFileShareFileType(final ResultBean<List<FileShareFileTypeBean>, String> resultBean) {
        if (resultBean == null || resultBean.getData().size() == 0) {
            return;
        }

        typeBeanList.clear();

        holderList.clear();

        typeBeanList.addAll(resultBean.getData());

        adapter = new CommonAdapter<FileShareFileTypeBean>(this, R.layout.item_file_share_my_file, typeBeanList) {
            @Override
            protected void convert(ViewHolder holder, FileShareFileTypeBean fileTypeBean, final int position) {

                holderList.add(holder);

                LinearLayout linearLayout = holder.getView(R.id.linear);
                linearLayout.setVisibility(View.INVISIBLE);

                TextView textView = holder.getView(R.id.item_file_share_file_type);

                textView.setVisibility(View.INVISIBLE);

                TextView time = holder.getView(R.id.item_file_share_time);

                time.setVisibility(View.INVISIBLE);

                ImageView typeImage = holder.getView(R.id.item_file_share_img);

                typeImage.setBackground(getResources().getDrawable(R.mipmap.file_type_img));

                TextView remark = holder.getView(R.id.item_file_share_upload);

                TextView typeName = holder.getView(R.id.item_file_share_file_name);

                final CheckBox checkBox = holder.getView(R.id.item_file_share_radio);

                remark.setText(fileTypeBean.getF_Describe());

                typeName.setText(fileTypeBean.getF_Name());

                // 处理标题过长的问题

                int okWidth = ScreenUtil.dp2px(FileTypeListActivity.this, 250f);

                // 测量TextView 的实际显示宽度 超过指定宽度后打点
                CharSequence s1 = TextUtils.ellipsize(typeName.getText(),typeName.getPaint(), okWidth, TextUtils.TruncateAt.END);
                CharSequence s2 = TextUtils.ellipsize(remark.getText(), remark.getPaint(), okWidth, TextUtils.TruncateAt.END);

                typeName.setText(s1);
                remark.setText(s2);

                checkBox.setChecked(false);

                checkedNum = 0;

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (position >= typeBeanList.size()) {
                            return;
                        }
                        if (checked) {
                            boxChecked.put(typeBeanList.get(position).getID(), true);
                        }

                        if (checked) {
                            checkedNum++;
                            newPosition = position;
                            if (checkedNum == 1) {
                                showPopupForBottom(showView);
                            } else {
                                Set<String> entitySet = boxChecked.keySet();
                                for (String key : entitySet) {
                                    if (boxChecked.get(key) && !key.equals(typeBeanList.get(position).getID())) {
                                        for (int i = 0; i < typeBeanList.size(); i++) {
                                            if (key.equals(typeBeanList.get(i).getID())) {
                                                ((CheckBox) holderList.get(i).getView(R.id.item_file_share_radio)).setChecked(false);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            checkedNum--;
                            if (checkedNum < 0) {
                                checkedNum = 0;
                            }
                            if (checkedNum == 0 && windowBottom.isShowing()) {
                                windowBottom.dismiss();
                            }
                        }
                    }
                });

                if (showView == null) {
                    showView = getLayoutInflater().inflate(R.layout.popupwindow_for_file_type, null);
                }

                LinearLayout delete_view = showView.findViewById(R.id.popup_dele_view);

                delete_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (newPosition >= 0 && !StringUtils.isEmpty(typeBeanList.get(newPosition).getID())) {
                            FileOptionUtil.fileTypeDelete(FileTypeListActivity.this, typeBeanList.get(newPosition).getID(), new FileOptionUtil.IFileTypeDeleteCallBack() {
                                @Override
                                public void onResult(boolean result, String content) {
                                    if (result) {
                                        windowBottom.dismiss();
                                        checkedNum = 0;
                                        boxChecked.clear();
                                        typeBeanList.remove(newPosition);
                                        holderList.remove(newPosition);
                                        adapter.notifyDataSetChanged();
                                        doRequestForMyFile();
                                    } else {
                                        HelperView.Toast(FileTypeListActivity.this, content);
                                    }
                                }
                            });
                        }


                    }
                });
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                Intent intent = new Intent(FileTypeListActivity.this, FileTypeEditorActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("optionId", 2);
                intent.putExtra("typeId", typeBeanList.get(position).getID());
                intent.putExtra("title", typeBeanList.get(position).getF_Name());
                intent.putExtra("remark", typeBeanList.get(position).getF_Describe());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (windowBottom != null) {
            boxChecked.clear();
            adapter.notifyDataSetChanged();
            checkedNum = 0;
            windowBottom.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doRequestForMyFile();
    }
}

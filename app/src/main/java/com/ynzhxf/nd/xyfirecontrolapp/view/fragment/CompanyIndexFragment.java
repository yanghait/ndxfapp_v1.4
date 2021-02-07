package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.DangerProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchForHomeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CompanyIndexFragment extends CompetentIndexFragment  {

    public static CompanyIndexFragment newInstance() {
        return new CompanyIndexFragment();
    }

    @Override
    public void init(View view) {

        ImageView fire_gis_img = view.findViewById(R.id.charge_gis);
        TextView fire_gis_text = view.findViewById(R.id.fire_gis_text);

        ImageView video_img = view.findViewById(R.id.charge_video);
        TextView video_text = view.findViewById(R.id.video_conference_text);

        fire_gis_img.setImageDrawable(context.getResources().getDrawable(R.drawable.com_manage_two));
        fire_gis_text.setText("维保管理");

        video_img.setImageDrawable(context.getResources().getDrawable(R.drawable.com_file_share_two));
        video_text.setText("文件分享");

        accurate_control.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_flag));
        accurate_control_text.setText(" 异常项目");

        //联网项目
        menuNetproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), CompanySearchForHomeActivity.class);
                startActivity(intent1);
            }
        });
        //通知公告
        menuNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsListActivity.class);
                startActivity(intent);
            }
        });

        // 文件分享
        videoConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGetProjectData("");
            }
        });

        // 维保管理
        fireGis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(getActivity(), MaintenanceCompanyActivity.class);
                MaintenanceCompanyActivity.detailType = 2;
                ProjectNodeBean nodeBean1 = new ProjectNodeBean();
                nodeBean1.setID(null);
                intent2.putExtra("data", nodeBean1);
                startActivity(intent2);
            }
        });

        // 无查看更多
        view_More.setVisibility(View.INVISIBLE);

        //组织架构名称
        txtOrgName.setText(datas.getLoginInfoBean().getOrgName());
        dangeroursUserProjectPersenter.doDangeroursUserProject();
        userMaxEventCountPersenter.doUserMaxEventCount();

        //设置相关属性
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setContentDescription("事件统计");
        mChart.getDescription().setEnabled(false);
        mChart.setNoDataText("加载中....");
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getAxisRight().setEnabled(false);  //不绘制右侧轴线
    }

    @Override
    public void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                precisionManagementView.setLayoutManager(manager);
                DangerProjectAdapter adapter = new DangerProjectAdapter(result.getData());
                adapter.setLoginType(4);
                precisionManagementView.setAdapter(adapter);

                if (result.getData().size() > 0) {
                    ProjectNodeBean bean = result.getData().get(0);
                    SPUtils.getInstance().put("CompanyProjectId", bean.getID());
                    SPUtils.getInstance().put("CompanyProjectName", bean.getName());
                    SPUtils.getInstance().put("ProjectNum",result.getData().size());
                }
            } else {
                String msg = "精准治理" + result.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, msg);
            }
        } catch (Exception e) {
            HelperView.Toast(context, "精准治理失败：" + e.getMessage());
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_com_internet:
                Intent intent1 = new Intent(getActivity(), CompanySearchForHomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_com_manage:
                Intent intent2 = new Intent(getActivity(), MaintenanceCompanyActivity.class);
                MaintenanceCompanyActivity.detailType = 2;
                ProjectNodeBean nodeBean1 = new ProjectNodeBean();
                nodeBean1.setID(null);
                intent2.putExtra("data", nodeBean1);
                startActivity(intent2);
                break;
            case R.id.menu_com_file_share:
                initGetProjectData("");
                break;
            case R.id.menu_com_message:
                Intent intent = new Intent(getActivity(), NewsListActivity.class);
                startActivity(intent);
                break;
        }
    }*/

    public void initGetProjectData(String query) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("keyword", query);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("提示");
        dialog.setMessage("加载中...");
        dialog.show();
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_SEARCH_PROJECT))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(getActivity(), e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("data");
                            final List<SearchProjectBean> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchProjectBean>>() {
                            }.getType());
                            if (list != null && list.size() > 0) {

                                Intent intent3 = new Intent(getActivity(), FileShareHomeActivity.class);
                                intent3.putExtra("id", list.get(0).getID());
                                intent3.putExtra("Name", list.get(0).getName());
                                startActivity(intent3);

                            } else {
                                HelperView.Toast(getActivity(), "未查找到项目!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(getActivity(), e.getMessage());
                        }
                    }
                });
    }
}

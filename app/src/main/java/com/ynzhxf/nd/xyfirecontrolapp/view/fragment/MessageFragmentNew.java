package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.event.MessageEvent;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.MessageTypeListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.message.AlarmMessageListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 消息处理
 */
public class MessageFragmentNew extends BaseFragment {

    private RecyclerView recyclerView;

    private List<ViewHolder> holderList = new ArrayList<>();

    private List<MessageTypeListBean> messageBean = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_new, null, false);

        recyclerView = view.findViewById(R.id.message_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        //LogUtils.showLoge("数据应用下载文件路径000---", PathUtils.getExternalAppDocumentsPath());
        return view;
    }

    public static MessageFragmentNew newInstance() {
        return new MessageFragmentNew();
    }

    @Override
    public void onResume() {
        super.onResume();
        holderList.clear();
        messageBean.clear();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event) {

        holderList.clear();
        messageBean.clear();
        initData();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initMessage(String typeId) {
        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("pageIndex", "1");

        params.put("MsgTypeId", typeId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_USER_MESSAGE_LOG_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("请求消息列表11212---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("请求消息列表8888---", response);

                    }
                });
    }

    private void getMessageCount() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("Version", "1.1.0");

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_USER_MESSAGE_NOTSEE_COUNT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //LogUtils.showLoge("请求未读消息数11212---", e.getMessage());
                        HelperView.Toast(getActivity(), e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("请求未读消息数11212---", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            List<Integer> countList = new Gson().fromJson(json.getJSONArray("data").toString(), new TypeToken<List<Integer>>() {
                            }.getType());
                            if (countList != null && messageBean.size() > 0) {
                                for (int i = 0; i < messageBean.size(); i++) {

                                    TextView messageCount = holderList.get(i).getView(R.id.message_layout_msg_count);

                                    int num = Integer.parseInt(messageBean.get(i).getValue());

                                    if (num <= countList.size() - 1) {
                                        int count = countList.get(num);
                                        if (count > 0 && count > 99) {
                                            messageCount.setVisibility(View.VISIBLE);
                                            messageCount.setText(String.valueOf("99+"));

                                        } else if (count > 0) {
                                            messageCount.setVisibility(View.VISIBLE);
                                            messageCount.setText(String.valueOf(count));
                                        } else {
                                            messageCount.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initData() {
        if (StringUtils.isEmpty(HelperTool.getToken())) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_GET_MESSAGE_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //LogUtils.showLoge("请求消息分类11212---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("请求消息分类88888---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (!json.getBoolean("success")) {
                                HelperView.Toast(getActivity(), json.getString("message"));
                                return;
                            } else {
                                final List<MessageTypeListBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<MessageTypeListBean>>() {
                                        }.getType());

                                if (beanList == null || beanList.size() == 0) {
                                    HelperView.Toast(getActivity(), "暂无消息!");
                                    return;
                                }

                                for (MessageTypeListBean bean : beanList) {
                                    SPUtils.getInstance().put(bean.getID(), URLConstant.URL_BASE1 + bean.getUploadPath());
                                }
                                messageBean.addAll(beanList);

                                CommonAdapter adapter = new CommonAdapter<MessageTypeListBean>(getActivity(), R.layout.fragment_item_message, beanList) {
                                    @Override
                                    protected void convert(ViewHolder holder, MessageTypeListBean bean, int position) {

                                        holderList.add(holder);

                                        ImageView img = holder.getView(R.id.message_layout_img);

                                        TextView title = holder.getView(R.id.message_layout_text);

                                        TextView messageCount = holder.getView(R.id.message_layout_msg_count);

                                        Picasso.get().load(URLConstant.URL_BASE1 + bean.getUploadPath()).into(img);

                                        title.setText(bean.getName());

//                                        if ("0".equals(bean.getValue())) {
//                                            messageCount.setVisibility(View.INVISIBLE);
//                                        } else {
//                                            messageCount.setVisibility(View.VISIBLE);
//
//                                            messageCount.setText(bean.getValue());
//                                        }
                                        messageCount.setVisibility(View.INVISIBLE);
                                    }
                                };

                                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        Intent intent = new Intent(getActivity(), AlarmMessageListActivity.class);
                                        intent.putExtra("TypeId", beanList.get(position).getValue());
                                        intent.putExtra("MessageType", beanList.get(position).getID());
                                        intent.putExtra("url", URLConstant.URL_BASE1 + beanList.get(position).getUploadPath());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        return false;
                                    }
                                });

                                getMessageCount();

                                recyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

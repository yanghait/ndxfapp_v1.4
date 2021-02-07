package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by nd on 2018-08-02.
 */

public class AlarmMessageAdapter extends RecyclerView.Adapter<AlarmMessageAdapter.ViewHolder> {
    private List<UserMessagePushLogBean> messageList;

    private IAlarmMessageClick alarmMessageClick;

    private String messageType;

    private Activity activity;

    private String url;

    public AlarmMessageAdapter(Activity activity,String url, List<UserMessagePushLogBean> alarmList, String messageType, IAlarmMessageClick alarmMessageClick) {
        this.alarmMessageClick = alarmMessageClick;
        this.messageList = alarmList;
        this.messageType = messageType;
        this.activity = activity;
        this.url = url;
    }

    //报警消息点击页面
    public interface IAlarmMessageClick {
        void AlarmMessageClick(UserMessagePushLogBean obj);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View contanter, mark;
        TextView txtAlarmPostion, txtPushTime, timeDate;
        ImageView icon;

        ViewGroup mParent;


        public ViewHolder(View view,ViewGroup parent) {
            super(view);
            contanter = view.findViewById(R.id.container);
            txtAlarmPostion = view.findViewById(R.id.txt_alarm_postion);
            txtPushTime = view.findViewById(R.id.text_alarm_time);
            mark = view.findViewById(R.id.txt_has_see);
            icon = view.findViewById(R.id.item_message_msg_icon);
            timeDate = view.findViewById(R.id.item_message_time);
            mParent = parent;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_message, parent, false);
        ViewHolder holder = new ViewHolder(view,parent);
        View queryView = view.findViewById(R.id.container);
        queryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMessagePushLogBean query = (UserMessagePushLogBean) v.getTag();
                alarmMessageClick.AlarmMessageClick(query);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserMessagePushLogBean queryBean = messageList.get(position);
        holder.contanter.setTag(queryBean);

//        String titleContent;
//        if (queryBean.getAppPushMsgLogObj().getMessageTitle().length() >= 9) {
//            titleContent = queryBean.getAppPushMsgLogObj().getMessageTitle().substring(0, 9) + "...";
//        } else {
//            titleContent = queryBean.getAppPushMsgLogObj().getMessageTitle();
//        }
//        holder.txtAlarmPostion.setText(titleContent);//queryBean.getAppPushMsgLogObj().getMessageContent()
//
//        String messageContent;
//        if (queryBean.getAppPushMsgLogObj().getMessageContent().length() >= 9) {
//            messageContent = queryBean.getAppPushMsgLogObj().getMessageContent().substring(0, 9) + "...";
//        } else {
//            messageContent = queryBean.getAppPushMsgLogObj().getMessageContent();
//        }
//        holder.txtPushTime.setText(messageContent);//queryBean.getLastPushTimeStr()

        holder.txtAlarmPostion.setText(queryBean.getAppPushMsgLogObj().getMessageTitle());

        holder.txtPushTime.setText(queryBean.getAppPushMsgLogObj().getMessageContent());

        // 测试内容实际显示长度

        int okWidth = ScreenUtil.dp2px(holder.mParent.getContext(), 200f);

        // 测量TextView 的实际显示宽度 超过指定宽度后打点
        CharSequence s1 = TextUtils.ellipsize(holder.txtAlarmPostion.getText(), holder.txtAlarmPostion.getPaint(), okWidth, TextUtils.TruncateAt.END);
        CharSequence s2 = TextUtils.ellipsize(holder.txtPushTime.getText(), holder.txtPushTime.getPaint(), okWidth, TextUtils.TruncateAt.END);

        holder.txtAlarmPostion.setText(s1);
        holder.txtPushTime.setText(s2);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            long time = formatter.parse(queryBean.getLastPushTimeStr()).getTime();

            String nowTime = HelperTool.MillTimeToYearMonth(time);

            holder.timeDate.setText(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("69".equals(messageType)) {
            holder.icon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.msg_alarm));
        } else if ("72".equals(messageType)) {
            holder.icon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.msg_operation));
        } else if ("71".equals(messageType)) {
            holder.icon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.msg_mainten));
        }
        if (queryBean.isHasSee()) {
            holder.mark.setVisibility(View.GONE);
        } else {
            holder.mark.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(SPUtils.getInstance().getString(messageType)).error(R.drawable.img_load).into(holder.icon);
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

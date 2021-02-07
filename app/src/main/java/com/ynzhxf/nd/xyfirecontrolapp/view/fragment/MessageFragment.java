package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.message.AlarmMessageListActivity;

/**
 * 消息
 * Created by nd on 2018-07-11.
 */

public class MessageFragment extends BaseFragment {
    /**
     * 全局缓存数据
     */
    private GloblePlantformDatas datas;

    //平台通知节点
    private View lyMessageNote;
    //平台消息通知数量结果
    private TextView txtNoteCount;
    //用户报警消息通知
    private View lyMessageAlarm;
    //用户报警消息数量
    private TextView txtAlarm;

    //线程是否需要继续运行
    private boolean isRun = true;
    //线程是否暂停
    private boolean isPause = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        datas = GloblePlantformDatas.getInstance();
        View view = inflater.inflate(R.layout.fragment_message, null, false);
        lyMessageNote = view.findViewById(R.id.ly_msg_note);
        txtNoteCount = view.findViewById(R.id.txt_user_note_count);
        lyMessageAlarm = view.findViewById(R.id.ly_msg_alarm);
        txtAlarm = view.findViewById(R.id.txt_user_alarm_count);
        datas = GloblePlantformDatas.getInstance();
        init();
        messageCountThread.start();
        return view;
    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 初始化界面
     */
    private void init() {
        lyMessageNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lyMessageAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlarmMessageListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                if (GloblePlantformDatas.UserNotSeeCount > 0) {
                    int queryCount = GloblePlantformDatas.UserNotSeeCount;
                    if (queryCount > 10) {
                        txtAlarm.setText("");
                    } else {
                        txtAlarm.setText(queryCount + "");
                    }

                    txtAlarm.setVisibility(View.VISIBLE);
                } else {
                    txtAlarm.setVisibility(View.GONE);
                }
            }
        }
    };

    private Thread messageCountThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (isRun) {
                try {
                    Thread.sleep(1000);
                    if (!isPause) {
                        handler.sendEmptyMessage(0x123);
                    }
                } catch (Exception e) {

                }
            }
        }
    });
}

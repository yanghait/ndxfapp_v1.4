package com.ynzhxf.nd.xyfirecontrolapp.util;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

/**
 * author hbzhou
 * date 2019/3/27 14:13
 */
public class DialogPopupUtil {

    public static void beginAlertDialog(final AlertDialog DIALOG, final FileUtils.OnClickInspectListener callBack) {
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
            //Button viewContent2 = window.findViewById(R.id.photodialog_btn_edit);
            //Button viewContent3 = window.findViewById(R.id.photodialog_btn_native);
            Button viewContent4 = window.findViewById(R.id.photodialog_btn_out);

            viewContent1.setText("拍照");
            viewContent4.setText("拍视频");
//            viewContent2.setText("只看正常");
//            viewContent3.setText("查看全部");

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
//            viewContent2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callBack.onButtonTwo();
//                }
//            });
//            viewContent3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callBack.onButtonThree();
//                }
//            });
            viewContent4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onButtonFour();
                }
            });
        }
    }
}

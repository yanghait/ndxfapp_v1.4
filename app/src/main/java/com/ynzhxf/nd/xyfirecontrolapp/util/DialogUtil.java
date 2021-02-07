package com.ynzhxf.nd.xyfirecontrolapp.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;


/**
 * @author hbzhou
 */

public class DialogUtil {

    public static void showErrorMessage(Context context, String errorMessage, final IComfirmListener listener) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.loan_dialog_error_message, null);
        TextView tvErroeMessage = (TextView) view.findViewById(R.id.tv_hint_message);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvErroeMessage.setText(errorMessage);
        dialog.setCanceledOnTouchOutside(false);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 70);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.show();
    }

    public static void showSelectMessage(Context context, String errorMessage, final IComfirmCancelListener listener) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_message_select, null);
        TextView tvErrorMessage =  view.findViewById(R.id.tv_hint_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvErrorMessage.setText(errorMessage);
        dialog.setCanceledOnTouchOutside(false);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 70);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.show();
    }

    public static void showSelectMessageForButton(Context context,String cancelText,String confirmText, String errorMessage, final IComfirmCancelListener listener) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_message_select, null);
        TextView tvErrorMessage =  view.findViewById(R.id.tv_hint_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvErrorMessage.setText(errorMessage);
        dialog.setCanceledOnTouchOutside(false);

        tvConfirm.setText(confirmText);
        tvCancel.setText(cancelText);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 70);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.show();
    }

    public static void showErrorMessage(Context context, String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.loan_dialog_error_message, null);
        TextView tvErroeMessage = (TextView) view.findViewById(R.id.tv_hint_message);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvErroeMessage.setText(errorMessage);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 70);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.show();
    }

    public static void showDialogMessage(Context context, String errorMessage, final IComfirmListener callBack) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.loan_dialog_error_message, null);
        TextView tvErroeMessage = view.findViewById(R.id.tv_hint_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        tvErroeMessage.setText(errorMessage);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callBack.onConfirm();
            }
        });
        int width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 70);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.setCancelable(false);
        dialog.show();
    }

    public interface IComfirmListener {
        void onConfirm();
    }

    public interface IComfirmCancelListener {
        void onConfirm();

        void onCancel();
    }
}

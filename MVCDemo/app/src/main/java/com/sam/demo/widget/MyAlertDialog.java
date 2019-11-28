package com.sam.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sam.demo.R;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/16
 * 描    述：AlertDialog
 * 修订历史：
 * ================================================
 */

public class MyAlertDialog extends Dialog {


    public MyAlertDialog(Context context) {
        super(context);
    }

    public MyAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private OnClickLitener onPositiveClickLitener;
        private OnClickLitener onNegativeClickLitener;
        private String positiveButtonText;
        private String negativeButtonText;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickLitener onPositiveClickLitener) {
            this.positiveButtonText = positiveButtonText;
            this.onPositiveClickLitener = onPositiveClickLitener;
            return this;
        }

        public Builder setNegativeeButton(String negativeButtonText, OnClickLitener onNegativeClickLitener) {
            this.negativeButtonText = negativeButtonText;
            this.onNegativeClickLitener = onNegativeClickLitener;
            return this;
        }

        public interface OnClickLitener {
            void OnClickLitener(View view);

        }

        public MyAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MyAlertDialog dialog = new MyAlertDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.dialog_myalertdialog, null);
            ((TextView) layout.findViewById(R.id.tv_mydialog_title)).setText(title);
            ((TextView) layout.findViewById(R.id.tv_mydialog_message)).setText(message);
            Button btNegativee = ((Button) layout.findViewById(R.id.bt_mydialog_negativee));
            Button btPositive = ((Button) layout.findViewById(R.id.bt_mydialog_positive));
            if(TextUtils.isEmpty(negativeButtonText)){
                btNegativee.setVisibility(View.GONE);
            }else {
                btNegativee.setText(negativeButtonText);
                btNegativee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(onNegativeClickLitener!=null)
                        onNegativeClickLitener.OnClickLitener(v);
                    }
                });
            }
            if(TextUtils.isEmpty(positiveButtonText)){
                btPositive.setVisibility(View.GONE);
            }else {
                btPositive.setText(positiveButtonText);
                btPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(onPositiveClickLitener!=null)
                        onPositiveClickLitener.OnClickLitener(v);
                    }
                });
            }

            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            assert window != null;
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

    }

}

package com.ynzhxf.nd.xyfirecontrolapp.adapter.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ynzhxf.nd.xyfirecontrolapp.R;

import java.util.List;

public class FileTypeAdapter extends BaseAdapter {
    private Context context;

    private List<String> list;

    public static int checkItemPosition = 0;

    public OnClickSelectListener callBack;


    public void setCheckItem(int position) {

        checkItemPosition = position;

        notifyDataSetChanged();

    }


    public FileTypeAdapter(Context context, List<String> list, OnClickSelectListener callBack) {

        this.context = context;

        this.list = list;

        this.callBack = callBack;

    }


    @Override

    public int getCount() {

        return list.size();

    }


    @Override

    public Object getItem(int position) {

        return null;

    }


    @Override

    public long getItemId(int position) {

        return position;

    }


    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView != null) {

            viewHolder = (ViewHolder) convertView.getTag();

        } else {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_file_type, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        }

        fillValue(position, viewHolder);

        return convertView;

    }


    private void fillValue(final int position, final ViewHolder viewHolder) {

        viewHolder.mText.setText(list.get(position));

        if (checkItemPosition != -1) {

            viewHolder.mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkItemPosition = position;
                    notifyDataSetChanged();
                    callBack.onClick(view, position);
                }
            });
            if (checkItemPosition == position) {

                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.primary_text_color));

                viewHolder.mText.setBackgroundResource(R.drawable.check_bg);

            } else {

                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.gray));

                viewHolder.mText.setBackgroundResource(R.drawable.uncheck_bg);

            }
        }
    }


    static class ViewHolder {

        Button mText;

        ViewHolder(View view) {

            mText = view.findViewById(R.id.file_item_text);
        }
    }

    public interface OnClickSelectListener {
        void onClick(View v, int position);
    }
}

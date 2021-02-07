package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CircularDraw;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MeterValueDraw;
import com.ynzhxf.nd.xyfirecontrolapp.ui.RotateImage;
import com.ynzhxf.nd.xyfirecontrolapp.ui.WaterImageAnimation;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备标签结构列表数据适配器
 * <p>
 * Created by nd on 2018-07-23.
 */

public class EquipmentLabelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //设备
    private static final int EQUIPMENT = 0;

    //模拟量
    private static final int LABEL_ANALOGO = 3;

    //开关量
    private static final int LABEL_DIGITAL = 1;

    //布尔量
    private static final int LABEL_STATE = 2;

    private List<LabelNodeBean> labelList;

    //保存一个列表，方便更新数据,持有labellist的引用
    private Map<String, LabelNodeBean> labelMap;


    //点击历史趋势按钮回调
    private ILableInfoClick lableInfoClick;

    //点击启停控制按钮回调
    ILabelControlClick labelControlClick;

    private Context mContext;


    public EquipmentLabelAdapter(Context mContext, List<LabelNodeBean> labelList, ILableInfoClick lableInfoClick, ILabelControlClick labelControlClick) {
        this.labelList = labelList;
        this.lableInfoClick = lableInfoClick;
        this.labelControlClick = labelControlClick;
        this.labelMap = new HashMap<>();
        this.mContext = mContext;
        for (LabelNodeBean queryBean : labelList) {
            labelMap.put(queryBean.getID(), queryBean);
        }
    }

    @Override
    public int getItemViewType(int position) {
        LabelNodeBean query = labelList.get(position);
        if (query.getNodeLevel() == 7) {
            if (query.getTagValueType().getID().equals("1")) {
                return LABEL_DIGITAL;
            }
            if (query.getTagValueType().getID().equals("2")) {
                return LABEL_STATE;
            }
            if (query.getTagValueType().getID().equals("3")) {
                return LABEL_ANALOGO;
            }
        }
        return EQUIPMENT;
    }

    /**
     * 标签历史趋势点击回调
     */
    public interface ILableInfoClick {
        void LableInfoClick(LabelNodeBean labelNodeBean);
    }

    public interface ILabelControlClick {
        void LabelControlClick(LabelNodeBean labelNodeBean);
    }

    class BaseLabelViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtTranVlaue;

        public TextView txtNameRect;
        public View btnDetail;

        public BaseLabelViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_name);
            txtTranVlaue = view.findViewById(R.id.txt_tran_value);
            btnDetail = view.findViewById(R.id.btn_detail);
            txtNameRect = view.findViewById(R.id.txt_name_rect);
        }
    }

    /**
     * 设备项
     */
    public class ViewHolderEquipemnt extends BaseLabelViewHolder {
        public ViewHolderEquipemnt(View view) {
            super(view);
        }
    }

    /**
     * 标签开关量
     */
    public class ViewHolderLabelDigital extends BaseLabelViewHolder {
        Switch swControl;
        RotateImage rotateImage;

        public ViewHolderLabelDigital(View view) {
            super(view);
            swControl = view.findViewById(R.id.sw_control);
            rotateImage = view.findViewById(R.id.ri_img);
        }
    }

    /**
     * 标签布尔量
     */
    public class ViewHolderLabelState extends BaseLabelViewHolder {
        RotateImage rotateImage;
        ImageView imgBack;

        public ViewHolderLabelState(View view) {
            super(view);
            rotateImage = view.findViewById(R.id.ri_img);
        }
    }

    /**
     * 标签布模拟量
     */
    public class ViewHolderLabelAnalogo extends BaseLabelViewHolder {
        TextView txtStandar, txtOffset;
        MeterValueDraw draw;
        CircularDraw cdDraw;
        View fl_shui;
        WaterImageAnimation waterImageAnimation;

        public ViewHolderLabelAnalogo(View view) {
            super(view);
            txtOffset = view.findViewById(R.id.txt_offset);
            txtStandar = view.findViewById(R.id.txt_standar_value);
            draw = view.findViewById(R.id.v_value);
            cdDraw = view.findViewById(R.id.cd_offeset);
            fl_shui = view.findViewById(R.id.fl_shui);
            waterImageAnimation = view.findViewById(R.id.img_water);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = null;
        BaseLabelViewHolder holder = null;
        switch (viewType) {
            case LABEL_DIGITAL:
                view = layout.inflate(R.layout.item_label_digital, parent, false);
                view.findViewById(R.id.sw_control).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        labelControlClick.LabelControlClick((LabelNodeBean) v.getTag());
                    }
                });
                holder = new ViewHolderLabelDigital(view);
                break;
            case LABEL_STATE:
                view = layout.inflate(R.layout.item_label_state, parent, false);
                holder = new ViewHolderLabelState(view);
                break;
            case LABEL_ANALOGO:
                view = layout.inflate(R.layout.item_label_analog, parent, false);
                holder = new ViewHolderLabelAnalogo(view);
                break;
            default:
                view = layout.inflate(R.layout.item_label_equipment, parent, false);
                holder = new ViewHolderEquipemnt(view);
        }
        if (viewType != EQUIPMENT) {
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lableInfoClick.LableInfoClick((LabelNodeBean) v.getTag());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LabelNodeBean queryObj = this.labelList.get(position);

        BaseLabelViewHolder queryBaseHolder = (BaseLabelViewHolder) holder;

        if (!StringUtils.isEmpty(queryObj.getNowAlarmType())) {
            int state = Integer.parseInt(queryObj.getNowAlarmType());
            if (state > 0) {
                int colorState = queryObj.getColorState();
                if (colorState == 0) {
                    //queryBaseHolder.txtName.setBackgroundColor(Color.parseColor("#eb5c15"));

                    queryBaseHolder.txtName.setBackground(mContext.getResources().getDrawable(R.drawable.label_alarm_tir_shape));
                    queryBaseHolder.txtNameRect.setBackground(mContext.getResources().getDrawable(R.drawable.tet));

                    setValueAnimator(queryBaseHolder.txtName);
                    setValueAnimator(queryBaseHolder.txtNameRect);
                } else {
                    //queryBaseHolder.txtName.setBackgroundColor(Color.parseColor("#3498DB"));

                    queryBaseHolder.txtName.setBackground(mContext.getResources().getDrawable(R.drawable.label_alarm_tir_no_select));
                    queryBaseHolder.txtNameRect.setBackground(mContext.getResources().getDrawable(R.drawable.tet_one));
                }
            } else {
                //queryBaseHolder.txtName.setBackgroundColor(Color.parseColor("#3498DB"));
                queryBaseHolder.txtName.setBackground(mContext.getResources().getDrawable(R.drawable.label_alarm_tir_no_select));
                queryBaseHolder.txtNameRect.setBackground(mContext.getResources().getDrawable(R.drawable.tet_one));
            }
        } else {
            //queryBaseHolder.txtName.setBackgroundColor(Color.parseColor("#3498DB"));
            queryBaseHolder.txtName.setBackground(mContext.getResources().getDrawable(R.drawable.label_alarm_tir_no_select));
            queryBaseHolder.txtNameRect.setBackground(mContext.getResources().getDrawable(R.drawable.tet_one));
        }

        if (!(holder instanceof ViewHolderEquipemnt)) {
            queryBaseHolder.txtTranVlaue.setText(queryObj.getTranslateValue());
            queryBaseHolder.btnDetail.setTag(queryObj);
        } else {
            queryBaseHolder.txtName.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        queryBaseHolder.txtName.setText(queryObj.getName());
        queryBaseHolder.itemView.setTag(queryObj);
        if (holder instanceof ViewHolderLabelDigital) {//开关量
            ViewHolderLabelDigital queryHolder = (ViewHolderLabelDigital) holder;
            int queryValue = HelperTool.objectToInt(queryObj.getRealValue());
            if (queryValue == 1) {
                queryHolder.rotateImage.canRoate = true;
                queryHolder.swControl.setChecked(true);
            } else {
                queryHolder.rotateImage.canRoate = false;
                queryHolder.swControl.setChecked(false);
            }
            queryHolder.swControl.setTag(queryObj);

        }
        if (holder instanceof ViewHolderLabelState) {//布尔量
            ViewHolderLabelState queryHolder = (ViewHolderLabelState) holder;
            //queryHolder.rotateImage.setVisibility(View.VISIBLE);
            //queryHolder.txtTranVlaue.setVisibility(View.GONE);
            if (HelperTool.objectToInt(queryObj.getRealValue()) == 1) {
                queryHolder.rotateImage.canRoate = true;
            } else {
                queryHolder.rotateImage.canRoate = false;
            }

            return;
        }
        if (holder instanceof ViewHolderLabelAnalogo) {//模拟量
            ViewHolderLabelAnalogo queryHolder = (ViewHolderLabelAnalogo) holder;
            if (queryObj.getAlalogOffset() != null) {
                queryHolder.txtStandar.setText( queryObj.getStandarLower() + queryObj.getUnit().getName() + "-" + queryObj.getStandardHight() + queryObj.getUnit().getName());
                queryHolder.txtOffset.setText( queryObj.getAlalogOffset());
                queryHolder.cdDraw.setVisibility(View.VISIBLE);
                queryHolder.cdDraw.loadData(queryObj.getAlalogOffset());
            } else {
                queryHolder.txtStandar.setText("");
                queryHolder.txtOffset.setText("-");
                queryHolder.cdDraw.setVisibility(View.INVISIBLE);
            }
            double queryTranvalue = HelperTool.objectToDouble(queryObj.getRealValue());
            double queryMaxValue = 0;
            if (queryObj.getMaxAnalogValue() != null) {
                if (queryTranvalue <= -1000) {
                    queryTranvalue = 0;
                }
                queryMaxValue = queryObj.getMaxAnalogValue();
            } else {
                double query = queryTranvalue * 0.4 + queryTranvalue;
                if (queryTranvalue <= -1000) {
                    query += 10;
                    queryTranvalue = 0;
                }
                queryMaxValue = query;
            }
            if ((queryObj.getTranslateValue() != null) && (queryObj.getTranslateValue().indexOf("mm") != -1 || queryObj.getTranslateValue().indexOf("m³") != -1)) {
                queryHolder.fl_shui.setVisibility(View.VISIBLE);
                queryHolder.draw.setVisibility(View.GONE);
                queryHolder.waterImageAnimation.loadAnimation(queryTranvalue, queryMaxValue);
            } else {
                queryHolder.fl_shui.setVisibility(View.GONE);
                queryHolder.draw.setVisibility(View.VISIBLE);
                queryHolder.draw.loadData(queryTranvalue, queryMaxValue);
            }

            return;
        }
        if (holder instanceof ViewHolderEquipemnt) {
            ViewHolderEquipemnt equipemnt = (ViewHolderEquipemnt) holder;
        }
    }

    private void setValueAnimator(View textView) {
        ValueAnimator colorAnim = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f,1f);
        colorAnim.setDuration(2000);
        colorAnim.start();
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    /**
     * 重载数据
     *
     * @param queryList
     */
    public void loadMap(List<LabelNodeBean> queryList) {
        for (LabelNodeBean query : queryList) {
            if (labelMap.containsKey((query.getID()))) {
                LabelNodeBean temp = labelMap.get(query.getID());
                temp.setRealValue(query.getRealValue());
                temp.setTranslateValue(query.getTranslateValue());
                temp.setAlalogOffset(query.getAlalogOffset());
                temp.setRealValue(query.getRealValue());
                temp.setMaxAnalogValue(query.getMaxAnalogValue());
            }
        }
    }

    public List<LabelNodeBean> getListData() {
        return labelList;
    }

    /**
     * 使用ID获取一个缓存节点
     *
     * @param ID
     * @return
     */
    public LabelNodeBean getLabelBeanByID(String ID) {
        return labelMap.get(ID);
    }
}

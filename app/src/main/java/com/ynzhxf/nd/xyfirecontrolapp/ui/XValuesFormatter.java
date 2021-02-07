package com.ynzhxf.nd.xyfirecontrolapp.ui;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;


/**
 * author hbzhou
 * date 2019/5/30 10:05
 */
public class XValuesFormatter extends ValueFormatter {
    private List<String> mValues;

    public XValuesFormatter(List<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value) {
        if (((int) value >= 1 && (int) value <= mValues.size())) {
            return mValues.get((int) value-1);
        } else
            return "";
    }
}
package com.ynzhxf.nd.xyfirecontrolapp.util;

import com.blankj.utilcode.util.StringUtils;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;


/**
 * author hbzhou
 * date 2019/10/31 11:46
 */
public class HistoryAlarmAreaFormatter extends ValueFormatter {

    private List<String> city;

    public HistoryAlarmAreaFormatter(List<String> city) {
        this.city = city;
    }

    @Override
    public String getFormattedValue(float value) {
        //int index = Math.round(value);

        //LogUtils.showLoge("----",value+"nnnnnnn");

        if (city.size() > 0 && !StringUtils.isEmpty(city.get(0)) && city.get(0).contains("-")) {
            int index = Math.round(value);
            if (index >= 0) {
                return city.get(index % city.size());
            }
        } else if (city.size() > 0 && (Math.round(value) <= city.size()) && Math.round(value) - 1 >= 0) {
            return city.get(Math.round(value) - 1);
        }
        return String.valueOf(Math.round(value));
    }
}

package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.content.Intent;

public class InspectionSettingGetUserListActivity extends InspectionResponsiblePersonActivity {

    @Override
    protected void setPersonData(int position, String id) {
        //

        Intent intent = new Intent();
        intent.putExtra("Name", personBeanList.get(position).getName());
        intent.putExtra("position", setPosition);
        intent.putExtra("inspectId", personBeanList.get(position).getID());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

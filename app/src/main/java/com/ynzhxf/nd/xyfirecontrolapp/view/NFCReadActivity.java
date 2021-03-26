package com.ynzhxf.nd.xyfirecontrolapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionQrCodeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import okhttp3.Call;

public class NFCReadActivity extends BaseActivity {

    public static final String TAG = "InspectionActivity";

    private NfcAdapter nfcAdapter;

    private PendingIntent mPendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;

    private InspectionItemListBean inspectionItemListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_n_f_c_read);
        super.onCreate(savedInstanceState);
        setBarTitle("NFC扫描");
        initNFC();
        resolveIntent(getIntent());
        if (getIntent().hasExtra("inspectionItem"))
            inspectionItemListBean = (InspectionItemListBean) getIntent().getSerializableExtra("inspectionItem");
    }

    private void initNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "不支持NFC功能！", Toast.LENGTH_SHORT).show();
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "请打开NFC功能！", Toast.LENGTH_SHORT).show();
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NFCReadActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
        techListsArray = new String[][]{new String[]{NfcA.class.getName()}};
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, intentFiltersArray, techListsArray);
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String nfcId = dumpTagData(tag);
            if (!nfcId.isEmpty()) {
                Log.i(TAG, "卡的内容" + nfcId);
                if (null == inspectionItemListBean) {
                    Intent backintent = getIntent();
                    backintent.putExtra("NFCread", nfcId);
                    setResult(RESULT_OK, backintent);
                    finish();
                } else
                    initInspectionQrResult(nfcId);
            } else {
                Toast.makeText(getApplicationContext(), "识别失败！请重新刷卡！", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void initInspectionQrResult(String result) {

        if (inspectionItemListBean.getQrCode() != null) {
            if (!inspectionItemListBean.getQrCode().equals(result)) {
                ToastUtils.showLong("NFC标签与巡检项不匹配!");
                finish();
                return;
            }
        } else {
            if (!inspectionItemListBean.getID().equals(result)) {
                ToastUtils.showLong("NFC标签与巡检项不匹配!");
                finish();
                return;
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("taskId", inspectionItemListBean.getTaskId());
        params.put("itemId", inspectionItemListBean.getID());
        params.put("qrCode", result);
        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_QR_CODE_VERIFY)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(NFCReadActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        LogUtils.showLoge("扫码校验结果1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                Intent intent = getIntent();
                                intent.putExtra("NFCread", result);
                                setResult(RESULT_OK, intent);
                                finish();
//                                Intent intent = new Intent(InspectionQrCodeActivity.this, InspectionResultSaveActivity.class);
//                                intent.putExtra("taskId", taskId);
//                                intent.putExtra("Name", Name);
//                                intent.putExtra("Remark", Remark);
//                                intent.putExtra("AreaId", AreaId);
//                                intent.putExtra("projectId", projectId);
//                                intent.putExtra("itemId", result.getContents());
//                                startActivity(intent);

                            } else {
                                finish();
                                HelperView.Toast(NFCReadActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(NFCReadActivity.this, e.getMessage());
                        }
                    }
                });

    }

    private String dumpTagData(Parcelable p) {
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        return hexToHexString(id);
    }

    /**
     * @param b
     * @return
     */
    public static String hexToHexString(byte[] b) {
        int len = b.length;
        int[] x = new int[len];
        String[] y = new String[len];
        StringBuilder str = new StringBuilder();
        int j = 0;
        for (; j < len; j++) {
            x[j] = b[j] & 0xff;
            y[j] = Integer.toHexString(x[j]);
            while (y[j].length() < 2) {
                y[j] = "0" + y[j];
            }
            str.append(y[j]);
            str.append("");
        }
        return new String(str).toUpperCase(Locale.getDefault());
    }

}
package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPwdChangePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl.PlatfromPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

/**
 * 用户密码修改
 */
public class UserPwdChangeActivity extends BaseActivity implements IUserPwdChangePersenter.IUserPwdChangeView {
    //原密码
    private EditText etxtOldpwd;
    //新密码
    private EditText etxtNewpwd1;
    //二次确认密码
    private  EditText etxtNewpwd2;
    //错误消息提示
    private TextView txtError;

    //提交按钮
    private Button btnSubmit;

    //用户密码修改桥梁
    private IUserPwdChangePersenter persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView( R.layout.activity_user_pwd_change);
        super.onCreate(savedInstanceState);
        setBarTitle("密码修改");
        etxtOldpwd = findViewById(R.id.etxt_oldpwd);
        etxtNewpwd1 = findViewById(R.id.etxt_newpwd1);
        etxtNewpwd2 = findViewById(R.id.etxt_newpwd2);
        txtError = findViewById(R.id.txt_error);
        btnSubmit = findViewById(R.id.btn_submit);
        persenter = PlatfromPersenterFactory.getUserPwdChangeImpl(this);
        addPersenter(persenter);
        init();
    }

    private void init(){
        //注册提交按钮
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(judgeInput()){
                        txtError.setText("");
                        //提交数据
                        String queryOld = etxtOldpwd.getText()+"";
                        String queryNew1 = etxtNewpwd1.getText()+"";
                        String queryNew2 = etxtNewpwd2.getText()+"";
                        showProgressDig(true);
                        persenter.dolUserPwdChangePersenter(queryOld , queryNew1);
                    }

                }catch (Exception e){
                    txtError.setText(e.getMessage());
                }
            }
        });
    }

    /**
     * 判断用户输入是否合法
     * @return
     */
    private boolean judgeInput() throws Exception {
        String queryOld = etxtOldpwd.getText()+"";
        if(HelperTool.stringIsEmptyOrNull(queryOld)){
            etxtOldpwd.setFocusable(true);
            throw  new Exception("请输入原密码！");
        }
        String queryNew1 = etxtNewpwd1.getText()+"";
        if(HelperTool.stringIsEmptyOrNull(queryNew1)){
            etxtNewpwd1.setFocusable(true);
            throw new Exception("请输入新密码！");
        }
        if(queryNew1.length() <6){
            throw new Exception("新密码长度必须大于6位！");
        }
        if(queryNew1.length()>24 ){
            throw new Exception("新密码长度必须小于24位！");
        }
        String queryNew2 = etxtNewpwd2.getText()+"";
        if(HelperTool.stringIsEmptyOrNull(queryNew2)){
            throw new Exception("请再次输入新密码！");
        }
        if(!queryNew2.equals(queryNew1)){
            throw new Exception("两次密码输入不一致！");
        }
        if(queryOld.equals(queryNew2)){
            throw  new Exception("新密码不能和原密码一致！");
        }
        return true;
    }

    /**
     * 用户密码修改回调
     * @param resultBean
     */
    @Override
    public void callBackUserPwdChange(ResultBean<String, String> resultBean) {
        hideProgressDig();
        try{
            if(resultBean.isSuccess()){
                //修改成功，回到登陆页面
                ActivityController.finishAll();
                Intent Intent = new Intent(this , LoginActivity.class);
                startActivity(Intent);
                HelperView.Toast(this , "修改成功，请重新登陆");
            }else{
                txtError.setText(resultBean.getMessage());
            }
        }catch (Exception e){
            HelperView.Toast(this , "修改密码失败："+e.getMessage());
        }


    }
}

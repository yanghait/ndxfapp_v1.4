package com.ynzhxf.nd.xyfirecontrolapp.util;

public class PermissionsUtil {

    public interface IGrantCallBack {
        void result(boolean Success,int requestCode);
    }

    // 申请相机权限
//    public static void getCameraPermissions(FragmentActivity activity, final IGrantCallBack callBack) {
//        final RxPermissions rxPermissions = new RxPermissions(activity);
//        final Disposable disposable = rxPermissions.request(Manifest.permission.CAMERA)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                       // if(disposable.isDisposed()){}
//                        callBack.result(aBoolean);
//                    }
//                });
//    }
}

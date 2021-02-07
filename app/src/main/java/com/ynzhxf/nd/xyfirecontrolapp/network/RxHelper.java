package com.ynzhxf.nd.xyfirecontrolapp.network;


/**
 * author hbzhou
 * date 2019/6/19 10:17
 */
public class RxHelper {
//    public static <T> ObservableTransformer<T, T> observableIO2Main(final Context context) {
//        return upstream -> {
//            Observable<T> observable = upstream.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread());
//            return composeContext(context, observable);
//        };
//    }
//
//    public static <T> ObservableTransformer<T, T> observableIO2Main(final RxFragment fragment) {
//        return upstream -> upstream.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).compose(fragment.<T>bindToLifecycle());
//    }
//
//    public static <T> FlowableTransformer<T, T> flowableIO2Main() {
//        return upstream -> upstream
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    private static <T> ObservableSource<T> composeContext(Context context, Observable<T> observable) {
//        if(context instanceof RxActivity) {
//            return observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        } else if(context instanceof RxFragmentActivity){
//            return observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        }else if(context instanceof RxAppCompatActivity){
//            return observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        }else {
//            return observable;
//        }
//    }
}

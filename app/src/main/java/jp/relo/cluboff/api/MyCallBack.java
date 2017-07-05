package jp.relo.cluboff.api;

import framework.phvtUtils.AppLog;
import rx.Subscriber;

/**
 * Created by tonkhanh on 6/13/17.
 */

public abstract class MyCallBack<T> extends Subscriber<T> {
    public abstract void onSuccess(T model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        AppLog.log(e.toString());
        onFailure(e.getMessage());
        onFinish();
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}

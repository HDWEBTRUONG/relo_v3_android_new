package net.fukuri.memberapp.memberapp.api;

import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.model.BaseReponse;
import net.fukuri.memberapp.memberapp.util.Constant;
import rx.Subscriber;

/**
 * Created by tonkhanh on 6/13/17.
 */

public abstract class MyCallBack<T> extends Subscriber<T> {
    public abstract void onSuccess(T model);

    public abstract void onFailure(int msg);

    public abstract void onFinish();

    @Override
    public void onNext(T t) {
        if(t instanceof BaseReponse){
            switch (((BaseReponse) t).getHeader().getDetail()){
                case Constant.HTTP0000:
                    onSuccess(t);
                    break;
                case Constant.HTTP0001:
                    onFailure(R.string.err_0001);
                    onFinish();
                    break;
                case Constant.HTTP0002:
                    onFailure(R.string.err_0002);
                    onFinish();
                    break;
                case Constant.HTTP0005:
                    onFailure(R.string.err_0005);
                    onFinish();
                    break;
                case Constant.HTTP0006:
                    onFailure(R.string.err_0006);
                    onFinish();
                    break;
                case Constant.HTTP0007:
                    onFailure(R.string.err_0007);
                    onFinish();
                    break;
                case Constant.HTTP0010:
                    onFailure(R.string.err_0010);
                    onFinish();
                    break;
                case Constant.HTTP0021:
                    onFailure(R.string.err_0021);
                    onFinish();
                    break;
                case Constant.HTTP0022:
                    onFailure(R.string.err_0022);
                    onFinish();
                    break;
                case Constant.HTTP0023:
                    onFailure(R.string.err_0023);
                    onFinish();
                    break;
                case Constant.HTTP0099:
                    onFailure(R.string.err_0099);
                    onFinish();
                    break;
            }
        }else{
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        AppLog.log(e.toString());
        onFinish();
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}

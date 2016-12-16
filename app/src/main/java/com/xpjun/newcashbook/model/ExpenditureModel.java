package com.xpjun.newcashbook.model;

import com.xpjun.newcashbook.model.imodel.IExpenditureModel;
import com.xpjun.newcashbook.presenter.ExpenditurePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U-nookia on 2016/11/24.
 */

public class ExpenditureModel implements IExpenditureModel{
    private List<ExpenditureBean> expenditureBeanList;
    private ExpenditurePresenter presenter;
    private static ExpenditureModel model;

    private ExpenditureModel() {
        expenditureBeanList = new ArrayList<>();
        presenter = new ExpenditurePresenter();
    }

    public void getMsgFromBmob(GetMsgCallBack callBack){

    }

    public List<ExpenditureBean> getExpenditureBeanList(){
        return expenditureBeanList;
    }

    public static ExpenditureModel getInstance(){
        if (model==null){
            model = new ExpenditureModel();
            return model;
        }else {
            return model;
        }
    }

    public interface GetMsgCallBack{
        void callback(List<ExpenditureBean> lists);
    }
}

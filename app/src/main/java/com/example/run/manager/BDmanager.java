package com.example.run.manager;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.run.activity.SelectActivity;
import com.example.run.model.SelectInfo;

import java.util.ArrayList;
import java.util.List;


public class BDmanager {
    private ReverseGeoCodeOption reverseGeoCodeOption;
    private GeoCoder geoCoder;

    private SuggestionSearchOption suggestionSearchOption;
    private SuggestionSearch suggestionSearch;


    private List<SelectInfo> list = new ArrayList<>();

    public BDmanager(){
        reverseGeoCodeOption = new ReverseGeoCodeOption();
        geoCoder =  GeoCoder.newInstance();
        suggestionSearchOption = new SuggestionSearchOption();
        suggestionSearch = SuggestionSearch.newInstance();
    }

    private OnGetGeoCoderResultListener getGeoCoderResultListener;

    public void setOnGetGeoCodeResultListener(OnGetGeoCoderResultListener onGetGeoCodeResultListener){
        this.getGeoCoderResultListener = onGetGeoCodeResultListener;
    }

    public void setLatLong(LatLng latLong){
        reverseGeoCodeOption.location(latLong);
        geoCoder.reverseGeoCode(reverseGeoCodeOption);
        geoCoder.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
    }

    public interface OnResultListener{
        void result(List<SelectInfo> list);
    }

    private OnResultListener resultListener;

    public void setOnResultListener(OnResultListener resultListener){
        this.resultListener = resultListener;
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                list.clear();

                List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
                if(allSuggestions.size() > 0){
                    for(int i=0;i<allSuggestions.size();i++){
                        SuggestionResult.SuggestionInfo suggestionInfo = allSuggestions.get(i);
                        SelectInfo selectInfo = new SelectInfo();
                        selectInfo.setAddress(suggestionInfo.key);
                        selectInfo.setCity(suggestionInfo.city + suggestionInfo.district);

                        if(suggestionInfo.pt != null)
                        {
                            selectInfo.setLatitude(suggestionInfo.pt.latitude);
                            selectInfo.setLongtitude(suggestionInfo.pt.longitude);
                        }


                        list.add(selectInfo);
                    }
                    BDmanager.this.resultListener.result(list);
                }else{

                }
            }
        });
    }

    public void startPoi(String key){
        suggestionSearch.requestSuggestion(suggestionSearchOption.citylimit(false).city("山西").keyword(key));

    }


}

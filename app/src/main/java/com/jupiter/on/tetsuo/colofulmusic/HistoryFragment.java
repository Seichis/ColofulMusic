package com.jupiter.on.tetsuo.colofulmusic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import data.StaticMusicType;
import data.operations.DataOperations;
import weka.gui.Main;

public class HistoryFragment extends Fragment {

    MyWebView browserAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        browserAll=(MyWebView)rootView.findViewById(R.id.webkit);

        browserAll.getSettings().setJavaScriptEnabled(true);
        browserAll.getSettings().setBuiltInZoomControls(true);
        browserAll.getSettings().setSupportZoom(true);
        browserAll.getSettings().setDisplayZoomControls(false);
        browserAll.addJavascriptInterface(new WebAppInterface(MainActivity.getMainActivity().getApplicationContext()), "Android");
        browserAll.loadUrl("file:///android_res/raw/googlechart.html");
        //browserAll.applyAfterMoveFix();
        return rootView;
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getData() {


            String task = "showall";
            switch (task) {
                case "showall":
                    Log.i("Kotsos",DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareAggregatedMusicDurationForJavascript()).toString());
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareAggregatedMusicDurationForJavascript());
                case StaticMusicType.rock:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.rock));
                case StaticMusicType.dance:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.dance));
                case StaticMusicType.classical:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.classical));
                case StaticMusicType.folk:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.folk));
                case StaticMusicType.reggae:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.reggae));
                case StaticMusicType.hipHop:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.hipHop));
                case StaticMusicType.metal:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.metal));
                case StaticMusicType.punk:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.punk));
                case StaticMusicType.pop:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.pop));
                case StaticMusicType.jazz:
                    return DataOperations.getInstance().javascriptFeedbackToJson(DataOperations.getInstance().prepareMusicDurationForJavascript(StaticMusicType.jazz));
                default:
                    return "Nothing to show";
            }
        }

    }

}

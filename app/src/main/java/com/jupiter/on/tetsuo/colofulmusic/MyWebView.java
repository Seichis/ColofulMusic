package com.jupiter.on.tetsuo.colofulmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by User1 on 26/6/2015.
 */
public class MyWebView extends WebView {
    public MyWebView(Context context,AttributeSet attrs) {
        super(context,attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            int temp_ScrollY = getScrollY();
            scrollTo(getScrollX(), getScrollY() + 1);
            scrollTo(getScrollX(), temp_ScrollY);

        }

        return super.onTouchEvent(event);
    }
    public void applyAfterMoveFix() { onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY()); }
}

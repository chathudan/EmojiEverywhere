package emoji.sithagi.com.emojieverywhere;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class VOIPemHorizontalScrollView extends HorizontalScrollView {

    private boolean mStartScroll;

    public VOIPemHorizontalScrollView(Context context) {
        this(context, null, 0);
    }

    public VOIPemHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VOIPemHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

//    	 Log.i("Scrolling", "X from ["+oldl+"] to ["+l+"]");

        if (oldl < l && !mStartScroll) {
            mStartScroll = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    mStartScroll = false;
                }
            }, 100L);
        } else if (l < oldl && !mStartScroll) {
            mStartScroll = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    mStartScroll = false;
                }
            }, 100L);
        }

//    	 fullScroll(HorizontalScrollView.FOCUS_LEFT); // temporary disable scroll

        super.onScrollChanged(l, t, oldl, oldt);
    }


}

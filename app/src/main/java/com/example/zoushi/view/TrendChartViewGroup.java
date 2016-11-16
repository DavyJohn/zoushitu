package com.example.zoushi.view;

//package com.wangyicaipiao.app.view;

import android.content.Context;
import android.content.SyncRequest;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zoushi.R;
import com.example.zoushi.view.scrollview.MyHorizontalScrollView;

import static android.view.Gravity.CENTER;


/**
 * Created by zhkqy on 15/8/11.
 */
public class TrendChartViewGroup extends RelativeLayout implements MiddleView.middleTouchEventListener, MyScrollView.OnScrollListener, MyHorizontalScrollView.OnHorizontalScrollListener {

    private Context mContext;
    private LinearLayout top_linearlayout,bottom_linearlayout,left_linearlayout;
    private MyHorizontalScrollView top_scrollview,bottom_scrollview;
    private MyScrollView left_scrollview;
    private MiddleView middleView;

    public TrendChartViewGroup(Context context) {
        super(context);
        initView(context);
        setFocusable(true);

    }

    public TrendChartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View v = View.inflate(context, R.layout.view_trend_chart, null);
        findById(v);
        addData();
        addView(v);
    }

    private void findById(View v) {
        top_linearlayout = (LinearLayout) v.findViewById(R.id.top_linearlayout);
        bottom_linearlayout = (LinearLayout) v.findViewById(R.id.bottom_linearlayout);
        left_linearlayout = (LinearLayout) v.findViewById(R.id.left_linearlayout);

        top_scrollview = (MyHorizontalScrollView) v.findViewById(R.id.top_scrollview);
        bottom_scrollview = (MyHorizontalScrollView) v.findViewById(R.id.bottom_scrollview);
        left_scrollview = (MyScrollView) v.findViewById(R.id.left_scrollview);

        middleView = (MiddleView) v.findViewById(R.id.middle_view);
        left_scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);   //  根据不同手机适配  不可在第一条还能下拉
        middleView.setMonTouchEventListener(this);

        top_scrollview.setOnHorizontalScrollListener(this);
        bottom_scrollview.setOnHorizontalScrollListener(this);
        left_scrollview.setOnScrollListener(this);


    }

    /**
     * 添加数据 TOP and left and bottom边角数据
     */
    public void addData() {
        top_linearlayout.removeAllViews();
        left_linearlayout.removeAllViews();
        bottom_linearlayout.removeAllViews();
        for (int i = 0; i < 33; i++) {
            TextView t = new TextView(mContext);
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            int random = i + 1;
            t.setText(String.valueOf(random));
            top_linearlayout.addView(t);
        }
        for (int i = 0; i < 33; i++) {
            TextView t = new TextView(mContext);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            int random = i + 1;
            t.setText(String.valueOf(random)+"期");
            left_linearlayout.addView(t);
        }
        //创建底部
        for (int i = 0; i < 33; i++) {
            TextView t = new TextView(mContext);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            final int random = i + 1;
            t.setText(String.valueOf(random));
            bottom_linearlayout.addView(t);

            t.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("==========",String.valueOf(random));
                }
            });
        }
    }

    @Override
    public void middleOnTouchEvent(final int initX, final int initY) {

        top_scrollview.scrollTo(-initX, 0);
        left_scrollview.scrollTo(0, -initY);
        bottom_scrollview.scrollTo(-initX,0);
    }

    @Override
    public void onScroll(int scrollX, int scrollY) {
        middleView.setScrollXY(-scrollX, -scrollY);
    }

}

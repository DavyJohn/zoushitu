package com.example.zoushi.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zoushi.JsonTools;
import com.example.zoushi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * Created by zzh on 16/11/115.
 * 数据没有开发结束
 */

public class MiddleView extends ViewGroup {

    int initX = 0;
    int initY = 0;
    int x = 0, y = 0;
    int tempInitX = 0;
    int tempInitY = 0;

    boolean isRestart = false;

    List<String> datas = new ArrayList<>();
    //定义一个数组
    List<String> test_data = new ArrayList<>();

    public static int cellWitch = 80;
    public static int cellHeight = 80;
    private int borderRight = 0;  //  右边距  包括没展示区域的总宽度
    private int borderBottom = 0;//   底边距  包括没展示区域的总高度
    private Context mContext;
//测试红球数据
    String [] test = {"4","7","9","12","20","30"};
    /**
     * 回调
     */
    public interface middleTouchEventListener {
        void middleOnTouchEvent(int gapX, int gapY);
    }

    private middleTouchEventListener eventListener;

    public void setMonTouchEventListener(middleTouchEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public MiddleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        removeAllViews();
        addData();
    }

    /**
     * 判断边界
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                tempInitX = initX;
                tempInitY = initY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                int gapX = moveX - x;
                int gapY = moveY - y;
                initX = tempInitX + gapX;
                initY = tempInitY + gapY;
                checkBorder();
                scrollTo(-initX, -initY);
                if (eventListener != null) {
                    eventListener.middleOnTouchEvent(initX, initY);
                }
                break;
        }
        return true;
    }

    /**
     * 添加测试数据
     * "4","7","9","12","20","30"
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addData() {

        String data_json = JsonTools.getJson(mContext,"test_data.json");
        parseJson(data_json);
        int left = 0;
        int right = cellWitch;
        int top = 0;
        int bottom = cellHeight;

        for (int i = 0; i < 33; i++) {//列
            int start = 0;
            boolean isMSTOP = false;
            for (int j = 0; j < 33; j++) {
                if (isRestart == true){
                    start = 0;
                    isRestart = false;
                }
                TextView t = new TextView(mContext);
                t.setGravity(CENTER);
                t.setTextSize(12);
                start++;
                t.setText(String.valueOf(start));
                if (isMSTOP == false){
                    for (int m = 0;m<6;m++){
                        //一期里面有多少数字
                        if (i+1 == Integer.parseInt(test[m])){
                            t.setText(test[m]);
                            t.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                            isRestart = true;
                            isMSTOP = true;
                        }
                    }
                }


                t.layout(left, top, right, bottom);
                bottom += cellWitch;
                top += cellWitch;
                addView(t);
            }
            //转到第二行初始化
//            left = 0;
//            right = cellWitch;
//            top += cellHeight;
//            bottom += cellHeight;
            //转到第二列初始化
            top = 0;
            right += cellWitch;
            left +=cellWitch;
            bottom += cellWitch;
        }
        borderRight = 33 * cellWitch;
        borderBottom = 33 * cellHeight;
    }

    private void parseJson(String data_json) {
        try {
            JSONArray jsonArray = new JSONArray(data_json);
            for (int i= 0;i<jsonArray.length();i++){
                JSONObject NAME = jsonArray.optJSONObject(i);
                String data= NAME.getString("name");
                //获取号码数组
                JSONArray numArray = NAME.optJSONArray("data");
                for (int s = 0 ; s <numArray.length();s++){
                    String ss = (String) numArray.get(s);
                    test_data.add(ss);
                }
                datas.addAll(test_data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查边界
     */
    private void checkBorder() {

        if (initX >= 0) {
            initX = 0;
        }
        if (initY >= 0) {
            initY = 0;
        }
        if (initX < -borderRight + getWidth()) {
            initX = -borderRight + getWidth();
        }
        if (initY < -borderBottom + getHeight()) {
            initY = -borderBottom + getHeight();
        }
    }

    /**
     * 外部调用滚动
     */
    public void setScrollXY(int x, int y) {
        initX = x;
        initY = y;
        scrollTo(-initX,-initY);
    }
}


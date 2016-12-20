package com.mao.toggleview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 毛麒添 on 2016/12/16 0016.
 * 自定义的开关控件
 */

public class MyToggleView extends View {
    private Bitmap switchBackgroundResource;//开关背景
    private Bitmap slideBackgroundResource;//滑块背景

    private Paint paint;//画笔

    private boolean mSwitchState=false;//开关状态

    private float currentX;//触摸X轴位置

    private OnSwitchStateUpdateListener onOnSwitchStateUpdateListener;//接口监听对象


    private String nameSpace="http://schemas.android.com/apk/res-auto";//名空间
    /**
     * 用于代码创建控件
     * @param context
     */
    public MyToggleView(Context context) {
        super(context);
        init();
    }

    /**
     * 用于在xml中使用，可以指定控件的自定义属性
     * @param context
     * @param attrs
     */
    public MyToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //在其中获取自定义的属性值，设置给自定义控件
         mSwitchState=attrs.getAttributeBooleanValue(nameSpace,"switch_state",false);

        int switch_background = attrs.getAttributeResourceValue(nameSpace, "switch_background", -1);
        int slide_button = attrs.getAttributeResourceValue(nameSpace, "slide_button", -1);

        setSwitchBackgroundResource(switch_background);
        setSlideButtonBackground(slide_button);
    }

    /**
     * 用于在xml中使用，可以指定控件的自定义属性，如果有样式，则调用此方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        //初始化画笔
        paint=new Paint();
    }

    /**
     * 控件的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽高为背景图片的宽高
        setMeasuredDimension(switchBackgroundResource.getWidth(),switchBackgroundResource.getHeight());
    }

    /**
     * 绘制控件
     * @param canvas 画布 画笔在方法里绘制内容显示到界面上
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(switchBackgroundResource,0,0,paint);

        //绘制滑块
        //根据触摸滑块的位置来绘制界面
       if(isTouchMode){
           //让滑块向左移动到自身滑块一一半的位置，以达到点中滑块就可以达到点中效果
           float newleft=  currentX-slideBackgroundResource.getWidth()/2.0f;

           //左边的最大范围
           float maxleft=switchBackgroundResource.getWidth()-slideBackgroundResource.getWidth();

           //限制滑块的范围
           if(newleft<0){
               newleft=0;//左边范围
           }else if(newleft>maxleft){//大于背景图减去滑块的长度
               //右边范围
               newleft=maxleft;
           }

           canvas.drawBitmap(slideBackgroundResource,newleft,0,paint);
       }else {
           //如果没有触摸，则根据初始设置滑块的位置
           if(mSwitchState){//开
               //计算滑块在开位置的X轴坐标
               int left=switchBackgroundResource.getWidth()-slideBackgroundResource.getWidth();
               canvas.drawBitmap(slideBackgroundResource,left,0,paint);

           }else {//关
               canvas.drawBitmap(slideBackgroundResource,0,0,paint);
           }
       }

    }

    //是否触摸滑块
    boolean isTouchMode=false;
    //触摸响应事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode=true;
                Log.w("毛麒添", "onTouchEvent: "+ currentX);
                //获取触碰的位置
                currentX=event.getX();

                break;
            case MotionEvent.ACTION_MOVE:
                Log.w("毛麒添", "onTouchEvent: "+ currentX);
                //获取触碰的位置
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode=false;
                //获取离开的位置
                Log.w("毛麒添", "onTouchEvent: "+ currentX);
                currentX=event.getX();

                //根据滑块的位置和当前背景的中心位置比较来判断滑块是开还是关
                float centerX=switchBackgroundResource.getWidth()/2.0f;

                boolean state=currentX>centerX;

                //如果开关的状态发生变化，将其状态传递到界面
                if(state!=mSwitchState&&onOnSwitchStateUpdateListener!=null){
                    onOnSwitchStateUpdateListener.onStateUpdate(state);
                }
                //将比较的结果赋值给开关的状态量
                mSwitchState=state;
                break;

        }
        invalidate();//重新绘制界面

        return true;//消费了触摸事件，才可以收到其他事件
    }

    /**
     * 设置背景图片
     * @param switch_background 图片资源id
     */
    public void setSwitchBackgroundResource(int switch_background) {
        switchBackgroundResource = BitmapFactory.decodeResource(getResources(), switch_background);
    }

    /**
     * 设置滑块图片资源
     * @param slide_button 图片资源id
     */
    public void setSlideButtonBackground(int slide_button) {
        slideBackgroundResource = BitmapFactory.decodeResource(getResources(), slide_button);
    }

    /**
     * 设置开关是否开启
     * @param isOpen 是否开启
     */
    public void setSwitchState(boolean isOpen) {
         this.mSwitchState=isOpen;
    }

    //声明一个接口对象
    public interface  OnSwitchStateUpdateListener{
        //状态回调，将空间内部的状态传递出去
        void onStateUpdate(boolean state);
    }
    //设置接口对象的方法，用于外部调用
    public void setOnOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onOnSwitchStateUpdateListener){
        this.onOnSwitchStateUpdateListener=onOnSwitchStateUpdateListener;

    }
}

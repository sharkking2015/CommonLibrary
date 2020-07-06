package com.syq.commonlibrary.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.syq.commonlibrary.R;
import com.syq.commonlibrary.utils.Logger;


public class AutoChangeLineLayout extends ViewGroup {
    public static final String TAG = AutoChangeLineLayout.class.getSimpleName();
    private int mLinePadding;
    private int mRowPadding;
    private int mLeftPadding;
    private int mRightPadding;
    private int mTopPadding;
    private int mBottomPadding;

    private int selectedTheme;
    private int unSelectedTheme;

    public AutoChangeLineLayout(Context context) {
        this(context,null);
    }

    public AutoChangeLineLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoChangeLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoChangeLineLayout);
        mLinePadding = (int) ta.getDimension(R.styleable.AutoChangeLineLayout_line_padding,dp2px(16));
        mRowPadding = (int) ta.getDimension(R.styleable.AutoChangeLineLayout_row_padding,dp2px(16));
        ta.recycle();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        int count = getChildCount();

        int widthUsed = mLeftPadding;
        int heightUsed = mTopPadding;
        int maxWidth = 0;
        int maxHeight = 0;
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            if(widthUsed+child.getMeasuredWidth()+mRightPadding > widthSize){
                //换行
                widthUsed = mLeftPadding +child.getMeasuredWidth()+mRowPadding;
                heightUsed += maxHeight+ mLinePadding;
                maxHeight = child.getMeasuredHeight();
            }else{
                widthUsed+=mRowPadding+child.getMeasuredWidth();
                Logger.i(TAG,"measure:"+widthUsed);
                if(maxHeight<child.getMeasuredHeight()){
                    maxHeight = child.getMeasuredHeight();
                }
            }
            if(widthUsed>maxWidth){
                maxWidth = widthUsed;
            }
        }
        heightUsed+=maxHeight+mBottomPadding;
        maxWidth +=mRightPadding;

        setMeasuredDimension(maxWidth,heightUsed);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int widthUsed = mLeftPadding;
        int heightUsed = mTopPadding;
        int width = r-l;
        int height = b-t;
        int lineHeight = 0;
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            int childW = child.getMeasuredWidth();
            int childH = child.getMeasuredHeight();
            if(widthUsed +childW +mRightPadding> width){
                //换行,加上上一行的高度
                heightUsed += mLinePadding+lineHeight;
                widthUsed = mLeftPadding;
                child.layout(widthUsed,heightUsed,widthUsed+childW,heightUsed+childH);
                widthUsed+= childW+mRowPadding;
                lineHeight = childH;
            }else{
                child.layout(widthUsed,heightUsed,widthUsed+childW,heightUsed+childH);
                widthUsed+=childW+mRowPadding;
                Logger.i(TAG,"layout:"+widthUsed+" "+getMeasuredWidth());
                if(childH>lineHeight){
                    lineHeight = childH;
                }
            }
        }
    }

    public int dp2px(float dpVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics()));
    }

    public void setTheme(int selectedTheme,int unSelectedTheme){
        this.selectedTheme = selectedTheme;
        this.unSelectedTheme = unSelectedTheme;
    }

    void childClick(){

    }
}

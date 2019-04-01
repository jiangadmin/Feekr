package com.tl.film.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.tl.film.utils.ScreenUtil;

/**
 * 继承RecyclerView重写{@link #getChildDrawingOrder(int, int)}对Item的绘制顺序进行控制
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.0
 * @Datetime 2017-04-18
 */

public class RecyclerCoverFlow extends RecyclerView {
    /**
     * 按下的X轴坐标
     */
    private float mDownX;

    private BorderListener borderListener;

    private boolean headerIsNull = false;
    private boolean isHeader = false;


    public RecyclerCoverFlow(Context context) {
        super(context);
        init(false);
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);
    }

    private void init(boolean isFlat) {
        setLayoutManager(new CoverFlowLayoutManger(isFlat));
        setChildrenDrawingOrderEnabled(true); //开启重新排序
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 设置是否为普通平面滚动
     * @param isFlat true:平面滚动；false:叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat) {
        init(isFlat);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition(); //计算正在显示的所有Item的中间位置
        if (center < 0) center = 0;
        else if (center > childCount) center = childCount;
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger)getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 设置选中监听
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE:
                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() -1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean isBorder(KeyEvent event) {
        int focusDirection = event.getKeyCode();
        View view = this.getFocusedChild();
        LayoutManager layoutManager = this.getLayoutManager();
        int focusPos = layoutManager.getPosition(view);
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            int itemCount = layoutManager.getItemCount();
            int rowCount;
            int row;
            int span;
            if (isHeader && itemCount != 1) {
                rowCount = (int) (Math.ceil((double) (itemCount - 1) / spanCount) + 1);
                if (focusPos != 0) {
                    row = (focusPos - 1) / spanCount + 2;
                } else {
                    row = (focusPos - 1) / spanCount + 1;
                }
                span = (focusPos - 1) % spanCount + 1;
            } else {
                rowCount = (int) Math.ceil((double) itemCount / spanCount);
                row = focusPos / spanCount + 1;
                span = focusPos % spanCount + 1;
            }
            if (event.hasNoModifiers()) {
                switch (focusDirection) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (row == rowCount) {
                            return borderListener.onKeyBottomDown();
                        } else {
                            //处理长按焦点错误问题;
                            View nextView = view.focusSearch(View.FOCUS_DOWN);
                            if (nextView != null) {
                                //过虑还没绘制完成的view和正在修改的view
                                if (!nextView.willNotDraw() || nextView.isDirty()) {
                                    return true;
                                }
                                // 这个根据布局的需求进行调整，用于解决长按遥控器，焦点跑掉的问题
                                if (nextView.getTop() > ScreenUtil.dip2px(getContext(), 680)) {
                                    return true;
                                }
                            }
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (row == 1) {
                            return borderListener.onKeyTopUp();
                        } else if (row == 2 && headerIsNull) {
                            return borderListener.onKeyTopUp();
                        } else {
                            //处理长按焦点错误问题
                            View nextView = view.focusSearch(View.FOCUS_UP);
                            if (nextView != null) {
                                if (!nextView.willNotDraw()) {
                                    return true;
                                }
                            }
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (span == spanCount) {
                            return borderListener.onKeyRightEnd();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if (span == 1) {
                            return borderListener.onKeyLeftEnd();
                        }
                        break;
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }

    public interface BorderListener {
        boolean onKeyBottomDown();

        boolean onKeyTopUp();

        boolean onKeyLeftEnd();

        boolean onKeyRightEnd();
    }
}

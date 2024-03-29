package com.hyena.framework.animation.sprite;

import java.util.Random;

import com.hyena.framework.animation.Director;
import com.hyena.framework.utils.UIUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 单个对象节点
 *
 * @author yangzc
 */
public abstract class CNode implements OnTouchListener {

//	private static final String LOG_TAG = CNode.class.getSimpleName();

    public static final int FILL_PARENT = -1;

    private CNode mParent;
    //对齐方式
    protected CAlign mAlign = null;
    //花费的时间
    private float elapsed = 0;
    //z轴索引
    private int mZIndex;

    private int mX = 0, mY = 0;
    private int mWidth = FILL_PARENT;
    private int mHeight = FILL_PARENT;
    private boolean isVisible = true;
    private Paint mPaint;

    private static Random mRandom = new Random();

    private Director mDirector;

    public CNode(Director director) {
        this.mDirector = director;
    }

    public void setParent(CNode parent) {
        this.mParent = parent;
    }

    /**
     * 渲染
     *
     * @param canvas
     */
    public void render(Canvas canvas) {
        if (mPaint != null) {
            canvas.drawRect(new Rect(getPosition().x, getPosition().y,
                    getPosition().x + getWidth(), getPosition().y + getHeight()), mPaint);
        }
    }

    /**
     * 刷新帧
     *
     * @param dt 刷新间隔
     */
    public void update(float dt) {
//		LogUtil.v(LOG_TAG, "update");
        elapsed += dt;
    }

    /**
     * 获得已经使用时间
     *
     * @return
     */
    protected float getElapsed() {
        return elapsed;
    }

    /**
     * 重置状态
     */
    public void reset() {
        elapsed = 0f;
    }

    /**
     * @return the isVisible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param isVisable the isVisible to set
     */
    public void setVisible(boolean isVisable) {
        this.isVisible = isVisable;
    }

    /**
     * 设置节点位置
     *
     * @param position
     */
    public void setPosition(Point position) {
        this.mX = position.x;
        this.mY = position.y;
    }

    /**
     * 设置view大小
     *
     * @param width
     * @param height
     */
    public void setViewSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    /**
     * 内容宽度
     *
     * @return
     */
    public int getWidth() {
        if (mParent != null) {
            if (mWidth == FILL_PARENT) {
                return mParent.getWidth();
            }
            return mWidth;
        }
        if (mDirector == null || mDirector.getViewSize() == null)
            return 0;

        return mDirector.getViewSize().width();
    }

    /**
     * 内容高度
     *
     * @return
     */
    public int getHeight() {
        if (mParent != null) {
            if (mHeight == FILL_PARENT) {
                return mParent.getHeight();
            }
            return mHeight;
        }
        if (mDirector == null || mDirector.getViewSize() == null) {
            return 0;
        }
        return mDirector.getViewSize().height();
    }

    /**
     * 设置对齐方式
     *
     * @param align
     */
    public void setAlign(CAlign align) {
        if (align == null)
            return;
        this.mAlign = align;
    }

    public enum CAlign {
        TOP_LEFT(0), TOP_CENTER(1), TOP_RIGHT(2), CENTER_LEFT(3), CENTER_CENTER(4),
        CENTER_RIGHT(5), BOTTOM_LEFT(6), BOTTOM_CENTER(7), BOTTOM_RIGHT(8);

        CAlign(int type) {
            this.type = type;
        }

        private int type;

        public int getValue() {
            return type;
        }
    }

    /**
     * 设置z轴索引
     *
     * @param zindex
     */
    public void setZIndex(int zindex) {
        this.mZIndex = zindex;
    }

    /**
     * 获得z轴索引
     *
     * @return
     */
    public int getZIndex() {
        return mZIndex;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * 获得随机数对象
     *
     * @return
     */
    public static Random getRandomObj() {
        if (mRandom == null)
            mRandom = new Random();
        return mRandom;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        return UIUtils.dip2px(dpValue);
    }

    /**
     * 获得节点位置
     *
     * @return
     */
    public Point getPosition() {
        if (mDirector == null || mDirector.getViewSize() == null)
            return new Point(mX, mY);

        int pLeft = 0, pTop = 0;
        int pWidth = mDirector.getViewSize().width();
        int pHeight = mDirector.getViewSize().height();
        if (mParent != null) {
            pLeft = mParent.getPosition().x;
            pTop = mParent.getPosition().y;
        }

        if (mAlign != null) {
            if (mParent != null) {
                pWidth = mParent.getWidth();
                pHeight = mParent.getHeight();
            }

            switch (mAlign) {
                case TOP_LEFT:
                    return new Point(pLeft, pTop);
                case TOP_CENTER:
                    return new Point(pLeft + (pWidth - getWidth()) / 2, pTop + 0);
                case TOP_RIGHT:
                    return new Point(pLeft + pWidth - getWidth(), pTop + 0);

                case CENTER_LEFT:
                    return new Point(pLeft, pTop + (pHeight - getHeight()) / 2);
                case CENTER_CENTER:
                    return new Point(pLeft + (pWidth - getWidth()) / 2, pTop + (pHeight - getHeight()) / 2);
                case CENTER_RIGHT:
                    return new Point(pLeft + pWidth - getWidth(), pTop + (pHeight - getHeight()) / 2);

                case BOTTOM_LEFT:
                    return new Point(pLeft + 0, pTop + pHeight - getHeight());
                case BOTTOM_CENTER:
                    return new Point(pLeft + (pWidth - getWidth()) / 2, pTop + pHeight - getHeight());
                case BOTTOM_RIGHT:
                    return new Point(pLeft + pWidth - getWidth(), pTop + pHeight - getHeight());
                default:
                    break;
            }
        }
        return new Point(pLeft + mX, pTop + mY);
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setColor(int color) {
        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setColor(color);
    }

    /**
     * 检查所有节点是否有变化
     *
     * @return
     */
    public boolean isActive() {
        return true;
    }

    public Director getDirector() {
        return mDirector;
    }
}

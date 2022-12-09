package gold.pay.billing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TextOutline extends androidx.appcompat.widget.AppCompatTextView {

    private static final int DEFAULT_OUTLINE_SIZE = 0;
    private static final int DEFAULT_OUTLINE_COLOR = Color.TRANSPARENT;

    private int mOutlineSize;
    private int mOutlineColor;
    private int mTextColor;
    private float mShadowRadius;
    private float mShadowDx;
    private float mShadowDy;
    private int mShadowColor;

    public TextOutline(Context context) {
        this(context, null);
    }

    public TextOutline(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    private void setAttributes(AttributeSet attrs) {
        mOutlineSize = DEFAULT_OUTLINE_SIZE;
        mOutlineColor = DEFAULT_OUTLINE_COLOR;

        mTextColor = getCurrentTextColor();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextOutline);
            if (a.hasValue(R.styleable.TextOutline_outlineSize)) {
                mOutlineSize = (int) a.getDimension(R.styleable.TextOutline_outlineSize, DEFAULT_OUTLINE_SIZE);
            }

            if (a.hasValue(R.styleable.TextOutline_outlineColor)) {
                mOutlineColor = a.getColor(R.styleable.TextOutline_outlineColor, DEFAULT_OUTLINE_COLOR);
            }

            if (a.hasValue(R.styleable.TextOutline_android_shadowRadius)
                    || a.hasValue(R.styleable.TextOutline_android_shadowDx)
                    || a.hasValue(R.styleable.TextOutline_android_shadowDy)
                    || a.hasValue(R.styleable.TextOutline_android_shadowColor)) {
                mShadowRadius = a.getFloat(R.styleable.TextOutline_android_shadowRadius, 0);
                mShadowDx = a.getFloat(R.styleable.TextOutline_android_shadowDx, 0);
                mShadowDy = a.getFloat(R.styleable.TextOutline_android_shadowDy, 0);
                mShadowColor = a.getColor(R.styleable.TextOutline_android_shadowColor, Color.TRANSPARENT);
            }

            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setPaintToOutline();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setPaintToOutline() {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mOutlineSize);
        super.setTextColor(mOutlineColor);
        super.setShadowLayer(0, 0, 0, Color.TRANSPARENT);

    }

    private void setPaintToRegular() {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        super.setTextColor(mTextColor);
        super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }


    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mTextColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setPaintToOutline();
        super.onDraw(canvas);

        setPaintToRegular();
        super.onDraw(canvas);
    }
}
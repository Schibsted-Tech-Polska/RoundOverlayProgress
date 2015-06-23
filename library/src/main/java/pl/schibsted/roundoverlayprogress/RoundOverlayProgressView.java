package pl.schibsted.roundoverlayprogress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Jacek Kwiecień on 11.05.15.
 */
public class RoundOverlayProgressView extends ImageView {

    private int progressColor;
    private int maxProgress;
    private int currentProgress;
    private int animationDuration;
    private boolean animate;

    public RoundOverlayProgressView(Context context) {
        super(context);
    }

    public RoundOverlayProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundOverlayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, progressColor, maxProgress, currentProgress, animationDuration, animate);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.progressColor = savedState.progressColor;
            this.maxProgress = savedState.maxProgress;
            this.currentProgress = savedState.currentProgress;
            this.animationDuration = savedState.animationDuration;
            this.animate = savedState.animate;
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundOverlayProgressView);

        int src = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(src);

        progressColor = a.getColor(R.styleable.RoundOverlayProgressView_progressColor, R.color.default_progress);
        maxProgress = a.getInteger(R.styleable.RoundOverlayProgressView_maxProgress, 100);
        animationDuration = a.getInteger(R.styleable.RoundOverlayProgressView_animationDuration, 300);
        animate = a.getBoolean(R.styleable.RoundOverlayProgressView_animate, true);

        a.recycle();

        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null) setImageBitmap(bitmap);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(resId);
        setImageBitmap(bitmapDrawable.getBitmap());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = calculateStartAngle(currentProgress);
        if (startAngle == 0) return;

        RectF box = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(progressColor);

        float percentProgress = (float) currentProgress / (float) maxProgress;
        float sweepAngle = 360f - (360f * percentProgress);

        canvas.drawArc(box, startAngle, sweepAngle, true, paint);
    }

    private float calculateStartAngle(int progress) {
        if (progress > maxProgress)
            throw new IndexOutOfBoundsException("Current progress can't be bigger than maxProgress. If you did not specify max progress, either in xml or by setMaxProgress() it is automatically set to 100");

        float percentProgress = (float) progress / (float) maxProgress;
        float angle = 360f * percentProgress + 270f;
        return angle;
    }

    public void setProgress(int newProgress) {
        if (animate) {
            ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentProgress", currentProgress, newProgress);
            anim.setDuration(animationDuration);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        } else {
            setCurrentProgress(newProgress);
        }
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    private void setCurrentProgress(int progress) {
        currentProgress = progress;
        invalidate();
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public int progressColor;
        public int maxProgress;
        public int currentProgress;
        public int animationDuration;
        public boolean animate;

        public SavedState(Parcelable superState, int progressColor, int maxProgress, int currentProgress, int animationDuration, boolean animate) {
            super(superState);
            this.progressColor = progressColor;
            this.maxProgress = maxProgress;
            this.currentProgress = currentProgress;
            this.animationDuration = animationDuration;
            this.animate = animate;
        }

        private SavedState(Parcel in) {
            super(in);
            this.progressColor = in.readInt();
            this.maxProgress = in.readInt();
            this.currentProgress = in.readInt();
            this.animationDuration = in.readInt();
            this.animate = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progressColor);
            out.writeInt(maxProgress);
            out.writeInt(currentProgress);
            out.writeInt(animationDuration);
            out.writeInt(animate ? 1 : 0);
        }
    }

}

package com.hardygtw.travelmemories.views;

/**
 * Created by gavin on 02/03/2015.
 */

        import android.content.Context;
        import android.graphics.drawable.Drawable;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.AutoCompleteTextView;

        import com.hardygtw.travelmemories.R;

public class AutoCompleteView extends AutoCompleteTextView {
    // was the text just cleared?
    boolean justCleared = false;

    // if not set otherwise, the default clear listener clears the text in the
    // text view
    private OnClearListener defaultClearListener = new OnClearListener() {

        @Override
        public void onClear() {
            AutoCompleteTextView et = AutoCompleteView.this;
            et.setText("");
        }
    };

    private OnClearListener onClearListener = defaultClearListener;

    // The image we defined for the clear button
    public Drawable imgClearButton = getResources().getDrawable(
            R.drawable.ic_action_remove);

    public interface OnClearListener {
        void onClear();
    }

    /* Required methods, not used in this implementation */
    public AutoCompleteView(Context context) {
        super(context);
        init();
    }

    /* Required methods, not used in this implementation */
    public AutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* Required methods, not used in this implementation */
    public AutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        // Set the bounds of the button
        this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                imgClearButton, null);

        // if the clear button is pressed, fire up the handler. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                AutoCompleteView et = AutoCompleteView.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight()	- imgClearButton.getIntrinsicWidth()) {
                    onClearListener.onClear();
                    justCleared = true;
                }
                return false;
            }
        });
    }

    public void setImgClearButton(Drawable imgClearButton) {
        this.imgClearButton = imgClearButton;
    }

    public void setOnClearListener(final OnClearListener clearListener) {
        this.onClearListener = clearListener;
    }

    public void hideClearButton() {
        this.setCompoundDrawables(null, null, null, null);
    }

    public void showClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
    }

}
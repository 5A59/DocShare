package docsadapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shamanland.fab.ScrollDetector;

import zy.com.document.R;

/**
 * Created by zy on 15-12-23.
 */
public class MyShowHideOnScroll extends ScrollDetector implements Animation.AnimationListener{

    private final View mView;
    private final View mView2;
    private final int mShow;
    private final int mHide;
    private final int mShowDown;
    private final int mHideUp;

    public MyShowHideOnScroll(View view, View view2) {
        this(view, view2,
                com.shamanland.fab.R.anim.floating_action_button_show, com.shamanland.fab.R.anim.floating_action_button_hide,
                R.anim.view_show_down, R.anim.view_hide_up);
    }

    public MyShowHideOnScroll(View view, View view2, int animShow, int animHide, int animShowDown, int animHideUp) {
        super(view.getContext());
        this.mView = view;
        this.mView2 = view2;
        this.mShow = animShow;
        this.mHide = animHide;
        this.mShowDown = animShowDown;
        this.mHideUp = animHideUp;
    }

    public void onScrollDown() {
        if(this.mView.getVisibility() != View.VISIBLE) {
            this.mView.setVisibility(View.VISIBLE);
            this.mView2.setVisibility(View.VISIBLE);
            this.animate(this.mShow);
            this.animate2(this.mShowDown);
        }

    }

    public void onScrollUp() {
        if(this.mView.getVisibility() == View.VISIBLE) {
            this.mView.setVisibility(View.GONE);
            this.mView2.setVisibility(View.GONE);
            this.animate(this.mHide);
            this.animate2(this.mHideUp);
        }

    }

    private void animate(int anim) {
        if(anim != 0) {
            Animation a = AnimationUtils.loadAnimation(this.mView.getContext(), anim);
            a.setAnimationListener(this);
            this.mView.startAnimation(a);
            this.setIgnore(true);
        }

    }

    private void animate2(int anim) {
        if(anim != 0) {
            Animation a = AnimationUtils.loadAnimation(this.mView.getContext(), anim);
            a.setAnimationListener(this);
            this.mView2.startAnimation(a);
            this.setIgnore(true);
        }

    }

    public void onAnimationStart(Animation animation) {
    }

    public void onAnimationEnd(Animation animation) {
        this.setIgnore(false);
    }

    public void onAnimationRepeat(Animation animation) {
    }
}

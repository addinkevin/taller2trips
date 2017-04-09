package com.example.pc.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.animationTools.TouchImageView;
import com.example.pc.myapplication.singletons.ImagesSingleton;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity fullScreenAct;

    // constructor
    public FullScreenImageAdapter(Activity activity) {
        this.fullScreenAct = activity;
    }

    @Override
    public int getCount() {
        return ImagesSingleton.getInstance().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    private void setImgViewPager(View viewLayout,int position){
        TouchImageView imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        Bitmap myBitmap = ImagesSingleton.getInstance().get(position);
        imgDisplay.setImageBitmap(myBitmap);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) fullScreenAct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        //Set the current Image to ImageView in ViewPager
        setImgViewPager(viewLayout, position);
        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}

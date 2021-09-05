package com.adaatham.suthar.common;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.adaatham.suthar.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
 
public class MyAdapter extends PagerAdapter {
 
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private OnImagePressedListener onImagePressedListener;
 
    public MyAdapter(Context context, ArrayList<String> images,OnImagePressedListener onImagePressedListener) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
        this.onImagePressedListener = onImagePressedListener;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
 
    @Override
    public int getCount() {
        return images.size();
    }
 
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);

        myImageLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(onImagePressedListener!=null){
                    onImagePressedListener.onImagePressed(v,event);
                }

                return true;
            }
        });

        SimpleDraweeView myImage = (SimpleDraweeView) myImageLayout
                .findViewById(R.id.image);
        myImage.setImageURI(Uri.parse(images.get(position)));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
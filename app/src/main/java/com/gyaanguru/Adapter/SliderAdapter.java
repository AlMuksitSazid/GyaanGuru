package com.gyaanguru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyaanguru.Models.SliderData;
import com.gyaanguru.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderData> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context, List<SliderData> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        final SliderData sliderData = mSliderItems.get(position);
      //  Picasso.get().load(sliderItem.getImgUrl()).into(viewHolder.imageView);

        Glide.with(viewHolder.itemView)
                .load(sliderData.getImgId())
                .centerCrop()
                .fitCenter()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageView;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.idIVimage);
            this.itemView = itemView;
        }
    }
}
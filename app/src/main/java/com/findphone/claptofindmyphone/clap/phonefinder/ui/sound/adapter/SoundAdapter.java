package com.findphone.claptofindmyphone.clap.phonefinder.ui.sound.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ItemSoundBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.model.SoundModel;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseViewHolder;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SoundModel> lists;
    private IClickSound iClickSound;

    public interface IClickSound {
        void onClick(SoundModel model);
    }


    public SoundAdapter(Context context, List<SoundModel> lists, IClickSound iClickSound) {
        this.context = context;
        this.lists = lists;
        this.iClickSound = iClickSound;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSoundBinding binding = ItemSoundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SoundViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class SoundViewHolder extends BaseViewHolder {
        ItemSoundBinding binding;

        public SoundViewHolder(@NonNull @NotNull ItemSoundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bind(int position) {
            SoundModel data = lists.get(position);
            binding.ivAvatar.setImageDrawable(context.getDrawable(data.getImageSound()));
            binding.btnSetSound1.setOnClickListener(v -> {
                setSelectSound(data.isoSound);
                iClickSound.onClick(data);
                notifyDataSetChanged();
            });
            if (data.getCheck()) {
                binding.btnSetSound1.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_item_sound_top_selected));
            } else {
                binding.btnSetSound1.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_item_sound_top));
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectSound(String code) {
        for (SoundModel data : lists) {
            if (data.getIsoSound().equals(code)) {
                data.setCheck(true);
            } else {
                data.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

}

package com.findphone.claptofindmyphone.clap.phonefinder.ui.language.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ItemLanguageBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.model.LanguageModel;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseViewHolder;

import java.util.List;

public class LanguageStartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<LanguageModel> lists;
    private IClickLanguage iClickLanguage;

    public LanguageStartAdapter(Context context, List<LanguageModel> lists, IClickLanguage iClickLanguage) {
        this.context = context;
        this.lists = lists;
        this.iClickLanguage = iClickLanguage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLanguageBinding binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LanguageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class LanguageViewHolder extends BaseViewHolder {

        private ItemLanguageBinding binding;


        public LanguageViewHolder(ItemLanguageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void bind(int position) {
            LanguageModel data = lists.get(position);
            if (data == null) {
                return;
            }
            binding.ivAvatar.setImageDrawable(context.getDrawable(data.getImage()));
            binding.tvTitle.setText(data.getLanguageName());
            if (data.getCheck()) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#F1EFFB"));
                binding. v2.setVisibility(View.VISIBLE);
            } else {
                binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.v2.setVisibility(View.GONE);
            }
            binding.getRoot().setOnClickListener(v -> {
                setSelectLanguage(data.isoLanguage);
                iClickLanguage.onClick(data);
                notifyDataSetChanged();
            });
        }
    }

    public void setSelectLanguage(String code) {
        for (LanguageModel data : lists) {
            if (data.getIsoLanguage().equals(code)) {
                data.setCheck(true);
            } else {
                data.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

    public interface IClickLanguage {
        void onClick(LanguageModel model);
    }
}

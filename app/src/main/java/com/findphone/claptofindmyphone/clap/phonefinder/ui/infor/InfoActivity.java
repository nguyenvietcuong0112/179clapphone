package com.findphone.claptofindmyphone.clap.phonefinder.ui.infor;


import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityInfoTemplateBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;

public class InfoActivity extends BaseActivity {

    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        ActivityInfoTemplateBinding binding = ActivityInfoTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBackButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}



package com.findphone.claptofindmyphone.clap.phonefinder.ui.language;

import android.view.View;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityLanguageSettingsBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.language.adapter.LanguageStartAdapter;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.mallegan.ads.util.Admob;

public class LanguageSettingActivity extends BaseActivity {
    String codeLang = "en";
    LanguageStartAdapter languageAdapter;
    ActivityLanguageSettingsBinding binding;

    @Override
    public void bind() {
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
        SystemUtil.setLocale(this);
        binding = ActivityLanguageSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        languageAdapter = new LanguageStartAdapter(this, Constant.getLanguage(), data -> {
            codeLang = data.getIsoLanguage();
        });
        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.rvLanguage.setAdapter(languageAdapter);
        binding.ivSelect.setOnClickListener(v -> {
            SystemUtil.saveLocale(this, codeLang);
            onBackPressed();
        });
        binding.ivSelect.setVisibility(View.GONE);
        loadBanner();
    }
    private void loadBanner() {
        Admob.getInstance().loadCollapsibleBanner(
                LanguageSettingActivity.this,
                getString(R.string.banner_collap),
                "bottom"
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
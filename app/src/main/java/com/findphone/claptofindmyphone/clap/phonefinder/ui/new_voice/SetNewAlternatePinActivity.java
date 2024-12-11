package com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.findphone.claptofindmyphone.clap.phonefinder.R;


public class SetNewAlternatePinActivity extends AppCompatActivity {
    public static final String TAG = "Set A P Activity";
    static MyViewPagerAdapter myViewPagerAdapter;
    static ViewPager viewPagerSetAlternatePin;
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_set_alternate_pin);


        this.relativeLayout = (RelativeLayout) findViewById(R.id.root_layout_set_alternative_password);
        Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.bg)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                Log.e("Set A P Activity", "Root Layout background image failed to load");
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                SetNewAlternatePinActivity.this.relativeLayout.setBackground(drawable);
                return true;
            }
        }).submit();
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_alternate_pin);
        viewPagerSetAlternatePin = viewPager;
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        viewPagerSetAlternatePin.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return true;
            }
        });
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPagerSetAlternatePin.setOffscreenPageLimit(0);
        viewPagerSetAlternatePin.setAdapter(myViewPagerAdapter);
    }


    public static class SetAlternatePIN extends Fragment implements View.OnClickListener {
        AppCompatButton btn_confirm_pass;
        AppCompatEditText et_pass;
        LinearLayout ll_clear_all_text;
        LinearLayout ll_clear_character;
        ImageView show_hide_pass;

        ImageView back_button;
        LinearLayout[] linearLayout = new LinearLayout[10];
        String[] ll_view_tag = {"ll_0", "ll_1", "ll_2", "ll_3", "ll_4", "ll_5", "ll_6", "ll_7", "ll_8", "ll_9"};

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_set_alternate_pin, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            initViews(view);
            this.back_button.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());

            this.et_pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length() >= 4) {
                        SetAlternatePIN.this.enableConfirmPassButton();
                    } else {
                        SetAlternatePIN.this.disableConfirmPassButton();
                    }
                }
            });
        }

        void enableConfirmPassButton() {
//            this.btn_confirm_pass.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_primary, null));
//            this.btn_confirm_pass.setTextColor(-1);
        }

        void disableConfirmPassButton() {
//            this.btn_confirm_pass.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_primary_disabled, null));
//            this.btn_confirm_pass.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_success_stroke, null));
        }

        public static SetAlternatePIN getInstance() {
            return new SetAlternatePIN();
        }

        public void initViews(View view) {
            this.et_pass = (AppCompatEditText) view.findViewById(R.id.et_password);
            this.back_button = (ImageView) view.findViewById(R.id.back_button);
            this.ll_clear_all_text = (LinearLayout) view.findViewWithTag("ll_clear_all_text");
            this.ll_clear_character = (LinearLayout) view.findViewWithTag("ll_clear_character");
            this.show_hide_pass = (ImageView) view.findViewWithTag("show_hide_pass");
            this.btn_confirm_pass = (AppCompatButton) view.findViewWithTag("btn_confirm_pin");
            this.ll_clear_all_text.setOnClickListener(this);
            this.ll_clear_character.setOnClickListener(this);
            this.show_hide_pass.setOnClickListener(this);
            this.btn_confirm_pass.setOnClickListener(this);
            int i = 0;
            while (true) {
                String[] strArr = this.ll_view_tag;
                if (i >= strArr.length) {
                    return;
                }
                this.linearLayout[i] = (LinearLayout) view.findViewWithTag(strArr[i]);
                this.linearLayout[i].setOnClickListener(this);
                i++;
            }
        }

        @Override
        public void onClick(View view) {
            String str = (String) view.getTag();
            int i = 0;
            while (true) {
                String[] strArr = this.ll_view_tag;
                if (i < strArr.length) {
                    if (str.equalsIgnoreCase(strArr[i])) {
                        Log.e("Set A P Activity", i + " Button clicked");
                        if (this.et_pass.getText() != null) {
                            this.et_pass.setText(et_pass.getText().toString() + i);
                            StringBuilder sb = new StringBuilder("edit text new value is ");
                            sb.append((Object) this.et_pass.getText());
                            Log.e("Set A P Activity", sb.toString());
                            return;
                        }
                        return;
                    }
                    i++;
                } else {
                    String obj = view.getTag().toString();
                    obj.hashCode();
                    char c = 65535;
                    switch (obj.hashCode()) {
                        case -1034734036:
                            if (obj.equals("show_hide_pass")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 169284344:
                            if (obj.equals("ll_clear_character")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 709073459:
                            if (obj.equals("btn_confirm_pin")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 783438684:
                            if (obj.equals("ll_clear_all_text")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            Log.e("Set A P Activity", String.valueOf(this.et_pass.getInputType()));
                            if (this.et_pass.getInputType() == 18) {
                                this.et_pass.setInputType(16);
                                this.show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.ic_show));
                                return;
                            }
                            this.et_pass.setInputType(18);
                            this.show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
                            return;
                        case 1:
                            if (this.et_pass.getText() != null && this.et_pass.getText().toString().length() > 0) {
                                this.et_pass.setText(this.et_pass.getText().toString().substring(0, this.et_pass.getText().toString().length() - 1));
                                return;
                            }
                            Log.e("Set A P Activity", "Clear Last Character Clicked : Edit text is null or length is 0");
                            return;
                        case 2:
                            if (this.et_pass.getText() == null || this.et_pass.getText().toString().length() < 4) {
                                return;
                            }
                            Log.e("Set A P Activity", "btn confirmd clicked");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SetNewAlternatePinActivity.viewPagerSetAlternatePin.setCurrentItem(2, true);
                                    if (SetAlternatePIN.this.getActivity() != null) {
                                        SetAlternatePIN.this.getActivity().getSharedPreferences("PASSWORDS", 0).edit().putString("temp_pin", SetAlternatePIN.this.et_pass.getText().toString()).apply();
                                    }
                                }
                            }, 50L);
                            return;
                        case 3:
                            if (this.et_pass.getText() != null && this.et_pass.getText().toString().length() > 0) {
                                this.et_pass.setText("");
                                return;
                            } else {
                                Log.e("Set A P Activity", "Clear All Text Clicked : Edit text is null or length is 0");
                                return;
                            }
                        default:
                            return;
                    }
                }
            }
        }
    }


    public static class ConfirmAlternatePIN extends Fragment implements View.OnClickListener {
        AppCompatButton btn_confirm_pass;
        AppCompatEditText et_pass;
        LinearLayout ll_clear_all_text;
        LinearLayout ll_clear_character;
        ImageView show_hide_pass;
        LinearLayout[] linearLayout = new LinearLayout[10];
        String[] ll_view_tag = {"ll_0", "ll_1", "ll_2", "ll_3", "ll_4", "ll_5", "ll_6", "ll_7", "ll_8", "ll_9"};

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_confirm_alternate_pin, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            initViews(view);
            this.et_pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length() >= 4 && ConfirmAlternatePIN.this.et_pass.getText() != null && ConfirmAlternatePIN.this.getActivity() != null && ConfirmAlternatePIN.this.et_pass.getText().toString().equalsIgnoreCase(ConfirmAlternatePIN.this.getActivity().getSharedPreferences("PASSWORDS", 0).getString("temp_pin", ConfirmAlternatePIN.this.et_pass.getText().toString()))) {
                        ConfirmAlternatePIN.this.enableConfirmPassButton();
                    } else if (editable.toString().length() >= 4) {
                        Toast.makeText(ConfirmAlternatePIN.this.getActivity(), "PIN doest not match", Toast.LENGTH_SHORT).show();
                        ConfirmAlternatePIN.this.disableConfirmPassButton();
                    } else {
                        ConfirmAlternatePIN.this.disableConfirmPassButton();
                    }
                }
            });
        }

        void enableConfirmPassButton() {
//            this.btn_confirm_pass.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_primary, null));
//            this.btn_confirm_pass.setTextColor(-1);
        }

        void disableConfirmPassButton() {
//            this.btn_confirm_pass.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_primary_disabled, null));
//            this.btn_confirm_pass.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_success_stroke, null));
        }

        public static ConfirmAlternatePIN getInstance() {
            return new ConfirmAlternatePIN();
        }

        public void initViews(View view) {
            this.et_pass = (AppCompatEditText) view.findViewById(R.id.et_password);
            this.ll_clear_all_text = (LinearLayout) view.findViewWithTag("ll_clear_all_text");
            this.ll_clear_character = (LinearLayout) view.findViewWithTag("ll_clear_character");
            this.show_hide_pass = (ImageView) view.findViewWithTag("show_hide_pass");
            this.btn_confirm_pass = (AppCompatButton) view.findViewWithTag("btn_done");
            this.ll_clear_all_text.setOnClickListener(this);
            this.ll_clear_character.setOnClickListener(this);
            this.show_hide_pass.setOnClickListener(this);
            this.btn_confirm_pass.setOnClickListener(this);
            int i = 0;
            while (true) {
                String[] strArr = this.ll_view_tag;
                if (i >= strArr.length) {
                    return;
                }
                this.linearLayout[i] = (LinearLayout) view.findViewWithTag(strArr[i]);
                this.linearLayout[i].setOnClickListener(this);
                i++;
            }
        }

        @Override
        public void onClick(View view) {
            Editable text = null;
            String str = (String) view.getTag();
            int i = 0;
            while (true) {
                String[] strArr = this.ll_view_tag;
                if (i < strArr.length) {
                    if (str.equalsIgnoreCase(strArr[i])) {
                        Log.e("Set A P Activity", i + " Button clicked");
                        if (this.et_pass.getText() != null) {
                            this.et_pass.setText(et_pass.getText().toString() + i);
                            StringBuilder sb = new StringBuilder("edit text new value is ");
                            sb.append((Object) this.et_pass.getText());
                            Log.e("Set A P Activity", sb.toString());
                            return;
                        }
                        return;
                    }
                    i++;
                } else {
                    String obj = view.getTag().toString();
                    obj.hashCode();
                    char c = 65535;
                    switch (obj.hashCode()) {
                        case -1034734036:
                            if (obj.equals("show_hide_pass")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 169284344:
                            if (obj.equals("ll_clear_character")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 783438684:
                            if (obj.equals("ll_clear_all_text")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 2107963269:
                            if (obj.equals("btn_done")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            Log.e("Set A P Activity", String.valueOf(this.et_pass.getInputType()));
                            if (this.et_pass.getInputType() == 18) {
                                this.et_pass.setInputType(16);
                                this.show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.ic_show));
                                return;
                            }
                            this.et_pass.setInputType(18);
                            this.show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
                            return;
                        case 1:
                            if (this.et_pass.getText() != null && this.et_pass.getText().toString().length() > 0) {
                                this.et_pass.setText(this.et_pass.getText().toString().substring(0, this.et_pass.getText().toString().length() - 1));
                                return;
                            }
                            Log.e("Set A P Activity", "Clear Last Character Clicked : Edit text is null or length is 0");
                            return;
                        case 2:
                            if (this.et_pass.getText() != null && this.et_pass.getText().toString().length() > 0) {
                                this.et_pass.setText("");
                                return;
                            } else {
                                Log.e("Set A P Activity", "Clear All Text Clicked : Edit text is null or length is 0");
                                return;
                            }
                        case 3:
                            if (this.et_pass.getText() == null || this.et_pass.getText().toString().length() < 4) {
                                return;
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SetNewAlternatePinActivity.viewPagerSetAlternatePin.setCurrentItem(2, true);
                                    if (ConfirmAlternatePIN.this.getActivity() == null || !ConfirmAlternatePIN.this.et_pass.getText().toString().equalsIgnoreCase(ConfirmAlternatePIN.this.getActivity().getSharedPreferences("PASSWORDS", 0).getString("temp_pin", ConfirmAlternatePIN.this.et_pass.getText().toString()))) {
                                        return;
                                    }
                                    ConfirmAlternatePIN.this.getActivity().getSharedPreferences("PASSWORDS", 0).edit().putString("user_pin", ConfirmAlternatePIN.this.et_pass.getText().toString()).apply();
                                    Intent intent = new Intent();
                                    intent.putExtra("result", "result_ok");
                                    ConfirmAlternatePIN.this.getActivity().setResult(-1, intent);
                                    ConfirmAlternatePIN.this.getActivity().finish();
                                }
                            }, 50L);
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }


    public static class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        public MyViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            if (i != 0) {
                if (i == 1) {
                    return ConfirmAlternatePIN.getInstance();
                }
                return SetAlternatePIN.getInstance();
            }
            return SetAlternatePIN.getInstance();
        }
    }
}

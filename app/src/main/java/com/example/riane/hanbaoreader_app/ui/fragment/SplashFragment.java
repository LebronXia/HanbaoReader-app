package com.example.riane.hanbaoreader_app.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseFragment;
import com.example.riane.hanbaoreader_app.ui.activity.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Riane on 2016/4/29.
 */
public class SplashFragment extends BaseFragment {
    @Bind(R.id.iv_main_left_head)
    CircleImageView mIvMainLeftHead;
    @Bind(R.id.logo_name)
    TextView mLogoName;
    @Bind(R.id.logo_text)
    TextView mLogoText;
    @Bind(R.id.splash_id)
    RelativeLayout mSplashId;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_splash, container, false);

        ButterKnife.bind(this, view);

        try {
            String str = getVersionName();
            mLogoText.setText("当前版本号：" + str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(3000);
        mSplashId.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent it = new Intent(getActivity(), MainActivity.class);
                startActivity(it);
                getActivity().finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private String getVersionName() throws PackageManager.NameNotFoundException {
        //获取packageManager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
        String version = packInfo.versionName;
        return version;

    }
}

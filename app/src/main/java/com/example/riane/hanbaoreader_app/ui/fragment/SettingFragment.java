package com.example.riane.hanbaoreader_app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.ui.activity.SettingsActivity;
import com.example.riane.hanbaoreader_app.util.SPUtils;

/**
 * Created by Riane on 2016/4/14.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    public static final String PREFERENCE_FIFE_NAME = "note.settings";
    private CheckBoxPreference cardLayoutPreference;
    private CheckBoxPreference switchPreference;
    private SettingsActivity settingsActivity;
    private boolean isCard = false;

    public static SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() != null && getActivity() instanceof SettingsActivity){
            this.settingsActivity = (SettingsActivity) getActivity();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        cardLayoutPreference = (CheckBoxPreference) findPreference(getString(R.string.card_note_item_layout_key));
        cardLayoutPreference.setOnPreferenceChangeListener(this);
        if ((boolean)SPUtils.get(getActivity(), "IS_CARD", false) == true){
            cardLayoutPreference.setChecked(true);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == cardLayoutPreference){
            isCard = (boolean)SPUtils.get(getActivity(), "IS_CARD", false);
            isCard = !isCard;
            SPUtils.put(getActivity(),"IS_CARD",isCard);
            return true;
        }
        return false;
    }
}

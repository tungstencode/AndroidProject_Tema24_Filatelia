package com.partenie.alex.filatelie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    SeekBar seekBar;
    TextView textView;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.SETTING_PREFERENCE), Context.MODE_PRIVATE);
        seekBar=view.findViewById(R.id.grid_layout_seekbar);
        textView=view.findViewById(R.id.grid_text);

        textView.setText(String.valueOf(sharedPreferences.getInt(getString(R.string.ITEM_PER_COLLUMN_KEY),1)+1));
        seekBar.setProgress(sharedPreferences.getInt(getString(R.string.ITEM_PER_COLLUMN_KEY),1));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(String.valueOf(i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.SETTING_PREFERENCE), Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt(getString(R.string.ITEM_PER_COLLUMN_KEY),seekBar.getProgress()).apply();
            }
        });



        return view;
    }
}

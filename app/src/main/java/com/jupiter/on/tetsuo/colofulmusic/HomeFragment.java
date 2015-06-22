package com.jupiter.on.tetsuo.colofulmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    Switch mSwitch;
    TextView switchStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mSwitch = (Switch) rootView.findViewById(R.id.switch1);
        switchStatus = (TextView) rootView.findViewById(R.id.switchStatus);

        if(AudioClassificationService.isOn){
            mSwitch.setChecked(true);
        }else {
            mSwitch.setChecked(false);
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("Switch is currently ON");
                    if (!MainActivity.getMainActivity().isServiceRunning()) {
                        MainActivity.getMainActivity().startAudioClassificationService();
                        MainActivity.getMainActivity().setIsServiceRunning(true);
                    }
                } else {
                    if (MainActivity.getMainActivity().isServiceRunning()) {
                        MainActivity.getMainActivity().stopAudioClassificationService();
                        MainActivity.getMainActivity().setIsServiceRunning(false);

                    }
                    switchStatus.setText("Switch is currently OFF");
                }

            }
        });

        //check the current state before we display the screen
        if (mSwitch.isChecked()) {
            switchStatus.append("ON");
        } else {
            switchStatus.append("OFF");
        }

        //MainActivity.getMainActivity().startAudioClassificationService();
        return rootView;
    }


}


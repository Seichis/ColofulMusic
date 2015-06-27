package com.jupiter.on.tetsuo.colofulmusic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jupiter.on.tetsuo.colofulmusic.sensorProc.Constants;

public class ColorPickFragment extends Fragment{
   Button setIpButton;
    SharedPreferences.Editor editor;
    EditText ipEditText,portEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pick_color, container, false);
        setIpButton=(Button)rootView.findViewById(R.id.buttonIP);
        ipEditText=(EditText)rootView.findViewById(R.id.editTextIPAddress);
        portEditText=(EditText)rootView.findViewById(R.id.editTextPortNumber);
        setIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipEditText.getText().length() < 7 || portEditText.getText().length()<2) {
                    Toast.makeText(MainActivity.getMainActivity().getApplicationContext(), "Enter correct IP and port", Toast.LENGTH_LONG).show();
                } else {
                    editor = MainActivity.getMainActivity().getSharedPreferences(Constants.MY_PREFS_NAME, 0).edit();
                    editor.putString("ip", ipEditText.getText().toString());
                    editor.putString("portNumber",portEditText.getText().toString());
                    editor.commit();
                }
            }
        });


        return rootView;
    }





}

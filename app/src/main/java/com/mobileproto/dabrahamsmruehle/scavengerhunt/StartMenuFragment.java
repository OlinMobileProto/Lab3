package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;

public class StartMenuFragment extends Fragment
{

    @Bind(R.id.continue_button)
    Button continueButton;
    @Bind(R.id.new_hunt_button)
    Button newHuntButton;
    @Bind(R.id.settings_button)
    Button settingsButton;
    @Bind(R.id.help_button)
    Button helpButton;

    public StartMenuFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_start_menu, container, false);

        return view;
    }
}

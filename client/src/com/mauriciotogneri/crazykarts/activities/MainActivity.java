package com.mauriciotogneri.crazykarts.activities;

import com.mauriciotogneri.crazykarts.R;
import com.mauriciotogneri.crazykarts.screens.home.HomeScreen;
import com.mauriciotogneri.crazykarts.util.Preferences;

public class MainActivity extends BaseActivity
{
	@Override
	protected void onInitialize()
	{
		Preferences.initialize(this);
	}
	
	@Override
	protected BaseFragment getHomeFragment()
	{
		return new HomeScreen();
	}
	
	@Override
	protected int getLayoutId()
	{
		return R.layout.activity_main;
	}
	
	@Override
	protected int getContainerId()
	{
		return R.id.fragment_container;
	}
	
	@Override
	protected void closeRequested()
	{
		finish();
	}
}
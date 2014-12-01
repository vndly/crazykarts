package com.mauriciotogneri.crazykarts.input;

public class InputEvent
{
	public boolean left = false;
	public boolean right = false;
	
	public void press(float x, int resolutionX)
	{
		if (x < (resolutionX / 2))
		{
			this.left = true;
		}
		else
		{
			this.right = true;
		}
	}
	
	public void release(float x, int resolutionX)
	{
		if (x < (resolutionX / 2))
		{
			this.left = false;
		}
		else
		{
			this.right = false;
		}
	}
	
	public void clear()
	{
		this.left = false;
		this.right = false;
	}
}
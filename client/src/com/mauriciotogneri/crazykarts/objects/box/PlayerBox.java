package com.mauriciotogneri.crazykarts.objects.box;

import android.os.Vibrator;
import com.mauriciotogneri.crazykarts.engine.Camera;
import com.mauriciotogneri.crazykarts.input.InputEvent;
import com.mauriciotogneri.crazykarts.objects.level.Level;

public class PlayerBox extends Box
{
	private final Vibrator vibrator;
	
	private boolean vibrating = false;
	
	public PlayerBox(Camera camera, Level level, Vibrator vibrator, float x, float y, int color)
	{
		super(camera, level, x, y, color);
		
		this.vibrator = vibrator;
	}
	
	public void update(double delta, InputEvent input)
	{
		if (!finished())
		{
			updateDirection(input.left, input.right);
			updatePosition(delta);
			
			if (collide())
			{
				if (!this.vibrating)
				{
					this.vibrator.vibrate(10000);
					this.vibrating = true;
				}
			}
			else
			{
				this.vibrating = false;
				this.vibrator.cancel();
			}
		}
	}
	
	@Override
	public synchronized void restart()
	{
		super.restart();
		
		pause();
	}
	
	public void pause()
	{
		this.vibrating = false;
		this.vibrator.cancel();
	}
}
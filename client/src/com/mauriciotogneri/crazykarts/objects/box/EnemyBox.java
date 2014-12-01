package com.mauriciotogneri.crazykarts.objects.box;

import com.mauriciotogneri.crazykarts.engine.Camera;
import com.mauriciotogneri.crazykarts.objects.level.Level;

public class EnemyBox extends Box
{
	private boolean left = false;
	private boolean right = false;
	
	private final Object lock = new Object();
	
	public EnemyBox(Camera camera, Level level, float x, float y, int color)
	{
		super(camera, level, x, y, color);
	}
	
	public void update(double delta)
	{
		if (!finished())
		{
			synchronized (this.lock)
			{
				left(this.left);
				right(this.right);
			}
			
			updatePosition(delta);
		}
	}
	
	@Override
	public void restart()
	{
		super.restart();
		
		synchronized (this.lock)
		{
			this.left = false;
			this.right = false;
		}
	}
	
	public void update(float x, float y, boolean left, boolean right)
	{
		synchronized (this.lock)
		{
			this.left = left;
			this.right = right;
		}
		
		updatePosition(x, y);
	}
}
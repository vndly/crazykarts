package com.mauriciotogneri.crazykarts.objects.box;

import com.mauriciotogneri.crazykarts.engine.Camera;
import com.mauriciotogneri.crazykarts.objects.level.Level;

public class EnemyBox extends Box
{
	public EnemyBox(Camera camera, Level level, float x, float y, int color)
	{
		super(camera, level, x, y, color);
	}
	
	public void update(double delta)
	{
		if (!finished())
		{
			updatePosition(delta);
		}
	}
	
	public void update(float x, float y, boolean left, boolean right)
	{
		updatePosition(x, y);
		updateDirection(left, right);
	}
}
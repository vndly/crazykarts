package com.mauriciotogneri.crazykarts.objects.box;

import com.mauriciotogneri.crazykarts.engine.Camera;
import com.mauriciotogneri.crazykarts.engine.Renderer;
import com.mauriciotogneri.crazykarts.engine.Sprite;
import com.mauriciotogneri.crazykarts.objects.level.Level;
import com.mauriciotogneri.crazykarts.shapes.Shape;
import com.mauriciotogneri.crazykarts.shapes.Square;

public class Box
{
	private final Camera camera;
	private final Level level;
	
	private final float initialX;
	private final float initialY;
	
	private final Sprite sprite;
	
	private boolean left = false;
	private boolean right = false;
	
	private final Object lock = new Object();
	
	private static final float SPEED_X = 20;
	private static final float SPEED_Y = 30;
	
	private static final int SIZE = 4;
	
	public Box(Camera camera, Level level, float x, float y, int color)
	{
		this.camera = camera;
		this.level = level;
		
		this.initialX = x;
		this.initialY = y;
		
		Shape square = new Square(Box.SIZE, color);
		this.sprite = new Sprite(square, x, y);
	}
	
	public void restart()
	{
		this.sprite.x = this.initialX;
		this.sprite.y = this.initialY;
	}
	
	protected void left(boolean value)
	{
		this.left = value;
	}
	
	protected void right(boolean value)
	{
		this.right = value;
	}
	
	protected void updatePosition(double delta)
	{
		synchronized (this.lock)
		{
			if (this.left)
			{
				this.sprite.x -= delta * getSpeed(Box.SPEED_X);
			}
			else if (this.right)
			{
				this.sprite.x += delta * getSpeed(Box.SPEED_X);
			}
			
			this.sprite.y += delta * getSpeed(Box.SPEED_Y);
			
			if (this.sprite.y < 0)
			{
				this.sprite.y = 0;
			}
			else if (this.sprite.y > (Renderer.RESOLUTION_Y - Box.SIZE))
			{
				this.sprite.y = Renderer.RESOLUTION_Y - Box.SIZE;
			}
		}
	}
	
	public boolean finished()
	{
		return this.level.finished(this.sprite);
	}
	
	public float getX()
	{
		synchronized (this.lock)
		{
			return this.sprite.x;
		}
	}
	
	public float getY()
	{
		synchronized (this.lock)
		{
			return this.sprite.y;
		}
	}
	
	protected void updatePosition(float x, float y)
	{
		synchronized (this.lock)
		{
			if (x > this.sprite.x)
			{
				this.sprite.x = x;
				this.sprite.y = y;
			}
		}
	}
	
	protected boolean collide()
	{
		return (this.level.collide(this.sprite));
	}
	
	private float getSpeed(float baseSpeed)
	{
		float result = baseSpeed;
		
		if (collide())
		{
			result *= 0.5f;
		}
		
		return result;
	}
	
	public void render(Renderer renderer)
	{
		synchronized (this.lock)
		{
			if (this.camera.isInside(this.sprite))
			{
				this.sprite.render(renderer);
			}
		}
	}
}
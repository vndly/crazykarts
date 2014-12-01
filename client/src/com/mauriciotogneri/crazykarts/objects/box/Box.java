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
	
	private boolean finished = false;
	
	private static final float SPEED_X = 20;
	private static final float SPEED_Y = 30;
	
	public static final int SIZE = 4;
	
	public Box(Camera camera, Level level, float x, float y, int color)
	{
		this.camera = camera;
		this.level = level;
		
		this.initialX = x;
		this.initialY = y;
		
		Shape square = new Square(Box.SIZE, color);
		this.sprite = new Sprite(square, x, y);
	}
	
	public synchronized void restart()
	{
		this.sprite.x = this.initialX;
		this.sprite.y = this.initialY;
		
		this.finished = false;
		
		this.left = false;
		this.right = false;
	}
	
	protected synchronized void updatePosition(double delta)
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
		
		if (this.sprite.x < 0)
		{
			this.sprite.x = 0;
		}
		else if (this.sprite.x > (Renderer.RESOLUTION_X - Box.SIZE))
		{
			this.sprite.x = Renderer.RESOLUTION_X - Box.SIZE;
		}
	}
	
	public synchronized boolean finished()
	{
		return this.finished || (this.finished = this.level.finished(this.sprite));
	}
	
	public synchronized float getX()
	{
		return this.sprite.x;
	}
	
	public synchronized float getY()
	{
		return this.sprite.y;
	}
	
	protected synchronized void updatePosition(float x, float y)
	{
		if (y > this.sprite.y)
		{
			this.sprite.x = x;
			this.sprite.y = y;
		}
	}
	
	protected synchronized void updateDirection(boolean left, boolean right)
	{
		this.left = left;
		this.right = right;
	}
	
	protected synchronized boolean collide()
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
	
	public synchronized void render(Renderer renderer)
	{
		if (this.camera.isInside(this.sprite))
		{
			this.sprite.render(renderer);
		}
	}
}
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
	
	private float accelerationX = 0;
	
	private boolean finished = false;
	
	private static final float ACCELERATION_X_RATE = 100;
	private static final float DECELERATION_X_RATE = 0.1f;
	private static final float MAX_ACCELERATION_X = 40;
	
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
		
		this.accelerationX = 0;
	}
	
	protected synchronized void updatePosition(double delta)
	{
		if (this.left)
		{
			this.accelerationX -= Box.ACCELERATION_X_RATE * delta;
		}
		else if (this.right)
		{
			this.accelerationX += Box.ACCELERATION_X_RATE * delta;
		}
		else
		{
			this.accelerationX *= Math.pow(Box.DECELERATION_X_RATE, delta);
		}
		
		if (this.accelerationX > Box.MAX_ACCELERATION_X)
		{
			this.accelerationX = Box.MAX_ACCELERATION_X;
		}
		else if (this.accelerationX < -Box.MAX_ACCELERATION_X)
		{
			this.accelerationX = -Box.MAX_ACCELERATION_X;
		}
		
		this.sprite.x += getSpeed(this.accelerationX * delta);
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
	
	private double getSpeed(double baseSpeed)
	{
		double result = baseSpeed;
		
		if (collide())
		{
			result *= 0.5;
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
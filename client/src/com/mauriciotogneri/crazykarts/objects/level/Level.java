package com.mauriciotogneri.crazykarts.objects.level;

import com.mauriciotogneri.crazykarts.engine.Camera;
import com.mauriciotogneri.crazykarts.engine.Renderer;
import com.mauriciotogneri.crazykarts.engine.Sprite;

public class Level
{
	private final Camera camera;
	private final LevelDefinition levelDefinition;
	
	public Level(Camera camera, LevelDefinition levelDefinition)
	{
		this.camera = camera;
		this.levelDefinition = levelDefinition;
	}
	
	public boolean finished(Sprite sprite)
	{
		return this.levelDefinition.finished(sprite);
	}
	
	public boolean collide(Sprite sprite)
	{
		boolean result = false;
		
		for (Sprite obstacle : this.levelDefinition.getCollisionableSprites())
		{
			if (obstacle.collide(sprite))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public void render(Renderer renderer)
	{
		for (Sprite sprite : this.levelDefinition.getNonCollisionableSprites())
		{
			if (this.camera.isInside(sprite))
			{
				sprite.render(renderer);
			}
		}
		
		for (Sprite sprite : this.levelDefinition.getCollisionableSprites())
		{
			if (this.camera.isInside(sprite))
			{
				sprite.render(renderer);
			}
		}
	}
}
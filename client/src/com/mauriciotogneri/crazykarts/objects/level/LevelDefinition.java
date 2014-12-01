package com.mauriciotogneri.crazykarts.objects.level;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.content.Context;
import android.graphics.Color;
import com.mauriciotogneri.crazykarts.engine.Renderer;
import com.mauriciotogneri.crazykarts.engine.Sprite;
import com.mauriciotogneri.crazykarts.shapes.Rectangle;
import com.mauriciotogneri.crazykarts.shapes.Shape;
import com.mauriciotogneri.crazykarts.util.FileUtils;

public class LevelDefinition
{
	private final int length;
	private final int laps;
	
	private final List<Sprite> base = new ArrayList<Sprite>();
	private final List<Sprite> collisionableSprites = new ArrayList<Sprite>();
	private final List<Sprite> nonCollisionableSprites = new ArrayList<Sprite>();
	
	public static final int WALL_COLOR = Color.argb(255, 90, 110, 120);
	
	private static final int WALL_WIDTH = 4;
	
	public LevelDefinition(Context context, int mapId, int laps)
	{
		Node root = getRoot(FileUtils.getInputStream(context, mapId));
		
		this.laps = laps;
		this.length = getLength(root);
		
		Shape wall = new Rectangle(LevelDefinition.WALL_WIDTH, (this.length * laps) + (Renderer.RESOLUTION_Y * 2), LevelDefinition.WALL_COLOR);
		Sprite wallLeft = new Sprite(wall, 0, -Renderer.RESOLUTION_Y);
		Sprite wallRight = new Sprite(wall, Renderer.RESOLUTION_X - LevelDefinition.WALL_WIDTH, -Renderer.RESOLUTION_Y);
		
		add(wallLeft);
		add(wallRight);
		
		readMap(root);
	}
	
	private int getLength(Node root)
	{
		Element element = (Element)root;
		
		return Integer.parseInt(element.getAttribute("width"));
	}
	
	private void readMap(Node root)
	{
		NodeList nodes = root.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				String name = node.getNodeName();
				
				if (name.equals("g"))
				{
					readObstacles(node.getChildNodes());
				}
			}
		}
	}
	
	private void readObstacles(NodeList nodes)
	{
		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				String name = node.getNodeName();
				
				if (name.equals("rect"))
				{
					Element element = (Element)node;
					String id = element.getAttribute("id");
					
					if ((!id.equals("wallLeft")) && (!id.equals("wallRight")))
					{
						float width = Float.parseFloat(element.getAttribute("width"));
						float height = Float.parseFloat(element.getAttribute("height"));
						float x = Float.parseFloat(element.getAttribute("x"));
						float y = Renderer.RESOLUTION_Y - Float.parseFloat(element.getAttribute("y")) - height;
						
						Shape shape = new Rectangle(width, height, LevelDefinition.WALL_COLOR);
						
						add(new Sprite(shape, x, y));
					}
				}
			}
		}
	}
	
	private Node getRoot(InputStream inputStream)
	{
		Node result = null;
		
		try
		{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			InputSource inputSource = new InputSource(inputStream);
			Document document = documentBuilder.parse(inputSource);
			
			NodeList nodes = document.getChildNodes();
			
			for (int i = 0; i < nodes.getLength(); i++)
			{
				Node node = nodes.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					result = node;
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void add(Sprite sprite)
	{
		this.base.add(sprite);
	}
	
	public List<Sprite> getCollisionableSprites()
	{
		return this.collisionableSprites;
	}
	
	public List<Sprite> getNonCollisionableSprites()
	{
		return this.nonCollisionableSprites;
	}
	
	public boolean finished(Sprite sprite)
	{
		return (sprite.x > (this.length * this.laps));
	}
	
	public void build()
	{
		for (int i = 0; i < this.laps; i++)
		{
			Shape startLineShape = new Rectangle(Renderer.RESOLUTION_X, 1, Color.argb(255, 200, 200, 200));
			Sprite startLine = new Sprite(startLineShape, this.length * i, 0);
			this.nonCollisionableSprites.add(startLine);
			
			for (Sprite sprite : this.base)
			{
				this.collisionableSprites.add(sprite.copyAt(sprite.x + (this.length * i), sprite.y));
			}
		}
		
		Shape lastStartLineShape = new Rectangle(Renderer.RESOLUTION_X, 1, Color.argb(255, 200, 200, 200));
		Sprite lastStartLine = new Sprite(lastStartLineShape, this.length * this.laps, 0);
		this.nonCollisionableSprites.add(lastStartLine);
	}
}
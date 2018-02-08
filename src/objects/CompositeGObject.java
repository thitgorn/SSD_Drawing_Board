package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		this.gObjects.add(gObject);
	}

	public void remove(GObject gObject) {
		this.gObjects.remove(gObject);
	}

	@Override
	public void move(int dX, int dY) {
		this.x += dX;
		this.y += dY;
		for (GObject gObject : gObjects) {
			gObject.move(dX, dY);
		}
	}

	public void recalculateRegion() {
		int newX = gObjects.get(0).x;
		int newY = gObjects.get(0).y;
		int newDx = gObjects.get(0).x + gObjects.get(0).width;
		int newDy = gObjects.get(0).y + gObjects.get(0).height;
		for (GObject gObject : this.gObjects) {
			if (gObject.x < newX) {
				newX = gObject.x;
			}
			if (gObject.x + gObject.width > newDx) {
				newDx = gObject.x + gObject.width;
			}
			if (gObject.y < newY) {
				newY = gObject.y;
			}
			if (gObject.y + gObject.height > newDy) {
				newDy = gObject.y + gObject.height;
			}
		}
		this.x = newX;
		this.y = newY;
		this.width = newDx - newX;
		this.height = newDy - newY;
	}

	@Override
	public void paintObject(Graphics g) {
		for (GObject gObject : this.gObjects) {
			gObject.paintObject(g);
		}
	}

	@Override
	public void paintLabel(Graphics g) {
		g.setColor(Color.black);
		g.drawString("Composite", x, y + height + 12);
	}

}

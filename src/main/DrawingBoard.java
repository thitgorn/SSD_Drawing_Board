package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter;
	private List<GObject> gObjects;
	private GObject target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}

	public void addGObject(GObject gObject) {
		gObjects.add(gObject);

		repaint();
	}

	public void groupAll() {
		CompositeGObject newGroup = new CompositeGObject();
		for (GObject object : this.gObjects) {
			newGroup.add(object);
		}
		this.gObjects.clear();
		newGroup.recalculateRegion();
		this.gObjects.add(newGroup);

		repaint();
	}

	public void deleteSelected() {
		this.gObjects.remove(target);

		repaint();
	}

	public void clear() {
		this.gObjects.clear();

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {
		private int currentX;
		private int currentY;

		private void deselectAll() {
			target = null;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			currentX = e.getX();
			currentY = e.getY();
			int count = 0;
			for (GObject gObject : gObjects) {
				gObject.deselected();
				if (gObject.pointerHit(currentX, currentY)) {
					target = gObject;
					count++;
				}
			}
			if (count == 0)
				deselectAll();
			else
				target.selected();
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (target == null)
				return;
			int dx = e.getX() - currentX;
			int dy = e.getY() - currentY;
			target.move(dx, dy);
			repaint();
			this.currentX = e.getX();
			this.currentY = e.getY();
		}

	}

}
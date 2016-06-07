

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Polygon2d implements Comparable {

    private Polygon P;
    private Color mycolor;
    private boolean visible;
    private double avgdist;
    private Polygon3d parent;

    public Polygon2d(double[] x, double[] y, Color c, Polygon3d mother, double dist, boolean visible) { // remember to be careful of aliases with arrays
        //PRECONDITION: x and y are same length!
        P = new Polygon();
        for (int i = 0; i < x.length; i++) {
            P.addPoint((int) (x[i] + 0.5), (int) (y[i] + 0.5));
        }
        mycolor = c;
        parent = mother;
        avgdist = dist;
        this.visible = visible;
    }

    public void setvisible(boolean visible) {
        this.visible = visible;
    }

    public void setcolor(Color c) {
        mycolor = c;
    }

    public Color getcolor() {
        return mycolor;
    }

    public double getdist() {
        return avgdist;
    }

    public Polygon3d getparent() {
        return parent;
    }

    public void drawpoly2d(Graphics2D g) {
        //method to simplify polygon2d drawing into 1 step in the graphics component
        g.setColor(mycolor);
        if (visible) {
            g.fillPolygon(P);
        }
        if (Screen.OutLines) {
            g.setColor(Color.black);
            g.drawPolygon(P);
        }
    }

    public boolean isMouseOver() {
        return P.contains(Screen.screensize.getWidth() / 2, Screen.screensize.getHeight() / 2);

    }

    @Override
    public int compareTo(Object t) {
        Polygon2d poly = (Polygon2d) (t);
        return (int) (poly.getdist() - avgdist);

    }

}

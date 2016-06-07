
import java.awt.Color;

public class Polygon3d implements Comparable {

    private double[] x, y, z;
    private Color mycolor;
    private boolean visible;
    private int numvertices;
    private double avgdist;

    public Polygon3d(double[] x, double[] y, double[] z, Color c, boolean visible) {
        //x y and z must be same length

        this.x = x;
        this.y = y;
        this.z = z;
        mycolor = c;
        this.visible = visible;
        numvertices = x.length;
    }


    public void setcolor(Color c) {
        mycolor = c;
    }

    public void setvisible(boolean visible) {
        this.visible = visible;
    }

    public double[] getx() {
        return x;
    }

    public double[] gety() {
        return y;
    }

    public double[] getz() {
        return z;
    }

    public void setx(double[] newx) {
        x = newx;
    }

    public void sety(double[] newy) {
        y = newy;
    }

    public void setz(double[] newz) {
        z = newz;
    }

    public void removethis() {
        for (int i = 0; i < Screen.allpolygons.size(); i++) {
            if (Screen.allpolygons.get(i) == this) {
                Screen.allpolygons.remove(i);
                break;
            }
        }
    }

    public void addVector(Vector v, double delta) {
        for (int i = 0; i < x.length; i++) {
            x[i] += v.getx() * delta;
            y[i] += v.gety() * delta;
            z[i] += v.getz() * delta;
        }
    }

    public int getnumvertices() {
        return numvertices;
    }

    public Color getColor() {
        return mycolor;
    }

    public boolean isvisible() {
        return visible;
    }

    double GetDist() {
        double total = 0;
        for (int i = 0; i < x.length; i++) {
            total += GetDistanceToP(i);
        }
        return total / x.length;
    }

    double GetDistanceToP(int i) {
        Vector temp = new Vector(x[i] - Camera.x, y[i] - Camera.y, z[i] - Camera.z);

        temp.RotateXZ(Camera.XZrot);
        temp.RotateYZ(Camera.YZrot);

        return Math.sqrt(temp.getx() * temp.getx() + temp.gety() * temp.gety() + temp.getz() * temp.getz());

    }

    @Override
    public int compareTo(Object t) {
        return (int) (((Polygon3d) t).GetDist() - this.GetDist() + 0.5);
    }

}

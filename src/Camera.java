
import java.awt.Toolkit;
import java.util.ArrayList;

public class Camera {

    public static double x, y, z;

    final int centerx = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
    final int centery = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

    private final int eyedist = 800; // distance from eye to viewbox of fustrum -- depth of field

    public static double YZrot = 0; // horizontal  rotation (flat plane a la floor)
    public static double XZrot = 0; // up and down rotation (flat plane a la saggital axis)
    public static double XYrot = 0;

    public Camera(int xx, int yy, int zz) {
        x = xx;
        y = yy;
        z = zz;
    }
    

    public void setX(double xx) {
        x = xx;
    }

    public void setY(double yy) {
        y = yy;
    }

    public void setZ(double zz) {
        z = zz;
    }

    public void addYZrot(double r, double delta) {
        YZrot += r * delta;
        if (YZrot > Math.PI) {
            YZrot -= 2 * Math.PI;
        } else if (YZrot < -Math.PI) {
            YZrot += 2 * Math.PI;
        }
    }

    public void addXZrot(double r, double delta) {
        if (YZrot > Math.PI / 2 || YZrot < -Math.PI / 2) {
            XZrot -= r * delta;
        } else {
            XZrot += r * delta;
        }

        if (XZrot > Math.PI) {
            XZrot -= 2 * Math.PI;
        } else if (XZrot < -Math.PI) {
            XZrot += 2 * Math.PI;
        }

    }

    public void setXZrot(double r) {
        XZrot = r;
    }

    public void setYZrot(double r) {
        YZrot = r;
    }

    public double getYZrot() {
        return YZrot;
    }

    public double getXZrot() {
        return XZrot;
    }

    public double getcamerax() {
        return x;
    }

    public double getcameray() {
        return y;
    }

    public double getcameraz() {
        return z;
    }

    public static Vector getVector() {
        return new Vector(x, y, z);
    }

    public void follow(FighterWrapper ship) {

        Vector temp = new Vector(0, 6, -50); // follows at this offset from the ship
        temp.RotateYZ(-YZrot);
        temp.RotateXZ(-XZrot);

        temp.addVector(ship.getVector());

        this.setVector(temp);
    }

    public void addVector(Vector v, double delta) {
        x += v.getx() * delta;
        y += v.gety() * delta;
        z += v.getz() * delta;
    }

    public void setVector(Vector v) {
        x = v.getx();
        y = v.gety();
        z = v.getz();
    }

    private Vector getdiffvector(Vector p1, Vector p2) { // from p1 to p2 shifted to be on origin
        double tempx = p2.getx() - p1.getx();
        double tempy = p2.gety() - p1.gety();
        double tempz = p2.getz() - p1.getz();

        return new Vector(tempx, tempy, tempz);
    }

    private double getavgdist(double[] x, double[] y, double[] z) {
        //x,y,z are same length

        double totaldist = 0;
        for (int i = 0; i < x.length; i++) {
            totaldist += Math.sqrt(x[i] * x[i] + y[i] * y[i] + z[i] * z[i]);
        }
        return totaldist / x.length;
    }

    public Vector ConvertVector(Vector v) {
        Vector temp = new Vector(v.getx(), v.gety(), v.getz());
        temp.subtractVector(this.getVector());
        temp.RotateXZ(XZrot);
        temp.RotateYZ(YZrot);
        temp.RotateXY(XYrot);
        double newx = centerx - (eyedist * temp.getx() / temp.getz());
        double newy = centery - (eyedist * temp.gety() / temp.getz());
        return new Vector(newx, newy, temp.getz());
    }

    public void Convert3d(ArrayList<Polygon3d> p) {

        Screen.drawablepolygons.clear();

        for (Polygon3d poly : p) {
            double[] x2d = new double[poly.getnumvertices()];
            double[] y2d = new double[poly.getnumvertices()];
            double[] xdist = new double[poly.getnumvertices()];
            double[] ydist = new double[poly.getnumvertices()];
            double[] zdist = new double[poly.getnumvertices()];
            boolean drawable = true;
            for (int i = 0; i < poly.getnumvertices(); i++) {
                if (drawable) {
                    Vector temp = new Vector(poly.getx()[i], poly.gety()[i], poly.getz()[i]);
                    Vector origin = new Vector(x, y, z);

                    //moves temp to where it would be as if the camera is at origin for rotation
                    temp.subtractVector(origin);

                    temp.RotateXZ(XZrot);
                    temp.RotateYZ(YZrot);
                    temp.RotateXY(XYrot);

                    if (temp.getz() <= 0.001) {
                        drawable = false;
                        break; // I know its a break statement but in this case it is saving CPU cycles -- if any vertex is behind camera it will not draw entire polygon
                    }
                    x2d[i] = centerx - (eyedist * temp.getx() / temp.getz());
                    y2d[i] = centery - (eyedist * temp.gety() / temp.getz());
                    xdist[i] = temp.getx();
                    ydist[i] = temp.gety();
                    zdist[i] = temp.getz();
                }
            }
            if (drawable) {

                Screen.drawablepolygons.add(new Polygon2d(x2d, y2d, poly.getColor(), poly, getavgdist(xdist, ydist, zdist), poly.isvisible()));

            }

        }

    }

}

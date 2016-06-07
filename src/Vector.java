

public class Vector {

    private double x;
    private double y; //where x y and z are transformations or distances anchored to a point
    private double z;

    public Vector(double xx, double yy, double zz) {
        x = xx;
        y = yy;
        z = zz;
    }

    public void addVector(Vector v) {
        x += v.getx();
        y += v.gety();
        z += v.getz();
    }

    public void subtractVector(Vector v) {
        x -= v.getx();
        y -= v.gety();
        z -= v.getz();
    } 
    
    public double distanceto(Vector v) {
       double tempx = x - v.getx();
       double tempy = y - v.gety();
       double tempz = z - v.getz();
       
       return Math.sqrt(tempx*tempx + tempy*tempy + tempz*tempz);
    }

    public void RotateXY(double rads) {

        double tempx = (x * Math.cos(rads) - y * Math.sin(rads));
        y = (x * Math.sin(rads) + y * Math.cos(rads));
        x = tempx;

    }

    public void RotateYZ(double rads) {
        double tempy = (y * Math.cos(rads) - z * Math.sin(rads));
        z = (y * Math.sin(rads) + z * Math.cos(rads));

        y = tempy;
    }

    public void RotateXZ(double rads) {
        double tempx = (x * Math.cos(rads) + z * Math.sin(rads));
        z = (z * Math.cos(rads) - x * Math.sin(rads));

        x = tempx;
    }

    public void scale(double sx, double sy, double sz) {
        x *= sx;
        y *= sy;
        z *= sz;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public double getz() {
        return z;
    }
    
    public void setx(double newx) {
        x = newx;
    }
    
    public void sety(double newy) {
        y = newy;
    }
    public void setz(double newz) {
        z = newz;
    }
    
    @Override
    public String toString() {
        return "" + x + "," + y + "," + z;
    }
}

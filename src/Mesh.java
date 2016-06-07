
public abstract class Mesh {

    double x, y, z; // coordinates of center -- all meshes will use this as it is the rotation point of the mesh as a whole
    double XZrot = 0; // yaw
    double YZrot = 0; // pitch
    Polygon3d[] faces;

    public Mesh(double xx, double yy, double zz) {
        x = xx;
        y = yy;
        z = zz;

    }

    public void setfaces(Polygon3d[] surfaces) {
        //must be called in creation of each mesh after faces has been initalized
        faces = surfaces;
    }

    public Polygon3d[] getfaces() {
        return faces;
    }

    public void addrotation(double XZrotspeed, double YZrotspeed, double delta) {
//        if (XZrot > Math.PI) {
//            XZrot -= 2 * Math.PI;
//        } else if (XZrot < -Math.PI) {
            XZrot += 2 * Math.PI;
     //   }
        if (YZrot > Math.PI) {
            YZrot -= 2 * Math.PI;
        } else if (YZrot < -Math.PI) {
            YZrot += 2 * Math.PI;
        }

        undefinerot();

        XZrot += XZrotspeed * delta; // updates stored angle counter
        YZrot += YZrotspeed * delta;
        for (Polygon3d poly : faces) {
            double[] newx = new double[poly.getnumvertices()];
            double[] newy = new double[poly.getnumvertices()];
            double[] newz = new double[poly.getnumvertices()];
            for (int i = 0; i < poly.getnumvertices(); i++) {
                Vector temp = new Vector(poly.getx()[i] - this.getx(), poly.gety()[i] - this.gety(), poly.getz()[i] - this.getz());

                temp.RotateYZ(YZrot);
                temp.RotateXZ(XZrot);

                newx[i] = temp.getx() + this.getx();
                newy[i] = temp.gety() + this.gety();
                newz[i] = temp.getz() + this.getz();
            }

            poly.setx(newx);
            poly.sety(newy);
            poly.setz(newz);
        }
    }

    private void undefinerot() {
        for (Polygon3d poly : faces) {
            double[] newx = new double[poly.getnumvertices()];
            double[] newy = new double[poly.getnumvertices()];
            double[] newz = new double[poly.getnumvertices()];
            for (int i = 0; i < poly.getnumvertices(); i++) {
                Vector temp = new Vector(poly.getx()[i] - this.getx(), poly.gety()[i] - this.gety(), poly.getz()[i] - this.getz());

                temp.RotateXZ(-XZrot);
                temp.RotateYZ(-YZrot);

                newx[i] = temp.getx() + this.getx();
                newy[i] = temp.gety() + this.gety();
                newz[i] = temp.getz() + this.getz();
            }

            poly.setx(newx);
            poly.sety(newy);
            poly.setz(newz);
        }
    }

    public void setrotation(double XZrotset, double YZrotset) {
        for (Polygon3d poly : faces) {
            double[] newx = new double[poly.getnumvertices()];
            double[] newy = new double[poly.getnumvertices()];
            double[] newz = new double[poly.getnumvertices()];
            for (int i = 0; i < poly.getnumvertices(); i++) {
                Vector temp = new Vector(poly.getx()[i] - this.getx(), poly.gety()[i] - this.gety(), poly.getz()[i] - this.getz());

                temp.RotateYZ(YZrotset - YZrot);
                temp.RotateXZ(XZrotset - XZrot);

                newx[i] = temp.getx() + this.getx();
                newy[i] = temp.gety() + this.gety();
                newz[i] = temp.getz() + this.getz();
            }

            poly.setx(newx);
            poly.sety(newy);
            poly.setz(newz);
        }
        XZrot = XZrotset;
        YZrot = YZrotset;

    }

    public double getXZrot() {
        return XZrot;
    }

    public double getYZrot() {
        return YZrot;
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

    public void addVector(Vector v) {
        for (Polygon3d face : faces) {
            face.addVector(v, Screen.delta);
        }
        x += v.getx() * Screen.delta;
        y += v.gety() * Screen.delta;
        z += v.getz() * Screen.delta;
    }

    public Vector getVector() {
        return new Vector(x, y, z);
    }
}

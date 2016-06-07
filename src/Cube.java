

import java.awt.Color;
import java.util.ArrayList;

public class Cube extends Mesh {
    
    double width, length, height;
    
    public Cube(double x, double y, double z, double width, double height, double length, Color c) {
        super(x, y, z);
        Polygon3d[] faces = new Polygon3d[6]; //faces are aliases that also correspond with polygons in the main array
        this.width = width;
        this.height = height;
        this.length = length;
        width = width / 2; // halves axes so that "center" remains centered
        length = length / 2;
        height = height / 2;
        
        faces[0] = new Polygon3d(new double[]{x + width, x + width, x + width, x + width}, new double[]{y - height, y + height, y + height, y - height}, new double[]{z + length, z + length, z - length, z - length}, c, true);
        Screen.allpolygons.add(faces[0]);
        faces[1] = new Polygon3d(new double[]{x - width, x - width, x - width, x - width}, new double[]{y - height, y + height, y + height, y - height}, new double[]{z + length, z + length, z - length, z - length}, c, true);
        Screen.allpolygons.add(faces[1]);
        faces[2] = new Polygon3d(new double[]{x - width, x + width, x + width, x - width}, new double[]{y + height, y + height, y + height, y + height}, new double[]{z + length, z + length, z - length, z - length}, c, true);
        Screen.allpolygons.add(faces[2]);
        faces[3] = new Polygon3d(new double[]{x - width, x + width, x + width, x - width}, new double[]{y - height, y - height, y - height, y - height}, new double[]{z + length, z + length, z - length, z - length}, c, true);
        Screen.allpolygons.add(faces[3]);
        faces[4] = new Polygon3d(new double[]{x + width, x + width, x - width, x - width}, new double[]{y - height, y + height, y + height, y - height}, new double[]{z + length, z + length, z + length, z + length}, c, true);
        Screen.allpolygons.add(faces[4]);
        faces[5] = new Polygon3d(new double[]{x + width, x + width, x - width, x - width}, new double[]{y - height, y + height, y + height, y - height}, new double[]{z - length, z - length, z - length, z - length}, c, true);
        Screen.allpolygons.add(faces[5]);
        
        super.setfaces(faces);
    }
    
    public void setColor(Color c) {
        for (Polygon3d face : faces) {
            face.setcolor(c);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;

public class Ship extends Mesh {

    final double length = 30;
    final double height = 6;
    final double width = 15;

    public Ship(double x, double y, double z, Color c) {
        super(x, y, z);

        int index = 0;
        Polygon3d[] faces = new Polygon3d[17];
        Cube hull = new Cube(x, y, z - length / 4, width, height / 2, length / 2, c);

        for (Polygon3d face : hull.getfaces()) {
            faces[index] = face;
            index++;
        }

        Pyramid nose = new Pyramid(x, y, z + length / 4, 10, length / 2, height, c);
        nose.setrotation(0, Math.PI / 2);
        for (Polygon3d face : nose.getfaces()) {
            faces[index] = face;
            index++;
        }

        super.setfaces(faces);
    }

}

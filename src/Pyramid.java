/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;

public class Pyramid extends Mesh { // creates a pyramid of a certain number of sides with the first angle pointing in the Z direction

    private double height, basesize;

    public Pyramid(double x, double y, double z, int numsides, double height, double basesize, Color c) {
        super(x, y, z);
        Polygon3d[] faces = new Polygon3d[numsides + 1];
        this.basesize = basesize;
        this.height = height;

        basesize = basesize / 2;
        height = height / 2;

        double XZrotstep = 2 * Math.PI / numsides;
        double currrot = 0;
        double[] xbase = new double[numsides];
        double[] ybase = new double[numsides];
        double[] zbase = new double[numsides];

        for (int i = 0; i < numsides; i++) {
            xbase[i] = x + basesize * Math.sin(currrot);
            ybase[i] = y - height;
            zbase[i] = z + basesize * Math.cos(currrot);
            
            currrot += XZrotstep;
            
            faces[i] = new Polygon3d(new double[]{xbase[i], x + basesize*Math.sin(currrot), x},
            new double[]{ybase[i], ybase[i], y+ height},
            new double[]{zbase[i], z+ basesize*Math.cos(currrot), z}, c, true);
            Screen.allpolygons.add(faces[i]);
        }
        
        faces[faces.length - 1] = new Polygon3d(xbase,ybase,zbase, c, true);
                Screen.allpolygons.add(faces[faces.length - 1]);
        
                super.setfaces(faces);

    }

}

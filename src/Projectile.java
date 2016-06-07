
import java.awt.Color;

public class Projectile {

    public static final int SPEED = 25;
    double decaytimer = 100;
    private Cube mycube;
    private double XZrot;
    private double YZrot;
    private int teamnum;

    public Projectile(double x, double y, double z, double XZrot, double YZrot, Color c, int team) {
        mycube = new Cube(x, y, z, 2, 2, 15, c);
        mycube.setrotation(XZrot, YZrot);
        teamnum = team;
        this.XZrot = XZrot;
        this.YZrot = YZrot;

    }

    public void update() {
        Vector temp = new Vector(0, 0, SPEED);
        temp.RotateYZ(YZrot);
        temp.RotateXZ(XZrot);

        mycube.addVector(temp);

        decaytimer -= Screen.delta;
        if (decaytimer < 0) {
            removethis();
        } else {
            this.checkhit();
        }
    }

    public void checkhit() {
        if (teamnum == 1) {
            for (int i = Screen.team2_ships.size() - 1; i >= 0; i--) {
                if (30 > this.getVector().distanceto(Screen.team2_ships.get(i).getVector())){
                    Screen.team2_ships.get(i).takedamage();
                    this.removethis();
                    break;
                }
            }
        } else if (teamnum == 2) {
            for (int i = Screen.team1_ships.size() - 1; i >= 0; i--) {
                if (30 > this.getVector().distanceto(Screen.team1_ships.get(i).getVector())){
                    Screen.team1_ships.get(i).takedamage();
                    this.removethis();
                    break;
                }
            }
        }
    }
    
    public Vector getVector() {
        return mycube.getVector();
    }

    private void removethis() {
        for (int i = 0; i < mycube.faces.length; i++) {
            mycube.faces[i].removethis();
        }
        if (teamnum == 1) {
            for (int i = 0; i < Screen.team1_lasers.size(); i++) {
                if (Screen.team1_lasers.get(i) == this) {
                    Screen.team1_lasers.remove(i);
                    break;
                }
            }
        } else if (teamnum == 2) {
            for (int i = 0; i < Screen.team2_lasers.size(); i++) {
                if (Screen.team2_lasers.get(i) == this) {
                    Screen.team2_lasers.remove(i);
                    break;
                }
            }
        }
    }

}

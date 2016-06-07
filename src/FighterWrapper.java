
import java.awt.Color;

public abstract class FighterWrapper extends Ship {

    private final double FIRERATE = 15; //higher is slower

    private final double XZrotspeed = 0.02;
    private final double YZrotspeed = 0.02;

    public double movespeed = 5; // public so that targeting can handle it from anywhere

    public double XZlaststep = 0;
    public double YZlaststep = 0;

    private final int MAXHEALTH = 25;
    private int health = MAXHEALTH;
    private double guncooldown = FIRERATE;

    private boolean isdead = false;

    private int teamnum;

    public FighterWrapper(double x, double y, double z, Color c, int team) {
        super(x, y, z, c);
        teamnum = team;
    }

    public void update(Vector target, boolean firing) {

        this.turn(target);
        this.move();

        if (firing) {
            this.fire();
        } else {
            guncooldown -= Screen.delta; // so that cooldown increments even if player or AI is not currently firing
        }
    }

    //postive XZpercent is "right" negative is "left"
    //positive YZpitches "down" negative pitches "up"
    public void turn(Vector target) { // percent turn from -1 to 1 ultimately modified by delta

        double bestXZangle = this.getidealXZangle(target);
        double turnXZ = XZrotspeed * Screen.delta; //defaults to turnspeed because in the for loop since it uses doubles, it wouldnt perfectly hit. whereas -turnspeed is defined in loop
        for (double d = -(XZrotspeed * Screen.delta); d <= (XZrotspeed * Screen.delta); d += (XZrotspeed * Screen.delta) / 10) { //alows a gradient of potential best turn angles up to 20 potential
            if ((Math.cos(d + XZrot) * Math.cos(bestXZangle) + Math.sin(d + XZrot) * Math.sin(bestXZangle))
                    > (Math.cos(turnXZ + XZrot) * Math.cos(bestXZangle) + Math.sin(turnXZ + XZrot) * Math.sin(bestXZangle))) {
                turnXZ = d;
            }
        }

        double bestYZangle = this.getidealYZangle(target);
        double turnYZ = YZrotspeed * Screen.delta; //defaults to turnspeed because in the for loop since it uses doubles, it wouldnt perfectly hit. whereas -turnspeed is defined in loop
        for (double d = -(YZrotspeed * Screen.delta); d <= (YZrotspeed * Screen.delta); d += (YZrotspeed * Screen.delta) / 10) { //alows a gradient of potential best turn angles up to 20 potential
            if ((Math.cos(d + YZrot) * Math.cos(bestYZangle) + Math.sin(d + YZrot) * Math.sin(bestYZangle))
                    > (Math.cos(turnYZ + YZrot) * Math.cos(bestYZangle) + Math.sin(turnYZ + YZrot) * Math.sin(bestYZangle))) {
                turnYZ = d;
            }
        }

        super.addrotation(turnXZ, turnYZ, Screen.delta);
    }

    public double getXZrotspeed() {
        return XZrotspeed;
    }

    public double getYZrotspeed() {
        return YZrotspeed;
    }

    public int getteamnum() {
        return teamnum;
    }

    public int gethealth() {
        return health;
    }

    public void move() {
        Vector temp = new Vector(0, 0, movespeed);

        temp.RotateYZ(super.getYZrot());
        temp.RotateXZ(super.getXZrot());

        this.addVector(temp);
    }

    public void takedamage() {
        health--;
        if (health <= 0) {
            isdead = true;
            removethis();
        }
    }

    public void removethis() {
        for (Polygon3d face : super.getfaces()) {
            face.removethis();
        }

        if (teamnum == 1) {
            for (int i = Screen.team1_ships.size() - 1; i >= 0; i--) {
                if (Screen.team1_ships.get(i) == this) {
                    Screen.team1_ships.remove(i);
                    break;
                }
            }
        } else if (teamnum == 2) {
            for (int i = Screen.team2_ships.size() - 1; i >= 0; i--) {
                if (Screen.team2_ships.get(i).equals(this)) {
                    Screen.team2_ships.remove(i);
                    break;
                }
            }
        }
    }

    public void fire() {

        guncooldown -= Screen.delta;

        if (guncooldown < 0) {
            guncooldown = FIRERATE;
            if (teamnum == 1) {
                Screen.team1_lasers.add(new Projectile(this.getx(), this.gety(), this.getz(), this.getXZrot(), this.getYZrot(), Color.cyan, teamnum));
            } else if (teamnum == 2) {
                Screen.team2_lasers.add(new Projectile(this.getx(), this.gety(), this.getz(), this.getXZrot(), this.getYZrot(), Color.orange, teamnum));
            }
        }
    }

    public double getidealXZangle(Vector v) {
        return Math.atan2(v.getx() - this.getx(), v.getz() - this.getz());

    }

    public double getidealYZangle(Vector v) {
        double x = v.getx() - this.getx();
        double z = v.getz() - this.getz();
        return -Math.atan2(v.gety() - this.gety(), Math.sqrt(x * x + z * z));
    }

    boolean isdeady() {
    return isdead;
    }

}

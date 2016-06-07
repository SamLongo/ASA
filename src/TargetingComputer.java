
import java.awt.Color;
import java.awt.Graphics;

public class TargetingComputer { //aim system for the player

    private FighterWrapper myship;
    private FighterWrapper target;
    private int targetindex = 0;
    private double aimdist;

    private Vector mousecircle;
    private Vector playeraim;
    private Vector aimlocation; // when returned to the screen this should be already converted to 2D

    TargetingComputer(Player player) {
        myship = player;
    }

    public void cycletarget() {
        if (!Screen.team2_ships.isEmpty()) {
            targetindex = (targetindex + 1) % Screen.team2_ships.size();
            target = Screen.team2_ships.get(targetindex);
        }
    }

    public void update() {
        if (target.isdeady()) {
            this.cycletarget();
        }

        if (target.isdeady()) {
            // failed to cycle target  do nothing
        } else {

            double shipdist = myship.getVector().distanceto(target.getVector());
            Vector temp = new Vector(0, 0, target.movespeed * shipdist / Projectile.SPEED);
            temp.RotateYZ(target.getYZrot());
            temp.RotateXZ(target.getXZrot());
            temp.addVector(target.getVector());
            aimlocation = temp;

            aimdist = myship.getVector().distanceto(aimlocation);
        }

        mousecircle = new Vector(0, 0, aimdist);
        mousecircle.RotateYZ(-Camera.YZrot);
        mousecircle.RotateXZ(-Camera.XZrot);
        mousecircle.addVector(myship.getVector());

        playeraim = new Vector(0, 0, aimdist); // z is number of units straight from tip of plane
        playeraim.RotateYZ(myship.YZrot);
        playeraim.RotateXZ(myship.XZrot);
        playeraim.addVector(myship.getVector());

        aimlocation = Screen.camera.ConvertVector(aimlocation);
        mousecircle = Screen.camera.ConvertVector(mousecircle);
        playeraim = Screen.camera.ConvertVector(playeraim);
    }

    public void drawaim(Graphics g) {

        if (target.isdeady() || aimlocation.getz() < 0) {
            // failed to cycle target do nothing
            // OR the aim location is behind the screen
        } else {
            //targetting square
            g.setColor(Color.yellow);
            g.drawRect((int) aimlocation.getx() - 10, (int) aimlocation.gety() - 10, 20, 20);
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect((int) aimlocation.getx() - 10, (int) aimlocation.gety() - 10, 20, 20);
        }
        double x = aimlocation.getx() - playeraim.getx();
        double y = aimlocation.gety() - playeraim.gety();
        if(aimlocation.getz() > 0 && 10 > Math.sqrt(x*x + y*y)){
            g.setColor(Color.red);
        } else {
            g.setColor(Color.yellow);
        }
        
        if (playeraim.getz() > 0) {
            g.drawLine((int) playeraim.getx() - 4, (int) playeraim.gety(), (int) playeraim.getx() + 4, (int) playeraim.gety());
            g.drawLine((int) playeraim.getx(), (int) playeraim.gety() - 4, (int) playeraim.getx(), (int) playeraim.gety() + 4);
        }
        if (mousecircle.getz() > 0) {
            g.drawOval((int) mousecircle.getx() - 15, (int) mousecircle.gety() - 15, 30, 30);
        }

    }

}

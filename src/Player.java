
import java.awt.Color;

public class Player extends FighterWrapper {

    public Player(double x, double y, double z, Color c, int teamnum) {
        super(x, y, z, c, teamnum);
    }

    public void update(double aimdist) {

        Vector target = new Vector(0, 0, aimdist);
        target.RotateYZ(-Camera.YZrot);
        target.RotateXZ(-Camera.XZrot);

        target.addVector(this.getVector());

        super.update(target, Screen.click);
    }

}

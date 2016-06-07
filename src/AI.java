
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sam
 */
public class AI extends FighterWrapper {

    FighterWrapper target;
    Vector faketarget;
    boolean chasing = false;
    double targetswitchtimer = 100;

    public AI(double x, double y, double z, Color c, int team) {
        super(x, y, z, c, team);
    }

    public void update() {
        if (target == null || targetswitchtimer < 0) { //just so i remember -- if a AI is not chasing but their original target is defeated they will switch immediately
            selecttarget();
            targetswitchtimer = 200 + Math.random() * 200;
        }
        targetswitchtimer -= Screen.delta;
        if (chasing) {
            
            super.update(target.getVector(), true);
        } else {
            super.update(faketarget, false);
        }
    }

    private void selecttarget() {
        if (Math.random() < 0.15) {
            Vector temp = new Vector(Math.random() * 3000 - 1500, Math.random() * 10000 - 5000, Math.random() * 3000 - 1500);
            temp.addVector(this.getVector());
            faketarget = temp;
            chasing = false;
            super.movespeed = 10;             
        } else {
            super.movespeed = 5;
            chasing = true;
            if (this.getteamnum() == 1 && !Screen.team2_ships.isEmpty()) {               
                target = Screen.team2_ships.get((int) (Math.random() * Screen.team2_ships.size()));
            } else if (this.getteamnum() == 2 && !Screen.team1_ships.isEmpty()) {
                target = Screen.team1_ships.get((int) (Math.random() * Screen.team1_ships.size()));
            }
        }

    }
}

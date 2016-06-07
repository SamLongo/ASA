
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    //This class handles all objects being drawn to screen -- renderer + gameloop?

    public static boolean click, rclick = false;
    private boolean keyup, keyleft, keydown, keyright, shift, escape, space;
    public static double mouseX;
    public static double mouseY;
    private boolean isgamerunning = true;
    private int FPS;

    private CountDownLatch latch;

    private double aimdist = 500;
    private Player player;
    private Vector playeraim;
    private Vector test;
    private TargetingComputer targetter;

    private final int aimSight = 5;

    public static double delta;
    public static final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    public static boolean OutLines = false; //determines if drawing polygons should have a black wireframe (handled inside Polygon2d)

    private BufferedImage offscreen;
    private Graphics2D buffer;

    static Camera camera = new Camera(0, 50, 0);

    private ArrayList<Cube> stars = new ArrayList();

    public static ArrayList<FighterWrapper> team1_ships = new ArrayList();
    public static ArrayList<Projectile> team1_lasers = new ArrayList();

    public static ArrayList<FighterWrapper> team2_ships = new ArrayList();
    public static ArrayList<Projectile> team2_lasers = new ArrayList();

    public static ArrayList<Polygon3d> allpolygons = new ArrayList(); //arraylists to be shared across all the classes (i know immutability and all that but this seems to make it much simpler)
    public static ArrayList<Polygon2d> drawablepolygons = new ArrayList();
    public static Polygon3d selectedpolygon;

    public Screen() {
        addKeyListener(this);
        setFocusable(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        this.invisibleMouse();

        for (int x = 0; x < 200; x++) {

            int random = (int) (Math.random() * 55) + 200;
            stars.add(new Cube(Math.random() * 10000 - 5000, Math.random() * 10000 - 5000, Math.random() * 10000 - 5000, 5, 5, 5, new Color(random, random, random)));

        }

        double teamx = 0;
        double teamy = 200;
        double team1z = -500;

        player = new Player(teamx, teamy, team1z, Color.blue, 1);
        targetter = new TargetingComputer(player);
        team1_ships.add(player);
        for (int i = 0; i < 5; i++) {
            team1_ships.add(new AI(teamx + Math.random() * 300 - 150, teamy + Math.random() * 300 - 150, team1z, Color.blue, 1));
        }

        double team2z = 4000;
        for (int i = 0; i < 6; i++) {
            team2_ships.add(new AI(teamx + Math.random() * 300 - 150, teamy + Math.random() * 300 - 150, team2z, Color.red, 2));
            team2_ships.get(i).setrotation(Math.PI, 0);
        }

        targetter.cycletarget();

        offscreen = new BufferedImage(screensize.width, screensize.height, BufferedImage.TYPE_INT_ARGB);
        buffer = offscreen.createGraphics();

        buffer.setBackground(Color.black);
        new Thread(r).start();
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {

            long lastLoopTime;
            final int TARGET_FPS = 60; // can also serve as a global speed modifier -- this many 'ticks' per second
            final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
            long now;
            long updateLength;
            int fpstimer = 0;
            int fpscounter = 0;

            do {
                lastLoopTime = System.nanoTime(); //ensures a step of almost 0 on a start of new game or after pausing
                while (isgamerunning) {
                    //******************************************************* finds FPS and delta for game updates
                    updateLength = 0;
                    do {
                        now = System.nanoTime();
                        updateLength += now - lastLoopTime;
                        lastLoopTime = now;
                        delta = updateLength / ((double) OPTIMAL_TIME); //delta serves as the fraction of the optimal framerate
                        //a delta of 1 is an ideal step, higher means slowdown is occuring
                    } while (delta < 0.5); //do while can limit fps if neccesary

                    //update frame counter
                    fpstimer += updateLength;
                    fpscounter++;

                    if (fpstimer >= 1000000000) {
                        FPS = fpscounter; //writes the counted fps to global FPS after each second
                        fpscounter = 0;
                        fpstimer = 0;
                    }

                    //****************************************************
                    //adds movements based on keys pressed
                    if (keyup) {
                        if (aimdist < 4000) {
                            aimdist += 10 * delta;
                        }
                    }
                    if (keyright) {

                    }
                    if (keyleft) {

                    }
                    if (keydown) {
                        if (aimdist > 100) {
                            aimdist -= 10 * delta;
                        }
                    }
                    if (shift) {

                    }
                    if (space) {

                    }

                    if (escape) { // closes program when pressing escape
                        System.exit(1);
                    }

                    if (10 < Math.sqrt(Math.pow(mouseX - screensize.width / 2, 2) + Math.pow(mouseY - screensize.height / 2, 2))) {
                        try {
                            Robot r = new Robot();

                            //adds rotation based on change from center of screen
                            camera.addXZrot(((double) mouseX - screensize.width / 2) * Math.PI / (2 * screensize.width), delta);
                            camera.addYZrot(((double) mouseY - screensize.height / 2) * -Math.PI / (2 * screensize.height), delta);

                            r.mouseMove(screensize.width / 2, screensize.height / 2);

                        } catch (AWTException ex) {
                            //ignore
                        }
                    }

                    //updates team1 ships which include player
                    for (int i = 0; i < team1_ships.size(); i++) {
                        if (team1_ships.get(i) instanceof Player) {
                            ((Player) team1_ships.get(i)).update(aimdist);
                        } else {
                            ((AI) team1_ships.get(i)).update();
                        }
                    }
                    //updates team2ships -- ALL AI
                    for (int i = 0; i < team2_ships.size(); i++) {
                        ((AI) team2_ships.get(i)).update();
                    }

                    if (rclick) {
                        targetter.cycletarget();
                        rclick = false;
                    }
                    targetter.update();
                    // updates lasers
                    for (int i = team1_lasers.size() - 1; i >= 0; i--) {
                        team1_lasers.get(i).update();
                    }
                    for (int i = team2_lasers.size() - 1; i >= 0; i--) {
                        team2_lasers.get(i).update();
                    }

                    camera.follow(player);

                    // converts 3d polygons to 2d form and sorts from farthest to closest for drawing
                    camera.Convert3d(allpolygons);
                    sortpoly();

                    setselectedpolygon(); // sets polygon for next update cycle -- mostly accurate since reasonable FPS

                    latch = new CountDownLatch(1);
                    repaint();

                    //latch to synchronize threads in extreme lag, overriding paint if 2 seconds elapse resulting in visual bugs
                    try {
                        latch.await(1, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                isgamerunning = true;

                if (escape) {
                    JOptionPane.showMessageDialog(null, "Paused");

                }

            } while (escape || JOptionPane.YES_OPTION
                    == JOptionPane.showConfirmDialog(
                            null, "Do you want to play again?"));
        }
    };

    @Override
    public void paintComponent(Graphics g) {
        buffer.clearRect(0, 0, screensize.width, screensize.height);

        for (int i = 0; i < drawablepolygons.size(); i++) {
            drawablepolygons.get(i).drawpoly2d(buffer);
        }
        buffer.setColor(Color.yellow);
        buffer.drawString("" + (int) camera.getcamerax() + ", " + (int) camera.getcameray() + ", " + (int) camera.getcameraz(), 10, 10);
        buffer.drawString("FPS: " + FPS, 10, 20);
        buffer.drawString("Health: " + player.gethealth(), 10, 30);
        buffer.drawString("Aiming " + Math.floor(aimdist) + " meters", 10, 40);

        targetter.drawaim(buffer);

        g.drawImage(offscreen, 0, 0, this);
        latch.countDown();

    }

// private utility methods ***************************
    private void setselectedpolygon() {
        //PRECONDITIONS: drawablepolygons is sorted
        //POSTCONDITIONS: selectedpolygon is the polygon3d of the polygon2d under the center of the screen
        selectedpolygon = null;
        for (int i = drawablepolygons.size() - 1; i >= 0; i--) {
            if (drawablepolygons.get(i).isMouseOver()) {
                selectedpolygon = drawablepolygons.get(i).getparent();
                break;
            }
        }

    }

    private void invisibleMouse() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
        setCursor(invisibleCursor);
    }

    private void drawCenterAim(Graphics2D g) { //shifted down 8 which is the same as the height difference of the camera following the ship
        if (selectedpolygon == null) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.RED);
            g.drawLine((int) (screensize.width / 2 - aimSight * 3), (int) (screensize.height / 2), (int) (screensize.width / 2 + aimSight * 3), (int) (screensize.height / 2));
            g.drawLine((int) (screensize.width / 2), (int) (screensize.height / 2 - aimSight * 3), (int) (screensize.width / 2), (int) (screensize.height / 2 + aimSight * 3));

        }
        g.drawOval(screensize.width / 2 - 10, screensize.height / 2, 20, 20);
    }

    private void sortpoly() {
        if (!drawablepolygons.isEmpty()) {
            Qsort(drawablepolygons, 0, drawablepolygons.size() - 1);
        }
    }

    private void Qsort(ArrayList<Polygon2d> array, int low, int high) {
        int i = low;
        int j = high;
        double pivot = array.get((high + low) / 2).getdist();

        while (i <= j) {
            while (array.get(i).getdist() > pivot) {
                i++;
            }
            while (array.get(j).getdist() < pivot) {
                j--;
            }
            if (i <= j) {
                array.set(i, array.set(j, array.get(i)));
                i++;
                j--;
            }
        }

        if (low < j) {
            Qsort(array, low, j);
        }
        if (high > i) {
            Qsort(array, i, high);
        }
    }

//********************************************************************************** mouse + Key listeners
    @Override
    public void keyTyped(KeyEvent ke) {
        //do nothing
    }

    @Override
    public void keyPressed(KeyEvent ke) {
//gets movements
        if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyChar() == 'w' || ke.getKeyChar() == 'W') {
            keyup = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyChar() == 'a' || ke.getKeyChar() == 'A') {
            keyleft = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyChar() == 's' || ke.getKeyChar() == 'S') {
            keydown = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyChar() == 'd' || ke.getKeyChar() == 'D') {
            keyright = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            space = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escape = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //clears movements when button is releases
        if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyChar() == 'w' || ke.getKeyChar() == 'W') {
            keyup = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyChar() == 'a' || ke.getKeyChar() == 'A') {
            keyleft = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyChar() == 's' || ke.getKeyChar() == 'S') {
            keydown = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyChar() == 'd' || ke.getKeyChar() == 'D') {
            keyright = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            space = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escape = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

        //presses left/right mouse button
        if (me.getButton() == MouseEvent.BUTTON1) {
            click = true;
        }
        if (me.getButton() == MouseEvent.BUTTON3) {
            rclick = true;
        }

        mouseX = me.getX();
        mouseY = me.getY();

    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //releases left/right mouse button
        if (me.getButton() == MouseEvent.BUTTON1) {
            click = false;
        }
        if (me.getButton() == MouseEvent.BUTTON3) {
            rclick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

    }

}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.*;

public class AnimationPlane implements ActionListener, KeyListener {
    private Button go;
    private TransformGroup wholePlane;
    private Transform3D translateTransform1 = new Transform3D();
    private Transform3D translateTransform2 = new Transform3D();
    private Transform3D translateTransform3 = new Transform3D();
    private Transform3D rotateTransformX1;
    private Transform3D rotateTransformX2;
    private Transform3D rotateTransformX3;
    private Transform3D rotateTransformY1;
    private Transform3D rotateTransformY2;
    private Transform3D rotateTransformY3;
    private Transform3D rotateTransformZ1;
    private Transform3D rotateTransformZ2;
    private Transform3D rotateTransformZ3;

    private JFrame mainFrame;

    private float sign = 1.0f;
    private float zoom = 0.5f;
    private float xloc = 0.3f;
    private float yloc = 0.3f;
    private float zloc = 0.0f;
    private int moveType = 1;
    private Timer timer;
    TransformGroup groupBack = new TransformGroup();
    TransformGroup groupFront = new TransformGroup();
    Transform3D transformation1 = new Transform3D();
    Transform3D transformation2 = new Transform3D();
    Transform3D transformation3 = new Transform3D();
    TransformGroup groupBody = new TransformGroup();

    float x1 = 0;
    float y1 = 0;
    float z1 = 0;
    float x2 = 0;
    float y2 = 0;
    float z2 = 0;
    float x3 = 0;
    float y3 = 0;
    float z3 = 0;
    int counter = 0;

    public AnimationPlane(TransformGroup animal, TransformGroup front, TransformGroup back, Transform3D trans1, Transform3D trans2, Transform3D trans3, JFrame frame) {
        go = new Button("Go");
        this.groupBack = back;
        this.groupFront = front;
        this.groupBody = animal;
        mainFrame = frame;
        rotateTransformX1 = new Transform3D();
        rotateTransformY1 = new Transform3D();
        rotateTransformZ1 = new Transform3D();
        rotateTransformX2 = new Transform3D();
        rotateTransformY2 = new Transform3D();
        rotateTransformZ2 = new Transform3D();
        rotateTransformX3 = new Transform3D();
        rotateTransformY3 = new Transform3D();
        rotateTransformZ3 = new Transform3D();
        transformation1 = trans1;
        transformation2 = trans2;
        transformation3 = trans3;
        FirstMainClass.canvas.addKeyListener(this);
        timer = new Timer(100, this);

        Panel p = new Panel();
        p.add(go);
        mainFrame.add("North", p);
        go.addActionListener(this);
        go.addKeyListener(this);
    }

    private void initialPlaneState() {
        //xloc=0.0f;
        //yloc=0.0f;
        //zloc=0.0f;
        //zoom=0.2f;
        // moveType=1;
        //sign = 1.0f;
        //rotateTransformY1.rotY(-Math.PI / 2.8);
        //translateTransform.mul(rotateTransformY);
        if (timer.isRunning()) {
            timer.stop();
        }
        go.setLabel("Go");
        y1 = y2 = y3 = -3;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // start timer when button is pressed
        if (e.getSource() == go) {
            if (!timer.isRunning()) {
                timer.start();
                go.setLabel("Stop");
            } else {
                timer.stop();
                go.setLabel("Go");
            }
        } else {
            Move();
            transformation1.setScale(new Vector3d(zoom, zoom, zoom));
            transformation2.setScale(new Vector3d(zoom, zoom, zoom));
            transformation3.setScale(new Vector3d(zoom, zoom, zoom));
            transformation1.setTranslation(new Vector3f(x1, y1, z1));
            groupBack.setTransform(transformation1);
            transformation2.setTranslation(new Vector3f(x2, y2, z2));
            groupFront.setTransform(transformation2);
            transformation3.setTranslation(new Vector3f(x3, y3, z3));
            groupBody.setTransform(transformation3);
        }
    }

    private void Move() {
        double a = 0.1;
        if (moveType == 1) {
            if(counter < 10){
            counter++;
                rotateTransformX1.rotX(0);
                transformation1.mul(rotateTransformX1);
            rotateTransformX2.rotX(Math.PI / 90);
            transformation2.mul(rotateTransformX2);
            }
            else if (counter >= 10 && counter < 20) {
                counter++;
                rotateTransformX2.rotX(-Math.PI / 90);
                transformation2.mul(rotateTransformX2);
                z2 += a;
                z1 += a;
                z3 += a;
                rotateTransformX1.rotX(-Math.PI / 90);
                transformation1.mul(rotateTransformX1);
            }
            else if(counter >= 20 && counter < 30){
                counter++;
                rotateTransformX1.rotX(Math.PI / 90);
                transformation1.mul(rotateTransformX1);
                rotateTransformX2.rotX(0);
                transformation2.mul(rotateTransformX2);
                counter = 0;
            }
            if(z3 >= 3){
                counter = 0;
                rotateTransformX1.rotX(0);
                transformation1.mul(rotateTransformX1);
                rotateTransformX2.rotX(0);
                transformation2.mul(rotateTransformX2);
                moveType = 2;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Invoked when a key has been typed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Invoked when a key has been pressed.

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }

}

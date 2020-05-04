//import com.microcrowd.loader.java3d.max3ds.Loader3DS;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;

import java.awt.Color;
import javax.media.j3d.*;
import javax.media.j3d.Material;
import javax.vecmath.*;
import javax.media.j3d.Background;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JFrame;

public class FirstMainClass extends JFrame implements ActionListener {
    static SimpleUniverse universe;
    static Scene scene;
    static Map<String, Shape3D> nameMap;
    static BranchGroup root;
    static Canvas3D canvas;
    static TransformGroup groupBack = new TransformGroup();
    static TransformGroup groupFront = new TransformGroup();
    static Transform3D transformation1 = new Transform3D();
    static Transform3D transformation2 = new Transform3D();
    static Transform3D transformation3 = new Transform3D();
    static TransformGroup groupBody = new TransformGroup();
    static TransformGroup wholePlane;
    static Transform3D transform3D;
    int moveType = 1;
    float x1 = 0;
    float y1 = 0;
    float z1 = 0;
    float x2 = 0;
    float y2 = 0;
    float z2 = 0;
    float x3 = 0;
    float y3 = 0;
    float z3 = 0;
    int counter;
    float zoom = 0.5f;

    public FirstMainClass() throws IOException {
        configureWindow();
        configureCanvas();
        configureUniverse();
        addModelToUniverse();
        setPlaneElementsList();
        addAppearance();
        addImageBackground();
        addLightToUniverse();
        addOtherLight();
        ChangeViewAngle();
        root.compile();
        universe.addBranchGraph(root);
    }

    private void configureWindow() {
        setTitle("Plane Animation Example");
        setSize(760, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configureCanvas() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    private void configureUniverse() {
        root = new BranchGroup();
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        OrbitBehavior ob = new OrbitBehavior(canvas);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
        universe.getViewingPlatform().setViewPlatformBehavior(ob);
    }

    private void addModelToUniverse() throws IOException {
        scene = getSceneFromFile("black.obj");
        // scene=getSceneFromLwoFile("d://3dModels//Aspen.lwo");
        root = scene.getSceneGroup();
    }

    private void addLightToUniverse() {
        Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 20000);
        Bounds bounds1 = new BoundingSphere(new Point3d(0.0, 0.0, 10.0), 20000);
        Color3f color = new Color3f(0 / 255f, 0 / 255f, 0 / 255f);
        Vector3f lightdirection = new Vector3f(-6f, -1f, 5f);
        Vector3f lightdirection1 = new Vector3f(0f, -1.0f, 5f);
        AmbientLight dirlight = new AmbientLight();
        DirectionalLight dirlight1 = new DirectionalLight(color, lightdirection1);
        dirlight.setInfluencingBounds(bounds);
        dirlight1.setInfluencingBounds(bounds1);
        root.addChild(dirlight);

        root.addChild(dirlight1);
    }

    private void printModelElementsList(Map<String, Shape3D> nameMap) {
        for (String name : nameMap.keySet()) {
            System.out.printf("Name: %s\n", name);
        }
    }

    private void setPlaneElementsList() {
        nameMap = scene.getNamedObjects();
        //Print elements of your model:
        printModelElementsList(nameMap);
transformation1 = new Transform3D();
transformation2 = new Transform3D();
transformation3 = new Transform3D();
        wholePlane = new TransformGroup();
        transform3D = new Transform3D();
        transform3D.setScale(new Vector3d(2.5, 0.5, 0.5));
        wholePlane.setTransform(transform3D);
        Hashtable names = scene.getNamedObjects();
        Shape3D[] frontLegs = {(Shape3D) names.get("leg1"), (Shape3D) names.get("leg2"), (Shape3D) names.get("leg7"), (Shape3D) names.get("leg8")};
        Shape3D[] backLegs = {(Shape3D) names.get("leg3"), (Shape3D) names.get("leg4"), (Shape3D) names.get("leg5"), (Shape3D) names.get("leg6")};
        Shape3D body = (Shape3D) names.get("blkw_body");



        //transformation1.rotZ(0);

        root.removeChild(body);
        groupBody.addChild(body);
        for (Shape3D shape : frontLegs) {
            root.removeChild(shape);
            //names.remove(shape);
            groupFront.addChild(shape);
        }
        for (Shape3D shape : backLegs) {
            root.removeChild(shape);
            //names.remove(shape);
            groupBack.addChild(shape);
        }
        groupBack.setTransform(transformation1);
        groupFront.setTransform(transformation2);
        groupBody.setTransform(transformation3);
        groupBack.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        groupFront.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        groupBody.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(groupFront);
        root.addChild(groupBack);
        root.addChild(groupBody);
    }

    Texture getTexture(String path) {
        TextureLoader textureLoader = new TextureLoader(path, "LUMINANCE", canvas);
        Texture texture = textureLoader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 0.0f, 0.0f, 0.0f));
        return texture;
    }

    Material getMaterial() {
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.33f, 0.26f, 0.23f));
        material.setDiffuseColor(new Color3f(0.50f, 0.11f, 0.00f));
        material.setSpecularColor(new Color3f(0.95f, 0.73f, 0.00f));
        material.setShininess(0.3f);
        material.setLightingEnable(true);
        return material;
    }

    private void addAppearance() {
        Appearance planeAppearance = new Appearance();
        planeAppearance.setTexture(getTexture("Drawing.jpeg"));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);
        planeAppearance.setTextureAttributes(texAttr);
        planeAppearance.setMaterial(getMaterial());
        Shape3D plane = nameMap.get("blkw_body");
        plane.setAppearance(planeAppearance);
    }

    private void addColorBackground() {
        Background background = new Background(new Color3f(Color.CYAN));
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }

    private void addImageBackground() {
        TextureLoader t = new TextureLoader("green-grass-56011.jpg", canvas);
        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }

    private void ChangeViewAngle() {
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup vpGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTranslation = new Transform3D();
        Vector3f translationVector = new Vector3f(0.0F, -1.2F, 6F);
        vpTranslation.setTranslation(translationVector);
        vpGroup.setTransform(vpTranslation);
    }

    private void addOtherLight() {
        Color3f directionalLightColor = new Color3f(Color.BLACK);
        Color3f ambientLightColor = new Color3f(Color.WHITE);
        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F); 

        AmbientLight ambientLight = new AmbientLight(ambientLightColor);
        DirectionalLight directionalLight = new DirectionalLight(directionalLightColor, lightDirection);

        Bounds influenceRegion = new BoundingSphere();

        ambientLight.setInfluencingBounds(influenceRegion);
        directionalLight.setInfluencingBounds(influenceRegion);
        root.addChild(ambientLight);
        root.addChild(directionalLight);
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        file.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
        return file.load(new FileReader(location));
    }

    //Not always works
    public static Scene getSceneFromLwoFile(String location) throws IOException {
        Lw3dLoader loader = new Lw3dLoader();
        return loader.load(new FileReader(location));
    }

    public static void main(String[] args) {
        try {
            FirstMainClass window = new FirstMainClass();

            AnimationPlane planeMovement = new AnimationPlane(groupBody, groupFront, groupBack, transformation1, transformation2, transformation3, window);
            window.addKeyListener(planeMovement);
            window.setVisible(true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void Move() {
        if (moveType == 1) { //fly forward and back

            Transform3D transformation = new Transform3D();
            transformation.rotX(Math.PI / 180);
            transformation2.mul(transformation);
            if (counter >= 20) {


                moveType = 2;
            }
            return;
        }
        /*if (moveType == 2) { //fly_away
            zloc += 0.1;
            if (zloc > 2) {
                rotateTransformX.rotX(-Math.PI / 2);
                translateTransform.mul(rotateTransformX);
                moveType = 3;
                return;
            }
        }
        if (moveType == 3) { //fly_away
            yloc += 0.1;
            if (yloc >= 0) {
                rotateTransformX.rotX(-Math.PI / 2);
                translateTransform.mul(rotateTransformX);
                moveType = 4;
                return;
            }
        }
        if (moveType == 4) { //fly_away
            zloc -= 0.1;
            if (zloc <= 0) {
                rotateTransformY.rotX(-Math.PI / 2);
                translateTransform.mul(rotateTransformX);
                moveType = 1;
                return;
            }
        }*/
    }
}

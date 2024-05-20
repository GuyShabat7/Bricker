package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * This is a strategic class that generates a puck if selected.
 */
public class PuckStrategy implements CollisionStrategy {
    /**
     * Represents the number of pucks in the brick.
     */
    public static final int NUM_OF_PUCKS = 2;
    /**
     *  Defines the relative size of the puck compared to the ball.
     */
    public static final float RELATIVE_BALL_SIZE = 0.75f;
    /**
     * Collection of GameObjects.
     */
    protected final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final Sound puckSound;
    private final int ballSize;
    private final Counter bricks;
    private final float ballSpeed;


    /**
     * @param gameObjects       The collection of game objects.
     * @param windowDimensions  The dimensions of the game window.
     * @param imageReader       The image reader object.
     * @param puckSound         The sound for the puck.
     * @param ballSize          The size of the ball.
     * @param bricks            The counter for the bricks.
     * @param ballSpeed         The speed of the ball.
     */
    public PuckStrategy(GameObjectCollection gameObjects, Vector2 windowDimensions,
                        ImageReader imageReader, Sound puckSound, int ballSize,
                        Counter bricks, float ballSpeed) {
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.puckSound = puckSound;
        this.ballSize = ballSize;
        this.bricks = bricks;
        this.ballSpeed = ballSpeed;
    }

    /**
     * This method first deletes the brick. Then, it creates a number of pucks (currently 2).
     *
     * @param gameObject The game object involved in the collision.
     * @param other      The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        if (gameObject instanceof Brick)
            if(this.gameObjects.removeGameObject(gameObject))
                bricks.decrement();

        float puckDiameter = (float) ballSize * RELATIVE_BALL_SIZE;
        int collisionCounter = 0; // A parameter that is not important, because the puck is a type of ball,
        // so the parameter is ignored.
        Renderable renderable = this.imageReader.readImage("assets/mockBall.png",
                true);

        // Creates two pucks.
        for (int i = 0; i < NUM_OF_PUCKS; i++) {
            Puck puck = new Puck(gameObjects,windowDimensions,gameObject.getCenter(),
                    new Vector2(puckDiameter,puckDiameter),
                    renderable, puckSound,collisionCounter, ballSpeed);
            this.gameObjects.addGameObject(puck, Layer.DEFAULT);
        }
    }
}

package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.CameraMove;
import bricker.gameobjects.Puck;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This is a strategic class responsible for creating camera behavior if this is the chosen strategy.
 */
public class CameraStrategy implements CollisionStrategy{
    /**
     *  This constant defines the maximum number of collisions allowed until the camera
     *  behavior stops. It is set to 4.
     */
    public static final int MAX_COLLISIONS = 4;
    /**
     *  This constant defines a factor (1.2f) that might be used in the camera
     *  behavior.
     */
    public static final float FACTOR = 1.2f;
    private final GameObjectCollection gameObjects;
    private final Ball ball;
    private final WindowController windowController;
    private final GameManager gameManager;
    private final Counter bricks;

    /**
     * @param gameObjects       The collection of game objects.
     * @param ball              The ball object.
     * @param windowController  The window controller.
     * @param gameManager       The game manager.
     * @param bricks            The counter of bricks.
     */
    public CameraStrategy(GameObjectCollection gameObjects, Ball ball,
                          WindowController windowController, GameManager gameManager,
                          Counter bricks) {

        this.gameObjects = gameObjects;
        this.ball = ball;
        this.windowController = windowController;
        this.gameManager = gameManager;
        this.bricks = bricks;
    }

    /**
     * This method first deletes the brick.
     * Then, it checks if the ball that collided with the block
     * is a ball or a puck,
     * and also checks if camera behavior already exists.
     * Finally, it creates camera behavior.
     *
     * @param gameObject The game object involved in the collision.
     * @param other      The othe game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        if (gameObject instanceof Brick)
            if(this.gameObjects.removeGameObject(gameObject))
                bricks.decrement();

        // Check if a camera behavior already exists,
        // and check if the ball collided and not the puck.
        if ((other instanceof Ball) && !(other instanceof Puck)) {
            if (gameManager.camera() == null) {
                ball.resetCollisionCounter();

                // Create a new camera behavior.
                gameManager.setCamera(new Camera(ball, Vector2.ZERO,
                        windowController.getWindowDimensions().mult(FACTOR),
                        windowController.getWindowDimensions()));
                gameObjects.addGameObject(new CameraMove(Vector2.ZERO,Vector2.ZERO,null, ball,
                        gameManager, gameObjects, MAX_COLLISIONS), Layer.BACKGROUND);
            }
        }
    }
}

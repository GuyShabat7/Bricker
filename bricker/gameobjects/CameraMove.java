package bricker.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class is responsible for ensuring that the behavior continues until the termination condition.
 */
public class CameraMove extends GameObject {
    private final Ball ball;
    private final GameManager gameManager;
    private final GameObjectCollection gameObjects;
    private final int maxCollisions;

    /**
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param ball              The ball object.
     * @param gameManager       The game manager object.
     * @param gameObjects       The collection of the game objects.
     * @param maxCollisions     The maximum number of collections before the camera behavior ends.
     */
    public CameraMove(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      Ball ball, GameManager gameManager, GameObjectCollection gameObjects,
                      int maxCollisions) {
        super(topLeftCorner, dimensions, renderable);

        this.ball = ball;
        this.gameManager = gameManager;
        this.gameObjects = gameObjects;
        this.maxCollisions = maxCollisions;
    }

    /**
     * Ending the camera behavior after the specified maximum number of collisions of the ball.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCounter() >= maxCollisions) {
            this.gameManager.setCamera(null);
            gameObjects.removeGameObject(this);
        }
    }
}

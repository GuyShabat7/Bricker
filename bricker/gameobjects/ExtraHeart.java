package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class is responsible for creating additional hearts (lives) that can be obtained
 * as a special behavior after colliding with a brick.
 */
public class ExtraHeart extends GameObject {
    private final Counter lives;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final int maxLives;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param lives             The counter for lives.
     * @param gameObjects       The collection of game objects.
     * @param windowDimensions  The dimensions of the game window.
     * @param maxLives              The maximum number of lives
     */
    public ExtraHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      Counter lives, GameObjectCollection gameObjects, Vector2 windowDimensions
                        ,int maxLives) {
        super(topLeftCorner, dimensions, renderable);
        this.lives = lives;
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.maxLives = maxLives;
    }

    /**
     * Updating the deletion of the brick from the screen.
     *
     *  @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > this.windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * @param other The other GameObject.
     * @return boolean - whether the collision of the extra heart is with the user's paddle
     * and not with the additional one.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("user paddle");
    }

    /**
     * This method updates whether the user caught the extra heart, increments the lives accordingly,
     *  and updates the deletion of the heart that went out of the game screen bounds.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (getTopLeftCorner().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
        if (shouldCollideWith(other) && this.lives.value() < maxLives) {
            this.lives.increment();
            gameObjects.removeGameObject(this);
        }
    }
}

package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * The class responsible for the properties of the puck.
 */
public class Puck extends Ball{

    private final GameObjectCollection gameObject;
    private final Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     * Puck class - As part of the special behaviors of the little ball, it behaves similarly to a
     * regular ball with similar properties.
     * @param gameObject       The collection of game objects.
     * @param windowDimensions Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param collisionSound   representing the puck's sound
     * @param collisionCounter A parameter that is not important,
     *                         because the small ball is a kind of ball,
     *                         so the parameter is ignored.
     * @param ballSpeed        The speed of the ball.
     */

    public Puck(GameObjectCollection gameObject, Vector2 windowDimensions, Vector2 topLeftCorner,
                Vector2 dimensions, Renderable renderable, Sound collisionSound,
                int collisionCounter, float ballSpeed) {
        super(topLeftCorner, dimensions, renderable, collisionSound, collisionCounter);
        this.gameObject = gameObject;
        this.windowDimensions = windowDimensions;

        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float ballVelX = (float) Math.cos(angle) * ballSpeed;
        float ballVelY = (float) Math.sin(angle) * ballSpeed;
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        this.setVelocity(new Vector2(ballVelX, ballVelY));
        collisionSound.play();
    }

    /**
     * Removing the brick with the behavior of the puck.
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
        if (this.getCenter().y() > this.windowDimensions.y()) {
            gameObject.removeGameObject(this);
        }
    }

    /**
     *
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
    }
}

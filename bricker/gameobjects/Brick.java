package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class generates the properties of a brick - the brick's behaviors upon collision.
 */
public class Brick extends GameObject implements CollisionStrategy {


    private final CollisionStrategy[] strategies;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collision      An array of CollisionStrategy instances representing the behaviors
     *                       of the brick upon collision.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy[] collision) {
        super(topLeftCorner, dimensions, renderable);
        this.strategies = collision;
    }

    /**
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        onCollision(this,other);
    }

    /**
     * Generates special behaviors of the brick upon collision with it.
     *
     * @param gameObject The GameObject representing the brick.
     * @param other      The other GameObject involved in the collision.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        if (other instanceof Ball) {
            for (CollisionStrategy strategy : this.strategies) {
                if (strategy != null) {
                    strategy.onCollision(gameObject, other);
                }
            }
        }
    }
}

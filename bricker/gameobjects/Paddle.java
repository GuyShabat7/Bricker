package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The class responsible for the properties of the paddle.
 */
public class Paddle extends GameObject {


    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final Vector2 dimensions;
    private final float paddleSpeed;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Responsible for the movement of the paddle.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener,
                  Vector2 windowDimensions, float paddleSpeed) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.dimensions = dimensions;
        this.paddleSpeed = paddleSpeed;
        this.setTag("user paddle");
    }

    /**
     * Updating for the movement of the paddle.
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
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (this.getTopLeftCorner().x() > 0) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if ((this.getTopLeftCorner().x() <  windowDimensions.x() - this.dimensions.x()) ) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }
        setVelocity(movementDir.mult(this.paddleSpeed));
    }
}

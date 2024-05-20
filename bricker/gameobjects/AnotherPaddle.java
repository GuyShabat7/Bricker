package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * This is a class responsible for the properties of the additional paddle.
 */
public class AnotherPaddle extends Paddle {
    private final Vector2 objDimensions;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private int livesOfPaddle;
    private final float paddleSpeed;
    private final GameObjectCollection gameObjects;


    /**
     * @param gameObjects       The collection of game objects.
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param objDimensions     Width and height in window coordinates.
     * @param renderable        The renderable representing the object,
     * @param inputListener     The user's input listener.
     * @param windowDimensions  Game dimensions.
     * @param paddleSpeed       The speed of the paddle.
     * @param livesOfPaddle     The initial number of lives for the paddle.
     */
    public AnotherPaddle(GameObjectCollection gameObjects,
                         Vector2 topLeftCorner,
                         Vector2 objDimensions,
                         Renderable renderable,
                         UserInputListener inputListener,
                         Vector2 windowDimensions,
                         float paddleSpeed,
                         int livesOfPaddle) {
        super(topLeftCorner, objDimensions, renderable, inputListener, windowDimensions,paddleSpeed);
        this.gameObjects = gameObjects;
        this.objDimensions = objDimensions;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.paddleSpeed = paddleSpeed;
        this.livesOfPaddle = livesOfPaddle;
        this.setTag("another paddle");
    }

    /**
     * Updating the movement of the paddle based on the user input.
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
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (this.getTopLeftCorner().x() > 0) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if ((this.getTopLeftCorner().x() <  windowDimensions.x() - this.objDimensions.x()) ) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }
        setVelocity(movementDir.mult(this.paddleSpeed));
    }

    /**
     * Upon collision with a ball, the life count of the additional paddle decreases by 1.
     * The maximum life count is 4.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Paddle's lives decrements when it collided with the ball/puck.
        if (other instanceof Ball) {
            this.livesOfPaddle -= 1;
        }
        if (livesOfPaddle <= 0) {
            this.gameObjects.removeGameObject(this);
        }
    }
}

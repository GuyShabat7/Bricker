package bricker.brick_strategies;

import bricker.gameobjects.AnotherPaddle;
import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This is a strategic class responsible for creating an additional disk
 * if this is the chosen strategy from among the five existing strategies.
 */
public class AnotherPaddleStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Vector2 objDimensions;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final float paddleSpeed;
    private final Counter bricks;
    private final int PADDLE_LIVES = 4; // Maximum paddle's lives.

    /**
     * @param gameObjects      The collection of game objects.
     * @param objDimensions    The dimensions of the object.
     * @param imageReader      The image reader object.
     * @param inputListener    The user input listener object.
     * @param windowDimensions The dimensions of the game window.
     * @param paddleSpeed      The speed of the paddle.
     * @param bricks           The counter of the bricks.
     */
    public AnotherPaddleStrategy(GameObjectCollection gameObjects,
                                 Vector2 objDimensions, ImageReader imageReader,
                                 UserInputListener inputListener,
                                 Vector2 windowDimensions, float paddleSpeed, Counter bricks)
    {
        this.gameObjects = gameObjects;
        this.objDimensions = objDimensions;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.paddleSpeed = paddleSpeed;
        this.bricks = bricks;
    }

    /**
     * This method operates during a collision.
     * First, the brick is deleted.
     * Additionally, the method checks for the absence of an additional paddle.
     * Finally, the method creates an additional paddle (if one does not already exist)
     * and places it in the center of the screen.
     *
     * @param gameObject The game object involved in the collision.
     * @param other      The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {

        // Remove the brick after the collision.
        if (gameObject instanceof Brick)
            if(this.gameObjects.removeGameObject(gameObject))
                bricks.decrement();

        // Check if there is no AnotherPaddle object in the gameObjects collection.
        for (GameObject obj : gameObjects) {
            if (obj instanceof AnotherPaddle) {
                return;// Exit method if the AnotherPaddle is found.
            }
        }

        // If no AnotherPaddle is found, create a new one and add it to the gameObjects collection.
        Renderable renderable = imageReader.readImage("assets/paddle.png",false);
        AnotherPaddle paddle = new AnotherPaddle(this.gameObjects,Vector2.ZERO,
                this.objDimensions, renderable, this.inputListener,
                this.windowDimensions, this.paddleSpeed, PADDLE_LIVES);

        // Set the position of the paddle to the center of the game.
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));
        this.gameObjects.addGameObject(paddle, Layer.DEFAULT);
    }
}

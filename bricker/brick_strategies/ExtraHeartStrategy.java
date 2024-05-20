package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This is a strategic class that generates the special behavior of adding a heart if selected.
 */
public class ExtraHeartStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjects;
    private final Renderable renderable;
    private final Counter lives; // The number of lives in the game.
    private final Vector2 windowDimensions;
    private final Vector2 diameter; // Heart's diameter
    private final Counter bricks;
    private final int maxLives;

    /**
     * @param gameObjects       The collection of game objects.
     * @param renderable        The renderable object for extra heart.
     * @param lives             The counter for lives.
     * @param windowDimensions  The dimensions of the game window.
     * @param diameter          The diameter of the extra heart.
     * @param bricks            The counter for bricks.
     */
    public ExtraHeartStrategy(GameObjectCollection gameObjects, Renderable renderable,
                              Counter lives, Vector2 windowDimensions, Vector2 diameter, Counter bricks,
                              int maxLives) {

        this.gameObjects = gameObjects;
        this.renderable = renderable;
        this.lives = lives;
        this.windowDimensions = windowDimensions;
        this.diameter = diameter;
        this.bricks = bricks;
        this.maxLives = maxLives;
    }

    /**
     * This method generates a falling heart and deletes the brick that created this collision.
     *
     * @param gameObject The game object involved in the collision.
     * @param other      The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject gameObject, GameObject other) {
        if (gameObject instanceof Brick)
            if(this.gameObjects.removeGameObject(gameObject))
                bricks.decrement();
        ExtraHeart heart = new ExtraHeart(other.getCenter(),this.diameter, renderable,lives,
                gameObjects,windowDimensions, maxLives);
        heart.setVelocity(new Vector2(Vector2.DOWN.x() * 100, Vector2.DOWN.y() * 100));
        gameObjects.addGameObject(heart);
    }
}

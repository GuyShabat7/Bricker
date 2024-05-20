package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * This class is responsible for the basic behavior of a brick when colliding with it.
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    /**
     * Collection of GameObjects.
     */
    protected final GameObjectCollection gameObjects;
    private final Counter bricks;

    /**
     * @param gameObjects The collection of game objects.
     * @param bricks      The counter for tracking bricks.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricks) {
        this.gameObjects = gameObjects;
        this.bricks = bricks;
    }

    /**
     * This method deletes the brick when an object collides with it.
     *
     * @param go1 The first GameObject involved in the collision.
     * @param go2 The second GameObject involved in the collision.
     */
    @Override
    public void onCollision(GameObject go1, GameObject go2) {
        if(this.gameObjects.removeGameObject(go1))
            bricks.decrement();

    }
}

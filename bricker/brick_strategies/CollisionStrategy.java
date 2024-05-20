package bricker.brick_strategies;

import danogl.GameObject;

/**
 *
 * This CollisionStrategy interface defines a contract for classes that handle collisions
 * between game objects (GameObjects). By implementing this interface,
 * a class provides a method onCollision that specifies how two GameObjects should behave
 * when they collide in the game. This allows for flexible and modular collision behavior,
 * as different strategies can be implemented by different classes,
 * each handling collisions in its own way.
 */
public interface CollisionStrategy {
    /**
     * @param go1   The first GameObject involved in the collision.
     * @param go2   The second GameObject involved in the collision.
     */
    void onCollision(GameObject go1, GameObject go2);
}

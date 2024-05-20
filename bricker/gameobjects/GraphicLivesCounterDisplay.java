package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class is responsible for representing the hearts displayed on the screen.
 */
public class GraphicLivesCounterDisplay extends GameObject {
    private final Vector2 windowDimensions;
    private final Vector2 dimensions;
    private final GameObjectCollection gameObject;
    private final Counter livesCounter;
    private final Renderable renderable;
    private final Heart[] heartsArray;
    private int heartInd;

    /**
     * Construct a new GameObject instance.
     *
     * @param windowDimensions      Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param dimensions            Width and height in window coordinates.
     * @param renderable            The renderable representing the object. Can be null, in which case
     *                              the GameObject will not be rendered.
     * @param gameObjectCollection  The collection of game objects.
     * @param livesCounter          The counter for lives.
     * @param maxLives              The maximum number of lives
     */
    public GraphicLivesCounterDisplay(Vector2 windowDimensions, Vector2 dimensions, Renderable renderable,
                                      GameObjectCollection gameObjectCollection,  Counter livesCounter
                                        ,int maxLives) {
        super(new Vector2(0, windowDimensions.y() - 40), dimensions, renderable);
        this.windowDimensions = windowDimensions;
        this.dimensions = dimensions;
        this.gameObject = gameObjectCollection;
        this.livesCounter = livesCounter;
        this.renderable = renderable;
        this.heartInd = 0;
        this.heartsArray = new Heart[maxLives];
    }

    /**
     * This method updates the quantity of hearts (lives) displayed on the screen
     * according to the number of lives.
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

        while (livesCounter.value() > heartInd) {
            createHearts();
        }
        if (livesCounter.value() < heartInd) {
            heartInd--;
            gameObject.removeGameObject(this.heartsArray[heartInd], Layer.UI);
        }
    }

    /**
     * This method creates hearts and inserts them into an array according to an index.
     */
    private void createHearts() {
        Heart heart = new Heart(new Vector2(heartInd*20, windowDimensions.y() - 40), this.dimensions,
                renderable);
        gameObject.addGameObject(heart, Layer.UI);
        heartsArray[heartInd] = heart;
        heartInd++;
    }
}

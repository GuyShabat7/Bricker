package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The class responsible for the properties of the numeric counter of lives.
 */
public class NumericCounterDisplay extends GameObject {
    private final Counter lives;
    private final TextRenderable txt;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param lives         The counter for lives.
     */
    public NumericCounterDisplay(Vector2 topLeftCorner, Vector2 dimensions,
                                 Counter lives) {
        super(topLeftCorner, dimensions, null);
        this.lives = lives;
        this.txt = new TextRenderable(this.lives.value() + "");
        this.txt.setColor(Color.GREEN);
        this.renderer().setRenderable(txt);
    }

    /**
     * Updating the number of lives
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
        this.txt.setString(this.lives.value() + "");
    }
}

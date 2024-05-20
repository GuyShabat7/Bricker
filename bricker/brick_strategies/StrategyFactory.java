package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * This class's goal is to select between the existing strategies randomly.
 */
public class StrategyFactory {

    private final GameManager gameManager;
    private final ImageReader imageReader;
    private final UserInputListener userInputListener;
    private final SoundReader soundReader;
    private final Ball ball;
    private final int ballSize;
    private final float paddleSpeed;
    private final Vector2 paddleDimensions;
    private final Counter lives;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    private final Counter bricks;
    private final int maxLives;
    private final Vector2 heartDiameter;
    private final float ballSpeed;
    private final Random random;
    private final Counter index;


    /**
     * @param gameManager           The game manager object.
     * @param imageReader           The image reader object.
     * @param userInputListener     The user input listener object.
     * @param soundReader           The sound reader object.
     * @param ball                  The ball object.
     * @param ballSize              The size of the ball.
     * @param paddleSpeed           The speed of the paddle.
     * @param paddleDimensions      The dimensions of the paddle.
     * @param lives                 The counter of lives.
     * @param windowController      The window controller object.
     * @param windowDimensions      The dimensions of the game window.
     * @param bricks                The counter for bricks.
     * @param heartDiameter         The heart diameter.
     * @param ballSpeed             The speed of the ball.
     */
    public StrategyFactory(GameManager gameManager, ImageReader imageReader,
                           UserInputListener userInputListener,
                           SoundReader soundReader, Ball ball, int ballSize, float paddleSpeed,
                           Vector2 paddleDimensions, Counter lives, WindowController windowController,
                           Vector2 windowDimensions, Counter bricks, int maxLives, Vector2 heartDiameter,
                           float ballSpeed) {

        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.userInputListener = userInputListener;
        this.soundReader = soundReader;
        this.ball = ball;
        this.ballSize = ballSize;
        this.paddleSpeed = paddleSpeed;
        this.paddleDimensions = paddleDimensions;
        this.lives = lives;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.bricks = bricks;
        this.maxLives = maxLives;
        this.heartDiameter = heartDiameter;
        this.ballSpeed = ballSpeed;
        this.random = new Random();
        this.index = new Counter();

    }

    // The probabilities of selection are based on the order of behaviors in the enum class.
    double[] probabilities = {0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

    /**
     * Determines the type of behavior based on a given random number.
     *
     * @param randNumber The random number used to determine the behavior type.
     * @return The behavior type corresponding to the random number.
     */
    private BehaviorsTypes behaviorsTypes(double randNumber) {
        if (randNumber <= probabilities[0]) {
            return BehaviorsTypes.BASIC;
        } else if (randNumber <= probabilities[1]) {
            return BehaviorsTypes.PUCK;
        } else if (randNumber <= probabilities[2]) {
            return BehaviorsTypes.PADDLE;
        } else if (randNumber <= probabilities[3]) {
            return BehaviorsTypes.HEART;
        } else if (randNumber <= probabilities[4]) {
            return BehaviorsTypes.CAMERA;
        } else {
            return BehaviorsTypes.DOUBLE;
        }
    }

    /**
     * Builds an array of selected strategies.
     * The array size is 3 - the maximum number of behaviors a brick can have.
     *
     * @param gameObjects The collection of game objects.
     * @return An array of CollisionStrategy objects representing the selected
     * strategies.
     */
    public CollisionStrategy[] buildStrategiesArray(GameObjectCollection gameObjects) {
        CollisionStrategy[] strategyArray = new CollisionStrategy[3];;
        double randNumber = this.random.nextDouble(); // Random number between 0 and 1.
        BehaviorsTypes type = behaviorsTypes(randNumber);
        if (type != BehaviorsTypes.DOUBLE) {
            addStrategy(gameObjects,type,strategyArray);
            index.reset();
            return strategyArray;
        } else {
            return doubleCase(gameObjects, strategyArray); // A double case.
        }
    }


    /**
     * Handles the case of a double case behavior.
     *
     * @param gameObjects The collection of game objects.
     * @param strategies  The array of CollisionStrategy objects.
     * @return An array of CollisionStrategy objects representing the selected strategies.
     */
    private CollisionStrategy[] doubleCase(GameObjectCollection gameObjects, CollisionStrategy[] strategies) {
        double randDouble = random.nextInt(5) + 1;
        double probability = probabilities[(int)randDouble];
        BehaviorsTypes type1 = behaviorsTypes(probability);
        BehaviorsTypes type2;
        // If first behavior is a double after the double case, we need to add three strategies
        // to the array
        if (type1 == BehaviorsTypes.DOUBLE) {
            for (int i = 0; i < 3; i++) {
                // Generate a random number to determine the type of the second behavior
                // (without double).
                double randNotDouble = random.nextInt(4) + 1;
                probability = probabilities[(int)randNotDouble];
                type2 = behaviorsTypes(probability);
                addStrategy(gameObjects,type2,strategies);
            }
            index.reset();
            return strategies;
        } else {
            // If the first behavior is not a double, add it to the array.
            addStrategy(gameObjects,type1,strategies);
            randDouble = random.nextInt(5) + 1;
            probability = probabilities[(int)randDouble];
            type2 = behaviorsTypes(probability);
            if (type2 != BehaviorsTypes.DOUBLE) {
                // If the second behavior is not a double add it to the array.
                addStrategy(gameObjects,type2,strategies);
                index.reset();
                return strategies;
            } else {
                // If the second behavior is a double, we need to add two
                // more strategies to the array.
                for (int i = 0; i < 2; i++) {
                    double randNotDouble = random.nextInt(4) +  1;
                    probability = probabilities[(int)randNotDouble];
                    type2 = behaviorsTypes(probability);
                    addStrategy(gameObjects,type2,strategies);
                }
            }
        }
        index.reset();
        return strategies;
    }

    /**
     * @param gameObjects        The collection of game objects.
     * @param type               The type of behavior to add.
     * @param strategiesArray    The array of CollisionStrategy objects to add the
     *                           strategy to.
     */
    private void addStrategy(GameObjectCollection gameObjects, BehaviorsTypes type,
                             CollisionStrategy[] strategiesArray) {
        strategiesArray[index.value()] = strategy(gameObjects, type);
        index.increment();
    }

    /**
     * Creates and returns a specific CollisionStrategy based on the provided behaviorsType.
     *
     * @param gameObjects       The collection of game objects.
     * @param behaviorsType     The type of behavior for which to create a strategy.
     * @return                  A collisionStrategy instance corresponding to the behaviorType.
     */
    private CollisionStrategy strategy(GameObjectCollection gameObjects, BehaviorsTypes behaviorsType) {

        if (behaviorsType == BehaviorsTypes.PUCK) {
            Sound sound = soundReader.readSound("assets/blop.wav");
            return new PuckStrategy(gameObjects, windowDimensions,imageReader,sound,ballSize, bricks,
                    ballSpeed);
        } else if (behaviorsType == BehaviorsTypes.PADDLE) {
            return new AnotherPaddleStrategy(gameObjects, paddleDimensions, imageReader,userInputListener,
                    windowDimensions,paddleSpeed, bricks);
        } else if (behaviorsType == BehaviorsTypes.HEART) {
            Renderable image = imageReader.readImage("assets/heart.png",true);
            return new ExtraHeartStrategy(gameObjects, image, lives, windowDimensions,
                    heartDiameter, bricks, maxLives);
        } else if (behaviorsType == BehaviorsTypes.CAMERA) {
            return new CameraStrategy(gameObjects, ball, windowController, gameManager, bricks);
        }
        return new BasicCollisionStrategy(gameObjects,bricks);
    }

}

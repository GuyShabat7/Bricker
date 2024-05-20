package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

import java.util.Random;

/**
 * Main class
 */
public class BrickerGameManager extends GameManager{
    /**
     *  The speed at which the paddle moves.
     */
    private static final float PADDLE_SPEED = 300;
    /**
     *  The speed of the ball.
     */
    private static final float BALL_SPEED = 200;
    /**
     *  The initial number of bricks per row.
     */
    public static final int INITIAL_BRICKS_PER_ROW = 7;
    /**
     *  The initial number of rows of bricks.
     */
    public static final int INITIAL_NUM_OF_ROWS = 10;
    /**
     *  The size of the border around the game window.
     */
    public static final int BORDER_SIZE = 10;
    /**
     *  The size of the ball.
     */
    public static final int BALL_SIZE = 20;
    /**
     *  Constants used as indices for command-line arguments.
     */
    public static final int ARG_0 = 0;
    /**
     *  Constants used as indices for command-line arguments.
     */
    public static final int ARG_1 = 1;
    /**
     *  The initial number of lives for the player.
     */
    public static final int INITIAL_LIVES = 3;
    /**
     *  The dimensions (width and height) of the paddle.
     */
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100,10);
    /**
     *  The diameter of the heart used as a life indicator.
     */
    public static final Vector2 HEART_DIAMETER = new Vector2(10, 10);
    /**
     *  The height of a brick.
     */
    public static final int BRICK_HEIGHT = 15;
    /**
     *  The padding between bricks.
     */
    public static final int BRICK_PADDING = 2;
    /**
     * Maximum number of Lives.
     */
    public static final int MAX_LIFE = 4;

    private final int bricksPerRow;
    private final int numOfRows;
    private Counter lives;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter numberOfBricks;

    /**
     * @param windowTitle       Title of the game window.
     * @param windowDimensions  Dimensions of the game window.
     * @param bricksPerRow      Number of bricks per row.
     * @param numOfRows         Number of rows of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int numOfRows) {
        super(windowTitle,windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.numOfRows = numOfRows;
    }

    /**
     * Initializes the game.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Controller for the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader
                            , SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        this.lives = new Counter(INITIAL_LIVES);
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(80);
        windowDimensions = windowController.getWindowDimensions();
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject background = new GameObject(Vector2.ZERO,windowDimensions,backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background,Layer.BACKGROUND);
        // create ball
        createBall(imageReader, soundReader, windowDimensions);

        //create paddles
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                false);
        createPaddle(inputListener, paddleImage, windowDimensions);

        //create brick
        createBrick(imageReader, soundReader, inputListener);

        //create boundaries
        createBorders(windowDimensions);

        //create hearts
        createGraphicLivesCounterDisplay(imageReader);

        //create numeric counter
        createNumericCounter();
    }

    /**
     * Create a graphic display for the lives counter.
     *
     * @param imageReader Reader for images of lives.
     */
    private void createGraphicLivesCounterDisplay(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage("assets/heart.png",
                true);

        GraphicLivesCounterDisplay graphicCounter =
                new GraphicLivesCounterDisplay(windowDimensions, HEART_DIAMETER,
                heartImage, this.gameObjects(), lives, MAX_LIFE);
        this.gameObjects().addGameObject(graphicCounter);
    }

    /**
     * Creates bricks for the game layout.
     *
     * @param imageReader   Reader for images.
     * @param soundReader   Reader for sounds.
     * @param inputListener Listener for user input.
     */
    private void createBrick(ImageReader imageReader, SoundReader soundReader,
                             UserInputListener inputListener) {
        // Calculate the total number of bricks.
        this.numberOfBricks = new Counter(this.bricksPerRow * this.numOfRows);

        // Load the image for the bricks.
        Renderable brickImage = imageReader.readImage("assets/brick.png",
                false);

        // Create strategies for collision behavior.
        StrategyFactory collisionStrategies = new StrategyFactory(this,
                imageReader,inputListener,
                soundReader,ball,BALL_SIZE,PADDLE_SPEED,PADDLE_DIMENSIONS,
                lives,windowController,windowDimensions,numberOfBricks,MAX_LIFE, HEART_DIAMETER,
                BALL_SPEED);


        int brickWidth = (int) (windowDimensions.x() - 2*BORDER_SIZE - 1)/bricksPerRow;

        // Iterate over the rows and columns to create individual bricks.
        for (int i = 0; i < this.bricksPerRow * this.numOfRows; i ++) {
            int row = i / bricksPerRow;
            int col = i % bricksPerRow;
            int xPos = BORDER_SIZE + (col * (brickWidth + BRICK_PADDING));
            int yPos  =  BRICK_PADDING + (row * (BRICK_HEIGHT + BRICK_PADDING));
            Vector2 brickPos = new Vector2(xPos, yPos);

            Brick brick = new Brick(brickPos, new Vector2(brickWidth,BRICK_HEIGHT),brickImage,
                    collisionStrategies.buildStrategiesArray(gameObjects()));

            gameObjects().addGameObject(brick, Layer.DEFAULT);
        }
    }

    /**
     * Creates a numeric counter display for remaining lives.
     */
    private void createNumericCounter() {
        GameObject numericCounterDisplay = new NumericCounterDisplay(
                new Vector2( Vector2.ZERO.x(), this.windowDimensions.y() - 30),
                new Vector2(20,20),
                this.lives);

        gameObjects().addGameObject(numericCounterDisplay,Layer.BACKGROUND);
    }

    /**
     * Creates border GameObjects to bound the game area.
     *
     * @param windowDimensions Dimensions of the game window.
     */
    private void createBorders(Vector2 windowDimensions) {
        GameObject rightBorder = new GameObject(Vector2.ZERO, new Vector2(BORDER_SIZE,
                windowDimensions.y()), null);
        gameObjects().addGameObject(rightBorder, Layer.DEFAULT);

        GameObject leftBorder = new GameObject(new Vector2(windowDimensions.x() - BORDER_SIZE,0),
                new Vector2(BORDER_SIZE, windowDimensions.y()),null);
        gameObjects().addGameObject(leftBorder, Layer.DEFAULT);

        GameObject topBorder = new GameObject(Vector2.ZERO,new Vector2(windowDimensions.x(),
                BORDER_SIZE),null);
        gameObjects().addGameObject(topBorder, Layer.DEFAULT);
    }

    /**
     * @param imageReader       Reader for images.
     * @param soundReader       Reader for sounds.
     * @param windowDimensions  Dimensions of the game window.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        int collisionCounter = 0; // Number of collisions involving the ball
        Sound collisionSound = soundReader.readSound("assets/blop.wav");

        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_SIZE,BALL_SIZE), ballImage,
                collisionSound, collisionCounter);

        ballPosForStarting(windowDimensions);

        gameObjects().addGameObject(ball);
    }

    /**
     * Sets the initial position and velocity for the ball.
     *
     * @param windowDimensions Dimensions of the game window.
     */
    private void ballPosForStarting(Vector2 windowDimensions) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /**
     * Creates the paddle GameObject for the player.
     *
     * @param inputListener     Listener for user input.
     * @param paddleImage       Renderable image for the paddle.
     * @param windowDimensions  Dimensions of the game window.
     */
    private void createPaddle(UserInputListener inputListener, Renderable paddleImage,
                              Vector2 windowDimensions) {
        GameObject userPaddle = new Paddle(
                Vector2.ZERO,
                PADDLE_DIMENSIONS,
                paddleImage,
                inputListener, windowDimensions,PADDLE_SPEED);

        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, (int) windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle, Layer.DEFAULT);
    }

    /**
     * Updates the game state based on the time that has passed since the last frame.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkLives();
        checkForGameEnd();
    }

    /**
     *  Checks the current state of the ball to determine if a life should be decremented.
     *  If the ball's position is below the window's bottom edge, decrements a life
     *  and resets the ball's position for the next life.
     */
    private void checkLives() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            this.lives.decrement();
            this.ballPosForStarting(this.windowDimensions);
        }
    }

    /**
     *  Checks if the game has ended by determining if all bricks are destroyed or if the
     *  player has no lives left. If the game has ended, prompts the user with a message
     *  to play again or close the window based on their choice.
     */
    private void checkForGameEnd() {
        String prompt = "";
        if (this.numberOfBricks.value() <= 0) {
            prompt = "You Win!";
        }

        if (this.lives.value() <= 0) {
            prompt = "You Lose!";
        }

        if (!prompt.isEmpty()) {
            prompt += " Play Again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }


    /**
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            int bricksPerRow = Integer.parseInt(args[ARG_0]);
            int numOfRows = Integer.parseInt(args[ARG_1]);
            new BrickerGameManager(
                    "Bricker",
                    new Vector2(700,500),bricksPerRow,numOfRows).run();
        } else {
            new BrickerGameManager(
                    "Bricker",
                    new Vector2(700,500), INITIAL_BRICKS_PER_ROW,
                    INITIAL_NUM_OF_ROWS).run();
        }
    }
}

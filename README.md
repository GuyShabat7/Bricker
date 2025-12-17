# 🧱 Bricker 

<img width="2816" height="1536" alt="Gemini_Generated_Image_jxzluojxzluojxzl" src="https://github.com/user-attachments/assets/5ca5a050-6448-4239-8791-b777e0e09597" />



> A robust, object-oriented implementation of the classic Arkanoid game developed in Java. This project emphasizes modular design using Design Patterns like **Strategy** and **Factory** to manage complex game behaviors.

## 📋 Table of Contents
- [Game Description](#game-description)
- [Architecture & Design Patterns](#architecture--design-patterns)
  - [Brick Strategies](#brick-strategies)
  - [Strategy Factory](#strategy-factory)
- [Game Object Management](#game-object-management)
  - [Lives Display](#lives-display)
- [Special Behaviors](#special-behaviors)
- [Class Diagram](#class-diagram)

---

## 🎮 Game Description
Bricker is a breakout-style game where the player controls a paddle to bounce a ball and destroy bricks. The game features various power-ups and penalties hidden within bricks, dynamic camera movements, and a sophisticated life-tracking system.

## 🏗 Architecture & Design Patterns

### Brick Strategies
To maintain modularity and the Open/Closed Principle, the project uses the **Strategy Pattern** for brick collision behaviors.

* **Interface:** `CollisionStrategy` (located in `brick.brick_strategies`) defines the `onCollision` contract.
* **Implementation:** The `Brick` class holds a reference to a strategy but remains unaware of the specific behavior implementation.
* **Execution:** Upon collision, the `Brick` delegates logic to the strategy's `onCollision` method. This allows behaviors (like spawning extra balls or changing camera focus) to be swapped at runtime without modifying the `Brick` class.

### Strategy Factory
The `StrategyFactory` class is responsible for selecting and instantiating behaviors. It supports complex behavior composition, specifically the **Double Behavior**:

1.  **Random Selection:** Strategies are chosen randomly via an Enum.
2.  **Double Strategy Logic:**
    * If a "Double" behavior is selected, the factory recursively selects up to 3 distinct behaviors (e.g., Camera + Puck + Extra Heart).
    * The logic ensures a balanced distribution of power-ups while preventing conflicting states.

---

## ❤️ Game Object Management

### Lives Display
The game features a dual-display system for player lives, managed by the `BrickerGameManager`:

1.  **Graphical Display (`GraphicLivesCounterDisplay`):**
    * Inherits from `GameObject`.
    * Manages a collection of `Heart` objects.
    * Visualizes lives as heart icons at the bottom of the screen.
2.  **Numeric Display (`NumericCounterDisplay`):**
    * Renders the numeric value of remaining lives.

Both displays listen to the centralized `checkLives` method in the main manager, ensuring synchronization when the ball falls or an extra heart is collected.

---

## ✨ Special Behaviors

| Behavior | Class / Logic | Description |
| :--- | :--- | :--- |
| **Puck** | `PuckStrategy` → `Puck` | Spawns multiple "Puck" balls (inheriting from `Ball`) with independent physics and audio. Pucks are removed when exiting screen bounds. |
| **Extra Heart** | `HeartStrategy` → `Heart` | Drops a falling heart. If collected by the main paddle, it increments the life counter (up to a max cap) and updates both displays. |
| **Camera Focus** | `CameraStrategy` → `CameraMove` | Focuses the game camera on the ball for a set number of collisions. Uses a modified `Ball` class to track collision counts before resetting to default view. |
| **Extra Paddle** | `AnotherPaddleStrategy` → `AnotherPaddle` | Spawns a secondary paddle in the screen center. Has its own life counter and disappears after taking specific damage. |

---

## 📊 Class Diagram (Architecture Overview)

```mermaid
classDiagram
    class BrickerGameManager {
        -checkLives()
    }
    class Brick {
        -CollisionStrategy strategy
    }
    class CollisionStrategy {
        <<interface>>
        +onCollision(GameObject, Counter)
    }
    class StrategyFactory {
        +getStrategy()
    }
    
    %% Strategies
    class PuckStrategy
    class CameraStrategy
    class HeartStrategy
    class DoubleStrategy
    
    %% Game Objects
    class Puck
    class CameraMove
    class Heart
    class AnotherPaddle
    
    BrickerGameManager --> StrategyFactory : Uses
    StrategyFactory ..> CollisionStrategy : Creates
    Brick --> CollisionStrategy : Has
    
    CollisionStrategy <|.. PuckStrategy
    CollisionStrategy <|.. CameraStrategy
    CollisionStrategy <|.. HeartStrategy
    CollisionStrategy <|.. DoubleStrategy
    
    PuckStrategy ..> Puck : Spawns
    CameraStrategy ..> CameraMove : Activates
    HeartStrategy ..> Heart : Spawns
    DoubleStrategy --> CollisionStrategy : Composes



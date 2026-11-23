package org.example;

public final class Enemy implements GameEntity {
    private PolarPosition position;
    private final EnemyType type;
    private final MovementStrategy strategy;
    private int health;
    private final int scorePoints;
    private boolean alive;

    public Enemy(double spawnRadius,
                 double initialAngle,
                 EnemyType type,
                 MovementStrategy strategy,
                 int health,
                 int scorePoints) {
        this.position = new PolarPosition(spawnRadius, initialAngle);
        this.type = type;
        this.strategy = strategy;
        this.health = health;
        this.scorePoints = scorePoints;
        this.alive = health > 0;
    }

    public Enemy(double spawnRadius, double initialAngle, MovementStrategy strategy) {
        this(spawnRadius, initialAngle, EnemyType.BASIQUE, strategy, 1, 1);
    }

    public Enemy(double spawnRadius, double initialAngle, double speed) {
        this(spawnRadius, initialAngle, new RadialMovement(speed));
    }

    @Override
    public PolarPosition getPosition() {
        return position;
    }

    @Override
    public void update() {
        if (!alive) {
            return;
        }

        PolarPosition d = strategy.calculerDeplacement(1.0);
        position = new PolarPosition(
                position.getRayon() + d.getRayon(),
                position.getAngle() + d.getAngle()
        );
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void takeDamage() {
        if (!alive) {
            return;
        }
        health--;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    public void die() {
        health = 0;
        alive = false;
    }

    public EnemyType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public MovementStrategy getStrategy() {
        return strategy;
    }
}
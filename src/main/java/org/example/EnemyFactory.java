package org.example;

public final class EnemyFactory {

    private EnemyFactory() {
    }

    public static Enemy creerEnemy(EnemyType type, double angle) {
        return switch (type) {
            case BASIQUE -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.BASIQUE,
                    new RadialMovement(0.6),
                    1,
                    1
            );
            case RAPIDE -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.RAPIDE,
                    new SinuousMovement(0.9, 0.015, 0.08),
                    1,
                    2
            );
            case TANK -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.TANK,
                    new AcceleratedMovement(0.25, 0.002),
                    3,
                    3
            );
        };
    }
}
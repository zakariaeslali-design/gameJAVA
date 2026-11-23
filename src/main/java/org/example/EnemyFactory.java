package org.example;

public final class EnemyFactory {

    private EnemyFactory() {
    }

    public static Enemy creerEnemy(EnemyType type, double angle) {
        return switch (type) {
            // Chaque type a son propre rayon de collision : le tank est plus gros
            case BASIQUE -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.BASIQUE,
                    new RadialMovement(10.0),
                    1,
                    1,
                    Enemy.DEFAULT_COLLISION_RADIUS
            );
            case RAPIDE -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.RAPIDE,
                    new SinuousMovement(14.0, 0.12, 0.6),
                    1,
                    2,
                    12.0
            );
            case TANK -> new Enemy(
                    0.0,
                    angle,
                    EnemyType.TANK,
                    new AcceleratedMovement(4.0, 0.5),
                    3,
                    3,
                    30.0
            );
        };
    }
}
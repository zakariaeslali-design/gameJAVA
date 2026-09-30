package org.example;


public class Main {

    public static void main(String[] args) {
        Player player = new Player(0.0, 3);
        GameEngine engine = new GameEngine(player);

        // Le joueur tourne d'un quart de tour
        player.move(Math.PI / 4);

        // Un ennemi sort du centre sur le même angle que le joueur
        Enemy enemy = new Enemy(0.0, player.getPosition().getAngle(), 10.0);
        engine.addEntity(enemy);

        // Le joueur tire : le projectile part de sa position vers le centre
        Projectile projectile = new Projectile(
                player.getPosition().getRayon(),
                player.getPosition().getAngle(),
                15.0);
        engine.addEntity(projectile);

        System.out.println("=== Orbital Defense - test console sprint 1 ===");
        afficherEtat(0, engine, enemy, projectile);

        for (int tick = 1; tick <= 8; tick++) {
            tickTemporaire(engine);
            afficherEtat(tick, engine, enemy, projectile);
        }

        System.out.println("=== Fin du test ===");
    }

    // TODO : remplacer par engine.tick(...) quand Wassim aura mergé son travail
    private static void tickTemporaire(GameEngine engine) {
        for (GameEntity entity : engine.getEntities()) {
            entity.update();
        }
    }

    // TODO : remplacer par engine.printStateConsole() quand Wassim aura mergé son travail
    private static void afficherEtat(int tick, GameEngine engine, Enemy enemy, Projectile projectile) {
        System.out.printf("Tick %d | vies joueur : %d | angle joueur : %.2f%n",
                tick, engine.getPlayer().getHealth(), engine.getPlayer().getPosition().getAngle());
        System.out.printf("   ennemi     : rayon = %6.2f | vivant = %b%n",
                enemy.getPosition().getRayon(), enemy.isAlive());
        System.out.printf("   projectile : rayon = %6.2f | actif  = %b%n",
                projectile.getPosition().getRayon(), projectile.isAlive());
        System.out.printf("   distance ennemi-projectile : %.2f%n",
                enemy.getPosition().distanceTo(projectile.getPosition()));
    }
}

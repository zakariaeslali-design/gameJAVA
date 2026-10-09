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
        printStateConsole(engine);

        for (int tick = 1; tick <= 8; tick++) {
            tickTemporaire(engine);
            printStateConsole(engine);
        }

        System.out.println("=== Fin du test ===");
    }

    // TODO : remplacer par engine.tick(...) quand Wassim aura mergé son travail
    private static void tickTemporaire(GameEngine engine) {
        for (GameEntity entity : engine.getEntities()) {
            entity.update();
            printStateConsole(engine);
        }
    }

    // Affiche l'état courant du jeu en texte lisible dans la console
    static void printStateConsole(GameEngine engine) {
        Player player = engine.getPlayer();
        PolarPosition pos = player.getPosition();

        StringBuilder sb = new StringBuilder();
        sb.append("=== État du jeu ===\n");
        sb.append(String.format("Position joueur : angle=%.2f, rayon=%.2f%n",
                pos.angle(), pos.rayon()));
        sb.append("Vies : ").append(player.getHealth()).append("\n");
        sb.append("Entités actives : ").append(engine.getEntities().size()).append("\n");
        sb.append("Game over : ").append(engine.isGameOver()).append("\n");

        System.out.println(sb);
    }
}

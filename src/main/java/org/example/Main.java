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

        // j'ai supprimé la méthode printStateConsole() et je l'ai remplacée
        // par view.render(engine), l'affichage est maintenant dans ConsoleView.-zakaria
        GameView view = new ConsoleView();

        System.out.println("=== Orbital Defense - test console sprint 1 ===");
        view.render(engine);

        for (int i = 1; i <= 8; i++) {
            engine.tick(0.1);
            view.render(engine);
        }

        System.out.println("=== Fin du test ===");
    }
}
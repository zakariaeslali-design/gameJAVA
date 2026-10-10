package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

    // Intervalle entre deux tick() du moteur, en secondes.
    // Plus c'est grand, plus le jeu est lent (les entités avancent d'un pas à chaque tick).
    private static final double TICK_INTERVAL = 0.5;

    private GameEngine engine;
    private GraphicalView view;

    // start() est appelée par JavaFX au lancement : elle crée le jeu et ouvre la fenêtre
    @Override
    public void start(Stage stage) {
        // Même initialisation que dans Main : un joueur et un moteur
        Player player = new Player(0.0, 3);
        engine = new GameEngine(player);

        // TEMPORAIRE : un ennemi et un projectile pour voir quelque chose bouger.
        // À supprimer quand spawnEnemy() et playerShoot() existeront.
        player.move(Math.PI / 4);
        engine.addEntity(new Enemy(0.0, player.getPosition().getAngle(), 10.0));
        engine.addEntity(new Projectile(
                player.getPosition().getRayon(),
                player.getPosition().getAngle(),
                15.0));

        // La vue graphique d'Amine sert de contenu à la fenêtre
        view = new GraphicalView();
        Scene scene = new Scene(view, 800, 800);
        stage.setTitle("Orbit Invasion");
        stage.setScene(scene);
        stage.show();

        // Boucle de jeu : JavaFX appelle handle() à chaque image (~60 fois par seconde)
        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;
            private double accumulateur = 0;

            @Override
            public void handle(long now) {
                // Première image : on mémorise juste l'heure
                if (last == 0) {
                    last = now;
                    return;
                }

                // Temps écoulé depuis l'image précédente (now est en nanosecondes)
                double delta = (now - last) / 1_000_000_000.0;
                last = now;

                // On ne fait avancer le moteur que toutes les TICK_INTERVAL secondes
                accumulateur += delta;
                if (accumulateur >= TICK_INTERVAL) {
                    engine.tick(accumulateur);
                    accumulateur = 0;
                }

                // Mais on redessine à chaque images
                view.render(engine);
            }
        };
        timer.start();
    }

    // Point d'entrée : lance JavaFX, qui appelle ensuite start()
    public static void main(String[] args) {
        launch(args);
    }
}
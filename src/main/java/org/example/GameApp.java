package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

    private static final double TARGET_FPS = 60.0;
    private static final double TICK_INTERVAL = 1.0 / TARGET_FPS;
    private static final double MAX_FRAME_DELTA = 0.25; // évite un rattrapage excessif si la fenêtre est gelée

    private GameEngine engine;
    private GraphicalView view;

    @Override
    public void start(Stage stage) {
        Player player = new Player(0.0, 3);
        engine = new GameEngine(player);

        view = new GraphicalView();
        Scene scene = new Scene(view, 800, 800);

        GameController controller = new GameController(engine);
        KeyboardInputHandler inputHandler = new KeyboardInputHandler(controller);
        scene.setOnKeyPressed(inputHandler);

        stage.setTitle("Orbit Invasion");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;
            private double accumulateur = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                double delta = Math.min((now - last) / 1_000_000_000.0, MAX_FRAME_DELTA);
                last = now;

                accumulateur += delta;
                while (accumulateur >= TICK_INTERVAL) {
                    engine.tick(TICK_INTERVAL);
                    accumulateur -= TICK_INTERVAL;
                }

                view.render(engine);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
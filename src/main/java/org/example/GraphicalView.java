package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GraphicalView extends Pane implements GameView {
    private final Canvas canvas;
    private final double width = 800;
    private final double height = 800;
    private final double centerX = width / 2;
    private final double centerY = height / 2;

    public GraphicalView() {
        this.canvas = new Canvas(width, height);
        this.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void render(GameEngine engine) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        double orbitRadius = 100.0;
        gc.strokeOval(centerX - orbitRadius, centerY - orbitRadius, orbitRadius * 2, orbitRadius * 2);

        for (GameEntity entity : engine.getEntities()) {
            if (!entity.isAlive()) continue;

            PolarPosition pos = entity.getPosition();

            double x = centerX + pos.toCartesianX();
            double y = centerY - pos.toCartesianY();

            switch (entity) {
                case Player p -> drawPlayer(gc, x, y);
                case Enemy e -> drawEnemy(gc, x, y);
                case Projectile proj -> drawProjectile(gc, x, y);
            }
        }
    }

    private void drawPlayer(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.CYAN);
        gc.fillOval(x - 10, y - 10, 20, 20);
    }

    private void drawEnemy(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.RED);
        gc.fillOval(x - 8, y - 8, 16, 16);
    }

    private void drawProjectile(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.YELLOW);
        gc.fillOval(x - 4, y - 4, 8, 8);
    }
}
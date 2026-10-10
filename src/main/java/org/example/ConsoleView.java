package org.example;

public class ConsoleView implements GameView {
	
	@Override
	
	public void render(GameEngine engine) {
		Player player =engine.getPlayer();
		PolarPosition pos = player.getPosition();
		
		StringBuilder sb = new StringBuilder();
        sb.append("=== État du jeu ===\n");
        sb.append(String.format("Position joueur : angle=%.2f, rayon=%.2f%n",
                pos.angle(), pos.rayon()));
        sb.append("Vies : ").append(player.getHealth()).append("\n");

        for (GameEntity entity : engine.getEntities()) {
            if (entity == player) {
                continue;
            }
            PolarPosition p = entity.getPosition();
            sb.append(String.format("  - %s : angle=%.2f, rayon=%.2f, vivant=%b%n",
                    entity.getClass().getSimpleName(), p.angle(), p.rayon(), entity.isAlive()));
        }

        sb.append("Entités actives : ").append(engine.getEntities().size()).append("\n");
        sb.append("Game over : ").append(engine.isGameOver()).append("\n");

        System.out.println(sb);
		
	}
	
	
	
}

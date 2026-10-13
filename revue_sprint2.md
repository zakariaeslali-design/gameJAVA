# Revue du sprint 2 : fenêtre graphique et contrôles

Projet Orbit Invasion, équipe : Zakariae, Amine, Bilal, Salah

## Livré

* `GameView`
* `GraphicalView`
* `ConsoleView`
* `KeyboardInputHandler`
* `GameController`
* `playerShoot()`
* `GameApp`
* `PlayerTest` (4 tests)

Le joueur bouge avec les flèches et tire.

## Non livré

* Rien de prévu n'est resté en suspens. Le nettoyage des ennemis et projectiles est planifié au sprint 3.

## Bugs connus et points relevés

* `GameApp` n'était pas dans le backlog mais était nécessaire pour ouvrir la fenêtre et lancer la boucle de jeu. Elle est maintenant officielle.
* Le `pom.xml` a changé : surefire 3.2.5 et le plugin Maven JavaFX (lancement par `mvn javafx:run`).
* `ConsoleView` a été faite par Zakariae.
* Un ennemi temporaire reste dans `GameApp` (à supprimer au sprint 3 quand `spawnEnemy()` existera). Le projectile temporaire a déjà été supprimé.
* Le rayon de l'orbite (100) est écrit en dur dans `GraphicalView` et le cercle est petit dans la fenêtre de 800 pixels. À centraliser dans une constante au sprint 3.
* `GameEntity` est sealed et `GraphicalView` a un `switch` sans `default` : ajouter une nouvelle entité (explosion, drop) oblige à modifier les deux. À coordonner au sprint 5.
* Le `mainClass` du jar est `org.example.Main`, alors que le jeu démarre par `GameApp`. À corriger au sprint 5.
* Les ennemis sortent de la fenêtre et les projectiles ne sont jamais retirés : sprint 3.

## Ce qu'on change pour le sprint suivant

* Ajouter collisions, nettoyage, apparition des ennemis, vies, score et HUD (sprint 3).
* Centraliser `ORBIT_RADIUS` et supprimer l'ennemi temporaire de `GameApp`.

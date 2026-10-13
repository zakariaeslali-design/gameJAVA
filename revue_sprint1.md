# Revue du sprint 1 : bases du jeu en console

Projet Orbit Invasion, équipe : Zakariae, Amine, Bilal, Salah

## Livré

* `PolarPosition`
* `Vector2D`
* `GameEntity` (sealed)
* `Player`
* `Enemy`
* `Projectile`
* `GameEngine` avec `tick(delta)`
* `Main` console
* Tests de `PolarPosition`

## Non livré

* Rien de prévu n'est resté en suspens. Les fonctionnalités de collision, de nettoyage et de score sont planifiées au sprint 3.

## Bugs connus et points relevés

* Les tests de `PolarPosition` ne tournaient pas : Maven utilisait surefire 2.17, qui ne détecte pas JUnit 5 (`Tests run: 0`). Corrigé au sprint 2 (surefire 3.2.5).
* `printStateConsole()` n'affichait pas le score (prévu au sprint 3, donc normal).
* Dans la sortie console, un ennemi et un projectile se croisent sans réaction et les entités mortes restent comptées : normal, car `checkCollisions()` et le nettoyage sont au sprint 3.

## Ce qu'on change pour le sprint suivant

* Passer à surefire 3.2.5 pour que `Tests run` ne soit plus à 0.
* Ajouter la fenêtre graphique, les contrôles clavier et le tir (sprint 2).

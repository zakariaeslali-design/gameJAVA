# Revue du sprint 3 : Orbit Invasion

Sprint 3 : jeu jouable (collisions, ennemis, score, vies)
Revue faite par : Zakariae
Équipe : Zakariae, Amine, Bilal, Salah

## Résultat global

Toutes les tâches du sprint 3 sont terminées. Le jeu est jouable : on gagne des points en tuant des ennemis et on perd quand trop d'ennemis s'échappent.

## Définition de « terminé »

- [x] Le code compile : `mvn clean compile`
- [x] Les tests passent : `mvn clean test`
- [x] Le jeu se lance : `mvn javafx:run`
- [x] Le diagramme UML du sprint est à jour avec le code
- [x] Le README reste valable
- [x] Commit sur sa branche, relecture rapide, merge sur `main`

## Revue par tâche

| Tâche | Responsable | Statut | Remarque |
|---|---|---|---|
| `checkCollisions()` | Zakariae | Fait | Seuil sur `distanceTo()`, cas du croisement entre deux ticks géré |
| Nettoyage des entités | Zakariae | Fait | Ennemis morts et projectiles sortis retirés sans modifier la liste pendant le parcours |
| `spawnEnemy(angle)` | Bilal | Fait | Appelé à intervalle régulier dans `tick()`, angle aléatoire |
| `checkEscapes()` | Bilal | Fait | Ennemi au-delà de l'orbite marqué mort, une vie perdue |
| Suppression de l'ennemi temporaire de `GameApp` | Bilal | Fait | |
| `takeDamage()` utilisé + game over | Salah | Fait | `gameOver` à `true` à 0 vie |
| Score | Salah | Fait | +1 par ennemi tué, stocké dans `GameEngine` |
| HUD (score et vies) | Amine | Fait | Affiché dans `GraphicalView` |
| Constante `ORBIT_RADIUS` | Amine | Fait | Une seule valeur partagée entre moteur et vue, orbite agrandie |
| `ConsoleView` : score | | Fait | |
| UML sprints 1 et 2 | Salah | Fait | Diagramme de classes et séquence de la boucle de jeu |
| UML sprint 3 | | Fait | Diagramme de classes + séquences (voir `uml-sprint3.md`) |

## Tests (testeur du sprint : Amine)

- [x] `checkCollisions` : touche / ne touche pas / croisement entre deux ticks
- [x] `checkEscapes`
- [x] Nettoyage des entités
- [x] Score
- [x] Game over

## Ce qui a bien marché

- Les tâches liées par personne ont évité les conflits sur `GameEngine`.
- Le seuil de collision règle le problème des croisements entre deux ticks.

## Points d'attention pour le sprint 4

- Prévoir `git pull` avant de toucher `GameEngine`, `GraphicalView` et `GameApp`, qui seront très modifiés (factory, stratégies, assets).
- Faire valider l'UML du sprint 4 par le prof avant de coder.
- Garder les tests à jour quand `Enemy` sera créé par `EnemyFactory`.

## Conclusion

Le sprint 3 est validé : toutes les tâches sont faites, le jeu est jouable et les tests passent. On peut passer au sprint 4 (variété des ennemis et vrais assets).

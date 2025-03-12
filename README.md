# ChessFX

ChessFX est un projet développé en Java permettant de jouer aux échecs, intégrant une intelligence artificielle avancée capable d'affronter un joueur humain. Ce projet met l'accent sur la prise de décision algorithmique et l’optimisation des performances, en utilisant notamment les algorithmes **MiniMax** et **Alpha-Bêta pruning** pour améliorer les capacités de l'IA.

---

## 1. Intelligence Artificielle

### Arbre de Décision

L'IA repose sur un **arbre de décision**, où chaque nœud représente un état du jeu après un coup. Cet arbre permet d’explorer différentes séquences de jeu et de sélectionner le meilleur coup en utilisant l’algorithme **MiniMax**.

- Chaque nœud contient l’état du jeu après un coup donné.
- Chaque branche représente un coup possible.
- L’IA analyse plusieurs coups à l’avance pour maximiser ses chances de victoire.

### Algorithme MiniMax

L’algorithme **MiniMax** fonctionne sous l'hypothèse que l'adversaire choisit toujours le meilleur coup possible. Son objectif est de maximiser le score de l’IA et de minimiser celui de l’adversaire.

#### Évaluation des positions

L’évaluation d’une position est basée sur une **fonction de score** attribuant une valeur à chaque pièce :

| Pièce   | Valeur |
|---------|--------|
| Dame    | 15     |
| Tour    | 4      |
| Cavalier / Fou | 3 |
| Pion    | 1      |

Des ajustements sont appliqués en fonction de la position sur l’échiquier :

- **Les pièces centrales** ont une valeur doublée.
- **Un échec et mat** donne un score de **+9999** pour l’IA et **-9999** pour l’adversaire.
- **Un pat** est évalué à **0**.
- **Le roque** procure un avantage de **10 points**.

### Optimisation avec l’Élagage Alpha-Bêta

L’algorithme MiniMax souffre d’un coût exponentiel en temps de calcul. Pour optimiser son exécution, l’IA intègre un **élagage Alpha-Bêta**, réduisant ainsi le nombre de branches explorées.

- Si une branche atteint un seuil défavorable, elle est ignorée.
- Cette optimisation permet d’explorer plus profondément l’arbre pour le même coût en performance.

---

## 2. Architecture du Code

### Modélisation des pièces

Les différentes pièces sont modélisées à l'aide d’une **classe abstraite `Piece`**, héritée par les classes spécifiques (`King`, `Queen`, `Bishop`, `Knight`, `Rook`, `Pawn`).

| Classe    | Rôle |
|-----------|------|
| `Piece`   | Classe mère définissant les attributs communs et des méthodes abstraites |
| `King`    | Gestion du déplacement du roi, y compris le **roque, l’échec et le mat** |
| `Queen`   | Combinaison des mouvements de la tour et du fou |
| `Bishop`  | Déplacement en diagonale |
| `Knight`  | Déplacement en "L" |
| `Rook`    | Déplacement en ligne droite |
| `Pawn`    | Déplacement spécifique avec **promotion et prise en passant** |

### Gestion de l’échiquier

L’échiquier est représenté sous forme de **matrice 8x8 de `Piece`** et géré par la classe `Board`. Celle-ci a plusieurs responsabilités :

- **Validation des déplacements** selon les règles du jeu.
- **Détection des situations critiques** : échec, mat, pat.
- **Coordination de l'IA et du joueur humain**.

---

## 3. Interface Graphique

L’interface utilisateur est développée avec **JavaFX** et offre une expérience interactive fluide.

### Fonctionnalités

- **Menu principal** permettant de choisir entre une partie contre l'IA ou un adversaire humain.
- **Sélection des couleurs** : possibilité de jouer les Blancs ou les Noirs.
- **Affichage du plateau et des pièces** sous forme de caractères ASCII.
- **Mise en évidence du roi en échec** avec une case colorée.
- **Console de suivi des actions** affichant les coups joués et l'état du jeu.
- **Retour au menu** disponible à tout moment.

---

## 4. Démonstration Technique

Vous pouvez voir ci-dessous les interfaces du programme. Dans un premier temps, à gauche, le menu qui s'ouvre au lancement du programme puis à droite l'interface qui est disponible lorsqu'une partie est en cours.

![image (1)](https://github.com/user-attachments/assets/f6095ed0-ce3c-44e7-bcb9-d3eb5a010fe8)
![image](https://github.com/user-attachments/assets/763406a3-5d76-4a2b-8945-0d92c2f79439)

- **Écran de menu**
- **Affichage du plateau**
- **Fonctionnement de l’IA**
- **Messages d’alerte en cas d’échec, mat ou pat**

---




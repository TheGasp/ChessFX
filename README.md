
# ChessFX

ChessFX est un projet réalisé dans le cadre scolaire visant à créer en JAVA un jeu d'echec. Nous avons par la suite intégré une intelligence artificielle pouvant jouer contre le joueur humain.


## Demonstration du programme

Vous pouvez voir ci-dessous les interfaces du programme. Dans un premier temps, à gauche, le menu qui s'ouvre au lancement du programme puis à droite l'interface qui est disponible lorsqu'une partie est en cours.

![image (1)](https://github.com/user-attachments/assets/f6095ed0-ce3c-44e7-bcb9-d3eb5a010fe8)
![image](https://github.com/user-attachments/assets/763406a3-5d76-4a2b-8945-0d92c2f79439)


## Aspects techniques
### A.	Les enum
Dans un premier temps, nous avions décidé d’attribuer un entier entre 0 et 5 à chaque type de pièce (0 pour le pion, 1 pour la tour, …) et un booléen à chaque couleur (false pour noir et true pour blanc). Cependant nous nous sommes vite rendu compte que ce système avait de nombreux inconvénients. En effet, la compréhension du code et son écriture était compliqué. Il fallait aller rechercher à chaque fois à quoi correspondait un 2 ou un true). De plus, si par erreur un entier qui ne correspondait à aucune pièce se glissait dans le code, il y aurait eu des problèmes. C’est pourquoi, afin de palier à ce problème et de facilement identifier le type d’une pièce, sa couleur, le statut du jeu, les types de mouvements ainsi que les erreurs qui peuvent se produire dans la partie nous avons décidé de créer de nouveaux types de variables : des types énumérés. Les variables de ce type prennent nécessairement leur valeur dans une liste prédéfinie. Par convention, ces valeurs sont notées en majuscule. Nous avons donc créé le type PieceType qui a pour valeurs KING, QUEEN, BISHOP, KNIGHT, ROOK et PAWN, les type ColourPiece qui a pour valeur WHITE et Black. Nous avons aussi créé les types GameStatus, MoveReturnCode et MoveType. De cette manière une instruction pour tester si une pièce est un roi passe de if (testedPiece.type == 5) à if (testedPiece.type == PieceType.KING) ce qui est bien plus compréhensible.

### B.	Les pièces

Dès nos premières réflexions sur le projet, nous avons identifié la nécessité de créer une classe abstraite Piece de laquelle hériterait toutes les classes spécifiques de pièces (les classes King, Queen, Bishop, Knight, Rook et Pawn). Nous avons en effet identifié plusieurs avantages à ce raisonnement. Déjà, certain attributs et méthodes communs à toutes les pièces ne seraient codés qu’un fois. De plus cela facilitait la représentation de l’échiquier. En effet il est représenté par une matrice de Pièces ce qui aurait été impossible si chaque pièce était d’une classe différente. Enfin, pour certaines méthodes comme celles liées au déplacement, nous avons pu créer une unique méthode abstraite dans la classe Pièce à appeler depuis l’échiquier qui renvoie aux méthodes des déplacements spécifiques de chaque pièce. Ceci facilite allège la classe échiquier. Dans cette même optique, nous avons créé dans la classe Pièce une méthode statique qui permet de vérifier si les coordonnées de position d’une pièce se trouve réellement sur l’échiquier. Ceci limite l’écritures de tests fastidieux.

Nous avons souhaité stocker la position des pièces dans l’échiquier lui-même. En effet cela permet à l’échiquier d’avoir une vision générale de l’état du jeu et de pouvoir ainsi autoriser ou non les pièces à se déplacer en fonctions des autres. Ceci allait notamment être un atout majeur pour l’intelligence artificielle. Ainsi pour se déplacer, la pièce demande sa position à l’échiquier et à l’utilisateur la position sur laquelle elle doit aller et elle vérifie si ce déplacement est dans ses mouvements autorisés (en diagonale pour le fou, en ligne droite pour la tour, d’une case pour le roi, …). C’est l’échiquier qui s’assurera ensuite qu’étant donné l’état du jeu, la pièce puisse faire ce déplacement.
La classe Pièce est pourvue d’une seconde méthode abstraite. Celle-ci permet, pour chaque type de pièce d’obtenir la liste des positions accessible à partir de la position actuelle. Cette méthode sera particulièrement utile pour l’élaboration de l’intelligence artificielle car elle permettra de parcourir facilement tous les états de jeux possibles.

La classe King, est un peu plus complexe que les autres classes de pièce, en effet, nous avons codé dans cette classe le déplacement du roque (petit et grand), le test d’échec et le test du roi. Pour le roque, nous vérifions si c’est le premier mouvement du roi. Pour l’échec, on parcourt toutes les cases de l’échiquier, et si on rencontre une pièce de la couleur opposée à celle du roi en question capable d’accéder à la position du roi, alors il y a échec pour ce roi. Pour le Mat, il y a deux conditions à vérifier. Il y a Mat si le roi est en échec sur toutes ses cases adjacentes et si aucune pièce de sa couleur ne peut intercepter une de ces mises en échec. 

### C.	L’échiquier

La classe de l’échiquier (Board) est une des plus grosses classes du programme puisque c’est elle qui centralise le mouvement des pièces sur l’échiquier. Comme nous l’avons précédemment dit, l’échiquier est représenté dans la classe Board par une matrice de Piece. 
Le processus de déplacement des pièces est assez complexe car l’nécessite de nombreux tests effectués à divers moments. Pour déplacer une pièce, on regarde d’abord si la pièce à déplacer est bien sur sa position de départ et si elle est de la couleur de celui à qui c’est le tour, on s’assure que le mouvement reste sur l’échiquier, enfin, on regarde si le mouvement ne met pas le roi en échec et si c’est un déplacement conforme (en diagonal pour le fou, d’une case ou en roque pour le roi, …). On peut alors déplacer la pièce normalement, en roque ou en effectuant une promotion du pion si les cas se présentent et on met à jour la couleur du joueur suivant.

### D.	L’intelligence artificielle

#### Arbre de décision

En Théorie des Jeux, on considère qu’on peut associer à chaque jeu un arbre dont chaque nœud représente les décisions d’un joueur. Ainsi à chaque branche est associé un état de jeu et on peut représenter sur cette arbre l’ensemble des parties qui peuvent être jouées. Dans le cas d’un jeu d’échec, à la racine, le premier joueur a trente-cinq coups possibles, il y aura donc trente-cinq branches qui représentent chacun de ces coups. Et à partir de tous ces coups possibles on pourra déterminer les suivants. Et ainsi de suite…
Afin de prédire le coup le plus avantageux possible, nous allons construire cet arbre de décision et lui appliquer ensuite l’algorithme MiniMax qui déterminera le meilleur coup à jouer. Nous avons choisi pour cet arbre une structure assez classique. Il est construit par récursivité. Chaque nœud possède deux valeurs : le score attribué à l’état du jeu que le nœud représente et le coup joué pour atteindre cet état ; ainsi qu’une liste de ses nœuds fils qui sont eux aussi des arbres. Nous avions au départ pensé à stocker l’ensemble du plateau de jeu sous forme de matrice dans chaque nœud mais cette proposition n’avait pas d’intérêt et aurait été trop coûteux en complexité spatiale et temporelle.

#### MiniMax

Nous construisons donc cet arbre de décision à chaque fois que c’est au tour de l’IA de jouer et nous lui appliquons l’algorithme MiniMax pour déterminer quel coup l’IA doit jouer. L’algorithme MiniMax fonctionne en supposant que l’adversaire utilise le même système de décision que l’IA. Ce système de décision est une fonction d’évaluation qui attribue un score au coup joué. Plus la position est avantageuse pour l’IA, plus le score est élevé. Comme nous ne disposons pas de fonction d’évaluation parfaite pour les Echec, nous réitérons le processus afin de prévoir plusieurs coups à l’avance et de déterminer le coup optimal. Dans notre algorithme, nous évaluons le meilleur coup en prévoyant 3 coups à l’avance ce qui était le meilleur rapport performance / temps d’exécution). Nous avons choisi d’attribuer un score à chaque pièce (15 pour la dame, 3 pour le cavalier et le fou, 4 pour la tour, 1 pour le pion). Ces valeurs ont été choisies à la suite d’une série de tests. Nous avons, de plus, décidé de doubler le score des pièces qui se trouvaient au centre de l’échiquier car elles ont en général plus d’importance pour le jeu. Le score d’un coup est la somme des scores des pièces du joueur une fois le coup joué (comptée positive pour l’IA et négative pour l’adversaire). Un score extrême est attribué à l’échec et mat (+9999 pour l’IA et -9999 pour l’adversaire). Un pat est à 0. Un roque apporte un bénéfice de 10 points. L’objectif de l’algorithme est de maximiser le score de l’IA et de minimiser celui du joueur.

#### Elagage Alpha-Bêta

Afin d’optimiser notre algorithme et notamment de réduire la complexité temporelle, nous avons décidé d’ajouter un élagage Alpha-Bêta. En effet, l’inconvénient de l’algorithme MiniMax est qu’il explore toutes les branches de l’arbre ce qui représente environ 35^^n possibilités, n étant le nombre de coups prévu à l’avance. L’élagage Alpha-Bêta sert à limiter les effets de cet inconvénient en réduisant le nombre de branches exploitées. En effet, si la valeur d’un nœud atteint un certain seuil, il est inutile d’exploiter ses nœuds descendants de ce nœud car la branche est trop mauvaise. Grâce à l’élagage Alpha-Bêta, on peut maintenant explorer l’arbre plus en profondeur pour le même temps d’exécution.

### E.	L’interface graphique

Lorsque l’utilisateur lance le programme, il se retrouve devant une fenêtre de menu qui lui offre la possibilité de choisir s’il souhaite jouer contre l’IA ou avec un ami. S’il choisit de jouer contre l’IA, la scène est effacée (clear) et de nouveaux boutons apparaissent pour faire choisir à l’utilisateur s’il souhaite jouer les blancs ou les noirs. Suivant son choix, les blancs seront positionnés en haut ou en bas du plateau. 
L’interface de jeu
Dès que ce choix est fait, la fenêtre de Menu se ferme et la fenêtre de Jeu apparaît : un plateau de 8x8 cases alternativement colorées et sur lesquelles sont positionnées les pièces. Nous avons choisi d’utiliser les caractères ASCII représentant les pièces d’échec plutôt que des images car l’intégration de texte était plein simple que celle d’images. Cette fenêtre de jeu dispose aussi d’une console qui donne en continu des informations à l’utilisateur : qui doit jouer, les coups déjà joués, est-ce qu’il y a échec, mat, pat ? A ce titre, lorsque le roi est en échec, la case sur laquelle il se trouve se colore en rouge pour plus de clarté. À tout moment, l’utilisateur peut quitter la partie en cliquant sur un bouton qui le ramène au Menu. Lorsque la partie est terminée, un message s’affiche et on ne peut plus bouger de pièces.
D’un point de vue technique, le Menu est lancé depuis la classe Chess. Il récupère les paramètres choisis par l’utilisateur et lance la classe Jeu avec les paramètres spécifiques choisis.



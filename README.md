# Application MCommerce

L'application MCommerce est une application de e-commerce très basique basée sur l'architecture micro-service qui est utilisé dans le cadre du cours sur OpenClassroom: https://openclassrooms.com/fr/courses/4668216-optimisez-votre-architecture-microservices

Dans le cadre du cours MGL846 de l'ÉTS, nous utiliserons cette application comme projet de session afin d'y intégrer divers types de tests (unitaire, intégration, système).
Le diagramme ci-bas illustre bien les différentes interactions entre les micro-services et donne une vue d'ensemble des fonctionnalités du système (l'image est également prise du site OpenClassroom).

![](https://user.oc-static.com/upload/2018/04/23/15245110970986_p01-figure20.png)

# Requis
- OpenJDK 17

# Description des différents microservices

- `eureka-server`: sert à trouver les autres microservices qui sont instantiés et qui peuvent être appelés. Ce service est déployé sur le port 8761.
- `config-server`: contient les configurations nécessaires permettant aux microservices de bien s'instantier. Ce service est déployé sur le port 9101.
- `springadmin`: acquiert l'information des microservices accessibles à travers `eureka-server`. Ce service est déployé sur le port 9105.
- `mproduits`: service qui permet d'interagir avec les produits (voir diagramme ci-haut). Ce service est déployé sur le port 9001.
- `mcommandes`: service qui permet d'interagir avec les commands (voir diagramme ci-haut). Ce service est déployé sur le port 9002.
- `mpaiements`: service qui permet d'interagir avec les paiments (voir diagramme ci-haut). Ce service est déployé sur le port 9003.
- `clientui`: service qui démarre le ui de l'application. Ce service est déployé sur le port 8080.
- `api-gateway`: passerelle afin d'avoir un point d'entrée unique. Ce service est déployé sur le port 80.

# Description des composantes implicites

- `ribbon`: équilibreur de charge entre les services démarrés et fonctionnels
- `zuul`: API gateway to permet d'appeler le bon service avec les bons accès et permet de retourner les réponses de ses services.
- `zipkin`: garde trace de tous les appels fait aux services et les montre sur ça propre page. Ceci est une application autonome.

# Ordre de démarrage des microservices

Pour démarrer il faut télécharger un IDE permettant de travailler avec les microservices Java comme IntelliJ et démarrer les classes `*Application.java` dans chacun des projets suivant et dans l'ordre suivant.

| **Ordre** | **Requis** | **Nom**                |
|-----------|------------|------------------------|
| 1         | Oui        | eureka-server          |
| 2         | Oui        | config-server          |
| 3         | Oui        | microservice-produits  |
| 3         | Oui        | microservice-commandes |
| 3         | Oui        | microservice-paiements |
| 4         | Oui        | clientui               |
| 5         | Oui        | api-gateway            |
| 6         | Non        | springadmin            |
| 6         | Non        | zipkin                 |

Vous pouvez ensuite aller sur http://localhost/mcommerce-ui pour visualiser votre application.
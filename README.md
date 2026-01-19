# I2CS_Ex3
This repository contains Ex3 from introducing to computer science course, this project is about creating the Pac-Man game and reuses the Ex2 project

Pacman goal is to eat all the food (green big dots) in the map while avoiding the ghosts that can eat the Pacman.
The first part of the project is to create an algorithm for the Pacman to choose witch direction to go in every step,
the algorithm is based on the BFS algorithm: (summarty of the algorithm steps; more deatiled explanation below)

1. check the distance from the nearest ghost to the Pacman using BFS algorithm.
2. if the distance is smaller than a threshold (set to 10 in this project) the Pacman will try to maximize the distance from the nearest ghost.
3. if there is green dot (power that make the ghosts eatable) nearby and closer that the nearest ghost the Pacman will go to eat it and then will hunt.
4. if there is no ghost nearby the Pacman will try to minimize the distance to the nearest food dot.

                                 This is my best score at max difficulty level(4) with lowest fps(dt 200):
<img width="895" height="1016" alt="best score" src="https://github.com/user-attachments/assets/5ffcc8d5-a5e2-4390-9713-2042c277cc45" />


detailed explanation of the algorithm and functions:

The alforithm has a data members for the map, pacman and ghosts. before every move the algorithm will update all the data from the game.
1. At first, it calculates the distance from the nearest ghost to the pacman using distToClosestGhost function that return the distance and the direction to the nearest ghost.
2. if the distance is smaller than a threshold (set to 10 in this project) it will have 3 options:
   a. if the ghosts are eatable it will try to hunt the nearest ghost using huntClosestGhostDir that reuturn the direction to the nearest ghost.
   b. if the ghosts aren't eatable, it will check the distance to the nearest green dot (power) using distToClosestGreenDot function that return the distance
      * if the distance to the nearest green dot is smaller than the distance to the nearest ghost it will go to eat the green dot using dirToClosestGreenDot function that return the direction to the nearest green dot.
      * else it will try to maximize the distance from all the ghosts using dirToRun function that return the direction that maximize the distance from all the ghosts.
3. if the distance to the nearest ghost is bigger than the threshold it will try to minimize the distance to the nearest food dot.

dirToRun function:
this function using allDistance function (see Ex2) for all ghosts and create array of maps witch every map represent the distance from the ghost to all the pixels in the map.
then combaine all the maps to one map that represent the minimum distance from all the ghosts to all the pixels in the map. (using combainedGhostDistMaps function)
after that it check all 4 possible moves from the pacman position and choose the direction that maximize the distance from all the ghosts.
for example if the pacman pixel value in the combined map is 5 and the right pixel to the pacman value is 6 it will choose to go right.
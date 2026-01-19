# I2CS_Ex3
This repository contains Ex3 from introducing to computer science course, this project is about creating the Pac-Man game and reuses the Ex2 project

Pacman goal is to eat all the food (green big dots) in the map while avoiding the ghosts that can eat the Pacman.
The first part of the project is to create an algorithm for the Pacman to choose witch direction to go in every step,
the algorithm is based on the BFS algorithm: (summarty of the algorithm steps; more deatiled explanation below)

1. check the distance frim the nearest ghost to the Pacman using BFS algorithm.
2. if the distance is smaller than a threshold (set to 10 in this project) the Pacman will try to maximize the distance from the nearest ghost.
3. if there is green dot (power that make the ghosts eatable) nearby and closer that the nearest ghost the Pacman will go to eat it and then will hunt.
4. if there is no ghost nearby the Pacman will try to minimize the distance to the nearest food dot.

<img width="895" height="1016" alt="best score" src="https://github.com/user-attachments/assets/5ffcc8d5-a5e2-4390-9713-2042c277cc45" />

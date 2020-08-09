# Predator-Prey-Simulation
#### A predator-prey simulation involving foxes, rabbits, wolfs, deer and grasses in an enclosed rectangular field.

How to start:
- Create an object of the Simulator class.
- Then call one of the following method:
  - simulate(int numSteps) - where numSteps is an int representing number of time steps to run the simulation for.
  - runLongSimulation() - run simulation for a default of 3000 time step.
 
### Simulator Parameters
The parameters for the simulator are listed as instance variable of the 'Simulator' class. To amend the parameters, simply amend the instance variable of this class.
 
### Simulator Description
#### Rabbits (color code – Yellow) and Deer (color code – Black):
* Rabbits and deer are 2 of the herbivores in this simulator. Their behaviors are quite similar. They both feed on plants/grass.
* They move around and search for their food i.e. move towards grass, if they are in the neighboring tile and eats them.
* In the process, if they meet their species, who happens to be of their opposite sex, then they may breed granted they have reached their minimum breeding age. Upon breeding, new offspring are produced in the neighboring spaces, if the space is not already occupied by a species.
* They may also die of hunger if they don’t find food for a specific number of steps. They also have a maximum age which upon reaching, they die.
* Occasionally, they may also be infected with disease, which may spread if the infected species of a particular kind comes in contact with their own kind, i.e. An infected rabbit can only spread its infection to other rabbits, it cannot spread to deer or other species. Same goes for deer. An infected deer can spread its disease to other deer, but cannot spread its disease to rabbits or other species
* During night time, both rabbits and deer and not active, i.e. they don’t move.

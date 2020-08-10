# Predator-Prey-Simulation
#### A predator-prey simulation involving different types of species competing for resources and surviving in an enclosed area.

How to start:
- Create an object of the Simulator class.
- Then call one of the following method:
  - simulate(int numSteps) - where numSteps is an int representing number of time steps to run the simulation for.
  - runLongSimulation() - run simulation for a default of 3000 time step.
 
### Simulator Parameters
The parameters for the core simulator are listed as instance variable of the 'Simulator' class. To amend the parameters, simply amend the instance variable of this class.

The parameters for the  species in the simulator is located as instance variable of the respective species class. For example, parameters for 'Fox' species is located in the instance variable of the 'Fox' class.
 
### Simulator Species Description and Their Properties
#### Rabbits (color code – Yellow) and Deer (color code – Black):
* Rabbits and deer are 2 of the herbivores in this simulator. Their behaviors are quite similar. They both feed on plants/grass.
* They move around and search for their food i.e. move towards grass, if they are in the neighboring tile and eats them.
* In the process, if they meet their species, who happens to be of their opposite sex, then they may breed granted they have reached their minimum breeding age. Upon breeding, new offspring are produced in the neighboring spaces, if the space is not already occupied by a species.
* They may also die of hunger if they don’t find food for a specific number of steps. They also have a maximum age which upon reaching, they die.
* Occasionally, they may also be infected with disease, which may spread if the infected species of a particular kind comes in contact with their own kind, i.e. An infected rabbit can only spread its infection to other rabbits, it cannot spread to deer or other species. Same goes for deer. An infected deer can spread its disease to other deer, but cannot spread its disease to rabbits or other species
* During night time, both rabbits and deer are not active, i.e. they don’t move.

#### Plants (color code – green):
* Grasses represent plant species in this simulator. They are eaten by deer and rabbits.
* Grasses can spread/grow/reproduce just like they do in real life. However, they cannot move like animal species do.
* Rainfall influences grass growth, as in, whenever rain falls, grasses may grow at places where there are no animals at the moment, depending on the grass creation probability value set.
* Plants are not affected by diseases, unlike animals.

#### Wolfs (color code – red) and Foxes (color code – blue):
* Wolfs and foxes are the 2 carnivores in this simulator.
* Wolfs feed on both rabbits and deer.
* However, foxes feed only on rabbits.
* They move around and search for their respective food i.e. wolfs move to the neighboring tile if there is either rabbit or deer in them. Foxes do the same for rabbits. If they find their respective food in their neighboring tile, they move towards it and eats them.
* In the process, if they meet their respective species, who happens to be of their opposite sex, then they may breed granted they have reached their minimum breeding age. Upon breeding, new offspring are produced in the neighboring spaces, if the space is not already occupied by a species.
* They may also die of hunger if they don’t find food for a specific number of steps. They also have a maximum age which upon reaching, they die.
* Occasionally, they may also be infected with disease, which may spread if the infected species of a particular kind comes in contact with their own kind
* Wolfs are the only nocturnal animal in the simulator i.e. they can move and interact even when it's night time. However, foxes remain inactive during night time i.e. they don’t move during night time.

**Note – Species who are infected by disease are represented by the magenta color, in the simulator grid.**

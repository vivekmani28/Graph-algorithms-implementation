# Graph-algorithms-implementation

This is a Implementation of Breadth first, Depth first and A* Search algorithms in Java.

These implementation solves the problem of finding a route between a pair of cities that are inputed.

### File Descriptions

#### GraphSearch.java

This file contains the implementation of all three algorithms. It reads the distance inputs from the two csv files. User Query inputs are expected as command line arguments in the below given format.

#### City.java

This is the class representing a city, it has the latitude, longitude, roads assosiated with a city, its F and H values that are being used in the algorithm.

#### city.csv (Test Data)

This is a simple CSV file containing the city names along with their latitude and longitude values, one city per line.

#### roads.csv (Test Data)

This is a simple CSV file containing the road details (pair of cities along with the distance between them), one road per line. 

### Expected Input format:
``
java GraphSearch Source_city Destination_City Algo_type(DFS/BFS/A*)
``


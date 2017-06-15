import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

public class GraphSearch{
	
	public static void main(String args[]){
		if (args.length != 3){
			System.out.println("Usuage:: Source_city Destination_City Algo_type(DFS/BFS/A*)");
			return;
		}
		String sourceCityName;
		String destinationCityName;
		String algoType = args[2].toUpperCase();
		if(!(algoType.equals("A*") || algoType.equals("BFS") || algoType.equals("DFS")) ){
			System.out.println("Invalid Algo_Type. Use Algo_type(DFS/BFS/A*)");
			return;
		}
		
		Map<String, City> cityMap = new TreeMap<String, City>();
		populateCityRoads(cityMap);
		sourceCityName = args[0].trim().toUpperCase();
		destinationCityName = args[1].trim().toUpperCase();

		
		City sourceCity = cityMap.get(sourceCityName);
		int noNodes =0;
		if(algoType.equals("DFS")){
			Stack<City> cityStack = new Stack<City>();
			City currentCity = sourceCity;
			cityStack.push(currentCity);
			currentCity.isVisited = true;
			System.out.print("Expanded Nodes " + currentCity.getName());
			noNodes++;
			City tmpCity ;
			boolean isCompleted = false;
			
			while(!cityStack.isEmpty()){
				currentCity = cityStack.peek();
				for(int i=0; i < currentCity.getNeighbours().size() && !isCompleted ; i++){
					tmpCity = currentCity.getNeighbours().get(i);
					if(tmpCity.parentCity == null && tmpCity.isVisited != true)
						tmpCity.parentCity = currentCity;
					if(tmpCity.isVisited != true){
						tmpCity.isVisited = true;
						System.out.print("-->"+tmpCity.getName());
						noNodes++;
						cityStack.push(tmpCity);
						currentCity = tmpCity;
						i=-1;
						if(tmpCity.getName().equals(destinationCityName)){
							isCompleted = true;
						}		
					}
				}
				cityStack.pop();
			}
			if(isCompleted){
				System.out.print("\nDFS search was success\n Number of expanded Nodes is "+ noNodes);
				printPath(cityMap.get(destinationCityName));
				
			}
		}
		else if (algoType.equals("BFS")){
			//BFS algorithm
			City currentCity = sourceCity;
			Queue<City> cityQueue = new LinkedList<City>();
			cityQueue.add(currentCity);
			currentCity.isVisited = true;
			System.out.print("Expanded Nodes :" + currentCity.getName());
			noNodes++;
			City tmpCity ;
			boolean isCompleted = false;
			
			while(!cityQueue.isEmpty()){
				currentCity = cityQueue.poll();
				for(int i=0; i < currentCity.getNeighbours().size() && !isCompleted; i++){
					tmpCity = currentCity.getNeighbours().get(i);
					if(tmpCity.parentCity == null && tmpCity.isVisited != true)
						tmpCity.parentCity = currentCity;
					if(tmpCity.isVisited != true){
						tmpCity.isVisited = true;
						System.out.print("-->"+tmpCity.getName());
						noNodes++;
						cityQueue.add(tmpCity);
						if(tmpCity.getName().equals(destinationCityName)){
							isCompleted = true;
						}
					}
				}
			}
			if(isCompleted){
				System.out.print("\nBFS search was success\n Number of expanded Nodes is "+ noNodes);
				printPath(cityMap.get(destinationCityName));
				
			}
		}
		else{
			//A* algorithm
			City currentCity = sourceCity;
			City destinationCity = cityMap.get(destinationCityName);
			
			double tmpCost;
			boolean isCompleted = false;
			Queue<City> openQueue = new PriorityQueue<City>(20,heuristicComparator);
			currentCity.hValue = getDistance(currentCity, destinationCity);

			openQueue.add(currentCity);

			currentCity.gValue = 0;
			System.out.print("Expanded Nodes :" );
			
			while(!openQueue.isEmpty() && !isCompleted){
				currentCity = openQueue.poll();
				System.out.print(currentCity.getName() + "-->");
				noNodes++;
				if(currentCity.equals(destinationCity)){
					isCompleted = true;
					break;
				}
				for(int i=0; i < currentCity.getNeighbours().size(); i++){
					City tmpCity;
					tmpCity = currentCity.getNeighbours().get(i);
					tmpCost = currentCity.gValue +  currentCity.neighboursDistance[currentCity.getNeighbours().indexOf(tmpCity)]+ getDistance(tmpCity, destinationCity);
					if(openQueue.contains(tmpCity)){
						if(tmpCity.gValue <= tmpCost)
							continue;
					}
					else if(tmpCity.isVisited == true){
						if(tmpCity.gValue <= tmpCost)
							continue;
						tmpCity.isVisited = false;
						tmpCity.hValue = getDistance(tmpCity, destinationCity);
						openQueue.add(tmpCity);
					}
					else{
						tmpCity.hValue = getDistance(tmpCity, destinationCity);
						tmpCity.gValue = currentCity.gValue +  currentCity.neighboursDistance[currentCity.getNeighbours().indexOf(tmpCity)];
						openQueue.add(tmpCity);
					}
					tmpCity.gValue = currentCity.gValue +  currentCity.neighboursDistance[currentCity.getNeighbours().indexOf(tmpCity)];
					tmpCity.parentCity = currentCity;
				}
				currentCity.isVisited = true;
			}
			if(isCompleted){
				System.out.print("\b\b\b   ");
				System.out.print("\nA* search was success\nNumber of expanded Nodes is "+ noNodes);
				printPath(destinationCity);
			}
		}
	}
	static void printPath(City destinationCity){
		City tmpParent, tmpCity;
		double cost =0;
		StringBuilder output = new StringBuilder();
		tmpParent = destinationCity;
		tmpCity = destinationCity.parentCity;
		while(tmpCity != null){
			output.insert(0,"-->"+tmpCity.getName());
			cost += tmpCity.neighboursDistance[tmpCity.getNeighbours().indexOf(tmpParent)];
			tmpParent = tmpCity;
			tmpCity = tmpCity.parentCity;
		}
		System.out.println("\nPath of Traversal :" +output.substring(3));
		System.out.println("Cost of the traversal : " + cost);
	}

	public static Comparator<City> heuristicComparator = new Comparator<City>(){
		@Override
		public int compare(City c1, City c2) {
            return (int) ((c1.gValue+c1.hValue) - (c2.hValue + c2.gValue));
        }
	};
	
	private static void populateCityRoads(Map<String, City> cityMap) {
		try{
			// Read and populate cityMap from city.csv file
			BufferedReader reader = new BufferedReader(new FileReader("city.csv"));
            String line;
            City newCity;
			while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] attributes = line.split(",");
                newCity = new City(attributes[0].trim().toUpperCase(), attributes[1].trim().toUpperCase(), attributes[2].trim());
                cityMap.put(newCity.getName(), newCity);
            }
			reader.close();
			
			// Read and populate roadList from roads.csv file
			reader = new BufferedReader(new FileReader("roads.csv"));
			while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] attributes = line.split(",");
                City city1 = (City)cityMap.get(attributes[0].trim().toUpperCase());
                City city2 = (City)cityMap.get(attributes[1].trim().toUpperCase());
                double distance = Double.parseDouble(attributes[2].trim());
                city1.getNeighbours().add(city2);
                city2.getNeighbours().add(city1);
                city1.neighboursDistance[city1.getNeighbours().indexOf(city2)] = distance;
                city2.neighboursDistance[city2.getNeighbours().indexOf(city1)] = distance;
            }
			reader.close();
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		} 
	}
	static double getDistance(City source, City destination){
		double distance = 0;
		distance = Math.pow((destination.getLatitude() - source.getLatitude()), 2) + Math.pow((destination.getLongitude() - source.getLongitude()), 2) ;
		distance = Math.sqrt(distance)*100;
		return distance;
	}
}

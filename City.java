import java.util.ArrayList;
import java.util.List;

class City{
	
	String name;
	double longitude, latitude;
	List<City> neighbours;
	double[] neighboursDistance = new double[25];
	City parentCity;
	boolean isVisited;
	double hValue, gValue;
	
	City(){}
	
	City(String name, String latitude, String longitude){
		this.name = name;
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
		this.neighbours = new ArrayList<City>();
		this.isVisited = false;
		this.parentCity = null;
	}
	
	@Override
	public String toString() {
		StringBuilder neighbourNames = new StringBuilder();
		for(int i=0; this.neighbours.size() > i ; i++)
			neighbourNames.append( this.neighbours.get(i).getName()).append(",");
		return "City [name=" + name + ", longitude=" + longitude + ", latitude=" + latitude +  ", parentCity=" + parentCity +  ", isVisited=" + isVisited + ", neighbours="
				+ neighbourNames + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getNeighbours() {
		return neighbours;
	}
	
	public List<City> getUnvisitedNeighbours(){
		List<City> unvisitedNeighbours = new ArrayList<City>();
		for(int i=0; this.neighbours.size() > i ; i++){
			if(this.neighbours.get(i).isVisited == false){
				unvisitedNeighbours.add(this.neighbours.get(i));
			}
		}
		return unvisitedNeighbours;
	}

	public void setNeighbours(List<City> neighbours) {
		this.neighbours = neighbours;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
}
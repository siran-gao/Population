import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *	Population - <description goes here>
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	Siran Gao
 *	@since	January 10, 2023
 */
public class Population {
	
	// List of cities
	private List<City> cities;
	private int userSelection;
	
	// US data file
	private final String DATA_FILE = "usPopData2017.txt";

	public Population(){
		cities = new ArrayList<>();
		userSelection = 0;
	}

	public static void main(String[] args){
		Population population = new Population();
		population.runProgram();
	}

	public void runProgram(){
		printIntroduction();
		readTextFile();
		printTotalCities();
		while(userSelection != 9){
			printMenu();
			getUserSelection();

			String location = "";
			long time = 0;
			List<City> cityList = new ArrayList<City>();
			switch(userSelection){
				case 1: time = ascendingPopSelectionSort(); break;
				case 2: time = descendingPopMergeSort(); break;
				case 3: time = ascendingNameInsertionSort(); break;
				case 4: time = descendingNameMergeSort(); break;
				case 5:
					location = promptState();
					cityList = sortPopByState(location);
					break;
				case 6:
					location = promptCity();
					cityList = sortPopByCityName(location);
					break;
				case 9: printEndMessage(); break;
			}

			if(userSelection != 9){
				printResultHeading(location);
				printResults(cityList);
				if(userSelection <= 4)
					printElapsedTime(time);
			}
		}
	}
	
	/**	Prints the introduction to Population */
	public void printIntroduction() {
		System.out.println("   ___                  _       _   _");
		System.out.println("  / _ \\___  _ __  _   _| | __ _| |_(_) ___  _ __ ");
		System.out.println(" / /_)/ _ \\| '_ \\| | | | |/ _` | __| |/ _ \\| '_ \\ ");
		System.out.println("/ ___/ (_) | |_) | |_| | | (_| | |_| | (_) | | | |");
		System.out.println("\\/    \\___/| .__/ \\__,_|_|\\__,_|\\__|_|\\___/|_| |_|");
		System.out.println("           |_|");
		System.out.println();
	}
	
	/**	Print out the choices for population sorting */
	public void printMenu() {
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}

	public void readTextFile(){
		Scanner reader = FileUtils.openToRead(DATA_FILE);
		reader.useDelimiter("\t|\n");
		while(reader.hasNext()){
			//String line = reader.next();
			//System.out.println(line + "\n");

//			String state = line.substring(0, line.indexOf("\t"));
//			line = line.substring(line.indexOf("\t")).trim();
//			String name = line.substring(0, line.indexOf("\t"));
//			line = line.substring(line.indexOf("\t")).trim();
//			String designation = line.substring(0, line.indexOf("\t"));
//			line = line.substring(line.indexOf("\t")).trim();
//			int population = Integer.parseInt(line);

			String state = reader.next();
			String name = reader.next();
			String designation = reader.next();
			int population = Integer.parseInt(reader.next());

			cities.add(new City(state, name, designation, population));
		}
	}

	public void getUserSelection(){
		userSelection = Prompt.getInt("Enter selection");
		while( !((userSelection>=1 && userSelection<=6) || userSelection==9) ){
			userSelection = Prompt.getInt("Enter selection");
		}
		System.out.println();
	}

	public String promptState(){
		String state = Prompt.getString("Enter state name (ie Alabama)");
		while( !isValidState(state) ){
			System.out.println("ERROR: " + state + " is not valid");
			state = Prompt.getString("Enter state name (ie Alabama)");
		}
		return state;
	}

	public String promptCity(){
		String city = Prompt.getString("Enter city name");
		while( !isValidCity(city) ){
			System.out.println("ERROR: " + city + " is not valid");
			city = Prompt.getString("Enter city name");
		}
		return city;
	}

	public boolean isValidState(String state){
		ascendingNameInsertionSort();
		int low = 0, mid, high = cities.size()-1;
		while(low<high){
			mid = (low+high)/2;
			if(cities.get(mid).getState().equals(state))
				return true;
			else if(cities.get(mid).getState().compareTo(state) > 0)
				low = mid+1;
			else
				high = mid-1;
		}
		return false;

		//return false;
	}

	public boolean isValidCity(String city){
//		descendingNameMergeSort();
//		int low = 0, mid, high = cities.size()-1;
//		while(low<high){
//			mid = (low+high)/2;
//			if(cities.get(mid).getName().equals(city))
//				return true;
//			else if(cities.get(mid).getName().compareTo(city) > 0)
//				low = mid+1;
//			else
//				high = mid-1;
//		}
//		return false;

		return false;
	}

	public void printResultHeading(String location){
		switch(userSelection){
			case 1: System.out.println("Fifty least populous cities"); break;
			case 2: System.out.println("Fifty most populous cities"); break;
			case 3: System.out.println("Fifty cities sorted by name"); break;
			case 4: System.out.println("Fifty cities sorted by name descending"); break;
			case 5: System.out.println("Fifty most populous cities in " + location); break;
			case 6: System.out.println("City " + location + " by population"); break;
		}
		System.out.printf("%4s %-23s%-23s%-13s%13s\n", "", "State", "City", "Type", "Population");
	}

	public void printResults(List<City> cityList){
		if(userSelection<5){
			for(int i = 0; i<50; i++){
				System.out.printf("%4s %s\n", ((i+1)+":"), cities.get(i).toString());
			}
		}
		else if(userSelection==5){
			for(int i = 0; i<50; i++){
				System.out.printf("%4s %s\n", ((i+1)+":"), cityList.get(i).toString());
			}
		}
		else if(userSelection==6){
			for(int i = 0; i<cityList.size(); i++){
				System.out.printf("%4s %s\n", ((i+1)+":"), cityList.get(i).toString());
			}
		}
	}

	public void printElapsedTime(long time){
		System.out.println("Elapsed Time " + time + " milliseconds\n");
	}

	public void printEndMessage(){
		System.out.println("Thanks for using Population!");
	}

	/**
	 * selection sort population
	 */
	public long ascendingPopSelectionSort(){
		long startMillisec = System.currentTimeMillis();
		for(int outer = cities.size()-1; outer>0; outer--){
			int maxIndex = 0;
			for(int inner = 0; inner<=outer; inner++){
				if(cities.get(inner).compareTo(cities.get(maxIndex)) > 0){
					maxIndex = inner;
				}
			}
			swap(cities, maxIndex, outer);
		}
		long endMillisec = System.currentTimeMillis();
		return endMillisec-startMillisec;
	}

	// name order
	public long ascendingNameInsertionSort(){
		long startMillisec = System.currentTimeMillis();
		for(int outer = 1; outer<cities.size(); outer++){
			for(int inner = outer; inner>0; inner--){
				if(cities.get(inner).getName().compareTo(cities.get(inner-1).getName()) < 0)
					swap(cities, inner, inner-1);
			}
		}
		long endMillisec = System.currentTimeMillis();
		return endMillisec-startMillisec;
	}

	/**
	 *	Swaps two Integer objects in array arr
	 * 	@param cities 	the list of cities to swap
	 *	@param x		index of first object to swap
	 *	@param y		index of second object to swap
	 */
	private void swap(List<City> cities, int x, int y) {
		City temp = cities.get(x);
		cities.set(x, cities.get(y));
		cities.set(y, temp);
	}

	/**
	 *	merge sort in descending order by population
	 *  @return   			the sorted list
	 */
	public long descendingPopMergeSort() {
		long startMillisec = System.currentTimeMillis();
		int n = cities.size();
		List<City> sorted = new ArrayList<>();
		for(int i = 0; i<n; i++){
			sorted.add(new City("", "", "", 0));
		}
		recursivePopSplit(0, n-1, sorted);
		long endMillisec = System.currentTimeMillis();
		return endMillisec-startMillisec;
	}

	/**
	 * recursively split the array for merge sort
	 * @param begIndex	the beginning index of one side of the split
	 * @param endIndex	the end index of ond side of the split
	 * @param sorted	the sorted array
	 */
	private void recursivePopSplit(int begIndex, int endIndex, List<City> sorted){
		if(endIndex-begIndex < 2){
			if(endIndex > begIndex &&
					cities.get(endIndex).compareTo(cities.get(begIndex)) > 0)
				swap(cities, begIndex, endIndex);
		}
		else{
			int middle = (begIndex+endIndex)/2;
			recursivePopSplit(begIndex, middle, sorted);
			recursivePopSplit(middle+1, endIndex, sorted);
			mergePopElements(begIndex, middle, endIndex, sorted);
		}
	}

	/**
	 * merges the elements of the split array so that it becomes sorted
	 * @param start		the start index of the array
	 * @param middle	the middle index of the array
	 * @param end		the end index of the array
	 * @param sorted	the sorted array
	 */
	private void mergePopElements(int start, int middle, int end, List<City> sorted){
		int i = start;
		int j = middle+1;
		int k = start;

		while(i<=middle && j<=end){
			if(cities.get(i).compareTo(cities.get(j)) > 0){
				sorted.set(k, cities.get(i));
				i++;
			}
			else{
				sorted.set(k, cities.get(j));
				j++;
			}
			k++;
		}

		while(i <= middle){
			sorted.set(k, cities.get(i));
			k++;
			i++;
		}

		while(j <= end){
			sorted.set(k, cities.get(j));
			k++;
			j++;
		}

		for(k=start; k<=end; k++){
			cities.set(k, sorted.get(k));
		}
	}

	/**
	 *	merge sort in descending order by population
	 *  @return   			the sorted list
	 */
	public long descendingNameMergeSort() {
		long startMillisec = System.currentTimeMillis();
		int n = cities.size();
		List<City> sorted = new ArrayList<>();
		for(int i = 0; i<n; i++){
			sorted.add(new City("","","",0));
		}
		recursiveNameSplit(0, n-1, sorted);
		long endMillisec = System.currentTimeMillis();
		return endMillisec-startMillisec;
	}

	/**
	 * recursively split the array for merge sort
	 * @param begIndex	the beginning index of one side of the split
	 * @param endIndex	the end index of ond side of the split
	 * @param sorted	the sorted array
	 */
	private void recursiveNameSplit(int begIndex, int endIndex, List<City> sorted){
		if(endIndex-begIndex < 2){
			if(endIndex > begIndex &&
					cities.get(endIndex).getName().compareTo(cities.get(begIndex).getName()) > 0)
				swap(cities, begIndex, endIndex);
		}
		else{
			int middle = (begIndex+endIndex)/2;
			recursiveNameSplit(begIndex, middle, sorted);
			recursiveNameSplit(middle+1, endIndex, sorted);
			mergeNameElements(begIndex, middle, endIndex, sorted);
		}
	}

	/**
	 * merges the elements of the split array so that it becomes sorted
	 * @param start		the start index of the array
	 * @param middle	the middle index of the array
	 * @param end		the end index of the array
	 * @param sorted	the sorted array
	 */
	private void mergeNameElements(int start, int middle, int end, List<City> sorted){
		int i = start;
		int j = middle+1;
		int k = start;

		while(i<=middle && j<=end){
			if(cities.get(i).getName().compareTo(cities.get(j).getName()) > 0){
				sorted.set(k, cities.get(i));
				i++;
			}
			else{
				sorted.set(k, cities.get(j));
				j++;
			}
			k++;
		}

		while(i <= middle){
			sorted.set(k, cities.get(i));
			k++;
			i++;
		}

		while(j <= end){
			sorted.set(k, cities.get(j));
			k++;
			j++;
		}

		for(k=start; k<=end; k++){
			cities.set(k, sorted.get(k));
		}
	}

	// descending insertion
	public List<City> sortPopByState(String state){
		List<City> oneStateOnly = new ArrayList<City>();
		for(int i = 0; i<cities.size(); i++){
			if(cities.get(i).getState().equals(state))
				oneStateOnly.add(cities.get(i));
		}
		for(int outer = 1; outer<oneStateOnly.size(); outer++){
			for(int inner = outer; inner>0; inner--){
				if(oneStateOnly.get(inner).getName().compareTo(oneStateOnly.get(inner-1).getName()) > 0)
					swap(oneStateOnly, inner, inner-1);
			}
		}
		return oneStateOnly;
	}

	// insertion descending
	public List<City> sortPopByCityName(String cityName){
		List<City> oneCityOnly = new ArrayList<City>();
		for(int i = 0; i<cities.size(); i++){
			if(cities.get(i).getName().equals(cityName))
				oneCityOnly.add(cities.get(i));
		}
		for(int outer = 1; outer<oneCityOnly.size(); outer++){
			for(int inner = outer; inner>0; inner--){
				if(oneCityOnly.get(inner).compareTo(oneCityOnly.get(inner-1)) > 0)
					swap(oneCityOnly, inner, inner-1);

			}
		}
		return oneCityOnly;
	}

	/**
	 * prints the total number of cities in teh database
	 */
	public void printTotalCities(){
		System.out.println(cities.size() + " cities in database.");
	}
}

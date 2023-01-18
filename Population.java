import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *	Population - the user is prompted with different options to search/sort
 *  through a large database with the name, state, designation, and population
 *  of cities in the United States. Depending on which option the user 
 *  selects, the program will use either selection sort, insertion sort, or
 *  merge sort to sort through the database to get the information that the
 *  user wants
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	Siran Gao
 *	@since	January 10, 2023
 */
public class Population {
	
	// List of cities
	private List<City> cities;
	private int userSelection; // the user selection of what data/what sort they want
	
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

	/**
	 * called from main and runs the entire program
	 */
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
		System.out.println();
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}

	/**
	 * uses the FileUtils class to read in all the data from the text file
	 */
	public void readTextFile(){
		Scanner reader = FileUtils.openToRead(DATA_FILE);
		reader.useDelimiter("\t|\n");
		while(reader.hasNext()){
			String state = reader.next();
			String name = reader.next();
			String designation = reader.next();
			int population = Integer.parseInt(reader.next());

			cities.add(new City(state, name, designation, population));
		}
	}

	/**
	 * uses the Prompt class to get the user selection for what choice they want
	 */
	public void getUserSelection(){
		userSelection = Prompt.getInt("Enter selection");
		while( !((userSelection>=1 && userSelection<=6) || userSelection==9) ){
			userSelection = Prompt.getInt("Enter selection");
		}
		System.out.println();
	}

	/**
	 * the user is prompted to enter the name of a state to search until
	 * a valid state is entered
	 * 
	 * @return 		the name of the state
	 */
	public String promptState(){
		String state = Prompt.getString("Enter state name (ie Alabama)");
		while( !isValidState(state) ){
			System.out.println("ERROR: " + state + " is not valid");
			state = Prompt.getString("Enter state name (ie Alabama)");
		}
		return state;
	}

	/**
	 * the user is prompted to enter the name of a city to search until
	 * a valid city is entered
	 * 
	 * @return 		the name of the city
	 */
	public String promptCity(){
		String city = Prompt.getString("Enter city name");
		while( !isValidCity(city) ){
			System.out.println("ERROR: " + city + " is not valid");
			city = Prompt.getString("Enter city name");
		}
		return city;
	}

	/**
	 * checks to see if the user input is of a valid state that can be 
	 * found in the database
	 * 
	 * @param state 	the state name to check
	 * @return 			true if valid name, false if not
	 */
	public boolean isValidState(String state){
		sortByStateName();
		
		int low = 0, mid, high = cities.size()-1;
		while(low<high){
			mid = (low+high)/2;
			
			if(cities.get(mid).getState().equals(state))
				return true;
			else if(state.compareTo(cities.get(mid).getState()) > 0)
				low = mid+1;
			else
				high = mid-1;
		}
		return false;
	}

	/**
	 * checks to see if the user input is of a valid city that can be 
	 * found in the database
	 * 
	 * @param city 		the city name to check
	 * @return 			true if valid name, false if not
	 */
	public boolean isValidCity(String city){
		descendingNameMergeSort();
		
		int low = 0, mid, high = cities.size()-1;
		while(low<high){
			mid = (low+high)/2;
			
			if(cities.get(mid).getName().equals(city))
				return true;
			else if(cities.get(mid).getName().compareTo(city) > 0)
				low = mid+1;
			else
				high = mid-1;
		}
		return false;
	}
	
	/**
	 * prints the option the user selects along with a heading for the data
	 * 
	 * @param location 		if the selection the user chose requires them to 
	 * 						input an location
	 */
	public void printResultHeading(String location){
		switch(userSelection){
			case 1: System.out.println("Fifty least populous cities"); break;
			case 2: System.out.println("Fifty most populous cities"); break;
			case 3: System.out.println("Fifty cities sorted by name"); break;
			case 4: System.out.println("Fifty cities sorted by name descending"); break;
			case 5: System.out.println("\nFifty most populous cities in " + location); break;
			case 6: System.out.println("\nCity " + location + " by population"); break;
		}
		System.out.printf("%4s %-23s%-23s%-13s%13s\n", "", "State", "City", "Type", "Population");
	}

	/**
	 * prints the result of the sorted data
	 * 
	 * @param cityList 		if the sorted data is in a separate list and 
	 * 						not stored as a field
	 */
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
	
	/**
	 * prints the time in milliseconds it takes to complete a sort
	 * 
	 * @param time 		the time in milliseconds
	 */
	public void printElapsedTime(long time){
		System.out.println("\nElapsed Time " + time + " milliseconds\n");
	}

	/**
	 * prints the end message at the end of the program
	 */
	public void printEndMessage(){
		System.out.println("Thanks for using Population!\n\n\n");
	}

	/**
	 * uses selection sort to sort the list of cities by population in 
	 * ascending order
	 * 
	 * @return 	the time it takes to complete the sort in milliseconds
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

	/**
	 * uses insertion sort to sort the list of cities by name in 
	 * ascending order
	 * 
	 * @return the time it takes to complete the sort in milliseconds
	 */
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
	 * uses insertion sort to sort the list of cities by their state name
	 * in ascending order
	 */
	public void sortByStateName(){
		for(int outer = 1; outer<cities.size(); outer++){
			for(int inner = outer; inner>0; inner--){
				if(cities.get(inner).getState().compareTo(cities.get(inner-1).getState()) < 0)
					swap(cities, inner, inner-1);
			}
		}
	}

	/**
	 *	Swaps two City objects in list cities
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
	 *	uses merge sort in descending order to sort the list of cities
	 *  by population
	 * 
	 *  @return    the time it takes to complete the sort in milliseconds
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
	 * recursively split the arraylist for merge sort
	 * 
	 * @param begIndex	the beginning index of one side of the split
	 * @param endIndex	the end index of ond side of the split
	 * @param sorted	the sorted list
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
	 * merges the elements of the split arraylist so that it becomes sorted
	 * @param start		the start index of the arraylist
	 * @param middle	the middle index of the arraylist
	 * @param end		the end index of the arraylist
	 * @param sorted	the sorted arraylist
	 */
	private void mergePopElements(int start, int middle, int end, List<City> sorted){
		int i = start;
		int j = middle+1;
		int n = start;

		while(i<=middle && j<=end){
			if(cities.get(i).compareTo(cities.get(j)) > 0){
				sorted.set(n, cities.get(i));
				i++;
			}
			else{
				sorted.set(n, cities.get(j));
				j++;
			}
			n++;
		}

		while(i <= middle){
			sorted.set(n, cities.get(i));
			n++;
			i++;
		}

		while(j <= end){
			sorted.set(n, cities.get(j));
			n++;
			j++;
		}

		for(int m = start; m<=end; m++){
			cities.set(m, sorted.get(m));
		}
	}

	/**
	 *	uses merge sort to sor the list of cities in descending order 
	 *  by population
	 * 
	 *  @return 	the time in milliseconds it takes to sort the list
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
	 * recursively split the arraylist for merge sort
	 * @param begIndex	the beginning index of one side of the split
	 * @param endIndex	the end index of ond side of the split
	 * @param sorted	the sorted list
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
	 * merges the elements of the split arraylist so that it becomes sorted
	 * @param start		the start index of the arraylist
	 * @param middle	the middle index of the arraylist
	 * @param end		the end index of the arraylist
	 * @param sorted	the sorted arraylist
	 */
	private void mergeNameElements(int start, int middle, int end, List<City> sorted){
		int i = start;
		int j = middle+1;
		int n = start;

		while(i<=middle && j<=end){
			if(cities.get(i).getName().compareTo(cities.get(j).getName()) > 0){
				sorted.set(n, cities.get(i));
				i++;
			}
			else{
				sorted.set(n, cities.get(j));
				j++;
			}
			n++;
		}

		while(i <= middle){
			sorted.set(n, cities.get(i));
			n++;
			i++;
		}

		while(j <= end){
			sorted.set(n, cities.get(j));
			n++;
			j++;
		}

		for(int m = start; m<=end; m++){
			cities.set(m, sorted.get(m));
		}
	}

	/**
	 * uses insertion sort to sort the population of cities of a specified state
	 * 
	 * @param state 	the state to sort the population
	 * @return 			the list of sorted cities by population for one state
	 */
	public List<City> sortPopByState(String state){
		List<City> oneStateOnly = new ArrayList<City>();
		for(int i = 0; i<cities.size(); i++){
			if(cities.get(i).getState().equals(state))
				oneStateOnly.add(cities.get(i));
		}
		for(int outer = 1; outer<oneStateOnly.size(); outer++){
			for(int inner = outer; inner>0; inner--){
				if(oneStateOnly.get(inner).compareTo(oneStateOnly.get(inner-1)) > 0)
					swap(oneStateOnly, inner, inner-1);
			}
		}
		return oneStateOnly;
	}

	/**
	 * uses insertion sort to sort the population of cities with the same
	 * specified name
	 */
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
	 * prints the total number of cities in the database
	 */
	public void printTotalCities(){
		System.out.println(cities.size() + " cities in database.");
	}
}

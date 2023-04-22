package com.company.cars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Scanner;

/**
* This class is the main entry point for the application
* It has a variety of processes/methods that incorporates mechanisms
* of a real life Car Shop. Such as being able to display current 
* car list, calculating the number of stock and total stock price.
* It is also able to give extra details such as current car conditions
* and will enable to let the user add vehicles to the car shop.
* 
* @author      Alexis Orias
* @version     1.0
* @date        March 2023
* @apiNote     Pre work for COM00141M
*/
public class CarShop {
	
	// To store current car list and current grade
	private static ArrayList<CarGrade> carGradeList = new ArrayList<>();
	private static ArrayList<Car> carList = new ArrayList<>();

	// To enable user interaction and functionality
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// add in the static data
		addSampleCarData();
		addSampleCarGrade();
		
		// entry point
		mainMenu();
	}

	/**
	 * This is the main menu where the user can interact with the application
	 * there are a variety of options to exercise and
	 * will exit the console when instructed
	 */
	static void mainMenu() {
		int choice = 0;
		do {
			
			System.out.println();
			System.out.println();
			System.out.println("*** CAR SHOP ***");
			System.out.println("*** Main Menu ***");
			System.out.println("Please enter your choice below");
			
			System.out.println("[1] Add Car");
			System.out.println("[2] Check Car from ID");
			System.out.println("[3] Check Car with lowest Price");
			System.out.println("[4] Check Car with lowest Mileage");
			System.out.println("[5] Display Current Cars");
			System.out.println("[6] Display Car Grades");
			System.out.println("[7] Display Car Stock");
			System.out.println("[8] Display Cars by Mileage");
			System.out.println("[9] Display Cars by Price");
			System.out.println("[10] Display Car by Model");
			System.out.println("[11] EXIT");

			
			System.out.println("Enter choice below:");
			int choiceNum = sc.nextInt();

			System.out.println();
			
			// choices below
			switch (choiceNum) {
			case 1: takeUserInput(); 
			        choice = choiceNum; 
			        break;
			case 2: enterCar(); 
	                choice = choiceNum; 
	                break;
			case 3: System.out.println(getLowestPrice(carList));
	                choice = choiceNum; 
	                break;
	        case 4: System.out.println(getLowestMileage(carList));
	                choice = choiceNum; 
	                break;
			case 5: System.out.println("********************");
	                System.out.println("CURRENT CARS");
	                displayCarList();
			        choice = choiceNum; 
			        break;
			case 6: displayCarGradeList();
	                choice = choiceNum; 
	                break;
			case 7: displayCarStockListHeader();
			        processStock(carList); 
			        choice = choiceNum; 
			        break;
			case 8: System.out.println("********************");
			        System.out.println("SORTED CARS BY MILEAGE (ASC)");
			        sortByMileage(carList);
			        displayCarList();
			        sortByID(carList);
			        choice = choiceNum; 
			        break;
			case 9: System.out.println("********************");
	                System.out.println("SORTED CARS BY PRICE (ASC)");
	                sortByPrice(carList);
	    	        displayCarList();
			        sortByID(carList);
			        choice = choiceNum; 
			        break;
			case 10:System.out.println("********************");
                    System.out.println("SORTED CARS BY MODEL");
	                sortByModel(carList);
	    	        displayCarList();
			        sortByID(carList);
	                choice = choiceNum; 
	                 break;
	                 
	                 // EXIT
			case 11: choice = choiceNum; 
			        System.out.println("Thank you for your time! Have a good day :)");
			        break;
			        
			        // If the choice is invalid
			default: System.out.println("Sorry, didn't get that - try again!");
			}

	   } while (choice != 11);
	}
	
	/**
	 * This method will be used to add a new car via 
	 * the user input using the scanner class
	 * @param none
	 * @return void
	 * @throws InputMismatchException for any invalid input
	 */
	static void takeUserInput() {
		
		try {

			// Gets the next available id
			int i = getNextAvailableID(carList);

			// Gets user input with corresponding car attributes
			Scanner sc = new Scanner(System.in);
			System.out.println("- - - New Car Details - - - ");
			System.out.println("Please enter Car Manufacturer (String):");
			String manufacturer = sc.nextLine();
			System.out.println("Please enter Car Model(String):");
			String model = sc.nextLine();
			System.out.println("Please enter Car Year(Number eg, 2023):");
			int year = sc.nextInt();
			System.out.println("Please enter Car Mileage(Number):");
			int mileage = sc.nextInt();
			System.out.println("Please enter Car Engine Size(Number eg, 1.8L):");
			double engineSize = sc.nextDouble();
			System.out.println("Please enter Car Grade(A,B,C or D):");
			char grade = sc.next().charAt(0);
			System.out.println("Please enter Car Price:(Number)");
			double price = sc.nextDouble();

			// Ensures that the manufacturer and model are not null
			if (manufacturer != null && model != null) {

				// Add the new car to the list
				Car newCar = new Car(i, manufacturer, model, year, mileage, engineSize, grade, price);
				carList.add(newCar);

				System.out.println(newCar.toString() + " has been added");
				System.out.println();
			}
		// throws a mismatch exception if some of the attributes do not match type	
		} catch (InputMismatchException e) {
			System.out.println("Sorry the inputs you have given are not valid!");
		}

	}

	/**
	 * This method will calculate the current stock list using the car list, 
	 * incrementing the total number of vehicles and the total price of the stock
	 * @param arrayList
	 * @return void
	 */
	static void processStock(ArrayList<Car> givenCarList) {
		
		//initialise id variable and hashmap
		int id = 0;
		HashMap<String, CarStock> currentStock = new HashMap<>();
		
		//loop through current car stock list
		for (Car car : givenCarList) {
			// get the car manufacturer
			String m = car.getCarManufacturer();
			// if the manufacturer is in the current map
			if (currentStock.containsKey(m)) {
				//loop through the current map
				for (Entry<String, CarStock> s : currentStock.entrySet()) {
					// find the matching manufacturer within the keys
					if (m.equalsIgnoreCase(s.getKey())) {
						// get the carStock object
						CarStock c = s.getValue();
						//gets current object attribute values
						int currentNum = c.getTotalNumOfVehicles();
						double currentTotal = c.getStockPrice();
						// add new values to the object, increment number of vehicles and add to total price
						c.setTotalNumOfVehicles(currentNum + 1);
						c.setStockPrice(car.getCarPrice() + currentTotal);
					}
				}
				
			/*
			 * If not found with current map, create a new object
			 * add in first values, num of vehicle = 1 and price = car.price
			 * add to the hashmap
			 * increment id by 1
			 */
			} else {
				CarStock newCarStock = new CarStock(id + 1,m,1,car.getCarPrice());
				currentStock.put(m, newCarStock);
				id++;
			}
		}
		
		//display the current stock within the map
		for (Entry<String, CarStock> s : currentStock.entrySet()) {
			CarStock carStock = s.getValue();
			System.out.println(carStock.getId()+ " | " + carStock.getManufacturer() 
			+ " - " + carStock.getTotalNumOfVehicles() + " - £" + carStock.getStockPrice() );
		}
	}

	/***
	 * This method is to join the getCar method with added user input
	 * @param none
	 * @return void
	 */
	static void enterCar() {
		// prompt user to enter id
		System.out.println("**************");
		System.out.println("Please enter the car ID you'd like to see");
		int id = sc.nextInt();

		System.out.println("********************");
		System.out.println("CAR ID " + id + "...");
		
		//output the getCar method which should grab the corresponding Car object
		System.out.println(getCar(id));
	}

	/**
	 * This method matches the id given to a car id in the current car list
	 * @param int car id given by user
	 * @return String the details of the car from the given id
	 */
	static String getCar(int id) {
		
		// extract the current id's from the carsList
		ArrayList<Integer> currentIDs = new ArrayList<>();
		for(int i = 0; i < carList.size(); i++) {
			int carId = carList.get(i).getCarId();
			currentIDs.add(carId);
		}
		
		// convert the id to Integer
		Integer newID = id;
		
		// check if the id exists
		if(currentIDs.contains(newID)) {
			// initialise output string;
			String output = "";

			// loop through current carList
			for (Car car : carList) {
				// if the car id and the given id match
				if (car.getCarId() == id) {
					// call the toString methods of the car object and add to output
					output += car.toAlternateString();

					// iterate through the car grade list
					for (CarGrade carGrade : carGradeList) {
						// get the car grade character
						char g = carGrade.getGrade();

						/**
						 * if the car grade list matches the car grade character add the car grade
						 * condition to the output
						 */
						if (car.getCarGrade() == g) {
							output += carGrade.getCondition();
						}
					}
					// add in the car price
					output += " £" + car.getCarPrice();
				}
			}
			// return the car object
			return output;
		}
		// id does not exist within the carsList
		return "Sorry cannot find that Car ID";
	}

	/**
	 * This method will check the length of the current list and 
	 * return the next available int for an ID
	 * @param ArrayList list of current cars
	 * @return int next usable car id
	 */
	static int getNextAvailableID(ArrayList<Car> givenCarList) {
		//checks the array size and increments
		int i = givenCarList.size() + 1;
		return i; 
	}

	/**
	 * This method will sort the current list by price by ascending order
	 * @param ArrayList list of current cars
	 * @return void
	 */
	static void sortByPrice(ArrayList<Car> givenCarList) {
		
		//Ensures the list is not empty
		if (givenCarList.size() != 0 || !(givenCarList.isEmpty())) {
			// Initialises a temporary arraylist
			ArrayList<Car> tmpCarList = new ArrayList<>();
			
			//get car list length
			int n = givenCarList.size();
			
			//Initialises a double array for the price
			double[] price = new double[n];
			
			//Loops through the given arrayList and adds the prices to the array
			for (int i = 0; i < n; i++) {
				price[i] = givenCarList.get(i).getCarPrice();
			}

			// using the Insertion Sort algorithm to sort the price
			
			//For loop through the id array
			for (int j = 1; j < n; j++) {
				//takes the price within id index j array
				double key = price[j];
				//sets the other value to j -1
				int i = j - 1;
				// while i greater than 0 and key (car price) is less than looping index
				while ((i > -1) && (price[i] > key)) {
					//add the value in next to index i
					price[i + 1] = price[i];
					// value has been placed
					i--;
				}
				//add key value in next to index i
				price[i + 1] = key;
			}

		/*
		 * From here we have a sorted car price list and we 
		 *  can loop through this rearrange car objects into a new arrayList
		 */
			for (int i = 0; i < n; i++) {
				//take the price id from index i
				double key = price[i];
				//loop through the current carList
				for (Car c : carList) {
					/*
					 * if car list matches the id within the sorted list
					 * we add the object car to new list
					 */
					if (c.getCarPrice() == key) {
						tmpCarList.add(c);
					}
				}
			}
			
			//Copy new sorted list to the original car list
			Collections.copy(carList, tmpCarList);
			System.out.println();

		} else {
			// If the given array list is empty, then exit out
			System.out.println("List is empty! Nothing to calculate :( ");
		}

	}
	
	/**
	 * This method will output the current car list alphabetically by model
	 * @param list
	 * @return void
	 */
	static void sortByModel(ArrayList<Car> givenCarList) {
		
		//Ensures the list is not empty
		if (givenCarList.size() != 0 || !(givenCarList.isEmpty())) {
			/* 
			 * LinkedHashSet - do not allow duplicates so any duplicated will be ignored
			 * ArrayList - empty to copy sorted list
			 */
			LinkedHashSet<Car> uniqueCarList = new LinkedHashSet<Car>();
			ArrayList<Car> tmpCarList = new ArrayList<>();

			// get size of the input list
			int listSize = givenCarList.size();

			//create a list for the models within the given car list
			String[] models = new String[listSize];

			// add the car models into the models array
			for(int i=0; i < listSize; i++) {
				models[i] = givenCarList.get(i).getCarModel();
			}

			// use insertion sort algorithm to sort models
			for (int j = 1; j < listSize; j++) {
				//get model from index j
				String key = models[j];
				int i = j - 1;
				/*
				 * while i greater than 0 and model index j is lexicographically greater 
				 * than model index i
				 */
				while ((i > -1) && (models[i].compareTo(key) > 0)) {
					//set model at index i to index i+1
					models[i + 1] = models[i];
					i--;
				}
				//set the model at index j and index i+1
				models[i + 1] = key;
			}

			/*
			 * From here we have a  model list in alphabetical order and we 
			 *  can loop through this rearrange car objects into a new arrayList
			 */
			for (int i = 0; i < listSize; i++) {
				//takes the model value at index i
				String key = models[i];
				// iterates through the current car objects within given array list
				for (Car c : carList) {
					// if the model at index i matches the car value 
					if (c.getCarModel() == key) {
						// add car to tmp car list
						uniqueCarList.add(c);
					} 
				} 
			}

			/*
			 * Add all the sorted cars in the Linked Hash Set into tmp ArrayList
			 * Override elements of current car list with tmp Array List
			 */
			tmpCarList.addAll(uniqueCarList);
			Collections.copy(carList, tmpCarList);

		} else {
			// If the given array list is empty, then exit out
			System.out.println("List is empty! Nothing to calculate :( ");
		}

	}

	/**
	 * This method will sort the current list of cars by mileage ascending order
	 * @param ArrayList
	 * @return void
	 */
	static void sortByMileage(ArrayList<Car> givenCarList) {

		//Ensures the list is not empty
		if (givenCarList.size() != 0 || !(givenCarList.isEmpty())) {
			
			// create a temporary arraylist
			ArrayList<Car> tmpCarList = new ArrayList<>();
			
			// get size of the input list
			int listSize = givenCarList.size();
			
			// initialise mileage array to store different mileages
			int[] mileage = new int[listSize];
			
			// add car mileage values into mileage array
			for (int i = 0; i < listSize; i++) {
				mileage[i] = givenCarList.get(i).getCarMileage();
			}
			
			//use insertion sort method
			for (int j = 1; j < listSize; j++) {
				//get car mileage at index j
				int key = mileage[j];
				int i = j - 1;
				// while i greater than 0 and key (car mileage) is less than the value at looping index
				while ((i > -1) && (mileage[i] > key)) {
					//set the car mileage value at index i into index i+1
					mileage[i + 1] = mileage[i];
					i--;
				}
				//set the key car mileage value into index i+1
				mileage[i + 1] = key;
			}

			/*
			 * From here we have a sorted car mileage list and we 
			 *  can loop through this rearrange car objects into a new arrayList
			 */
			for (int i = 0; i < listSize; i++) {
				// takes the car mileage value
				double key = mileage[i];
				// loops through the given arrayList 
				for (Car c : carList) {
					// if current car matches the car mileage value from mileage array
					if (c.getCarMileage() == key) {
						// add car to tmp array list
						tmpCarList.add(c);
					}
				}
			}

			//Copy new sorted list to the original car list
			Collections.copy(carList, tmpCarList);
			System.out.println();
		} else {
			// If the given array list is empty, then exit out
			System.out.println("List is empty! Nothing to calculate :( ");
		}

	}

	/**
	 * This method will sort the current list by id in ascending order
	 * @param ArrayList
	 * @return void
	 */
	static void sortByID(ArrayList<Car> givenCarList) {
		
		//Ensures the list is not empty
		if (givenCarList.size() != 0 || !(givenCarList.isEmpty())) {
			
			// create a temporary arraylist
			ArrayList<Car> tmpCarList = new ArrayList<>();
			
			// get size of the input car list
			int listSize = givenCarList.size();
			
			//initialise id array 
			double[] id = new double[listSize];
			
			//add all the ids of the car list and store in the id array
			for (int i = 0; i < listSize; i++) {
				id[i] = givenCarList.get(i).getCarId();
			}

			// using the Insertion Sort algorithm to sort
			int n = id.length;
			for (int j = 1; j < n; j++) {
				//get car value at index j from id array
				double key = id[j];
				int i = j - 1;
				// while i greater than 0 and key (car id) is less than looping index
				while ((i > -1) && (id[i] > key)) {
					//set the car id value of index i into index i+1
					id[i + 1] = id[i];
					i--;
				}
				//set the key car id value into index i+1
				id[i + 1] = key;
			}

			/*
			 * From here we have a sorted car id list and we 
			 *  can loop through this rearrange car objects into a new arrayList
			 */
			for (int i = 0; i < listSize; i++) {
				//takes the car id value at i
				double key = id[i];
				// iterates through the current car objects within given array list
				for (Car c : carList) {
					// if the value of the car object and the value within the id array match
					if (c.getCarId() == key) {
						// add car to tmp car list
						tmpCarList.add(c);
					}
				}
			}

			//Copy new sorted list to the original car list
			Collections.copy(carList, tmpCarList);
			System.out.println();

		} else {
			// If the given array list is empty, then exit out
			System.out.println("List is empty! Nothing to calculate :( ");
		}

	}

	/**
	 * This method will sort the list by mileage asc then return the car with the
	 * lowest mileage
	 * @param ArrayList current car
	 * @return String car object with lowest mileage
	 */
	static String getLowestMileage(ArrayList<Car> givenCarList) {
		
		//call method to sort by mileage
		sortByMileage(givenCarList);
		
		// it will be sorted asc order so the value at index 0 will be the lowest
		Car c = carList.get(0);
		
		//display the car with the lowest mileage
		displayTitleHeader();
		return c.toString() + " - LOWEST MILEAGE";
	}

	/**
	 * This method will sort the list by price asc then return the car with the
	 * lowest mileage
	 * @param ArrayList
	 * @return Car with lowest price
	 */
	static String getLowestPrice(ArrayList<Car> givenCarList) {
		//call method to sort by price
		sortByPrice(givenCarList);
		
		// it will be sorted asc order so the value at index 0 will be the lowest
		Car c = carList.get(0);
		
		//display the car with the lowest price
		displayTitleHeader();
		return c.toString() + " - LOWEST PRICE";
	}

	/**
	 * This method will add the pre-work static data into the corresponding collection
	 * @param none
	 * @return void
	 * @return Car adds car objects into the carsList Array List
	 */
	static void addSampleCarData() {
		
		// declare and initialise the given static data
		Car firstCar = new Car(1, "Honda", "Fit", 2013, 200500, 1.3, 'A', 5550.50);
		Car secondCar = new Car(2, "Toyota", "Prius", 2012, 88000, 1.8, 'A', 8450.50);
		Car thirdCar = new Car(3, "Volkswagen", "Golf", 2016, 74550, 1.5, 'B', 12500.00);
		Car fourthCar = new Car(4, "Toyota", "Yaris", 2011, 110100, 1.0, 'A', 6500.50);
		Car fifthCar = new Car(5, "Toyota", "Prius", 2015, 52300, 1.8, 'C', 9999.95);
		Car sixthCar = new Car(6, "Volkswagen", "Polo", 2012, 140820, 1.2, 'B', 3050.50);

		// add the objects into the list
		carList.add(firstCar);
		carList.add(secondCar);
		carList.add(thirdCar);
		carList.add(fourthCar);
		carList.add(fifthCar);
		carList.add(sixthCar);

	}
	
	/**
	 * This method will add the pre-work static data into the corresponding collection
	 * @param none
	 * @return void
	 * @return CarGrade adds carGrade objects into the carsGradeList Array List
	 */
	static void addSampleCarGrade() {
		
		// declare and initialise the given static data
		CarGrade firstCarGrade = new CarGrade(1, 'A', "Excellent", "Very slightly used, virtually as good as new.");
		CarGrade secondCarGrade = new CarGrade(2, 'B', "Good", "Good condition but with visible flaws.");
		CarGrade thirdCarGrade = new CarGrade(3, 'C', "Average", "Average condition, with minor damage.");
		CarGrade fourthCarGrade = new CarGrade(4, 'D', "Poor","Poor condition with significant damage, but the car is functional.");
		
		// add the objects into the list
		carGradeList.add(firstCarGrade);
		carGradeList.add(secondCarGrade);
		carGradeList.add(thirdCarGrade);
		carGradeList.add(fourthCarGrade);

	}

	/**
	 * This method is used to display the car objects in the carsList array list
	 * @param none
	 * @return void
	 */
	static void displayCarList() {
		displayTitleHeader();
		
		//loop and display through the current car objects
		for (Car car : carList) {
			System.out.print(car.getCarId() + "  | ");
			System.out.print(car.getCarManufacturer() + " - ");
			System.out.print(car.getCarModel() + " - ");
			System.out.print(car.getCarYear() + " - ");
			System.out.print(car.getCarEngineSize() + "L - ");
			System.out.print(car.getCarGrade() + " - ");
			System.out.print(car.getCarMileage() + " - ");
			System.out.print("£" + car.getCarPrice() + " - ");
			System.out.println();
		}

	}
	
	/**
	 * This method is used to display the titles for the columns for carsList array list
	 * @param none
	 * @return void
	 */
	static void displayTitleHeader() {
		
		//display headers for the car list
		System.out.print("ID | ");
		System.out.print("Manufacturer - ");
		System.out.print("Model - ");
		System.out.print("Year - ");
		System.out.print("Engine Size - ");
		System.out.print("Grade - ");
		System.out.print("Mileage - ");
		System.out.print("Price - ");
		System.out.println();
	}
	

	/**
	 * This method is used to display the titles for the columns for carsStockList array list
	 * @param none
	 * @return void
	 */
	static void displayCarStockListHeader() {
		
		// display header for car stock
		System.out.println("*** Current Car Stock List ***");
		System.out.print("ID | ");
		System.out.print("Manufacturer - ");
		System.out.print("Num of Stock - ");
		System.out.print("Stock Price - ");
		System.out.println();
	}

	static void displayCarGradeList() {
		displayCarGradeListHeader();
		System.out.println();
		
		// loop and display the current car grade objects
		for (CarGrade c : carGradeList) {
			System.out.print(c.getId() + "  | ");
			System.out.print(c.getGrade() + " - ");
			System.out.print(c.getCondition() + " - ");
			System.out.print(c.getDescription() + " - ");
			System.out.println();
		}
	}
	
	/**
	 * This method is used to display the titles for the columns for carsGradeList array list
	 * @param none
	 * @return void
	 */
	static void displayCarGradeListHeader() {
		
		// display car grade headers
		System.out.println("*** CAR GRADE LIST ***");
		System.out.print("ID | ");
		System.out.print("Grade - ");
		System.out.print("Condition - ");
		System.out.print("Description - ");
	}

}

/**
 *  This class represents a Car object which has a variety of 
 *  attributes which could be associate to how it would be stored at 
 *  a car shop. It has two methods which return display a string of different lengths and
 *  shared attributes.
 * @author alexisorias
 *
 */

class Car {
	private int carId;
	private String carManufacturer;
	private String carModel;
	private int carYear;
	private int carMileage;
	private double carEngineSize;
	private char carGrade;
	private double carPrice;

	/**
	 * Car Class constructor
	 * @param carId
	 * @param carManufacturer
	 * @param carModel
	 * @param carYear
	 * @param carMileage
	 * @param carEngineSize
	 * @param carGrade
	 * @param carPrice
	 */
	public Car(int carId, String carManufacturer, String carModel, int carYear, int carMileage, double carEngineSize,
			char carGrade, double carPrice) {
		super();
		this.carId = carId;
		this.carManufacturer = carManufacturer;
		this.carModel = carModel;
		this.carYear = carYear;
		this.carMileage = carMileage;
		this.carEngineSize = carEngineSize;
		this.carGrade = carGrade;
		this.carPrice = carPrice;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getCarManufacturer() {
		return carManufacturer;
	}

	public void setCarManufacturer(String carManufacturer) {
		this.carManufacturer = carManufacturer;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public int getCarYear() {
		return carYear;
	}

	public void setCarYear(int carYear) {
		this.carYear = carYear;
	}

	public int getCarMileage() {
		return carMileage;
	}

	public void setCarMileage(int carMileage) {
		this.carMileage = carMileage;
	}

	public double getCarEngineSize() {
		return carEngineSize;
	}

	public void setCarEngineSize(double carEngineSize) {
		this.carEngineSize = carEngineSize;
	}

	public char getCarGrade() {
		return carGrade;
	}

	public void setCarGrade(char carGrade) {
		this.carGrade = carGrade;
	}

	public double getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(double carPrice) {
		this.carPrice = carPrice;
	}
	
	/**
	 * Details the car attributes 
	 * @return String prints all attributes of car class
	 */
	@Override
	public String toString() {
		return carId + " " + carManufacturer + " " + carModel + " " + carYear + " " + carEngineSize
				+ "L " +  carGrade + " " + carMileage + " £" + carPrice;
	}
	
	/**
	 * Similar to the one above but with two less attributes
	 * @return String prints some attributes of the car class
	 */
	public String toAlternateString() {
		return carId + " " + carManufacturer + " " + carModel + " " + carYear + " " + carMileage + " " + carEngineSize
				+ "L ";
	}
}

/**
 * This class will be used to display and store the different car
 * grade options. This details the needed description, conditions and
 * id for the corresponding car grade. 
 * @author alexisorias
 */
class CarGrade {
	private int id;
	private char grade;
	private String condition;
	private String description;
	
	/**
	 * Car Grade Class Constructpr
	 * @param id
	 * @param grade
	 * @param condition
	 * @param description
	 */
	public CarGrade(int id, char grade, String condition, String description) {
		super();
		this.id = id;
		this.grade = grade;
		this.condition = condition;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getGrade() {
		return grade;
	}

	public void setGrade(char grade) {
		this.grade = grade;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

/**
 * This class is to store and display the current stock within
 * a car shop. Each instance should keep a unique count of the manufacturer,
 * total number of vehicles overall and total price of the stock
 * @author alexisorias
 */
class CarStock {
	private int id;
	private String manufacturer;
	private int totalNumOfVehicles;
	private double stockPrice;
	
	/**
	 * Car Stock Constructor
	 * @param id
	 * @param manufacturer
	 * @param totalNumOfVehicles
	 * @param stockPrice
	 */
	public CarStock(int id, String manufacturer, int totalNumOfVehicles, double stockPrice) {
		this.id = id;
		this.manufacturer = manufacturer;
		this.totalNumOfVehicles = totalNumOfVehicles;
		this.stockPrice = stockPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getTotalNumOfVehicles() {
		return totalNumOfVehicles;
	}

	public void setTotalNumOfVehicles(int totalNumOfVehicles) {
		this.totalNumOfVehicles = totalNumOfVehicles;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	/**
	 * This will display all the attributes of a Car Stock Object
	 * @return String prints all attributes
	 */
	@Override
	public String toString() {
		return id + " " + manufacturer + " " + totalNumOfVehicles + " " + stockPrice;
	}

}
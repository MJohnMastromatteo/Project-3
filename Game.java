import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game {

	//
	// Public
	//

	// Globals
	private static Scanner inputReader = new Scanner(System.in);
	public static final boolean DEBUGGING = false; // Debugging flag.
	public static final int MAX_LOCALES = 18; // Total number of rooms/locations
												// we have in the game.
	public static int currentLocale = 0; // Player starts in locale 0.
	public static String command; // What the player types as he or she plays
									// the game.
	public static boolean stillPlaying = true; // Controls the game loop.
	public static Locale[] locations; // An uninitialized array of type Locale.
										// See init() for initialization.
	public static int[][] nav; // An uninitialized array of type int int.
	public static int moves = 0; // Counter of the player's moves.
	public static int score = 0; // Tracker of the player's score.
	public static int hasVisited[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // Sets when a player has visited a new place
	public static double achievementRatio = 0.0; // Ratio for moves/score.
	public static String[] playerInventory = { "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", "Nothing", }; // Keeps
																																																																																							// track
																																																																																							// of
																																																																																							// which
																																																																																							// items
																																																																																							// a
																																																																																							// player
																																																																																							// has
	public static boolean hasMap = false; // Checks if the player has the map
	public static boolean inShop = false; // Checks if the player is in the shop
	public static int playerMoney = 0;
	private static Queue myQueue = new Queue();
	private static Stack myStack = new Stack();
	// Declare Locations
	public static Locale loc0 = new Locale(0);
	public static Locale loc1 = new Locale(1);
	public static Locale loc2 = new Locale(2);
	public static Locale loc3 = new Locale(3);
	public static Locale loc4 = new Locale(4);
	public static Locale loc5 = new Locale(5);
	public static Locale loc6 = new Locale(6);
	public static Locale loc7 = new Locale(7);
	public static Locale loc8 = new Locale(8);
	public static Locale loc9 = new Locale(9);
	public static Locale loc10 = new Locale(10);
	public static Locale loc11 = new Locale(11);
	public static Locale loc12 = new Locale(12);
	public static Locale loc13 = new Locale(13);
	public static Locale loc14 = new Locale(14);
	public static Locale loc15 = new Locale(15);
	public static Locale loc16 = new Locale(16);
	public static Locale loc17 = new Locale(17);

	public static String playerName;
	public static double amountCost;
	public static int playerMaxInventory = 0;
	public static int playerLevel = 1;
	public static int playerExperience = 0;
	public static int playerHealth = 50;
	public static int playerMaxHealth = 50;
	public static int playerStrength = 5;

	public static int maxQueue;
	public static boolean haveGem = false;

	public static void main(String[] args) {
		if (DEBUGGING) {
			// Display the command line args.
			System.out.println("Starting with args:");
			for (int i = 0; i < args.length; i++) {
				System.out.println(i + ":" + args[i]);
			}
		}

		// Set starting locale, if it was provided as a command line parameter.
		if (args.length > 0) {
			try {
				int startLocation = Integer.parseInt(args[0]);
				// Check that the passed-in value for startLocation is within
				// the range of actual locations.
				if (startLocation >= 0 && startLocation <= MAX_LOCALES) {
					currentLocale = startLocation;
				} else {
					System.out.println("WARNING: passed-in starting location (" + args[0] + ") is out of range.");
				}
			} catch (NumberFormatException ex) {
				System.out.println("WARNING: Invalid command line arg: " + args[0]);
				if (DEBUGGING) {
					System.out.println(ex.toString());
				}
			}
		}

		// Get the game started.
		startGame();
		init();
		updateDisplay();

		// Game Loop
		while (stillPlaying) {
			getCommand();
			navigate();
			updateDisplay();
		}

		// We're done. Thank the player and exit.
		System.out.println("Your path in forward order.");
		System.out.println("Key: 0:Boys Dorms, 1: Gym, 2: Cafeteria, 3. Front Gate, 4. Classrooms, 5. Club Building, 6. Old Building, 7. Girls Dorm, 8. Theatre");
		System.out.println("Alternate Key: 9: Base, 10: Boys Dorms, 11: Gym, 12: Front Gate, 13: Classrooms, 14: Club Building, 15: Old Building, 16: Girls Dorms, 17: Theatre");
		try {
			for (int i = 0; i < myQueue.getCapacity(); i++) {
				System.out.println(myQueue.dequeue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Your path in reverse order.");
		System.out.println("Key: 0:Boys Dorms, 1: Gym, 2: Cafeteria, 3. Front Gate, 4. Classrooms, 5. Club Building, 6. Old Building, 7. Girls Dorm, 8. Theatre");
		System.out.println("Alternate Key: 9: Base, 10: Boys Dorms, 11: Gym, 12: Front Gate, 13: Classrooms, 14: Club Building, 15: Old Building, 16: Girls Dorms, 17: Theatre");
		try {
			for (int i = 0; i < myStack.getCapacity(); i++) {
				System.out.println(myStack.pop());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Final Score:");
		System.out.print("[" + moves + " moves, score " + score + " ratio + " + achievementRatio + "] ");
		System.out.println(playerMoney + " Bucks");
		System.out.println("-----Stats-----");
		System.out.println("Name: " + playerName);
		System.out.println("Level: " + playerLevel);
		System.out.println("Exp: " + playerExperience + "/100");
		System.out.println("Health: " + playerHealth + "/" + playerMaxHealth);
		System.out.println("Strength: " + (playerStrength) + "\n");
		System.out.println("Ignore the -1's I couldn't figure out how to get it to fully work.");
		System.out.println("Thank you for playing.");
	}

	//
	// Private
	//

	private static void startGame() {
		System.out.println("What is your name?");
		playerName = inputReader.next();
		System.out.println("Just a normal day in a normal life for " + playerName + ". School was no mystery for me, but what was peculiar was the old building across campus. \nIt's been sitting their for as long as I can remember. Even when I was a child and took road trips I always remembered seeing it.\n Legend does say however that whomever walks into that building never comes back. Last year three brave souls stumbled into the building and their rotting corpses were found days later. \n Although as scary as that sounds I can't help but wonder what really lies beyond those doors.\n");
		System.out.println("You wake up and let out a loud yawn as you prepare yourself for the day.");
		System.out.println("Stepping outside your room you notice your friends scattering along and wave your way\n.");
	}

	private static void init() {
		// Initialize any uninitialized globals.
		command = new String();
		stillPlaying = true; // TODO: Do we need this?
		// initialize all the values

		// Set up the location instances of the Locale class.
		// Locale loc0 = new Locale(0);
		loc0.setName("Boys Dorms");
		loc0.setDesc("The schools dorms for the guys. Kind of smells like a combination of feet and beer.");
		loc0.setDirections("North, East, South.");
		loc0.setWater("No");
		loc0.setItem("a Phone");
		loc0.setNorth(2);
		loc0.setSouth(1);
		loc0.setWest(-1);
		loc0.setEast(4);

		// Locale loc1 = new Locale(1);
		loc1.setName("Gym");
		loc1.setDesc("A group of jocks are running around playing basketball while you hear the rythmn of a song you know in the background.");
		loc1.setDirections("North, East.");
		loc1.setWater("No");
		loc1.setItem("nothing");
		loc1.setNorth(1);
		loc1.setSouth(-1);
		loc1.setWest(-1);
		loc1.setEast(5);

		// Locale loc2 = new Locale(2);
		loc2.setName("Cafeteria");
		loc2.setDesc("Crowds flock in lines as if it were the last food on the planet. I struggle to even breathe.");
		loc2.setDirections("East, South.");
		loc2.setWater("No");
		loc2.setItem("nothing");
		loc2.setNorth(-1);
		loc2.setSouth(0);
		loc2.setWest(-1);
		loc2.setEast(3);

		// Locale loc3 = new Locale(3);
		loc3.setName("Front Gate");
		loc3.setDesc("People surround the front of the gate trying their best not to be late to class.");
		loc3.setDirections("West, East, South.");
		loc3.setWater("No");
		loc3.setItem("nothing");
		loc3.setNorth(-1);
		loc3.setSouth(4);
		loc3.setWest(2);
		loc3.setEast(6);

		// Locale loc4 = new Locale(4);
		loc4.setName("Classrooms");
		loc4.setDesc("The crowded hallways are packed with teachers and students alike. I work my best to go to my class as fast as I can.");
		loc4.setDirections("North, West, East, South.");
		loc4.setWater("No");
		loc4.setItem("nothing");
		loc4.setNorth(3);
		loc4.setSouth(5);
		loc4.setWest(1);
		loc4.setEast(7);

		// Locale loc5 = new Locale(5);
		loc5.setName("Club Building");
		loc5.setDesc("Posters hang around everywhere with people scattering to get their work done for their respective groups.");
		loc5.setDirections("North, East, West.");
		loc5.setWater("Yes");
		loc5.setItem("nothing");
		loc5.setNorth(4);
		loc5.setSouth(-1);
		loc5.setWest(1);
		loc5.setEast(8);

		// Locale loc6 = new Locale(6);
		loc6.setName("Old Building");
		loc6.setDesc("Bright lights flash and you feel your body start to fade.");
		loc6.setDirections("Nowhere");
		loc6.setWater("No");
		loc6.setItem("nothing");
		loc6.setNorth(-1);
		loc6.setSouth(7);
		loc6.setWest(3);
		loc6.setEast(-1);

		// Locale loc7 = new Locale(7);
		loc7.setName("Girls Dorms");
		loc7.setDesc("The girls dorms, reeks of perfume and week old scrambeled eggs, Gross.");
		loc7.setDirections("South, North, West.");
		loc7.setWater("No");
		loc7.setItem("nothing");
		loc7.setNorth(6);
		loc7.setSouth(8);
		loc7.setWest(4);
		loc7.setEast(-1);

		// Locale loc8 = new Locale(8);
		loc8.setName("Theatre");
		loc8.setDesc("The room is empty but the stage is covered with props and the set for the upcoming play.");
		loc8.setDirections("North, West.");
		loc8.setWater("Yes");
		loc8.setItem("nothing");
		loc8.setNorth(7);
		loc8.setSouth(-1);
		loc8.setWest(5);
		loc8.setEast(-1);

		loc9.setName("Base");
		loc9.setDesc("The room is covered with boxes almost as high as the ceiling reaches and there is a big table with people crowded about.");
		loc9.setDirections("South, East.");
		loc9.setWater("No");
		loc9.setItem("nothing");
		loc9.setNorth(-1);
		loc9.setSouth(10);
		loc9.setWest(-1);
		loc9.setEast(12);

		loc10.setName("Boys Dorm");
		loc10.setDesc("Bugs crowd the floor or whats left of it and the faint smell of dirty laungry is still in the air.");
		loc10.setDirections("South, East.");
		loc10.setWater("No");
		loc10.setItem("nothing");
		loc10.setNorth(9);
		loc10.setSouth(11);
		loc10.setWest(-1);
		loc10.setEast(13);

		loc11.setName("Gym");
		loc11.setDesc("You see shattered glass all over the floor the the somewhat visible marks of where the basketball courts used to be.");
		loc11.setDirections("South, East.");
		loc11.setWater("No");
		loc11.setItem("nothing");
		loc11.setNorth(10);
		loc11.setSouth(-1);
		loc11.setWest(-1);
		loc11.setEast(14);

		loc12.setName("Front Gate");
		loc12.setDesc("Locks upon locks are covered on the rusty front gate. Seems like noones been here in years.");
		loc12.setDirections("South, East.");
		loc12.setWater("No");
		loc12.setItem("nothing");
		loc12.setNorth(-1);
		loc12.setSouth(13);
		loc12.setWest(9);
		loc12.setEast(15);

		loc13.setName("Classrooms");
		loc13.setDesc("Desks are scattered all around the room in all kinds of directions. There are papers lying on the teachers desk as if the whole class just froze in time.");
		loc13.setDirections("South, East.");
		loc13.setWater("No");
		loc13.setItem("nothing");
		loc13.setNorth(12);
		loc13.setSouth(14);
		loc13.setWest(10);
		loc13.setEast(16);

		loc14.setName("Club Building");
		loc14.setDesc("Torn apart posters line the halls and trashcans lie on the floor acting as barricades as to what could lead ahead.");
		loc14.setDirections("South, East.");
		loc14.setWater("No");
		loc14.setItem("nothing");
		loc14.setNorth(13);
		loc14.setSouth(-1);
		loc14.setWest(11);
		loc14.setEast(17);

		loc15.setName("Old Building");
		loc15.setDesc("Just big old rooms, nothing really special.");
		loc15.setDirections("South, East.");
		loc15.setWater("No");
		loc15.setItem("nothing");
		loc15.setNorth(-1);
		loc15.setSouth(16);
		loc15.setWest(12);
		loc15.setEast(-1);

		loc16.setName("Girls Dorm");
		loc16.setDesc("The faint aroma of perfume still clouds the air around me as I gasp for air.");
		loc16.setDirections("South, East.");
		loc16.setWater("No");
		loc16.setItem("nothing");
		loc16.setNorth(15);
		loc16.setSouth(17);
		loc16.setWest(13);
		loc16.setEast(-1);

		loc17.setName("Theatre");
		loc17.setDesc("The doors are locked but I can see through the window that there is something crawling on the floor. Best I not intrude.");
		loc17.setDirections("South, East.");
		loc17.setWater("No");
		loc17.setItem("nothing");
		loc17.setNorth(16);
		loc17.setSouth(-1);
		loc17.setWest(14);
		loc17.setEast(-1);
		// TODO: Lead to elsewhere

		if (DEBUGGING) {
			System.out.println("All game locations:");
			for (int i = 0; i < locations.length; ++i) {
				System.out.println(i + ":" + locations[i].toString());
			}
		}
		;
		createMagicItems();
	}

	private static void updateDisplay() {
		try {
			myQueue.enqueue(currentLocale);
			myStack.push(currentLocale);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (currentLocale == 0) {
			System.out.println(loc0.getText());
		} else if (currentLocale == 1) {
			System.out.println(loc1.getText());
		} else if (currentLocale == 2) {
			System.out.println(loc2.getText());
		} else if (currentLocale == 2) {
			System.out.println(loc2.getText());
		} else if (currentLocale == 3) {
			System.out.println(loc3.getText());
		} else if (currentLocale == 4) {
			System.out.println(loc4.getText());
		} else if (currentLocale == 5) {
			System.out.println(loc5.getText());
		} else if (currentLocale == 6) {
			System.out.println(loc6.getText());
			enterOldBuilding();
		} else if (currentLocale == 7) {
			System.out.println(loc7.getText());
		} else if (currentLocale == 8) {
			System.out.println(loc8.getText());
		} else if (currentLocale == 9) {
			System.out.println(loc9.getText());
		} else if (currentLocale == 10) {
			System.out.println(loc10.getText());
		} else if (currentLocale == 11) {
			System.out.println(loc11.getText());
		} else if (currentLocale == 12) {
			System.out.println(loc12.getText());
		} else if (currentLocale == 13) {
			System.out.println(loc13.getText());
		} else if (currentLocale == 14) {
			System.out.println(loc14.getText());
		} else if (currentLocale == 15) {
			System.out.println(loc15.getText());
			enterSanctuary();
		} else if (currentLocale == 16) {
			System.out.println(loc16.getText());
		} else if (currentLocale == 17) {
			System.out.println(loc17.getText());
		}
	}

	private static void getCommand() {
		System.out.print("[" + moves + " moves, score " + score + " ratio + " + achievementRatio + "] ");
		System.out.println(playerMoney + " Bucks");
		command = inputReader.nextLine(); // command is global.
	}

	private static void navigate() {
		final int INVALID = -1;
		int dir = INVALID; // This will get set to a value > 0 if a direction
							// command was entered.
		int newLocation = 0;
		double gemEncounter = 0;
		if (command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n")) {
			gemEncounter = Math.random() * 10;
			if (gemEncounter > 8) {
				if (currentLocale > 9) {
					if (!haveGem) {
						System.out.println("You found a shiny glowing object on the floor. It feels as if you should take it to the sanctuary");
						playerLevel = 10;
						playerInventory[playerMaxInventory] = "Gem of Truth";
						playerMaxInventory++;
						haveGem = true;
					}
				}
			}
			if (currentLocale == 0) {
				newLocation = loc0.getNorth();
			} else if (currentLocale == 1) {
				newLocation = loc1.getNorth();
			} else if (currentLocale == 2) {
				newLocation = loc2.getNorth();
			} else if (currentLocale == 3) {
				newLocation = loc3.getNorth();
			} else if (currentLocale == 4) {
				newLocation = loc4.getNorth();
			} else if (currentLocale == 5) {
				newLocation = loc5.getNorth();
			} else if (currentLocale == 6) {
				newLocation = loc6.getNorth();
			} else if (currentLocale == 7) {
				newLocation = loc7.getNorth();
			} else if (currentLocale == 8) {
				newLocation = loc8.getNorth();
			} else if (currentLocale == 9) {
				newLocation = loc9.getNorth();
			} else if (currentLocale == 10) {
				newLocation = loc10.getNorth();
			} else if (currentLocale == 11) {
				newLocation = loc11.getNorth();
			} else if (currentLocale == 12) {
				newLocation = loc12.getNorth();
			} else if (currentLocale == 13) {
				newLocation = loc13.getNorth();
			} else if (currentLocale == 14) {
				newLocation = loc14.getNorth();
			} else if (currentLocale == 15) {
				newLocation = loc15.getNorth();
			} else if (currentLocale == 16) {
				newLocation = loc16.getNorth();
			} else if (currentLocale == 17) {
				newLocation = loc17.getNorth();
			}
		} else if (command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s")) {
			gemEncounter = Math.random() * 10;
			if (gemEncounter > 8) {
				if (currentLocale > 9) {
					if (!haveGem) {
						System.out.println("You found a shiny glowing object on the floor. It feels as if you should take it to the sanctuary");
						playerLevel = 10;
						playerInventory[playerMaxInventory] = "Gem of Truth";
						playerMaxInventory++;
						haveGem = true;
					}
				}
			}
			if (currentLocale == 0) {
				newLocation = loc0.getSouth();
			} else if (currentLocale == 1) {
				newLocation = loc1.getSouth();
			} else if (currentLocale == 2) {
				newLocation = loc2.getSouth();
			} else if (currentLocale == 3) {
				newLocation = loc3.getSouth();
			} else if (currentLocale == 4) {
				newLocation = loc4.getSouth();
			} else if (currentLocale == 5) {
				newLocation = loc5.getSouth();
			} else if (currentLocale == 6) {
				newLocation = loc6.getSouth();
			} else if (currentLocale == 7) {
				newLocation = loc7.getSouth();
			} else if (currentLocale == 8) {
				newLocation = loc8.getSouth();
			} else if (currentLocale == 9) {
				newLocation = loc9.getSouth();
			} else if (currentLocale == 10) {
				newLocation = loc10.getSouth();
			} else if (currentLocale == 11) {
				newLocation = loc11.getSouth();
			} else if (currentLocale == 12) {
				newLocation = loc12.getSouth();
			} else if (currentLocale == 13) {
				newLocation = loc13.getSouth();
			} else if (currentLocale == 14) {
				newLocation = loc14.getSouth();
			} else if (currentLocale == 15) {
				newLocation = loc15.getSouth();
			} else if (currentLocale == 16) {
				newLocation = loc16.getSouth();
			} else if (currentLocale == 17) {
				newLocation = loc17.getSouth();
			}
		} else if (command.equalsIgnoreCase("east") || command.equalsIgnoreCase("e")) {
			gemEncounter = Math.random() * 10;
			if (gemEncounter > 8) {
				if (currentLocale > 9) {
					if (!haveGem) {
						System.out.println("You found a shiny glowing object on the floor. It feels as if you should take it to the sanctuary");
						playerLevel = 10;
						playerInventory[playerMaxInventory] = "Gem of Truth";
						playerMaxInventory++;
						haveGem = true;
					}
				}
			}
			if (currentLocale == 0) {
				newLocation = loc0.getEast();
			} else if (currentLocale == 1) {
				newLocation = loc1.getEast();
			} else if (currentLocale == 2) {
				newLocation = loc2.getEast();
			} else if (currentLocale == 3) {
				newLocation = loc3.getEast();
			} else if (currentLocale == 4) {
				newLocation = loc4.getEast();
			} else if (currentLocale == 5) {
				newLocation = loc5.getEast();
			} else if (currentLocale == 6) {
				newLocation = loc6.getEast();
			} else if (currentLocale == 7) {
				newLocation = loc7.getEast();
			} else if (currentLocale == 8) {
				newLocation = loc8.getEast();
			} else if (currentLocale == 9) {
				newLocation = loc9.getEast();
			} else if (currentLocale == 10) {
				newLocation = loc10.getEast();
			} else if (currentLocale == 11) {
				newLocation = loc11.getEast();
			} else if (currentLocale == 12) {
				newLocation = loc12.getEast();
			} else if (currentLocale == 13) {
				newLocation = loc13.getEast();
			} else if (currentLocale == 14) {
				newLocation = loc14.getEast();
			} else if (currentLocale == 15) {
				newLocation = loc15.getEast();
			} else if (currentLocale == 16) {
				newLocation = loc16.getEast();
			} else if (currentLocale == 17) {
				newLocation = loc17.getEast();
			}
		} else if (command.equalsIgnoreCase("west") || command.equalsIgnoreCase("w")) {
			gemEncounter = Math.random() * 10;
			if (gemEncounter > 8) {
				if (currentLocale > 9) {
					if (!haveGem) {
						System.out.println("You found a shiny glowing object on the floor. It feels as if you should take it to the sanctuary");
						playerLevel = 10;
						playerInventory[playerMaxInventory] = "Gem of Truth";
						playerMaxInventory++;
						haveGem = true;
					}
				}
			}
			if (currentLocale == 0) {
				newLocation = loc0.getWest();
			} else if (currentLocale == 1) {
				newLocation = loc1.getWest();
			} else if (currentLocale == 2) {
				newLocation = loc2.getWest();
			} else if (currentLocale == 3) {
				newLocation = loc3.getWest();
			} else if (currentLocale == 4) {
				newLocation = loc4.getWest();
			} else if (currentLocale == 5) {
				newLocation = loc5.getWest();
			} else if (currentLocale == 6) {
				newLocation = loc6.getWest();
			} else if (currentLocale == 7) {
				newLocation = loc7.getWest();
			} else if (currentLocale == 8) {
				newLocation = loc8.getWest();
			} else if (currentLocale == 9) {
				newLocation = loc9.getWest();
			} else if (currentLocale == 10) {
				newLocation = loc10.getWest();
			} else if (currentLocale == 11) {
				newLocation = loc11.getWest();
			} else if (currentLocale == 12) {
				newLocation = loc12.getWest();
			} else if (currentLocale == 13) {
				newLocation = loc13.getWest();
			} else if (currentLocale == 14) {
				newLocation = loc14.getWest();
			} else if (currentLocale == 15) {
				newLocation = loc15.getWest();
			} else if (currentLocale == 16) {
				newLocation = loc16.getWest();
			} else if (currentLocale == 17) {
				newLocation = loc17.getWest();
			}
		} else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q")) {
			quit();
		} else if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h")) {
			help();
		} else if (command.equalsIgnoreCase("map") || command.equalsIgnoreCase("m")) {
			openMap();
		} else if (command.equalsIgnoreCase("take") || command.equalsIgnoreCase("t")) {
			takeItem();
		} else if (command.equalsIgnoreCase("dance") || command.equalsIgnoreCase("d")) {
			dance();
		} else if (command.equalsIgnoreCase("sleep") || command.equalsIgnoreCase("sl")) {
			playerSleep();
		} else if (command.equalsIgnoreCase("stats") || command.equalsIgnoreCase("st")) {
			playerStats();
		} else if (command.equalsIgnoreCase("inventory") || command.equalsIgnoreCase("i")) {
			managePlayerInventory();
		} else if (command.equalsIgnoreCase("shop") || command.equalsIgnoreCase("sh")) {
			enterMagickShoppe();
		} else if (command.equalsIgnoreCase("leave")) {
			System.out.println("You have to be in the Magick Shoppe.");
		} else {
			System.out.println("That is not a valid command.");
		}
		;
		// All the locations and set it to one when a location is newly visited
		if (newLocation < 0) {
			System.out.println("You cannot go that way.");
		} else {
			if (currentLocale == 0) {
				currentLocale = newLocation;
				while (hasVisited[0] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[0] = 1;
				}
			} else if (currentLocale == 1) {
				currentLocale = newLocation;
				while (hasVisited[1] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[1] = 1;
				}
			} else if (currentLocale == 2) {
				currentLocale = newLocation;
				while (hasVisited[2] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[2] = 1;
				}

			} else if (currentLocale == 3) {
				currentLocale = newLocation;
				while (hasVisited[3] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[3] = 1;
				}

			} else if (currentLocale == 4) {
				currentLocale = newLocation;
				while (hasVisited[4] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[4] = 1;
				}

			} else if (currentLocale == 5) {
				currentLocale = newLocation;
				while (hasVisited[5] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[5] = 1;
				}

			} else if (currentLocale == 6) {
				currentLocale = newLocation;
				while (hasVisited[6] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[6] = 1;
				}

			} else if (currentLocale == 7) {
				currentLocale = newLocation;
				while (hasVisited[7] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[7] = 1;
				}

			} else if (currentLocale == 8) {
				currentLocale = newLocation;
				while (hasVisited[8] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[8] = 1;
				}

			} else if (currentLocale == 9) {
				currentLocale = newLocation;
				while (hasVisited[9] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[9] = 1;
				}
			} else if (currentLocale == 10) {
				currentLocale = newLocation;
				while (hasVisited[10] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[10] = 1;
				}
			} else if (currentLocale == 11) {
				currentLocale = newLocation;
				while (hasVisited[11] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[11] = 1;
				}
			} else if (currentLocale == 12) {
				currentLocale = newLocation;
				while (hasVisited[12] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[12] = 1;
				}
			} else if (currentLocale == 13) {
				currentLocale = newLocation;
				while (hasVisited[13] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[13] = 1;
				}
			} else if (currentLocale == 14) {
				currentLocale = newLocation;
				while (hasVisited[14] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[14] = 1;
				}
			} else if (currentLocale == 15) {
				currentLocale = newLocation;
				while (hasVisited[15] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[15] = 1;
				}
			} else if (currentLocale == 16) {
				currentLocale = newLocation;
				while (hasVisited[16] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[16] = 1;
				}
			} else if (currentLocale == 17) {
				currentLocale = newLocation;
				while (hasVisited[17] == 0) {
					score += 5;
					System.out.println("This is your first time here!");
					hasVisited[17] = 1;
				}
			}
			moves = moves + 1;
			playerMoney = playerMoney += Math.round(Math.random() * 10);
			achievementRatio = score / moves;
		}
	}

	private static void help() {
		System.out.println("The commands are as follows:");
		System.out.println("   n/north");
		System.out.println("   s/south");
		System.out.println("   e/east");
		System.out.println("   w/west");
		System.out.println("   q/quit");
		System.out.println("   m/map");
		System.out.println("   d/dance");
		System.out.println("   sl/sleep");
		System.out.println("   st/stats");
		System.out.println("Shop Commands:");
		System.out.println("   sh/shop");
		System.out.println("     leave");
	}

	private static void quit() {
		stillPlaying = false;
	}

	private static void dance() {
		System.out.println("You dance around like a mad man.");
	}

	private static void playerStats() {
		System.out.println("-----Stats-----");
		System.out.println("Name: " + playerName);
		System.out.println("Level: " + playerLevel);
		System.out.println("Exp: " + playerExperience + "/100");
		System.out.println("Health: " + playerHealth + "/" + playerMaxHealth);
		System.out.println("Strength: " + (playerStrength) + "\n");
		// TODO: Other stats and classes
	}

	private static void takeItem() {
		if (currentLocale == 0 && loc0.getItem() != "nothing") {
			System.out.println("You've picked up " + loc0.getItem() + ".");
			hasMap = true;
			loc0.setItem("nothing");
		} else if (currentLocale == 0) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 1) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 2) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 3) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 4) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 5) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 6) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 7) {
			System.out.println("There is nothing to pick up");
		} else if (currentLocale == 8) {
			System.out.println("There is nothing to pick up");
		}
	}

	private static void openMap() {
		if (hasMap == true) {
			if (currentLocale == 0) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 1) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 2) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 3) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 4) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 5) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 6) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 7) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 8) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Cafet| __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 9) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 10) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 11) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||  C  |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 12) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 13) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 14) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |  C  |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 15) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 16) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			} else if (currentLocale == 17) {
				System.out.println("Key: C = Current Location");
				System.out.println("+-----------------------------+");
				System.out.println("            School             ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||Base | __ |FGate| __ |OldBu||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("||BoysD| __ |Class| __ |GDorm||");
				System.out.println("||     |    |     |    |     ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("    |          |          |    ");
				System.out.println("|-------    -------    -------|");
				System.out.println("|| Gym | __ |ClubB| __ |Theat||");
				System.out.println("||     |    |     |    |  C  ||");
				System.out.println("|-------    -------    -------|");
				System.out.println("+-----------------------------+");
			}
		}
	}

	private static void enterMagickShoppe() {
		if (currentLocale == 9) {
			inShop = true;
			while (inShop == true) {
				System.out.println("Here's what I have.");
				ListMan lm1 = new ListMan();
				lm1.setName("Magic Items");
				lm1.setDesc("These are some of my favorite things.");

				final String fileName = "magic.txt";
				String playerBuy;

				readMagicItemsFromFileToList(fileName, lm1);
				// Display the list of items.
				// System.out.println(lm1.toString());

				// Declare an array for the items.
				ListItem[] items = new ListItem[lm1.getLength()];
				readMagicItemsFromFileToArray(fileName, items);
				selectionSort(items);
				Scanner inputReader = new Scanner(System.in);
				System.out.println("Here's what I have: ");
				for (int i = 0; i < items.length; i++) {
					if (items[i] != null) {
						System.out.println(items[i].toString());
					}
				}
				System.out.print("What item would you like? ");
				String targetItem = new String();
				targetItem = inputReader.nextLine();
				System.out.println();

				if (targetItem.equalsIgnoreCase("Leave")) {
					inShop = false;
					currentLocale = 9;
				} else {
					ListItem li = new ListItem();
					li = binarySearchArray(items, targetItem);
					amountCost = Math.round(Math.random() * 10);
					System.out.println("Would you like to buy " + targetItem + " for " + amountCost + "?");
					playerBuy = inputReader.next();
					if (playerBuy.equalsIgnoreCase("Yes")) {
						if (playerMoney >= amountCost) {
							playerMoney -= amountCost;
							// Take item and put it in inventory TODO:
							// initialize all the values
							if (playerMaxInventory <= 27) {
								playerInventory[playerMaxInventory] = targetItem;
								playerMaxInventory++;
							} else {
								System.out.println("You can't hold any more.");
							}
						} else {
							System.out.println("You don't have enough cash freeloader.");
						}
					} else if (playerBuy.equalsIgnoreCase("No")) {
						System.out.println("I see, but how about this?");
					} else {
						System.out.println("I don't understand your accent.");
					}
				}
			}
		} else {
			System.out.println("You are not near the shop.");
		}
	}

	private static ListItem binarySearchArray(ListItem[] items, String target) {
		ListItem retVal = null;
		System.out.println("Binary Searching for " + target + ".");
		ListItem currentItem = new ListItem();
		boolean isFound = false;
		int counter = 0;
		int low = 0;
		int high = items.length - 1; // because 0-based arrays
		while ((!isFound) && (low <= high)) {
			int mid = Math.round((high + low) / 2);
			currentItem = items[mid];
			if (currentItem.getName().equalsIgnoreCase(target)) {
				// We found it!
				isFound = true;
				retVal = currentItem;
			} else {
				// Keep looking.
				counter++;
				if (currentItem.getName().compareToIgnoreCase(target) > 0) {
					// target is higher in the list than the currentItem (at
					// mid)
					high = mid - 1;
				} else {
					// target is lower in the list than the currentItem (at mid)
					low = mid + 1;
				}
			}
		}
		if (isFound) {
			System.out.println("Found " + target + " after " + counter + " comparisons.");
		} else {
			System.out.println("Could not find " + target + " in " + counter + " comparisons.");
		}
		return retVal;
	}

	private static void readMagicItemsFromFileToList(String fileName, ListMan lm) {
		File myFile = new File(fileName);
		try {
			Scanner input = new Scanner(myFile);
			while (input.hasNext()) {
				// Read a line from the file.
				String itemName = input.nextLine();

				// Construct a new list item and set its attributes.
				ListItem fileItem = new ListItem();
				fileItem.setName(itemName);
				fileItem.setCost(Math.random() * 100);
				fileItem.setNext(null); // Still redundant. Still safe.

				// Add the newly constructed item to the list.
				lm.add(fileItem);
			}
			// Close the file.
			input.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File not found. " + ex.toString());
		}

	}

	private static void readMagicItemsFromFileToArray(String fileName, ListItem[] items) {
		File myFile = new File(fileName);
		try {
			int itemCount = 0;
			Scanner input = new Scanner(myFile);
			while (input.hasNext() && itemCount < items.length) {
				// Read a line from the file.
				String itemName = input.nextLine();

				// Construct a new list item and set its attributes.
				ListItem fileItem = new ListItem();
				fileItem.setName(itemName);
				fileItem.setCost(Math.random() * 10);
				fileItem.setNext(null); // Still redundant. Still safe.

				// Add the newly constructed item to the array.
				items[itemCount] = fileItem;
				itemCount = itemCount + 1;
			}
			// Close the file.
			input.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File not found. " + ex.toString());
		}
	}

	private static void selectionSort(ListItem[] items) {
		for (int pass = 0; pass < items.length - 1; pass++) {
			// System.out.println(pass + "-" + items[pass]);
			int indexOfTarget = pass;
			int indexOfSmallest = indexOfTarget;
			for (int j = indexOfTarget + 1; j < items.length; j++) {
				if (items[j].getName().compareToIgnoreCase(items[indexOfSmallest].getName()) < 0) {
					indexOfSmallest = j;
				}
			}
			ListItem temp = items[indexOfTarget];
			items[indexOfTarget] = items[indexOfSmallest];
			items[indexOfSmallest] = temp;
		}
	}

	private static void managePlayerInventory() {
		System.out.println("Inventory:");
		System.out.println("-------------------");
		for (int i = 0; i < playerInventory.length; i++) {
			System.out.println(playerInventory[i]);
		}
		System.out.println("-------------------");
	}

	private static void createMagicItems() {
		// Create the list manager for our magic items.
		List0 magicItems = new List0();
		magicItems.setName("Magic Items");
		magicItems.setDesc("These are the magic items.");
		magicItems.setHead(null);

		// Create some magic items and put them in the list.
		ListItem i1 = new ListItem();
		i1.setName("Ruby ring");
		i1.setDesc("A bright gold ring with a ruby in the middle.");
		i1.setCost(5.0);

		ListItem i2 = new ListItem();
		i2.setName("Claymore");
		i2.setDesc("A double edged sword that looks like it's been used before.");
		i2.setCost(10.0);

		ListItem i3 = new ListItem();
		i3.setName("Cloak of Darkness");
		i3.setDesc("A cloak with the same color as the night sky.");
		i3.setCost(10.0);

		ListItem i4 = new ListItem();
		i4.setName("Shield of the Divine");
		i4.setDesc("A massive bright colored shield, looks like it can block any blow.");
		i4.setCost(100.0);

		// Link it all up.
		magicItems.setHead(i1);
		i1.setNext(i2);
		i2.setNext(i3);
		i3.setNext(i4);
		i4.setNext(null);

		// System.out.println(magicItems.toString());
	}

	private static void enterOldBuilding() {
		System.out.println("You wake up in a small room with desks cluttered, windows shattered and glass all over the floor.");
		System.out.println("A figure walks up to you and holds out a hand. A whisper comes from the figure.");
		System.out.println("What are you doing here? Quick we have to run and fast.");
		System.out.println("So let me get this straight you're name is " + playerName + " and you're from another dimension?");
		System.out.println("Sounds a little weird if you ask me. In any case, here, this should help you find your way around.");
		System.out.println("The girl throws you a piece of paper with a small map scribbled.");
		System.out.println("You should be fine to explore but once night rolls around come right back here.");
		System.out.println("First things first I have to find a way to make it back to my dimension. Wouldn't hurt to find my way around.");
		System.out.println("It also looks like one of the people here has goods for sale, maybe I should check it out.");
		currentLocale = 9;
		updateDisplay();
	}

	private static void enterSanctuary() {
		if (haveGem) {
			System.out.println("You shine the gem high into the cracks of light in the ceiling.");
			System.out.println("Your body begins to glow as you can feel yourself drifting off into a deep slumber.");
			System.out.println("You wake up bedside next to a couple of your friends who explain that you've been gone for weeks and just turned up.");
			System.out.println("You think back and it all seems like a blur but one day you know deep down that you will return.");
			System.out.println("The End");
			quit();
		}
	}

	private static void playerSleep() {
		if (currentLocale == 9 || currentLocale == 0) {
			System.out.println("You lay down and fall into a deep slumber and wake up hours later.");
			playerHealth = playerMaxHealth;
		} else {
			System.out.println("You cannot sleep here.");
		}
	}

	public static void battleSystem() {
		// Never Finished Just a work in progress
		boolean inBattle = false;
		int monsterHealth = playerLevel + 10;
		int monsterStrength = playerLevel + 3;
		inBattle = true;
		if (inBattle) {
			System.out.println("You have encountered a monster.");
			System.out.println("What would you like to do? Attack/A Run/R");
			if (command.equalsIgnoreCase("attack") || command.equalsIgnoreCase("a")) {
				monsterHealth -= playerStrength;
				System.out.println("You have attacked the monster for " + playerStrength + " damage.");
				playerHealth -= monsterStrength;
				System.out.println("The monster attacks you for " + monsterStrength + " damage.");
			}
			if (command.equalsIgnoreCase("run") || command.equalsIgnoreCase("r")) {
				System.out.println("You have fled!");
				inBattle = false;
			}
			if (monsterHealth <= 0) {
				System.out.println("You have defeated the monster!");
				playerExperience += Math.round(Math.random() * 10);
				playerMoney = playerMoney += Math.round(Math.random() * 10);
				if (playerExperience >= 100) {
					System.out.println("You have leveled up!");
					System.out.println("You have gained 5 health and 1 strength!");
					playerMaxHealth += 5;
					playerStrength += 1;
				}
				inBattle = false;
			}
			if (playerHealth <= 0) {
				currentLocale = 9;
				System.out.println("You blacked out.");
				playerHealth = playerMaxHealth;
			}
		}
	}
}

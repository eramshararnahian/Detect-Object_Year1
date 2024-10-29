import swiftbot.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class Main {
	private static double distance;
	static SwiftBotAPI swiftBot;
	static String currentMode = "";
	static int objectEncounters=0;
	static int imagesCaptured=0;
	static  double executionTime;

	private static void showExecutionLog() {
		System.out.println("Mode: " + currentMode);
		System.out.println("Objects Detected: " + objectEncounters);
		System.out.println("Images Captured: " + imagesCaptured);
		System.out.println("Total Execution Time:"+ executionTime);
	}

	private static void blinkGreenLights() throws InterruptedException {
		int[] greenLights = {0, 0, 255};
		int[] lightsOff = {0, 0, 0};

		// Blink green lights for 1 second
		for (int i = 0; i < 2; i++) {
			swiftBot.fillUnderlights(greenLights); // Turn underlights green
			Thread.sleep(500); // On for 500ms
			swiftBot.fillUnderlights(lightsOff); // Turn underlights off
			Thread.sleep(500); // Off for 500ms
		}
		// Ensure lights are Green after blinking
		swiftBot.fillUnderlights(new int[]{0, 0, 255});
	}

	private static void blinkRedLights() throws InterruptedException {
		int[] redLights = {255, 0, 0};
		int[] lightsOff = {0, 0, 0};

		// Blink red lights for 1 second
		for (int i = 0; i < 2; i++) {
			swiftBot.fillUnderlights(redLights); // Turn underlights red
			Thread.sleep(500); // On for 500ms
			swiftBot.fillUnderlights(lightsOff); // Turn underlights off
			Thread.sleep(500); // Off for 500ms
		}
		// Ensure lights are red after blinking
		swiftBot.fillUnderlights(new int []{255,0,0});
	}

	public static void curiousSwiftBot() {
		System.out.println("****************************************************************************************************************************************");
		System.out.println("                  ***Welcome to Curious Swiftbot***");
        System.out.println("****************************************************************************************************************************************");  
		System.out.println("This Swiftbot will detect any object within its 15cm distance and take an image of it.");
		System.out.println("The Swiftbot is now moving.");
		System.out.println("                       Underlights = Blue");
		System.out.println("                         Set Speed = 50");
		System.out.println("Place any object in front or behind the Swiftbot");

		objectEncounters = 0; // Reset object encounters
		imagesCaptured = 0; // Reset images captured count
		swiftBot.fillUnderlights(new int[]{0, 255, 0}); // Set underlights to blue
		swiftBot.startMove(50, 50); // Start moving at speed 50
		long startTime = System.currentTimeMillis(); // Start timing
		distance = swiftBot.useUltrasound();
		System.out.println("Initial distance from object: " + distance + " cm.");
		try {
			while (distance != 15) {

				distance = swiftBot.useUltrasound();
				if(distance < 20) {
					objectEncounters++;
					swiftBot.fillUnderlights(new int[]{0, 0, 255}); // Set Underlights to green
				}
				if (distance > 15 && distance < 18) {
					swiftBot.startMove(20, 20); // Start moving forward

					//	                    
				} else if(distance < 15){
					swiftBot.startMove(-20, -20); // Start moving forward
				}
				else {
					swiftBot.startMove(50, 50);
				}

			}

			swiftBot.stopMove(); // Stop the Bot

			System.out.println("              ***Object Detected!***");
			blinkGreenLights();
			System.out.println("       ****Congratulations! The Swiftbot has reached the buffer zone****");
			captureImage();
			Thread.sleep(1000); // Wait for 1 second after capturing the image

			// After capturing the image, the SwiftBot makes a slight turn to the right
			swiftBot.startMove(50, 0); // Right wheel is stopped, left wheel moves at speed 50 to turn
			try {
				Thread.sleep(1500); // The wheel will turn for one and a half second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			swiftBot.stopMove(); // Stop for 1 second
			try {
				Thread.sleep(1000); // Stop for 1 second
				swiftBot.fillUnderlights(new int[]{0, 255, 0}); // Set underlights back to blue
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long endTime = System.currentTimeMillis();
			executionTime = (endTime - startTime) / 1000.0;
			currentMode = "Curious"; // Set the current mode
			System.out.println("   Press Y within 5 seconds to see the log,otherwise the mode will start again");

			// Wait for button press
			promptForLog();
			System.out.println("   [Press X to exit]");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: An exception occurred.");
			System.exit(1);
		}
	}


	private static void captureImage() {
		try {
			BufferedImage image = swiftBot.getQRImage();
			if (image != null) {
				String imagePath = "capturedImage.png";
				File outputfile = new File(imagePath);
				ImageIO.write(image, "png", outputfile);
				System.out.println("Image captured and saved to " + imagePath);
				imagesCaptured++;
			} else {
				System.out.println("Failed to capture the image.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Unable to capture and save the image.");
		}

	}


	public static void scaredySwiftBot() {
		    System.out.println("**********************************************************************************************************************************");
			System.out.println("           ***Welcome to Scaredy Swiftbot***");
			System.out.println("**********************************************************************************************************************************");
			System.out.println("  This Swiftbot will detect any object within its 100 distance and take an image of it.");
			System.out.println("    The Swiftbot is now moving.");
			System.out.println("               Underlights = Blue");
			System.out.println("               Set Speed = 50");
			System.out.println("  Place any object in front or behind the Swiftbot");
		
		objectEncounters = 0; // Reset object encounters
		imagesCaptured = 0; // Reset images captured count
		swiftBot.fillUnderlights(new int[]{0, 255, 0}); // Set underlights to blue
		swiftBot.startMove(50, 50); // Start moving at speed 50
		long startTime = System.currentTimeMillis(); // Start timing
		distance = swiftBot.useUltrasound();
		System.out.println("Initial distance from object: " + distance + " cm.");

		
		try {
			distance = swiftBot.useUltrasound();
			if (distance <= 120) {
				objectEncounters++;
				swiftBot.stopMove(); // Stop the bot
				Thread.sleep(1000); // Wait for 1 second
				swiftBot.fillUnderlights(new int[]{255, 0, 0}); // Set underlights to red

				// Move towards the object to 100 cm distance
				swiftBot.startMove(50, 50);
				while (swiftBot.useUltrasound() > 100) {
					// loop until the distance is reduced to 100 cm
				}
				swiftBot.stopMove(); // Stop the bot
				System.out.println("      ****Congratulations!! You have reached the bufferzone****");
				blinkRedLights();
				captureImage(); // Capture and save the image

				// Turning left 180 degrees and move forward
				swiftBot.startMove(0, 100); // Turn left
				Thread.sleep(1800); // Turn for 1.5 seconds
				swiftBot.fillUnderlights(new int[]{0, 255, 0}); // Set underlights back to blue
				swiftBot.stopMove(); // Stop moving
				long endTime = System.currentTimeMillis();
				executionTime = (endTime - startTime) / 1000.0;
				currentMode = "Scaredy"; // Set the current mode
				System.out.println("    Press Y within 5 seconds to see the log,otherwise the mode will start again");
				

				// Wait for button press
				promptForLog();
				System.out.println("   [Press X to exit]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: An exception occurred.");
			System.exit(1);
		}
		
	}

	public static void dubiousSwiftBot() {
		System.out.println("*************************************************************************************************************************");
		System.out.println("              ***Welcome to Dubious Swiftbot***");
		System.out.println("*************************************************************************************************************************");
		System.out.println("    Randomly selecting between Curious and Scaredy modes...");

		// Generate a random number between 0 and 1
		double randomNumber = Math.random();

		// Assuming an equal chance for each mode
		if (randomNumber < 0.5) {
			System.out.println("      <>Curious SwiftBot mode selected<>");
			curiousSwiftBot(); // Call the curiousSwiftBot method
		} else {
			System.out.println("      <>Scaredy SwiftBot mode selected<>");
			scaredySwiftBot(); // Call the scaredySwiftBot method
		}
	}

	public static String testQRCodeDetection() {
		try {
			System.out.println("Taking a capture in 5 seconds..");
			Thread.sleep(5000);

			BufferedImage img = swiftBot.getQRImage(); // Use the swiftBot instance
			String decodedMessage = swiftBot.decodeQRImage(img); // Use the swiftBot instance

			if (decodedMessage.isEmpty()) {
				System.out.println("No QR Code was found. Try adjusting the distance of the SwiftBot's Camera from the QR code, or try another.");
				return "";
			} else {
				System.out.println("SUCCESS: QR code found");
				System.out.println("Decoded message: " + decodedMessage);
				return decodedMessage;
			}
		} catch (Exception e) {
			System.out.println("ERROR: Unable to scan for code.");
			e.printStackTrace();
			System.exit(5);
			return "";
		}
	}

	private static void promptForLog() {
		try {
			swiftBot.enableButton(Button.Y, () -> {
				showExecutionLog(); // Show log when Y is pressed
			});
			Thread.sleep(5000); // Wait for 5 seconds
			swiftBot.disableButton(Button.Y); // Disable the button after waiting
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		swiftBot = new SwiftBotAPI();
		boolean check = true;
		String modeSelection = testQRCodeDetection();
		while(true) {
			if(check == false)
				modeSelection = testQRCodeDetection();
			try {
				swiftBot.enableButton(Button.X, () -> {
					swiftBot.disableUnderlights();
					swiftBot.disableAllButtons();
					swiftBot.stopMove();
					System.exit(0);				
				});
			}
			catch(Exception E) {
				E.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			}
			catch(Exception E){
				E.printStackTrace();
			}
			switch (modeSelection) {
			case "curiousSwiftBot":
				check = true;
				curiousSwiftBot();
				break;
			case "scaredySwiftBot":
				check = true;
				scaredySwiftBot();
				break;
			case "dubiousSwiftBot":
				check = true;
				dubiousSwiftBot();
				break;
			default:
				System.out.println("Invalid mode: " + modeSelection);
				check = false;
				break;
			}
			swiftBot.disableUnderlights();
			swiftBot.disableAllButtons();
			swiftBot.stopMove();
		}
	}

}


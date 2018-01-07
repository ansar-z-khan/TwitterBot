import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
/*
 * Trigonometry Calculator 
 * 	Made by Ansar Khan
 * 	February 27th, 2017
 * Functionality:
 * 	Able to calculate angle or side in right triangle
 * 	Able to calculate angle or side in a non right triangle using sine and cosine law
 * 	Logs Users calculations in a file
 */



/*
 * Rubric
 
 * Critical Requirements (Program must have all of these requirements ____/6
 		*Selection menu using if/else if/else to handle the user selection //Done
 		*Program calculates the sin, cos, tan //calculates ratio and side lengths
 		*Program calculates the asin, acos, atan angles for two sides //Done
 		*Error handles the input from the user //Pretty much impossible to break my program
 		*Appropriate commenting and Program header //You're reading it right now
 		*Program works without bugs //Done


*Optional Requirements (Any Combinations to get 4 marks) __/4
	*Extended user interaction(Contextualized interactions) //Asks for further information based on what user wants to solve
	* 2 Simple Additional functions (Solve for point of intersections) //Sine law, and Cosine Law, handles degrees and radians
	** 1 complicated Additional Function //Logs calculations in a file
	***Magical items of an impressive quality  

 * */



public class UI {
	static Scanner s = new Scanner(System.in);
	static String[] arg; //Need to create this variable because I call main again, and need to send in the right parameter, nothing is actually done with it
	static boolean init = true; //boolean to run intial things only once
	static BufferedWriter out = null; //writer used to write to log file
	static File f; //file used to store log
	static Calculator calc; //object that handle all the math
	static int decVal = 1; //stores how many decimal places user wants in a answer
	static boolean isDegrees = false;//stores whether or not to use degrees or radians

	public static void main(String[] args) throws IOException {
		if (init) {
			println("Welcome To The Trig Calculator");
			decVal = getDecVal("How Many Decimal Places Would You Like Your Answers to Be In? ", 10);//Gets how many decimal points user wants
			isDegrees = isYes("Would you like to use degrees (Yes or No), no will allow you to use radians");//Asks if user wants degrees or radians
			arg = args;
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM:dd HH.mm.ss");//need date in order to have a different file name each time
			f = new File( "CalcLog.txt -" + dateFormat.format(date) + ".txt"); 
			out = new BufferedWriter(new FileWriter(f));
			init = false;
		}
		calc = new Calculator(decVal, isDegrees); //Construct calculator
		
		//Ask if user has a right triangle or other triangle
		int typeTri = getChoice(
				"Press 1 if you have a right triangle, Press 2 if your triangle is not a right triangle", 2);
		if (typeTri == 1) {
			rightTriangleCalc();
		}
		if (typeTri == 2) {
			nonRightTriangleCalc();
		}

		s.close();
	}

	static void println(String s) {
		System.out.println(s);//Method is used to prevent typing system.out...... every time
	}

	static double getValue(String prompt) {
		//This method asks the user for a value based on a prompt sent in, makes sure user is entereing a valid number
		boolean validResponse = false;
		String error = "Please Enter a Valid Response";
		double response = -Math.PI;
		while (!validResponse) {
			try {
				println(prompt);
				response = s.nextDouble();
				validResponse = true;
			} catch (InputMismatchException e) {//If input is not a double then user must try again
				println(error);
				s.next();//used to clear error from scanner
			}

		}
		return response;
	}

	static int getChoice(String prompt, int maxVal) {//Gets choice from users when asking them what they want to calculate
		boolean validResponse = false;
		String error = "Please Enter a Valid Response";
		int response = -67857565;
		while (!validResponse) {
			try {
				println(prompt);
				response = s.nextInt();
				if (response > maxVal || response <= 0) {//Catches if user tries to enter a response that does not exist
					println(error);
				} 
				else {
					validResponse = true;
				}
			} catch (InputMismatchException e) {//Catches if user does not enter an int
				println(error);
				s.next();
			}
		}
		return response;
	}

	static int getDecVal(String prompt, int maxVal) {//Function used just to get decimal val 
		boolean validResponse = false;
		String error = "Please Enter a Valid Response";
		int response = -67857565;
		while (!validResponse) {
			try {
				println(prompt);
				response = s.nextInt();
				if (response > maxVal || response < 0) {//Allows user to have decimal value at zero, only reason this function was recreated
					println(error);
				} 
				else {
					validResponse = true;
				}
			} catch (InputMismatchException e) {//catches input that is not an int
				println(error);
				s.next();
			}
		}
		return response;
	}	
	
	static void end() throws IOException {//Ran once user gets an answer
		if (isYes("Would You Like to Calculate Some Thing Else (Yes or No)")) { // asks if they want to continue
			main(arg);//calls public static void main again
		} else {//if user does not want  to continue
			if (isYes("Would You Like to save a log of your calculations? (Yes or No)")) {//asks if users want to save log
				out.close();//calling out.close actually write everything to the file rather than keeping it in a buffer
				println("Your log can be found in the same directory as this project");
				println("Thank You For Using The Trig Calculator");
				System.exit(0);
			} else {
				f.delete();//deletes file if they do not want the log
				println("Thank You For Using The Trig Calculator");
				System.exit(0);
			}
		}

	}
	
	static double getAngle() {//used when working with right triangle
		double angle = -Math.PI;
		while (angle == -Math.PI) {
			angle = getValue("Please Enter Your Angle In Degrees");
			if (angle > 90) {//Prevents users from inputting angles greater than 90 degrees
				println("Please Enter an Angle Less Than 90 Degrees");
				angle = getValue("Please Enter Your Angle");
			}
			if (angle < 0) {//stops negative angles
				println("Please Enter an Angle Greater Than 0 Degrees");
				angle = getValue("Please Enter Your Angle");
			}
		}
	
		return angle;
	}

	static double getAngle(String prompt) {// Only used for Sine and cosine law
		double angle = -Math.PI;
		while (angle == -Math.PI) {
			angle = getValue(prompt);
			if (angle < 0) {
				println("Please Enter an Angle Greater Than 0 Degrees");
			}
			if (angle > 180) {
				println("Please Enter an Angle less Than 180 Degrees");//stops users from entering angles greater than 180
			}			
		}
		return angle;
	}

	static double getSideLength(String side) {//Used to get input of side length
		double val = -50;
		while (val < 0) {
			val = getValue("Please enter the " + side + " side length");
			if (val < 0) {
				println("Please Enter a Positive Side Length");//Makes sure user enters a positive side length
			}
		}
		return val;
	}

	static boolean isYes(String prompt) {//used for yes or no questions
		String resp = "Ansar is the Best Programmer Ever";//needed to initialize with a string so...
		boolean validResponse = false;
		while (!validResponse) {
			println(prompt);
			resp = s.next();
			if (resp.contains("Yes") || resp.contains("yes") || resp.contains("YES") && !(resp.contains("NO") || resp.contains("no") || resp.contains("No"))) {//looks for yes
				validResponse = true; 
				return true;
			} else if (resp.contains("NO") || resp.contains("no") || resp.contains("No") && !resp.contains("Yes") || resp.contains("yes") || resp.contains("YES")) {//Prevents users from entering yes and no
				validResponse = true;
				return false;
			} else {
				println("Please Enter a Valid Response");
			}
		}
		return true;
	}

	static void rightTriangleCalc() throws IOException {//Used to handle all right triangle calculations
		
		int choice0 = getChoice("Press 1 to solve for a Side Length, Press 2 to Solve for an Angle", 2);
		if (choice0 == 1) {//Solving for side length
			double angle = getAngle();
			int choice1 = getChoice("Press 1 to Solve for Hypotenuse, 2 To Solve For Adjacent, 3 to Solve for Opposite",
					3);
			if (choice1 == 1) {//Solving for hypotenuse
				int choice2 = getChoice(
						"Press 1 if you know The adjacent side length, 2 if you know the opposite side length", 2);
				if (choice2 == 1) {//Using Adjacent Side Length
					double adj = getSideLength("adjacent");
					println("The Hypotenuse is " + calc.getHypCos(angle, adj));
					out.write("-Calculated a Hypotenuse of " + calc.getHypCos(angle, adj) + " using an angle of "
							+ angle + " degrees " + "with An adjacent of " + adj + "\n");
					end();

				}
				if (choice2 == 2) {//using opposite side length
					double opp = getSideLength("opposite");
					println("The Hypotenuse is " + calc.getHypSin(angle, opp));
					out.write("-Calculated a Hypotenuse of " + calc.getHypSin(angle, opp) + " using an angle of "
							+ angle + " degrees " + "with An opposite of " + opp + "\n");
					end();
				}
			}
			if (choice1 == 2) {//Solving for Adjacent
				int choice2 = getChoice("Press 1 if you know hypotenuse, 2 if you know the opposite side length", 2);
				if (choice2 == 1) {//Using hypotenuse
					double hyp = getSideLength("hypotenuse");
					println("The Adjacent is " + calc.getAdjCos(angle, hyp));
					out.write("-Calculated an Adjacent of " + calc.getAdjCos(angle, hyp) + " using an angle of " + angle
							+ " degrees " + "with a hypotenuse of " + hyp + "\n");
					end();

				}
				if (choice2 == 2) {//Using Opposite
					double opp = getSideLength("opposite");
					println("The Adjacent is " + calc.getAdjTan(angle, opp));
					out.write("-Calculated an Adjacent of " + calc.getAdjTan(angle, opp) + " using an angle of " + angle
							+ " degrees " + "with an opposite of " + opp + "\n");
					end();
				}
			}
			if (choice1 == 3) {//Solving for hypotenuse
				int choice2 = getChoice(
						"Press 1 if you know Adjacent Side Length, 2 if you know the Hypotenuse side length", 2);
				if (choice2 == 1) {//Using adjacent
					double adj = getSideLength("adjacent");
					println("The Opposite is " + calc.getOppTan(angle, adj));
					if (isDegrees) {
						out.write("-Calculated an Opposite of " + calc.getOppTan(angle, adj) + " using an angle of "
								+ angle + " degrees " + "with an adjacent of " + adj + "\n");
					} else {
						out.write("-Calculated an Opposite of " + calc.getOppTan(angle, adj) + " using an angle of "
								+ angle + " radians " + "with an adjacent of " + adj + "\n");
					}
					end();

				}
				if (choice2 == 2) {//Using hypotenuse
					double hyp = getSideLength("hypotenuse");
					println("The Opposite is " + calc.getOppSin(angle, hyp));
					if (isDegrees) {
						out.write("-Calculated an Opposite of " + calc.getOppSin(angle, hyp) + " using an angle of "
								+ angle + " degrees " + "with a hypotenuse of " + hyp + "\n");
					} else {
						out.write("-Calculated an Opposite of " + calc.getOppSin(angle, hyp) + " using an angle of "
								+ angle + " radians " + "with a hypotenuse of " + hyp + "\n");
					}
					end();

				}
			}
		}
		if (choice0 == 2) {//Solving for an angle
			int choice2 = getChoice("Press 1 if You know the hypotenuse, press 2 if you do not know the hypotenuse", 2);
			if (choice2 == 1) {//When hypotenuse and one other side are known
				double hyp = getSideLength("hypotnesue");
				int choice3 = getChoice("Press 1 if you know the adjacent, press 2 if you know the opposite", 2);
				if (choice3 == 1) {//Adjacent is known
					double adj = getSideLength("adjacent");
					if (isDegrees) {
						println("The Angle is " + calc.getAngCos(adj, hyp) + " Degrees");
						out.write("-Calculated an angle of " + calc.getAngCos(adj, hyp) + " degrees, "
								+ " using an adjacent of " + adj + " with a hypotenuse of " + hyp + "\n");
					} else {
						println("The Angle is " + calc.getAngCos(adj, hyp) + " Radians");
						out.write("-Calculated an angle of " + calc.getAngCos(adj, hyp) + " radians, "
								+ " using an adjacent of " + adj + " with a hypotenuse of " + hyp + "\n");
					}
					end();

				}
				if (choice3 == 2) {//Opposite is known
					double opp = getSideLength("opposite");
					if (isDegrees) {
						println("The Angle is " + calc.getAngSin(opp, hyp) + " Degrees");
						out.write("-Calculated an angle of " + calc.getAngSin(opp, hyp) + " degrees, "
								+ " using an opposite of " + opp + " with a hypotenuse of " + hyp + "\n");
					} else {
						println("The Angle is " + calc.getAngSin(opp, hyp) + " Radians");
						out.write("-Calculated an angle of " + calc.getAngSin(opp, hyp) + " radians, "
								+ " using an opposite of " + opp + " with a hypotenuse of " + hyp + "\n");
					}
					end();
				}
			}
			if (choice2 == 2) {//when adjacent and opposite are known
				double adj = getSideLength("adjacent");
				double opp = getSideLength("opposite");
				if (isDegrees) {
					println("The Angle is " + calc.getAngTan(opp, adj) + " Degrees");
					out.write("-Calculated an angle of " + calc.getAngTan(opp, adj) + " degrees, "
							+ " using an opposite of " + opp + "with an adjacent of " + adj + "\n");
				} else {
					println("The Angle is " + calc.getAngTan(opp, adj) + " Radians");
					out.write("-Calculated an angle of " + calc.getAngTan(opp, adj) + " radians, "
							+ " using an opposite of " + opp + "with an adjacent of " + adj + "\n");
				}
				end();
			}

		}

	}

	static void nonRightTriangleCalc() throws IOException {
		boolean useSin = isYes("Do you want to use Sine law (Yes, or No), no will allow you to use cosine law");
		if (useSin) {//Use sine
			//Gets pair of corresponding side and angle
			double angle1 = getAngle("Please Enter the angle opposite to the known side length");
			double side1 = getSideLength("Known");
			int getAngleOrSide = getChoice(
					"Press 1 if you want to solve for an angle, Press 2 if you want to solve a side length", 2);
			if (getAngleOrSide == 1) {//Solving for angle
				double side2 = getSideLength("Opposite");
				if (isDegrees) {
					println("The angle is " + calc.getAngleSin(side1, angle1, side2) + " degrees");
					out.write("-Calculated an angle of  " + calc.getAngleSin(side1, angle1, side2)
							+ " degrees, opposite to the side of length " + side2 + " with the pair of " + angle1
							+ " degrees " + "and the side of length " + side1 + "\n");
					end();
				} else {
					println("The angle is " + calc.getAngleSin(side1, angle1, side2) + " radians");
					out.write("-Calculated an angle of  " + calc.getAngleSin(side1, angle1, side2)
							+ " radians, opposite to the side of length " + side2 + " with the pair of " + angle1
							+ " radians " + "and the side of length " + side1 + "\n");
					end();
				}

			}
			if (getAngleOrSide == 2) {//Solving for 
				double angle2 = getAngle("Please Enter the angle opposite to the side you want to solve for");
				println("The side length is " + calc.getSideSin(side1, angle1, angle2));
				out.write("-Calculated a side length of " + calc.getSideSin(side1, angle1, angle2)
						+ " opposite to the angle of " + angle2 + " with the pair of " + angle1 + " degrees "
						+ "and the side of length " + side1 + "\n");
				end();
			}
		} else if (!useSin) {// Use Cosine
			int getAngleOrSide = getChoice(
					"Press 1 if you want to solve for an angle, Press 2 if you want to solve a side length", 2);

			if (getAngleOrSide == 1) {
				double sideC = getSideLength("opposite");
				double sideA = getSideLength("other");
				double sideB = getSideLength("third");
				if (isDegrees) {
					println("The angle is " + calc.getAngleCos(sideA, sideB, sideC) + " degrees ");
					out.write("-Calculated an angle of " + calc.getAngleCos(sideA, sideB, sideC) + " degrees "
							+ "using sides " + sideC + " , " + sideB + " , " + sideA + "\n") ;
					end();
				} else {
					println("The angle is " + calc.getAngleCos(sideA, sideB, sideC) + " radians ");
					out.write("-Calculated an angle of " + calc.getAngleCos(sideA, sideB, sideC) + " radians "
							+ "using sides " + sideC + " , " + sideB + " , " + sideA + "\n");
					end();

				}

			}
			if (getAngleOrSide == 2) {
				double angle = getAngle("Please Enter the known angle");
				double sideA = getSideLength("first");
				double sideB = getSideLength("second");
				println("The side length is " + calc.getSideCos(sideA, sideB, angle));
				out.write("-Calculated a side length of " + calc.getSideCos(sideA, sideB, angle)
						+ "opposite to the angle " + angle + " with sides of length " + sideA + " and length " + sideB + "\n");
				end();
			}

		}
	}
}

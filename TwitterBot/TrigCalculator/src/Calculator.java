
public class Calculator {
	
	int decVal;//Int used to store decimal value
	boolean isDegrees;
	
	public Calculator(int _decVal, boolean _isDegrees) {//Constructor sets decimal Value and angle mode
		decVal = _decVal;
		isDegrees = _isDegrees; 
	}
	
	//Methods Used to Return Side Lengths
	public double getHypSin(double angle, double opp) {//Returns hypotenuse when angle and opposite are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}
		return roundVal ((opp/(Math.sin(angle))));
	}
	public double getOppSin(double angle, double hyp) {//Returns opposite when angle and Hypotenuse are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}		
		return roundVal (hyp * Math.sin(angle));
	}
	public double getHypCos(double angle, double adj) {//Returns Hypotenuse When angle and adjacent are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}	
		return roundVal(adj/(Math.cos(angle)));
	}
	public double getAdjCos(double angle, double hyp) {//Returns adjacent When angle and Hypotenuse are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}	
		return roundVal(hyp * Math.cos(angle));
	}
	public double getOppTan(double angle, double adj) {//Returns Opposite when angle and adjacent are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}	
		return roundVal (adj*(Math.tan(angle)));
	}
	public double getAdjTan(double angle, double opp) {//Returns adjacent when angle and Opposite are known
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}	
		return roundVal (opp/(Math.tan(angle)));
	}
	//Methods Used to Return Angles
	public double getAngSin(double opp, double hyp) {//Returns angle when Hypotenuse and Opposite are known
		if (isDegrees) {
			return roundVal (Math.toDegrees(Math.asin(opp/hyp)));
		}
		else {
			return roundVal ((Math.asin(opp/hyp)));
		}
		
	}
	public double getAngCos(double adj, double hyp) {//Returns angle when Adjacent and Hypotenuse are known
		if (isDegrees) {
			return roundVal (Math.toDegrees(Math.acos(adj/hyp)));
		}
		else {
			return roundVal ((Math.acos(adj/hyp)));
		}
	}
	public double getAngTan(double opp, double adj) {//Returns angle when Opposite and Adjacent are known
		if (isDegrees) {
			return roundVal (Math.toDegrees(Math.atan(opp/adj)));
		}
		else {
			return roundVal ((Math.atan(opp/adj)));
		}
		
	}
	//Methods for non right triangles
	public double getSideSin(double side1, double angle1, double angle2) {//Calculate a side length using sine law
		if (isDegrees) {
			angle1 = Math.toRadians(angle1);
			angle2 = Math.toRadians(angle2);
		}
		angle1 = Math.sin(angle1);
		angle2 = Math.sin(angle2);
	 	return roundVal (side1*(angle2/angle1));
	}
	
	public double getAngleSin(double side1, double angle1, double side2) {//calculates an angle using sine law
		if (!isDegrees) {
			angle1 = Math.toRadians(angle1);
		}
		angle1 = Math.sin(angle1);
		if (isDegrees) {
			return roundVal (Math.toDegrees (Math.asin(((side2/side1) * angle1))));
		}
		else {
			return roundVal ((Math.asin(((side2/side1) * angle1))));
		}
	}
	
	public double getAngleCos(double a, double b, double c) {//Calculate an angle using cos law 
		double angle = (Math.acos ((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2))/(2*a*b)));
		if (isDegrees) {
			return roundVal (Math.toDegrees(angle));
		}
		else {
			return roundVal (angle);
		}
		
	}
	
	public double getSideCos(double b, double c, double angle) {//Calculates a side using cos law
		if (isDegrees) {
			angle = Math.toRadians(angle);
		}
		return roundVal(Math.sqrt(Math.pow(b, 2) + Math.pow(c, 2) - 2* b* c * Math.cos(angle)));
	}
	
	//Linear Methods
	//**This method is not used but its still pretty interesting**
	public boolean checkIntersect(double m1, double b1, double m2, double b2, int startVal) {//Checks for point of intersection on every possible integer java can store
		int numPoints = startVal;
		double line1[];
		double line2[];
		try {
			line1 = new double [numPoints];
			line2 = new double [numPoints];
		}
		catch (OutOfMemoryError E) {
			System.out.println("Out of Memmory");
			return false;
		}
		for (int i = -(numPoints/2); i< (numPoints/2); i++) {
			line1 [i + numPoints/2] = (m1*i)+b1;
			line2 [i + numPoints/2] = (m2*i)+b2;
		}
		for (int i = 0; i<numPoints; i++) {
			if (line1 [i] == line2[i]) {
				double x = -numPoints/2 + i;
				double y = line1 [i];
				System.out.println(x+", " + y);
				return true;
			}
		}
		return checkIntersect(m1, b1, m2, b2, numPoints*=10);
				
	}


	
	private double roundVal (double val) {
		val = Math.round(val * Math.pow(10, decVal))/Math.pow(10, decVal);
		return val;
	}
	

}

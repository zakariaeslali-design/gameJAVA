package org.example;

public record PolarPosition(double rayon, double angle) {
	public double toCartesianX() {
		return rayon*Math.cos(angle);
	}
	public double toCartesianY() {
		return rayon*Math.sin(angle);
	}
	public double distanceTo(PolarPosition other) {
		double x1= this.toCartesianX();
		double y1= this.toCartesianY();
		double x2=other.toCartesianX();
		double y2=other.toCartesianY();
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
		
		
	}
}
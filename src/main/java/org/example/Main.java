package org.example;

//main temporaire pour tester PolarPoistion 
//à modifer par le test game engine
public class Main {
    public static void main(String[] args) {
        PolarPosition p1 = new PolarPosition(10, 0);
        System.out.println("angle=0, radius=10 -> x=" + p1.toCartesianX() + ", y=" + p1.toCartesianY());

        PolarPosition p2 = new PolarPosition(10, Math.PI / 2);
        System.out.println("angle=90°, radius=10 -> x=" + p2.toCartesianX() + ", y=" + p2.toCartesianY());

        System.out.println("distance entre p1 et p2 = " + p1.distanceTo(p2));
    }
}
package by.Danefka.Calculations;

import by.Danefka.Solids.Polyhedron;
import by.Danefka.Solids.PolyhedronClipper;


public class CalculateForces {
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    public double[] calculate(Polyhedron p) {
        Polyhedron pClip = PolyhedronClipper.clipPolyhedron(p);

        double[] force = {0, 0, -9.8 * p.getMass()};
//        System.out.println("mg = " + -9.8 * p.getMass());
        if (pClip == null) {
            return force;
        }
        force[Z] += (1000 * 9.8 * pClip.getVolume());

//
//        System.out.println("pgv = " + (1000 * 9.8 * pClip.getVolume()));
//        System.out.println();
        return force;
    }
}

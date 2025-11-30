package by.Danefka.Calculations;

import by.Danefka.Solids.Polyhedron;
import by.Danefka.Solids.PolyhedronClipper;

public class CalculateMomentOfForce {
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;


    public double[] calculate(Polyhedron p){
        Polyhedron pClip = PolyhedronClipper.clipPolyhedron(p);
        double[] archimedesForce = {0,0, pClip.getVolume()*10*1000};

        double[] cp = p.getCenterMass();
        double[] cpClip = pClip.getCenterMass();

        return new double[]{cpClip[X] - cp[X], cpClip[Y] - cp[Y], cpClip[Z] - cp[Z]};
    }
}

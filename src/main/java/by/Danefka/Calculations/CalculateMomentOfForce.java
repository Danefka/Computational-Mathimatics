package by.Danefka.Calculations;

import by.Danefka.Solids.Polyhedron;
import by.Danefka.Solids.PolyhedronClipper;
import by.Danefka.Utils.VectorUtils;

public class CalculateMomentOfForce {
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;


    public double[] calculate(Polyhedron p){
        Polyhedron pClip = PolyhedronClipper.clipPolyhedron(p);
        if (pClip == null) return new double[]{0,0,0};
        double[] archimedesForce = {0,0, 1000*9.8 * pClip.getVolume()};

        double[] cp = p.getCenterMass();
        double[] cpClip = pClip.getCenterMass();

        double[] r = new double[]{cpClip[X] - cp[X], cpClip[Y] - cp[Y], cpClip[Z] - cp[Z]};
        return VectorUtils.crossProduct(archimedesForce,r);
    }
}

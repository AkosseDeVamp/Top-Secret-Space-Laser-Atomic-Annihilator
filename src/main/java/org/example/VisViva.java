package org.example;

import static java.lang.Math.sqrt;

public class VisViva {
    //Some functions to assist the process
    //Just adds the body radius to a sea level altitude.
    public static double radiusFromBodyCentre(OrbitBody body, double currentAlt) {
        return body.bodyRadius + currentAlt;
    }

    //The main vis-viva equation, converts the current altitude at current orbit into current orbital velocity. Takes into account if from sea level.
    public static double findVelocity(Keplerian kepler, double currentAlt, boolean isFromSeaLevel) {
        double currentRadius;

        // Add the average body radius if from sea level
        if (isFromSeaLevel == true) {
            currentRadius = radiusFromBodyCentre(kepler.orbitBody, currentAlt);
        }
        // Or don't if it isn't
        else {
            currentRadius = currentAlt;
        }

        //Then return the velocity from the VisViva formula (metres per second)
        return sqrt(kepler.orbitBody.bodyMu * ((2 / currentRadius) - (1 / kepler.semiMajorAxis)));
    }

    //Method that finds a transfer orbit between two orbits.
    //It will always attempt to achieve the lower periapsis first to maximise the Oberth effect.
    //Aka velocity changes more effective the faster (lower) you go.
    //Method returns Keplerian orbit parameters.
    public static Keplerian findTransferOrbit(Keplerian initialOrbit, Keplerian finalOrbit) {
        // Because this references the keplerian which is true radius from centre of the body, isFromSeaLevel = false
        Keplerian transferOrbit = new Keplerian();
        // Don't forget to initialise the orbitbody
        transferOrbit.setOrbitBody(initialOrbit.orbitBody);

        //If the first orbit has a higher periapsis than the second, the first burn will be getting to that lower (more efficient) periapsis
        if (initialOrbit.periapsis > finalOrbit.periapsis) {
            double transferApoapsis = initialOrbit.apoapsis;
            double transferPeriapsis = finalOrbit.periapsis;
            transferOrbit.setEllipticalsFromApses(transferPeriapsis, transferApoapsis, false);
            return transferOrbit;
        }

        //Otherwise the first burn will be from the initial periapsis.
        else {
            double transferApoapsis = finalOrbit.apoapsis;
            double transferPeriapsis = initialOrbit.periapsis;
            transferOrbit.setEllipticalsFromApses(transferPeriapsis, transferApoapsis, false);
            return transferOrbit;
        }
    }
}

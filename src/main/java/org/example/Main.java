package org.example;

import static java.lang.Math.*;

// Class for the body being orbited
class OrbitBody {
    double bodyMu; //The standard gravitational parameter for the body orbited
    double bodyRadius; //The average radius of the body being orbited (in metres)

    //Setter for the SGP.
    public void setBodyMu(double newMuValue) {
        bodyMu = newMuValue;
    }

    //Setter for the average body radius
    public void setBodyRadius(double newRadiusValue) {
        bodyRadius = newRadiusValue;
    }
}

// Class defining the elements of the orbit
class Keplerian {
    //Orbital body being orbited
    OrbitBody orbitBody;

    // ELLIPTICAL
    double eccentricity; //Dimensionless
    double semiMajorAxis; //In metres
    // PLANAR
    double inclination; //in RADIANS
    double longitudeAscendingNode; //in RADIANS
    // POSITION
    double argumentOfPeriapsis; //in RADIANS
    double trueAnomaly; //in Radians
    // The PLANAR and POSITION parameters are here to complete the Keplerian elements but serve no current use


    // APSES (all in metres), these are all technically part of the ELLIPTICAL parameters, but useful to have defined.
    double apoapsis;
    double periapsis;
    double apoapsisFromSeaLevel; //taking the average radius of the body as sea level.
    double periapsisFromSeaLevel;

    //Setter for the orbital body
    public void setOrbitBody(OrbitBody body) {
        orbitBody = body;
    }

    //Setter for the ELLIPTICAL and APSES from given apses altitudes, taking into account whether given apses are from sea level or not
    public void setEllipticalsFromApses(double altitude1, double altitude2, boolean isFromSeaLevel) {
        double radius1;
        double radius2;

        // Adds the orbital body radius if the given altitude is above sea level
        if (isFromSeaLevel == true) {
            radius1 = altitude1 + orbitBody.bodyRadius;
            radius2 = altitude2 + orbitBody.bodyRadius;
        }

        // If the altitude is from the centre of the orbital body
        else {
            radius1 = altitude1;
            radius2 = altitude2;
        }

        // Set the apoapsis and periapsis
        apoapsis = max(radius1, radius2);
        periapsis = min(radius1, radius2);
        // Set the sea level altitudes for the apoapsis and periapsis
        apoapsisFromSeaLevel = apoapsis - orbitBody.bodyRadius;
        periapsisFromSeaLevel = periapsis - orbitBody.bodyRadius;
        // Set the ELLIPTICAL elements
        eccentricity = (apoapsis - periapsis) / (apoapsis + periapsis);
        semiMajorAxis = (apoapsis + periapsis) / 2;
    }

}

class VisViva {
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

public class Main {
    public static void main(String[] args) {

        //Define parameters for Earth
        OrbitBody earth = new OrbitBody();
        earth.setBodyMu(3.986004418E14);
        earth.setBodyRadius(6371E3);

        //Get the Altitudes of desired orbits in metres.
        double[] initialAltitudes = {170E3, 250E3};
        double[] finalAltitudes = {300E3, 8000E3};

        //Initialise the Keplarians for each orbit
        Keplerian initialOrbit = new Keplerian();
        Keplerian finalOrbit = new Keplerian();

        //Set the Keplerian to be orbiting Earth;
        initialOrbit.setOrbitBody(earth);
        finalOrbit.setOrbitBody(earth);

        //Set the Ellipticals for Each, remember that isFromSeaLevel is true for the altitudes
        initialOrbit.setEllipticalsFromApses(initialAltitudes[0], initialAltitudes[1], true);
        finalOrbit.setEllipticalsFromApses(finalAltitudes[0], finalAltitudes[1], true);

        //Get the Transfer Orbit
        Keplerian transferOrbit = VisViva.findTransferOrbit(initialOrbit, finalOrbit);

        //Printouts to test
        System.out.println("Initial Orbit\t: Periapsis(ASL) - \t" + initialOrbit.periapsisFromSeaLevel + "m\t; Apoapsis(ASL) - " + initialOrbit.apoapsisFromSeaLevel + "m");
        System.out.println("Transfer Orbit\t: Periapsis(ASL) - \t" + transferOrbit.periapsisFromSeaLevel + "m\t; Apoapsis(ASL) - " + transferOrbit.apoapsisFromSeaLevel + "m");
        System.out.println("Initial Orbit\t: Periapsis(ASL) - \t" + finalOrbit.periapsisFromSeaLevel + "m\t; Apoapsis(ASL) - " + finalOrbit.apoapsisFromSeaLevel + "m");


    }
}
package org.example;

// Class for the body being orbited
public class OrbitBody {
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

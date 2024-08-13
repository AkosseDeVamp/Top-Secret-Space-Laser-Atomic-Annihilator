package org.example;

import org.example.application.VisViva;
import org.example.model.Keplerian;
import org.example.model.OrbitBody;

public class Main {
  public static void main(String[] args) {

    // Define parameters for Earth
    OrbitBody earth = new OrbitBody();
    earth.setBodyMu(3.986004418E14);
    earth.setBodyRadius(6371E3);

    // Get the Altitudes of desired orbits in metres.
    double[] initialAltitudes = {170E3, 250E3};
    double[] finalAltitudes = {300E3, 8000E3};

    // Initialise the Keplarians for each orbit
    Keplerian initialOrbit = new Keplerian();
    Keplerian finalOrbit = new Keplerian();

    // Set the Keplerian to be orbiting Earth;
    initialOrbit.setOrbitBody(earth);
    finalOrbit.setOrbitBody(earth);

    // Set the Ellipticals for Each, remember that isFromSeaLevel is true for the altitudes
    initialOrbit.genEllipticalsFromApses(initialAltitudes[0], initialAltitudes[1], true);
    finalOrbit.genEllipticalsFromApses(finalAltitudes[0], finalAltitudes[1], true);

    // Get the Transfer Orbit
    Keplerian transferOrbit = VisViva.findTransferOrbit(initialOrbit, finalOrbit);

    // Printouts to test
    System.out.println(
        "Initial Orbit\t: Periapsis(ASL) - \t"
            + initialOrbit.getPeriapsisFromSeaLevel()
            + "m\t; Apoapsis(ASL) - "
            + initialOrbit.getApoapsisFromSeaLevel()
            + "m");
    System.out.println(
        "Transfer Orbit\t: Periapsis(ASL) - \t"
            + transferOrbit.getPeriapsisFromSeaLevel()
            + "m\t; Apoapsis(ASL) - "
            + transferOrbit.getApoapsisFromSeaLevel()
            + "m");
    System.out.println(
        "Initial Orbit\t: Periapsis(ASL) - \t"
            + finalOrbit.getPeriapsisFromSeaLevel()
            + "m\t; Apoapsis(ASL) - "
            + finalOrbit.getApoapsisFromSeaLevel()
            + "m");
  }
}
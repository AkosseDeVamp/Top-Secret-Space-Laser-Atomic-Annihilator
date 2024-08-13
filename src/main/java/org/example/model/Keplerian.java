package org.example.model;

import static java.lang.Math.max;
import static java.lang.Math.min;

// Class defining the elements of the orbit
public class Keplerian {
  // Orbital body being orbited
  OrbitBody orbitBody;

  // ELLIPTICAL
  double eccentricity; // Dimensionless
  double semiMajorAxis; // In metres
  // PLANAR
  double inclination; // in RADIANS
  double longitudeAscendingNode; // in RADIANS
  // POSITION
  double argumentOfPeriapsis; // in RADIANS
  double trueAnomaly; // in Radians
  // The PLANAR and POSITION parameters are here to complete the Keplerian elements but serve no
  // current use

  // APSES (all in metres), these are all technically part of the ELLIPTICAL parameters, but useful
  // to have defined.
  double apoapsis;
  double periapsis;
  double apoapsisFromSeaLevel; // taking the average radius of the body as sea level.
  double periapsisFromSeaLevel;

  // Setter for the orbital body
  public void setOrbitBody(OrbitBody body) {
    orbitBody = body;
  }

  public OrbitBody getOrbitBody() {
    return orbitBody;
  }

  public double getEccentricity() {
    return eccentricity;
  }

  public void setEccentricity(double eccentricity) {
    this.eccentricity = eccentricity;
  }

  public double getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(double semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  public double getInclination() {
    return inclination;
  }

  public void setInclination(double inclination) {
    this.inclination = inclination;
  }

  public double getLongitudeAscendingNode() {
    return longitudeAscendingNode;
  }

  public void setLongitudeAscendingNode(double longitudeAscendingNode) {
    this.longitudeAscendingNode = longitudeAscendingNode;
  }

  public double getArgumentOfPeriapsis() {
    return argumentOfPeriapsis;
  }

  public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
    this.argumentOfPeriapsis = argumentOfPeriapsis;
  }

  public double getTrueAnomaly() {
    return trueAnomaly;
  }

  public void setTrueAnomaly(double trueAnomaly) {
    this.trueAnomaly = trueAnomaly;
  }

  public double getApoapsis() {
    return apoapsis;
  }

  public void setApoapsis(double apoapsis) {
    this.apoapsis = apoapsis;
  }

  public double getPeriapsis() {
    return periapsis;
  }

  public void setPeriapsis(double periapsis) {
    this.periapsis = periapsis;
  }

  public double getApoapsisFromSeaLevel() {
    return apoapsisFromSeaLevel;
  }

  public void setApoapsisFromSeaLevel(double apoapsisFromSeaLevel) {
    this.apoapsisFromSeaLevel = apoapsisFromSeaLevel;
  }

  public double getPeriapsisFromSeaLevel() {
    return periapsisFromSeaLevel;
  }

  public void setPeriapsisFromSeaLevel(double periapsisFromSeaLevel) {
    this.periapsisFromSeaLevel = periapsisFromSeaLevel;
  }

  // Setter for the ELLIPTICAL and APSES from given apses altitudes, taking into account whether
  // given apses are from sea level or not
  public void generateEllipticalsFromApses(
      double altitude1, double altitude2, boolean isFromSeaLevel) {
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

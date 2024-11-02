package org.example;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a geographical area with latitude, longitude, denomination, and state information.
 */
public class Area implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The latitude of the area. */
    private double latitudine;

    /** The longitude of the area. */
    private double longitudine;

    /** The name or designation of the area. */
    private String denominazione;

    /** The state or country information of the area. */
    private String stato;

    /**
     * Class constructor
     */
    public Area(String denominazione, String stato, double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.denominazione = denominazione;
        this.stato = stato;
    }

    /**
     * Class constructor with all fields set to null
     */
    public Area(){
        this.latitudine = 0;
        this.longitudine = 0;
        this.denominazione = null;
        this.stato = null;
    }

    // metodi getter
    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public String getStato() {
        return stato;
    }

    // metodi setter
    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Converts the attributes of an object of type Area to a string array representation.
     * The string array includes the denomination, state, latitude, and longitude of the object.
     *
     * @return A string array containing the denomination, state, latitude, and longitude of the object.
     */
    public String[] toStringArray(){
        return new String[] {this.denominazione, this.stato, Double.toString(this.latitudine), Double.toString(this.longitudine)};
    }

    /**
     * Checks if the area's attributes contain the specified substring, case-insensitive.
     *
     * @param str The substring to search for within the area's attributes.
     * @return {@code true} if the substring is found in any of the area's attributes,
     *         {@code false} otherwise.
     */
    public boolean contains(String str){
        if (this.denominazione.toLowerCase().contains(str.toLowerCase()))
            return true;
        if (this.stato.toLowerCase().contains(str.toLowerCase()))
            return true;
        if (Double.toString(this.latitudine).contains(str))
            return true;
        return Double.toString(this.longitudine).contains(str);
    }

    /**
     * Checks if the provided geographic coordinates are in the 55Km radius of the area's latitude and longitude.
     *
     * @param crd The array containing the latitude and longitude coordinates to compare with.
     * @return {@code true} if the if the provided geographic coordinates are in the 55Km radius of the area's latitude and longitude,
     * {@code false} otherwise.
     */
    public boolean isClose(double[] crd){
        double latitude = crd[0];
        double longitude = crd[1];
        double radius = 0.5;   //55 Km approximately
        return Math.abs(this.latitudine - latitude) < radius && Math.abs(this.longitudine - longitude) < radius;
    }

    /**
     * Checks if the area is located at the provided coordinates.
     *
     * @param crd The coordinates to compare with the area's latitude and longitude.
     * @return {@code true} if the area's coordinates match the provided coordinates,
     *         {@code false} otherwise.
     */
    public boolean isAtCoordinates(double[] crd){
        return this.latitudine == crd[0] && this.longitudine == crd[1];
    }


    @Override
    public String toString() {
        return "AreaInteresse{" +
                "latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                ", denominazione='" + denominazione + '\'' +
                ", stato='" + stato + '\'' +
                '}';
    }

    /**
     * Compares this Area object to the specified object for equality.
     *
     * @param o The object to compare with.
     * @return {@code true} if the compared areas have the same attributes, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Area))
            return false;

        Area area = (Area) o;

        if(area.getLongitudine() != this.longitudine)
            return false;
        if(area.getLatitudine() != this.latitudine)
            return false;
        if(!area.getDenominazione().equals(this.denominazione))
            return false;
        return area.getStato().equals(this.stato);
    }
}
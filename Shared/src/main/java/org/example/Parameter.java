package org.example;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a parameter observation with its associated information, such as category, score, notes, date,
 * area, and monitoring center. Instances of this class can be serialized for storage and retrieval.
 */
public class Parameter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Enumeration of parameter categories, including wind, humidity, pressure, temperature, precipitation,
     * and other related categories.
     */
    public enum Category{
        VENTO, UMIDITA, PRESSIONE, TEMPERATURA, PRECIPITAZIONE, ADG, MDG
    }

    /** The category of the parameter. */
    private final Category category;

    /** The score associated with the parameter. */
    private final int score;

    /** Additional notes for the parameter observation. */
    private final String notes;

    /** The date of the parameter observation. */
    private final Date date;

    /** The geographical area associated with the parameter observation. */
    private final Area area;

    /** The monitoring center associated with the parameter observation. */
    private MonitoringCenter mc;

    /**
     * Constructs a Parameter instance with the provided category, score, notes, date, area, and monitoring center.
     *
     * @param category The category of the parameter.
     * @param score The score associated with the parameter.
     * @param notes Additional notes for the parameter observation.
     * @param date The date of the parameter observation.
     * @param area The geographical area associated with the parameter observation.
     * @param mc The monitoring center associated with the parameter observation.
     */
    public Parameter(Category category, int score, String notes, Date date, Area area, MonitoringCenter mc){
        this.category = category;
        this.score = score;
        this.notes = notes;
        this.date = date;
        this.area = area;
        this.mc = mc;
    }

    /*
    public Parameter(Category category, int score, String notes, Date date, Area area){
        this.category = category;
        this.score = score;
        this.notes = notes;
        this.date = date;
        this.area = area;
    }*/

    //Getters
    /**
     * Retrieves the area associated with this parameter.
     *
     * @return The associated geographical area.
     */
    public Area getArea() {
        return this.area;
    }

    /**
     * Retrieves the category of the parameter.
     *
     * @return The category of the parameter.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Retrieves the score associated with the parameter.
     *
     * @return The score of the parameter.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Retrieves the additional notes for the parameter observation.
     *
     * @return Additional notes for the parameter observation.
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Retrieves the date of the parameter observation.
     *
     * @return The date of the parameter observation.
     */
    public Date getDate() {
        return this.date;
    }
    public MonitoringCenter getMc(){
        return this.mc;
    }

    /**
     * Returns an array of string representations of parameter categories.
     *
     * @return A string array containing the names of parameter categories.
     */
    public static String[] getCategoryNames() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category c : Category.values()){
            categories.add(c.toString());
        }
        return categories.toArray(new String[0]);
    }

    /**
     * Converts the parameter into a string array representation.
     *
     * @return A string array containing the parameter's attributes.
     */
    public String[] toStringArray(){
        return new String[] {this.category.toString(), Integer.toString(this.score), new SimpleDateFormat("dd-MM-yyyy").format(this.date), this.notes};
    }

}

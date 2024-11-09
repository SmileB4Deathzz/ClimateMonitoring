package org.example;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a monitoring center with a name, address, and a collection of associated areas.
 * Instances of this class can be serialized for storage and retrieval.
 */
public class MonitoringCenter implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The name of the monitoring center. */
    private final String nome;

    /** The address of the monitoring center. */
    private final String indirizzo;

    /** The list of areas associated with the monitoring center. */
    private ArrayList<Area> areas;


    /**
     * Constructs a MonitoringCenter instance with the provided name, address, and areas.
     *
     * @param nome      The name of the monitoring center.
     * @param indirizzo The address of the monitoring center.
     * @param areas     The list of areas associated with the monitoring center.
     */
    public MonitoringCenter(String nome, String indirizzo, ArrayList<Area> areas) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.areas = areas;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Retrieves the list of associated areas for the monitoring center.
     *
     * @return The list of associated areas.
     */
    public ArrayList<Area> getAreas() {
        return areas;
    }

    /**
     * Adds a list of areas to the monitoring center
     * @param areas the list of areas to add to the monitoring center
     */
    public void addAreas(ArrayList<Area> areas) {
        if (this.areas == null)
            this.areas = areas;
        else
            this.areas.addAll(areas);
        //new MCManager().saveMCenter(this);
    }

    /**
     * Checks if a monitoring center contains the specified area
     *
     * @param a the area to search for
     * @return  {@code true} if the monitoring contains the provided area,
     * {@code false} otherwise
     *
     */
    public boolean containsArea(Area a) {
        if (areas == null)
            return false;
        for (Area area : areas) {
            if (area.equals(a))
                return true;
        }
        return false;
    }

}

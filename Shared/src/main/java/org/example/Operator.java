package org.example;

import java.io.Serializable;

/**
 * Represents an operator with personal information, user credentials, and an optional associated monitoring center.
 * Instances of this class can be serialized for storage and retrieval.
 */
public class Operator implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The first name of the operator. */
    private final String nome;

    /** The last name of the operator. */
    private final String cognome;

    /** The unique identifier of the operator. */
    private final String codiceFiscale;

    /** The email address of the operator. */
    private final String email;

    /** The user ID of the operator for authentication. */
    private final String userId;

    /** The password of the operator for authentication. */
    private final String password;

    /** The associated monitoring center of the operator. */
    private MonitoringCenter monitoringCenter;

    /**
     * Constructs an Operator instance with personal information, user credentials, and an associated monitoring center.
     *
     * @param userId           The user ID of the operator for authentication.
     * @param nome             The first name of the operator.
     * @param cognome          The last name of the operator.
     * @param codiceFiscale    The unique identifier of the operator.
     * @param email            The email address of the operator.
     * @param password         The password of the operator for authentication.
     * @param monitoringCenter The associated monitoring center of the operator.
     */
    public Operator(String userId, String nome, String cognome, String codiceFiscale, String email, String password, MonitoringCenter monitoringCenter) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.monitoringCenter = monitoringCenter;
    }

    /**
     * Retrieves the user ID of the operator.
     *
     * @return The user ID of the operator.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the password of the operator.
     *
     * @return The password of the operator.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the associated monitoring center of the operator.
     *
     * @return The associated monitoring center of the operator.
     */
    public MonitoringCenter getCentroMonitoraggio() {
        return monitoringCenter;
    }

    /**
     * Sets the associated monitoring center of the operator.
     *
     * @param monitoringCenter The monitoring center to associate with the operator.
     */
    public void setCentroMonitoraggio(MonitoringCenter monitoringCenter) {
        this.monitoringCenter = monitoringCenter;
    }

    /**
     * Returns a string representation of the operator's details.
     *
     * @return A formatted string containing operator information.
     */
    @Override
    public String toString() {
        return "ClimateMonitor.Operatore{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                ", email='" + email + '\'' +
                ", userId=" + userId +
                ", password='" + password + '\'' +
                ", centroMonitoraggio='" + monitoringCenter + '\'' +
                '}';
    }
}

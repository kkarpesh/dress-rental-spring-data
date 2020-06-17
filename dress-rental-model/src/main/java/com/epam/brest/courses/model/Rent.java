package com.epam.brest.courses.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents a rent model from the database.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "RENT")
public class Rent {

    /**
     * The rent ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Integer rentId;

    /**
     * The client.
     */
    @Column(name = "client")
    private String client;

    /**
     * The rent date.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "rent_date")
    private LocalDate rentDate;

    /**
     * The dress.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dress_id", nullable = false)
    private Dress dress;

    /**
     * Constructor without params.
     */
    public Rent(){
    }

    /**
     * Gets the rent ID.
     *
     * @return the rent ID.
     */
    public Integer getRentId() {
        return rentId;
    }

    /**
     * Sets the rent ID.
     *
     * @param rentId A Integer containing the rent ID.
     */
    public void setRentId(Integer rentId) {
        this.rentId = rentId;
    }

    /**
     * Gets the client.
     *
     * @return the client.
     */
    public String getClient() {
        return client;
    }

    /**
     * Sets the client.
     *
     * @param client A String containing the client.
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * Gets the rent date.
     *
     * @return the rent date.
     */
    public LocalDate getRentDate() {
        return rentDate;
    }

    /**
     * Sets the rent date.
     *
     * @param rentDate A LocalDate containing the rent date.
     */
    public void setRentDate(LocalDate rentDate) {
        this.rentDate = rentDate;
    }

    /**
     * Gets the dress.
     *
     * @return the dress.
     */
    public Dress getDress() {
        return dress;
    }

    /**
     * Sets the dress.
     *
     * @param dress a dress..
     */
    public void setDress(Dress dress) {
        this.dress = dress;
    }

    /**
     * Outputs this rent as a String.
     *
     * @return a string representation of this rent.
     */
    @Override
    public String toString() {
        return "Rent{"
                + "rentId=" + rentId
                + ", client='" + client + '\''
                + ", rentDate=" + rentDate
                + ", dress=" + dress+ '}';
    }
}

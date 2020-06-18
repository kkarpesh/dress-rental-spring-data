package com.epam.brest.courses.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Represents a dress model from the database.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "DRESS")
public class Dress {

    /**
     * The dress ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dress_id")
    private Integer dressId;

    /**
     * The dress name.
     */
    @Column(name = "dress_name")
    private String dressName;

    /**
     * The rents.
     */
    @OneToMany(mappedBy = "dress", fetch = FetchType.LAZY)
    private Set<Rent> rents;

    /**
     * Constructor without params.
     */
    public Dress() {
    }

    /**
     * Gets the dress ID.
     *
     * @return the dress ID.
     */
    public Integer getDressId() {
        return dressId;
    }

    /**
     * Sets the dress ID.
     *
     * @param dressId A Integer containing the dress ID.
     */
    public void setDressId(Integer dressId) {
        this.dressId = dressId;
    }

    /**
     * Gets the dress name.
     *
     * @return the dress name.
     */
    public String getDressName() {
        return dressName;
    }

    /**
     * Sets the dress name.
     *
     * @param dressName A String containing the dress name.
     */
    public void setDressName(String dressName) {
        this.dressName = dressName;
    }

    /**
     * Gets the rents.
     *
     * @return the rents.
     */
    public Set<Rent> getRents() {
        return rents;
    }

    /**
     * Sets the rents.
     *
     * @param rents a set of rents.
     */
    public void setRents(Set<Rent> rents) {
        this.rents = rents;
    }

    /**
     * Outputs this dress as a String.
     *
     * @return a string representation of this dress.
     */
    @Override
    public String toString() {
        return "Dress{"
                + "dressId=" + dressId
                + ", dressName='" + dressName + '\'' + '}';
    }
}

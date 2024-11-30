package com.harshi_solution.inventorymate.entities;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "party")
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;
    
    @Column(unique = true)
    @Length(min=3, max=30, message="partyName should be min 3 and max 30")
    @NotBlank(message="partyName is mandatory")
    private String partyName;
    
    @Length(min=3, max=30, message="partyLocation should be min 3 and max 30")
    @NotBlank(message="partyLocation is mandatory")
    private String partyLocation;

    // Constructors, getters, and setters

    public Party() {
        // Default constructor
    }

    public Party(String partyName, String partyLocation) {
        this.partyName = partyName;
        this.partyLocation = partyLocation;
    }

    // Getters and setters

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLocation() {
        return partyLocation;
    }

    public void setPartyLocation(String partyLocation) {
        this.partyLocation = partyLocation;
    }


    // Other methods if needed

    @Override
    public String toString() {
        return "Party{" +
                "partyId=" + partyId +
                ", partyName='" + partyName + '\'' +
                ", partyLocation='" + partyLocation + '\'' +
                '}';
    }
}

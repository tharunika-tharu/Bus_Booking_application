package com.bus.booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int noOfPersons;
    private Double totalCalculated;
    private String filterDate;
    private String toDestination;
    private String fromDestination;
    private String busName;
    private int userId;
    private String time;
    private String fileName;
    private boolean tripStatus = false ;
    private String seatNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public Double getTotalCalculated() {
        return totalCalculated;
    }

    public void setTotalCalculated(Double totalCalculated) {
        this.totalCalculated = totalCalculated;
    }

    public String getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(String filterDate) {
        this.filterDate = filterDate;
    }

    public String getToDestination() {
        return toDestination;
    }

    public void setToDestination(String toDestination) {
        this.toDestination = toDestination;
    }

    public String getFromDestination() {
        return fromDestination;
    }

    public void setFromDestination(String fromDestination) {
        this.fromDestination = fromDestination;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(boolean tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }
}
package org.dm.transit.controller.request;

import me.math.Vertex;
import org.dm.transit.controller.DataMiningAlgoritms;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.List;

public class Parameters {

    private String name;
    private DataMiningAlgoritms algoritm;
    private List<String> agencies;
    private Date startDate;
    private Date endDate;
    private double gridSpaceInMeters;
    private Vertex upperLeftGrid;
    private Vertex lowerRightGrid;
    private GeoShape shape;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataMiningAlgoritms getAlgoritm() {
        return algoritm;
    }

    public void setAlgoritm(DataMiningAlgoritms algoritm) {
        this.algoritm = algoritm;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) {
        this.agencies = agencies;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getGridSpaceInMeters() {
        return gridSpaceInMeters;
    }

    public void setGridSpaceInMeters(double gridSpaceInMeters) {
        this.gridSpaceInMeters = gridSpaceInMeters;
    }

    public Vertex getUpperLeftGrid() {
        return upperLeftGrid;
    }

    public void setUpperLeftGrid(Vertex upperLeftGrid) {
        this.upperLeftGrid = upperLeftGrid;
    }

    public Vertex getLowerRightGrid() {
        return lowerRightGrid;
    }

    public void setLowerRightGrid(Vertex lowerRightGrid) {
        this.lowerRightGrid = lowerRightGrid;
    }

    public GeoShape getShape() {
        return shape;
    }

    public void setShape(GeoShape shape) {
        this.shape = shape;
    }

    public Geometry grid() {
        return null;
    }

}


package org.dm.transit.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.datamining.BandWidthTypes;
import me.datamining.DensityKernelTypes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.Color;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "dm_userPreferences")
public class UserPreferences implements Serializable
{

    @Id
    @Column(name = "UP_UUID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator( name = "native", strategy = "native")
    private long uuid = -1;

    @Column(name = "USER", nullable = false)
    private String user;

    @Column(name = "COLOR", nullable = false)
    private Color addRangeColor = Color.blue;

    @Column(name = "ALPHA_VALUE", nullable = false)
    private int addAlphaValue = 20;
    @Column(name = "CLUSTER_HI_RANGE", nullable = false)
    private int clusterHiRange = Integer.MAX_VALUE;
    @Column(name = "CLUSTER_LOW_RANGE", nullable = false)
    private int clusterLowRange = 0;

    @Column(name = "CLUSTER_CONFIDENCE", nullable = false)
    private double clusterConfidence = 0.5;
    @Column(name = "CLUSTER_DENSITY", nullable = false)
    private double clusterDensity = 1;

    @Column(name = "KDE_BANDWIDTH", nullable = false)
    private BandWidthTypes bandWidth = null;

    @Column(name = "KDE_DENSITY_KERNAL", nullable = false)
    private DensityKernelTypes denstiyKernal = null;

    @JsonGetter("uuid")
    public long getUuid() {
        return uuid;
    }

    @JsonSetter("uuid")
    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    @JsonGetter("user")
    public String getUser() {
        return user;
    }

    @JsonSetter("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonGetter("rangeColor")
    public Color getAddRangeColor() {
        return addRangeColor;
    }

    @JsonSetter("rangeColor")
    public void setAddRangeColor(Color addRangeColor) {
        this.addRangeColor = addRangeColor;
    }

    @JsonGetter("alphaValue")
    public int getAddAlphaValue() {
        return addAlphaValue;
    }

    @JsonSetter("alphaValue")
    public void setAddAlphaValue(int addAlphaValue) {
        this.addAlphaValue = addAlphaValue;
    }

    @JsonGetter("clusterHiRange")
    public int getClusterHiRange() {
        return clusterHiRange;
    }

    @JsonSetter("clusterHiRange")
    public void setClusterHiRange(int clusterHiRange) {
        this.clusterHiRange = clusterHiRange;
    }

    @JsonGetter("clusterLowRange")
    public int getClusterLowRange() {
        return clusterLowRange;
    }

    @JsonSetter("clusterLowRange")
    public void setClusterLowRange(int clusterLowRange) {
        this.clusterLowRange = clusterLowRange;
    }

    @JsonGetter("clusterConfidence")
    public double getClusterConfidence() {
        return clusterConfidence;
    }

    @JsonSetter("clusterConfidence")
    public void setClusterConfidence(double clusterConfidence) {
        this.clusterConfidence = clusterConfidence;
    }

    @JsonGetter("clusterDensity")
    public double getClusterDensity() {
        return clusterDensity;
    }

    @JsonSetter("clusterDensity")
    public void setClusterDensity(double clusterDensity) {
        this.clusterDensity = clusterDensity;
    }

    @JsonGetter("bandWidth")
    public BandWidthTypes getBandWidth() {
        return bandWidth;
    }

    @JsonSetter("bandWidth")
    public void setBandWidth(BandWidthTypes bandWidth) {
        this.bandWidth = bandWidth;
    }

    @JsonGetter("densityKernel")
    public DensityKernelTypes getDenstiyKernal() {
        return denstiyKernal;
    }

    @JsonSetter("densityKernel")
    public void setDenstiyKernal(DensityKernelTypes denstiyKernal) {
        this.denstiyKernal = denstiyKernal;
    }
}

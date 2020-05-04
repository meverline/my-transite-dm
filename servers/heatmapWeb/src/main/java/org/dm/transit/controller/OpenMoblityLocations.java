package org.dm.transit.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.transit.omd.dao.LocationDao;
import me.transit.omd.data.Location;

@RestController
@RequestMapping("omd-locations")
public class OpenMoblityLocations {

    private final LocationDao locationDao;

    @Autowired
    public OpenMoblityLocations(LocationDao locationDao) {
        this.locationDao = Objects.requireNonNull(locationDao, "locationDao can not be null");
    }

    @RequestMapping(value = "/list")
    public List<Location> listNames() throws IOException {
        return locationDao.list();
    }
}

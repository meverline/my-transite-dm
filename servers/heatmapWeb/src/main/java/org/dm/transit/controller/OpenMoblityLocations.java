package org.dm.transit.controller;

import me.transit.omd.dao.LocationDao;
import me.transit.omd.data.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class OpenMoblityLocations {

    private final LocationDao locationDao;

    @Autowired
    public OpenMoblityLocations(LocationDao locationDao) {
        this.locationDao = Objects.requireNonNull(locationDao, "locationDao can not be null");
    }

    @RequestMapping(value = "/omdlocations/list")
    public ModelAndView listNames(HttpServletResponse response) throws IOException {
        List<Location> agencies = locationDao.list();
        ModelAndView view = new ModelAndView("openMobilityData");
        view.addObject("locations", agencies);
        return view;
    }
}

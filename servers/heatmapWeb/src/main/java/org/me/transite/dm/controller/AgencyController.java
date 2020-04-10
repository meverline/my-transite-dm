package org.me.transite.dm.controller;

import me.transit.dao.AgencyDao;
import me.transit.database.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class AgencyController {

    private final AgencyDao agencyDao;

    @Autowired
    public AgencyController(AgencyDao agencyDao) {
        this.agencyDao = Objects.requireNonNull(agencyDao, "agencyDao can not be null");
    }

    @RequestMapping(value = "/agency/list")
    public ModelAndView listNames(HttpServletResponse response) throws IOException {
        List<String> agencies = agencyDao.list().stream().map(agency -> {
            return agency.getName();
        }).collect(Collectors.toList());

        ModelAndView view = new ModelAndView("agency");
        view.addObject("agencyNames", agencies);
        return view;
    }
}

package org.dm.transit.controller;

import me.transit.dao.AgencyDao;
import me.transit.database.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("agency")
public class AgencyController {

    private final AgencyDao agencyDao;

    @Autowired
    public AgencyController(AgencyDao agencyDao) {
        this.agencyDao = Objects.requireNonNull(agencyDao, "agencyDao can not be null");
    }

    @GetMapping(value = "/names")
    public List<String> listNames() throws IOException {
        return agencyDao.list().stream().map(agency -> {
            return agency.getName();
        }).collect(Collectors.toList());
    }

    @GetMapping(value = "/list")
    public List<Agency> list() throws IOException {
        return agencyDao.list();
    }
}

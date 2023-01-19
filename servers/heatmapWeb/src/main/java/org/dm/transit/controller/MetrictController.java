package org.dm.transit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.datamining.types.MetricTypes;

@RestController
@RequestMapping("metric")
public class MetrictController {

    @RequestMapping(value= "/list", method = RequestMethod.GET, produces = "application/json" )
    public List<String> listNames() throws IOException {
        List<String> rtn = new ArrayList<>();
        for (MetricTypes type : MetricTypes.values()) {
            rtn.add(type.name());
        }
        return rtn;
    }
}

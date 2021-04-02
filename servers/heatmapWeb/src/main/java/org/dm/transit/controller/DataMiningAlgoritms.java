package org.dm.transit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.datamining.types.DataMiningTypes;

@RestController
@RequestMapping("dm-algorithms")
public class DataMiningAlgoritms {

    @RequestMapping(value= "/list", method = RequestMethod.GET, produces = "application/json" )
    public List<String> listNames() throws IOException {
        List<String> rtn = new ArrayList<>();
        for (DataMiningTypes type : DataMiningTypes.values()) {
            rtn.add(type.name());
        }
        return rtn;
    }
}

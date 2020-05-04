package org.dm.transit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.datamining.DataMiningTypes;

@RestController
@RequestMapping("dm-algorithms")
public class DataMiningAlgoritms {

    @GetMapping(value = "/list")
    public List<String> listNames() throws IOException {
        List<String> rtn = new ArrayList<>();
        for (DataMiningTypes type : DataMiningTypes.values()) {
            rtn.add(type.name());
        }

        return rtn;
    }
}

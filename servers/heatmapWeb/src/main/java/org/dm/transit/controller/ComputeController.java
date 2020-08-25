package org.dm.transit.controller;

import java.io.IOException;
import java.util.UUID;

import org.dm.transit.controller.response.SearchRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.datamining.DataMiningJob;

@RestController
public class ComputeController {

    @PostMapping(value = "/search")
    public SearchRequest search(@RequestBody DataMiningJob parameters) throws IOException {
        SearchRequest rtn = new SearchRequest();
        rtn.setName(parameters.getName());
        rtn.setSearchId(UUID.randomUUID().toString());
        
        // send to the compute server.

        return rtn;
    }
    
}

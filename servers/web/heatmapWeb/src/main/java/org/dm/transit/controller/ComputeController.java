package org.dm.transit.controller;

import java.io.IOException;
import java.util.UUID;

import org.dm.transit.controller.response.SearchRequest;
import org.springframework.web.bind.annotation.*;

import me.datamining.DataMiningJob;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ComputeController {

    @RequestMapping(value= "/search", method = POST, consumes = "application/json", produces = "application/json" )
    public SearchRequest search(@RequestBody DataMiningJob parameters) throws IOException {
        SearchRequest rtn = new SearchRequest();
        rtn.setName(parameters.getName());
        rtn.setSearchId(UUID.randomUUID().toString());
        
        // send to the compute server.
        return rtn;
    }
    
}

package org.dm.transit.controller;

import me.datamining.DataMiningTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DataMiningAlgoritms {

    @RequestMapping(value = "/dataMiningAlgoritms/list")
    public ModelAndView listNames(HttpServletResponse response) throws IOException {
        List<String> rtm = new ArrayList<>();
        for (DataMiningTypes type : DataMiningTypes.values()) {
            rtm.add(type.name());
        }

        ModelAndView view = new ModelAndView("datamining");
        view.addObject("algorithms", rtm);
        return view;
    }
}

package com.egencia.webapp.spur.controller;


import com.egencia.webapp.spur.Routes;
import com.egencia.webapp.spur.model.Spur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import java.util.Map;

/**
 * 	Spur's Controller
 *  NOTE: By convention method names match the name of their View, e.g. HotelController.index() returns views/hotel/index
 */
@Controller
public class SpurController {

@Autowired
protected Environment environment;

    @Value("classpath:data.json")
    private Resource searchResult;

/**
 * Spur's Home page
 * @param model
 * @return
 * @throws Exception
 */
    @RequestMapping("")
    public String index(ModelMap model) throws Exception {
        // Add properties to EG that need to be available client-side and use EG.boostrapped for bootstrapped data
        Map eg = (Map) model.get("EG");
        Map bootstapped = (Map) eg.get("bootstrapped");

        // Get some data from the mock service
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> hotels = objectMapper.readValue(searchResult.getFile(), HashMap.class);

        // Put data on the model
        bootstapped.put("hotels", hotels.get("hotelResultList"));
        model.addAttribute("pageTitle", "Spur");

        return Routes.SPUR + "/index";
    }


    /**
     * Spur Details page
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/details/{id}")

    public String details(@PathVariable("id") final String id, ModelMap model) throws Exception {
        //get data from global services, and create some models
        //here we just use some fake data
        Spur spur = new Spur();
        spur.setId(id);
        spur.setName("The Brennan");
        spur.setDescription("It's a classy place, very minimal amount of mafia activity.");
        spur.setPhotos(new String[]{"http://media.expedia.com/hotels/1000000/20000/11700/11672/11672_32_t.jpg", "http://media.expedia.com/hotels/1000000/20000/11700/11672/11672_32_l.jpg", "http://media.expedia.com/hotels/1000000/20000/11700/11672/11672_32_b.jpg"});
        spur.setPrice(new BigDecimal(359.99));

        //add stuff to the ModelMap
        model.addAttribute("spur", spur);

        return Routes.SPUR + "/details";
    }

}


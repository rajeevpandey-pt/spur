package com.egencia.webapp.spur.model;

import com.egencia.library.uitoolkit.model.UitkCommonModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * This class makes sure that the Model is always populated with some common data.
 *
 * @author aallegret
 */
@ControllerAdvice
public class CommonModelProvider extends UitkCommonModelProvider {
    @Autowired
    Environment environment;

    // Get a property into the Model
    @ModelAttribute("myProperty")
    public String getDocSiteVersion() {
        return environment.getProperty("my_property");
    }

    // Get anything and put it on the Model
    @ModelAttribute("something")
    public String getSomething() {
        // do stuff
        return "some custom thing";
    }

    // Use this to add objects to 'EG'
    @Override
    public Map<String, Object> extendEG() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("spur", new HashMap<String, Object>());
        result.put("example", "value");
        return result;
    }

}
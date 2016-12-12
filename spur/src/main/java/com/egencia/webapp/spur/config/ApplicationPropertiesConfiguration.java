package com.egencia.webapp.spur.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.egencia.library.configuration.configservice.manager.properties.StripedItemProperties;
import com.egencia.library.configuration.configservice.manager.properties.StripedManagerProperties;

import com.egencia.library.logging.decision.rules.MapRuleRepository;
import com.egencia.library.logging.decision.rules.Rule;


/**
 * Deals with loading the appropriate properties file and allows place holders to be used for these values. 
 * @author aallegret
 */
@Configuration
public class ApplicationPropertiesConfiguration { 

	private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationPropertiesConfiguration.class);

	@Bean
	public StripedManagerProperties stripedManagerProperties(){
		return new StripedManagerProperties()
				.defaultTimeToRefreshInSeconds(3600);
	}

	@Bean
	public StripedItemProperties inboundRulesRepositoryConfigurationItemItemProperties(){
		return new StripedItemProperties(AccessLoggingConfiguration.INBOUND_RULES_REPOSITORY_CONFIGURATION_ITEM)
				.maptype(String.class, Rule.class)
				.timeToRefreshInSeconds(60L)
				.defaultValue(defaultAccessLoggingRulesConfigValue());
	}
	
	//TODO:Create the required accesslogging rules item in all environments in config-service and remove this default from here.
    //See AccesssLoggingConfiguration for how to setup the item.
	private Map<String, Rule<HttpServletRequest>> defaultAccessLoggingRulesConfigValue() {
		LOGGER.warn("The TODO for removing the archetype generated default accesslogging rules has not yet been addressed!");
		MapRuleRepository<HttpServletRequest> inboundAccessLoggingRules = new MapRuleRepository<>();
		inboundAccessLoggingRules.addRule("LOG ALL GET", new Rule<HttpServletRequest>(true, "method == 'GET'"));
		inboundAccessLoggingRules.addRule("LOG ALL MODIFY", new Rule<HttpServletRequest>(true, "method == 'POST' or method == 'PUT' or method == 'DELETE'"));
		return inboundAccessLoggingRules.ruleSet();
	}
	
}

package com.egencia.webapp.spur.config;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.egencia.library.configuration.configservice.resolver.StripedConfigurationResolver;
import com.egencia.library.logging.decision.LoggingDecisionMaker;
import com.egencia.library.logging.decision.rules.RuleBasedLoggingDecisionMaker;
import com.egencia.library.logging.decision.rules.StripedConfigurationRuleRepository;
import com.egencia.library.logging.servlet.TeeAccessLoggingFilter;

@Configuration
public class AccessLoggingConfiguration {
	
	// TODO: Create spur.accesslogging.inboundrules in config-service for all environments to control access logging rules
	// You may use step.archetype.webapp.accesslogging.inboundrules (item in maui) as an example to create this item
	// Once that is done, remove the default value that is setup for this item in ApplicationPropertiesConfiguration
	public static final String INBOUND_RULES_REPOSITORY_CONFIGURATION_ITEM = "spur.accesslogging.inboundrules";
	
	@Inject
	private StripedConfigurationResolver stripedConfigurationResolver;
	
	// TODO: Actually add logging rules to Config Service under the new name
	@Bean
	public StripedConfigurationRuleRepository<HttpServletRequest> stripedConfigurationRuleRepository() {
		StripedConfigurationRuleRepository<HttpServletRequest> stripedConfigurationRuleRepository = 
				new StripedConfigurationRuleRepository<HttpServletRequest>(stripedConfigurationResolver, INBOUND_RULES_REPOSITORY_CONFIGURATION_ITEM, true);
		return stripedConfigurationRuleRepository;
	}
	
	@Bean
	public LoggingDecisionMaker<HttpServletRequest> loggingDecisionMaker() {
		return new RuleBasedLoggingDecisionMaker<HttpServletRequest>(stripedConfigurationRuleRepository());
	}

    //Logging filter
	@Bean
    public TeeAccessLoggingFilter teeAccessLoggingFilter() {
    	final TeeAccessLoggingFilter filter = new TeeAccessLoggingFilter(loggingDecisionMaker());
    	filter.setAccessLogger(LoggerFactory.getLogger("ACCESS-LOGGER"));
    	return filter;
    }
}
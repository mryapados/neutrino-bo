package org.neutrinocms.bo.conf;

import org.neutrinocms.core.conf.NeutrinoCoreConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"org.neutrinocms.bo"})
//@Import (NeutrinoCoreConfiguration.class)
public class NeutrinoBOConfiguration{
	

}

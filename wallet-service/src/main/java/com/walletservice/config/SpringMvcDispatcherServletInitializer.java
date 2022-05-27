package com.walletservice.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		//here we specify config class for main application context.
		
		return new Class[] {
				WalletConfig.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		//here we specify servlet config classes where we configure ViewResolver,Resources and etc. 
		
		return new Class[] {
				WebMvcConfig.class
		};
	}

	@Override
	protected String[] getServletMappings() {

		return new String[] {
				"/"
		};
	}

}

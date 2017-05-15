package com.virtorg.bi.tst;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.virtorg.bi.tst.components.HealthController;

public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
        register(RequestContextFilter.class);
        packages("com.virtorg.bi.tst.components");
		register(HealthController.class);
		//register( UploadFileREST.class);
	}

}

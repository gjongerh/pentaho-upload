package com.virtorg.bi.tst;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.virtorg.bi.tst.components.HealthController;
import com.virtorg.bi.tst.components.UploadFileREST;

public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
        register(RequestContextFilter.class);
        //packages("com.virtorg.bi.tst.components");	// load all packages in this package
		register(HealthController.class);
		register(UploadFileREST.class);
		//register(UploaderREST.class);
	}

}

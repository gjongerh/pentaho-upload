package com.virtorg.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.virtorg.bi.tst.components.UploadFileREST;

@Configuration
@ComponentScan(basePackageClasses = UploadFileREST.class)
public class TestConfig {

}
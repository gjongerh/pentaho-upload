package com.virtorg.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.virtorg.bi.tst.SparklUploadTestApp;

@Configuration
@ComponentScan(basePackageClasses = SparklUploadTestApp.class)
public class TestConfig {

}
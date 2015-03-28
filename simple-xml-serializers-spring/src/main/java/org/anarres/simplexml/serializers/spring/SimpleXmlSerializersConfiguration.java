/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring;

import java.util.List;
import org.anarres.simplexml.factory.PersisterFactory;
import org.anarres.simplexml.serializers.Marker;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 *
 * @author shevek
 */
@Configuration
@ComponentScan(
        basePackageClasses = Marker.class,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConverterFactory.class),
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TransformFactory.class)
        }
)
public class SimpleXmlSerializersConfiguration {

    @Bean
    public PersisterFactory simpleXmlSerializerFactory(
            List<ConverterFactory> converters,
            List<TransformFactory> transforms
    ) {
        PersisterFactory factory = new PersisterFactory();
        factory.addConverters(converters);
        factory.addTransforms(transforms);
        return factory;
    }
}

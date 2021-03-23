package com.guava.app.annotations;

import com.guava.app.config.QPSConfig;
import com.guava.app.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class QPSSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
            QPSConfig.class.getName(),
                GlobalExceptionHandler.class.getName()
        };
    }
}

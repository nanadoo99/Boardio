package com.nki.t1.utils;

import com.nki.t1.dto.ObjDto;
import org.springframework.stereotype.Component;

@Component
public class AttributeNameUtils {
    public static String getAttributeName(ObjDto object) {
        String className = object.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
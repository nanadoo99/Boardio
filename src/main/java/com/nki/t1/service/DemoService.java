package com.nki.t1.service;

import com.nki.t1.domain.ErrorType;
import com.nki.t1.exception.InvalidPostException;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    public void invalidPost() {
        throw new InvalidPostException(ErrorType.POST_NOT_FOUND);
    }
}

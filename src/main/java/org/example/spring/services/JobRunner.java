package org.example.spring.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobRunner {
    Logger logger = Logger.getLogger(getClass().getName());

    public void run(List<String> jobs) {
        jobs.forEach(it -> logger.log(Level.ALL, it));
    }
}

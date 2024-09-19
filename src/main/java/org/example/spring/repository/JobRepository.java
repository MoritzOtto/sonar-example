package org.example.spring.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class JobRepository {
    Logger logger = Logger.getLogger(getClass().getName());

    public List<String> getJobs(){
        logger.log(Level.ALL, "run");
        return new ArrayList<>();
    }
}

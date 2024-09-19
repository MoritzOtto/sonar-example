package org.example.spring.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobRepository {
    public List<String> getJobs(){
        return new ArrayList<>();
    }
}

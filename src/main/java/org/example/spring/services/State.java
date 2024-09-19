package org.example.spring.services;

import lombok.Getter;
import lombok.Setter;

public class State {
    private State() {}

    @Setter
    @Getter
    private static boolean isActive;

}

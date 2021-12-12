package com.manager.infrastructure;

import lombok.Data;

@Data
public class IdGenerator {
    private Integer counter = 0;

    public Integer getNextId() {
        this.counter = this.counter + 1;
        return this.counter;
    }

    public void reset() {
        this.counter = 0;
    }
}

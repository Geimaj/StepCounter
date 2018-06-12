package com.example.jamie.stepcounter;

import java.util.Date;

public class LogStep {

    private int steps;
    private Date date;

    public LogStep(int steps, Date date) {
        this.steps = steps;
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void step(){
        this.steps++;
    }

    @Override
    public String toString() {
        return this.steps + "," + this.date;
    }
}

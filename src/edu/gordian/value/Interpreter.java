package edu.gordian.value;

public interface Interpreter {

    public Value interpretValue(String s);

    public Value[] interpretValues(String[] s);
}
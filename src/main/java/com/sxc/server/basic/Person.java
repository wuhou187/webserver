package com.sxc.server.basic;

public class Person {
    private String name;
    private int age;
    Person() {}

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return ("name: " + this.name + " age: " + this.age);
    }
}

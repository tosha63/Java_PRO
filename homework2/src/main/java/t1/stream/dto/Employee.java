package t1.stream.dto;

public class Employee {
    private String name;
    private int age;
    private Position position;

    public Employee(String name, int age, Position position) {
        this.name = name;
        this.age = age;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public enum Position {
        DIRECTOR,
        MANAGER,
        ENGINEER,
        TECHNICIAN
    }
}


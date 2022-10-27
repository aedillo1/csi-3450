package models;

public class Employee {
    int id;
    String name;
    String job;
    String ssn;
    float salary;

    public Employee(int id, String name, String job/*,String ssn, float salary*/)
    {
        this.id = id;
        this.name = name;
        this.job = job;
        /*
        this.ssn = ssn;
        this.salary = salary;
        */
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    /*
    public String getSSN() {
        return ssn;
    }

    public void setSSN(String ssn) {
        this.ssn = ssn;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
    */
}

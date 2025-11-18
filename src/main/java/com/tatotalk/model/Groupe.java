package com.tatotalk.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "groupes")
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false, length = 100)
    public String name;

    @Column(nullable = true, length = 150)
    public String description;

    @ManyToMany
    @JoinTable(name = "groupe_employees", joinColumns = @JoinColumn(name = "groupe_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    public List<Employees> employees;

    public Groupe(String name, String description, List<Employees> employees) {
        this.name = name;
        this.description = description;
        this.employees = employees;
    }

    public Groupe() {

    }

    public void addEmployee(Employees employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.getGroupes().add(this);
        }
    }

    public void removeEmployee(Employees employee){
        this.employees.remove(employee);
        employee.getGroupes().remove(this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }


}

package com.videoclub.model;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private double saldo;

    public Cliente(int id, String nombre, String dni, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
    }

    public Cliente(String nombre, String dni, double saldo) {
        this.nombre = nombre;
        this.dni = dni;
        this.saldo = saldo;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
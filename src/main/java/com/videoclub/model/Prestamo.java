package com.videoclub.model;

import java.util.Date;

public class Prestamo {

    private int id;
    private int clienteId;
    private int peliculaId;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private boolean disponible;


    public Prestamo(int id, int clienteId, int peliculaId, Date fechaPrestamo, Date fechaDevolucion) {
        this.id = id;
        this.clienteId = clienteId;
        this.peliculaId = peliculaId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.disponible = true;
    }

    public Prestamo(int clienteId, int peliculaId, Date fechaPrestamo, Date fechaDevolucion) {
        this.clienteId = clienteId;
        this.peliculaId = peliculaId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.disponible = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(int peliculaId) {
        this.peliculaId = peliculaId;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", peliculaId=" + peliculaId +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }
}



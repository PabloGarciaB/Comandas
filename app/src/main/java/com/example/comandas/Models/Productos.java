package com.example.comandas.Models;

public class Productos {
    private String idProductos;
    private String nombreProducto;
    private String numUnidades;
    private String precioProducto;
    private String fechaRegistro; //Fecha ingresada por el usuario
    private String timestamp; //Fecha que usara Firebase por milisegundos

    public String getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(String idProductos) {
        this.idProductos = idProductos;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNumUnidades() {
        return numUnidades;
    }

    public void setNumUnidades(String numUnidades) {
        this.numUnidades = numUnidades;
    }

    public String getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(String precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return nombreProducto;
    }
}

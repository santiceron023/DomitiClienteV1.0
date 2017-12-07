package com.example.danni.domiti;

/**
 * Created by danni on 08/11/2017.
 */

public class NuevoProducto {
    private String marca, nombre, precio, tamaño, foto;

    public NuevoProducto() {
    }

    public NuevoProducto(String marca, String nombre, String precio, String tamaño, String foto) {
        this.marca = marca;
        this.nombre = nombre;
        this.precio = precio;
        this.tamaño = tamaño;
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

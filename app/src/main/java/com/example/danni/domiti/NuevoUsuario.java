package com.example.danni.domiti;

/**
 * Created by danni on 29/11/2017.
 */

public class NuevoUsuario {
    private String nombre, celular, correo, ubicacion,foto;

    public NuevoUsuario(String nombre, String celular, String correo, String ubicacion, String foto) {
        this.nombre = nombre;
        this.celular = celular;
        this.correo = correo;
        this.ubicacion = ubicacion;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public NuevoUsuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}

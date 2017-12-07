package com.example.danni.domiti;

/**
 * Created by danni on 29/11/2017.
 */

public class Tienda {
    private String celular, costoEnvio, direccion, email, foto, nombre, pedidoMin;
    private String propietario, tiempoEnvio, tipo;

    public Tienda() {
    }

    public Tienda(String celular, String costoEnvio, String direccion, String email, String foto, String nombre, String pedidoMin, String propietario, String tiempoEnvio, String tipo) {
        this.celular = celular;
        this.costoEnvio = costoEnvio;
        this.direccion = direccion;
        this.email = email;
        this.foto = foto;
        this.nombre = nombre;
        this.pedidoMin = pedidoMin;
        this.propietario = propietario;
        this.tiempoEnvio = tiempoEnvio;
        this.tipo = tipo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPedidoMin() {
        return pedidoMin;
    }

    public void setPedidoMin(String pedidoMin) {
        this.pedidoMin = pedidoMin;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getTiempoEnvio() {
        return tiempoEnvio;
    }

    public void setTiempoEnvio(String tiempoEnvio) {
        this.tiempoEnvio = tiempoEnvio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

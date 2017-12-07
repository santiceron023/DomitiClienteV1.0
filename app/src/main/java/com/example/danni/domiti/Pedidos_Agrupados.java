package com.example.danni.domiti;

/**
 * Created by danni on 01/12/2017.
 */

public class Pedidos_Agrupados {
    private String Cantidad;
    private String Total;
    private String NombreTienda;
    private String NumeroPedido;
    private String TiendaId;
    private String Foto;

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getNombreTienda() {
        return NombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        NombreTienda = nombreTienda;
    }

    public String getNumeroPedido() {
        return NumeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        NumeroPedido = numeroPedido;
    }

    public String getTiendaId() {
        return TiendaId;
    }

    public void setTiendaId(String tiendaId) {
        TiendaId = tiendaId;
    }

    public Pedidos_Agrupados() {

    }

    public Pedidos_Agrupados(String cantidad, String total, String nombreTienda, String numeroPedido, String tiendaId, String foto) {
        Cantidad = cantidad;
        Total = total;
        NombreTienda = nombreTienda;
        NumeroPedido = numeroPedido;
        TiendaId = tiendaId;
        Foto = foto;
    }
}

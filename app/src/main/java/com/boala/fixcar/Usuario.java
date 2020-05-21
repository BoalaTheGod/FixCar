package com.boala.fixcar;

import java.util.Date;

public class Usuario {
    private int idusuario, telefono;
    private String nombre, password, direccion, localidad, email, imagen;
    private Date fecha;

    public Usuario(int idusuario, int telefono, int estado, String nombre, String password, String confirma, String direccion, String localidad, String email, String imagen, Date fecha) {
        this.idusuario = idusuario;
        this.telefono = telefono;
        this.nombre = nombre;
        this.password = password;
        this.direccion = direccion;
        this.localidad = localidad;
        this.email = email;
        this.imagen = imagen;
        this.fecha = fecha;
    }

    public Usuario() {
        this.idusuario = 0;
        this.telefono = 0;
        this.nombre = "";
        this.password = "";
        this.direccion = "";
        this.localidad = "";
        this.email = "";
        this.imagen = "";
        this.fecha = Vehiculo.stringToDate("00/00/0000");
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idusuario=" + idusuario +
                ", telefono=" + telefono +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", direccion='" + direccion + '\'' +
                ", localidad='" + localidad + '\'' +
                ", email='" + email + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}

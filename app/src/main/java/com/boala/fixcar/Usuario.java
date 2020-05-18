package com.boala.fixcar;

import java.sql.Date;

public class Usuario {
    private int idusuario, telefono, estado;
    private String nombre, password, confirma, direccion, localidad, email, imagen;

    public Usuario(int idusuario, int telefono, int estado, String nombre, String password, String confirma, String direccion, String localidad, String email, String imagen, Date fecha) {
        this.idusuario = idusuario;
        this.telefono = telefono;
        this.estado = estado;
        this.nombre = nombre;
        this.password = password;
        this.confirma = confirma;
        this.direccion = direccion;
        this.localidad = localidad;
        this.email = email;
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "idusuario=" + idusuario +
                ", telefono=" + telefono +
                ", estado=" + estado +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", confirma='" + confirma + '\'' +
                ", direccion='" + direccion + '\'' +
                ", localidad='" + localidad + '\'' +
                ", email='" + email + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public String getConfirma() {
        return confirma;
    }

    public void setConfirma(String confirma) {
        this.confirma = confirma;
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
}

package com.example.epucp.dto;

public class Usuario {
    private String correo;
    private String rol;
    private String key;

    public Usuario(String correo, String key){
        this.correo = correo;
        this.key = key;
    }

    public Usuario() {
    }

    public Usuario(String correo, String rol, String key) {
        this.correo = correo;
        this.rol = rol;
        this.key = key;
    }

    public String getCorreo() {
        return correo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

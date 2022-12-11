package com.example.epucp.dto;

public class Usuario {
    private String correo;
    private String rol;
    private String key;
    private String nombre;
    private String codigo;
    private String fotoFilename;

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

    public Usuario(String correo, String rol, String key, String nombre, String codigo) {
        this.correo = correo;
        this.rol = rol;
        this.key = key;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Usuario(String correo, String rol, String key, String nombre, String codigo, String fotoFilename) {
        this.correo = correo;
        this.rol = rol;
        this.key = key;
        this.nombre = nombre;
        this.codigo = codigo;
        this.fotoFilename = fotoFilename;
    }

    public String getFotoFilename() {
        return fotoFilename;
    }

    public void setFotoFilename(String fotoFilename) {
        this.fotoFilename = fotoFilename;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String imprimirInformacion(){
        return "Nombre: "+this.nombre+"\n"+
                "Codigo: "+this.codigo+"\n"+
                "Correo: "+this.correo;
    }
}

package com.example.epucp.dto;

import java.io.Serializable;

public class Evento implements Serializable {
    private String nombre;
    private String responsable;
    private String descripcion;
    private String facultad;
    private String aula;
    private String fecha;
    private String hora;
    private String filename;
    private String key;

    public Evento() {
    }

    public Evento(String nombre, String responsable, String descripcion, String facultad, String aula, String fecha, String hora) {
        this.nombre = nombre;
        this.responsable = responsable;
        this.descripcion = descripcion;
        this.facultad = facultad;
        this.aula = aula;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Evento(String nombre, String responsable, String descripcion, String facultad, String aula, String fecha, String hora, String filename, String key) {
        this.nombre = nombre;
        this.responsable = responsable;
        this.descripcion = descripcion;
        this.facultad = facultad;
        this.aula = aula;
        this.fecha = fecha;
        this.hora = hora;
        this.filename = filename;
        this.key = key;
    }

    public String getDetalle(){
        return "Nombre: "+this.nombre+"\n"+
                "Descrición: "+this.descripcion+"\n"+
                "Facultad: "+this.facultad+"\n"+
                "Aula: "+this.aula+"\n"+
                "Fecha: "+this.fecha+"\n"+
                "Hora: "+this.hora+"\n"+
                "Responsable: "+this.responsable+"\n";

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

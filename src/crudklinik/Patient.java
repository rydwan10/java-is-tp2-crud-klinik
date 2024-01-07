/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crudklinik;

/**
 *
 * @author muhammadrydwan
 */
public class Patient {
    private int id;
    private String namaPasien;
    private String nik;
    private String tanggalLahir;
    private String alamat;
    

    public Patient(int id, String namaPasien, String nik, String tanggalLahir, String alamat) {
        this.id = id;
        this.namaPasien = namaPasien;
        this.nik = nik;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
    }
    
    public Patient() {
        
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id; 
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}

package com.hels.miapp.Model;

public class Customers {
    private String id, nama_customers, email_customer, alamat_customer;

    public Customers(){

    }

    public Customers (String nama_customers, String email_customer, String alamat_customer) {
        this.nama_customers = nama_customers;
        this.email_customer = email_customer;
        this.alamat_customer = alamat_customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_customers() {
        return nama_customers;
    }

    public void setNama_customers(String nama_customers) {
        this.nama_customers = nama_customers;
    }

    public String getEmail_customer() {
        return email_customer;
    }

    public void setEmail_customer(String email_customer) {
        this.email_customer = email_customer;
    }

    public String getAlamat_customer() {
        return alamat_customer;
    }

    public void setAlamat_customer(String alamat_customer) {
        this.alamat_customer = alamat_customer;
    }
}
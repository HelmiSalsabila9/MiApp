package com.hels.miapp.Model;

public class Product {
    private String id, gambar_produk, nama_produk, harga, deskripsi, nama_toko;

    public Product() {

    }

    public Product (String gambar_produk ,String nama_produk, String harga, String deskripsi, String nama_toko) {
        this.gambar_produk = gambar_produk;
        this.nama_produk = nama_produk;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.nama_toko = nama_toko;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }
}

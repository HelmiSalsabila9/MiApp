package com.hels.miapp.Model;

public class Category {

    private String id, nm_kategori, img_kategori;

    public Category() {

    }

    public Category(String nm_kategori, String img_kategori) {
        this.nm_kategori = nm_kategori;
        this.img_kategori = img_kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNm_kategori() {
        return nm_kategori;
    }

    public void setNm_kategori(String nm_kategori) {
        this.nm_kategori = nm_kategori;
    }

    public String getImg_kategori() {
        return img_kategori;
    }

    public void setImg_kategori(String img_kategori) {
        this.img_kategori = img_kategori;
    }
}

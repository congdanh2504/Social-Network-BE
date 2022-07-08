package com.example.social_network_fpt_be.cloudStorage.image;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_image;
    private String url;
    private LocalDateTime create_date;
    private String type;
    private int id;

    public Image(){}

    public Image(String id_image, String url, LocalDateTime create_date, String type, int id) {
        this.id_image = id_image;
        this.url = url;
        this.create_date = create_date;
        this.type = type;
        this.id = id;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id_image='" + id_image + '\'' +
                ", url='" + url + '\'' +
                ", create_date=" + create_date +
                ", type='" + type + '\'' +
                ", id=" + id +
                '}';
    }
}

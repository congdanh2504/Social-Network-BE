package com.example.social_network_fpt_be.cloudStorage.video;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_video;
    private String url;
    private LocalDateTime create_date;

    public Video(){}

    public Video(String url, LocalDateTime create_date) {
        this.url = url;
        this.create_date = create_date;
    }

    public Long getId_video() {
        return id_video;
    }

    public void setId_video(Long id_video) {
        this.id_video = id_video;
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

    @Override
    public String toString() {
        return "Video{" +
                "id_video=" + id_video +
                ", url='" + url + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}

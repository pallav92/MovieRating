package com.android.movierating.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by pallav on 8/11/17.
 */
@Entity
public class Movie implements Serializable {

    private static final long serialVersionUID = 5300053526162124583L;
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "rating")
    private String rating;
    @Property(nameInDb = "yearOfRelease")
    private String yearOfRelease;
    @Property(nameInDb = "imdbURL")
    private String imdbURL;
    @Property(nameInDb = "image")
    private String image;


    @Generated(hash = 1266164782)
    public Movie(Long id, String name, String rating, String yearOfRelease,
            String imdbURL, String image) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.yearOfRelease = yearOfRelease;
        this.imdbURL = imdbURL;
        this.image = image;
    }

    @Generated(hash = 1263461133)
    public Movie() {
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(String yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getImdbURL() {
        return imdbURL;
    }

    public void setImdbURL(String imdbURL) {
        this.imdbURL = imdbURL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.framgia.mysoundcloud.data.model;

import android.support.annotation.StringDef;

import java.io.Serializable;

import static com.framgia.mysoundcloud.data.model.User.TYPE_LOGIN.LOGIN_FACEBOOK;
import static com.framgia.mysoundcloud.data.model.User.TYPE_LOGIN.LOGIN_GG;

/**
 * Created by buidanhnam on 4/28/2018.
 */

public class User implements Serializable {
    private String name;
    private String email;
    private String token;
    private String image;
    private String id;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static final class UserEntry {
        public static final String TABLE_NAME_USER = "user";
        public static final String COLUMN_NAME_USER = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_TOKEN = "token";
        public static final String COLUMN_NAME_USER_ID = "user_id";
    }

    @StringDef({
            LOGIN_FACEBOOK, LOGIN_GG
    })
    public @interface TYPE_LOGIN {
        String LOGIN_GG = "google";
        String LOGIN_FACEBOOK = "facebook";
    }

}

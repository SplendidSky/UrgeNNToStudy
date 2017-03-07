package com.example.nns_app_1;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by 伟宸 on 2017/3/6.
 */

public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}

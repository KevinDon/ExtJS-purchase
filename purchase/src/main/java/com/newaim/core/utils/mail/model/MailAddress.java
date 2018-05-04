package com.newaim.core.utils.mail.model;

import java.io.Serializable;

public class MailAddress implements Serializable {

    protected String address = "";

    protected String name = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MailAddress() {

    }

    public MailAddress(String address, String name) {
        this.address = address;
        this.name = name;
    }
}

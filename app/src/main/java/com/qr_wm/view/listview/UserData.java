package com.qr_wm.view.listview;

public class UserData {
    private String name;
    private String phone;
    private String email;
    private String group;

    public UserData(String name, String phone, String email, String group) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group = group;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getGroup() { return this.group; }
    
}

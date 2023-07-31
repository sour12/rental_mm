package com.qr_wm.view.listview;

public class BoardData {
    private String name;
    private String status;
    private String user;
    private String cdate;
    private String mdate;
    private String desc;

    public BoardData(String name, String status, String user, String cdate, String mdate, String desc) {
        this.name = name;
        this.status = status;
        this.user = user;
        this.cdate = cdate;
        this.mdate = mdate;
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }
    public String getStatus() {
        return this.status;
    }
    public String getUser() {
        return this.user;
    }
    public String getCdate() { return this.cdate; }
    public String getMdate() { return this.mdate; }
    public String getDesc() { return this.desc; }
    public String getAllCSV() {
        return (this.name + "," + this.status + "," + this.user + "," + this.cdate + "," + this.mdate + "," + this.desc + "\n");
    }
}

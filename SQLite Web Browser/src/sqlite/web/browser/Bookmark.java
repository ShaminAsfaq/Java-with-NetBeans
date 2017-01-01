/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite.web.browser;

/**
 *
 * @author shamin
 */
public class Bookmark {

    private String url;
    private String name;
    private String time;

    @Override
    public String toString() {
        return "Bookmark{" + "url=" + url + ", name=" + name + ", time=" + time + '}';
    }

    public Bookmark(String url, String name, String time) {
        this.url = url;
        this.name = name;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

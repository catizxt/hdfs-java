package com.example.hdfs.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author zly
 */
@Entity
public class HdfsFile {
    @Id
    @GeneratedValue
    private Integer id;


    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    private long updatedAt;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    private String cover;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private  String filename;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private  String type;


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String createdAt;

    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    private String subDescription;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    //后缀fpath，因为api就是那一个，hadoop的地址是在java函数中
}

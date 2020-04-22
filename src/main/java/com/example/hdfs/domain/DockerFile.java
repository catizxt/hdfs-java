package com.example.hdfs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author zly
 */
@Entity
public class DockerFile {
    @Id
    @GeneratedValue
    private Integer id;
    //才时候就直接通过这个删除数据库中的记录，还是直接存储到这里，不删除这个
    //不删除，就定期导出数据库之后清空此库，再继续使用

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String createdAt;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

}

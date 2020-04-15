package com.example.hdfs.repository;

import com.example.hdfs.domain.HdfsFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zly
 */

public interface HdfsFileRepository extends JpaRepository<HdfsFile,Integer> {

    public void  deleteByTitle(String title);

    /**
     * @param title
     * @return
     */
    public List<HdfsFile> findByTitle(String title);
    public List<HdfsFile> findByFiletype(String filetype);

}

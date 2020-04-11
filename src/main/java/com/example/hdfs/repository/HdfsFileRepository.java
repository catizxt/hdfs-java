package com.example.hdfs.repository;

import com.example.hdfs.domain.HdfsFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author zly
 */

public interface HdfsFileRepository extends JpaRepository<HdfsFile,Integer> {

}

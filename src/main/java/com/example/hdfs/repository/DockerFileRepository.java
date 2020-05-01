package com.example.hdfs.repository;

import com.example.hdfs.domain.DockerFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zly
 */

public interface DockerFileRepository extends JpaRepository<DockerFile,Integer> {

}

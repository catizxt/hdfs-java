package com.example.hdfs.repository;

import com.example.hdfs.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zly
 */
public interface StudentRepository extends JpaRepository<Student,Integer> {
    /**
     * @param age
     * @return List<Student>
     */
    public List<Student> findByAge(Integer age);
}

//public interface ProductRepository extends CrudRepository<Product, Long> {
//}
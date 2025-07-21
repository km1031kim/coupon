package com.example.coupon.repository;

import com.example.coupon.domain.filemeta.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {
}

package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}

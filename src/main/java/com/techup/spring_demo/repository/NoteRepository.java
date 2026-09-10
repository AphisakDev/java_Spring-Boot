package com.techup.spring_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techup.spring_demo.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}

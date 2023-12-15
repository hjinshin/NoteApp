package com.noteapp.noteapi.repository;

import com.noteapp.noteapi.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
    Note findByUserId(String userId);
    boolean existsByUserId(String userId);
}

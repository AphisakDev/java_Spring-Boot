package com.techup.spring_demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.entity.Note;
import com.techup.spring_demo.repository.NoteRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse createNote(NoteRequest request) {
        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        Note savedNote = noteRepository.save(note);
        return NoteResponse.fromEntity(savedNote);
    }

    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(NoteResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<NoteResponse> getNoteById(Long id) {
        return noteRepository.findById(id)
                .map(NoteResponse::fromEntity);
    }

    public Optional<NoteResponse> updateNote(Long id, NoteRequest request) {
        return noteRepository.findById(id).map(existingNote -> {
            existingNote.setTitle(request.getTitle());
            existingNote.setContent(request.getContent());
            Note updatedNote = noteRepository.save(existingNote);
            return NoteResponse.fromEntity(updatedNote);
        });
    }

    public boolean deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /** แนบรูปเข้ากับโน้ต: อัปเดต imageUrl แล้วคืน DTO */
    public NoteResponse attachFileUrl(Long id, String url) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        note.setImageUrl(url);
        Note saved = noteRepository.save(note);
        return NoteResponse.fromEntity(saved);
    }
}

package com.techup.spring_demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.service.NoteService;
import com.techup.spring_demo.service.SupabaseStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final SupabaseStorageService supabaseStorageService;

    // Create: POST /api/notes -> 201 Created
    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request) {
        NoteResponse createdNote = noteService.createNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    // Read All: GET /api/notes -> 200 OK
    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes() {
        List<NoteResponse> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    // Read by ID: GET /api/notes/{id} -> 200 OK or 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update: PUT /api/notes/{id} -> 200 OK or 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id, @RequestBody NoteRequest request) {
        return noteService.updateNote(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete: DELETE /api/notes/{id} -> 204 No Content or 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        boolean isDeleted = noteService.deleteNote(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /** อัปโหลดไฟล์ แล้วผูก URL เข้ากับโน้ต */
    @PostMapping("/{id}/upload")
    public ResponseEntity<NoteResponse> uploadForNote(@PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String url = supabaseStorageService.uploadFile(file);
        NoteResponse updated = noteService.attachFileUrl(id, url);
        return ResponseEntity.ok(updated);
    }
}

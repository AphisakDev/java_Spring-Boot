package com.techup.spring_demo.dto;

import com.techup.spring_demo.entity.Note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;

    public static NoteResponse fromEntity(Note note) {
        if (note == null) {
            return null;
        }
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .imageUrl(note.getImageUrl())
                .build();
    }
}

package com.noteapp.noteapi.service;

import com.noteapp.noteapi.dto.PageUpdateDto;
import com.noteapp.noteapi.dto.UpdateAllDto;
import com.noteapp.noteapi.entity.Note;
import com.noteapp.noteapi.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    @Transactional
    public void createEntity(String userId) {
        Note note = new Note(userId, new ArrayList<>());
        noteRepository.save(note);
    }
    @Transactional
    public void pageUpdate(String userId, String oldPageName, PageUpdateDto.NewPage newPageDto) {
        Note note = noteRepository.findByUserId(userId);
        for(Note.Page page: note.getPages()) {
            if(page.getName().equals(oldPageName)) {
                page.setName(newPageDto.getName());
                page.setData(newPageDto.getData());
                page.setLast_modified_date(LocalDateTime.now());
                noteRepository.save(note);
                return;
            }
        }
        Note.Page newPage = new Note.Page(newPageDto.getName(), newPageDto.getData(), LocalDateTime.now());
        Objects.requireNonNull(note).getPages().add(newPage);
        noteRepository.save(note);
    }
    @Transactional
    public void updateAll(String userId, List<UpdateAllDto.NewPage> newPages) {
        Note note = noteRepository.findByUserId(userId);
        List<Note.Page> pageList = new ArrayList<>();
        for(UpdateAllDto.NewPage newPage: newPages) {
            pageList.add(new Note.Page(newPage.getName(), newPage.getData(), LocalDateTime.now()));
        }
        note.setPages(pageList);
        noteRepository.save(note);
    }
    @Transactional
    public void deletePageByName(String userId, String pageName) {
        Note note = noteRepository.findByUserId(userId);
        if(note != null && note.getPages() != null) {
            note.getPages().removeIf(page->page.getName().equals(pageName));
            noteRepository.save(note);
        }
    }
    @Transactional
    public void deleteAllPages(String userId) {
        Note note = noteRepository.findByUserId(userId);
        note.setPages(new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public List<String> getAllPageNamesByUserId(String userId) {
        Note note = noteRepository.findByUserId(userId);
        return note.getPages().stream()
                .map(Note.Page::getName)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<Note.Page> readAllPages(String userId) {
        Note note = noteRepository.findByUserId(userId);
        return note.getPages();
    }
    @Transactional(readOnly = true)
    public Note.Page readPage(String userId, String pageName) {
        Note note = noteRepository.findByUserId(userId);
        if(note != null && note.getPages() != null) {
            for(Note.Page page: note.getPages()) {
                if(page.getName().equals(pageName)) {
                    return page;
                }
            }
        }
        return null;
    }
    @Transactional(readOnly = true)
    public boolean existPageName(String userId, String pageName) {
        Note note = noteRepository.findByUserId(userId);
        for(Note.Page page: note.getPages()) {
            if(page.getName().equals(pageName))
                return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public boolean existUserId(String userId) {
        return noteRepository.existsByUserId(userId);
    }
}

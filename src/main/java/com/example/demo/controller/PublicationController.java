package com.example.demo.controller;

import com.example.demo.model.Publication;
import com.example.demo.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publications")

public class PublicationController {

    @Autowired
    private PublicationRepository publicationRepository;

    @PostMapping
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        try {
            Publication newPublication = publicationRepository.save(publication);
            return new ResponseEntity<>(newPublication, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Publication>> getAllPublications() {
        try {
            List<Publication> publications = publicationRepository.findAll();
            if (publications.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(publications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id) {
        Optional<Publication> publication = publicationRepository.findById(id);
        if (publication.isPresent()) {
            return new ResponseEntity<>(publication.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(@PathVariable Long id, @RequestBody Publication updatedPublication) {
        Optional<Publication> existingPublication = publicationRepository.findById(id);
        if (existingPublication.isPresent()) {
            Publication publication = existingPublication.get();
            publication.setContent(updatedPublication.getContent());
            publication.setImageUrl(updatedPublication.getImageUrl());
            publication.setPrivacy(updatedPublication.getPrivacy());
            publication.setTypeContent(updatedPublication.getTypeContent());
            Publication updated = publicationRepository.save(publication);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePublication(@PathVariable Long id) {
        try {
            if (publicationRepository.existsById(id)) {
                publicationRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  Buscar publicaciones por privacidad (GET /api/publications/filter?privacy=public)
    @GetMapping("/filter")
    public ResponseEntity<List<Publication>> getPublicationsByPrivacy(@RequestParam String privacy) {
        try {
            List<Publication> publications = publicationRepository.findByPrivacy(privacy);
            if (publications.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(publications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


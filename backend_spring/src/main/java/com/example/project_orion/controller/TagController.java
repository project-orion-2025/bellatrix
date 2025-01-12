package com.example.project_orion.controller;

import com.example.project_orion.models.Tag;
import com.example.project_orion.payload.dtos.TagDTO;
import com.example.project_orion.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {

    @Autowired
    public TagService tagService;

    @GetMapping("/public/tags")
    public ResponseEntity<List<Tag>> getAllTags(){
        List<Tag> tagList =  tagService.getAllQuestions();
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }

    @PostMapping("/author/tags")
    public ResponseEntity<Tag> createTag(@RequestParam String tagName){
        Tag savedTag = tagService.createTag(tagName);
        return new ResponseEntity<>(savedTag, HttpStatus.OK);
    }

    @PutMapping("/author/tags")
    public ResponseEntity<TagDTO> updateTag(@Valid @RequestBody TagDTO tagDTO){
        TagDTO savedTagDTO = tagService.updateTag(tagDTO);
        return new ResponseEntity<>(savedTagDTO, HttpStatus.OK);
    }

    @DeleteMapping("/author/tags/{tagId}")
    public ResponseEntity<TagDTO> deleteTag(@PathVariable Long tagId){
        TagDTO deletedTagDTO = tagService.deleteTag(tagId);
        return new ResponseEntity<>(deletedTagDTO, HttpStatus.OK);
    }

}

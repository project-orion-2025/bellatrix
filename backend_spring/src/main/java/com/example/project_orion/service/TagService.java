package com.example.project_orion.service;

import java.util.List;

import com.example.project_orion.models.Tag;
import com.example.project_orion.payload.dtos.TagDTO;

import jakarta.validation.Valid;

public interface TagService {
    List<Tag> getAllQuestions();

    Tag createTag(String tagName);

    TagDTO updateTag(@Valid TagDTO tagDTO);

    TagDTO deleteTag(Long tagId);
}

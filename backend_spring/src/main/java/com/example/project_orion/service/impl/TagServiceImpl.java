package com.example.project_orion.service.impl;

import com.example.project_orion.exceptions.APIException;
import com.example.project_orion.exceptions.ResourceNotFoundException;
import com.example.project_orion.models.Question;
import com.example.project_orion.models.Tag;
import com.example.project_orion.payload.dtos.TagDTO;
import com.example.project_orion.repository.QuestionRepository;
import com.example.project_orion.repository.TagRepository;
import com.example.project_orion.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    public TagRepository tagRepository;

    @Autowired
    public QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Tag> getAllQuestions() {
        return tagRepository.findAll();
    }

    @Override
    public Tag createTag(String tagName) {
        Tag tagFromDB = tagRepository.findByText(tagName);
        if(tagFromDB != null){
            throw new APIException("Tag with the name " + tagName + " already exists!!!");
        }
        Tag tag = new Tag();
        tag.setText(tagName);
        return tagRepository.save(tag);
    }

    @Override
    public TagDTO updateTag(TagDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        Tag tagFromDB = tagRepository.findById(tag.getTagId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId", tag.getTagId()));
        Tag updatedTag = tagRepository.save(tag);
        return modelMapper.map(updatedTag, TagDTO.class);
    }

    @Override
    public TagDTO deleteTag(Long tagId) {
        Optional<Tag> savedTagOptional = tagRepository.findById(tagId);
        Tag savedTag = savedTagOptional
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "tagId", tagId));

        // Remove the tag from all associated questions
        for (Question question : savedTag.getQuestions()) {
            question.getTagList().remove(savedTag);
            questionRepository.save(question);
        }

        tagRepository.delete(savedTag);
        return modelMapper.map(savedTag, TagDTO.class);
    }
}

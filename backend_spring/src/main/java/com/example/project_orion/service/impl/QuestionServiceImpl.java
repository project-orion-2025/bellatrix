package com.example.project_orion.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.example.project_orion.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project_orion.exceptions.APIException;
import com.example.project_orion.exceptions.ResourceNotFoundException;
import com.example.project_orion.models.Answer;
import com.example.project_orion.models.Option;
import com.example.project_orion.models.Question;
import com.example.project_orion.models.Tag;
import com.example.project_orion.payload.Filter;
import com.example.project_orion.payload.dtos.QuestionDTO;
import com.example.project_orion.payload.responses.QuestionResponse;
import com.example.project_orion.repository.QuestionRepository;
import com.example.project_orion.repository.TagRepository;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

//    private final List<Question> questionList = new ArrayList<>();

    @Override
    public QuestionResponse getAllQuestions(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Question> questionPage = questionRepository.findAll(pageDetails);

        List<Question> questionList = questionPage.getContent();
        List<QuestionDTO> questionDTOS = questionList.stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .toList();
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setContent(questionDTOS);
        questionResponse.setPageNumber(questionPage.getNumber());
        questionResponse.setPageSize(questionPage.getSize());
        questionResponse.setTotalElements(questionPage.getTotalElements());
        questionResponse.setTotalPages(questionPage.getTotalPages());
        questionResponse.setLastPage(questionPage.isLast());
        return questionResponse;
    }

    @Override
    public QuestionDTO createQuestion(String authorName, QuestionDTO questionDTO) {

        Question questionFromDB = questionRepository.findByTitle(questionDTO.getTitle());

        if(questionFromDB != null){
            throw new APIException("Question with title " + questionDTO.getTitle() + " already exists!!!");
        }

        Question question = Question.builder()
                .title(questionDTO.getTitle())
                .description(questionDTO.getDescription())
                .author(authorName)
                .status(questionDTO.getStatus())
                .subject(questionDTO.getSubject())
                .difficulty(questionDTO.getDifficulty())
                .options(questionDTO.getOptions())
                .tagList(questionDTO.getTagList())
                .build();

        if (question.getOptions() != null) {
            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }
        }

        if (question.getTagList() != null) {
            Set<Tag> tags = new HashSet<>();
            for (Tag tag : question.getTagList()) {
                Tag existingTag = tagRepository.findByText(tag.getText());
                // Save new tag if it doesn't exist
                tags.add(Objects.requireNonNullElseGet(existingTag, () -> tagRepository.save(tag)));
            }
            question.setTagList(tags);
        }

        Question savedQuestion = questionRepository.save(question);

        List<Option> options = savedQuestion.getOptions();
        Long correctOptionId = options.get(questionDTO.getCorrectOptionId() - 1).getOptionId();

        // Create an Answer and set the correct option
        Answer answer = new Answer();
        answer.setCorrectOptionId(correctOptionId);
        savedQuestion.setAnswer(answer);

        Question updateQuestion = questionRepository.save(savedQuestion);
        return modelMapper.map(updateQuestion, QuestionDTO.class);
    }

    @Override
    public QuestionDTO getQuestionById(Long questionId) {
        Question questionFromDB = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "questionId", questionId));

        return modelMapper.map(questionFromDB, QuestionDTO.class);
    }

    @Override
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO) {

        Question questionFromDB = questionRepository.findById(questionId)
                .orElseThrow(() -> new APIException("Question with id " + questionId + " not found!"));

        // Only update fields that are provided in the DTO
        if (questionDTO.getTitle() != null) {
            questionFromDB.setTitle(questionDTO.getTitle());
        }
        if (questionDTO.getDescription() != null) {
            questionFromDB.setDescription(questionDTO.getDescription());
        }
        /*
            don't update the author, it will be fixed once it is set
            if (questionDTO.getAuthor() != null) {
                questionFromDB.setAuthor(questionDTO.getAuthor());
            }
        */
        if (questionDTO.getSubject() != null) {
            questionFromDB.setSubject(questionDTO.getSubject());
        }
        if (questionDTO.getDifficulty() != null) {
            questionFromDB.setDifficulty(questionDTO.getDifficulty());
        }
        if (questionDTO.getOptions() != null) {
            List<Option> newOptionList = questionDTO.getOptions();
            List<Option> originalOptionList = questionFromDB.getOptions();
            for(int idx = 0; idx < newOptionList.size(); idx++){
                originalOptionList.get(idx).setText(newOptionList.get(idx).getText());
            }
          // questionFromDB.setOptions(originalOptionList); redundant update
        }
        if (questionDTO.getTagList() != null) {
            Set<Tag> tags = new HashSet<>();
            for (Tag tag : questionDTO.getTagList()) {
                Tag existingTag = tagRepository.findByText(tag.getText());
                tags.add(Objects.requireNonNullElseGet(existingTag, () -> tagRepository.save(tag)));
            }
            questionFromDB.setTagList(tags);
        }
        if(questionDTO.getStatus() != null){
            questionFromDB.setStatus(questionDTO.getStatus());
        }
        Question updatedQuestion = questionRepository.save(questionFromDB);

        if (questionDTO.getCorrectOptionId() != null) {
            List<Option> options = questionFromDB.getOptions();
            Long correctOptionId = options.get(questionDTO.getCorrectOptionId() - 1).getOptionId();

            Answer answer = questionFromDB.getAnswer() != null ? questionFromDB.getAnswer() : new Answer();
            answer.setCorrectOptionId(correctOptionId);
            questionFromDB.setAnswer(answer);
            questionRepository.save(questionFromDB);
        }

        return modelMapper.map(updatedQuestion, QuestionDTO.class);
    }

    @Override
    public QuestionDTO deleteQuestion(Long questionId) {
        Question questionFromDB = questionRepository.findById(questionId)
                .orElseThrow(() -> new APIException("Question with id " + questionId + " not found!"));

        QuestionDTO questionDTO = modelMapper.map(questionFromDB, QuestionDTO.class);
        questionRepository.delete(questionFromDB);
        return questionDTO;
    }

    @Override
    public QuestionResponse fetchAllQuestions(Filter filter, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        /*
            TODO: inactive questions are getting selected
             if admin -> then active and inactive
             if public -> then active only
        */
        if(filter.isEmpty()){
            return getAllQuestions(pageNumber, pageSize, sortBy, sortOrder);
        }

        String subjectValue = (filter.getSubject() != null) ? filter.getSubject().toString() : null;
        String difficultyValue = (filter.getDifficulty() != null) ? filter.getDifficulty().toString() : null;
        Integer tagCount = (filter.getTagList() != null) ? filter.getTagList().size() : null;

        List<Question> questionList;
        if(filter.getTagList() == null){
            questionList = questionRepository.findQuestions(filter.getTitle(), subjectValue, difficultyValue);
        }else{
            questionList = questionRepository.findQuestionsWithTags(filter.getTitle(),
                    subjectValue,
                    difficultyValue,
                    filter.getTagList(),
                    tagCount
            );
        }

        Comparator<Question> comparator = getComparator(sortBy, sortOrder);
        if (comparator != null) {
            questionList.sort(comparator);
        }

        int totalQuestions = questionList.size();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalQuestions);
        List<Question> paginatedQuestions = new ArrayList<>();
        if(startIndex < endIndex){
            paginatedQuestions = questionList.subList(startIndex, endIndex);
        }
        List<QuestionDTO> questionDTOS = paginatedQuestions.stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .toList();
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setContent(questionDTOS);
        questionResponse.setPageNumber(pageNumber);
        questionResponse.setPageSize(pageSize);
        questionResponse.setTotalElements((long) totalQuestions);
        questionResponse.setTotalPages((int) Math.ceil((double) totalQuestions / pageSize));
        questionResponse.setLastPage(endIndex == totalQuestions);
        return questionResponse;

    }

    private Comparator<Question> getComparator(String sortBy, String sortOrder) {
        return switch (sortBy) {
            case "title" -> sortOrder.equalsIgnoreCase("asc") ?
                    Comparator.comparing(Question::getTitle) :
                    Comparator.comparing(Question::getTitle).reversed();
            case "subject" -> sortOrder.equalsIgnoreCase("asc") ?
                    Comparator.comparing(Question::getSubject) :
                    Comparator.comparing(Question::getSubject).reversed();
            case "difficulty" -> sortOrder.equalsIgnoreCase("asc") ?
                    Comparator.comparing(Question::getDifficulty) :
                    Comparator.comparing(Question::getDifficulty).reversed();
            case "questionId" ->
                    sortOrder.equalsIgnoreCase("asc") ?
                            Comparator.comparingLong(Question::getQuestionId) :
                            Comparator.comparingLong(Question::getQuestionId).reversed();
            default -> null;
        };
    }

}


/*   
Note on Bidirectional Relationships in JPA:
In a bidirectional relationship, Spring JPA does not automatically
update the association on both sides. For example, when persisting
a Question with its Options, the "question" field in the Option entity
must be explicitly set. This ensures that the foreign key (QUESTION_ID)
in the options table is populated.

Reason: JPA does not infer the relationship on the inverse (child) side
unless explicitly mapped in the code. Without setting the relationship
on both sides, the child entity (Option) won't know about the parent
entity (Question), resulting in a null foreign key.

Example fix:
Before saving the Question, ensure each Option has its "question" field set:
*/



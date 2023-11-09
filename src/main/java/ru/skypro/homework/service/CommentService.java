package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MyMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {
    private final MyMapper mapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public Comments getCommentsByAds(Integer id) {
        Optional<AdEntity> ad = adRepository.findById(id);
        if(ad.isEmpty()){
            throw new AdNotFoundException();
        }
        List<CommentDto> comments = commentRepository.findAllByAd(ad.get()).stream()
                .map(entity -> mapper.map(entity))
                .collect(Collectors.toList());
        return new Comments(comments.size(), comments);
    }

    public CommentDto addComment(Integer id, CreateOrUpdateComment text) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username);
        Optional<AdEntity> ad = adRepository.findById(id);
        if(ad.isEmpty()){
            throw new AdNotFoundException();
        }
        CommentEntity comment = mapper.map(text, user, ad.get());
        commentRepository.save(comment);
        return mapper.map(comment);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentDto updateComment(Integer commentId, CreateOrUpdateComment text) {
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            throw new CommentNotFoundException();
        }
        comment.get().setText(text.getText());
        commentRepository.save(comment.get());
        return mapper.map(comment.get());
    }
}

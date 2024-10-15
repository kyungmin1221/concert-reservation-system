package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

   @Transactional
   public UserResponseDto registerUser(UserRequestDto requestDto) {

       String username = requestDto.getName();
       if(userRepository.findByName(username).isPresent()) {
           throw new IllegalArgumentException("이미 등록된 유저의 이름입니다.");
       }

       User user = User.builder()
               .name(username)
               .build();

       userRepository.save(user);
       return new UserResponseDto(user.getName(), user.getUuid());
   }

}

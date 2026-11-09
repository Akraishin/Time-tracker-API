package training.restapi.demo1;

import jakarta.transaction.Transactional;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import training.restapi.demo1.dtos.*;
import training.restapi.demo1.exceptions.BadRequestException;
import training.restapi.demo1.exceptions.ConflictException;
import training.restapi.demo1.exceptions.NotFoundException;
import training.restapi.demo1.models.User;
import training.restapi.demo1.models.UserFilter;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    Page<UserDTO> getUsers(Pageable pageable, UserFilter filter) {
        Specification<User> specification = UserSpecification.withFilter(filter);

        Page<User> page;

        try{
            page = userRepository.findAll(specification, pageable);
        }catch(PropertyReferenceException e){
            throw new BadRequestException("Unknown sorting parameter " + pageable.getSort().toString());
        }

        return page.map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setActive(user.isActive());
            return dto;
        });
    }

    @Transactional
    UserDTO createUser(UserDTO userDTO) {
        User user = new User();

        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new ConflictException("This email already registered: " + userDTO.getEmail());
        };
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setActive(true);
        user.setUsername(userDTO.getUserName());

        User createdUser = userRepository.save(user);

        userDTO.setId(createdUser.getId());
        userDTO.setActive(createdUser.isActive());
        return userDTO;
    }

    UserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null){
            throw new NotFoundException("User with id " + id + " not found");
        }

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setUserName(user.getUsername());
        return dto;
    }

    @Transactional
    UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            throw new NotFoundException("User with id " + id + " not found");
        }

        if(!user.getEmail().equals(userDTO.getEmail()) & userRepository.existsByEmail(userDTO.getEmail())){
            throw new ConflictException("This email already registered: " + userDTO.getEmail());
        }

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUserName());

        userRepository.save(user);

        userDTO.setActive(user.isActive());
        userDTO.setUserName(userDTO.getUserName());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setName(userDTO.getName());
        userDTO.setId(id);


        return userDTO;
    }

    @Transactional
    void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null){
            throw new NotFoundException("User with id " + id + " not found");
        }
        user.setActive(false);
        userRepository.save(user);
    }


}

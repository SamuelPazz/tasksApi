package com.samuelpaz.tasksApi.services;

import com.samuelpaz.tasksApi.models.User;
import com.samuelpaz.tasksApi.models.enums.ProfileEnum;
import com.samuelpaz.tasksApi.repositories.UserRepository;
import com.samuelpaz.tasksApi.services.exceptions.DataBindingViolationException;
import com.samuelpaz.tasksApi.services.exceptions.ObjectNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// @Service: indica que a classe é um serviço do Spring.
// Serviços contêm a regra de negócio e interagem com os repositórios.
@Service
public class UserService {

    // Dependência do codificador de senha BCrypt para criptografar as senhas
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Repositório de usuários que permite interagir com a base de dados
    private final UserRepository userRepository;

    // Construtor da classe, injeta o codificador BCrypt e o repositório de usuários
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    // Método que busca um usuário pelo ID, lança exceção se o usuário não for encontrado
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "User not found! Id:" + id + ", Type: " + User.class.getName()
        ));
    }

    // Método que cria um novo usuário, criptografa a senha e atribui o perfil de usuário padrão
    @Transactional
    public User create(User obj) {
        // Define o ID como null para garantir a criação de um novo usuário
        // Criptografa a senha antes de salvar no banco de dados
        // Define o perfil padrão como "USER"
        // Salva o usuário no banco de dados
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    // Método que atualiza os dados de um usuário, incluindo a criptografia da nova senha
    @Transactional
    public User update(User obj) {
        // Atualiza a senha do usuário
        // Criptografa a nova senha
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    // Método que deleta um usuário pelo ID, lança exceção se houver entidades relacionadas
    public void delete(Long id) {
        // Verifica se o usuário existe
        findById(id);
        try {
            // Deleta o usuário pelo ID
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            // Lança exceção personalizada em caso de violação de integridade referencial
            throw new DataBindingViolationException(
                    "It's not possible to delete as there are related entities!");
        }
    }
}

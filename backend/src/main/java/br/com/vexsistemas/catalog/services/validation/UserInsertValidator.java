package br.com.vexsistemas.catalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.vexsistemas.catalog.dto.UserInsertDTO;
import br.com.vexsistemas.catalog.entities.User;
import br.com.vexsistemas.catalog.repositories.UserRepository;
import br.com.vexsistemas.catalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {


    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        User user = userRepository.findByEmail(dto.getEmail());
        if (user != null) {
            list.add(new FieldMessage("email", "E-mail já cadastrado"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
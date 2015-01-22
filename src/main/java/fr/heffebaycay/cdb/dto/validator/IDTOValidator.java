package fr.heffebaycay.cdb.dto.validator;

import java.util.List;

public interface IDTOValidator<T> {

  boolean validate(T object, List<String> errors);
  
}

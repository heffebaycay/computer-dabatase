package fr.heffebaycay.cdb.dao.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;

public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
    
    if(entityValue != null) {
      return Timestamp.valueOf(entityValue);
    } else {
      return null;
    }
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp databaseValue) {
    if(databaseValue != null) {
      return databaseValue.toLocalDateTime();
    } else {
      return null;
    }
  }

  
  
}

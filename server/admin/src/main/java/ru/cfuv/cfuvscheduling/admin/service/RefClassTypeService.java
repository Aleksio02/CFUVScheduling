package ru.cfuv.cfuvscheduling.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassTypeConverter;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import javax.persistence.EntityNotFoundException;

@Service
public class RefClassTypeService {

    @Autowired
    private RefClassTypeDao refClassTypeDao;

    public List<RefClassTypeBom> findAll() {
        List<RefClassTypeDto> dtos = refClassTypeDao.findAll();
        List<RefClassTypeBom> boms = new ArrayList<RefClassTypeBom>();

        for (int i = 0; i < dtos.size(); i++) {
            RefClassTypeBom bom = new RefClassTypeBom();
            new RefClassTypeConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

    public RefClassTypeBom createClassType(RefClassTypeBom type) {
        if (type.getName() == null || type.getName().trim().length() < 3) {
            throw new IncorrectRequestDataException("The class type name cannot be null and should contain three or more symbols");
        }
        try {
            type.setId(null);
            RefClassTypeDto typeDto = new RefClassTypeDto();
            RefClassTypeConverter converter = new RefClassTypeConverter();
            converter.toDto(type, typeDto);
            refClassTypeDao.save(typeDto);
            converter.fromDto(typeDto, type);
            return type;
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Type %s already exists or it has empty value".formatted(type.getName()));
        }
    }

    public RefClassTypeBom renameClassType(RefClassTypeBom type) {
        try {
            if (type.getName() == null || type.getName().trim().length() < 3) {
                throw new IncorrectRequestDataException("The class type name cannot be null and should contain three or more symbols");
            }
            if (type.getId() == null || !refClassTypeDao.existsById(type.getId())) {
                throw new IllegalArgumentException("Id shouldn't be null or type with this id isn't exist!");
            }
            RefClassTypeDto typeDto = new RefClassTypeDto();
            RefClassTypeConverter converter = new RefClassTypeConverter();
            converter.toDto(type, typeDto);
            refClassTypeDao.save(typeDto);
            converter.fromDto(typeDto, type);
            return type;
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestDataException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Type %s already exists or it has empty value".formatted(type.getName()));
        }
    }

    public void deleteClassType(Integer typeId) {
        if (!refClassTypeDao.existsById(typeId)) {
            throw new EntityNotFoundException("Class type with ID %d not found".formatted(typeId));
        }
        refClassTypeDao.deleteById(typeId);
    }
}

package ru.cfuv.cfuvscheduling.admin.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefClassTypeServiceTest {

    @InjectMocks
    private RefClassTypeService refClassTypeService;

    @Mock
    private RefClassTypeDao refClassTypeDao;

    @Test
    public void testFindAll() {
        List<RefClassTypeDto> dtos = new ArrayList<>();
        dtos.add(new RefClassTypeDto());
        dtos.add(new RefClassTypeDto());
        when(refClassTypeDao.findAll()).thenReturn(dtos);

        List<RefClassTypeBom> result = refClassTypeService.findAll();

        Assertions.assertEquals(dtos.size(), result.size());
        verify(refClassTypeDao, times(1)).findAll();
    }

    @Test
    public void testCreateClassType_Success() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setName("Valid Name");
        RefClassTypeDto refClassTypeDto = new RefClassTypeDto();
        when(refClassTypeDao.save(any(RefClassTypeDto.class))).thenReturn(refClassTypeDto);

        RefClassTypeBom result = refClassTypeService.createClassType(refClassTypeBom);


        assertEquals(refClassTypeBom.getName(), result.getName());
    }

    @Test
    public void testCreateClassType_InvalidName() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setName("In");

        Assertions.assertThrows(IncorrectRequestDataException.class, () -> refClassTypeService.createClassType(refClassTypeBom));
        verify(refClassTypeDao, never()).save(any(RefClassTypeDto.class));
    }

    @Test
    public void testCreateClassType_DataIntegrityViolation() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setName("Valid Name");
        when(refClassTypeDao.save(any(RefClassTypeDto.class)))
                .thenThrow(new DataIntegrityViolationException("Violation of unique constraint"));

        Assertions.assertThrows(AlreadyExistsException.class, () -> refClassTypeService.createClassType(refClassTypeBom));
        verify(refClassTypeDao, times(1)).save(any(RefClassTypeDto.class));
    }

    @Test
    public void testRenameClassType_Success() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setId(1);

        refClassTypeBom.setName("Valid Name");
        RefClassTypeDto refClassTypeDto = new RefClassTypeDto();
        when(refClassTypeDao.existsById(1)).thenReturn(true);
        when(refClassTypeDao.save(any(RefClassTypeDto.class))).thenReturn(refClassTypeDto);

        refClassTypeService.renameClassType(refClassTypeBom);

        verify(refClassTypeDao, times(1)).existsById(1);
    }

    @Test
    public void testRenameClassType_InvalidName() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setId(1);
        refClassTypeBom.setName("NT");

        Assertions.assertThrows(IncorrectRequestDataException.class, () -> refClassTypeService.renameClassType(refClassTypeBom));
        verify(refClassTypeDao, never()).save(any(RefClassTypeDto.class));
    }

    @Test
    public void testRenameClassType_TypeNotFound() {
        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        refClassTypeBom.setId(1);
        refClassTypeBom.setName("Valid Name");
        when(refClassTypeDao.existsById(1)).thenReturn(false);

        Assertions.assertThrows(IncorrectRequestDataException.class, () -> refClassTypeService.renameClassType(refClassTypeBom));
        verify(refClassTypeDao, times(1)).existsById(1);
        verify(refClassTypeDao, never()).save(any(RefClassTypeDto.class));
    }

    @Test
    public void testDeleteClassType_Success() {
        Integer typeId = 1;
        when(refClassTypeDao.existsById(typeId)).thenReturn(true);

        refClassTypeService.deleteClassType(typeId);

        verify(refClassTypeDao, times(1)).deleteById(typeId);
    }

    @Test
    public void testDeleteClassType_TypeNotFound() {
        Integer typeId = 1;
        when(refClassTypeDao.existsById(typeId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> refClassTypeService.deleteClassType(typeId));
        verify(refClassTypeDao, times(1)).existsById(typeId);
        verify(refClassTypeDao, never()).deleteById(anyInt());
    }
}

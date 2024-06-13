package ru.cfuv.cfuvscheduling.admin.service;

import javassist.LoaderClassPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import ru.cfuv.cfuvscheduling.admin.dao.RefClassDurationsDao;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefClassDurationsServiceTest {

    @InjectMocks
    private RefClassDurationsService refClassDurationsService;

    @Mock
    private RefClassDurationsDao refClassDurationsDao;

    @Test
    public void testFindAll() {
        List<RefClassDurationsDto> dtos = new ArrayList<>();
        dtos.add(new RefClassDurationsDto());
        dtos.add(new RefClassDurationsDto());
        dtos.add(new RefClassDurationsDto());

        when(refClassDurationsDao.findAll()).thenReturn(dtos);

        List<RefClassDurationsBom> result = refClassDurationsService.findAll();
        assertEquals(dtos.size(), result.size());
        verify(refClassDurationsDao, Mockito.times(1)).findAll();
    }

    @Test
    public void testAddClassDuration_NumberIsNull() {
        RefClassDurationsBom refClassDurationsBom = new RefClassDurationsBom();

        refClassDurationsBom.setNumber(null);
        refClassDurationsBom.setStartTime(LocalTime.of(21, 0));
        refClassDurationsBom.setEndTime(LocalTime.of(22, 30));

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.addClassDuration(refClassDurationsBom));
    }

    @Test
    public void testAddClassDuration_StartTimeIsNull() {
        RefClassDurationsBom refClassDurationsBom = new RefClassDurationsBom();

        refClassDurationsBom.setNumber(8);
        refClassDurationsBom.setStartTime(null);
        refClassDurationsBom.setEndTime(LocalTime.of(22, 30));

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.addClassDuration(refClassDurationsBom));
    }

    @Test
    public void testAddClassDuration_EndTimeIsNull() {
        RefClassDurationsBom refClassDurationsBom = new RefClassDurationsBom();

        refClassDurationsBom.setNumber(8);
        refClassDurationsBom.setStartTime(LocalTime.of(21, 0));
        refClassDurationsBom.setEndTime(null);

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.addClassDuration(refClassDurationsBom));
    }

    @Test
    public void testAddClassDuration_NumberAlreadyExists() {
        RefClassDurationsBom bom = new RefClassDurationsBom();
        bom.setNumber(1);
        bom.setStartTime(LocalTime.of(21, 0));
        bom.setEndTime(LocalTime.of(22, 30));

        RefClassDurationsDto dto = new RefClassDurationsDto();
        dto.setNumber(1);
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setEndTime(LocalTime.of(9, 30));

        when(refClassDurationsDao.findById(bom.getNumber())).thenReturn(Optional.of(dto));

        assertThrows(AlreadyExistsException.class, () -> refClassDurationsService.addClassDuration(bom));
    }

    @Test
    public void testChangeClassDuration_Success() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(1);
        expectedBom.setStartTime(LocalTime.of(8, 1));
        expectedBom.setEndTime(LocalTime.of(9, 31));

        RefClassDurationsDto dto = new RefClassDurationsDto();
        dto.setNumber(1);
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setEndTime(LocalTime.of(9, 0));

        when(refClassDurationsDao.findById(expectedBom.getNumber())).thenReturn(Optional.of(dto));
        when(refClassDurationsDao.findByStartTimeAndEndTime(expectedBom.getStartTime(), expectedBom.getEndTime())).
                thenReturn(Optional.empty());

        refClassDurationsService.changeClassDuration(expectedBom);

        verify(refClassDurationsDao, times(1)).save(any());
    }

    @Test
    public void testChangeClassDuration_NumberIsNull() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(null);
        expectedBom.setStartTime(LocalTime.of(8, 1));
        expectedBom.setEndTime(LocalTime.of(9, 31));

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.changeClassDuration(expectedBom));
    }

    @Test
    public void testChangeClassDuration_StartTimeIsNull() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(1);
        expectedBom.setStartTime(null);
        expectedBom.setEndTime(LocalTime.of(9, 31));

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.changeClassDuration(expectedBom));
    }

    @Test
    public void testChangeClassDuration_EndTimeIsNull() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(1);
        expectedBom.setStartTime(LocalTime.of(8, 1));
        expectedBom.setEndTime(null);

        assertThrows(IncorrectRequestDataException.class, () -> refClassDurationsService.changeClassDuration(expectedBom));
    }

    @Test
    public void testChangeClassDuration_NumberNotFound() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(21);
        expectedBom.setStartTime(LocalTime.of(8, 1));
        expectedBom.setEndTime(LocalTime.of(9, 31));

        when(refClassDurationsDao.findById(expectedBom.getNumber())).
                thenThrow(new EntityNotFoundException("Class duration with this ID was not found."));

        assertThrows(EntityNotFoundException.class, () -> refClassDurationsService.changeClassDuration(expectedBom));
    }

    @Test
    public void testChangeClassDuration_TimeAlreadyExists() {
        RefClassDurationsBom expectedBom = new RefClassDurationsBom();

        expectedBom.setNumber(3);
        expectedBom.setStartTime(LocalTime.of(8, 0));
        expectedBom.setEndTime(LocalTime.of(9, 30));

        RefClassDurationsDto dto = new RefClassDurationsDto();
        dto.setNumber(3);
        dto.setStartTime(LocalTime.of(8, 1));
        dto.setEndTime(LocalTime.of(9, 31));

        RefClassDurationsDto dto2 = new RefClassDurationsDto();
        dto2.setNumber(1);
        dto2.setStartTime(LocalTime.of(8, 0));
        dto2.setEndTime(LocalTime.of(9, 30));

        when(refClassDurationsDao.findById(expectedBom.getNumber())).thenReturn(Optional.of(dto));
        when(refClassDurationsDao.findByStartTimeAndEndTime(expectedBom.getStartTime(), expectedBom.getEndTime())).
                thenReturn(Optional.of(dto2));

        assertThrows(AlreadyExistsException.class, () -> refClassDurationsService.changeClassDuration(expectedBom));
    }

    @Test
    public void testDeleteClassDuration_Success() {
        Integer expectedID = 1;

        when(refClassDurationsDao.existsById(expectedID)).thenReturn(true);

        refClassDurationsService.deleteClassDuration(expectedID);

        verify(refClassDurationsDao, times(1)).deleteById(expectedID);
    }

    @Test
    public void testDeleteClassDuration_EntityNotFound() {
        Integer expectedID = 1;

        when(refClassDurationsDao.existsById(expectedID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> refClassDurationsService.deleteClassDuration(expectedID));
    }
}
package ru.cfuv.cfuvscheduling.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.admin.dao.RefClassDurationsDao;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;


@Service
public class RefClassDurationsService {

    @Autowired
    private RefClassDurationsDao refClassDurationsDao;

    public List<RefClassDurationsBom> findAll() {
        List<RefClassDurationsDto> dtos = refClassDurationsDao.findAll();
        List<RefClassDurationsBom> boms = new ArrayList<RefClassDurationsBom>();

        for (int i = 0; i < dtos.size(); i++) {
            RefClassDurationsBom bom = new RefClassDurationsBom();
            new RefClassDurationsConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

    public RefClassDurationsBom addClassDuration(RefClassDurationsBom classDuration) throws AlreadyExistsException {
        if (classDuration.getNumber() == null || classDuration.getStartTime() == null || classDuration.getEndTime() == null) {
            throw new IncorrectRequestDataException("Obj fields can't be null.");
        }

        RefClassDurationsDto existingClassDuration = refClassDurationsDao.findById(classDuration.getNumber()).
                orElse(null);
        if (existingClassDuration != null) {
            throw new AlreadyExistsException("A class duration with this ID already exists.");
        }

        classDuration.setStartTime(LocalTime.parse(classDuration.getStartTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));
        classDuration.setEndTime(LocalTime.parse(classDuration.getEndTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));

        if (refClassDurationsDao.existsByStartTimeAndEndTime(classDuration.getStartTime(), classDuration.getEndTime())) {
            throw new AlreadyExistsException("This duration already exists.");
        }

        RefClassDurationsDto newDto = new RefClassDurationsDto();
        new RefClassDurationsConverter().toDto(classDuration, newDto);
        refClassDurationsDao.save(newDto);

        return classDuration;
    }

    public void changeClassDuration(RefClassDurationsBom newClassDurationBom) {
        if (newClassDurationBom.getNumber() == null
                || newClassDurationBom.getStartTime() == null
                || newClassDurationBom.getEndTime() == null) {
            throw new IncorrectRequestDataException("Obj fields can't be null.");
        }

        RefClassDurationsDto existsClassDurationDto = refClassDurationsDao.findById(newClassDurationBom.getNumber()).
                orElseThrow(() -> new EntityNotFoundException("Class duration with this ID was not found."));
        refClassDurationsDao.findByStartTimeAndEndTime(newClassDurationBom.getStartTime(), newClassDurationBom.getEndTime()).
                ifPresent((i) -> {
                    throw new AlreadyExistsException("This class duration already exists.");
                });

        existsClassDurationDto.setStartTime(newClassDurationBom.getStartTime());
        existsClassDurationDto.setEndTime(newClassDurationBom.getEndTime());

        refClassDurationsDao.save(existsClassDurationDto);
    }

    public void deleteClassDuration(Integer id) {
        if (!refClassDurationsDao.existsById(id)) {
            throw new EntityNotFoundException("The class duration with this ID was not found.");
        }
        refClassDurationsDao.deleteById(id);
    }

}

package ru.cfuv.cfuvscheduling.admin.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.admin.dao.RefClassDurationsDao;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

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
        if (classDuration.getNumber() == null || classDuration.getStartTime() == null ||
            classDuration.getEndTime() == null) {
            throw new IncorrectRequestDataException("Obj fields can't be null.");
        }

        RefClassDurationsDto existingClassDuration = refClassDurationsDao.findById(classDuration.getNumber()).
            orElse(null);
        if (existingClassDuration != null) {
            throw new AlreadyExistsException("A class duration with this ID already exists.");
        }

        List<RefClassDurationsDto> tableClassDuration = refClassDurationsDao.findAll();
        for (int i = 0; i < tableClassDuration.size(); i++) {
            if (tableClassDuration.get(i).getStartTime().getHour() == classDuration.getStartTime().getHour() &&
                tableClassDuration.get(i).getStartTime().getMinute() == classDuration.getStartTime().getMinute()) {
                throw new AlreadyExistsException("A class duration with this startTime already exists.");
            }
            if (tableClassDuration.get(i).getEndTime().getHour() == classDuration.getEndTime().getHour() &&
                tableClassDuration.get(i).getEndTime().getMinute() == classDuration.getEndTime().getMinute()) {
                throw new AlreadyExistsException("A class duration with this endTime already exists.");
            }
        }

        RefClassDurationsDto newDto = new RefClassDurationsDto();
        new RefClassDurationsConverter().toDto(classDuration, newDto);
        refClassDurationsDao.save(newDto);

        return classDuration;
    }

}

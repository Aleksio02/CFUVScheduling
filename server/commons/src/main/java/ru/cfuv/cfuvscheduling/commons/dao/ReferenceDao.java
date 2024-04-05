package ru.cfuv.cfuvscheduling.commons.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.cfuv.cfuvscheduling.commons.dao.dto.ReferenceDto;

@NoRepositoryBean
public interface ReferenceDao<T extends ReferenceDto, ID> extends JpaRepository<T, ID> {
    Optional<T> findByName(String name);
}

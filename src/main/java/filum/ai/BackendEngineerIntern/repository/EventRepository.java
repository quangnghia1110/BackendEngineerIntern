package filum.ai.BackendEngineerIntern.repository;

import filum.ai.BackendEngineerIntern.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface EventRepository extends PagingAndSortingRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

}

package training.restapi.demo1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import training.restapi.demo1.models.TimeLog;

import java.time.LocalDate;

public interface TimeRepository extends JpaRepository<TimeLog, Long>, JpaSpecificationExecutor<TimeLog> {

    @EntityGraph(attributePaths = {"user", "project"})
    Page<TimeLog> findAll(Pageable pageable);

    @Query("Select coalesce(sum(tl.hours), 0) from TimeLog tl where tl.user.id = :userId and tl.logDate = :date")
    Integer countHoursForUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}

package training.restapi.demo1;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.restapi.demo1.dtos.TimeLogDTO;
import training.restapi.demo1.exceptions.BadRequestException;
import training.restapi.demo1.exceptions.ConflictException;
import training.restapi.demo1.exceptions.NotFoundException;
import training.restapi.demo1.models.Project;
import training.restapi.demo1.models.TimeLog;
import training.restapi.demo1.models.TimeLogFilter;
import training.restapi.demo1.models.User;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TimeLogMapper timeLogMapper;

    public TimeService(TimeRepository timeRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository,
                       TimeLogMapper timeLogMapper) {
        this.timeRepository = timeRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.timeLogMapper = timeLogMapper;
    }

    @Transactional(readOnly = true)
    public Page<TimeLogDTO> getTimeLogs(Pageable pageable, TimeLogFilter filter) {
        Specification<TimeLog> specification = TimeLogSpecification.withFilter(filter);

        Page<TimeLog> timeLogPage;
        try {
            timeLogPage = timeRepository.findAll(specification, pageable);
        } catch (PropertyReferenceException e) {
            throw new BadRequestException("Unknown sorting parameter " + pageable.getSort().toString());
        }

        return timeLogPage.map(timeLogMapper::toDTO);
    }

    @Transactional
    public TimeLogDTO createTimeLog(TimeLogDTO dto) {
        if (dto.getHours() > 24) {
            throw new BadRequestException("Total logged time cannot exceed 24 hours per day");
        }

        if (timeRepository.countHoursForUserAndDate(dto.getUserId(), dto.getLogDate()) + dto.getHours() > 24) {
            throw new BadRequestException("Total logged time cannot exceed 24 hours per day");
        }

        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found: " + dto.getUserId());
        }

        if (!user.isActive()){
            throw new ConflictException("User is not active");
        }

        Project project = projectRepository.findById(dto.getProjectId()).orElse(null);
        if (project == null) {
            throw new NotFoundException("Project not found: " + dto.getProjectId());
        }
        if (project.getDeadLine()  != null){
            if (project.getDeadLine().isBefore(dto.getLogDate())) {
                throw new ConflictException("Deadline already passed");
            }}

        TimeLog timeLog = timeLogMapper.toEntity(dto, project, user);
        TimeLog createdTimeLog = timeRepository.save(timeLog);

        return timeLogMapper.toDTO(createdTimeLog);
    }
}

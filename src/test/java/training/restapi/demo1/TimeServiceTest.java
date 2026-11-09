package training.restapi.demo1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import training.restapi.demo1.dtos.TimeLogDTO;
import training.restapi.demo1.exceptions.BadRequestException;
import training.restapi.demo1.exceptions.ConflictException;
import training.restapi.demo1.exceptions.NotFoundException;
import training.restapi.demo1.models.Project;
import training.restapi.demo1.models.TimeLog;
import training.restapi.demo1.models.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private TimeLogMapper timeLogMapper = new TimeLogMapper();

    @InjectMocks
    private TimeService timeService;

    private TimeLogDTO timeLogDTO;

    private Project project;

    private User user;

    @BeforeEach
    void setUp() {
        timeLogDTO = new TimeLogDTO();
        timeLogDTO.setDescription("test");
        timeLogDTO.setProjectId(1L);
        timeLogDTO.setUserId(1L);
        timeLogDTO.setLogDate(LocalDate.now());
        timeLogDTO.setHours(5);

        project = new Project();
        project.setId(1L);
        project.setName("test project");
        project.setDeadLine(LocalDate.of(2027, 1, 1));
        project.setActive(true);

        user = new User();
        user.setId(1L);
        user.setName("test user");
        user.setEmail("test@test.com");
        user.setUsername("test user");
        user.setActive(true);
    }

    @Test
    void CreateTimeLogSuccess() {
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        when(timeRepository.save(any(TimeLog.class))).thenAnswer(invocationOnMock -> {
            TimeLog timeLog = invocationOnMock.getArgument(0);
            timeLog.setId(1L);
            return timeLog;
        });

        TimeLogDTO timeLog = timeService.createTimeLog(timeLogDTO);

        Assertions.assertNotNull(timeLog.getProjectId());
        Assertions.assertEquals(timeLogDTO.getDescription(), timeLog.getDescription());
        Assertions.assertEquals(project.getName(), timeLog.getProjectName());
        Assertions.assertEquals(user.getUsername(), timeLog.getUserName());

        verify(timeRepository, times(1)).save(any(TimeLog.class));
    }

    @Test
    void CreateTimeLogFail_24hLimitExceeded() {
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(25);
        Assertions.assertThrows(BadRequestException.class, () -> timeService.createTimeLog(timeLogDTO));

        verifyNoInteractions(userRepository, projectRepository);
    }

    @Test
    void CreateTimeLogFail_24hLimitExceededCombined() {
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(24);
        Assertions.assertThrows(BadRequestException.class, () -> timeService.createTimeLog(timeLogDTO));

        verifyNoInteractions(userRepository, projectRepository);
    }

    @Test
    void CreateTimeLogFail_UserNotExist() {
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(0);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> timeService.createTimeLog(timeLogDTO));
    }

    @Test
    void CreateTimeLogFail_UserNotActive() {
        user.setActive(false);
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(ConflictException.class, () -> timeService.createTimeLog(timeLogDTO));
    }

    @Test
    void CreateTimeLogFail_ProjectNotExist() {
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> timeService.createTimeLog(timeLogDTO));
    }

    @Test
    void CreateTimeLogFail_DeadlineIsOver() {
        project.setDeadLine(LocalDate.of(2026, 1, 1));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(timeRepository.countHoursForUserAndDate(1L, LocalDate.now())).thenReturn(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Assertions.assertThrows(ConflictException.class, () -> timeService.createTimeLog(timeLogDTO));
    }
}

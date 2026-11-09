package training.restapi.demo1;

import org.springframework.stereotype.Component;
import training.restapi.demo1.dtos.TimeLogDTO;
import training.restapi.demo1.models.Project;
import training.restapi.demo1.models.TimeLog;
import training.restapi.demo1.models.User;

@Component
public class TimeLogMapper {

    public TimeLogDTO toDTO(TimeLog timeLog) {
        if (timeLog == null) {
            return null;
        }

        TimeLogDTO dto = new TimeLogDTO();
        dto.setId(timeLog.getId());
        dto.setLogDate(timeLog.getLogDate());
        dto.setHours(timeLog.getHours());
        dto.setDescription(timeLog.getDescription());

        if (timeLog.getProject() != null) {
            dto.setProjectId(timeLog.getProject().getId());
            dto.setProjectName(timeLog.getProject().getName());
        }

        if (timeLog.getUser() != null) {
            dto.setUserId(timeLog.getUser().getId());
            dto.setUserName(timeLog.getUser().getUsername());
        }

        return dto;
    }

    public TimeLog toEntity(TimeLogDTO dto, Project project, User user) {
        if (dto == null) {
            return null;
        }

        TimeLog timeLog = new TimeLog();
        timeLog.setHours(dto.getHours());
        timeLog.setLogDate(dto.getLogDate());
        timeLog.setDescription(dto.getDescription());
        timeLog.setProject(project);
        timeLog.setUser(user);

        return timeLog;
    }
}

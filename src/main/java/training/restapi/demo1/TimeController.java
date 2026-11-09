package training.restapi.demo1;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.restapi.demo1.dtos.TimeLogDTO;
import training.restapi.demo1.models.TimeLogFilter;

@RestController
public class TimeController {

    private final TimeService timeLogService;

    public TimeController(TimeService timeService) {
        this.timeLogService = timeService;
    }

    @GetMapping("/timelogs")
    public Page<TimeLogDTO> getAllTimeLogs(@PageableDefault(sort = "logDate") Pageable pageable, TimeLogFilter filter){
        return timeLogService.getTimeLogs(pageable, filter);
    }

    @PostMapping("/timelogs")
    @ResponseStatus(HttpStatus.CREATED)
    public TimeLogDTO postTimeLog(@Valid @RequestBody TimeLogDTO dto){
       return timeLogService.createTimeLog(dto);
    }
}

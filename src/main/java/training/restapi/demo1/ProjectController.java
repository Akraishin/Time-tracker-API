package training.restapi.demo1;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.restapi.demo1.dtos.ProjectDTO;
import training.restapi.demo1.models.ProjectFilter;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    Page<ProjectDTO> getAllProjects(Pageable pageable, ProjectFilter projectFilter) {
        return  projectService.getAllProjects(pageable,projectFilter);
    }

    @GetMapping("/projects/{id}")
    ProjectDTO getProjectById(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @DeleteMapping("/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
    }


    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    ProjectDTO createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }
}

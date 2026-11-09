package training.restapi.demo1;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import training.restapi.demo1.dtos.ProjectDTO;
import training.restapi.demo1.exceptions.BadRequestException;
import training.restapi.demo1.exceptions.NotFoundException;
import training.restapi.demo1.models.Project;
import training.restapi.demo1.models.ProjectFilter;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public Page<ProjectDTO> getAllProjects(Pageable pageable, ProjectFilter filter) {

        Specification<Project> specification = ProjectSpecification.withFilter(filter);

        Page<Project> page;

        try{
            page = projectRepository.findAll(specification, pageable);
        }catch (PropertyReferenceException e){
            throw new BadRequestException("Unknown sorting parameter: " + pageable.getSort().toString());
        }

        return page.map(project -> {
            ProjectDTO dto = new ProjectDTO();
            dto.setActive(project.isActive());
            dto.setId(project.getId());
            dto.setName(project.getName());
            dto.setDeadLine(project.getDeadLine());
            return dto;
        });
    }


    public ProjectDTO createProject (ProjectDTO dto){

        Project project = new Project();

        project.setActive(true);
        project.setName(dto.getName());
        project.setDeadLine(dto.getDeadLine());

        Project createdProject = projectRepository.save(project);

        dto.setId(createdProject.getId());
        return dto;
    }

    public void deleteProject (Long id){
        Project project = projectRepository.findById(id).orElse(null);

        if(project == null){
            throw new NotFoundException("Project not found");
        }

        project.setActive(false);
        projectRepository.save(project);
    }

    public ProjectDTO getProject (Long id){
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null){
            throw new BadRequestException("Project not found");
        }
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setActive(project.isActive());
        dto.setDeadLine(project.getDeadLine());
        return dto;
    }




}

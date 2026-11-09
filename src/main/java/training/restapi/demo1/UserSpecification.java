package training.restapi.demo1;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import training.restapi.demo1.models.UserFilter;
import training.restapi.demo1.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> withFilter(UserFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.isActive() != null){
                predicates.add(criteriaBuilder.equal(root.get("isActive"), filter.isActive()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
        );
    }
}

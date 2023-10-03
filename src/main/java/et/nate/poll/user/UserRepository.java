package et.nate.poll.user;

import et.nate.poll.user.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByEmail(String email);

    void save(User user);

    Boolean existsUserById(Long id);

    Boolean existsByEmail(String email);

}

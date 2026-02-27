package com.booleanuk.api.repository;

import com.booleanuk.api.models.Follow;
import com.booleanuk.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

}

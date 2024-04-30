package com.whatpl.project.repository;

import com.whatpl.project.domain.ProjectLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {

    Optional<ProjectLike> findByProjectIdAndMemberId(Long projectId, Long memberId);

    @EntityGraph(attributePaths = {"member"})
    Optional<ProjectLike> findWithMemberById(Long memberId);
}

package edu.java.scrapper.repository.subscription.jpa;

import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.dto.SubscriptionId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionDto, SubscriptionId> {
    @Query(value = "select * from subscription where link_id = :linkId", nativeQuery = true)
    List<SubscriptionDto> findAllByLinkId(@Param("linkId") long linkId);
}

package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.Seat;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaSeatRepository extends JpaRepository<Seat,Long>{

    // 좌석 조회 시 비관적 락 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.seatNumber = :seatNumber AND s.concertEvent.id = :eventId")
    Optional<Seat> findSeatForUpdate(@Param("seatNumber") String seatNumber,
                                     @Param("eventId") Long eventId);

    @Query("SELECT s FROM Seat s WHERE s.concertEvent.id = :eventId AND s.available = true")
    List<Seat> findAvailableSeatsByEventId(@Param("eventId") Long eventId);

}

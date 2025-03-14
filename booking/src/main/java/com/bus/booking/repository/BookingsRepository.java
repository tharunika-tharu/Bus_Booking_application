package com.bus.booking.repository;

import com.bus.booking.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {
    List<Bookings> findByUserId(int userId);
}
package com.bus.booking.repository;

import com.bus.booking.model.BusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusDataRepository extends JpaRepository<BusData, Integer> {
    @Query(value = "select * from Reservation  where reservation.to_destination =:to and reservation.from_destination =:from and reservation.filter_date =:filterDate  order By reservation.filter_date desc " , nativeQuery = true)
    List<BusData> findByToFromAndDate(@Param("to") String toDestination,
                                      @Param("from") String fromDestination,
                                      @Param("filterDate") String filterDate);
}
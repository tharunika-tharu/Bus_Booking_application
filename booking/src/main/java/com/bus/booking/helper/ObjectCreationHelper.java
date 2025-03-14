package com.bus.booking.helper;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.BusData;

public class ObjectCreationHelper {

    public static BookingsDTO createBookingsDTO(BusData busdata) {
        BookingsDTO bks = new BookingsDTO();

        bks.setBusName(busdata.getBusName());
        bks.setFilterDate(busdata.getFilterDate());
        bks.setFromDestination(busdata.getFromDestination());
        bks.setToDestination(busdata.getToDestination());
        bks.setPrice(busdata.getPrice());
        bks.setNoOfPersons(0);
        bks.setTime(busdata.getTime());
        bks.setTotalCalculated(0.0);

        return bks;
    }


        public static BookingsDTO createBookingsDTO(Bookings bookings)  {
        BookingsDTO dto = new BookingsDTO();
        dto.setId(bookings.getId());
        dto.setBusName(bookings.getBusName());
        dto.setTime(bookings.getTime());
        dto.setFromDestination(bookings.getFromDestination());
        dto.setToDestination(bookings.getToDestination());
        dto.setFilterDate(bookings.getFilterDate());
        dto.setNoOfPersons(bookings.getNoOfPersons());
        dto.setTotalCalculated(bookings.getTotalCalculated());
        dto.setTripStatus(bookings.isTripStatus());
            return dto;
    }
    }




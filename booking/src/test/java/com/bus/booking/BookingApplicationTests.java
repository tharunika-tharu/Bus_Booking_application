package com.bus.booking;

import com.bus.booking.DTO.ReservationDTO;
import com.bus.booking.DTO.UserRegisteredDTO;
import com.bus.booking.model.BusData;
import com.bus.booking.repository.BusDataRepository;
import com.bus.booking.repository.UserRepository;
import com.bus.booking.service.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class BookingApplicationTests {

	@Autowired
	DefaultUserService userService;
	@Autowired
	UserRepository userRepo;
	@Autowired
	BusDataRepository busDataRepository;


	@Test
	public void registerAndLoginWithUserAccount() {
		UserRegisteredDTO userRegisteredDTO = new UserRegisteredDTO();
		userRegisteredDTO.setEmail_id("tharunika@gmail.com");
		userRegisteredDTO.setName("tharunika");
		userRegisteredDTO.setPassword("12345");
		userService.save(userRegisteredDTO);
		Assert.notNull(userRepo.findByEmail("tharunika@gmail.com"), "User found in DB");
		UserDetails user = userService.loadUserByUsername("tharunika@gmail.com");
		Assert.notNull(user, "Logined successfully");
	}



	@Test
	public void saveBusData() {
		BusData busData = new BusData();
		busData.setBusName("TestBus");
		busData.setFromDestination("ND");
		busData.setToDestination("AMT");
		busData.setFilterDate("2022-11-10");
		busData.setTime("11:25");
		busData.setPrice(40.0);
		BusData bs = busDataRepository.save(busData);
		Assert.notNull(bs, "Busdata Saved Successfully");
	}


	@Test
	public void fetchBusData() {
		ReservationDTO rs = new ReservationDTO();
		rs.setFrom("ND");
		rs.setTo("AMT");
		rs.setFilterDate("2022-11-10");
		List<BusData> bs = busDataRepository.findByToFromAndDate(rs.getTo(), rs.getFrom(), rs.getFilterDate());
		Assert.notEmpty(bs, "Bus Data available with above filters ");
	}

}


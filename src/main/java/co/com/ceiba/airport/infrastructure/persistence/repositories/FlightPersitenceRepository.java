package co.com.ceiba.airport.infrastructure.persistence.repositories;

import co.com.ceiba.airport.domain.exceptions.NotExistException;
import co.com.ceiba.airport.domain.models.entities.Flight;
import co.com.ceiba.airport.domain.ports.repositories.FlightRepository;
import co.com.ceiba.airport.infrastructure.persistence.builder.FlightBuilder;
import co.com.ceiba.airport.infrastructure.persistence.entities.FlightEntity;
import co.com.ceiba.airport.infrastructure.persistence.repositories.jpa.FlightJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FlightPersitenceRepository implements FlightRepository {

    private static final String THE_FLIGHT_DOESNOT_EXIST = "The flight does not exist";
    private static final int LIMIT_VALIDATE_DATE_IN_MINUTES = 5;

    @Autowired
    FlightJPARepository flightJPARepository;

    @Override
    public Flight getFlight(String id) {
        FlightEntity flightEntity;
        Optional flightquery = flightJPARepository.findById(id);
        if(!flightquery.isPresent()) {
            throw new NotExistException(THE_FLIGHT_DOESNOT_EXIST);
        }else{
            flightEntity = (FlightEntity) flightquery.get();
        }
        return FlightBuilder.convertToDomain(flightEntity);
    }

    @Override
    public List<Flight> getAllFlight() {
        List<Flight> flightList = new ArrayList<>();
        List<FlightEntity> flightEntityList = new ArrayList<>();
        Iterable<FlightEntity> flightListSource = flightJPARepository.findAll();
        flightListSource.forEach(flightEntityList::add);
        flightEntityList.forEach((final FlightEntity flightEntity) -> flightList.add(FlightBuilder.convertToDomain(flightEntity)));
        return flightList;
    }

    @Override
    public String createFlight(Flight flight) {
        return flightJPARepository.save(FlightBuilder.convertToEntity(flight)).getId();
    }

    @Override
    public void updateFlight(Flight flight) {
        flightJPARepository.save(FlightBuilder.convertToEntity(flight));
    }

    @Override
    public void deleteFlight(String id) {
        flightJPARepository.deleteById(id);
    }

    @Override
    public boolean isExiste(String idFlight) {
        return flightJPARepository.existsById(idFlight);
    }

    @Override
    public boolean isValidateTime(LocalDateTime timeDeparture) {
        boolean isValid = true;
        LocalDateTime fiveMinutesBefore = timeDeparture.minusMinutes(LIMIT_VALIDATE_DATE_IN_MINUTES);
        LocalDateTime fiveMinutesAfter = timeDeparture.plusMinutes(LIMIT_VALIDATE_DATE_IN_MINUTES);
        List<Flight> flightList = getAllFlight();
        for(Flight flight : flightList){
            if (flight.getTimeDeparture().isAfter(fiveMinutesBefore) && flight.getTimeDeparture().isBefore(fiveMinutesAfter)){
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}

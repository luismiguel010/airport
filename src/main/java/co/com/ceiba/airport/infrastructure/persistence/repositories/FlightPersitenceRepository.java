package co.com.ceiba.airport.infrastructure.persistence.repositories;

import co.com.ceiba.airport.domain.models.entities.Flight;
import co.com.ceiba.airport.domain.ports.repositories.FlightRepository;
import co.com.ceiba.airport.infrastructure.persistence.builder.FlightBuilder;
import co.com.ceiba.airport.infrastructure.persistence.entities.FlightEntity;
import co.com.ceiba.airport.infrastructure.persistence.repositories.jpa.FlightJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FlightPersitenceRepository implements FlightRepository {

    private static final String FLIGH_FIND_BY_ID = "Flight.findById";
    private static final String ID = "id";

    private EntityManager entityManager;

    @Autowired
    FlightJPARepository flightJPARepository;

    public FlightPersitenceRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Flight getFlight(Long id) {
        Optional flightquery = flightJPARepository.findById(id);
        FlightEntity flightEntity = (FlightEntity) flightquery.get();
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
    public Long createFlight(Flight flight) {
        flightJPARepository.save(FlightBuilder.convertToEntity(flight));
        return flight.getId();
    }

    @Override
    public void updateFlight(Flight flight) {
        flightJPARepository.save(FlightBuilder.convertToEntity(flight));
    }

    @Override
    public void deleteFlight(Long id) {
        flightJPARepository.deleteById(id);
    }

    @Override
    public boolean isValidateCalendarTime(Long idFlight) {
        return false;
    }

    @Override
    public boolean isExiste(Long idFlight) {
        return flightJPARepository.existsById(idFlight);
    }
}

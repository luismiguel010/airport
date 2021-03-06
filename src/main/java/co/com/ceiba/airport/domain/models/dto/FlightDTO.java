package co.com.ceiba.airport.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FlightDTO {
    private String id;
    private LocalDateTime timeDeparture;
    private String arrival;
    private float cost;
    private boolean isReprogrammed;
}

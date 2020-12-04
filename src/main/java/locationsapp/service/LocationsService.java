package locationsapp.service;

import locationsapp.dto.CreateLocationCommand;
import locationsapp.dto.LocationDto;
import locationsapp.dto.UpdateLocationCommand;
import locationsapp.entities.Location;
import locationsapp.repository.LocationsRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationsService {

    private LocationsRepository locationsRepository;

    private ModelMapper modelMapper;

    public LocationsService(LocationsRepository locationsRepository, ModelMapper modelMapper) {
        this.locationsRepository = locationsRepository;
        this.modelMapper = modelMapper;
    }

    public Page<LocationDto> listLocations(Pageable pageable) {
        log.debug("List locations");
        return locationsRepository.findAllWithTags(pageable).map(l -> modelMapper.map(l, LocationDto.class));
    }

    public List<LocationDto> listLocations() {
        log.debug("List locations");
        return locationsRepository.findAllWithTags(Pageable.unpaged()).getContent()
                .stream()
                .map(l -> modelMapper.map(l, LocationDto.class))
                .collect(Collectors.toList());
    }

    public LocationDto createLocation(CreateLocationCommand command) {
        Scanner scanner = new Scanner(command.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();

        Location location = new Location();
        location.setLat(lat);
        location.setLon(lon);
        location.setName(command.getName());
        location.setInterestingAt(command.getInterestingAt());
        location.setTags(parseTags(command.getTags()));

        locationsRepository.save(location);
        log.info(String.format("Location has been created id: %s, name: %s", location.getId(), command.getName()));
        return modelMapper.map(location, LocationDto.class);
    }

    public Optional<LocationDto> getLocationById(long id) {
        return locationsRepository.findByIdWithTags(id)
                .stream().map(l -> modelMapper.map(l, LocationDto.class))
                .findFirst();
    }

    @Transactional
    public Optional<LocationDto> updateLocation(UpdateLocationCommand command) {
        var maybeLocation = locationsRepository.findById(command.getId());
        if (maybeLocation.isEmpty()) {
            return Optional.empty();
        }
        var location = maybeLocation.get();
        Scanner scanner = new Scanner(command.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();

        location.setName(command.getName());
        location.setLat(lat);
        location.setLon(lon);
        location.setInterestingAt(command.getInterestingAt());
        location.setTags(parseTags(command.getTags()));

        log.info(String.format("Location has been updated id: %s, name: %s", command.getId(), command.getName()));
        return Optional.of(modelMapper.map(location, LocationDto.class));
    }

    public boolean deleteLocation(long id) {
        var location = locationsRepository.findById(id);
        if (location.isPresent()) {
            locationsRepository.delete(location.get());
            log.info(String.format("Location has been deleted, id: %s", id));
            return true;
        }
        return false;
    }

    private List<String> parseTags(String tags) {
        if (tags == null) {
            return List.of();
        }
        return Arrays.stream(tags.split(",")).map(s -> s.trim()).filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

    public void deleteAllLocations() {
        log.info("Delete all locations");
        locationsRepository.deleteAll();
    }
}

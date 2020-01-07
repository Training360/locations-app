package locationsapp.service;

import locationsapp.controller.CreateLocationCommand;
import locationsapp.controller.LocationDto;
import locationsapp.controller.UpdateLocationCommand;
import locationsapp.entities.Location;
import locationsapp.repository.LocationsRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationsService.class);

    private LocationsRepository locationsRepository;

    private ModelMapper modelMapper;

    public LocationsService(LocationsRepository locationsRepository, ModelMapper modelMapper) {
        this.locationsRepository = locationsRepository;
        this.modelMapper = modelMapper;
    }

    public Page<LocationDto> listLocations(Pageable pageable) {
        return locationsRepository.findAllWithTags(pageable).map(l -> modelMapper.map(l, LocationDto.class));
    }

    public List<LocationDto> listLocations() {
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
        LOGGER.info(String.format("Location has created id: %s, name: %s", location.getId(), command.getName()));
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

        LOGGER.info(String.format("Location has updated id: %s, name: %s", command.getId(), command.getName()));
        return Optional.of(modelMapper.map(location, LocationDto.class));
    }

    public boolean deleteLocation(long id) {
        var location = locationsRepository.findById(id);
        if (location.isPresent()) {
            locationsRepository.delete(location.get());
            LOGGER.info(String.format("Location has deleted, id: %s", id));
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

}

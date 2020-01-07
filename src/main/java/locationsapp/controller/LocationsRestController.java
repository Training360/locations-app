package locationsapp.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LocationsRestController {

    private LocationsService locationsService;

    public LocationsRestController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.GET)
    public Page<LocationDto> listLocations(@PageableDefault(sort = "name") Pageable pageable) {
        return locationsService.listLocations(pageable);
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getLocationById(@PathVariable long id) {
        var location = locationsService.getLocationById(id);
        if (location.isEmpty()) {
            return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
        }
        else {
            return ResponseEntity.ok(location.get());
        }
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteLocation(@PathVariable long id) {
        var success = locationsService.deleteLocation(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> updateLocation(@PathVariable long id, @Valid @RequestBody UpdateLocationCommand req) {
        req.setId(id);

        var location = locationsService.updateLocation(req);
       if (location.isEmpty()) {
           return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
       }
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.POST)
    public ResponseEntity<Object> createLocation(@Valid @RequestBody CreateLocationCommand req) {

        var location = locationsService.createLocation(req);
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            if (((InvalidFormatException) ex.getCause()).getPath().toString().contains("interestingAt")) {
                return new ResponseEntity<>(new DataError("Invalid Interesting at format!"), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new DataError(ex.getMessage()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new DataError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

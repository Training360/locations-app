package locationsapp.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import locationsapp.dto.CreateLocationCommand;
import locationsapp.dto.LocationDto;
import locationsapp.dto.UpdateLocationCommand;
import locationsapp.error.ValidationError;
import locationsapp.service.LocationsService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Locations", description = "Operations with locations")
public class LocationsRestController {

    private LocationsService locationsService;

    public LocationsRestController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "/api/locations/pages", method = RequestMethod.GET)
    @PageableAsQueryParam
    @Operation(summary = "List locations", description = "List locations with paging feature")
    @ApiResponse(responseCode = "200", description = "locations have been listed")
    public Page<LocationDto> listLocationsPerPages(@Parameter(hidden = true) @PageableDefault(sort = "name") Pageable pageable) {
        return locationsService.listLocations(pageable);
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.GET)
    @Operation(summary = "List locations", description = "List all locations")
    @ApiResponse(responseCode = "200", description = "locations have been listed")
    public List<LocationDto> listLocations() {
        return locationsService.listLocations();
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.GET)
    @Operation(summary = "Get location by id")
    @ApiResponse(responseCode = "200", description = "location has been queried")
    @ApiResponse(responseCode = "404", description = "location not found")
    public ResponseEntity<Object> getLocationById(@PathVariable long id) {
        var location = locationsService.getLocationById(id);
        if (location.isEmpty()) {
            return createNotFoundResponse();
        }
        else {
            return ResponseEntity.ok(location.get());
        }
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Delete location by id")
    @ApiResponse(responseCode = "204", description = "location has been deleted")
    @ApiResponse(responseCode = "404", description = "location not found")
    public ResponseEntity<Object> deleteLocation(@PathVariable long id) {
        var success = locationsService.deleteLocation(id);
        if (success) {
            return ResponseEntity.status(204).build();
        } else {
            return createNotFoundResponse();
        }
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.DELETE)
    @Operation(summary = "Delete all locations")
    @ApiResponse(responseCode = "204", description = "all locations have been deleted")
    public ResponseEntity<Object> deleteAllLocations() {
        locationsService.deleteAllLocations();
        return ResponseEntity.status(204).build();
    }


    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.POST)
    @Operation(summary = "Update location by id")
    @ApiResponse(responseCode = "200", description = "location has been updated")
    @ApiResponse(responseCode = "404", description = "location not found")
    public ResponseEntity<Object> updateLocation(@PathVariable long id, @Valid @RequestBody UpdateLocationCommand req) {
        req.setId(id);

        var location = locationsService.updateLocation(req);
       if (location.isEmpty()) {
           return createNotFoundResponse();
       }
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.POST)
    @Operation(summary = "Create location")
    @ApiResponse(responseCode = "201", description = "location has been created")
    public ResponseEntity<Object> createLocation(@Valid @RequestBody CreateLocationCommand req) {
        var location = locationsService.createLocation(req);
        return new ResponseEntity(location, HttpStatus.CREATED);
    }

    private ResponseEntity<Object> createNotFoundResponse() {
        var problem = Problem.builder()
                .withType(URI.create("locations/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail("Not found")
                .build();

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        var validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map((FieldError e) -> new ValidationError(e.getField(), e.getDefaultMessage())).collect(Collectors.toList());
        return createValidationError(validationErrors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            if (((InvalidFormatException) ex.getCause()).getPath().toString().contains("interestingAt")) {
                return createValidationError(List.of(new ValidationError("interestingAt", "Invalid Interesting at format!")));
            } else {
                return createErrorForInvalidJson(ex);
            }
        } else {
            return createErrorForInvalidJson(ex);
        }
    }

    private ResponseEntity<Object> createErrorForInvalidJson(HttpMessageNotReadableException ex) {
        var problem = Problem.builder()
                .withType(URI.create("locations/invalid-json-request"))
                .withTitle("JSON error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problem);
    }

    private ResponseEntity<Object> createValidationError(List<ValidationError> validationErrors) {
        var problem = Problem.builder()
                .withType(URI.create("locations/validation-error"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail("Validation error")
                .with("validationErrors", validationErrors)
                .build();

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problem);
    }
}

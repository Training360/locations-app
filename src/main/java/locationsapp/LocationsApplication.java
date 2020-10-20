package locationsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import locationsapp.dto.LocationDto;
import locationsapp.dto.UpdateLocationCommand;
import locationsapp.ws.AuthEndpoint;
import locationsapp.ws.FilesEndpoint;
import locationsapp.ws.LocationsEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.xml.ws.Endpoint;
import java.util.Locale;

@SpringBootApplication
@Configuration
public class LocationsApplication implements WebMvcConfigurer
{

	public static void main(String[] args) {
		SpringApplication.run(LocationsApplication.class, args);
	}

    @Autowired
    private Bus bus;

    @Autowired
    private Environment environment;

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.createTypeMap(LocationDto.class, UpdateLocationCommand.class)
                .setConverter(new Converter<LocationDto, UpdateLocationCommand>() {
                    @Override
                    public UpdateLocationCommand convert(MappingContext<LocationDto, UpdateLocationCommand> context) {
                        var source = context.getSource();
                        var command = new UpdateLocationCommand();
                        command.setId(source.getId());
                        command.setName(source.getName());
                        command.setCoords(source.getLat() + "," + source.getLon());
                        command.setInterestingAt(source.getInterestingAt());
                        command.setTags(source.getTags().toString());
                        return command;
                    }
                });
        return modelMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.ENGLISH);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    public Endpoint publishedLocationsEndpoint(LocationsEndpoint locationsEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, locationsEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/locations");
        endpoint.publish("/locations");
        return endpoint;
    }

    @Bean
    public Endpoint publishedFilesEndpoint(FilesEndpoint filesEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, filesEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/files");
        endpoint.publish("/files");
        return endpoint;
    }

    @Bean
    public Endpoint publishedAuthEndpoint(AuthEndpoint authEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, authEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/auth");
        endpoint.publish("/auth");
        return endpoint;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                                .title("Locations API")
                                .version("1.0.0")
                                .description("Operations with locations"));
    }


}

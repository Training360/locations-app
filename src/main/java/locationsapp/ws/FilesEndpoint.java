package locationsapp.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@WebService(targetNamespace = "http://locations.com/services/locations")
@MTOM
@Service
public class FilesEndpoint {

    @Value("${file.location}")
    private Path dir;

    @WebMethod
    @WebResult(name = "generatedName")
    public String uploadFile(@WebParam(name = "file") DataHandler file, @WebParam(name = "originalName") @XmlElement(required = true) String originalName) {
        String filename = UUID.randomUUID() + getExtension(originalName);
        try (var os = Files.newOutputStream(dir.resolve(filename))) {
            file.writeTo(os);
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Can not write file", ioe);
        }
        return filename;
    }

    private String getExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    @WebMethod
    @XmlMimeType("application/octet-stream")
    @WebResult(name = "content")
    public DataHandler downloadFile(@WebParam(name = "generatedName") @XmlElement(required = true) String name) {
        return new DataHandler(new FileDataSource(dir.resolve(name).toFile()));
    }

    @WebMethod
    @WebResult
    public FileResponse downloadFileAsBase64(@WebParam(name = "generatedName") @XmlElement(required = true) String name) {
        var file = dir.resolve(name);
        try {
            var content = Base64.getEncoder().encodeToString(Files.readAllBytes(file));
            return new FileResponse(name, content);
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Can not encode", ioe);
        }
    }
}

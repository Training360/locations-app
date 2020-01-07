package locationsapp.ws;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

@WebService(targetNamespace = "http://locations.com/services/locations")
@Service
public class AuthEndpoint {

    @Resource
    private WebServiceContext ctx;

    @WebMethod
    @WebResult(name = "message")
    public String sayHello(@WebParam(name = "name") @XmlElement(required = true) String name) {
        Map headers = (Map) ctx.getMessageContext().get(MessageContext.HTTP_REQUEST_HEADERS);
        List<String> values = (List) headers.get("username");
        String username = null;
        if (values.size() > 0) {
            username = values.get(0);
        }
        if (username == null || !username.equals("admin")) {
            throw new SecurityException("Illegal authentication");
        }

        return "Hello " + name + "!";
    }
}

import java.io.*;
import java.util.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class SystemInfo {
    //create a list of servers
    List<ServerInfo> servers = new ArrayList<ServerInfo>();

    public int readXML(String file) throws FileNotFoundException, XMLStreamException {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(new File(file)));
            ServerInfo server;
            
            //add attributes to individual server objects
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT: {
                        if (reader.getLocalName().equals("server")) {
                            server = new ServerInfo();
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeName(i).toString().equals("type")) {
                                    server.serverType = reader.getAttributeValue(i);
                                }
                                if (reader.getAttributeName(i).toString().equals("limit")) {
                                    server.serverLimit = Integer.parseInt(reader.getAttributeValue(i));
                                }
                                if (reader.getAttributeName(i).toString().equals("bootupTime")) {
                                    server.serverBootupTime = Integer.parseInt(reader.getAttributeValue(i));
                                }
                                if (reader.getAttributeName(i).toString().equals("hourlyRate")) {
                                    server.serverHourlyRate = Double.parseDouble(reader.getAttributeValue(i));
                                }
                                if (reader.getAttributeName(i).toString().equals("coreCount")) {
                                    server.serverCoreCount = Integer.parseInt(reader.getAttributeValue(i));
                                }
                                if (reader.getAttributeName(i).toString().equals("memory")) {
                                    server.serverMemory = Integer.parseInt(reader.getAttributeValue(i));
                                }
                                if (reader.getAttributeName(i).toString().equals("disk")) {
                                    server.serverDisk = Integer.parseInt(reader.getAttributeValue(i));
                                }
                            }
                            servers.add(server);
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
            return 1;
        }
        return 0;
    }
}

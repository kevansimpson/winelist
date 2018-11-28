package com.sccpa.winelist.print;

import com.sccpa.winelist.data.WineEntry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Component
public class PrintService {
    private static final TransformerFactory factory = TransformerFactory.newInstance();

    private FopFactory fopFactory;
    private File configFile;

    @PostConstruct
    public void init() throws IOException {
        try {
            configFile = new File(System.getProperty("user.home"),"fop.xconf");
            if (!configFile.exists())
                Files.copy(getClass().getResourceAsStream("/fop.xconf"), configFile.toPath());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public File print(final List<WineEntry> list) throws Exception {
        return print(toXml(list));
    }

    public File print(final String xml) throws Exception {
        if (fopFactory == null)
            fopFactory = FopFactory.newInstance(configFile);

        final File file = File.createTempFile("winelist", ".pdf");
        try (InputStream xsl = getClass().getResourceAsStream("/wine-report.xsl");
             OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Step 4: Setup JAXP using WINELIST transformer
            Transformer transformer = factory.newTransformer(new StreamSource(xsl));

            // Step 5: Setup input and output for XSLT transformation
            Source src = new StreamSource(new StringReader(xml));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        }

        return file;
    }

    private String toXml(final List<WineEntry> list) {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<List hello=\"foobar\">\n");
        list.forEach(entry ->
            bldr.append("\t<Entry>\n")
                .append("\t\t<Producer>").append(escape(entry.getProducer())).append("</Producer>\n")
                .append("\t\t<Name>").append(escape(entry.getName())).append("</Name>\n")
                .append("\t\t<Type>").append(escape(entry.getType())).append("</Type>\n")
                .append("\t\t<Year>").append(escape(entry.getYear())).append("</Year>\n")
                .append("\t\t<Price>").append(entry.getPrice()).append("</Price>\n")
                .append("\t\t<Qty>").append(entry.getQty()).append("</Qty>\n")
                .append("\t\t<Bin>").append(escape(entry.getBin())).append("</Bin>\n")
                .append("\t\t<Ready>").append(escape(entry.getReady())).append("</Ready>\n")
                .append("\t\t<Rating>").append(escape(entry.getRating())).append("</Rating>\n")
                .append("\t</Entry>\n")
        );
        bldr.append("</List>\n");

        final File wlXml = new File(System.getProperty("user.home"), "wl.xml");
        try (FileWriter writer = new FileWriter(wlXml)) {
            IOUtils.write(bldr.toString(), writer);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return bldr.toString();
    }

    private String escape(final String value) {
        return StringEscapeUtils.escapeXml11(value);
    }
}

package dataguru.codec.reader;

import dataguru.codec.sentence.SentenceLine;
import dataguru.codec.sentence.Vdm;
import dataguru.codec.sentence.message.AisMessage;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
//import java.util.function.Consumer;

public class DecodeTest {

    private static final Logger LOG = LoggerFactory.getLogger(DecodeTest.class);


    /**
     * Decode all messages in a file Tries to handle proprietary messages
     *
     * Demonstrates and tests the process of decoding lines into Vdm messages, and the decoding into AIS messages
     *
     * @throws java.io.IOException
     */
    @Test
    public void readLoopTest() throws IOException {
        // Make a list of proprietary handlers

        // Open file
        URL url = ClassLoader.getSystemResource("stream_example.txt");
        Assert.assertNotNull(url);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Assert.assertNotNull(in);
            String line;

            // Prepare message classes
//            AisMessage message;
            Vdm vdm = new Vdm();

            while ((line = in.readLine()) != null) {

                // Ignore everything else than sentences
                if (!line.startsWith("$") && !line.startsWith("!")) {
                    continue;
                }


                // Handle VDM/VDO line
                try {
                    int result = vdm.parse(new SentenceLine(line));
                    // LOG.info("result = " + result);
                    if (result == 0) {
                        LOG.info(vdm.getBinArray().toString());
                        AisMessage message = AisMessage.getInstance(vdm);
                        if(message!=null)
                        System.out.println("test = "+message.toString());
                        // Message ready for handling

                    } else if (result == 1) {
                        LOG.info("wait for more gps");
                        // Wait for more gps
                        continue;
                    } else {
                        LOG.error("Failed to parse line: " + line + " result = " + result);
                        Assert.assertTrue(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.info("VDM failed: " + e.getMessage() + " line: " + line  );
                    Assert.assertTrue(false);
                }

                // Create new VDM
                vdm = new Vdm();
//                tags.clear();
            }
        }
    }

}

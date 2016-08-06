本工程用Intellij Idea开发，采用Maven构建工具，运行环境JDK1.7，源程序位于src/main/java 中，测试代码位于src/test/java中，测试用例
src\test\java\dataguru\codec\reader\DecodeTest.java 与 src\test\java\dataguru\codec\reader\ReaderTest.java，测试数据src\test\resources

数据源读取位于 src\main\java\dataguru\codec\reader
NmeaObject位于 src\main\java\dataguru\codec\message
编解码器位于  src\main\java\dataguru\codec\manager
封装语句解析位于 src\main\java\dataguru\codec\sentence


读取参数语句进行解析
Readers读取数据后调用CodecManager解析，判断不同语句使用工厂CodecFactory调用不同解析器解析，解析完后使用观察者模式返回给观察者结果
AbstractDataSource reader = Readers.createReaderFromFile("src/test/resources/small_example.txt");
reader.setPacketHandler(new Consumer() {
    @Override
    public void accept(String str) {
        System.out.println(str);
    }
});
reader.start();
reader.join();

封装语句读取解析
读取数据后，使用SentenceLine分析语句，使用Vdm分析语句是否结束，如果结束返回解析数据，创建一个新的Vdm解析数据；
如果未结束，用当前Vdm继续分析后面的数据
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

                        // Message ready for handling

                    } else if (result == 1) {
                        LOG.info("wait for more data");
                        // Wait for more data
                        continue;
                    } else {
                        LOG.error("Failed to parse line: " + line + " result = " + result);
                        Assert.assertTrue(false);
                    }

                } catch (Exception e) {
                    LOG.info("VDM failed: " + e.getMessage() + " line: " + line  );
                    Assert.assertTrue(false);
                }

                // Create new VDM
                vdm = new Vdm();
//                tags.clear();
            }
        }

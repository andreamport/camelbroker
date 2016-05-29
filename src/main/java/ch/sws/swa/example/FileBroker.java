package ch.sws.swa.example;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.builder.RouteBuilder;

public class FileBroker {

    public static void main(final String[] arguments)
    {
        System.out.println("========== Arguments ==========");
        for(String param: arguments) {
            System.out.println(param);
        }
        System.out.println("===============================");
        if(arguments.length != 3) {
            showUsage();
            System.exit(1);
        }
        final String fromPath = arguments[0];
        final String toPath = arguments[1];
        final String option = arguments [2];
        final CamelContext camelContext = new DefaultCamelContext();
        final StringBuilder fromPathString = new StringBuilder("file:").append(fromPath);
        if(option.startsWith("?")) {
            fromPathString.append(option);
        }
        final StringBuilder toPathString = new StringBuilder("file:").append(toPath);
        try
        {
            camelContext.addRoutes(
                    new RouteBuilder()
                    {
                        @Override
                        public void configure() throws Exception
                        {
                            from(fromPathString.toString()).to(toPathString.toString());
                            //from("file:C:\\datafiles\\input?noop=true").to("file:C:\\datafiles\\output");
                        }
                    });
            camelContext.start();

            System.out.println("========================================================");
            System.out.println("FileBroker is running");
            System.out.println("");
            System.out.println("from = " + fromPathString);
            System.out.println("to = " + toPathString);
            System.out.println("");
            System.out.println("Use ctrl + c to stop this application.");
            System.out.println("========================================================");

            while(true) {
                Thread.sleep(1000);
            }

        }
        catch (Exception camelException) {
            System.err.println(
                    "Exception trying to copy files - " + camelException.toString());
        }
        finally {
            try {
                camelContext.stop();
            } catch (Exception exp) {
                System.err.println(
                        "Exception trying to stop CamelContext - " + exp.toString());
            }
        }
    }

    private static void showUsage() {
        System.err.println("=================================================");
        System.err.println("Usage:");
        System.err.println("FileBroker inputPath outputPath option");
        System.err.println("option: must start with a '?' otherwise ignored");
        System.err.println("=================================================");
    }
}

package com.sccpa.winelist;

import com.sccpa.winelist.config.SqlConfig;
import com.sccpa.winelist.data.WineRepository;
import com.sccpa.winelist.gui.AppFrame;
import com.sccpa.winelist.print.PrintService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
@EnableConfigurationProperties(SqlConfig.class)
public class Application {

    public static void main(final String... args) {
        final ConfigurableApplicationContext context =
                new SpringApplicationBuilder(Application.class).headless(false).run(args);
        final WineRepository repository = context.getBean(WineRepository.class);
        final PrintService printService = context.getBean(PrintService.class);

        EventQueue.invokeLater(() -> {
            AppFrame.setUIFont(     // Dad's blind, his emails are all UCase... so, yeah: 24
                    new javax.swing.plaf.FontUIResource("Monospaced",Font.PLAIN,24));
            final AppFrame appFrame = new AppFrame(repository, printService);
            appFrame.setVisible(true);
        });
    }
}

package com.sccpa.winelist;

import com.sccpa.winelist.config.SqlConfig;
import com.sccpa.winelist.data.WineRepository;
import com.sccpa.winelist.gui.AppFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
@EnableConfigurationProperties(SqlConfig.class)
public class Application {

    public static void main(final String... args) {
//        SpringApplication.run(Application.class, args);
        final ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
                .headless(false).run(args);
        WineRepository repository = context.getBean(WineRepository.class);
//        System.out.println("LOGIN: "+ repository.login());
//        System.out.println("COUNT: "+ repository.fetchEntireList().size());

        EventQueue.invokeLater(() -> {
//            final AppFrame appFrame = context.getBean(AppFrame.class);
            AppFrame.setUIFont(
                    new javax.swing.plaf.FontUIResource("Monospaced",Font.PLAIN,24));
            final AppFrame appFrame = new AppFrame(repository);
            appFrame.setVisible(true);
        });
    }
}

package br.com.velumsoft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.velumsoft.controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class JavafxSpringApp extends Application {

    Logger log = LoggerFactory.getLogger(JavafxSpringApp.class);
    
    private static String[] initialArgs;
    
    private ConfigurableApplicationContext applicationContext;
    
    
    @Override
    public void init() throws Exception {
        log.info("Initializing JavaFxSpringApp");
        super.init();
        applicationContext = SpringApplication.run(getClass(), initialArgs);
        initSecurity();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting JavaFxSpringApp");
        Stage stage = primaryStage;
        
        MainController mainController = applicationContext.getBean(MainController.class);
        
        Scene mainScene = new Scene(mainController.getView());
        stage.setScene(mainScene);
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        log.info("Stopping JavaFxSpringApp");
        super.stop();
    }

    public static void main(String[] args) {
        initialArgs = args;
        launch(args);
    }
    
    public static void initSecurity() {
        SecurityContextHolder.setStrategyName("MODE_GLOBAL");
        initAnonymous();
    }
    
    public static void initAnonymous() {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
                "anonymous", "anonymous",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void logout(){
        SecurityContextHolder.clearContext();
        initAnonymous();
    }
}

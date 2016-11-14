package br.com.velumsoft.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.velumsoft.JavafxSpringApp;
import br.com.velumsoft.fxml.FXMLController;
import br.com.velumsoft.service.ClientDataService;
import br.com.velumsoft.vo.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

@Component
public class MainController extends FXMLController implements Initializable {

    Logger log = LoggerFactory.getLogger(MainController.class);
        
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private ClientDataService clientDataService;
    
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    
    private ObservableList<String> userRoles = FXCollections.observableArrayList();
    
    @FXML
    private MenuItem mnItmLoginAsUser;

    @FXML
    private MenuItem mnItmLoginAsAdmin;
    
    @FXML
    private MenuItem mnItmLogout;
    
    @FXML
    private MenuItem mnItmExit;
    
    @FXML
    private Menu mnMain;

    @FXML
    private AnchorPane ancPnMaster;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Button btnLogin;

    @FXML
    private AnchorPane ancPnView;

    @FXML
    private TableView<Client> tblVwClients;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtFldName;

    @FXML
    private TextField txtFldSurname;

    @FXML
    private TextField txtFldUser;
    
    @FXML
    private PasswordField pswdFldPassword;
    
    @FXML
    private Button btnRemove;

    @FXML
    private AnchorPane ancPnDetails;

    @FXML
    private Font x3;

    @FXML
    private Color x4;
    
    @FXML
    private Label lblUserName;
    
    @FXML
    private ListView<String> lstVwRoles;
    
    @FXML
    void handleLogin(ActionEvent event) {
        
        final String userName = txtFldUser.getText().trim();
        final String userPassword = pswdFldPassword.getText().trim();
        
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(userName, userPassword);
            Authentication result = authManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);            
            
            updateUserInfo();
        
        } catch (AuthenticationException e) {
            
            Alert alertWrongCredentials = new Alert(AlertType.INFORMATION);
            alertWrongCredentials.setHeaderText("Ops!");
            alertWrongCredentials.setContentText("Usuário ou senha incorreta!");
            
        }
        txtFldUser.clear();
        pswdFldPassword.clear();
    }

    @FXML
    void handleLoginAsAdmin(ActionEvent event) {
        log.info("Login as Admin");
        setUserAndPassword("admin", "p");
    }
    
    @FXML
    void handleLoginAsUser(ActionEvent event) {
        log.info("Login as User");
        setUserAndPassword("user", "p");
    }
    
    private void setUserAndPassword(String user, String password){
        txtFldUser.setText(user);
        pswdFldPassword.setText(password);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        JavafxSpringApp.logout();
        updateUserInfo();
    }

    private void updateUserInfo(){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();        
        lblUserName.setText(auth.getName());
        List<String> grantedAuthorities = auth.getAuthorities().stream().map( a -> a.toString()).collect(Collectors.toList());
        userRoles.clear();
        userRoles.addAll(grantedAuthorities);
        
    }

    @FXML
    private void handleRemove(ActionEvent event) {
        
        log.info("Remove client");
        
        Client selectedClient = tblVwClients.getSelectionModel().getSelectedItem();
        if( selectedClient == null ){
            
            Alert selectClient = new Alert(AlertType.WARNING);
            selectClient.setHeaderText("Ops!");
            selectClient.setContentText("Please select a client!");
            selectClient.show();
        } else {
            try {
                
                List<Client> clientsRefreshed = clientDataService.remove(selectedClient);
                clients.clear();
                clients.addAll(clientsRefreshed);
                
            } catch(AccessDeniedException e){
                
                showNotAllowedMessage(e);
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        
        log.info("Save client");
        
        Client newClient = new Client(txtFldName.getText(), txtFldSurname.getText());
        
        if( ! newClient.validate() ){
            Alert selectClient = new Alert(AlertType.WARNING);
            selectClient.setHeaderText("Ops!");
            selectClient.setContentText("Please select a client!");
            selectClient.show();
        } else {
            try {
                
                List<Client> newClients = clientDataService.addClient(newClient);
                if( ! newClients.isEmpty() ){
                    clients.clear();
                    clients.addAll(newClients);
                }
                
            } catch(AccessDeniedException e){
                showNotAllowedMessage(e);
            }
        }
        
    }
    
    @FXML
    private void handleExit(ActionEvent event){
        System.exit(0);
    }
    
    private void showNotAllowedMessage(AccessDeniedException e){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Ops!, \n Você não possui acesso a este recurso");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        log.info("Iniciando controller principal");
        
        tblVwClients.setItems(clients);        
        clients.addAll(clientDataService.getClients());
        
        lstVwRoles.setItems(userRoles);
        updateUserInfo();
        
    }

    
    
}

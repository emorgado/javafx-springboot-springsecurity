package br.com.velumsoft.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import br.com.velumsoft.vo.Client;

@Service
public class ClientDataService {
    
    List<Client> clients = new ArrayList<>();
    
    @PostConstruct
    private void initialize(){
        
        clients.add(new Client("Emerson", "Morgado Brito"));
        clients.add(new Client("Angela ", "Dias Brito"));
        clients.add(new Client("Julia", "Dias Brito"));
        
    }

    @Secured("ROLE_USER")
    public List<Client> addClient(Client cliente){
        clients.add(cliente);
        return clients;
    }
    
    @Secured("ROLE_ADMIN")
    public List<Client> remove(Client cliente){
        
        Iterator<Client> iterator = clients.iterator();
        while(iterator.hasNext()){
            Client c = iterator.next();
            if( c.equals(cliente) ){
                iterator.remove();
            }
        }
        return clients;
    }
    
    @Secured("ROLE_ANONYMOUS")
    public List<Client> getClients(){
        return clients;
    }
}

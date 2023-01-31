package com.UBank.Auth.Service;

import com.UBank.Auth.IO.Request.ClientDetailsRequest;
import com.UBank.Auth.IO.Request.ClientJobRequest;
import com.UBank.Auth.IO.Response.ClientResponse;
import com.UBank.Auth.Model.Authority;
import com.UBank.Auth.Model.Client;
import com.UBank.Auth.Model.Role;
import com.UBank.Auth.Repository.AuthorityRepository;
import com.UBank.Auth.Repository.ClientRepository;
import com.UBank.Auth.Repository.RoleRepository;
import com.UBank.Auth.Util.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@Service
public class ClientService implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    public ClientResponse addNewClient(ClientDetailsRequest clientDetailsRequest){
        Client client = modelMapper.map(clientDetailsRequest, Client.class);
        client.getClientAddresses().forEach(address -> {
            address.setClientDetail(client);
        });
        client.getClientJobDetail().forEach(job -> {
            job.setClientDetail(client);
        });
        client.setEncryptedPassword(bCryptPasswordEncoder.encode(clientDetailsRequest.getPassword()));
        client.setCrn(Utils.generateRandomString(5));

        Role role = roleRepository.findByName("ROLE_CLIENT");
        Collection<Role> arrayList = new ArrayList<Role>();
        arrayList.add(role);
        client.setRoles(arrayList);
        Client newClient = save(client);
        return convertToResponse(newClient);
    }

    public ClientResponse fetchClientById(Integer clientId){
        return convertToResponse(clientRepository.findById(clientId).get());
    }

    public ClientResponse fetchByClient(HttpServletRequest req){
        String crn = Utils.getCrn(req.getHeader("Authorization"));
        return convertToResponse(clientRepository.findByCrn(crn));
    }

    public ClientResponse fetchByCrn(String crn){
        return convertToResponse(clientRepository.findByCrn(crn));
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public ClientResponse convertToResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        modelMapper.map(client, clientResponse);
        return clientResponse;
    }

    public Client getUser(String email) {
        Client client = clientRepository.findByEmail(email);
        Client returnValue = new Client();
        BeanUtils.copyProperties(client,returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username);
        return new User(client.getEmail(),client.getEncryptedPassword(),
                true,true,true,true,new ArrayList<>());
    }
}

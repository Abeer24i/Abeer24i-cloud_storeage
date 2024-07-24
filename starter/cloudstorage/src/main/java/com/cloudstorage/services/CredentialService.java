package com.cloudstorage.services;

import com.cloudstorage.mapper.UserMapper;
import com.cloudstorage.mapper.CredentialMapper;
import com.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserMapper userMapper;


    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.findCredential(credentialId);
    }


    public List<Credential> getAllCredentials() {
        return credentialMapper.findAllCredentials();
    }

    public String getDecryptedPassword(Integer credentialid) {
        Credential credential = credentialMapper.findCredential(credentialid);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public void addCredential(Credential credential) throws IOException {
        credentialMapper.insert(credential);
    }


    public Credential[] getCredentialListings(Integer userId) {
        return credentialMapper.getCredentialForUser(userId);
    }


    public void addCredential(String url, String user, String credentialUserName, String key, String password) {
        Integer userId = userMapper.findUser(user).getUserid();
        Credential credential = new Credential(0, url, credentialUserName, key, password, userId);
        credentialMapper.insert(credential);
    }


    public void updateCredential(Integer credentialId, String newUserName, String url, String key, String password) {
        credentialMapper.updateCredential(credentialId, newUserName, url, key, password);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }
}

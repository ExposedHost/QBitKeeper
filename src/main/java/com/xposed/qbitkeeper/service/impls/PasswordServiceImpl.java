package com.xposed.qbitkeeper.service.impls;

import com.xposed.qbitkeeper.entity.Password;
import com.xposed.qbitkeeper.entity.User;
import com.xposed.qbitkeeper.repo.PasswordRepository;
import com.xposed.qbitkeeper.repo.UserRepository;
import com.xposed.qbitkeeper.security.KeyStoreManager;
import com.xposed.qbitkeeper.security.KyberAES;
import com.xposed.qbitkeeper.service.PasswordService;
import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.xposed.qbitkeeper.security.KyberAES.*;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;

    public PasswordServiceImpl(PasswordRepository passwordRepository, UserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Password addPassword(String website, String websitePassword, long userId) throws Exception {
        Password password = new Password();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Account does not exist"));
        List<Password> passwords = passwordRepository.findByUserId(userId);
        for(Password p : passwords){
            if(p.getWebsite().equals(website)){
                throw new IllegalStateException("Password for this website already exists");
            }
        }
        password.setUser(user);
        password.setWebsite(website);
        Security.addProvider(new BouncyCastleProvider());
        Security.addProvider(new BouncyCastlePQCProvider());
        KeyPair keyPair = KyberAES.generateKeyPair();
        SecretKeyWithEncapsulation initKeyWithEnc = generateSecretKeySender(keyPair.getPublic());
        String encryptedPassword = encrypt(websitePassword, initKeyWithEnc.getEncoded());
        try{
            KeyStoreManager.storeSecretKeyForUser(user.getUserName(), initKeyWithEnc);
        }catch (Exception e){
            e.printStackTrace();
        }
        password.setPassword(encryptedPassword);
        return passwordRepository.save(password);
    }

    @Override
    public Map<String, String> getPassword(String website, long userId) throws Exception {
        List<Password> passwords = passwordRepository.findByUserId(userId);
        Map<String, String> resultPassword = new HashMap<>();
        for(Password password : passwords){
            if(password.getWebsite().equals(website)){
                byte[] initKeyWithEnc = KeyStoreManager.getSecretKeyForUser(password.getUser().getUserName()).getEncoded();
                resultPassword.put(website, decrypt(password.getPassword(), initKeyWithEnc));
            }
        }
        if (resultPassword.isEmpty()) {
            throw new IllegalStateException("You have not stored any passwords of this website");
        }
        return resultPassword;
    }

}

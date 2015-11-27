package org.sipr.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.sipr.core.domain.AuthDetails;
import org.sipr.core.domain.User;
import org.sipr.core.service.AuthenticationService;
import org.sipr.core.service.UserNotFoundException;
import org.sipr.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Random;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Component
public class DigestAuthenticationService implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestAuthenticationService.class);

    @Value("${sip.auth.realm}")
    String realm;

    @Value("${sip.auth.algorithm}")
    String algorithm;

    @Inject
    UserService userService;

    @Override
    public String getRealm() {
        return realm;
    }

    @Override
    public String generateNonce() {
        long time = currentTimeInMillis();
        long pad = getPad();
        String nonceString = (new Long(time)).toString() + (new Long(pad)).toString();
        return md5Hex(nonceString);
    }

    long currentTimeInMillis() {
        return System.currentTimeMillis();
    }

    long getPad() {
        return new Random(currentTimeInMillis()).nextLong();
    }

    @Override
    public boolean authenticate(AuthDetails authDetails, String user) {
        DigestAuthDetails details = (DigestAuthDetails) authDetails;

        if (details == null) {
            return false;
        }

        if (!StringUtils.equals(StringUtils.substringBefore(details.getUsername(), "/"), user)) {
            return false;
        }

        try {
            User dbUser = (User) userService.getUser(user);
            String A1 = details.getUsername() + ":" + details.getRealm() + ":" + dbUser.getSipPassword();
            String A2 = details.getMethod().toUpperCase() + ":" + details.getUri();

            String KD = md5Hex(A1) + ":" + details.getNonce();
            if (details.getCnonce() != null) {
                KD += ":" + details.getCnonce();
            }
            KD += ":" + md5Hex(A2);

            return md5Hex(KD).compareTo(details.getResponse()) == 0;
        } catch (UserNotFoundException uex) {
            LOGGER.debug("No user found " + user);
            return false;
        }
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }
}

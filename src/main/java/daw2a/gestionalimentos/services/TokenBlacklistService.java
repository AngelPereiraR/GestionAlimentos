package daw2a.gestionalimentos.services;


import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();
    /**
     * Añade un token a la blacklist.
     *
     * @param token El token JWT a invalidar.
     */
    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Comprueba si un token está en la blacklist.
     *
     * @param token El token JWT a verificar.
     * @return true si el token está en la blacklist, false en caso contrario.
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Limpia toda la blacklist (opcional, por ejemplo, al reiniciar la aplicación).
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }

}

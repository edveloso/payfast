package br.com.caelum.payfast.oauth2;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenDao {

	private List<String> tokens = new ArrayList<String>();
	private List<String> autorizations = new ArrayList<String>();

	public void adicionaToken(String token) {
		tokens.add(token);
	}
	
	
	public void adicionaAuthorizationCode(String token) {
		autorizations.add(token);
	}
	
	public boolean existeAuthorizationCode(String token){
		return autorizations.contains(token);
	}
	
	
	public boolean existeToken(String token){
		return token.contains(token);
	}
}

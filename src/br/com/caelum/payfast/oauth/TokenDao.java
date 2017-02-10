package br.com.caelum.payfast.oauth;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenDao {

	private List<String> tokens = new ArrayList<String>();

	public void adicionaToken(String token) {
		tokens.add(token);
	}
	
	public boolean existeToken(String token){
		return token.contains(token);
	}
}

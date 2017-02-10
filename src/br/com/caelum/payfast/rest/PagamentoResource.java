package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import br.com.caelum.payfast.modelo.Pagamento;
import br.com.caelum.payfast.modelo.Transacao;
import br.com.caelum.payfast.oauth.TokenDao;


//@Api(value = "pagamentos")
@Path("/pagamentos")
@Singleton
public class PagamentoResource {


	@Inject
	private TokenDao tokenDao;
	
	@Inject
	private HttpServletRequest request;
	
	private static Map<Integer, Pagamento> repo = new HashMap<Integer, Pagamento>();
	private Integer idPagamento = 1;
	
	public PagamentoResource() {
		Pagamento pg = new Pagamento();
		pg.setId(idPagamento++);
		pg.setValor(BigDecimal.TEN);
		pg.comStatusCriado();
		repo.put(pg.getId(), pg);
	}
	
	
//	@ApiOperation(value = "Finds Pets by status",
//		    notes = "Multiple status values can be provided with comma seperated strings",
//		    responseClass = "Pagamento"
//		    )
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Pagamento> allPagamentos(){
		List<Pagamento> pagamentos = new ArrayList<Pagamento>();
		for (Integer chave : repo.keySet()) {
			pagamentos.add(repo.get(chave));
		}
		return pagamentos;
	}
	 
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Pagamento buscaPagamento(@PathParam("id") Integer id){
		return repo.get(id);
	}
	
	

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response cadastro(Transacao transacao){
		
		Response unauthorized = Response.status(Status.UNAUTHORIZED).build();
		
		try {
			OAuthAccessResourceRequest oauth = new OAuthAccessResourceRequest(request);
			String accessToken = oauth.getAccessToken();
			
			if(tokenDao.existeToken(accessToken)){
				Pagamento pagamento = new Pagamento();
				pagamento.setId(idPagamento++);
				pagamento.setValor(transacao.getValor());
				pagamento.comStatusCriado();
				repo.put(pagamento.getId(), pagamento);
				return Response.status(201).entity(pagamento)
						.type(MediaType.APPLICATION_JSON)
						.build();
			}else{
				return unauthorized;
			}
		} catch (Exception e) {
			return unauthorized;
		}
	}
	
	
	@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response confirmaPagamentos(@PathParam("id") Integer id){
		Pagamento pagamento = repo.get(id);
		pagamento.comStatusConfirmado();
		repo.put(id, pagamento);
		return Response.status(200).entity(pagamento)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	@PATCH
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Pagamento cancelarPagamento(@PathParam("id") Integer id){
		Pagamento pag = repo.get(id);
		pag.comStatusCancelado();
		repo.put(id, pag);
		return pag;
	}
	
	
	@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response alteraPagamento(String valor, @PathParam("id") Integer id){
		Pagamento pagamento = repo.get(id);
		pagamento.setValor(new BigDecimal(valor));
		return Response.status(201).entity(pagamento).build();
	}
	
	
	
	
}

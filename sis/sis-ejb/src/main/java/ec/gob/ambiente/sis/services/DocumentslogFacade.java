/**
@autor proamazonia [Christian Báez]  2 dic. 2022

**/
package ec.gob.ambiente.sis.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Documentslog;

@Stateless
@LocalBean
public class DocumentslogFacade extends AbstractFacade<Documentslog, Integer>{

	public DocumentslogFacade() {
		super(Documentslog.class, Integer.class);
	}
	
	public Documentslog agregarActualizar(Documentslog documento) throws Exception{
		if(documento.getDcloId() == null)
			documento = create(documento);
		else
			documento = edit(documento);
		return documento;
	}
	
	public List<Documentslog> listaBitacorasTodas() throws Exception{
		String sql="SELECT D FROM Documentslog D WHERE D.dcloStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<Documentslog> listaBitacorasFecha(Date fechaInicio,Date fechaFin) throws Exception{
		String sql="SELECT D FROM Documentslog D WHERE D.dcloStatus=TRUE AND D.dcloSendDate>=:fechaInicio AND D.dcloSendDate<=:fechaFin ORDER BY D.dcloSendDate";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("fechaInicio", fechaInicio);
		camposCondicion.put("fechaFin", fechaFin);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Realiza la consulta en base a los filtros
	 * @param tipo
	 * @param fechaInicio
	 * @param fechaFin
	 * @param proyecto
	 * @param destinatario
	 * @param nroOficio
	 * @param remitente
	 * @param asunto
	 * @param institucion
	 * @return
	 * @throws Exception
	 */
	public List<Documentslog> listaBitacorasFiltros(Date fechaInicio, Date fechaFin, Projects proyecto, String destinatario, String nroOficio, String remitente, String asunto, String institucion) throws Exception{
		String sql="SELECT D FROM Documentslog D WHERE D.dcloStatus=TRUE ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		if(fechaInicio !=null && fechaFin !=null){
			sql = sql +" AND D.dcloSendDate>=:fechaInicio AND D.dcloSendDate<=:fechaFin ";
			camposCondicion.put("fechaInicio", fechaInicio);
			camposCondicion.put("fechaFin", fechaFin);
		}
		if (proyecto!=null && proyecto.getProjId()!=null){
			sql = sql +" AND D.projects.projId = :proyecto ";
			camposCondicion.put("proyecto", proyecto.getProjId());
		}
		if(destinatario !=null){
			sql = sql +" AND D.dcloAddressee = :destinatario ";
			camposCondicion.put("destinatario", destinatario);
		}
		if(nroOficio !=null){
			sql = sql +" AND D.dcloDocumentNumber = :nrooficio ";
			camposCondicion.put("nrooficio", nroOficio);
		}
		if(remitente !=null){
			sql = sql +" AND D.dcloSender = :remitente ";
			camposCondicion.put("remitente", remitente);
		}
		if(asunto !=null){
			sql = sql +" AND D.dcloSubject = :asunto ";
			camposCondicion.put("asunto", asunto);
		}
		if(institucion !=null){
			sql = sql +" AND D.dcloInstitution = :institucion ";
			camposCondicion.put("institucion", institucion);
		}
		sql = sql + " ORDER BY D.dcloSendDate";
		return findByCreateQuery(sql, camposCondicion);
	}
}


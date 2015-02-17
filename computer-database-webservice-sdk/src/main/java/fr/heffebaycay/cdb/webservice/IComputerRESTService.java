package fr.heffebaycay.cdb.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dto.ComputerDTO;

@Service
@Path("/computers")
public interface IComputerRESTService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response create(ComputerDTO computerDTO);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<ComputerDTO> findAll();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id: [0-9]+}")
	ComputerDTO findById(long id);

	@DELETE
	@Path("/{id: [0-9]+}")
	Response remove(long id);

	@PUT
	@Path("/{id: [0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response update(ComputerDTO computer);

}

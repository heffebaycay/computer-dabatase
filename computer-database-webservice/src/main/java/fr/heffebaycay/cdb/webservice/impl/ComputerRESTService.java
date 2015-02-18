package fr.heffebaycay.cdb.webservice.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.webservice.IComputerRESTService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
@Path("/computers")
public class ComputerRESTService implements IComputerRESTService {

  private static final Logger LOGGER          = LoggerFactory
                                                  .getLogger(ComputerRESTService.class);

  private static final Long   NB_RESULTS_PAGE = 10L;

  @Autowired
  private IComputerService    computerService;

  @Autowired
  private ICompanyService     companyService;

  @Autowired
  private ComputerMapper      computerMapper;

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(ComputerDTO computerDTO) {

    Company company = null;
    if (computerDTO.getCompanyId() > 0) {
      // A valid companyId was supplied
      company = companyService.findById(computerDTO.getCompanyId());
      if (company == null) {
        // Requested company does not exist
        LOGGER.warn("create(): Attempted to create a computer linked an invalid company");
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    Computer computer = computerMapper.fromDTO(computerDTO);
    computer.setCompany(company);

    long computerId = computerService.create(computer);

    URI location = null;
    try {
      location = new URI(String.format("/computers/%d", computerId));
      LOGGER.debug("create(): location is set to {}", location);
    } catch (URISyntaxException e) {
      LOGGER.warn("create(): Failed to generate location URI: {}", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return Response.created(location).status(Status.CREATED).build();

  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ComputerDTO> findAll() {

    List<Computer> computers = computerService.findAll();

    return computerMapper.toDTO(computers);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  public ComputerDTO findById(@PathParam("id") long id) {

    Computer computer = computerService.findById(id);

    return computerMapper.toDTO(computer);
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  public Response remove(@PathParam("id") long id) {

    boolean result = computerService.remove(id);

    Status status = result ? Status.NO_CONTENT : Status.BAD_REQUEST;

    return Response.status(status).build();
  }

  @Override
  @PUT
  @Path("/{id: [0-9]+}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(ComputerDTO computer) {
    Status responseStatus;
    String message = "";

    Computer computerDO = computerService.findById(computer.getId());
    if (computerDO == null) {
      // Computer not found
      responseStatus = Status.NOT_FOUND;
      message = "Requested computer does not exist";
    } else {
      computerMapper.updateDO(computerDO, computer);
      computerService.update(computerDO);
      responseStatus = Status.OK;
    }

    return Response.status(responseStatus).entity(message).build();

  }

  @GET
  @Path("/page/{page: [0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public SearchWrapper<ComputerDTO> findAllPaged(@PathParam("page") long pageNumber) {

    long offset = (pageNumber - 1) * NB_RESULTS_PAGE;

    ComputerPageRequest request = new ComputerPageRequest.Builder()
        .offset(offset)
        .nbRequested(NB_RESULTS_PAGE)
        .sortCriterion(ComputerSortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Computer> sw = computerService.findAll(request);
    
    return computerMapper.convertWrappertoDTO(sw);
    
  }

}

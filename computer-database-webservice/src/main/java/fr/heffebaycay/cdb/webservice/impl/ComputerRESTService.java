package fr.heffebaycay.cdb.webservice.impl;

import com.wordnik.swagger.annotations.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Path("/computers")
@Api(value = "/computers", description = "Computer related operations")
public class ComputerRESTService implements IComputerRESTService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ComputerRESTService.class);

  private static final Long NB_RESULTS_PAGE = 10L;

  @Autowired
  private IComputerService computerService;

  @Autowired
  private ICompanyService companyService;

  @Autowired
  private ComputerMapper computerMapper;

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create a Computer")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "The company does not exist"),
      @ApiResponse(code = 201, message = "Success")
  })
  public Response create(@ApiParam(value = "Computer object", required = true) ComputerDTO computerDTO) {

    Company company = null;
    if (computerDTO.getCompanyId() != null && computerDTO.getCompanyId() > 0) {
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
  @ApiOperation(value = "Get all computers")
  public List<ComputerDTO> findAll() {

    List<Computer> computers = computerService.findAll();

    return computerMapper.toDTO(computers);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Find computer by id")
  public ComputerDTO findById(@ApiParam(value = "ID of Company to fetch", required = true) @PathParam("id") long id) {

    Computer computer = computerService.findById(id);

    if (computer == null) {
      throw new WebApplicationException("Computer does not exist", Status.NOT_FOUND);
    }

    return computerMapper.toDTO(computer);

  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Remove a computer")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success"),
      @ApiResponse(code = 400, message = "Failed to remove the computer")
  })
  public Response remove(@ApiParam(value = "ID of the Computer to remove", required = true) @PathParam("id") long id) {

    boolean result = computerService.remove(id);

    Status status = result ? Status.NO_CONTENT : Status.BAD_REQUEST;

    return Response.status(status).build();
  }

  @Override
  @PUT
  @Path("/{id: [0-9]+}")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update an existing Computer")
  @ApiResponses(value = {
      @ApiResponse(code = 404, message = "Requested computer does not exist"),
      @ApiResponse(code = 200, message = "Success")
  })
  public Response update(@ApiParam(value = "Computer object with the updated details. The ID property must be set.", required = true) ComputerDTO computer) {
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
  @ApiOperation(value = "Fetches a single Computer page")
  public SearchWrapper<ComputerDTO> findAllPaged(@ApiParam(value = "Number of the page to fetch", required = true) @PathParam("page") long pageNumber) {

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

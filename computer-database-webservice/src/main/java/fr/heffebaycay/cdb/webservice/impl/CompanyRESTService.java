package fr.heffebaycay.cdb.webservice.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
@Path("/companies")
@Api(value = "/companies", description = "Company related operations")
public class CompanyRESTService implements ICompanyRESTService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRESTService.class);
  
  private static final Long NB_RESULTS_PAGE = 10L;
  
  @Autowired
  private ICompanyService companyService;
  
  @Autowired
  private CompanyMapper companyMapper;

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create a Company")
  public CompanyDTO create(@ApiParam(value = "Company object", required = true) CompanyDTO companyDTO) {
    
    Company company = companyMapper.fromDTO(companyDTO);
    long companyId = companyService.create(company);
    
    companyDTO.setId(companyId);
    
    return companyDTO;
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all companies")
  public List<CompanyDTO> findAll() {
    
    List<Company> companies = companyService.findAll();
    
    return companyMapper.toDTO(companies);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Find company by ID")
  public CompanyDTO findById(@ApiParam(value = "ID of Company to fetch", required = true) @PathParam("id") long id) {
    
    Company company = companyService.findById(id);
    
    return companyMapper.toDTO(company);
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Remove a company and all associated computers")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Company removed successfully" )
  })
  public Response remove(@ApiParam(value = "ID of Company to remove", required = true) @PathParam("id") long id) {
    
    companyService.remove(id);
    
    return Response.status(Status.NO_CONTENT).build();
    
  }
  
  @GET
  @Path("/page/{page: [0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Fetches a single Company page")
  public SearchWrapper<CompanyDTO> findAllPaged(@ApiParam(value = "Number of the page to fetch", required = true) @PathParam("page") long pageNumber) {
    
    Long offset = (pageNumber - 1) * NB_RESULTS_PAGE;

    CompanyPageRequest pageRequest = new CompanyPageRequest.Builder()
        .offset(offset)
        .nbRequested(NB_RESULTS_PAGE)
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    
    SearchWrapper<Company> companies = companyService.findAll(pageRequest);
    
    SearchWrapper<CompanyDTO> dtoWrapper = companyMapper.convertWrappertoDTO(companies);
    
    return dtoWrapper;
    
  }

}

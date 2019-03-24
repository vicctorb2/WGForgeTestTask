package com.vicctorb.wgtesttask36;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicctorb.wgtesttask36.controller.RequestsCountController;
import com.vicctorb.wgtesttask36.entities.CatsEntity;
import com.vicctorb.wgtesttask36.repository.CatsRepository;
import com.vicctorb.wgtesttask36.validation.OffsetBasedPageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WgTestTask36ApplicationTests {

	String REST_URI = "http://localhost:8080/";
	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	CatsRepository catsRepository;


	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void catsWithCorrectLimit() throws Exception {
		int limit = 2;
		String uri = REST_URI + "cats?limit=" + limit;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content, CatsEntity[].class);
		assertEquals(200, status);
		assertTrue(catsEntities.length == limit);
	}

	@Test
	public void catsWithIncorrectLimit() throws Exception {
		int limit = -3;
		String uri = REST_URI + "cats?limit=" + limit;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		assertTrue(mvcResult.getResponse().getContentAsString().equals("Incorrect 'limit' value. 'Limit' value must be positive\n"));
	}

	@Test
	public void catsWithCorrectOffset() throws Exception {
		int offset = 3;
		String uri = REST_URI + "cats?offset=" + offset;
		MvcResult mvcResultWithOffset = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = mvcResultWithOffset.getResponse().getStatus();
		String content = mvcResultWithOffset.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content,CatsEntity[].class);
		//status must be OK
		assertEquals(200,status);
		//is this a json type response
		assertTrue(Objects.equals(mvcResultWithOffset.getResponse().getContentType(), MediaType.APPLICATION_JSON_UTF8_VALUE));
		//trying to test an offset (first entity from result equals third entity from db)
		assertTrue(catsEntities[0].equals(catsRepository.findAll().get(3)));
	}

	@Test
	public void catsWithIncorrectOffset() throws Exception {
		int offset = -5;
		String uri = REST_URI + "cats?offset=" + offset;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(400,status);
		assertEquals(content,"Incorrect 'offset' value. 'Offset' value must be positive or zero\n");

	}

	@Test
	public void catsWithCorrectAttribute() throws Exception {
		String attribute = "name";
		String uri = REST_URI + "cats?attribute=" + attribute;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content,CatsEntity[].class);
		assertEquals(200,status);
		List<CatsEntity> entitiesFromDB =  catsRepository.findAll(new Sort(Sort.Direction.ASC,attribute));
		for (int i=0;i<catsEntities.length;i++){
			assertEquals(catsEntities[i],entitiesFromDB.get(i));
		}
	}

	@Test
	public void catsWithIncorrectAttribute() throws Exception {
		String attribute = "name_wtf";
		String uri = REST_URI + "cats?attribute=" + attribute;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(400,status);
		assertEquals(content,"Error attribute value. It can be 'color', 'name', 'tail_length' or 'whiskers_length\n");
	}



	@Test
	public void catsWithCorrectOrder() throws Exception {
		String order = "desc&attribute=name";
		String uri = REST_URI + "cats?order=" + order;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content,CatsEntity[].class);
		assertEquals(200,status);
		List<CatsEntity> entitiesFromDB =  catsRepository.findAll(new Sort(Sort.Direction.DESC,"name"));
		for (int i=0;i<catsEntities.length;i++){
			assertEquals(catsEntities[i],entitiesFromDB.get(i));
		}
	}

	@Test
	public void catsWithIncorrectOrder() throws Exception {
		String order = "descorascidontknow&attribute=name";
		String uri = REST_URI + "cats?order=" + order;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(400,status);
		assertEquals(content,"Error 'order' value.'Order' parameter value can be only 'desc' or 'asc'\n");
	}

	@Test
	public void newCatWithIncorrectParams() throws Exception {
		String uri = REST_URI + "cat";
		CatsEntity newCatEntity = new CatsEntity("NameForTest","2d",-22,12);
		int status;
		String content;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType("application/json").content(mapToJson(newCatEntity))).andReturn();
		content = mvcResult.getResponse().getContentAsString();
		status = mvcResult.getResponse().getStatus();
		assertEquals(400,status);
	}

	@Test
	public void catsWithoutParams() throws Exception {
		String uri = REST_URI + "cats";
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content,CatsEntity[].class);
		List<CatsEntity> entitiesFromDB = catsRepository.findAll();
		assertEquals(status,200);
		for (int i=0;i<catsEntities.length;i++){
			assertEquals(catsEntities[i],entitiesFromDB.get(i));
		}
	}

	@Test
	public void catsWithAllParams() throws Exception {
		String uri = REST_URI + "cats?attribute=name&order=desc&offset=3&limit=2";
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		CatsEntity[] catsEntities = mapFromJson(content,CatsEntity[].class);
		List<CatsEntity> fromDbEntities = catsRepository.findAll(new OffsetBasedPageRequest(3,2,new Sort(Sort.Direction.DESC,"name"))).getContent();
		for (int i=0;i<catsEntities.length;i++){
			assertEquals(catsEntities[i],fromDbEntities.get(i));
		}
	}

	@Test
	public void pingTest()throws Exception{
		String uri = REST_URI + "ping";
		int status;
		String content;
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		status = mvcResult.getResponse().getStatus();
		content = mvcResult.getResponse().getContentAsString();
		assertEquals(content,"Cats Service. Version 0.1");
		assertEquals(status,200);
	}

	@Test
	public void newCatWithCorrectParams() throws Exception {
		String uri = REST_URI + "cat";
		CatsEntity newCatEntity = new CatsEntity("NameForTest","black",11,12);
		int status;
		String content;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType("application/json").content(mapToJson(newCatEntity))).andReturn();
		content = mvcResult.getResponse().getContentAsString();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200,status);
		assertEquals(mapToJson(newCatEntity), content);
		catsRepository.delete(newCatEntity);
	}

	@Test
	public void ddosTest() throws Exception {
		RequestsCountController.getInstance().reset();
		String uri = REST_URI + "ping";
		int countOfRequests=0;
		int status = 200;
		while(status!=429){
			MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
			status = mvcResult.getResponse().getStatus();
			countOfRequests++;
		}
		MvcResult mvcResult = mvc.perform((MockMvcRequestBuilders.get(uri))).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertTrue(countOfRequests>600);
		assertEquals(status,429);
		RequestsCountController.getInstance().reset();
	}


}

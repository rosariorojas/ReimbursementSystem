package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.revature.beans.Reimbursement;
import com.revature.beans.ReimbursementStatus;
import com.revature.beans.User;
import com.revature.services.ReimbursementService;

public class ReimbursementController {
	private Logger log = Logger.getRootLogger();
	private ReimbursementService rs = new ReimbursementService();

	public void delegateGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("GET request delegated to reimbursement controller");
		String actualURL = request.getRequestURI()
				.substring(request.getContextPath().length() + "/reimbursements".length());
		log.debug("GET: " + actualURL);
		if (actualURL.startsWith("/aprove-deny")) {
			User u = (User) request.getSession().getAttribute("user");
			try {
				List<Reimbursement> updateReimb = new ArrayList<>();
				updateReimb = rs.listToApproveOrDeny(u);
				ObjectMapper om = new ObjectMapper();
				ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(updateReimb);
				PrintWriter writer = response.getWriter();
				writer.write(json);
				writer.close();

				log.debug("Finance Manager Can Approve" + json);
				log.debug("wrote users to the body of the response");

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (actualURL.startsWith("/request")) {
			log.debug("Request to update the reimbursement");
			User u = (User) request.getSession().getAttribute("user");
			String json;
			//BufferedReader actualJson;

			// read the body of the request into a single string
			try {
				//actualJson = request.getReader();
				//log.debug(actualJson);
				
				json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
				log.trace("json received: " + json);
				log.debug("request to update reimbursement");

				ObjectMapper om = new ObjectMapper();
				// convert the body of the request into an actual object
				// ObjectMapper om = new ObjectMapper();
				Reimbursement r = om.readValue(json, Reimbursement.class);
				log.trace("amount received: " + r.getReimbAmount());
				log.trace("description status id: " + r.getReimbDescription());
				log.trace("reimbursement type status id: " + r.getReimbStatusId());

				rs.requestReimbursement(r,u);
				log.debug("finished updating the reimburement");
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void delegatePost(HttpServletRequest request, HttpServletResponse response) {
		log.debug("Post request delegated to reimbursement controller");
		String actualURL = request.getRequestURI()
				.substring(request.getContextPath().length() + "/reimbursements".length());

		if ("".equals(actualURL)) {
			User u = (User) request.getSession().getAttribute("user");
			log.debug("User from session from login: " + request.getSession().getAttribute("user"));
			log.debug(actualURL);
			try {
				List<Reimbursement> allReimb = rs.getReimbursementsById(u);

				ObjectMapper om = new ObjectMapper();
				ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(allReimb);

				PrintWriter writer = response.getWriter();
				writer.write(json);
				writer.close();

				log.debug("wrote users to the body of the response");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if (actualURL.startsWith("/all-reimbursements")) {
			User u = (User) request.getSession().getAttribute("user");
			log.debug("User from session from login: " + request.getSession().getAttribute("user"));
			log.debug(actualURL);
			try {
				List<Reimbursement> allReimb = rs.getAllReimbursements();

				ObjectMapper om = new ObjectMapper();
				ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(allReimb);

				PrintWriter writer = response.getWriter();
				writer.write(json);
				writer.close();
				log.debug(json);
				log.debug("wrote all reimbursements to the body of the response");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (actualURL.startsWith("/update")) {
			log.debug("Request to update the reimbursement");
			User u = (User) request.getSession().getAttribute("user");
			String json;

			// read the body of the request into a single string
			try {
				json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
				log.trace("json received: " + json);
				log.debug("request to update reimbursement");

				ObjectMapper om = new ObjectMapper();
				// convert the body of the request into an actual object
				// ObjectMapper om = new ObjectMapper();
				Reimbursement r = om.readValue(json, Reimbursement.class);
				log.trace("reimbId received: " + r.getReimbId());
				log.trace("new status id: " + r.getReimbStatusId());

				rs.updateReimbursement(r, u);
				log.debug("finished updating the reimburement");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (actualURL.startsWith("/request")) {
			log.debug("Request to update the reimbursement");
			User u = (User) request.getSession().getAttribute("user");
			String json;

			// read the body of the request into a single string
			try {
				//json = request.getReader();
				//log.debug(json);
				json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
				log.trace("json received: " + json);
				log.debug("request to update reimbursement");

				ObjectMapper om = new ObjectMapper();
				// convert the body of the request into an actual object
				// ObjectMapper om = new ObjectMapper();
				Reimbursement r = om.readValue(json, Reimbursement.class);
				log.trace("amount received: " + r.getReimbAmount());
				log.trace("description status id: " + r.getReimbDescription());
				log.trace("reimbursement type status id: " + r.getReimbStatusId());

				rs.requestReimbursement(r,u);
				log.debug("finished updating the reimburement");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

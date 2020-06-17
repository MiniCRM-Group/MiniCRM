// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.step.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;

import com.google.step.data.Lead;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Testing code for the Cloud Function Servlet to receive lead data */
@WebServlet("/CFTest")
public class CloudFunctionTest extends HttpServlet {

  @Override
  public void init(){
    //This is the init method!
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //This is the get method!
    response.setContentType("text/html;");
    Lead myLead = new Lead("Hello!");
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    response.getWriter().println(gson.toJson(myLead));

  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //This is the post method!
    response.setContentType("text/html;");
    Gson gson = new Gson();
    //response.getWriter().println(gson.toJson());


  }
}

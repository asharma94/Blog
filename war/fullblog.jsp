<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 <%@ page import="java.util.Collections" %>
 <%@ page import="com.googlecode.objectify.Objectify" %>
 <%@ page import="com.googlecode.objectify.ObjectifyService" %>
 <%@ page import="blog.BlogPost" %>
 
<html>
  <head>
	 <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

  <body>

<%
    String guestbookName = request.getParameter("guestbookName");
    if (guestbookName == null) {
        guestbookName = "default";
    }
    pageContext.setAttribute("guestbookName", guestbookName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
      pageContext.setAttribute("user", user);
%>

<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>

<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to make a blog post.</p>
<%
    }
%>

<%
//ObjectifyService.register(BlogPost.class);
List<BlogPost> blogposts = ObjectifyService.ofy().load().type(BlogPost.class).list();   
Collections.sort(blogposts); 
    if (blogposts.isEmpty()) {
        %>
        <p>Blog '${fn:escapeXml(guestbookName)}' has no posts.</p>
        <%
    } else {
        %>

        <p>Messages in Blog '${fn:escapeXml(guestbookName)}'.</p>
        <a href="/blog.jsp">Show less posts</a>

        <%
        for (int x = (blogposts.size()-1);x>=0;x--) {
        	BlogPost g = blogposts.get(x);
            pageContext.setAttribute("greeting_content",
                                     g.getContent());
            if (g.getUser() == null) {
                %>

                <p>An anonymous person wrote:</p>

                <%
            } else {
                pageContext.setAttribute("greeting_user",
                                        g.getUser());
                %>
                <p><b>${fn:escapeXml(greeting_user.nickname)}</b> wrote:</p>
                <%
            }
            %>
            <blockquote>${fn:escapeXml(greeting_content)}</blockquote>
            <%
        }
    }
%>


<% 
if (user != null) {
	%>
	<a href="/post.jsp">Create Post</a>
	<%
	
} %>
		
  </body>
</html>
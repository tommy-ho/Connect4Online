<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="UTF-8">
  <title>Connect 4</title>
  
      <link rel="stylesheet" href="css/style.css">

  
</head>

<body>
  <!--Google Font - Work Sans-->
<link href='https://fonts.googleapis.com/css?family=Work+Sans:400,300,700' rel='stylesheet' type='text/css'>

<form id="signInForm" action="StartServerServlet" method="post">
<div class="container">
  <div class="profile--open">
    <button class="profile__avatar" id="toggleProfile">
     <img src="images/c4.jpg"  alt="Avatar" /> 
    </button>
    <div class="profile__form">
      <div class="profile__fields">
        <div class="field">
          <div>${status}</div><br><br>
          <input name="signIn" type="text" id="fieldUser" class="input" required pattern=.*\S.* />
          <label for="fieldUser" class="label">Room Name</label>
        </div>
        <div class="field">
        	<input type="radio" name="boardSize" value="standard" checked="yes" />  Standard (7x6)<br />
			<input type="radio" name="boardSize" value="epic" />  Epic (10x9)<br />
        </div>
        <div class="profile__footer">
          <button name="request" value="login" type="submit" id="login" class="btn">Start Server</button>
        </div>
      </div>
     </div>
  </div>
</div>
</form>

</body>
</html>

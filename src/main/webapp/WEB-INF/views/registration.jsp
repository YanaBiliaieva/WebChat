<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<html>
<head>
</head>
<link rel="stylesheet" href="./static/css/cssfile.css">
<body>

<div class="container">
    <h2>Registration</h2>

    <div class="row">
        <div class="col-sm-6 col-sm-push-3">
            <sf:form modelAttribute="user" action="" cssClass="form-horizontal">
                <div class="panel panel-primary">
                    <div class="panel-heading">Registration</div>
                    <div class="panel-body">

                        <div class="error">
                                ${error}
                        </div>

                        <div class="form-group">
                            <label for="name" class="col-sm-3 control-label"><s:message code="auth.name"></s:message></label>
                            <div class="col-sm-9">
                                <sf:input path="name" cssClass="form-control"/>
                            </div>
                            <sf:errors path="name" cssClass="error"/>
                        </div>

                        <div class="form-group">
                            <label for="login" class="col-sm-3 control-label"><s:message code="auth.login"></s:message></label>
                            <div class="col-sm-9">
                                <sf:input path="login" cssClass="form-control"/>
                            </div>
                            <sf:errors path="login" cssClass="error"/>
                        </div>

                        <div class="form-group">
                            <label for="password" class="col-sm-3 control-label"><s:message code="auth.password"></s:message></label>
                            <div class="col-sm-9">
                                <sf:password path="password" cssClass="form-control" />
                            </div>
                            <sf:errors path="password" cssClass="error"/>
                        </div>
                    </div>

                    <div class="panel-footer text-center">
                        <button type="submit" class="btn btn-primary"><s:message code="auth.submit"/></button>
                    </div>
                </div>
            </sf:form>
        </div>
    </div>
</div>



</body>
</html>
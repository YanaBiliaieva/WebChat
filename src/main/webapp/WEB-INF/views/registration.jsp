<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<html>
<body>
<div class="container">
    <h2>Regisration</h2>
    <div class="row">
        <div class="col-sm-6 col-sm-push-3">
            <sf:form modelAttribute="user" action="" cssClass="form-horizontal">
                <div class="panel panel-primary">
                    <div class="panel-heading">Registration</div>
                    <div class="panel-body">

                            ${error}

                        <div class="form-group">
                            <label for="name" class="col-sm-3 control-label"><spring:message code="auth.name"></spring:message></label>
                            <div class="col-sm-9">
                                <sf:input path="name" cssClass="form-control"/>
                            </div>
                            <sf:errors path="name" cssClass="error"/>
                        </div>

                        <div class="form-group">
                            <label for="login" class="col-sm-3 control-label"><spring:message code="auth.login"></spring:message></label>
                            <div class="col-sm-9">
                                <sf:input path="login" cssClass="form-control"/>
                            </div>
                            <sf:errors path="login" cssClass="error"/>
                        </div>

                        <div class="form-group">
                            <label for="password" class="col-sm-3 control-label"><spring:message code="auth.password"></spring:message></label>
                            <div class="col-sm-9">
                                <sf:password path="password" cssClass="form-control" />
                            </div>
                            <sf:errors path="password" cssClass="error"/>
                        </div>
                    </div>

                    <div class="panel-footer text-center">
                        <button type="submit" class="btn btn-primary"><spring:message code="auth.submit"/></button>
                    </div>
                </div>
            </sf:form>
        </div>
    </div>
</div>
</body>
</html>

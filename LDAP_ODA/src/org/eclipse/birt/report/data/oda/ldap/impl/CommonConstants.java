/*
 *************************************************************************
 * Copyright (c) 2011 Ramesh S
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.ldap.impl;

public interface CommonConstants 
{
	public static final String CONN_HOSTNAME_PROP = "HOSTNAME";
	public static final String CONN_SECURITYCREDENTIALS_PROP = "SecurityCredentials";
	public static final String CONN_AUTHENTICANTION_PROP = "Authentication";
	public static final String CONN_SECURITYPRINCIPAL_PROP = "SecurityPrincipal";
	public static final String CONN_PORT_PROP = "PORT";
	public static final String DATASET_SEARCHSCOPE_PROP = "SearchScope";
	
	///
	
	public static final String QUERY_SELECT_TAG  = "<SELECT ATTRIBUTES>";
	public static final String QUERY_FROM_TAG  = "<FROM BASE DN>";
	public static final String QUERY_WHERE_TAG  = "<WHERE>";
	public static final String SEARCH_SCOPE_PARAMETER = "Search Scope (OBJECT or ONELEVEL or SUBTREE)";
	public static final String SEARCH_MULTI_VALUE_ATTR_DELIMITER_PARAMETER = "MultiValue Attribute Delimiter";
	public static final Object SEARCH_SCOPE_OBJECT = "OBJECT";
	public static final Object SEARCH_SCOPE_ONELEVEL = "ONELEVEL";
}


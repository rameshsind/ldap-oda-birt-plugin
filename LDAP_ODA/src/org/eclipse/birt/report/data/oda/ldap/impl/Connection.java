/*
 *************************************************************************
 * Copyright (c) 2011 Ramesh S
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.ldap.impl;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an LDAP ODA runtime driver.
 */
public class Connection implements IConnection
{
    private boolean m_isOpen = false;
    InitialDirContext context = null;
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		
		String hostName = connProperties.getProperty(CommonConstants.CONN_HOSTNAME_PROP);
		String port = connProperties.getProperty(CommonConstants.CONN_PORT_PROP);
		String securityCredentials = connProperties.getProperty(CommonConstants.CONN_SECURITYCREDENTIALS_PROP);
		String authentication = connProperties.getProperty(CommonConstants.CONN_AUTHENTICANTION_PROP);
		String securityPrincipal = connProperties.getProperty(CommonConstants.CONN_SECURITYPRINCIPAL_PROP);
		
		
		String url = "ldap://"+hostName+":"+port+"/"; 
		
		try
		{
			context = new InitialDirContext(initializeLDAP(url, authentication,	securityPrincipal, securityCredentials));
		}
		catch (NamingException e) 
		{
			throw new OdaException(e);
		}
	    m_isOpen = true;        
 	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
		try 
		{
			context.close();
		} catch (NamingException e) 
		{
			throw new OdaException(e);
		}
	    m_isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
        // TODO Auto-generated method stub
		return m_isOpen;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType ) throws OdaException
	{
	    // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new DataSetMetaData( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
        // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new Query(context);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException
	{
		return 0;	// no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException
	{
	    // do nothing; assumes no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException
	{
        // do nothing; assumes no transaction support needed
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu.util.ULocale)
     */
    public void setLocale( ULocale locale ) throws OdaException
    {
        // do nothing; assumes no locale support
    }
    
    private Hashtable<String, String> initializeLDAP(String url, String authentication,String securityPricipal, String securityCredentials)	throws NamingException 
	{
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.REFERRAL, "ignore");
		env.put(Context.SECURITY_AUTHENTICATION, authentication);
		if(securityPricipal != null)
		{
			env.put(Context.SECURITY_PRINCIPAL, securityPricipal);
		}
		if(securityCredentials != null)
		{
			env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		}
		// env.put("com.sun.jndi.ldap.connect.pool", "true");
		// env.put("com.sun.jndi.ldap.connect.pool.maxsize","50");
		// env.put("com.sun.jndi.ldap.connect.pool.prefsize", "50");

		return env;
	}
    
}

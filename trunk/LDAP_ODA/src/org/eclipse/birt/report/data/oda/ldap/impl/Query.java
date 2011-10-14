/*
 *************************************************************************
 * Copyright (c) 2011 Ramesh S
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.ldap.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * Implementation class of IQuery for a LDAP ODA runtime driver.
 *  
 */
public class Query implements IQuery
{
	private int m_maxRows;
    private String m_preparedText;
    private InitialDirContext context;
    private String[] attrIDs = null;
	private String m_base;
	private String attributes;
	private int searchScope = SearchControls.SUBTREE_SCOPE;
	private String multiValueAttributeDelimiter = "\n";

    
    
	public Query(InitialDirContext context) 
	{
		this.context=context;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare( String queryText ) throws OdaException
	{
		
		//Split the query string in to Attributes, BASE DN, Filter Text
		
		queryText = queryText.trim();
		String queryInfo[] = processQueryText(queryText)	;	
		
		//Remove all the new line and leading and trailing spaces
		attributes = queryInfo[0].trim();
		
		attributes = attributes.replaceAll("\r\n", "");
		attributes = attributes.replaceAll("\n", "");
		attrIDs =  attributes.split(",");
		for(int count = 0 ; count<attrIDs.length; count++ )
		{
			attrIDs[count] = attrIDs[count].trim();
		}
		
		m_base = queryInfo[1].trim();
		
		
		m_preparedText = queryInfo[2].trim();
		
	}
	
	

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException
	{
        // TODO Auto-generated method stub
        m_preparedText = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
		return new ResultSetMetaData(attrIDs);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException
	{
		SearchControls constraints = new SearchControls();
        constraints.setSearchScope(searchScope);
        constraints.setReturningAttributes(attrIDs);
        constraints.setCountLimit(m_maxRows);
		NamingEnumeration enumeration;
		try 
		{
			enumeration = context.search( m_base, m_preparedText, constraints );
		} catch (NamingException e) 
		{
			throw new OdaException( e);
		}
		IResultSet resultSet = new ResultSet(enumeration, attrIDs, multiValueAttributeDelimiter);
		resultSet.setMaxRows( getMaxRows() );
		return resultSet;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
	    m_maxRows = max;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException
	{
		return m_maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value ) throws OdaException
	{
       
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		
        if (parameterId == 1) // Search Scope
        {
			try 
			{
				if(value != null )
				{
					if( value.trim().equals(CommonConstants.SEARCH_SCOPE_OBJECT))
					{
						searchScope = SearchControls.OBJECT_SCOPE;
						return;
					}
					else if( value.trim().equals(CommonConstants.SEARCH_SCOPE_ONELEVEL))
					{
						searchScope = SearchControls.ONELEVEL_SCOPE;
						return;
					}
					
				}
				searchScope = SearchControls.SUBTREE_SCOPE;
			} 
			catch (Throwable e)
			{
				searchScope = SearchControls.SUBTREE_SCOPE;
			}
        }
        else if (parameterId == 2) // MultiValue Attribute Delimiter
        {
        	if(value != null && !value.equals(""))
        	{
        		multiValueAttributeDelimiter = value;
        	}
        }
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter

	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean( String parameterName, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
     */
    public void setBoolean( int parameterId, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub       
        // only applies to input parameter
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject( String parameterName, Object value )
            throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int, java.lang.Object)
     */
    public void setObject( int parameterId, Object value ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to input parameter
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
     */
    public void setNull( String parameterName ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
     */
    public void setNull( int parameterId ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to input parameter
    }

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
		return new ParameterMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		// only applies to sorting, assumes not supported
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException
	{
		// only applies to sorting
		return null;
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.datatools.connectivity.oda.spec.QuerySpecification)
     */
    public void setSpecification( QuerySpecification querySpec )
            throws OdaException, UnsupportedOperationException
    {
        // assumes no support
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
     */
    public QuerySpecification getSpecification()
    {
        // assumes no support
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
     */
    public String getEffectiveQueryText()
    {
        // TODO Auto-generated method stub
        return m_preparedText;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
     */
    public void cancel() throws OdaException, UnsupportedOperationException
    {
        // assumes unable to cancel while executing a query
        throw new UnsupportedOperationException();
    }
    
    private String[] processQueryText(String queryText) throws OdaException 
	{
		if( !queryText.startsWith(CommonConstants.QUERY_SELECT_TAG) )
        {
          throw new OdaException("Improper Query : No "+CommonConstants.QUERY_SELECT_TAG+" Tag Found");
        }
         queryText = queryText.substring(queryText.indexOf(CommonConstants.QUERY_SELECT_TAG)+CommonConstants.QUERY_SELECT_TAG.length());
         if( queryText.indexOf(CommonConstants.QUERY_FROM_TAG) == -1 )
         {
             throw new OdaException("Improper Query : No "+CommonConstants.QUERY_FROM_TAG+" Tag Found");
         }
         String selectText = queryText.substring(0, queryText.indexOf(CommonConstants.QUERY_FROM_TAG));
         if( selectText == null || selectText.trim().equals("")  )
         {
              throw new OdaException("Improper Query : No "+CommonConstants.QUERY_SELECT_TAG+ " Clause Found");
         }
         queryText = queryText.substring(queryText.indexOf(CommonConstants.QUERY_FROM_TAG)+CommonConstants.QUERY_FROM_TAG.length());
         if( queryText.indexOf(CommonConstants.QUERY_WHERE_TAG) == -1 )
         {
              throw new OdaException("Improper Query : No "+CommonConstants.QUERY_WHERE_TAG+" Tag Found");
         }
         String fromText = queryText.substring(0, queryText.indexOf(CommonConstants.QUERY_WHERE_TAG));
         if( fromText == null || fromText.trim().equals("")  )
         {
             throw new OdaException("Improper Query : No "+CommonConstants.QUERY_FROM_TAG+" Clause Found");
         }
         String filterText = queryText.substring(queryText.indexOf(CommonConstants.QUERY_WHERE_TAG)+CommonConstants.QUERY_WHERE_TAG.length());
         if( filterText == null || filterText.trim().equals("")  )
         {
            throw new OdaException("Improper Query : No "+CommonConstants.QUERY_WHERE_TAG+" Clause Found");
         }
 		
		return new String[]{selectText,fromText,filterText};
	}
    
}

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
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchResult;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IResultSet for a LDAP ODA runtime driver.
 *  
 */
public class ResultSet implements IResultSet
{
	private int m_maxRows;
    private int m_currentRowId;
    private IResultSetMetaData resultSetMetaData;
	private NamingEnumeration enumeration;
	private NamingEnumeration  attribsList;
	private String multiValueAttributeDelimiter;
	
	Map<String, String> columnNameValueMap = new HashMap<String, String>();
	
	public ResultSet(NamingEnumeration enumeration, String[] attrIDs, String multiValueAttributeDelimiter) {
		// TODO Auto-generated constructor stub
				this.enumeration =enumeration;
				resultSetMetaData = new ResultSetMetaData(attrIDs);
				this.multiValueAttributeDelimiter = multiValueAttributeDelimiter;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this result set.
         */
		return resultSetMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		m_maxRows = max;
	}
	
	/**
	 * Returns the maximum number of rows that can be fetched from this result set.
	 * @return the maximum number of rows to fetch.
	 */
	protected int getMaxRows()
	{
		return m_maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	public boolean next() throws OdaException
	{
		// TODO replace with data source specific implementation
        
        // simple implementation done below for demo purpose only
        int maxRows = getMaxRows();
        if( maxRows <= 0 )  // no limit is specified
            maxRows = 5;    // hard-coded for demo purpose
        
        if( m_currentRowId < maxRows )
        {
        	try {
				if ( enumeration.hasMore() )
				{
					SearchResult result = (SearchResult)enumeration.next();
				    
				    System.out.println("result.getName() : " + result.getName());
				    
				    
				    Attributes attribs = result.getAttributes();
				    attribsList = attribs.getAll();
				    columnNameValueMap = new HashMap<String, String>();
				    while ( attribsList.hasMore() )
				    {
				        BasicAttribute basicAttribute = (BasicAttribute)attribsList.next();
				        System.out.println("basicAttribute.getID() : " + basicAttribute.getID());
				       
				        NamingEnumeration values = basicAttribute.getAll();
				        while (values.hasMore()) 
				        {
				        	String value=values.next().toString();
				            System.out.println(" values.next().toString() : "+ value);
				            String prevValue = columnNameValueMap.get(basicAttribute.getID());
				            if( prevValue == null || prevValue.equals(""))
				            {
				            	columnNameValueMap.put(basicAttribute.getID(), value);
				            }
				            else
				            {
				            	columnNameValueMap.put(basicAttribute.getID(),prevValue+multiValueAttributeDelimiter+ value);
				            }
				        }
				    }
				    m_currentRowId++;
				    return true;    
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new OdaException(e);
			}
			
            
            
        }
        
        return false;        
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	public void close() throws OdaException
	{
        // TODO Auto-generated method stub       
        m_currentRowId = 0;     // reset row counter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	public int getRow() throws OdaException
	{
		return m_currentRowId;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	public String getString( int index ) throws OdaException
	{
		String colName = this.resultSetMetaData.getColumnName(index);
		return getString(colName);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.String)
	 */
	public String getString( String columnName ) throws OdaException
	{
		return columnNameValueMap.get( columnName );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	public int getInt( int index ) throws OdaException
	{
        // TODO replace with data source specific implementation
        
        // hard-coded for demo purpose
        return getRow();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String)
	 */
	public int getInt( String columnName ) throws OdaException
	{
	    return getInt( findColumn( columnName ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	public double getDouble( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.String)
	 */
	public double getDouble( String columnName ) throws OdaException
	{
	    return getDouble( findColumn( columnName ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String columnName ) throws OdaException
	{
	    return getBigDecimal( findColumn( columnName ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	public Date getDate( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String)
	 */
	public Date getDate( String columnName ) throws OdaException
	{
	    return getDate( findColumn( columnName ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	public Time getTime( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String)
	 */
	public Time getTime( String columnName ) throws OdaException
	{
	    return getTime( findColumn( columnName ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String columnName ) throws OdaException
	{
	    return getTimestamp( findColumn( columnName ) );
	}

    /* 
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
     */
    public IBlob getBlob( int index ) throws OdaException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /* 
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String)
     */
    public IBlob getBlob( String columnName ) throws OdaException
    {
        return getBlob( findColumn( columnName ) );
    }

    /* 
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
     */
    public IClob getClob( int index ) throws OdaException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /* 
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String)
     */
    public IClob getClob( String columnName ) throws OdaException
    {
        return getClob( findColumn( columnName ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
     */
    public boolean getBoolean( int index ) throws OdaException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.String)
     */
    public boolean getBoolean( String columnName ) throws OdaException
    {
        return getBoolean( findColumn( columnName ) );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
     */
    public Object getObject( int index ) throws OdaException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.String)
     */
    public Object getObject( String columnName ) throws OdaException
    {
        return getObject( findColumn( columnName ) );
    }

    /*
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
     */
    public boolean wasNull() throws OdaException
    {
        // TODO Auto-generated method stub
        
        // hard-coded for demo purpose
        return false;
    }

    /*
     * @see org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.String)
     */
    public int findColumn( String columnName ) throws OdaException
    {
        // TODO replace with data source specific implementation
        
        // hard-coded for demo purpose
        int columnId = 1;   // dummy column id
        if( columnName == null || columnName.length() == 0 )
            return columnId;
        String lastChar = columnName.substring( columnName.length()-1, 1 );
        try
        {
            columnId = Integer.parseInt( lastChar );
        }
        catch( NumberFormatException e )
        {
            // ignore, use dummy column id
        }
        return columnId;
    }
    
}

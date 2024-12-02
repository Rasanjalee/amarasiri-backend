package com.amarasiricoreservice.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaFile
{
    private String url;
    private Long id;
    private String name;
    private String filePath;
    private String type;
    private byte[] file;
    private short height;
    private short width;

    public MediaFile()
    {
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the filePath
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath( String filePath )
    {
        this.filePath = filePath;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public byte[] getFile()
    {
        return file != null ? ( byte[] ) file.clone() : null;
    }

    public void setFile( byte[] file )
    {
        //this.file = file;
        this.file = file != null ? file.clone() : null;
    }

    public short getHeight()
    {
        return height;
    }

    public void setHeight( short height )
    {
        this.height = height;
    }

    public short getWidth()
    {
        return width;
    }

    public void setWidth( short width )
    {
        this.width = width;
    }
}


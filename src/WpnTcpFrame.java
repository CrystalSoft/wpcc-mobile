/*
 * CWpnTcpFrame.java
 *
 * Created on 2006/09/03, 1:58
 * Modified on 2009/03/21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author [Nushi]@[2SN] Copyright 2006 2SN http://2sen.dip.jp/
 * @modified Emulator Copyright 2009 CrystalSoft http://www.emulator.netsons.org
 */

public class WpnTcpFrame
{
    protected byte [] m_FrameBuffer;
    protected short m_sFrameType;
    protected int m_iFrameSize;
    protected int m_iItemPos;

    private final int m_iBufferSize = 1028;	//Default Buffer Size 1024 + 4byte

    /** Creates a new instance of CWpnTcpFrame */
    public WpnTcpFrame()
    {
	m_FrameBuffer = new byte[m_iBufferSize + 1];
	m_iFrameSize = 0;
	m_iItemPos = 0;
    }

    //Convert byte to int
    private static int ByteToInt(final byte bCode)
    {
	return (int)((bCode >= 0)? bCode : bCode + 256);
    }

    //Get Refarence to Buffer
    protected byte [] GetBuffer()
    {
	return m_FrameBuffer;
    }

    //Get Frame Size
    public int GetFrameSize()
    {
	return m_iFrameSize;
    }

    //Get Frame Type
    public short GetFrameType()
    {
	return m_sFrameType;
    }

    //Decrypt and Analyze Frame Header
    public void DecryptHeader(WpnEnc.CWpnCryptKey pCryptKey)
    {
	//Decrypt Header
	WpnEnc.DecryptTcp(m_FrameBuffer, 4, pCryptKey);
	
	//Analyze Frame Type
	m_sFrameType = (short)(ByteToInt(m_FrameBuffer[0]) + ByteToInt(m_FrameBuffer[1]) * 256);
	
	//Analyze Frame Size
	m_iFrameSize = ByteToInt(m_FrameBuffer[2]) + ByteToInt(m_FrameBuffer[3]) * 256;
    }

    //Decrypt Frame
    public void DecryptFrame(WpnEnc.CWpnCryptKey pCryptKey)
    {
	//Decrypt Frame
	WpnEnc.DecryptTcp(m_FrameBuffer, m_iFrameSize, pCryptKey);

    	//Reset Position
	m_iItemPos = 0;
    }

    //Get Byte Item
    public byte GetByteItem()
    {
	return m_FrameBuffer[m_iItemPos++];
    }

    //Get Word Item
    public int GetWordItem()
    {
	final int iItem = ByteToInt(m_FrameBuffer[m_iItemPos]) + ByteToInt(m_FrameBuffer[m_iItemPos + 1]) * 256;
	m_iItemPos += 2;
	
	return iItem;
    }

    //Get Dword Item
    public long GetDwordItem()
    {
	final long lItem = (long)ByteToInt(m_FrameBuffer[m_iItemPos]) + (long)ByteToInt(m_FrameBuffer[m_iItemPos + 1]) * 256L + (long)ByteToInt(m_FrameBuffer[m_iItemPos + 2]) * 65536L + (long)ByteToInt(m_FrameBuffer[m_iItemPos + 3]) * 16777216L;
	m_iItemPos += 4;
	
	return lItem;
    }

    //Get String Item
    public String GetStringItem()
    {
	//Count String Length
	int iLength;
	for (iLength = 0; (m_FrameBuffer[m_iItemPos + iLength] != 0x00) && (m_iItemPos + iLength) < m_iFrameSize; iLength++);

	m_FrameBuffer[m_iItemPos + iLength] = 0x00;	// NULL Terminate
	m_iItemPos += (++iLength);			// Increment Position

	try
	{
	    return new String(m_FrameBuffer, m_iItemPos - iLength, iLength - 1);
	}

	catch(Exception e)
	{
	    return null;
	}
    }

    //Get String Item by Lenght
    public String GetStringItem(int Len)
    {
	//Count String Length
	int iLength;
	for (iLength = 0; (iLength < Len) && (m_iItemPos + iLength) < m_iFrameSize; iLength++);

	m_FrameBuffer[m_iItemPos + iLength] = 0x00;	//NULL Terminate
	m_iItemPos += (++iLength);			//Increment Position

	try
	{
	    return new String(m_FrameBuffer, m_iItemPos - iLength, iLength - 1);
	}

	catch(Exception e)
	{
	    return null;
	}
    }

    //Set Frame Type and Reset Frame Size
    public void SetFrameType(final short sType)
    {
	//Set Frame Type
	m_FrameBuffer[0] = (byte)(sType & 0x00FF);
	m_FrameBuffer[1] = (byte)((sType >> 8) & 0x00FF);

	//Reset Frame Size
	m_iFrameSize = 0;
    }

    //Set Byte Item
    public void SetByteItem(final byte bItem)
    {
	m_FrameBuffer[(m_iFrameSize++) + 4] = bItem;
    }

    //Set Word Item
    public void SetWordItem(final int iItem)
    {
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)(iItem & 0x000000FF);
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)((iItem >> 8) & 0x000000FF);
    }

    //Set Dword Item
    public void SetDwordItem(final long lItem)
    {
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)(lItem & 0x000000FF);
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)((lItem >> 8) & 0x000000FF);
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)((lItem >> 16) & 0x000000FF);
	m_FrameBuffer[(m_iFrameSize++) + 4] = (byte)((lItem >> 24) & 0x000000FF);
    }

    //Set String Item
    public void SetStringItem(String strItem)
    {
	try
	{
            int iLength = strItem.length();
            byte pString[] = strItem.getBytes();

            for (int i = 0; i < iLength; i++)
            {
                m_FrameBuffer[m_iFrameSize + i + 4] = pString[i];
            }

	    m_FrameBuffer[m_iFrameSize + iLength + 4] = 0x00;   //NULL Terminate
	    m_iFrameSize += (iLength + 1);			//Increment Frame Size
	}
        
	catch(Exception e)
	{
	    return;
	}
    }

    //Encrypt Frame
    public void EncryptFrame(WpnEnc.CWpnCryptKey pCryptKey)
    {
	// Set Frame Size
	m_FrameBuffer[2] = (byte)(m_iFrameSize & 0x000000FF);
	m_FrameBuffer[3] = (byte)((m_iFrameSize >> 8) & 0x000000FF);
	m_iFrameSize += 4;

	// Encrypt Frame
	WpnEnc.EncryptTcp(m_FrameBuffer, m_iFrameSize, pCryptKey);
    }
}

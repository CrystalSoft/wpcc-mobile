import javax.microedition.rms.*;
import java.util.Vector;
import java.util.Enumeration;

public class RMSManage
{
  private RecordStore rs = null;    
  private String RECORD_STORE; 
    
  public RMSManage(String Str)
  {
      RECORD_STORE = Str;
  }

    //APERTURA DEL RECORD
    public void openRMS()
    {
        try
        {
            rs = RecordStore.openRecordStore(RECORD_STORE, true);
        }
        
        catch (Exception e)
        {
            System.err.println("Open: " + e.toString());
        }
    }

    //LETTURA DEL RECORD
    public Vector readRMS()
    {
        Vector Tmp = new Vector();

        try
        {
            byte[] recData = new byte[1000];
            int dataLen;
            
            int Len = rs.getNumRecords();
            
            if (Len > 0)
            {
                for (int i = 0; i < Len; i++)
                {
                    try
                    {
                        dataLen = rs.getRecord(i+1, recData, 0);

                        Tmp.addElement(new String(recData, 0, dataLen));
                    }

                    catch (InvalidRecordIDException e)
                    {
                        
                    }
                }
            }
            
            return Tmp;
        }

        catch (Exception e)
        {
            System.err.println("Read: " + e.toString());
        }
        
        return Tmp;
    }

    //SCRITTURA DEL RECORD
    public void writeRMS(Vector Objs)
    {
        try
        {
            Enumeration vEnum = Objs.elements();

            for (int i = 0; vEnum.hasMoreElements() != false; i++)
            {
                byte[] record;
                record = ((String)vEnum.nextElement()).getBytes();

                try
                {
                    rs.setRecord(i+1, record, 0, record.length);
                }

                catch (InvalidRecordIDException e)
                {
                    rs.addRecord(record, 0, record.length);
                }
            }
        }

        catch (Exception e)
        {
            System.err.println("Write: " + e.toString());
        }
    }

    //CANCELLAZIONE DEFINITIVA DEL RECORD
    public void deleteRMS()
    {
        if (RecordStore.listRecordStores() != null)
        {
            try
            {
                RecordStore.deleteRecordStore(RECORD_STORE);
            }
            
            catch (Exception e)
            {
                System.err.println("Delete: " + e.toString());
            }
        }
    }

    //CHIUSURA DEL RECORD
    public void closeRMS()
    {
        try
        {
            rs.closeRecordStore();
        }
        
        catch (Exception e)
        {
            System.err.println("Close: " + e.toString());
        }
    }
}
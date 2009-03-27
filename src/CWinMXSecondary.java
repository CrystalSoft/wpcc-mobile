/*
 * CWinMXSecondary.java
 *
 * by Emulator
 *
 */

import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;

public class CWinMXSecondary
{
    private WPCC WPCCIstance;

    private String PeerCache = "";

    private long t;
    private boolean stop;

    private boolean FirstFetch;

    private long nodeIP;
    private int nodePort;

    private int Digits;
    private String NickName;
    private int Line;
    private int Files;

    private List InitDlg;
    private List ChannelList;
    private Form NetworksDlg;

    private String Lang[];

    private String Lines[] = new String [11];

    private SecondaryThread th;

    private StreamConnection Socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;

    private WpnEnc.CWpnCryptKey CryptKey = new WpnEnc.CWpnCryptKey();
    private WpnTcpFrame SendFrame = new WpnTcpFrame();
    private WpnTcpFrame RecvFrame = new WpnTcpFrame();

    private int state = 0;

    Vector Channels;

    public CWinMXSecondary(WPCC WPCCIstance, boolean FirstFetch, String NickName, int Line, int Files, List InitDlg, List ChannelList, Form NetworksDlg, String[] Lang)
    {
        th = null;

        stop = false;

        this.FirstFetch = FirstFetch;

        nodeIP = 0;
        nodePort = 7941;

        this.WPCCIstance = WPCCIstance;

        this.Digits = 45678;
        this.NickName = NickName;
        this.Line = Line;
        this.Files = Files;

        this.InitDlg = InitDlg;
        this.ChannelList = ChannelList;
        this.NetworksDlg = NetworksDlg;

        this.Lang = Lang;

        Lines[0] = "Unknown";
        Lines[1] = "14.4K";
        Lines[2] = "28.8K";
        Lines[3] = "33.6K";
        Lines[4] = "56K";
        Lines[5] = "64K ISDN";
        Lines[6] = "128K ISDN";
        Lines[7] = "Cable";
        Lines[8] = "DSL";
        Lines[9] = "T1";
        Lines[10] = "T3+";

        Channels = new Vector();
    }

    public class SecondaryThread extends Thread
    {
        public void run()
        {
            t = 0;

            long ip;
            int port;

            boolean writed = false;

            while (true)
            {
                try
                {
                    th.sleep(20);

                    if ((state == 3) && !writed)
                    {
                        ((StringItem)NetworksDlg.get(0)).setText(Lang[30]);

                        writed = true;
                    }

                    if (state != 3)
                    {
                        writed = false;

                        ((StringItem)NetworksDlg.get(0)).setText(Lang[31]);

                        ParentNodeInfo nodes[] = null;

                        while (nodes == null)
                        {
                            nodes = Utils.GetParentNode(PeerCache);

                            th.sleep(500);
                        }

                        int best = 0;

                        for (int i = 0; i < 10; i++)
                        {
                                if (nodes[i].freesec() > nodes[best].freesec())
                                {
                                        best = i;
                                }
                        }

                        if (best > 9)
                            best = 9;

                        ip = nodes[best].ip();
                        port = nodes[best].tcpport();

                        try
                        {
                            Socket = (StreamConnection)Connector.open("socket://" + Utils.IntToIP(ip) + ":" + port, Connector.READ_WRITE);
                            is = Socket.openDataInputStream();
                            os = Socket.openDataOutputStream();

                            byte str[] = new byte[10000];

                            if ((Utils.readit(is, str, 0, 1) != 1) || (str[0] != 0x31) || (state == 3))
                            {
                                Socket.close();

                                continue;
                            }

                            WpnEnc.CreateCryptKeyID((short)0x52, str);

                            os.write(str, 0, 16);
                            os.flush();

                            if ((Utils.readit(is, str, 0, 16) != 16) || (state == 3))
                            {
                                Socket.close();

                                continue;
                            }

                            short Key = WpnEnc.GetCryptKey(str, CryptKey);

                            if (Key == 0xFFFF)
                            {
                                Utils.mangle(str);
                            }

                            Key = WpnEnc.GetCryptKey(str, CryptKey);

                            if ((Key != 0x53) || (state == 3))
                            {
                                Socket.close();

                                continue;
                            }

                            state = 2;

                            SendNickNameInfo();

                            ((StringItem)NetworksDlg.get(0)).setText(Lang[32]);

                            nodeIP = ip;
                            nodePort = nodes[best].udpport();

                            if (FirstFetch)
                            {
                                RequireChannelList();

                                FirstFetch = false;
                            }

                            while (state == 2)
                            {
                                if (stop)
                                {
                                    th.sleep(10);
                                }

                                else
                                {
                                    if ((System.currentTimeMillis()/1000) > t+30)
                                    {
                                        stop = true;

                                        t = (System.currentTimeMillis()/1000);

                                        SendFrame.SetFrameType((short)0x0384);
                                        SendFrame.SetStringItem("9006 WPCC Mobile v" + WPCCIstance.GetVer() + " of Emulator available");
                                        SendFrame.EncryptFrame(CryptKey);

                                        os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                                        os.flush();

                                        stop = false;
                                    }
                                }

                                int Res = Utils.readit(is, RecvFrame.GetBuffer(), 0, 4);

                                if (Res != 4)
                                {
                                    System.err.println("CWinMXSecondary Invalid Header size... Break! Received: " + Res + ". To Receive: " + RecvFrame.GetFrameSize());

                                    break;
                                }

                                RecvFrame.DecryptHeader(CryptKey);

                                try
                                {
                                    Res = Utils.readit(is, RecvFrame.GetBuffer(), 0, RecvFrame.GetFrameSize());

                                    if (Res != RecvFrame.GetFrameSize())
                                    {
                                        System.err.println("CWinMXSecondary Invalid Data size... Break! Received: " + Res + ". To Receive: " + RecvFrame.GetFrameSize());

                                        break;
                                    }

                                    if (RecvFrame.GetFrameSize() == 0)
                                    {
                                        System.err.println("CWinMXSecondary Null size... Continue!");

                                        continue;
                                    }

                                    RecvFrame.DecryptFrame(CryptKey);
                                }

                                catch (ArrayIndexOutOfBoundsException e)
                                {
                                    System.err.println("CWinMXSecondary ThreadArrayOtBound Error: " + e.toString());
                                }

                                if (RecvFrame.GetFrameType() == (short)0x0460) //Login Ack
                                {
                                    Digits = RecvFrame.GetWordItem(); //Final Digits

                                    String digits = String.valueOf(Digits);

                                    if (digits.length() < 5)
                                    {
                                        int app = 5-digits.length();

                                        for (int i = 0; i < app; i++) digits = "0" + digits;
                                    }

                                    NickName = NickName.substring(0, NickName.length()-6) + "_" + digits;

                                    WPCCIstance.SetNickNameInfo(NickName, Integer.parseInt(digits), nodeIP, nodePort);
                                }

                                else if (RecvFrame.GetFrameType() == (short)0x138A) //Whois Answer
                                {
                                    String Hash = RecvFrame.GetStringItem();
                                    Hash = Hash.substring(6, 18);
                                    String UserName = RecvFrame.GetStringItem();
                                    int Line = RecvFrame.GetWordItem();
                                    long time = RecvFrame.GetDwordItem();
                                    int Files = RecvFrame.GetWordItem();
                                    String QueStatus = RecvFrame.GetStringItem();
                                    QueStatus = QueStatus.substring(5);
                                    String ElapsedTime = Utils.GetTime(time);

                                    String var = "Reported by WinMX Peer Network remote host\nTime/date of report: " + Utils.GetStandardTime() + "\nUser Name: " + UserName + "\nConnection Type: " + Lines[Line] + "\nFiles shared: " + Files + "\nElapsed time online: " + ElapsedTime + "\nTransfer Status: " + QueStatus;

                                    Utils.MessageBox(WPCCIstance.getDisplay(), UserName + " on WinMX " + Hash, var, Alert.FOREVER, AlertType.ALARM);
                                }

                                else if (RecvFrame.GetFrameType() == (short)0x138B) //Whois Failed 9 ; user offline , switched host
                                {
                                    String Hash = RecvFrame.GetStringItem();
                                    Hash = Hash.substring(6, 18);
                                    String UserName = RecvFrame.GetStringItem();

                                    Utils.MessageBox(WPCCIstance.getDisplay(), UserName + " on WinMX " + Hash, "Whois query failed.\nUser: " + UserName + "\n\nUser offline or switched hosts.", Alert.FOREVER, AlertType.ALARM);
                                }

                                else if (RecvFrame.GetFrameType() == (short)0x13ED) //PM Acknowledged
                                {
                                    String Hash = RecvFrame.GetStringItem();
                                    Hash = Hash.substring(6, 18);
                                    String UserName = RecvFrame.GetStringItem();
                                    String Case = RecvFrame.GetStringItem();


                                }

                                else if (RecvFrame.GetFrameType() == (short)0x13F6) //PM
                                {
                                    boolean Invalid = true;

                                    String Hash = "";
                                    String MessageID = RecvFrame.GetStringItem(30);

                                    if (MessageID.length() >= 30)
                                    {
                                        Hash = MessageID.substring(18, 30);
                                        MessageID = MessageID.substring(0, 12);

                                        Invalid = false;
                                    }

                                    else
                                    {
                                        System.err.println("CWinMXSecondary PM Invalid ID!");
                                    }

                                    String UserName = RecvFrame.GetStringItem();
                                    String Text = RecvFrame.GetStringItem();

                                    Utils.MessageBox(WPCCIstance.getDisplay(), UserName, Text, Alert.FOREVER, AlertType.ALARM);

                                    if (!Invalid)
                                    {
                                        String Case = "SENT";

                                        SendPMAck(UserName, Hash, MessageID, Case);
                                    }
                                }

                                else if (RecvFrame.GetFrameType() == (short)0x238E) //Roomlist entry
                                {
                                    String ChannelName;

                                    RecvFrame.GetDwordItem();
                                    RecvFrame.GetDwordItem();
                                    ChannelName = RecvFrame.GetStringItem();
                                    RecvFrame.GetStringItem();

                                    Channels.addElement(ChannelName);

                                    String Filt = WPCCIstance.getFilter().getString();
                                    Filt = Filt.toLowerCase();

                                    int Len = Filt.length();

                                    if (Len > 0)
                                    {
                                        int index = ChannelName.toLowerCase().indexOf(Filt);

                                        if (index >= 0)
                                        {
                                            ChannelList.append(ChannelName, null);
                                        }
                                    }

                                    else
                                    {
                                        ChannelList.append(ChannelName, null);
                                    }
                                }

                                else //Unknown Type
                                {
                                    System.err.println("CWinMXSecondary Unknown Packet: " + RecvFrame.GetFrameType() + " received.");
                                }
                            }

                            if (state != 3)
                            {
                                state = 0;
                            }
                        }

                        catch (IOException e)
                        {
                            System.err.println("CWinMXSecondary ThreadException Error: " + e.toString() + " Possible IP: " + Utils.IntToIP(ip));

                            if (state != 3)
                            {
                                state = 0;
                            }
                        }
                    }
                }

                catch (InterruptedException e)
                {
                    System.err.println("CWinMXSecondary ThreadInterrputed Error: " + e.toString());
                }

                catch (Exception e)
                {
                    System.err.println("CWinMXSecondary Thread Exception Error: " + e.toString());
                }
            }
        }
    }

    //state is 0 when is in Finding Connection
    //state is 1 when is Disconnected and attemping to Reconnect
    //state is 2 when is Connected
    //state is 3 when is Disconnected
    public void Connect()
    {
        NetworksDlg.removeCommand(WPCCIstance.getConnect());
        NetworksDlg.addCommand(WPCCIstance.getRefresh());
        NetworksDlg.addCommand(WPCCIstance.getDisconnect());

        state = 0;

        CloseSockets();

        if (th == null)
        {
            th = new SecondaryThread();
            th.start();
        }
    }

    public void Refresh()
    {
        Connect();
    }

    public void Disconnect()
    {
        NetworksDlg.removeCommand(WPCCIstance.getDisconnect());
        NetworksDlg.removeCommand(WPCCIstance.getRefresh());
        NetworksDlg.addCommand(WPCCIstance.getConnect());

        if ((state == 2) || (state == 0))
        {
            CloseSockets();
        }

        state = 3;
    }

    private void CloseSockets()
    {
        try
        {
            if (Socket != null)
            {
                Socket.close();
            }

            if (is != null)
            {
                is.close();
            }

            if (os != null)
            {
                os.close();
            }
        }

        catch (IOException e)
        {
             System.err.println("CWinMXSecondary Disconnect Error: " + e.toString());
        }
    }

    public void RequireChannelList()
    {
        Channels.removeAllElements();

        if (state == 2)
        {
            ChannelList.deleteAll();

            SendFrame.SetFrameType((short)0x238D);
            SendFrame.EncryptFrame(CryptKey);

            try
            {
                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();
            }

            catch (IOException e)
            {
                System.err.println("CWinMXSecondary RequireChannelList Error: " + e.toString());

                state = 0;
            }
        }
    }

    public boolean SendPM(String UserName, String Text, String Hash)
    {
        while (state == 2)
        {
            if (stop)
            {
                try
                {
                    th.sleep(10);

                    Utils.MessageBox(WPCCIstance.getDisplay(), "WPCC Mobile", Lang[76], Alert.FOREVER, AlertType.INFO);
                }

                catch (InterruptedException e)
                {
                    System.err.println("CWinMXSecondary SendPM Interrupted Error: " + e.toString());
                }
            }

            else
            {
                stop = true;

                SendFrame.SetFrameType((short)0x13EC);
                SendFrame.SetStringItem("WinMX " + Hash);
                SendFrame.SetStringItem(UserName);
                SendFrame.SetStringItem(Text);
                SendFrame.EncryptFrame(CryptKey);

                try
                {
                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient SendPM Error: " + e.toString());

                    stop = false;

                    return false;
                }

                stop = false;

                break;
            }
        }

        if (state == 2)
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    public boolean SendPMAck(String UserName, String Hash, String MessageID, String Case)
    {
        if (state == 2)
        {
            SendFrame.SetFrameType((short)0x144F);
            SendFrame.SetStringItem("WinMX " + Hash);
            SendFrame.SetStringItem(UserName);
            SendFrame.SetStringItem(MessageID + Case);
            SendFrame.EncryptFrame(CryptKey);

            try
            {
                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();
            }

            catch (IOException e)
            {
                System.err.println("CWinMXChatClient SendPMAck Error: " + e.toString());

                return false;
            }

            return true;
        }

        return false;
    }

    public boolean Whois(String UserName, String Hash)
    {
        while (state == 2)
        {
            if (stop)
            {
                try
                {
                    th.sleep(10);

                    Utils.MessageBox(WPCCIstance.getDisplay(), "WPCC Mobile", Lang[76], Alert.FOREVER, AlertType.INFO);
                }

                catch (InterruptedException e)
                {
                    System.err.println("CWinMXSecondary Whois Interrupted Error: " + e.toString());
                }
            }

            else
            {
                Utils.MessageBox(WPCCIstance.getDisplay(), UserName + " on WinMX " + Hash, Lang[77], Alert.FOREVER, AlertType.INFO);

                stop = true;

                SendFrame.SetFrameType((short)0x1389);
                SendFrame.SetStringItem("WinMX " + Hash);
                SendFrame.SetStringItem(UserName);
                SendFrame.EncryptFrame(CryptKey);

                try
                {
                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient Whois Error: " + e.toString());

                    stop = false;

                    Utils.MessageBox(WPCCIstance.getDisplay(), UserName + " on WinMX " + Hash, "Whois query failed.\nUser: " + UserName + "\n\nUser offline or switched hosts.", Alert.FOREVER, AlertType.INFO);

                    return false;
                }

                stop = false;

                break;
            }
        }

        if (state == 2)
        {
            return true;
        }

        else
        {
            Utils.MessageBox(WPCCIstance.getDisplay(), UserName + " on WinMX " + Hash, "Whois query failed.\nUser: " + UserName + "\n\nUser offline or switched hosts.", Alert.FOREVER, AlertType.ALARM);

            return false;
        }
    }

    public void SetPeerCache(String PeerCache)
    {
        this.PeerCache = PeerCache;
    }

    public void UpdateInfo(String NickName, int Line, int Files)
    {
        this.NickName = NickName;
        this.Line = Line;
        this.Files = Files;
    }

    private boolean SendNickNameInfo()
    {
        if (state == 2)
        {
            try
            {
                SendFrame.SetFrameType((short)0x03E9);
                SendFrame.SetStringItem(GetNickNameCut());
                SendFrame.SetWordItem(Line);
                SendFrame.SetWordItem(6600);

                SendFrame.EncryptFrame(CryptKey);

                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();
            }

            catch (IOException e)
            {
                System.err.println("CWinMXSecondary SendNickNameInfo Error: " + e.toString());

                state = 0;
            }
        }

        return true;
    }

    public void SendNickNameInfo(String NickName, int Line, int Files)
    {
        this.NickName = NickName;

        this.Line = Line;
        this.Files = Files;

        SendNickNameInfo();
    }

    public Vector GetChannelList()
    {
        return this.Channels;
    }

    private String GetNickNameCut()
    {
        return this.NickName.substring(0, this.NickName.length()-6);
    }

    public String GetNickName()
    {
        return this.NickName;
    }

    public long GetNodeIP()
    {
        return this.nodeIP;
    }

    public int GetNodePort()
    {
        return this.nodePort;
    }

    public int GetConnStatus()
    {
        return this.state;
    }
}

/*
 * CWinMXChatClient.java
 *
 * by Emulator
 *
 */

import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;

public class CWinMXChatClient
{
    WPCC WPCCIstance;

    private String Version;

    private String ChannelName;
    private String Hostname;
    private String NickName;
    private int Line;
    private int Files;
    private long nodeIP;
    private int nodePort;

    private Vector Login;

    private String PersonalMessage;
    private int Status;

    private Form ChatClient;
    private CWinMXEdit Log;
    private List UserList;

    private String Lang[];

    private String Lines[] = new String [11];

    private ChatThread th;
    private PingThread pt;

    private boolean LoginOK;

    private StreamConnection Socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;

    private WpnEnc.CWpnCryptKey CryptKey = new WpnEnc.CWpnCryptKey();
    private WpnTcpFrame SendFrame = new WpnTcpFrame();
    private WpnTcpFrame RecvFrame = new WpnTcpFrame();

    private int state = 0;

    private boolean Rejoining;
    private boolean AutoRejoin;
    private int RejoinRetry;
    private int RejoinRetryed;

    private boolean v353 = false;
    private boolean compEA = false;

    private String AutoMessage;

    private boolean First;

    private boolean ShowStatus = true;
    private boolean ShowVersion = true;

    public CWinMXChatClient(WPCC WPCCIstance, String Version, String ChannelName, String Hostname, String NickName, int Line, int Files, long nodeIP, int nodePort, Form ChannelDlg, CWinMXEdit Log, List UserList, String Lang[], Vector Login, String PersonalMessage, int Status)
    {
        this.WPCCIstance = WPCCIstance;

        LoginOK = false;

        th = null;
        pt = null;

        this.Version = Version;

        this.ChannelName = ChannelName;
        this.Hostname = Hostname;
        this.NickName = NickName;
        this.Line = Line;
        this.Files = Files;
        this.nodeIP = nodeIP;
        this.nodePort = nodePort;

        this.PersonalMessage = PersonalMessage;
        this.Status = Status;

        this.Login = Login;

        this.ChatClient = ChannelDlg;
        this.Log = Log;
        this.UserList = UserList;

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

        Rejoining = false;

        AutoRejoin = false;
        RejoinRetry = 3;
        RejoinRetryed = 1;

        AutoMessage = "";

        First = false;

        th = new ChatThread();
        th.start();

        pt = new PingThread();
        pt.start();
    }

    //state is 0 when is Disconnected
    //state is 1 when is Connected
    //state is 2 when there is a Protocol Error
    public class ChatThread extends Thread
    {
        public void run()
        {
            try
            {
                LoginOK = false;

                String RoomName = new String("");

                String IP;

                if (Hostname.length() == 0)
                {
                    ChatClient.setTitle(ChannelName);

                    RoomName += "" + Utils.HexToInt(ChannelName.substring(ChannelName.length() - 6,  ChannelName.length() - 4 )) + ".";
                    RoomName += "" + Utils.HexToInt(ChannelName.substring(ChannelName.length() - 8,  ChannelName.length() - 6 )) + ".";
                    RoomName += "" + Utils.HexToInt(ChannelName.substring(ChannelName.length() - 10, ChannelName.length() - 8 )) + ".";
                    RoomName += "" + Utils.HexToInt(ChannelName.substring(ChannelName.length() - 12, ChannelName.length() - 10));

                    IP = RoomName + ":" + Utils.HexToInt(ChannelName.substring(ChannelName.length() - 4,  ChannelName.length() - 0 ));
                }

                else
                {
                    IP = Hostname;

                    ChannelName = Hostname;
                }

                ChatClient.setTitle(ChannelName);

                First = true;
                UserList.deleteAll();

                Socket = (StreamConnection)Connector.open("socket://" + IP, Connector.READ_WRITE);
                is = Socket.openDataInputStream();
                os = Socket.openDataOutputStream();

                state = 1;

                byte str[] = new byte[10000];

                if ((Utils.readit(is, str, 0, 1) != 1) || (str[0] != 0x31))
                {
                    state = 2;
                }

                WpnEnc.CreateCryptKeyID((short)0x57, str);

                os.write(str, 0, 16);
                os.flush();

                if (Utils.readit(is, str, 0, 16) != 16)
                {
                    state = 2;
                }

                WpnEnc.GetCryptKey(str, CryptKey);

                //WinMX v3.53 Compatibility
                SendFrame.SetFrameType((short)0x13ED);
                SendFrame.SetWordItem(0x31);
                SendFrame.EncryptFrame(CryptKey);

                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();

                //Login Information
                SendFrame.SetFrameType((short)0x0064);
                SendFrame.SetStringItem(ChannelName);
                SendFrame.SetWordItem(Line);
                SendFrame.SetDwordItem(nodeIP);
                SendFrame.SetWordItem(nodePort);
                SendFrame.SetDwordItem(Files);
                SendFrame.SetStringItem(NickName);
                SendFrame.EncryptFrame(CryptKey);

                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();

                //Version Information
                if (ShowVersion)
                {
                    SendFrame.SetFrameType((short)0x9905);
                    SendFrame.SetStringItem("WPCC Mobile");
                    SendFrame.SetStringItem("v" + Version + " by Emulator");
                    SendFrame.EncryptFrame(CryptKey);

                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                while (state == 1)
                {
                    int Res = Utils.readit(is, RecvFrame.GetBuffer(), 0, 4);

                    if (Res != 4)
                    {
                        System.err.println("CWinMXChatClient Invalid Header size... Break! Received: " + Res + ". To Receive: " + RecvFrame.GetFrameSize());

                        state = 2;

                        break;
                    }

                    RecvFrame.DecryptHeader(CryptKey);

                    try
                    {
                        Res = Utils.readit(is, RecvFrame.GetBuffer(), 0, RecvFrame.GetFrameSize());

                        if (Res != RecvFrame.GetFrameSize())
                        {
                            System.err.println("CWinMXChatClient Invalid Data size... Break! Received: " + Res + ". To Receive: " + RecvFrame.GetFrameSize());

                            state = 2;

                            break;
                        }

                        else if (RecvFrame.GetFrameSize() == 0)
                        {
                            System.err.println("CWinMXChatClient Null size... Continue!");

                            continue;
                        }

                        RecvFrame.DecryptFrame(CryptKey);
                    }

                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        System.err.println("CWinMXChatClient ThreadArrayOtBound Error: " + e.toString());
                    }

                    if (RecvFrame.GetFrameType() == (short)0x0066) //Login Granted
                    {
                        LoginOK = true;
                    }

                    if (LoginOK != true)
                    {
                        continue;
                    }

                    if (RecvFrame.GetFrameType() == (short)0x0066) //Login Granted
                    {
                        //Do nothing
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0068) //WinMX v3.53 Compatibility
                    {
                        v353 = true;
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x006E) //User Entered
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        Log.appendString(Log.GetColor(4) + Nick + " (" + Lines[line] + " " + files + " " + Lang[6]);
                        AddUser(Nick, line, files, 0, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x006F) //User List
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        AddUser(Nick, line, files, 0, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0070) //User Rename
                    {
                        String OldNick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip2 = RecvFrame.GetDwordItem();
                        int nodeport2 = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String OldHash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);
                        String NewHash = Utils.IntToHex((int)nodeip2) + Utils.IntToHex(nodeport2);

                        RenameUser(OldNick, OldHash, Nick, NewHash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0071) //User Entered (No IP)
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        Log.appendString(Log.GetColor(4) + Nick + " (" + Lines[line] + " " + files + " " + Lang[6]);
                        AddUser(Nick, line, files, 0, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0072) //User List
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        AddUser(Nick, line, files, 0, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0073) //User Exited
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        Log.appendString(Log.GetColor(5) + Nick + " " + Lang[7]);

                        RemoveUser(Nick, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0074) //User Rename
                    {
                        String OldNick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip2 = RecvFrame.GetDwordItem();
                        int nodeport2 = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();

                        String OldHash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);
                        String NewHash = Utils.IntToHex((int)nodeip2) + Utils.IntToHex(nodeport2);

                        RenameUser(OldNick, OldHash, Nick, NewHash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0075) //User Entered (IP view)
                    {
                        String Nick = RecvFrame.GetStringItem();
                        long nodeip = RecvFrame.GetDwordItem();
                        int nodeport = RecvFrame.GetWordItem();
                        int line = RecvFrame.GetWordItem();
                        long files = RecvFrame.GetDwordItem();
                        RecvFrame.GetByteItem();
                        long ip = RecvFrame.GetDwordItem();

                        String Hash = Utils.IntToHex((int)nodeip) + Utils.IntToHex(nodeport);

                        Log.appendString(Log.GetColor(4) + Nick + " (" + Utils.IntToIP(ip) + ") (" + Lines[line] + " " + files + " " + Lang[6]);

                        AddUser(Nick, line, files, ip, Hash);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0078) //Motd
                    {
                        Log.appendString(Log.GetColor(6) + RecvFrame.GetStringItem());
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x00C9) //Normal Text
                    {
                        String Nick = RecvFrame.GetStringItem();
                        String Text = RecvFrame.GetStringItem();
                        byte Rank = RecvFrame.GetByteItem();

                        String Colour = Log.GetColor(2);

                        if (Rank == 1)
                        {
                            Colour = Log.GetColor(8);
                        }

                        Log.appendString(Colour + "<" + Log.GetColor(2) + Nick + Colour + "> " + Log.GetColor(1) + Text);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x00CB) //Action
                    {
                        Log.appendString(Log.GetColor(3) + RecvFrame.GetStringItem() + " " + RecvFrame.GetStringItem());
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x00D2) //Colour Text
                    {
                        Log.appendString(RecvFrame.GetStringItem());
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x00D3) //Echo Command
                    {
                        Log.appendString(Log.GetColor(8) + "> " + Log.GetColor(1) + RecvFrame.GetStringItem());
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x012C) //Topic
                    {
                        String Topic = RecvFrame.GetStringItem();

                        int off = 20;

                        if (Topic.length() > off)
                        {
                            Topic = Topic.substring(0, off-3);
                            Topic += "...";
                        }

                        else
                        {
                            off = Topic.length();
                        }

                        Log.setLabel(Lang[50] + ": " + Topic.substring(0, off));
                        Log.appendString(Log.GetColor(7) + Lang[50] + ": " + Topic);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x012D) //Channel Rename
                    {
                        String Chan = RecvFrame.GetStringItem();

                        Log.setLabel(Chan);
                        Log.appendString(Log.GetColor(5) + Lang[8] + " " + Chan);

                        WPCCIstance.UpdateName(ChannelName, Chan);

                        ChannelName = Chan;
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x0190) //Channel Redirected
                    {
                        String Chan = RecvFrame.GetStringItem();

                        WPCCIstance.UpdateName(ChannelName, Chan);

                        ChannelName = Chan;
                        Hostname = "";

                        Log.appendString(Log.GetColor(5) + Lang[9] + " " + Chan);

                        Disconnect();

                        run();

                        return;
                    }

                    ////0x34XX Packets////
                    else if (RecvFrame.GetFrameType() == (short)0x3400) //EA Compatibility
                    {
                        SendFrame.SetFrameType((short)0x3470);
                        SendFrame.SetDwordItem(1);
                        SendFrame.EncryptFrame(CryptKey);

                        os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                        os.flush();

                        compEA = true;

                        ChangeMessage(PersonalMessage);
                        ChangeState(Status);
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3401) //Nick typing
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3402) //Nick stopped type
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3403) //OnJoin Nick typing [Ignored]
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3411) //User Status
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3412) //OnJoin Users state [Ignored]
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x3421) //User Personal Message
                    {
                        
                    }
                    //////////////////////

                    else if (RecvFrame.GetFrameType() == (short)0x9904) //Colour Codes
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0x9905) //Server Version
                    {
                        
                    }

                    else if (RecvFrame.GetFrameType() == (short)0xFDE8) //PING
                    {
                        
                    }

                    else //Unknown Type
                    {
                        System.err.println("CWinMXChatClient Unknown Packet: " + RecvFrame.GetFrameType() + " received.");
                    }

                    if (First)
                    {
                        if (AutoMessage.length() > 0)
                        {
                            SendText(AutoMessage);
                        }

                        int Len = Login.size();

                        if (Login.size() > 0)
                        {
                            String login = "", Tmp, Tmp2 = ChannelName, Tmp3;

                            Enumeration vEnum = Login.elements();

                            for (int i = 0; i < Len; i++)
                            {
                                Tmp3 = (String)vEnum.nextElement();
                                Tmp = Tmp3;

                                int index = Tmp.lastIndexOf('_');

                                if (index >= 0)
                                {
                                    Tmp = Tmp.substring(index+1);
                                }

                                Tmp = Tmp.substring(0, Tmp.lastIndexOf(' '));

                                index = Tmp2.lastIndexOf('_');

                                if (index >= 0)
                                {
                                    Tmp2 = Tmp2.substring(index+1);
                                }

                                if (Tmp2.equalsIgnoreCase(Tmp))
                                {
                                    login = Tmp3.substring(Tmp3.lastIndexOf(' ')+1);

                                    SendText("/login " + login);

                                    break;
                                }
                            }
                        }

                        First = false;
                    }
                }
            }

            catch (Exception e)
            {
                System.err.println("CWinMXChatClient Thread Exception Error: " + e.toString());
            }

            Disconnect();

            if (Rejoining)
            {
                Rejoining = false;

                run();
            }

            else
            {
                Log.appendString(Log.GetColor(5) + "You left the channel.");

                if (AutoRejoin)
                {
                    if (RejoinRetryed <= RejoinRetry)
                    {
                        Log.appendString(Log.GetColor(5) + "Rejoining Channel... #" + RejoinRetryed + "/" + RejoinRetry);
                        RejoinRetryed++;

                        run();
                    }
                }
            }
        }
    }

    private void AddUser(String NickName, int Line, long Files, long IP, String Hash)
    {
        Hash = Hash.toLowerCase();

        UserList.append(NickName + " " + Utils.IntToIP(IP) + " " + Lines[Line] + " " + Files + " files. Hash: " + Hash, null);
    }

    private void RenameUser(String OldNickName, String OldHash, String NewNickName, String NewHash)
    {
        OldHash = OldHash.toLowerCase();
        NewHash = NewHash.toLowerCase();

        for (int i = 0; i < UserList.size(); i++)
        {
            if (OldNickName.equalsIgnoreCase(UserList.getString(i).substring(0, UserList.getString(i).indexOf(" ", 0))))
            {
                String tmp = UserList.getString(i);
                tmp = Utils.replaceAll(tmp, OldNickName, NewNickName);
                tmp = Utils.replaceAll(tmp, OldHash, NewHash);

                UserList.set(i, tmp, null);

                break;
            }
        }
    }

    private void RemoveUser(String NickName, String Hash)
    {
        Hash = Hash.toLowerCase();

        for (int i = 0; i < UserList.size(); i++)
        {
            if (NickName.equalsIgnoreCase(UserList.getString(i).substring(0, UserList.getString(i).indexOf(" ", 0))))
            {
                UserList.delete(i);

                break;
            }
        }
    }

    public void SendText(String Text)
    {
        if (Text.length() > 0)
        {
            if (v353)
            {
                SendFrame.SetFrameType((short)0x1450);
            }

            else
            {
                SendFrame.SetFrameType((short)0x00C8);
            }

            SendFrame.SetStringItem(Text);

            if (state == 1)
            {
                SendFrame.EncryptFrame(CryptKey);

                try
                {
                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient SendText Error: " + e.toString());
                }
            }
        }
    }

    public void UpdateInfo(String NickName, int Line, int Files, long nodeIP, int nodePort, String PersonalMessage, int Status)
    {
        this.NickName = NickName;
        this.Line = Line;
        this.Files = Files;
        this.nodeIP = nodeIP;
        this.nodePort = nodePort;

        this.PersonalMessage = PersonalMessage;
        this.Status = Status;

        ChangedNick();

        ChangeState(this.Status);
        ChangeMessage(this.PersonalMessage);
    }

    public String GetChannelName()
    {
        if (Hostname.length() > 0)
        {
            return Hostname;
        }

        else
        {
            return ChannelName;
        }
    }

    private void ChangedNick()
    {
        if (state == 1)
        {
            SendFrame.SetFrameType((short)0x0065);
            SendFrame.SetWordItem(Line);
            SendFrame.SetDwordItem(nodeIP);
            SendFrame.SetWordItem(nodePort);
            SendFrame.SetDwordItem(Files);
            SendFrame.SetStringItem(NickName);
            SendFrame.EncryptFrame(CryptKey);

            try
            {
                os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                os.flush();
            }

            catch (IOException e)
            {
                System.err.println("CWinMXChatClient ChangedNick Error: " + e.toString());

                state = 0;
            }
        }
    }

    private void ChangeState(int State)
    {
        if (compEA && ShowStatus)
        {
            if (state == 1)
            {
                SendFrame.SetFrameType((short)0x3481);
                SendFrame.SetWordItem(State);
                SendFrame.EncryptFrame(CryptKey);

                try
                {
                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient ChangeState Error: " + e.toString());

                    state = 0;
                }
            }
        }
    }

    private void ChangeMessage(String Message)
    {
        if (compEA && ShowStatus)
        {
            if (state == 1)
            {
                SendFrame.SetFrameType((short)0x3491);
                SendFrame.SetStringItem(Message);
                SendFrame.EncryptFrame(CryptKey);

                try
                {
                    os.write(SendFrame.GetBuffer(), 0, SendFrame.GetFrameSize());
                    os.flush();
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient ChangeMessage Error: " + e.toString());

                    state = 0;
                }
            }
        }
    }

    public void Rejoin()
    {
        Rejoining = true;

        Disconnect();

        Log.appendString(Log.GetColor(5) + "Rejoining Channel...");

        if (!th.isAlive())
        {
            th = new ChatThread();
            th.start();
        }
    }

    public void Disconnect()
    {
        try
        {
            state = 0;

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
            System.err.println("CWinMXChatClient Disconnect Error: " + e.toString());
        }
    }

    public void Disconnect(boolean def)
    {
        if (def)
        {
            AutoRejoin = false;
        }

        Disconnect();
    }

    public void SetShowStatus(boolean Show)
    {
        this.ShowStatus = Show;
    }

    public void SetAutoRejoin(boolean Rejoin)
    {
        this.AutoRejoin = Rejoin;
    }

    public void SetRejoinRetry(int Times)
    {
        this.RejoinRetry = Times;
    }

    public void SetAutoMessage(String AutoMessage)
    {
        this.AutoMessage = AutoMessage;
    }

    public void SetShowVersion(boolean Show)
    {
        this.ShowVersion = Show;
    }

    public void SetLogin(Vector Login)
    {
        this.Login = Login;
    }

    public int GetConnStatus()
    {
        return this.state;
    }

    private class PingThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(40000);
                }

                catch (InterruptedException e)
                {
                    System.err.println("CWinMXChatClient Ping Error 1: " + e.toString());
                }

                WpnTcpFrame PingFrame = new WpnTcpFrame();
                PingFrame.SetFrameType((short)0xFDE8);
                PingFrame.SetStringItem("1");
                PingFrame.EncryptFrame(CryptKey);

                try
                {
                    if (state == 1)
                    {
                        os.write(PingFrame.GetBuffer(), 0, PingFrame.GetFrameSize());
                        os.flush();
                    }
                }

                catch (IOException e)
                {
                    System.err.println("CWinMXChatClient Ping Error 2: " + e.toString());

                    state = 0;
                }
            }
        }
    }
}
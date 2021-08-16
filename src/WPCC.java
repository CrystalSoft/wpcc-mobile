/*
* @author Emulator
*/

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class WPCC extends MIDlet implements CommandListener
{
    private boolean midletPaused = false;

    RMSManage WPCCSettings;
    RMSManage FavChannels;
    RMSManage AutoChannels;
    RMSManage LoginChannels;

    String Version = "0.2";

    private int MaxChat = 8; //Max Chat at the same time

    private int Width;
    private int Height;

    private int MyLang;
    private String Lang[] = new String[500];
    public String[] LangList = {"English", "Italiano", "Deutsch", "Français", "Norwegian", "Espanol"};

    private CWinMXSecondary SecondaryConnection;
    private CWinMXChatClient ChatClient[] = new CWinMXChatClient[MaxChat];

    private CAutoUpdater AutoUpdater;

    String Lines[] = new String[11];
    String States[] = new String[7];

    private String PeerCache;

    private boolean FirstFetch;

    private int Digits;
    private String NickName;
    private int Line;
    private int File;
    private long nodeIP;
    private int nodePort;

    private int PersState;
    private String PersonalMessage;

    private boolean AutoConnect;

    private int RejoinRetry;

    private String AutoMex;

    private boolean ShowStatus;
    private boolean ShowVer;
    private boolean EnableColours;
    private boolean DarkMode;

    private Form ChannelDlgs[] = new Form[MaxChat];
    private List UserLists[] = new List[MaxChat];

    private Command Refresh;

    private Command Sends;
    private Command Sends2;
    private Command Sends3;
    private Command Rejoin;
    private Command RejoinAll;
    private Command JoinAll;
    private Command AddFavorites;
    private Command AddAutoJoin;
    private Command Clear;
    private Command GoToUserLists;
    private Command ExitFromRoom;
    private Command GoToMain;
    private Command GoToNet;
    private Command Backs;
    private Command ExitWPCC;

    private Command SendPM;
    private Command Whois;
    private Command Levels;
    private Command Kicks;
    private Command Ban;
    private Command KickBan;
    private Command UserLevel;
    private Command ListBack;

    private String PMUser;
    private String PMHash;

    private int ChType;

    Image Icons[] = new Image[4];

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private java.util.Hashtable __previousDisplayables = new java.util.Hashtable();
    private Form ChannelListFilter;
    private TextField Filter;
    private Form NetworksDlg;
    private StringItem ConnStatus;
    private ChoiceGroup Status;
    private TextField PersMex;
    private TextField ScreenName;
    private TextField SharedFiles;
    private ChoiceGroup ConnType;
    private Form SettingsDlg;
    private TextField AutoRejoin;
    private ChoiceGroup Language;
    private ChoiceGroup AutoFetch;
    private ChoiceGroup ShowVersion;
    private ChoiceGroup ShowState;
    private TextField AutoMessage;
    private ChoiceGroup AutoConn;
    private TextField DefaultPeerCache;
    private ChoiceGroup EnableCol;
    private ChoiceGroup ColorMode;
    private List InitDlg;
    private List ChannelList;
    private TextBox PMTextBox;
    private List FavoritesList;
    private List AutoJoinList;
    private List AutoLoginsList;
    private Form GenericAddChannel;
    private TextField GenericChannel;
    private TextField GenericLogin;
    private Command SettingsBack;
    private Command NetworksBack;
    private Command ChannelBack;
    private Command Join;
    private Command Go;
    private Command ExitApp;
    private Command ChanneListBack;
    private Command GoToChannels;
    private Command JoinChannel;
    private Command Disconnect;
    private Command Connect;
    private Command Apply;
    private Command RefreshList;
    private Command ChGoToNetworks;
    private Command ChListGoToMainPage;
    private Command ChListGoMain;
    private Command ChListGoToMain;
    private Command ChListGoToNetworks;
    private Command SettApply;
    private Command PMCancel;
    private Command PMSend;
    private Command AutoJoinCh;
    private Command FavoriteCh;
    private Command AutoLoginListCh;
    private Command AutoJoinListCh;
    private Command FavoritesListCh;
    private Command AutoLoginCh;
    private Command FavAddChannel;
    private Command FavRemChannel;
    private Command AutoJoinBack;
    private Command FavBack;
    private Command LoginsRemChannel;
    private Command FavJoin;
    private Command AutoAddChannel;
    private Command AutoRemChannel;
    private Command LoginsBack;
    private Command LoginsAddChannel;
    private Command ChAddToAutoJoin;
    private Command ChAddToFavorites;
    private Command About;
    private Command GenericOK;
    private Command GenericBack;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The WPCC constructor.
     */
    public WPCC()
    {
        WPCCSettings = new RMSManage("WPCCMobile-Cfg");
        FavChannels = new RMSManage("WPCCMobile-FavCh");
        AutoChannels = new RMSManage("WPCCMobile-AutoCh");
        LoginChannels = new RMSManage("WPCCMobile-LogCh");

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

        MyLang = 0;

        Digits = 45678;
        NickName  = "Nick123_" + Digits;
        Line = 8;
        File = 34;
        nodeIP = 0;
        nodePort = 7941;

        PersonalMessage = "";

        PeerCache = "";

        AutoConnect = false;
        FirstFetch = true;

        RejoinRetry = 0;

        AutoMex = "";

        ShowStatus = true;
        ShowVer = true;
        EnableColours = true;
        DarkMode = true;

        PersState = 0;

        PMUser = "";
        PMHash = "";

        ChType = -1;

        for (int i = 0; i < MaxChat; i++)
        {
            ChatClient[i] = null;

            ChannelDlgs[i] = null;
            UserLists[i] = null;
        }

        Sends = null;
        Sends2 = null;
        Sends3 = null;
        Rejoin = null;
        RejoinAll = null;
        JoinAll = null;
        AddFavorites = null;
        AddAutoJoin = null;
        Clear = null;
        GoToUserLists = null;
        ExitFromRoom = null;
        GoToMain = null;
        GoToNet = null;
        Backs = null;
        ExitWPCC = null;

        SendPM = null;
        Whois = null;
        Levels = null;
        Kicks = null;
        Ban = null;
        KickBan = null;
        UserLevel = null;
        ListBack = null;

        AutoUpdater = null;

        Icons[0] = Utils.loadImage("/Networks.png");
        Icons[1] = Utils.loadImage("/ChatList.png");
        Icons[2] = Utils.loadImage("/Settings.png");
        Icons[3] = Utils.loadImage("/Chat.png");

        LoadSettings();

        switch (MyLang)
        {
            case 0:
            {
                English Lng = new English();
                Lang = Lng.Lang;

                break;
            }

            case 1:
            {
                Italian Lng = new Italian();
                Lang = Lng.Lang;

                break;
            }

            case 2:
            {
                Dutch Lng = new Dutch();
                Lang = Lng.Lang;

                break;
            }

            case 3:
            {
                Francais Lng = new Francais();
                Lang = Lng.Lang;

                break;
            }

            case 4:
            {
                Norwegian Lng = new Norwegian();
                Lang = Lng.Lang;

                break;
            }

            case 5:
            {
                Spanish Lng = new Spanish();
                Lang = Lng.Lang;

                break;
            }

            default:
            {
                break;
            }
        }

        for (int i = 0; i < 7; i++)
        {
            States[i] = Lang[20+i];
        }

        if (PersonalMessage.length() == 0)
        {
            PersonalMessage = Lang[27];
        }

        SecondaryConnection = new CWinMXSecondary(this, FirstFetch, NickName, Line, File, getInitDlg(), getChannelList(), getNetworksDlg(), Lang);
        SecondaryConnection.SetPeerCache(PeerCache);

        if (AutoConnect == true)
        {
            SecondaryConnection.Connect();
        }

        //Disbled, J2ME doesn't permit socket connection to port 80.
        /*AutoUpdater = new CAutoUpdater(this, Lang);
        AutoUpdater.CheckNow();*/
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    /**
     * Switches a display to previous displayable of the current displayable.
     * The <code>display</code> instance is obtain from the <code>getDisplay</code> method.
     */
    private void switchToPreviousDisplayable() {
        Displayable __currentDisplayable = getDisplay().getCurrent();
        if (__currentDisplayable != null) {
            Displayable __nextDisplayable = (Displayable) __previousDisplayables.get(__currentDisplayable);
            if (__nextDisplayable != null) {
                switchDisplayable(null, __nextDisplayable);
            }
        }
    }
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // 
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // 
        switchDisplayable(null, getInitDlg());//GEN-LINE:|3-startMIDlet|1|3-postAction

        Joiner J = new Joiner();
        J.start();
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        Displayable __currentDisplayable = display.getCurrent();
        if (__currentDisplayable != null  &&  nextDisplayable != null) {
            __previousDisplayables.put(nextDisplayable, __currentDisplayable);
        }
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChannelListFilter ">//GEN-BEGIN:|15-getter|0|15-preInit
    /**
     * Returns an initiliazed instance of ChannelListFilter component.
     * @return the initialized component instance
     */
    public Form getChannelListFilter() {
        if (ChannelListFilter == null) {//GEN-END:|15-getter|0|15-preInit
            // write pre-init user code here
            ChannelListFilter = new Form(Lang[35], new Item[] { getFilter() });//GEN-BEGIN:|15-getter|1|15-postInit
            ChannelListFilter.addCommand(getJoin());
            ChannelListFilter.addCommand(getGoToChannels());
            ChannelListFilter.addCommand(getFavoritesListCh());
            ChannelListFilter.addCommand(getAutoJoinListCh());
            ChannelListFilter.addCommand(getAutoLoginListCh());
            ChannelListFilter.addCommand(getChGoToNetworks());
            ChannelListFilter.addCommand(getChannelBack());
            ChannelListFilter.setCommandListener(this);//GEN-END:|15-getter|1|15-postInit
            // write post-init user code here
        }//GEN-BEGIN:|15-getter|2|
        return ChannelListFilter;
    }
    //</editor-fold>//GEN-END:|15-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: NetworksDlg ">//GEN-BEGIN:|17-getter|0|17-preInit
    /**
     * Returns an initiliazed instance of NetworksDlg component.
     * @return the initialized component instance
     */
    public Form getNetworksDlg() {
        if (NetworksDlg == null) {//GEN-END:|17-getter|0|17-preInit
            // write pre-init user code here
            NetworksDlg = new Form(Lang[34], new Item[] { getConnStatus(), getScreenName(), getSharedFiles(), getConnType(), getStatus(), getPersMex() });//GEN-BEGIN:|17-getter|1|17-postInit
            NetworksDlg.addCommand(getConnect());
            NetworksDlg.addCommand(getDisconnect());
            NetworksDlg.addCommand(getApply());
            NetworksDlg.addCommand(getNetworksBack());
            NetworksDlg.setCommandListener(this);//GEN-END:|17-getter|1|17-postInit
            NetworksDlg.removeCommand(getDisconnect());
        }//GEN-BEGIN:|17-getter|2|
        return NetworksDlg;
    }
    //</editor-fold>//GEN-END:|17-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: SettingsDlg ">//GEN-BEGIN:|20-getter|0|20-preInit
    /**
     * Returns an initiliazed instance of SettingsDlg component.
     * @return the initialized component instance
     */
    public Form getSettingsDlg() {
        if (SettingsDlg == null) {//GEN-END:|20-getter|0|20-preInit
            // write pre-init user code here
            SettingsDlg = new Form(Lang[36], new Item[] { getLanguage(), getAutoRejoin(), getAutoMessage(), getAutoConn(), getDefaultPeerCache(), getAutoFetch(), getShowVersion(), getShowState(), getEnableCol(), getColorMode() });//GEN-BEGIN:|20-getter|1|20-postInit
            SettingsDlg.addCommand(getSettApply());
            SettingsDlg.addCommand(getSettingsBack());
            SettingsDlg.setCommandListener(this);//GEN-END:|20-getter|1|20-postInit
            // write post-init user code here
        }//GEN-BEGIN:|20-getter|2|
        return SettingsDlg;
    }
    //</editor-fold>//GEN-END:|20-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        if (displayable == AutoJoinList) {//GEN-BEGIN:|7-commandAction|1|145-preAction
            if (command == AutoAddChannel) {//GEN-END:|7-commandAction|1|145-preAction
                getGenericChannel().setString("");
                getGenericLogin().setString("");

                if (getGenericAddChannel().size() > 1)
                {
                    getGenericAddChannel().delete(1);
                }

                ChType = 1;

                switchDisplayable(null, getGenericAddChannel());//GEN-LINE:|7-commandAction|2|145-postAction
                // write post-action user code here
            } else if (command == AutoJoinBack) {//GEN-LINE:|7-commandAction|3|143-preAction
                // write pre-action user code here
                switchDisplayable(null, getChannelListFilter());//GEN-LINE:|7-commandAction|4|143-postAction
                // write post-action user code here
            } else if (command == AutoRemChannel) {//GEN-LINE:|7-commandAction|5|147-preAction
                if (getAutoJoinList().size() > 0)
                    RemoveFromAutoJoin(getAutoJoinList().getString(getAutoJoinList().getSelectedIndex()));
//GEN-LINE:|7-commandAction|6|147-postAction
                // write post-action user code here
            } else if (command == List.SELECT_COMMAND) {//GEN-LINE:|7-commandAction|7|109-preAction
                if (getAutoJoinList().size() > 0)
                    JoinChannel(getAutoJoinList().getString(getAutoJoinList().getSelectedIndex()), true);
                AutoJoinListAction();//GEN-LINE:|7-commandAction|8|109-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|115-preAction
        } else if (displayable == AutoLoginsList) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|9|115-preAction
                // 
                AutoLoginsListAction();//GEN-LINE:|7-commandAction|10|115-postAction
                // write post-action user code here
            } else if (command == LoginsAddChannel) {//GEN-LINE:|7-commandAction|11|151-preAction
                getGenericChannel().setString("");
                getGenericLogin().setString("");

                if (getGenericAddChannel().size() < 2)
                {
                    getGenericAddChannel().append(getGenericLogin());
                }

                ChType = 2;

                switchDisplayable(null, getGenericAddChannel());//GEN-LINE:|7-commandAction|12|151-postAction
                // write post-action user code here
            } else if (command == LoginsBack) {//GEN-LINE:|7-commandAction|13|149-preAction
                // write pre-action user code here
                switchDisplayable(null, getChannelListFilter());//GEN-LINE:|7-commandAction|14|149-postAction
                // write post-action user code here
            } else if (command == LoginsRemChannel) {//GEN-LINE:|7-commandAction|15|153-preAction
                if (getAutoLoginsList().size() > 0)
                    RemoveFromAutoLogin(getAutoLoginsList().getString(getAutoLoginsList().getSelectedIndex()));
//GEN-LINE:|7-commandAction|16|153-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|172-preAction
        } else if (displayable == ChannelList) {
            if (command == ChAddToAutoJoin) {//GEN-END:|7-commandAction|17|172-preAction
                if (getChannelList().size() > 0)
                    AddToAutoJoin(getChannelList().getString(getChannelList().getSelectedIndex()));
//GEN-LINE:|7-commandAction|18|172-postAction
                // write post-action user code here
            } else if (command == ChAddToFavorites) {//GEN-LINE:|7-commandAction|19|170-preAction
                if (getChannelList().size() > 0)
                    AddToFavorites(getChannelList().getString(getChannelList().getSelectedIndex()));
//GEN-LINE:|7-commandAction|20|170-postAction
                // write post-action user code here
            } else if (command == ChListGoToMain) {//GEN-LINE:|7-commandAction|21|85-preAction
                // write pre-action user code here
                switchDisplayable(null, getInitDlg());//GEN-LINE:|7-commandAction|22|85-postAction
                // write post-action user code here
            } else if (command == ChListGoToNetworks) {//GEN-LINE:|7-commandAction|23|82-preAction
                // write pre-action user code here
                switchDisplayable(null, getNetworksDlg());//GEN-LINE:|7-commandAction|24|82-postAction
                // write post-action user code here
            } else if (command == ChanneListBack) {//GEN-LINE:|7-commandAction|25|56-preAction
                // write pre-action user code here
                switchDisplayable(null, getChannelListFilter());//GEN-LINE:|7-commandAction|26|56-postAction
                // write post-action user code here
            } else if (command == JoinChannel) {//GEN-LINE:|7-commandAction|27|58-preAction
                if (getChannelList().size() > 0)
                    JoinChannel(getChannelList().getString(getChannelList().getSelectedIndex()), true);
//GEN-LINE:|7-commandAction|28|58-postAction
                // write post-action user code here
            } else if (command == List.SELECT_COMMAND) {//GEN-LINE:|7-commandAction|29|50-preAction
                // write pre-action user code here
                ChannelListAction();//GEN-LINE:|7-commandAction|30|50-postAction
                // write post-action user code here
            } else if (command == RefreshList) {//GEN-LINE:|7-commandAction|31|77-preAction
                SecondaryConnection.RequireChannelList();
//GEN-LINE:|7-commandAction|32|77-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|126-preAction
        } else if (displayable == ChannelListFilter) {
            if (command == AutoJoinListCh) {//GEN-END:|7-commandAction|33|126-preAction
                // write pre-action user code here
                switchDisplayable(null, getAutoJoinList());//GEN-LINE:|7-commandAction|34|126-postAction
                // write post-action user code here
            } else if (command == AutoLoginListCh) {//GEN-LINE:|7-commandAction|35|128-preAction
                // write pre-action user code here
                switchDisplayable(null, getAutoLoginsList());//GEN-LINE:|7-commandAction|36|128-postAction
                // write post-action user code here
            } else if (command == ChGoToNetworks) {//GEN-LINE:|7-commandAction|37|79-preAction
                // write pre-action user code here
                switchDisplayable(null, getNetworksDlg());//GEN-LINE:|7-commandAction|38|79-postAction
                // write post-action user code here
            } else if (command == ChannelBack) {//GEN-LINE:|7-commandAction|39|38-preAction
                // write pre-action user code here
                switchDisplayable(null, getInitDlg());//GEN-LINE:|7-commandAction|40|38-postAction
                // write post-action user code here
            } else if (command == FavoritesListCh) {//GEN-LINE:|7-commandAction|41|124-preAction
                // write pre-action user code here
                switchDisplayable(null, getFavoritesList());//GEN-LINE:|7-commandAction|42|124-postAction
                // write post-action user code here
            } else if (command == GoToChannels) {//GEN-LINE:|7-commandAction|43|53-preAction
                getChannelList().deleteAll();

                String Filt = getFilter().getString();
                Filt = Filt.toLowerCase();

                Vector Channels = SecondaryConnection.GetChannelList();
                int Len = Channels.size(), Len2 = Filt.length();

                if (Len > 0)
                {
                    Enumeration vEnum = Channels.elements();

                    for (int i = 0; i < Len; i++)
                    {
                        String Tmp = (String)vEnum.nextElement();

                        if (Len2 > 0)
                        {
                            int index = Tmp.toLowerCase().indexOf(Filt);

                            if (index >= 0)
                            {
                                getChannelList().append(Tmp, null);
                            }
                        }

                        else
                        {
                            getChannelList().append(Tmp, null);
                        }
                   }
                }

                switchDisplayable(null, getChannelList());//GEN-LINE:|7-commandAction|44|53-postAction
                // write post-action user code here
            } else if (command == Join) {//GEN-LINE:|7-commandAction|45|41-preAction
                JoinChannel(getFilter().getString(), true);                
//GEN-LINE:|7-commandAction|46|41-postAction

            }//GEN-BEGIN:|7-commandAction|47|139-preAction
        } else if (displayable == FavoritesList) {
            if (command == FavAddChannel) {//GEN-END:|7-commandAction|47|139-preAction
                getGenericChannel().setString("");
                getGenericLogin().setString("");

                if (getGenericAddChannel().size() > 1)
                {
                    getGenericAddChannel().delete(1);
                }

                ChType = 0;

                switchDisplayable(null, getGenericAddChannel());//GEN-LINE:|7-commandAction|48|139-postAction
                // write post-action user code here
            } else if (command == FavBack) {//GEN-LINE:|7-commandAction|49|136-preAction
                // write pre-action user code here
                switchDisplayable(null, getChannelListFilter());//GEN-LINE:|7-commandAction|50|136-postAction
                // write post-action user code here
            } else if (command == FavJoin) {//GEN-LINE:|7-commandAction|51|157-preAction
                if (getFavoritesList().size() > 0)
                    JoinChannel(getFavoritesList().getString(getFavoritesList().getSelectedIndex()), true);
//GEN-LINE:|7-commandAction|52|157-postAction
                // write post-action user code here
            } else if (command == FavRemChannel) {//GEN-LINE:|7-commandAction|53|141-preAction
                if (getFavoritesList().size() > 0)
                    RemoveFromFavorites(getFavoritesList().getString(getFavoritesList().getSelectedIndex()));
//GEN-LINE:|7-commandAction|54|141-postAction
                // write post-action user code here
            } else if (command == List.SELECT_COMMAND) {//GEN-LINE:|7-commandAction|55|112-preAction
                if (getFavoritesList().size() > 0)
                    JoinChannel(getFavoritesList().getString(getFavoritesList().getSelectedIndex()), true);
                FavoritesListAction();//GEN-LINE:|7-commandAction|56|112-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|57|165-preAction
        } else if (displayable == GenericAddChannel) {
            if (command == GenericBack) {//GEN-END:|7-commandAction|57|165-preAction
                // write pre-action user code here
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|58|165-postAction
                // write post-action user code here
            } else if (command == GenericOK) {//GEN-LINE:|7-commandAction|59|167-preAction
                switch (ChType)
                {
                    case 0:
                    {
                        AddToFavorites(getGenericChannel().getString());

                        break;
                    }

                    case 1:
                    {
                        AddToAutoJoin(getGenericChannel().getString());

                        break;
                    }

                    case 2:
                    {
                        AddToAutoLogin(getGenericChannel().getString(), getGenericLogin().getString());

                        break;
                    }

                    case -1:
                    {
                        //Nothing
                    }
                }

                switchToPreviousDisplayable();

                ChType = -1;
//GEN-LINE:|7-commandAction|60|167-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|61|175-preAction
        } else if (displayable == InitDlg) {
            if (command == About) {//GEN-END:|7-commandAction|61|175-preAction
                 Utils.MessageBox(getDisplay(), "About", "WPCC Mobile - WinMX Professional Chat Client Mobile v" + GetVer() + " by Emulator\nJ2ME Version\n\nThanks to:\nLanguage Translations:\nItaliano: Emulator\nEnglish: Emulator\nDeutsch: Ome_Leo\nEspanol: NachoTico\nFrançais: Zenar\nNorwegian: Beat Doctor\n\nTesters:\nTNT-Tony [Nokia N95]\nEnemy [Nokia e61i]\nyuki [Nokia N96]\n\nThanks to Quartz for cooperation!\n\n\nEmulator, CrystalSoft ®\nCopyright © 2009 CrystalSoft ®", Alert.FOREVER, AlertType.INFO);
//GEN-LINE:|7-commandAction|62|175-postAction
                // write post-action user code here
            } else if (command == ExitApp) {//GEN-LINE:|7-commandAction|63|46-preAction
                exitMIDlet();
//GEN-LINE:|7-commandAction|64|46-postAction
                // write post-action user code here
            } else if (command == Go) {//GEN-LINE:|7-commandAction|65|48-preAction
                InitDlgAction();
//GEN-LINE:|7-commandAction|66|48-postAction
                // write post-action user code here
            } else if (command == List.SELECT_COMMAND) {//GEN-LINE:|7-commandAction|67|24-preAction
                // write pre-action user code here
                InitDlgAction();//GEN-LINE:|7-commandAction|68|24-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|69|69-preAction
        } else if (displayable == NetworksDlg) {
            if (command == Apply) {//GEN-END:|7-commandAction|69|69-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|70|69-postAction
                Apply(false);
            } else if (command == Connect) {//GEN-LINE:|7-commandAction|71|62-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|72|62-postAction
                SecondaryConnection.Connect();
            } else if (command == Disconnect) {//GEN-LINE:|7-commandAction|73|64-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|74|64-postAction
                SecondaryConnection.Disconnect();
            } else if (command == NetworksBack) {//GEN-LINE:|7-commandAction|75|33-preAction
                // write pre-action user code here
                switchDisplayable(null, getInitDlg());//GEN-LINE:|7-commandAction|76|33-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|77|104-preAction
        } else if (displayable == PMTextBox) {
            if (command == PMCancel) {//GEN-END:|7-commandAction|77|104-preAction
                // write pre-action user code here
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|78|104-postAction
                // write post-action user code here
            } else if (command == PMSend) {//GEN-LINE:|7-commandAction|79|106-preAction
                switchToPreviousDisplayable();
//GEN-LINE:|7-commandAction|80|106-postAction
                SecondaryConnection.SendPM(PMUser, PMTextBox.getString(), PMHash);
            }//GEN-BEGIN:|7-commandAction|81|93-preAction
        } else if (displayable == SettingsDlg) {
            if (command == SettApply) {//GEN-END:|7-commandAction|81|93-preAction
                Apply(true);
//GEN-LINE:|7-commandAction|82|93-postAction
                // write post-action user code here
            } else if (command == SettingsBack) {//GEN-LINE:|7-commandAction|83|36-preAction
                // write pre-action user code here
                switchDisplayable(null, getInitDlg());//GEN-LINE:|7-commandAction|84|36-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|85|7-postCommandAction
        }//GEN-END:|7-commandAction|85|7-postCommandAction

        if (displayable == NetworksDlg)
        {
            if (command == Refresh)
            {
                SecondaryConnection.Refresh();
            } 
        }

        //ChannelDlg Commands
        for (int i = 0; i < MaxChat; i++)
        {
            if (displayable == ChannelDlgs[i])
            {
                if ((command == Sends) || (command == Sends2))
                {
                    SendText(i, ((TextField)ChannelDlgs[i].get(1)).getString());
                }

                else if (command == Rejoin)
                {
                    ChatClient[i].Rejoin();
                }

                else if (command == RejoinAll)
                {
                    for (int z = 0; z < MaxChat; z++)
                    {
                        ChatClient[z].Rejoin();
                    }
                }

                else if (command == JoinAll)
                {
                    Joiner J = new Joiner();
                    J.start();
                }

                else if (command == AddFavorites)
                {
                    AddToFavorites(ChannelDlgs[i].getTitle());
                }

                else if (command == AddAutoJoin)
                {
                    AddToAutoJoin(ChannelDlgs[i].getTitle());
                }

                else if (command == GoToUserLists)
                {
                    switchDisplayable(null, UserLists[i]);
                }

                else if (command == GoToMain)
                {
                    switchDisplayable(null, getInitDlg());
                }

                else if (command == GoToNet)
                {
                    switchDisplayable(null, getNetworksDlg());
                }

                else if (command == Clear)
                {
                    ((CWinMXEdit)ChannelDlgs[i].get(0)).clear();
                }

                else if (command == Backs)
                {
                    switchToPreviousDisplayable();
                }

                else if (command == ExitFromRoom)
                {
                    ExitChannel(i);
                }

                else if (command == ExitWPCC)
                {
                    exitMIDlet();
                }
            }

            if ((displayable == UserLists[i]))
            {
                if (UserLists[i].size() > 0)
                {
                    String Nickname = UserLists[i].getString(UserLists[i].getSelectedIndex());

                    if ((command == SendPM) || (command == UserLists[i].SELECT_COMMAND))
                    {
                        PMUser = Nickname.substring(0, Nickname.indexOf(" ", 0));
                        PMHash = Nickname.substring(Nickname.lastIndexOf(' ')+1);

                        getPMTextBox().setTitle(Lang[59] + " " + PMUser);
                        getPMTextBox().setString("");

                        switchDisplayable(null, getPMTextBox());
                    }

                    else if (command == Whois)
                    {
                        SecondaryConnection.Whois(Nickname.substring(0, Nickname.indexOf(" ", 0)), Nickname.substring(Nickname.lastIndexOf(' ')+1));
                    }

                    else if (command == Levels)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        SendText(i, "/level " + Nickname.substring(0, Nickname.indexOf(" ", 0)));
                    }

                    else if (command == Kicks)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        SendText(i, "/kick " + Nickname.substring(0, Nickname.indexOf(" ", 0)));
                    }

                    else if (command == Ban)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        SendText(i, "/ban " + Nickname.substring(0, Nickname.indexOf(" ", 0)));
                    }

                    else if (command == KickBan)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        SendText(i, "/kickban " + Nickname.substring(0, Nickname.indexOf(" ", 0)));
                    }

                    if (command == UserLevel)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        ((TextField)ChannelDlgs[i].get(1)).setString("/setuserlevel " + Nickname.substring(0, Nickname.indexOf(" ", 0)) + " ");
                    }
                }

                if (command == ListBack)
                {
                    switchDisplayable(null, ChannelDlgs[i]);
                }
            }
        }
    }//GEN-BEGIN:|7-commandAction|86|
    //</editor-fold>//GEN-END:|7-commandAction|86|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: InitDlg ">//GEN-BEGIN:|22-getter|0|22-preInit
    /**
     * Returns an initiliazed instance of InitDlg component.
     * @return the initialized component instance
     */
    public List getInitDlg() {
        if (InitDlg == null) {//GEN-END:|22-getter|0|22-preInit
            // write pre-init user code here
            InitDlg = new List("WPCC Mobile v" + GetVer() + " by Emulator", Choice.IMPLICIT);//GEN-BEGIN:|22-getter|1|22-postInit
            InitDlg.addCommand(getGo());
            InitDlg.addCommand(getAbout());
            InitDlg.addCommand(getExitApp());
            InitDlg.setCommandListener(this);
            InitDlg.setSelectedFlags(new boolean[] {  });//GEN-END:|22-getter|1|22-postInit

            InitDlg.append(Lang[34], Icons[0]);
            InitDlg.append(Lang[35], Icons[1]);
            InitDlg.append(Lang[36], Icons[2]);

            Width = InitDlg.getWidth();
            Height = InitDlg.getHeight();
        }//GEN-BEGIN:|22-getter|2|
        return InitDlg;
    }
    //</editor-fold>//GEN-END:|22-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: InitDlgAction ">//GEN-BEGIN:|22-action|0|22-preAction
    /**
     * Performs an action assigned to the selected list element in the InitDlg component.
     */
    public void InitDlgAction() {//GEN-END:|22-action|0|22-preAction
        // enter pre-action user code here
        String __selectedString = getInitDlg().getString(getInitDlg().getSelectedIndex());//GEN-LINE:|22-action|1|22-postAction

        if (__selectedString != null)
        {
            if (__selectedString.equals(Lang[34]))
            {
                switchDisplayable(null, getNetworksDlg());
            }

            else if (__selectedString.equals(Lang[35]))
            {
                switchDisplayable(null, getChannelListFilter());
            }

            else if (__selectedString.equals(Lang[36]))
            {
                switchDisplayable(null, getSettingsDlg());
            }
            
            else
            {
                for (int i = 0; i < MaxChat; i++)
                {
                    if (ChannelDlgs[i] != null)
                    {
                        String Tmp = __selectedString, Tmp2 = ChatClient[i].GetChannelName();

                        int index = Tmp.lastIndexOf('_');

                        if (index >= 0)
                        {
                            Tmp = Tmp.substring(index+1);
                        }

                        index = Tmp2.lastIndexOf('_');

                        if (index >= 0)
                        {
                            Tmp2 = Tmp2.substring(index+1);
                        }

                        if (Tmp2.equalsIgnoreCase(Tmp))
                        {
                            switchDisplayable(null, ChannelDlgs[i]);
                            getDisplay().setCurrentItem(ChannelDlgs[i].get(1));

                            break;
                        }
                    }
                }
            }
        }
    }//GEN-BEGIN:|22-action|2|
    //</editor-fold>//GEN-END:|22-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: NetworksBack ">//GEN-BEGIN:|32-getter|0|32-preInit
    /**
     * Returns an initiliazed instance of NetworksBack component.
     * @return the initialized component instance
     */
    public Command getNetworksBack() {
        if (NetworksBack == null) {//GEN-END:|32-getter|0|32-preInit
            // write pre-init user code here
            NetworksBack = new Command(Lang[28], Command.BACK, 3);//GEN-LINE:|32-getter|1|32-postInit
            // write post-init user code here
        }//GEN-BEGIN:|32-getter|2|
        return NetworksBack;
    }
    //</editor-fold>//GEN-END:|32-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: SettingsBack ">//GEN-BEGIN:|35-getter|0|35-preInit
    /**
     * Returns an initiliazed instance of SettingsBack component.
     * @return the initialized component instance
     */
    public Command getSettingsBack() {
        if (SettingsBack == null) {//GEN-END:|35-getter|0|35-preInit
            // write pre-init user code here
            SettingsBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|35-getter|1|35-postInit
            // write post-init user code here
        }//GEN-BEGIN:|35-getter|2|
        return SettingsBack;
    }
    //</editor-fold>//GEN-END:|35-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChannelBack ">//GEN-BEGIN:|37-getter|0|37-preInit
    /**
     * Returns an initiliazed instance of ChannelBack component.
     * @return the initialized component instance
     */
    public Command getChannelBack() {
        if (ChannelBack == null) {//GEN-END:|37-getter|0|37-preInit
            // write pre-init user code here
            ChannelBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|37-getter|1|37-postInit
            // write post-init user code here
        }//GEN-BEGIN:|37-getter|2|
        return ChannelBack;
    }
    //</editor-fold>//GEN-END:|37-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Filter ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initiliazed instance of Filter component.
     * @return the initialized component instance
     */
    public TextField getFilter() {
        if (Filter == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            Filter = new TextField(Lang[49] + ":", "", 1000, TextField.ANY);//GEN-LINE:|39-getter|1|39-postInit
            // 
        }//GEN-BEGIN:|39-getter|2|
        return Filter;
    }
    //</editor-fold>//GEN-END:|39-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Join ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initiliazed instance of Join component.
     * @return the initialized component instance
     */
    public Command getJoin() {
        if (Join == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            Join = new Command(Lang[4], Command.ITEM, 0);//GEN-LINE:|40-getter|1|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|2|
        return Join;
    }
    //</editor-fold>//GEN-END:|40-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ExitApp ">//GEN-BEGIN:|45-getter|0|45-preInit
    /**
     * Returns an initiliazed instance of ExitApp component.
     * @return the initialized component instance
     */
    public Command getExitApp() {
        if (ExitApp == null) {//GEN-END:|45-getter|0|45-preInit
            // write pre-init user code here
            ExitApp = new Command(Lang[5], Command.BACK, 0);//GEN-LINE:|45-getter|1|45-postInit

        }//GEN-BEGIN:|45-getter|2|
        return ExitApp;
    }
    //</editor-fold>//GEN-END:|45-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Go ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initiliazed instance of Go component.
     * @return the initialized component instance
     */
    public Command getGo() {
        if (Go == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            Go = new Command(Lang[33], Command.OK, 0);//GEN-LINE:|47-getter|1|47-postInit
            
        }//GEN-BEGIN:|47-getter|2|
        return Go;
    }
    //</editor-fold>//GEN-END:|47-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChannelList ">//GEN-BEGIN:|49-getter|0|49-preInit
    /**
     * Returns an initiliazed instance of ChannelList component.
     * @return the initialized component instance
     */
    public List getChannelList() {
        if (ChannelList == null) {//GEN-END:|49-getter|0|49-preInit
            // write pre-init user code here
            ChannelList = new List(Lang[35], Choice.IMPLICIT);//GEN-BEGIN:|49-getter|1|49-postInit
            ChannelList.addCommand(getJoinChannel());
            ChannelList.addCommand(getRefreshList());
            ChannelList.addCommand(getChAddToFavorites());
            ChannelList.addCommand(getChAddToAutoJoin());
            ChannelList.addCommand(getChListGoToNetworks());
            ChannelList.addCommand(getChListGoToMain());
            ChannelList.addCommand(getChanneListBack());
            ChannelList.setCommandListener(this);//GEN-END:|49-getter|1|49-postInit
            //
        }//GEN-BEGIN:|49-getter|2|
        return ChannelList;
    }
    //</editor-fold>//GEN-END:|49-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: ChannelListAction ">//GEN-BEGIN:|49-action|0|49-preAction
    /**
     * Performs an action assigned to the selected list element in the ChannelList component.
     */
    public void ChannelListAction() {//GEN-END:|49-action|0|49-preAction
        // enter pre-action user code here
        String __selectedString = getChannelList().getString(getChannelList().getSelectedIndex());//GEN-LINE:|49-action|1|49-postAction

        if (getChannelList().size() > 0)
            JoinChannel(__selectedString, true);
    }//GEN-BEGIN:|49-action|2|
    //</editor-fold>//GEN-END:|49-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GoToChannels ">//GEN-BEGIN:|52-getter|0|52-preInit
    /**
     * Returns an initiliazed instance of GoToChannels component.
     * @return the initialized component instance
     */
    public Command getGoToChannels() {
        if (GoToChannels == null) {//GEN-END:|52-getter|0|52-preInit
            // write pre-init user code here
            GoToChannels = new Command(Lang[41], Command.ITEM, 1);//GEN-LINE:|52-getter|1|52-postInit
            // write post-init user code here
        }//GEN-BEGIN:|52-getter|2|
        return GoToChannels;
    }
    //</editor-fold>//GEN-END:|52-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChanneListBack ">//GEN-BEGIN:|55-getter|0|55-preInit
    /**
     * Returns an initiliazed instance of ChanneListBack component.
     * @return the initialized component instance
     */
    public Command getChanneListBack() {
        if (ChanneListBack == null) {//GEN-END:|55-getter|0|55-preInit
            // write pre-init user code here
            ChanneListBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|55-getter|1|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|2|
        return ChanneListBack;
    }
    //</editor-fold>//GEN-END:|55-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: JoinChannel ">//GEN-BEGIN:|57-getter|0|57-preInit
    /**
     * Returns an initiliazed instance of JoinChannel component.
     * @return the initialized component instance
     */
    public Command getJoinChannel() {
        if (JoinChannel == null) {//GEN-END:|57-getter|0|57-preInit
            // write pre-init user code here
            JoinChannel = new Command(Lang[4], Command.OK, 0);//GEN-LINE:|57-getter|1|57-postInit
            // write post-init user code here
        }//GEN-BEGIN:|57-getter|2|
        return JoinChannel;
    }
    //</editor-fold>//GEN-END:|57-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ConnStatus ">//GEN-BEGIN:|60-getter|0|60-preInit
    /**
     * Returns an initiliazed instance of ConnStatus component.
     * @return the initialized component instance
     */
    public StringItem getConnStatus() {
        if (ConnStatus == null) {//GEN-END:|60-getter|0|60-preInit
            // write pre-init user code here
            ConnStatus = new StringItem(Lang[29] + ":", "");//GEN-LINE:|60-getter|1|60-postInit
            ConnStatus.setText(Lang[30]);
        }//GEN-BEGIN:|60-getter|2|
        return ConnStatus;
    }
    //</editor-fold>//GEN-END:|60-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Connect ">//GEN-BEGIN:|61-getter|0|61-preInit
    /**
     * Returns an initiliazed instance of Connect component.
     * @return the initialized component instance
     */
    public Command getConnect() {
        if (Connect == null) {//GEN-END:|61-getter|0|61-preInit
            // write pre-init user code here
            Connect = new Command(Lang[39], Command.ITEM, 0);//GEN-LINE:|61-getter|1|61-postInit
            // write post-init user code here
        }//GEN-BEGIN:|61-getter|2|
        return Connect;
    }
    //</editor-fold>//GEN-END:|61-getter|2|

    /**
     * Returns an initiliazed instance of Refresh component.
     * @return the initialized component instance
     */
    public Command getRefresh()
    {
        if (Refresh == null)
        {
            Refresh = new Command(Lang[45], Command.ITEM, 0);
        }
        return Refresh;
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Disconnect ">//GEN-BEGIN:|63-getter|0|63-preInit
    /**
     * Returns an initiliazed instance of Disconnect component.
     * @return the initialized component instance
     */
    public Command getDisconnect() {
        if (Disconnect == null) {//GEN-END:|63-getter|0|63-preInit
            // write pre-init user code here
            Disconnect = new Command(Lang[40], Command.ITEM, 1);//GEN-LINE:|63-getter|1|63-postInit
            // write post-init user code here
        }//GEN-BEGIN:|63-getter|2|
        return Disconnect;
    }
    //</editor-fold>//GEN-END:|63-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ScreenName ">//GEN-BEGIN:|65-getter|0|65-preInit
    /**
     * Returns an initiliazed instance of ScreenName component.
     * @return the initialized component instance
     */
    public TextField getScreenName() {
        if (ScreenName == null) {//GEN-END:|65-getter|0|65-preInit
            // write pre-init user code here
            ScreenName = new TextField(Lang[0], "", 40, TextField.ANY);//GEN-LINE:|65-getter|1|65-postInit
            ScreenName.setString(GetNickName());
        }//GEN-BEGIN:|65-getter|2|
        return ScreenName;
    }
    //</editor-fold>//GEN-END:|65-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: SharedFiles ">//GEN-BEGIN:|66-getter|0|66-preInit
    /**
     * Returns an initiliazed instance of SharedFiles component.
     * @return the initialized component instance
     */
    public TextField getSharedFiles() {
        if (SharedFiles == null) {//GEN-END:|66-getter|0|66-preInit
            // write pre-init user code here
            SharedFiles = new TextField(Lang[2], "", 5, TextField.NUMERIC);//GEN-LINE:|66-getter|1|66-postInit
            SharedFiles.setString("" + File);
        }//GEN-BEGIN:|66-getter|2|
        return SharedFiles;
    }
    //</editor-fold>//GEN-END:|66-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ConnType ">//GEN-BEGIN:|67-getter|0|67-preInit
    /**
     * Returns an initiliazed instance of ConnType component.
     * @return the initialized component instance
     */
    public ChoiceGroup getConnType() {
        if (ConnType == null) {//GEN-END:|67-getter|0|67-preInit
            // write pre-init user code here
            ConnType = new ChoiceGroup(Lang[1], Choice.POPUP);//GEN-LINE:|67-getter|1|67-postInit

            for (int i = 0; i < 11; i++)
            {
                ConnType.append(Lines[i], null);
            }
            
            ConnType.setSelectedIndex(Line, true);
        }//GEN-BEGIN:|67-getter|2|
        return ConnType;
    }
    //</editor-fold>//GEN-END:|67-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Apply ">//GEN-BEGIN:|68-getter|0|68-preInit
    /**
     * Returns an initiliazed instance of Apply component.
     * @return the initialized component instance
     */
    public Command getApply() {
        if (Apply == null) {//GEN-END:|68-getter|0|68-preInit
            // write pre-init user code here
            Apply = new Command(Lang[17], Command.ITEM, 2);//GEN-LINE:|68-getter|1|68-postInit
           //
        }//GEN-BEGIN:|68-getter|2|
        return Apply;
    }
    //</editor-fold>//GEN-END:|68-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Status ">//GEN-BEGIN:|70-getter|0|70-preInit
    /**
     * Returns an initiliazed instance of Status component.
     * @return the initialized component instance
     */
    public ChoiceGroup getStatus() {
        if (Status == null) {//GEN-END:|70-getter|0|70-preInit
            // write pre-init user code here
            Status = new ChoiceGroup(Lang[37], Choice.POPUP);//GEN-LINE:|70-getter|1|70-postInit

            for (int i = 0; i < 7; i++)
            {
                Status.append(States[i], null);
            }
            
            Status.setSelectedIndex(PersState, true);
        }//GEN-BEGIN:|70-getter|2|
        return Status;
    }
    //</editor-fold>//GEN-END:|70-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: PersMex ">//GEN-BEGIN:|71-getter|0|71-preInit
    /**
     * Returns an initiliazed instance of PersMex component.
     * @return the initialized component instance
     */
    public TextField getPersMex() {
        if (PersMex == null) {//GEN-END:|71-getter|0|71-preInit
            // write pre-init user code here
            PersMex = new TextField(Lang[38], "", 1000, TextField.ANY);//GEN-LINE:|71-getter|1|71-postInit
            PersMex.setString(PersonalMessage);
        }//GEN-BEGIN:|71-getter|2|
        return PersMex;
    }
    //</editor-fold>//GEN-END:|71-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChListGoToMainPage ">//GEN-BEGIN:|72-getter|0|72-preInit
    /**
     * Returns an initiliazed instance of ChListGoToMainPage component.
     * @return the initialized component instance
     */
    public Command getChListGoToMainPage() {
        if (ChListGoToMainPage == null) {//GEN-END:|72-getter|0|72-preInit
            // write pre-init user code here
            ChListGoToMainPage = new Command(Lang[42], "", Command.ITEM, 0);//GEN-LINE:|72-getter|1|72-postInit
            // write post-init user code here
        }//GEN-BEGIN:|72-getter|2|
        return ChListGoToMainPage;
    }
    //</editor-fold>//GEN-END:|72-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChListGoMain ">//GEN-BEGIN:|74-getter|0|74-preInit
    /**
     * Returns an initiliazed instance of ChListGoMain component.
     * @return the initialized component instance
     */
    public Command getChListGoMain() {
        if (ChListGoMain == null) {//GEN-END:|74-getter|0|74-preInit
            // write pre-init user code here
            ChListGoMain = new Command(Lang[42], Command.ITEM, 0);//GEN-LINE:|74-getter|1|74-postInit
            // write post-init user code here
        }//GEN-BEGIN:|74-getter|2|
        return ChListGoMain;
    }
    //</editor-fold>//GEN-END:|74-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: RefreshList ">//GEN-BEGIN:|76-getter|0|76-preInit
    /**
     * Returns an initiliazed instance of RefreshList component.
     * @return the initialized component instance
     */
    public Command getRefreshList() {
        if (RefreshList == null) {//GEN-END:|76-getter|0|76-preInit
            // write pre-init user code here
            RefreshList = new Command(Lang[44], Command.ITEM, 1);//GEN-LINE:|76-getter|1|76-postInit
            // write post-init user code here
        }//GEN-BEGIN:|76-getter|2|
        return RefreshList;
    }
    //</editor-fold>//GEN-END:|76-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChGoToNetworks ">//GEN-BEGIN:|78-getter|0|78-preInit
    /**
     * Returns an initiliazed instance of ChGoToNetworks component.
     * @return the initialized component instance
     */
    public Command getChGoToNetworks() {
        if (ChGoToNetworks == null) {//GEN-END:|78-getter|0|78-preInit
            // write pre-init user code here
            ChGoToNetworks = new Command(Lang[46], Command.ITEM, 5);//GEN-LINE:|78-getter|1|78-postInit
            // write post-init user code here
        }//GEN-BEGIN:|78-getter|2|
        return ChGoToNetworks;
    }
    //</editor-fold>//GEN-END:|78-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChListGoToNetworks ">//GEN-BEGIN:|81-getter|0|81-preInit
    /**
     * Returns an initiliazed instance of ChListGoToNetworks component.
     * @return the initialized component instance
     */
    public Command getChListGoToNetworks() {
        if (ChListGoToNetworks == null) {//GEN-END:|81-getter|0|81-preInit
            // write pre-init user code here
            ChListGoToNetworks = new Command(Lang[46], Command.ITEM, 4);//GEN-LINE:|81-getter|1|81-postInit
            // write post-init user code here
        }//GEN-BEGIN:|81-getter|2|
        return ChListGoToNetworks;
    }
    //</editor-fold>//GEN-END:|81-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChListGoToMain ">//GEN-BEGIN:|84-getter|0|84-preInit
    /**
     * Returns an initiliazed instance of ChListGoToMain component.
     * @return the initialized component instance
     */
    public Command getChListGoToMain() {
        if (ChListGoToMain == null) {//GEN-END:|84-getter|0|84-preInit
            // write pre-init user code here
            ChListGoToMain = new Command(Lang[42], Command.ITEM, 5);//GEN-LINE:|84-getter|1|84-postInit
            // write post-init user code here
        }//GEN-BEGIN:|84-getter|2|
        return ChListGoToMain;
    }
    //</editor-fold>//GEN-END:|84-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: PMTextBox ">//GEN-BEGIN:|90-getter|0|90-preInit
    /**
     * Returns an initiliazed instance of PMTextBox component.
     * @return the initialized component instance
     */
    public TextBox getPMTextBox() {
        if (PMTextBox == null) {//GEN-END:|90-getter|0|90-preInit
            // write pre-init user code here
            PMTextBox = new TextBox(Lang[47], "", 1000, TextField.ANY);//GEN-BEGIN:|90-getter|1|90-postInit
            PMTextBox.addCommand(getPMSend());
            PMTextBox.addCommand(getPMCancel());
            PMTextBox.setCommandListener(this);//GEN-END:|90-getter|1|90-postInit
            // write post-init user code here
        }//GEN-BEGIN:|90-getter|2|
        return PMTextBox;
    }
    //</editor-fold>//GEN-END:|90-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: Language ">//GEN-BEGIN:|91-getter|0|91-preInit
    /**
     * Returns an initiliazed instance of Language component.
     * @return the initialized component instance
     */
    public ChoiceGroup getLanguage() {
        if (Language == null) {//GEN-END:|91-getter|0|91-preInit
            // write pre-init user code here
            Language = new ChoiceGroup("Language:", Choice.POPUP);//GEN-LINE:|91-getter|1|91-postInit

            for (int i = 0; i < 6; i++)
            {
                Language.append(LangList[i], null);
            }

            Language.setSelectedIndex(MyLang, true);
        }//GEN-BEGIN:|91-getter|2|
        return Language;
    }
    //</editor-fold>//GEN-END:|91-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: SettApply ">//GEN-BEGIN:|92-getter|0|92-preInit
    /**
     * Returns an initiliazed instance of SettApply component.
     * @return the initialized component instance
     */
    public Command getSettApply() {
        if (SettApply == null) {//GEN-END:|92-getter|0|92-preInit
            // write pre-init user code here
            SettApply = new Command(Lang[17], Command.OK, 0);//GEN-LINE:|92-getter|1|92-postInit
            // write post-init user code here
        }//GEN-BEGIN:|92-getter|2|
        return SettApply;
    }
    //</editor-fold>//GEN-END:|92-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoRejoin ">//GEN-BEGIN:|95-getter|0|95-preInit
    /**
     * Returns an initiliazed instance of AutoRejoin component.
     * @return the initialized component instance
     */
    public TextField getAutoRejoin() {
        if (AutoRejoin == null) {//GEN-END:|95-getter|0|95-preInit
            // write pre-init user code here
            AutoRejoin = new TextField(Lang[51] + ":", "", 2, TextField.NUMERIC);//GEN-LINE:|95-getter|1|95-postInit
            AutoRejoin.setString("" + RejoinRetry);
        }//GEN-BEGIN:|95-getter|2|
        return AutoRejoin;
    }
    //</editor-fold>//GEN-END:|95-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoMessage ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of AutoMessage component.
     * @return the initialized component instance
     */
    public TextField getAutoMessage() {
        if (AutoMessage == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            AutoMessage = new TextField(Lang[52], "", 400, TextField.ANY);//GEN-LINE:|96-getter|1|96-postInit
            AutoMessage.setString(AutoMex);
        }//GEN-BEGIN:|96-getter|2|
        return AutoMessage;
    }
    //</editor-fold>//GEN-END:|96-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoConn ">//GEN-BEGIN:|97-getter|0|97-preInit
    /**
     * Returns an initiliazed instance of AutoConn component.
     * @return the initialized component instance
     */
    public ChoiceGroup getAutoConn() {
        if (AutoConn == null) {//GEN-END:|97-getter|0|97-preInit
            // write pre-init user code here
            AutoConn = new ChoiceGroup(Lang[53], Choice.EXCLUSIVE);//GEN-LINE:|97-getter|1|97-postInit

            AutoConn.append(Lang[54], null);
            AutoConn.append(Lang[55], null);

            AutoConn.setSelectedIndex(AutoConnect? 0 : 1, true);
        }//GEN-BEGIN:|97-getter|2|
        return AutoConn;
    }
    //</editor-fold>//GEN-END:|97-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ShowState ">//GEN-BEGIN:|98-getter|0|98-preInit
    /**
     * Returns an initiliazed instance of ShowState component.
     * @return the initialized component instance
     */
    public ChoiceGroup getShowState() {
        if (ShowState == null) {//GEN-END:|98-getter|0|98-preInit
            // write pre-init user code here
            ShowState = new ChoiceGroup(Lang[56], Choice.EXCLUSIVE);//GEN-BEGIN:|98-getter|1|98-postInit
            ShowState.setSelectedFlags(new boolean[] {  });//GEN-END:|98-getter|1|98-postInit

            ShowState.append(Lang[54], null);
            ShowState.append(Lang[55], null);

            ShowState.setSelectedIndex(ShowStatus? 0 : 1, true);
        }//GEN-BEGIN:|98-getter|2|
        return ShowState;
    }
    //</editor-fold>//GEN-END:|98-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ShowVersion ">//GEN-BEGIN:|100-getter|0|100-preInit
    /**
     * Returns an initiliazed instance of ShowVersion component.
     * @return the initialized component instance
     */
    public ChoiceGroup getShowVersion() {
        if (ShowVersion == null) {//GEN-END:|100-getter|0|100-preInit
            // write pre-init user code here
            ShowVersion = new ChoiceGroup(Lang[57], Choice.EXCLUSIVE);//GEN-LINE:|100-getter|1|100-postInit

            ShowVersion.append(Lang[54], null);
            ShowVersion.append(Lang[55], null);

            ShowVersion.setSelectedIndex(ShowVer? 0 : 1, true);
        }//GEN-BEGIN:|100-getter|2|
        return ShowVersion;
    }
    //</editor-fold>//GEN-END:|100-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoFetch ">//GEN-BEGIN:|102-getter|0|102-preInit
    /**
     * Returns an initiliazed instance of AutoFetch component.
     * @return the initialized component instance
     */
    public ChoiceGroup getAutoFetch() {
        if (AutoFetch == null) {//GEN-END:|102-getter|0|102-preInit
            // write pre-init user code here
            AutoFetch = new ChoiceGroup(Lang[58], Choice.EXCLUSIVE);//GEN-LINE:|102-getter|1|102-postInit

            AutoFetch.append(Lang[54], null);
            AutoFetch.append(Lang[55], null);

            AutoFetch.setSelectedIndex(FirstFetch? 0 : 1, true);
        }//GEN-BEGIN:|102-getter|2|
        return AutoFetch;
    }
    //</editor-fold>//GEN-END:|102-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: PMCancel ">//GEN-BEGIN:|103-getter|0|103-preInit
    /**
     * Returns an initiliazed instance of PMCancel component.
     * @return the initialized component instance
     */
    public Command getPMCancel() {
        if (PMCancel == null) {//GEN-END:|103-getter|0|103-preInit
            // write pre-init user code here
            PMCancel = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|103-getter|1|103-postInit
            // write post-init user code here
        }//GEN-BEGIN:|103-getter|2|
        return PMCancel;
    }
    //</editor-fold>//GEN-END:|103-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: PMSend ">//GEN-BEGIN:|105-getter|0|105-preInit
    /**
     * Returns an initiliazed instance of PMSend component.
     * @return the initialized component instance
     */
    public Command getPMSend() {
        if (PMSend == null) {//GEN-END:|105-getter|0|105-preInit
            // write pre-init user code here
            PMSend = new Command(Lang[47], Command.OK, 0);//GEN-LINE:|105-getter|1|105-postInit
            // write post-init user code here
        }//GEN-BEGIN:|105-getter|2|
        return PMSend;
    }
    //</editor-fold>//GEN-END:|105-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoJoinList ">//GEN-BEGIN:|108-getter|0|108-preInit
    /**
     * Returns an initiliazed instance of AutoJoinList component.
     * @return the initialized component instance
     */
    public List getAutoJoinList() {
        if (AutoJoinList == null) {//GEN-END:|108-getter|0|108-preInit
            // write pre-init user code here
            AutoJoinList = new List(Lang[62], Choice.IMPLICIT);//GEN-BEGIN:|108-getter|1|108-postInit
            AutoJoinList.addCommand(getAutoAddChannel());
            AutoJoinList.addCommand(getAutoRemChannel());
            AutoJoinList.addCommand(getAutoJoinBack());
            AutoJoinList.setCommandListener(this);//GEN-END:|108-getter|1|108-postInit

            LoadAutoJoin();
        }//GEN-BEGIN:|108-getter|2|
        return AutoJoinList;
    }
    //</editor-fold>//GEN-END:|108-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: AutoJoinListAction ">//GEN-BEGIN:|108-action|0|108-preAction
    /**
     * Performs an action assigned to the selected list element in the AutoJoinList component.
     */
    public void AutoJoinListAction() {//GEN-END:|108-action|0|108-preAction
        // enter pre-action user code here
        String __selectedString = getAutoJoinList().getString(getAutoJoinList().getSelectedIndex());//GEN-LINE:|108-action|1|108-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|108-action|2|
    //</editor-fold>//GEN-END:|108-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavoritesList ">//GEN-BEGIN:|111-getter|0|111-preInit
    /**
     * Returns an initiliazed instance of FavoritesList component.
     * @return the initialized component instance
     */
    public List getFavoritesList() {
        if (FavoritesList == null) {//GEN-END:|111-getter|0|111-preInit
            // write pre-init user code here
            FavoritesList = new List(Lang[61], Choice.IMPLICIT);//GEN-BEGIN:|111-getter|1|111-postInit
            FavoritesList.addCommand(getFavJoin());
            FavoritesList.addCommand(getFavAddChannel());
            FavoritesList.addCommand(getFavRemChannel());
            FavoritesList.addCommand(getFavBack());
            FavoritesList.setCommandListener(this);//GEN-END:|111-getter|1|111-postInit

            LoadFavorites();
        }//GEN-BEGIN:|111-getter|2|
        return FavoritesList;
    }
    //</editor-fold>//GEN-END:|111-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: FavoritesListAction ">//GEN-BEGIN:|111-action|0|111-preAction
    /**
     * Performs an action assigned to the selected list element in the FavoritesList component.
     */
    public void FavoritesListAction() {//GEN-END:|111-action|0|111-preAction
        // enter pre-action user code here
        String __selectedString = getFavoritesList().getString(getFavoritesList().getSelectedIndex());//GEN-LINE:|111-action|1|111-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|111-action|2|
    //</editor-fold>//GEN-END:|111-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoLoginsList ">//GEN-BEGIN:|114-getter|0|114-preInit
    /**
     * Returns an initiliazed instance of AutoLoginsList component.
     * @return the initialized component instance
     */
    public List getAutoLoginsList() {
        if (AutoLoginsList == null) {//GEN-END:|114-getter|0|114-preInit
            // write pre-init user code here
            AutoLoginsList = new List(Lang[63], Choice.IMPLICIT);//GEN-BEGIN:|114-getter|1|114-postInit
            AutoLoginsList.addCommand(getLoginsAddChannel());
            AutoLoginsList.addCommand(getLoginsRemChannel());
            AutoLoginsList.addCommand(getLoginsBack());
            AutoLoginsList.setCommandListener(this);//GEN-END:|114-getter|1|114-postInit

            LoadAutoLogins();
        }//GEN-BEGIN:|114-getter|2|
        return AutoLoginsList;
    }
    //</editor-fold>//GEN-END:|114-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: AutoLoginsListAction ">//GEN-BEGIN:|114-action|0|114-preAction
    /**
     * Performs an action assigned to the selected list element in the AutoLoginsList component.
     */
    public void AutoLoginsListAction() {//GEN-END:|114-action|0|114-preAction
        // enter pre-action user code here
        String __selectedString = getAutoLoginsList().getString(getAutoLoginsList().getSelectedIndex());//GEN-LINE:|114-action|1|114-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|114-action|2|
    //</editor-fold>//GEN-END:|114-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavoriteCh ">//GEN-BEGIN:|117-getter|0|117-preInit
    /**
     * Returns an initiliazed instance of FavoriteCh component.
     * @return the initialized component instance
     */
    public Command getFavoriteCh() {
        if (FavoriteCh == null) {//GEN-END:|117-getter|0|117-preInit
            // write pre-init user code here
            FavoriteCh = new Command(Lang[61], Command.ITEM, 0);//GEN-LINE:|117-getter|1|117-postInit
            // write post-init user code here
        }//GEN-BEGIN:|117-getter|2|
        return FavoriteCh;
    }
    //</editor-fold>//GEN-END:|117-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoJoinCh ">//GEN-BEGIN:|119-getter|0|119-preInit
    /**
     * Returns an initiliazed instance of AutoJoinCh component.
     * @return the initialized component instance
     */
    public Command getAutoJoinCh() {
        if (AutoJoinCh == null) {//GEN-END:|119-getter|0|119-preInit
            // write pre-init user code here
            AutoJoinCh = new Command(Lang[62], Command.ITEM, 0);//GEN-LINE:|119-getter|1|119-postInit
            // write post-init user code here
        }//GEN-BEGIN:|119-getter|2|
        return AutoJoinCh;
    }
    //</editor-fold>//GEN-END:|119-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoLoginCh ">//GEN-BEGIN:|121-getter|0|121-preInit
    /**
     * Returns an initiliazed instance of AutoLoginCh component.
     * @return the initialized component instance
     */
    public Command getAutoLoginCh() {
        if (AutoLoginCh == null) {//GEN-END:|121-getter|0|121-preInit
            // write pre-init user code here
            AutoLoginCh = new Command(Lang[63], Command.ITEM, 0);//GEN-LINE:|121-getter|1|121-postInit
            // write post-init user code here
        }//GEN-BEGIN:|121-getter|2|
        return AutoLoginCh;
    }
    //</editor-fold>//GEN-END:|121-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavoritesListCh ">//GEN-BEGIN:|123-getter|0|123-preInit
    /**
     * Returns an initiliazed instance of FavoritesListCh component.
     * @return the initialized component instance
     */
    public Command getFavoritesListCh() {
        if (FavoritesListCh == null) {//GEN-END:|123-getter|0|123-preInit
            // write pre-init user code here
            FavoritesListCh = new Command(Lang[70] + " " + Lang[61], Command.ITEM, 2);//GEN-LINE:|123-getter|1|123-postInit
            // write post-init user code here
        }//GEN-BEGIN:|123-getter|2|
        return FavoritesListCh;
    }
    //</editor-fold>//GEN-END:|123-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoJoinListCh ">//GEN-BEGIN:|125-getter|0|125-preInit
    /**
     * Returns an initiliazed instance of AutoJoinListCh component.
     * @return the initialized component instance
     */
    public Command getAutoJoinListCh() {
        if (AutoJoinListCh == null) {//GEN-END:|125-getter|0|125-preInit
            // write pre-init user code here
            AutoJoinListCh = new Command(Lang[70] + " " + Lang[62], Command.ITEM, 3);//GEN-LINE:|125-getter|1|125-postInit
            // write post-init user code here
        }//GEN-BEGIN:|125-getter|2|
        return AutoJoinListCh;
    }
    //</editor-fold>//GEN-END:|125-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoLoginListCh ">//GEN-BEGIN:|127-getter|0|127-preInit
    /**
     * Returns an initiliazed instance of AutoLoginListCh component.
     * @return the initialized component instance
     */
    public Command getAutoLoginListCh() {
        if (AutoLoginListCh == null) {//GEN-END:|127-getter|0|127-preInit
            // write pre-init user code here
            AutoLoginListCh = new Command(Lang[70] + " " + Lang[63], Command.ITEM, 4);//GEN-LINE:|127-getter|1|127-postInit
            // write post-init user code here
        }//GEN-BEGIN:|127-getter|2|
        return AutoLoginListCh;
    }
    //</editor-fold>//GEN-END:|127-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavBack ">//GEN-BEGIN:|135-getter|0|135-preInit
    /**
     * Returns an initiliazed instance of FavBack component.
     * @return the initialized component instance
     */
    public Command getFavBack() {
        if (FavBack == null) {//GEN-END:|135-getter|0|135-preInit
            // write pre-init user code here
            FavBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|135-getter|1|135-postInit
            // write post-init user code here
        }//GEN-BEGIN:|135-getter|2|
        return FavBack;
    }
    //</editor-fold>//GEN-END:|135-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavAddChannel ">//GEN-BEGIN:|138-getter|0|138-preInit
    /**
     * Returns an initiliazed instance of FavAddChannel component.
     * @return the initialized component instance
     */
    public Command getFavAddChannel() {
        if (FavAddChannel == null) {//GEN-END:|138-getter|0|138-preInit
            // write pre-init user code here
            FavAddChannel = new Command(Lang[64], Command.ITEM, 1);//GEN-LINE:|138-getter|1|138-postInit
            // write post-init user code here
        }//GEN-BEGIN:|138-getter|2|
        return FavAddChannel;
    }
    //</editor-fold>//GEN-END:|138-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavRemChannel ">//GEN-BEGIN:|140-getter|0|140-preInit
    /**
     * Returns an initiliazed instance of FavRemChannel component.
     * @return the initialized component instance
     */
    public Command getFavRemChannel() {
        if (FavRemChannel == null) {//GEN-END:|140-getter|0|140-preInit
            // write pre-init user code here
            FavRemChannel = new Command(Lang[65], Command.ITEM, 2);//GEN-LINE:|140-getter|1|140-postInit
            // write post-init user code here
        }//GEN-BEGIN:|140-getter|2|
        return FavRemChannel;
    }
    //</editor-fold>//GEN-END:|140-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoJoinBack ">//GEN-BEGIN:|142-getter|0|142-preInit
    /**
     * Returns an initiliazed instance of AutoJoinBack component.
     * @return the initialized component instance
     */
    public Command getAutoJoinBack() {
        if (AutoJoinBack == null) {//GEN-END:|142-getter|0|142-preInit
            // write pre-init user code here
            AutoJoinBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|142-getter|1|142-postInit
            // write post-init user code here
        }//GEN-BEGIN:|142-getter|2|
        return AutoJoinBack;
    }
    //</editor-fold>//GEN-END:|142-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoAddChannel ">//GEN-BEGIN:|144-getter|0|144-preInit
    /**
     * Returns an initiliazed instance of AutoAddChannel component.
     * @return the initialized component instance
     */
    public Command getAutoAddChannel() {
        if (AutoAddChannel == null) {//GEN-END:|144-getter|0|144-preInit
            // write pre-init user code here
            AutoAddChannel = new Command(Lang[64], Command.ITEM, 0);//GEN-LINE:|144-getter|1|144-postInit
            // write post-init user code here
        }//GEN-BEGIN:|144-getter|2|
        return AutoAddChannel;
    }
    //</editor-fold>//GEN-END:|144-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: AutoRemChannel ">//GEN-BEGIN:|146-getter|0|146-preInit
    /**
     * Returns an initiliazed instance of AutoRemChannel component.
     * @return the initialized component instance
     */
    public Command getAutoRemChannel() {
        if (AutoRemChannel == null) {//GEN-END:|146-getter|0|146-preInit
            // write pre-init user code here
            AutoRemChannel = new Command(Lang[65], Command.ITEM, 1);//GEN-LINE:|146-getter|1|146-postInit
            // write post-init user code here
        }//GEN-BEGIN:|146-getter|2|
        return AutoRemChannel;
    }
    //</editor-fold>//GEN-END:|146-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: LoginsBack ">//GEN-BEGIN:|148-getter|0|148-preInit
    /**
     * Returns an initiliazed instance of LoginsBack component.
     * @return the initialized component instance
     */
    public Command getLoginsBack() {
        if (LoginsBack == null) {//GEN-END:|148-getter|0|148-preInit
            // write pre-init user code here
            LoginsBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|148-getter|1|148-postInit
            // write post-init user code here
        }//GEN-BEGIN:|148-getter|2|
        return LoginsBack;
    }
    //</editor-fold>//GEN-END:|148-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: LoginsAddChannel ">//GEN-BEGIN:|150-getter|0|150-preInit
    /**
     * Returns an initiliazed instance of LoginsAddChannel component.
     * @return the initialized component instance
     */
    public Command getLoginsAddChannel() {
        if (LoginsAddChannel == null) {//GEN-END:|150-getter|0|150-preInit
            // write pre-init user code here
            LoginsAddChannel = new Command(Lang[64], Command.ITEM, 0);//GEN-LINE:|150-getter|1|150-postInit
            // write post-init user code here
        }//GEN-BEGIN:|150-getter|2|
        return LoginsAddChannel;
    }
    //</editor-fold>//GEN-END:|150-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: LoginsRemChannel ">//GEN-BEGIN:|152-getter|0|152-preInit
    /**
     * Returns an initiliazed instance of LoginsRemChannel component.
     * @return the initialized component instance
     */
    public Command getLoginsRemChannel() {
        if (LoginsRemChannel == null) {//GEN-END:|152-getter|0|152-preInit
            // write pre-init user code here
            LoginsRemChannel = new Command(Lang[65], Command.ITEM, 1);//GEN-LINE:|152-getter|1|152-postInit
            // write post-init user code here
        }//GEN-BEGIN:|152-getter|2|
        return LoginsRemChannel;
    }
    //</editor-fold>//GEN-END:|152-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: FavJoin ">//GEN-BEGIN:|156-getter|0|156-preInit
    /**
     * Returns an initiliazed instance of FavJoin component.
     * @return the initialized component instance
     */
    public Command getFavJoin() {
        if (FavJoin == null) {//GEN-END:|156-getter|0|156-preInit
            // write pre-init user code here
            FavJoin = new Command(Lang[4], Command.OK, 0);//GEN-LINE:|156-getter|1|156-postInit
            // write post-init user code here
        }//GEN-BEGIN:|156-getter|2|
        return FavJoin;
    }
    //</editor-fold>//GEN-END:|156-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GenericAddChannel ">//GEN-BEGIN:|158-getter|0|158-preInit
    /**
     * Returns an initiliazed instance of GenericAddChannel component.
     * @return the initialized component instance
     */
    public Form getGenericAddChannel() {
        if (GenericAddChannel == null) {//GEN-END:|158-getter|0|158-preInit
            // write pre-init user code here
            GenericAddChannel = new Form(Lang[64], new Item[] { getGenericChannel(), getGenericLogin() });//GEN-BEGIN:|158-getter|1|158-postInit
            GenericAddChannel.addCommand(getGenericOK());
            GenericAddChannel.addCommand(getGenericBack());
            GenericAddChannel.setCommandListener(this);//GEN-END:|158-getter|1|158-postInit
            // write post-init user code here
        }//GEN-BEGIN:|158-getter|2|
        return GenericAddChannel;
    }
    //</editor-fold>//GEN-END:|158-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GenericChannel ">//GEN-BEGIN:|159-getter|0|159-preInit
    /**
     * Returns an initiliazed instance of GenericChannel component.
     * @return the initialized component instance
     */
    public TextField getGenericChannel() {
        if (GenericChannel == null) {//GEN-END:|159-getter|0|159-preInit
            // write pre-init user code here
            GenericChannel = new TextField(Lang[71], null, 500, TextField.ANY);//GEN-LINE:|159-getter|1|159-postInit
            // write post-init user code here
        }//GEN-BEGIN:|159-getter|2|
        return GenericChannel;
    }
    //</editor-fold>//GEN-END:|159-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GenericLogin ">//GEN-BEGIN:|160-getter|0|160-preInit
    /**
     * Returns an initiliazed instance of GenericLogin component.
     * @return the initialized component instance
     */
    public TextField getGenericLogin() {
        if (GenericLogin == null) {//GEN-END:|160-getter|0|160-preInit
            // write pre-init user code here
            GenericLogin = new TextField(Lang[72], null, 200, TextField.ANY | TextField.PASSWORD);//GEN-LINE:|160-getter|1|160-postInit
            // 
        }//GEN-BEGIN:|160-getter|2|
        return GenericLogin;
    }
    //</editor-fold>//GEN-END:|160-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GenericBack ">//GEN-BEGIN:|164-getter|0|164-preInit
    /**
     * Returns an initiliazed instance of GenericBack component.
     * @return the initialized component instance
     */
    public Command getGenericBack() {
        if (GenericBack == null) {//GEN-END:|164-getter|0|164-preInit
            // write pre-init user code here
            GenericBack = new Command(Lang[28], Command.BACK, 0);//GEN-LINE:|164-getter|1|164-postInit
            // write post-init user code here
        }//GEN-BEGIN:|164-getter|2|
        return GenericBack;
    }
    //</editor-fold>//GEN-END:|164-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: GenericOK ">//GEN-BEGIN:|166-getter|0|166-preInit
    /**
     * Returns an initiliazed instance of GenericOK component.
     * @return the initialized component instance
     */
    public Command getGenericOK() {
        if (GenericOK == null) {//GEN-END:|166-getter|0|166-preInit
            // write pre-init user code here
            GenericOK = new Command(Lang[73], Command.OK, 0);//GEN-LINE:|166-getter|1|166-postInit
            // write post-init user code here
        }//GEN-BEGIN:|166-getter|2|
        return GenericOK;
    }
    //</editor-fold>//GEN-END:|166-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChAddToFavorites ">//GEN-BEGIN:|169-getter|0|169-preInit
    /**
     * Returns an initiliazed instance of ChAddToFavorites component.
     * @return the initialized component instance
     */
    public Command getChAddToFavorites() {
        if (ChAddToFavorites == null) {//GEN-END:|169-getter|0|169-preInit
            // write pre-init user code here
            ChAddToFavorites = new Command(Lang[68], Command.ITEM, 2);//GEN-LINE:|169-getter|1|169-postInit
            // write post-init user code here
        }//GEN-BEGIN:|169-getter|2|
        return ChAddToFavorites;
    }
    //</editor-fold>//GEN-END:|169-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ChAddToAutoJoin ">//GEN-BEGIN:|171-getter|0|171-preInit
    /**
     * Returns an initiliazed instance of ChAddToAutoJoin component.
     * @return the initialized component instance
     */
    public Command getChAddToAutoJoin() {
        if (ChAddToAutoJoin == null) {//GEN-END:|171-getter|0|171-preInit
            // write pre-init user code here
            ChAddToAutoJoin = new Command(Lang[69], Command.ITEM, 3);//GEN-LINE:|171-getter|1|171-postInit
            // write post-init user code here
        }//GEN-BEGIN:|171-getter|2|
        return ChAddToAutoJoin;
    }
    //</editor-fold>//GEN-END:|171-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: DefaultPeerCache ">//GEN-BEGIN:|173-getter|0|173-preInit
    /**
     * Returns an initiliazed instance of DefaultPeerCache component.
     * @return the initialized component instance
     */
    public TextField getDefaultPeerCache() {
        if (DefaultPeerCache == null) {//GEN-END:|173-getter|0|173-preInit
            // write pre-init user code here
            DefaultPeerCache = new TextField(Lang[74], "", 300, TextField.ANY);//GEN-LINE:|173-getter|1|173-postInit
            DefaultPeerCache.setString(PeerCache);
        }//GEN-BEGIN:|173-getter|2|
        return DefaultPeerCache;
    }
    //</editor-fold>//GEN-END:|173-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: About ">//GEN-BEGIN:|174-getter|0|174-preInit
    /**
     * Returns an initiliazed instance of About component.
     * @return the initialized component instance
     */
    public Command getAbout() {
        if (About == null) {//GEN-END:|174-getter|0|174-preInit
            // write pre-init user code here
            About = new Command(Lang[13], Command.HELP, 0);//GEN-LINE:|174-getter|1|174-postInit
            // write post-init user code here
        }//GEN-BEGIN:|174-getter|2|
        return About;
    }
    //</editor-fold>//GEN-END:|174-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: EnableCol ">//GEN-BEGIN:|176-getter|0|176-preInit
    /**
     * Returns an initiliazed instance of EnableCol component.
     * @return the initialized component instance
     */
    public ChoiceGroup getEnableCol() {
        if (EnableCol == null) {//GEN-END:|176-getter|0|176-preInit
            // write pre-init user code here
            EnableCol = new ChoiceGroup(Lang[79], Choice.EXCLUSIVE);//GEN-LINE:|176-getter|1|176-postInit

            EnableCol.append(Lang[54], null);
            EnableCol.append(Lang[55], null);

            EnableCol.setSelectedIndex(EnableColours? 0 : 1, true);
        }//GEN-BEGIN:|176-getter|2|
        return EnableCol;
    }
    //</editor-fold>//GEN-END:|176-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ColorMode ">//GEN-BEGIN:|177-getter|0|177-preInit
    /**
     * Returns an initiliazed instance of ColorMode component.
     * @return the initialized component instance
     */
    public ChoiceGroup getColorMode() {
        if (ColorMode == null) {//GEN-END:|177-getter|0|177-preInit
            // write pre-init user code here
            ColorMode = new ChoiceGroup(Lang[80], Choice.EXCLUSIVE);//GEN-LINE:|177-getter|1|177-postInit

            ColorMode.append(Lang[81], null);
            ColorMode.append(Lang[82], null);

            ColorMode.setSelectedIndex(DarkMode? 0 : 1, true);
        }//GEN-BEGIN:|177-getter|2|
        return ColorMode;
    }
    //</editor-fold>//GEN-END:|177-getter|2|

    ////////////////////////////////////////////////////////////////////////////
    private CWinMXEdit NewLog()
    {
        CWinMXEdit ChatLog = new CWinMXEdit(null, null, Width, Height, DarkMode);
        ChatLog.setLabel(Lang[50] + ": ");
        ChatLog.setEnableColours(EnableColours);

        return ChatLog;
    }

    private TextField NewWriter()
    {
        TextField Writ = new TextField("", "", 32, TextField.ANY);
        Writ.setMaxSize(400);

        return Writ;
    }

    private Form NewChannelDlg(String ChannelName, int ChatNum)
    {
        if (GoToMain == null)
        {
            Rejoin = new Command(Lang[12], Command.ITEM, 3);
            RejoinAll = new Command(Lang[66], Command.ITEM, 4);
            JoinAll = new Command(Lang[67], Command.ITEM, 5);
            AddFavorites = new Command(Lang[68], Command.ITEM, 6);
            AddAutoJoin = new Command(Lang[69], Command.ITEM, 7);
            Clear = new Command(Lang[48], Command.ITEM, 8);
            GoToMain = new Command(Lang[42], Command.ITEM, 2);
            GoToNet = new Command(Lang[46], Command.ITEM, 1);
            Backs = new Command(Lang[28], Command.ITEM, 9);
            Sends = new Command(Lang[16], Command.STOP, 0);
            Sends2 = new Command(Lang[16], Command.BACK, 0);
            Sends3 = new Command(Lang[16], Command.ITEM, 0);
            GoToUserLists = new Command(Lang[10], Command.ITEM, 1);
            ExitFromRoom = new Command(Lang[14], Command.ITEM, 10);
            ExitWPCC = new Command(Lang[15], Command.ITEM, 11);
        }

        CWinMXEdit ChatLog = NewLog();
        TextField Writ = NewWriter();

        Form Chan = new Form(ChannelName, new Item[] {ChatLog, Writ});
        Chan.addCommand(Sends);
        Chan.addCommand(Sends3);
        Chan.addCommand(GoToUserLists);
        Chan.addCommand(GoToNet);
        Chan.addCommand(GoToMain);
        Chan.addCommand(Rejoin);
        Chan.addCommand(RejoinAll);
        Chan.addCommand(JoinAll);
        Chan.addCommand(AddFavorites);
        Chan.addCommand(AddAutoJoin);
        Chan.addCommand(Clear);
        Chan.addCommand(Backs);
        Chan.addCommand(ExitFromRoom);
        Chan.addCommand(ExitWPCC);
        Chan.addCommand(Sends2);

        Chan.setCommandListener(this);

        return Chan;
    }

    private List NewUserList(int ChatNum)
    {
        if (ListBack == null)
        {
            ListBack = new Command(Lang[28], Command.BACK, 0);
            SendPM = new Command(Lang[47], Command.ITEM, 0);
            Whois = new Command(Lang[75], Command.ITEM, 1);
            Kicks = new Command("/kick", Command.ITEM, 2);
            Ban = new Command("/ban", Command.ITEM, 3);
            KickBan = new Command("/kickban", Command.ITEM, 4);
            Levels = new Command("/level", Command.ITEM, 5);
            UserLevel = new Command("/setuserlevel <user>", Command.ITEM, 6);
        }

        List UsList = new List(Lang[10], Choice.IMPLICIT);
        UsList.addCommand(SendPM);
        UsList.addCommand(Whois);
        UsList.addCommand(Kicks);
        UsList.addCommand(Ban);
        UsList.addCommand(KickBan);
        UsList.addCommand(Levels);
        UsList.addCommand(UserLevel);
        UsList.addCommand(ListBack);

        UsList.setCommandListener(this);

        return UsList;
    }

    public void JoinChannel(String ChannelName, boolean SetCurrent)
    {
        boolean Valid = false;

        String Hostname = GetWinMXChannelName(ChannelName);
        
        if (Hostname.length() > 0)
        {
            String Chan2 = ChannelName;
            Chan2 = Chan2.toLowerCase();

            for (int i = 0; i < MaxChat; i++)
            {
                if (ChannelDlgs[i] != null)
                {
                    String Chan = ChannelDlgs[i].getTitle();
                    Chan = Chan.toLowerCase();

                    if (Chan.compareTo(Chan2) == 0)
                    {
                        switchDisplayable(null, ChannelDlgs[i]);

                        return;
                    }
                }
            }

            Valid = true;
        }

        else if ((ChannelName.length() > 12))
        {
            int pos = ChannelName.lastIndexOf('_');

            if (pos >= 0)
            {
                if (ChannelName.substring(pos).length() == 13)
                {
                    String Chan2 = ChannelName;
                    Chan2 = Chan2.toLowerCase();
                    Chan2 = Chan2.substring(Chan2.length()-12);

                    for (int i = 0; i < MaxChat; i++)
                    {
                        if (ChannelDlgs[i] != null)
                        {
                            String Chan = ChannelDlgs[i].getTitle();
                            Chan = Chan.toLowerCase();
                            Chan = Chan.substring(Chan.length()-12);

                            if (Chan.compareTo(Chan2) == 0)
                            {
                                if (ChatClient[i] != null)
                                {
                                    if (ChatClient[i].GetConnStatus() != 1)
                                    {
                                        ChatClient[i].Rejoin();
                                    }
                                }
                                
                                switchDisplayable(null, ChannelDlgs[i]);
                                getDisplay().setCurrentItem(ChannelDlgs[i].get(1));

                                return;
                            }
                        }
                    }

                    Valid = true;
                }
            }
        }
        
        if (Valid)
        {
            int c = -1;

            for (c = 0; c < MaxChat; c++)
            {
                if (ChannelDlgs[c] == null)
                {
                    break;
                }
            }

            if (!((c < 0) || (c > (MaxChat-1))))
            {
                Vector tmp = new Vector();

                int Len = getAutoLoginsList().size();

                for (int i = 0; i < Len; i++)
                {
                    tmp.addElement(getAutoLoginsList().getString(i));
                }

                ChannelDlgs[c] = NewChannelDlg(ChannelName, c);
                UserLists[c] = NewUserList(c);

                ChatClient[c] = new CWinMXChatClient(this, GetVer(), ChannelName, Hostname, NickName, Line, File, nodeIP, nodePort, ChannelDlgs[c], ((CWinMXEdit)ChannelDlgs[c].get(0)), UserLists[c], Lang, tmp, PersonalMessage, PersState);
                ChatClient[c].SetAutoRejoin(RejoinRetry > 0? true : false);
                ChatClient[c].SetRejoinRetry(RejoinRetry);
                ChatClient[c].SetAutoMessage(AutoMex);
                ChatClient[c].SetShowStatus(ShowStatus);
                ChatClient[c].SetShowVersion(ShowVer);

                getInitDlg().append(ChannelName, Icons[3]);

                if (SetCurrent)
                {
                    switchDisplayable(null, ChannelDlgs[c]);
                    getDisplay().setCurrentItem(ChannelDlgs[c].get(1));
                }
            }

            else
            {
                //
            }
        }
    }

    public void ExitChannel(int ChatNum)
    {
        if (ChatClient[ChatNum] != null)
        {
            for (int i = 3; i < InitDlg.size(); i++)
            {
                String Tmp = InitDlg.getString(i), Tmp2 = ChatClient[ChatNum].GetChannelName();

                int index = Tmp.lastIndexOf('_');

                if (index >= 0)
                {
                    Tmp = Tmp.substring(index+1);
                }

                index = Tmp2.lastIndexOf('_');

                if (index >= 0)
                {
                    Tmp2 = Tmp2.substring(index+1);
                }

                if (Tmp2.equalsIgnoreCase(Tmp))
                {
                    InitDlg.delete(i);

                    break;
                }
            }

            ChatClient[ChatNum].Disconnect(true);

            ChannelDlgs[ChatNum].deleteAll();

            UserLists[ChatNum].deleteAll();

            UserLists[ChatNum] = null;
            ChannelDlgs[ChatNum] = null;
            ChatClient[ChatNum] = null;

            switchDisplayable(null, InitDlg);
        }
    }

    private void SendText(int ChatNum, String Text)
    {
        if (ChatClient[ChatNum] != null)
        {
            if (Text.compareTo("/clr") == 0)
            {
                ((CWinMXEdit)ChannelDlgs[ChatNum].get(0)).clear();
            }

            else
            {
                ChatClient[ChatNum].SendText(Text);
            }

            ((TextField)ChannelDlgs[ChatNum].get(1)).setString("");
        }
    }

    private void UpdateInfo(int ChatNum)
    {
        if (ChatClient[ChatNum] != null)
        {
            ChatClient[ChatNum].UpdateInfo(NickName, Line, File, nodeIP, nodePort, PersonalMessage, PersState);
        }
    }

    private String GetWinMXChannelName(String ChannelName)
    {
        int pos = ChannelName.lastIndexOf('_');

        if (pos >= 0)
        {
            if (ChannelName.substring(pos).indexOf(':') >= 0)
            {
                return ChannelName.substring(pos+1);
            }
        }

        else
        {
            if (ChannelName.indexOf(':') >= 0)
            {
                return ChannelName;
            }
        }

        return "";
    }

    public void SetNickNameInfo(String NickName, int Digits, long nodeIP, int nodePort)
    {
        this.NickName = NickName;
        this.Digits = Digits;
        this.nodeIP = nodeIP;
        this.nodePort = nodePort;
        
        ScreenName.setString(GetNickName());

        for (int i = 0; i < MaxChat; i++)
        {
            UpdateInfo(i);
        }
    }

    public void UpdateName(String OldName, String NewName)
    {
        for (int i = 3; i < InitDlg.size(); i++)
        {
            String Tmp = InitDlg.getString(i), Tmp2 = OldName;

            int index = Tmp.lastIndexOf('_');

            if (index >= 0)
            {
                Tmp = Tmp.substring(index+1);
            }

            index = Tmp2.lastIndexOf('_');

            if (index >= 0)
            {
                Tmp2 = Tmp2.substring(index+1);
            }

            if (Tmp2.equalsIgnoreCase(Tmp))
            {
                InitDlg.set(i, NewName, Icons[3]);

                break;
            }
        }
    }

    public void Apply(boolean Show)
    {
        if (Show)
        {
            Utils.MessageBox(getDisplay(), "WPCC", Lang[76], 2000, AlertType.WARNING);
        }

        boolean ValidNick = false;

        String Nick = getScreenName().getString();
        Nick = Nick.replace(' ', ' ');

        if (Nick.length() >= 6)
        {
            ValidNick = Utils.IsNumeric(Nick.substring(Nick.length()-3, Nick.length()));
        }

        if (ValidNick)
        {
            NickName = Nick + "_" + Digits;
        }

        else
        {
            NickName = Nick + "123";

            getScreenName().setString(NickName);

            NickName += "_" + Digits;
        }

        Line = getConnType().getSelectedIndex();
        File = Utils.GetInt(getSharedFiles().getString());

        if (File > 65535)
        {
            File = 65535;
        }

        getSharedFiles().setString("" + File);

        PersonalMessage = getPersMex().getString();
        PersState = getStatus().getSelectedIndex();

        if (MyLang != getLanguage().getSelectedIndex())
        {
            Utils.MessageBox(getDisplay(), "WPCC", "The language change will take effect the next time you start WPCC.", Alert.FOREVER, AlertType.WARNING);
        }

        MyLang = getLanguage().getSelectedIndex();
        RejoinRetry = Utils.GetInt(getAutoRejoin().getString());

        if (RejoinRetry > 10)
        {
            RejoinRetry = 10;
        }

        getAutoRejoin().setString("" + RejoinRetry);

        AutoMex = getAutoMessage().getString();
        AutoConnect = getAutoConn().getSelectedIndex() == 0? true : false;
        PeerCache = getDefaultPeerCache().getString();
        FirstFetch = getAutoFetch().getSelectedIndex() == 0? true : false;
        ShowStatus = getShowState().getSelectedIndex() == 0? true : false;
        ShowVer = getShowVersion().getSelectedIndex() == 0? true : false;
        EnableColours = getEnableCol().getSelectedIndex() == 0? true : false;
        DarkMode = getColorMode().getSelectedIndex() == 0? true : false;

        SecondaryConnection.SendNickNameInfo(NickName, Line, File);
        SecondaryConnection.SetPeerCache(PeerCache);

        Vector tmp = new Vector();

        int Len = getAutoLoginsList().size();

        for (int i = 0; i < Len; i++)
        {
            tmp.addElement(getAutoLoginsList().getString(i));
        }

        for (int i = 0; i < MaxChat; i++)
        {
            UpdateInfo(i);

            if (ChatClient[i] != null)
            {
                ChatClient[i].SetAutoRejoin(RejoinRetry > 0? true : false);
                ChatClient[i].SetRejoinRetry(RejoinRetry);
                ChatClient[i].SetAutoMessage(AutoMex);
                ChatClient[i].SetShowStatus(ShowStatus);
                ChatClient[i].SetShowVersion(ShowVer);
                ChatClient[i].SetLogin(tmp);

                if (((CWinMXEdit)ChannelDlgs[i].get(0)).getEnableColours() != EnableColours)
                {
                    ((CWinMXEdit)ChannelDlgs[i].get(0)).setEnableColours(EnableColours);
                }

                ((CWinMXEdit)ChannelDlgs[i].get(0)).setColours(DarkMode, true);
            }
        }
                
        SaveSettings();
    }

    private String GetNickName()
    {
        return this.NickName.substring(0, this.NickName.length()-6);
    }

    private void AddToFavorites(String Channel)
    {
        if (Channel.length() < 1)
        {
            return;
        }

        int Len = getFavoritesList().size();

        if (Len < MaxChat)
        {
            for (int i = 0; i < Len; i++)
            {
                if (getFavoritesList().getString(i).equalsIgnoreCase(Channel))
                {
                    return;
                }
            }
        }

        getFavoritesList().append(Channel, null);

        GenericListSaver(getFavoritesList(), FavChannels);
    }

    private void AddToAutoJoin(String Channel)
    {
        if (Channel.length() < 1)
        {
            return;
        }

        int Len = getAutoJoinList().size();

        for (int i = 0; i < Len; i++)
        {
            if (getAutoJoinList().getString(i).equalsIgnoreCase(Channel))
            {
                return;
            }
        }

        getAutoJoinList().append(Channel, null);

        GenericListSaver(getAutoJoinList(), AutoChannels);
    }

    private void AddToAutoLogin(String Channel, String Login)
    {
        if ((Channel.length() < 1) || (Login.length() < 1))
        {
            return;
        }

        int Len = getAutoLoginsList().size();

        for (int i = 0; i < Len; i++)
        {
            if (getAutoLoginsList().getString(i).equalsIgnoreCase(Channel + " " + Login))
            {
                return;
            }
        }

        getAutoLoginsList().append(Channel + " " + Login, null);

        GenericListSaver(getAutoLoginsList(), LoginChannels);
    }
    
    private void RemoveFromFavorites(String Channel)
    {
        GenericRemover(Channel, getFavoritesList(), FavChannels);
    }

    private void RemoveFromAutoJoin(String Channel)
    {
        GenericRemover(Channel, getAutoJoinList(), AutoChannels);
    }

    private void RemoveFromAutoLogin(String Channel)
    {
        GenericRemover(Channel, getAutoLoginsList(), LoginChannels);
    }

    private void GenericRemover(String Channel, List list, RMSManage manage)
    {
        if (Channel.length() < 1)
        {
            return;
        }

        int Len = list.size();

        for (int i = 0; i < Len; i++)
        {
            if (list.getString(i).equalsIgnoreCase(Channel))
            {
                list.delete(i);

                GenericListSaver(list, manage);

                return;
            }
        }
    }

    private void LoadSettings()
    {
        WPCCSettings.openRMS();
        Vector Setts = WPCCSettings.readRMS();
        WPCCSettings.closeRMS();
        
        int Len = Setts.size();

        if (Len > 0)
        {
            Enumeration vEnum = Setts.elements();

            for (int i = 0; i < Len; i++)
            {
                if (i == 0)
                {
                    MyLang = Utils.GetInt((String)vEnum.nextElement());
                }

                else if (i == 1)
                {
                    NickName = (String)vEnum.nextElement();
                }

                else if (i == 2)
                {
                    Line = Utils.GetInt((String)vEnum.nextElement());
                }

                else if (i == 3)
                {
                    File = Utils.GetInt((String)vEnum.nextElement());
                }

                else if (i == 4)
                {
                    PeerCache = (String)vEnum.nextElement();
                }

                else if (i == 5)
                {
                    AutoConnect = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 6)
                {
                    FirstFetch = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 7)
                {
                    PersonalMessage = (String)vEnum.nextElement();
                }

                else if (i == 8)
                {
                    RejoinRetry = Utils.GetInt((String)vEnum.nextElement());
                }

                else if (i == 9)
                {
                    AutoMex = (String)vEnum.nextElement();
                }

                else if (i == 10)
                {
                    AutoConnect = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 11)
                {
                    ShowStatus = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 12)
                {
                    ShowVer = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 13)
                {
                    EnableColours = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }

                else if (i == 14)
                {
                    DarkMode = ((String)vEnum.nextElement()).compareTo("0") == 0? false : true;
                }
            }
        }
    }

    private void SaveSettings()
    {
        Vector Setts = new Vector();
        Setts.addElement("" + MyLang);
        Setts.addElement(NickName);
        Setts.addElement("" + Line);
        Setts.addElement("" + File);
        Setts.addElement(PeerCache);
        Setts.addElement(AutoConnect? "1":"0");
        Setts.addElement(FirstFetch? "1":"0");
        Setts.addElement(PersonalMessage);
        Setts.addElement("" + RejoinRetry);
        Setts.addElement(AutoMex);
        Setts.addElement(AutoConnect? "1":"0");
        Setts.addElement(ShowStatus? "1":"0");
        Setts.addElement(ShowVer? "1":"0");
        Setts.addElement(EnableColours? "1":"0");
        Setts.addElement(DarkMode? "1":"0");

        WPCCSettings.openRMS();
        WPCCSettings.writeRMS(Setts);
        WPCCSettings.closeRMS();
    }

    private void LoadFavorites()
    {
        GenericListLoader(getFavoritesList(), FavChannels);
    }

    private void LoadAutoJoin()
    {
        GenericListLoader(getAutoJoinList(), AutoChannels);
    }

    private void LoadAutoLogins()
    {
        GenericListLoader(getAutoLoginsList(), LoginChannels);
    }

    private void GenericListLoader(List list, RMSManage manage)
    {
        list.deleteAll();

        manage.openRMS();
        Vector Setts = manage.readRMS();
        manage.closeRMS();

        int Len = Setts.size();

        if (Len > 0)
        {
            Enumeration vEnum = Setts.elements();

            for (int i = 0; i < Len; i++)
            {
                list.append((String)vEnum.nextElement(), null);
            }
        }
    }

    private void GenericListSaver(List list, RMSManage manage)
    {
        Vector Setts = new Vector();

        int Len = list.size();

        manage.deleteRMS();

        if (Len > 0)
        {
            for (int i = 0; i < Len; i++)
            {
                Setts.addElement(list.getString(i));
            }

            manage.openRMS();
            manage.writeRMS(Setts);
            manage.closeRMS();
        }
    }

    public String GetVer()
    {
        return Version;
    }

    private class Joiner extends Thread
    {
        public void run()
        {
            try
            {
                int Len = getAutoJoinList().size();

                if (Len > 0)
                {
                    for (int i = 0; i < Len; i++)
                    {
                        JoinChannel(getAutoJoinList().getString(i), false);

                        sleep(100);
                    }
                }
            }

            catch (InterruptedException e)
            {
                System.err.println("Joiner Error: " + e.toString());
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp()
    {
        if (midletPaused)
        {
            resumeMIDlet ();
        }

        else
        {
            initialize();
            startMIDlet();
        }
        
        midletPaused = false;
    }

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay()
    {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet()
    {
        switchDisplayable(null, null);

        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp()
    {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional)
    {
        SecondaryConnection.Disconnect();

        for (int i = 0; i < MaxChat; i++)
        {
            ExitChannel(i);
        }

        if (AutoUpdater != null)
        {
            AutoUpdater.Stop();
        }

        Apply(false);
    }
}
